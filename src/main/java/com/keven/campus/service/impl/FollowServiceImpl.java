package com.keven.campus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.*;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.common.utils.redis.RedisConstants;
import com.keven.campus.entity.Comment;
import com.keven.campus.entity.Follow;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.entity.User;
import com.keven.campus.entity.vo.UserVo;
import com.keven.campus.service.FollowService;
import com.keven.campus.mapper.FollowMapper;
import com.keven.campus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description 针对表【t_campus_follow】的数据库操作Service实现
 * @createDate 2023-04-04 11:23:31
 */
@Service
@Slf4j
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {


    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R follow(Follow follow, Boolean isFollow) {
        // 1、获取登录用户id
        Long userId = SecurityUtil.getUserId();
        // 2、判断是否关注
        if (isFollow) {
            //3、关注，新增数据
            follow.setUserId(userId);
            boolean save = save(follow);
            if (!save) {
                return R.error("操作失败");
            }
            //  存到redis
            this.followRedis(userId, follow.getEntityType(), follow.getEntityId());
            //todo 触发关注事件 消息通知
        } else {
            //4、取消关注
            boolean remove = remove(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getEntityType, follow.getEntityType())
                    .eq(Follow::getEntityId, follow.getEntityId())
                    .eq(Follow::getUserId, userId));
            if (!remove) {
                return R.error("操作失败");
            }
            //  删除redis
            this.unFollowRedis(userId, follow.getEntityType(), follow.getEntityId());
        }
        return R.ok();
    }

    /**
     * 关注 在 粉丝键 和关注键 分别加入 userId  和 entityId
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    private void followRedis(Long userId, Integer entityType, Long entityId) {
        stringRedisTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String fansKey = RedisConstants.getFansKey(entityId, entityType);
                String followerKey = RedisConstants.getFolloweeKey(entityType, userId);
                // 开启事务
                operations.multi();

                operations.opsForZSet().add(fansKey, userId.toString(), System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, entityId.toString(), System.currentTimeMillis());
                // 提交事务
                return operations.exec();

            }
        });
    }

    private void unFollowRedis(Long userId, Integer entityType, Long entityId) {
        stringRedisTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String fansKey = RedisConstants.getFansKey(entityId, entityType);
                String followerKey = RedisConstants.getFolloweeKey(entityType, userId);
                // 开启事务
                operations.multi();

                operations.opsForZSet().remove(fansKey, userId.toString());
                operations.opsForZSet().remove(followerKey, entityId.toString());
                // 提交事务
                return operations.exec();

            }
        });
    }

    /**
     * 当前用户是否关注 对应的实体
     *
     * @param entityId
     * @param entityType
     * @return
     */
    @Override
    public R isFollow(Long entityId, Integer entityType) {
        Long userId = SecurityUtil.getUserId();
        if (userId == null) {
            return R.error(ResultCode.NullData);
        }
        String followeeKey = RedisConstants.getFolloweeKey(entityType, userId);
        /* long count = count(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId)
                .eq(Follow::getEntityId, entityId)
                .eq(Follow::getEntityType, entityType));*/
        Double score = stringRedisTemplate.opsForZSet().score(followeeKey, entityId.toString());
        return score != null && score > 0 ? R.ok() : R.error(ResultCode.NullData);
    }

    /**
     * 获取粉丝
     *
     * @param entityId
     * @param curPage
     * @param limit
     * @return
     */
    @Override
    public R getFans(Integer entityType, Long entityId, Integer curPage, Integer limit) {
        String fansKey = RedisConstants.getFansKey(entityId, entityType);
        IPage page = new Query().getPage(CampusUtil.getPageMap(curPage, limit));
        // 分页查询 0 为第一条数据
        // 查询粉丝id

        Set<String> strings = stringRedisTemplate.opsForZSet()
                .reverseRange(fansKey, page.offset(), page.offset() + page.getSize() - 1);
        if (strings == null || strings.size() == 0) {
            return R.error(ResultCode.NullData);
        }
        // 查询粉丝信息
        List<UserVo> userVos = userService.listByIds(strings.stream().map(Long::valueOf).collect(Collectors.toList()))
                .stream().map(user -> BeanUtil.copyProperties(user, UserVo.class))
                .collect(Collectors.toList());
        // 粉丝数量
        Integer fansCount = getFansCount(entityId, entityType);
        return R.ok().put(new PageUtils(userVos, fansCount, (int) page.getSize(), (int) page.getCurrent()));

    }

    @Override
    public R getFollows(Integer entityType, Long entityId, Integer curPage, Integer limit) {
        String followeeKey = RedisConstants.getFolloweeKey(entityType, entityId);
        IPage page = new Query().getPage(CampusUtil.getPageMap(curPage, limit));
        // 分页查询 0 为第一条数据
        // 查询关注的id
        Set<String> strings = stringRedisTemplate.opsForZSet()
                .reverseRange(followeeKey, page.offset(), page.offset() + page.getSize() - 1);
        if (strings == null || strings.size() == 0) {
            return R.error(ResultCode.NullData);
        }

        // 查询关注的信息
        List<UserVo> userVos = userService.listByIds(strings.stream().map(Long::valueOf).collect(Collectors.toList()))
                .stream().map(user -> BeanUtil.copyProperties(user, UserVo.class))
                .collect(Collectors.toList());
        // 关注的数量
        Integer followsCount = getFollowsCount(entityId, entityType);
        return R.ok().put(new PageUtils(userVos, followsCount, (int) page.getSize(), (int) page.getCurrent()));
    }

    /**
     * 获取关注的数量
     *
     * @param entityId
     * @param entityType
     * @return
     */
    private Integer getFollowsCount(Long entityId, Integer entityType) {
        String followeeKey = RedisConstants.getFolloweeKey(entityType, entityId);
        Long aLong = stringRedisTemplate.opsForZSet().zCard(followeeKey);
        if (aLong == null) {
            return 0;
        }
        return Integer.valueOf(aLong.toString());
    }

    /**
     * 获取实体类的粉丝数量
     *
     * @param entityId
     * @param entityType
     * @return
     */
    private Integer getFansCount(Long entityId, Integer entityType) {
        String fansKey = RedisConstants.getFansKey(entityId, entityType);
        Long aLong = stringRedisTemplate.opsForZSet().zCard(fansKey);
        if (aLong == null) {
            return 0;
        }
        return Integer.valueOf(aLong.toString());
    }
}




