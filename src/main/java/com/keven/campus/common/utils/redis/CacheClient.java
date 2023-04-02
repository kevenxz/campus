package com.keven.campus.common.utils.redis;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.keven.campus.entity.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.keven.campus.common.utils.redis.RedisConstants.CACHE_NULL_TTL;

/**
 * 封装方法
 *
 * @author Keven
 * @version 1.0
 */
@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置 key
     *
     * @param key
     * @param value
     * @param time     时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, timeUnit);
    }

    /**
     * 设置 逻辑过期的key
     *
     * @param key
     * @param value
     * @param time     时间
     * @param timeUnit 时间单位
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit timeUnit) {
        // 设置逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        // 写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key, Class<T> type) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(value, type);
    }

    /**
     * 避免缓存穿透
     * 通过设置key 的value "" 存放在redis中
     *
     * @param keyPrefix  关键前缀
     * @param id         id
     * @param type       类型
     * @param dbFallback db回退
     * @param time       时间
     * @param timeUnit   时间单位
     * @return {@link R}
     */
    public <R, ID> R queryWithPassThrough(String keyPrefix,
                                          ID id, Class<R> type,
                                          Function<ID, R> dbFallback,
                                          Long time,
                                          TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 1、从redis 查询对象信息
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2、判断是否存在
        if (StrUtil.isNotBlank(json)) {
            // 3、存在 返回
            return JSONUtil.toBean(json, type);
        }
        // 判断命中的是否是空值
        if (json != null) { // 前面已经判断了必须不为空才返回
            // 说明是空串
            return null;
        }
        // 4、不存在，根据id查数据库
        R r = dbFallback.apply(id);
        // 5、不存在，返回错误
        if (r == null) {
            // 将空值存入redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.SECONDS);
        }
        // 6、存在，写入redis
        this.set(key, r, time, timeUnit);
        return r;
    }

    // 线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 缓存击穿 解决 -> 逻辑到期
     *
     * @param keyPrefix     key前缀
     * @param lockKeyPrefix 锁前缀
     * @param id            id
     * @param type          类型
     * @param dbFallback    db回退
     * @param time          时间
     * @param timeUnit      时间单位
     * @return {@link R}
     */
    public <R, ID> R queryWithLogicalExpire(String keyPrefix,
                                            String lockKeyPrefix,
                                            ID id, Class<R> type,
                                            Function<ID, R> dbFallback,
                                            Long time,
                                            TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 1、从redis查询信息
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2、判断是否存在
        if (StrUtil.isBlank(json)) {
            // 3、不存在，直接返回(热点数据已经预热了)
            return null;
        }
        // 4、命中，需要先把json反序列为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        // 5、判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 5.1、未过期，直接返回信息
            return r;
        }
        // 5.2、已过期，需要缓存重建
        // 6、缓存重建
        // 6.1、获取互斥锁
        String lockKey = lockKeyPrefix + id;
        boolean isLock = tryLock(lockKey);
        // 6.2、判断是否获取锁成功
        if (isLock) {
            // 6.3、成功--> 再判断缓存是否过期(意义是可能其他线程已经更新了，避免再度更新热点数据)
            String jsonAgain = stringRedisTemplate.opsForValue().get(key);
            RedisData redisDataAgain = JSONUtil.toBean(jsonAgain, RedisData.class);
            LocalDateTime expireTimeAgain = redisDataAgain.getExpireTime();
            R rAgain = JSONUtil.toBean((JSONObject) redisDataAgain.getData(), type);
            if (expireTimeAgain.isAfter(LocalDateTime.now())) {
                // 6.4、未过期，返回缓存数据
                return rAgain;
            }
            // 6.5、过期，开辟子线程，然后主线程返回过期的缓存数据
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库(交给使用者自己写)
                    R apply = dbFallback.apply(id);
                    // 写入redis
                    this.setWithLogicalExpire(key, apply, time, timeUnit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
            return rAgain;
        }
        // 6.6、 返回过期的商品信息
        return r;
    }

    /**
     * 试着锁
     *
     * @param key 关键
     * @return boolean
     */// 锁 -->市级是redis中setnx 不可变，只能删
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 解锁
     *
     * @param key 关键
     */// 删除锁
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
