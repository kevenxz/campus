package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.CampusConstant;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.SecurityUtil;
import com.keven.campus.common.utils.redis.RedisConstants;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.entity.User;
import com.keven.campus.entity.vo.BaseUserVo;
import com.keven.campus.entity.vo.UserInfo;
import com.keven.campus.mapper.DiscussPostMapper;
import com.keven.campus.service.FollowService;
import com.keven.campus.service.TagService;
import com.keven.campus.service.UserService;
import com.keven.campus.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description 针对表【t_campus_user(用户表)】的数据库操作Service实现
 * @createDate 2023-03-20 23:50:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService, CampusConstant {


    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TagService tagService;

    @Override
    public R getUserInfo(Long userId) {

        UserInfo userInfo = new UserInfo();
        // 获取标签
        userInfo.setTags(tagService.getUserTags(userId));
        // 获取获赞数量
        String userLikeKey = RedisConstants.getUserLikeKey(userId);
        String likes = stringRedisTemplate.opsForValue().get(userLikeKey);
        userInfo.setLikes(likes != null ? Integer.parseInt(likes) : 0);
        // 获取关注数量
        String followeeKey = RedisConstants.getFolloweeKey(ENTITY_TYPE_USER, userId);
        Long follows = stringRedisTemplate.opsForZSet().size(followeeKey);
        userInfo.setFollows(follows != null ? follows.intValue() : 0);
        // 获取帖子数量
        Long posts = discussPostMapper.selectCount(new LambdaQueryWrapper<DiscussPost>()
                .eq(DiscussPost::getUserId, userId)
                .eq(DiscussPost::getPostStatus, POST_STATUS_NORMAL));
        userInfo.setPosts(posts.intValue());
        // 获取粉丝数量
        String fansKey = RedisConstants.getFansKey(userId, ENTITY_TYPE_USER);
        Long fans = stringRedisTemplate.opsForZSet().size(fansKey);
        userInfo.setFans(fans != null ? fans.intValue() : 0);
        // 获取用户基本信息
        User user = getById(userId);
        BeanUtils.copyProperties(user, userInfo);
        return R.ok().put(userInfo);
    }

    @Override
    public R getBaseInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null) {
            return R.error();
        }
        BaseUserVo baseUserVo = new BaseUserVo();
        BeanUtils.copyProperties(loginUser.getUser(), baseUserVo);
        return R.ok().put(baseUserVo);
    }

}




