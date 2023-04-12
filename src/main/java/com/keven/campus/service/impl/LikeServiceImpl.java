package com.keven.campus.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.keven.campus.common.utils.CampusConstant;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.SecurityUtil;
import com.keven.campus.common.utils.redis.RedisConstants;
import com.keven.campus.entity.Comment;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.mapper.CommentMapper;
import com.keven.campus.mapper.DiscussPostMapper;
import com.keven.campus.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Keven
 * @version 1.0
 */
@Service
public class LikeServiceImpl implements LikeService, CampusConstant {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 点赞
     *
     * @param entityType   实体类型 （1、帖子 2、评论 ）
     * @param entityId     实体id
     * @param entityUserId 实体的作者
     * @return {@link R}
     */
    @Override
    public R like(Integer entityType, Long entityId, Long entityUserId) {
        // 1、获取当前登录用户
        Long userId = SecurityUtil.getLoginUser().getUser().getId();
        // 2、判断当前用户有没有点赞
        String entityLikeKey = RedisConstants.getEntityLikeKey(entityType, entityId);
        String userLikeKey = RedisConstants.getUserLikeKey(entityUserId);
        Boolean member = stringRedisTemplate.opsForSet().isMember(entityLikeKey, userId.toString());
        int update = 0;
        if (BooleanUtil.isFalse(member)) {
            // 3、 如果未点赞，可以点赞
            // 3.1、数据库点赞数加一
            if (entityType == ENTITY_TYPE_POST) {
                update = discussPostMapper.update(null, new LambdaUpdateWrapper<DiscussPost>().
                        setSql("praise_count = praise_count + 1").eq(DiscussPost::getId, entityId));
            } else if (entityType == ENTITY_TYPE_COMMENT) {
                update = commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                        .setSql("praise_count = praise_count + 1").eq(Comment::getId, entityId));
            }
            // 3.2、保存用户到redis的set集合
            if (update > 0) {
                // 开启事务做两个操作
                stringRedisTemplate.execute(new SessionCallback<Object>() {
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                        operations.multi();
                        // 1、点赞实体的用户id
                        stringRedisTemplate.opsForSet().add(entityLikeKey, userId.toString());
                        // 2、实体作者的赞增加
                        stringRedisTemplate.opsForValue().increment(userLikeKey);
                        return operations.exec();
                    }
                });
            }
        } else {
            // 4、已点赞
            // 4.1、数据库点赞数-1
            if (entityType == ENTITY_TYPE_POST) {
                update = discussPostMapper.update(null, new LambdaUpdateWrapper<DiscussPost>().
                        setSql("praise_count = praise_count - 1").eq(DiscussPost::getId, entityId));
            } else if (entityType == ENTITY_TYPE_COMMENT) {
                update = commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                        .setSql("praise_count = praise_count - 1").eq(Comment::getId, entityId));
            }
            // 4.2、把用户从redis的set集合移除
            if (update > 0) {
                stringRedisTemplate.execute(new SessionCallback<Object>() {
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                        operations.multi();
                        // 1、点赞实体的用户id
                        stringRedisTemplate.opsForSet().remove(entityLikeKey, userId.toString());
                        // 2、实体作者的赞增加
                        stringRedisTemplate.opsForValue().decrement(userLikeKey);
                        return operations.exec();
                    }
                });
            }
        }
        // TODO 触发点赞事件

        return R.ok();
    }
}
