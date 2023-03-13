package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.common.utils.*;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.entity.Comment;
import com.keven.campus.entity.User;
import com.keven.campus.entity.vo.CommentVo;
import com.keven.campus.mapper.UserMapper;
import com.keven.campus.service.CommentService;
import com.keven.campus.mapper.CommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @description 针对表【t_campus_comment(评论表)】的数据库操作Service实现
 * @createDate 2023-02-28 16:26:52
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 得到评论数
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return int
     */
    @Override
    public int getCommentCount(Integer entityType, Long entityId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getEntityType, entityType)
                .eq(Comment::getEntityId, entityId)
                .eq(Comment::getCommentStatus, CampusConstant.STAUS_EXIST);
        long aLong = baseMapper.selectCount(wrapper);
        return (int) aLong;
    }

    /**
     * 获取页面评论
     * 获取分页评论
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param curPage    当前页面
     * @param limit      页面条数
     * @return {@link R}
     */
    @Override
    public R getPageComments(Integer entityType, Long entityId, Integer curPage, Integer limit) {
        try {
            if (ObjectUtils.isNull(entityId, entityType)) {
                throw new RRException(ResultCode.RequestParamsNull);
            }

            PageUtils commentVos = this.getCommentVos(entityType, entityId, curPage, limit);
            // 找二级评论
            if (entityType == CampusConstant.ENTITY_TYPE_POST) {
                List<CommentVo> list = (List<CommentVo>) commentVos.getList();
                // 包含子评论和回复
                List<Map<String, Object>> collect = list.stream().map(commentVo -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("commentVo", commentVo);
                    // 二级评论
                    PageUtils replies =
                            this.getCommentVos(CampusConstant.ENTITY_TYPE_COMMENT, commentVo.getId(), curPage, limit);
                    map.put("replies", replies);
                    return map;
                }).collect(Collectors.toList());
                commentVos.setList(collect);
            }
            return R.ok().put(commentVos);
        } catch (RRException e) {
            return R.error(e.getResultCode());
        }

    }


    /**
     * 得到评论vos
     * 一级评论降序  二级评论升序
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param curPage
     * @param limit
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    private PageUtils getCommentVos(Integer entityType, Long entityId, Integer curPage, Integer limit) {

        Map<String, Object> pageMap = CampusUtil.getPageMap(curPage, limit);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getEntityType, entityType).eq(Comment::getEntityId, entityId)
                .eq(Comment::getCommentStatus, CampusConstant.STAUS_EXIST);
        // 一级评论 按分数 时间降序
        if (entityType == CampusConstant.ENTITY_TYPE_POST) {
            wrapper.orderByDesc(Comment::getCommentScore)
                    .orderByDesc(Comment::getCreatedTime);
        } else if (entityType == CampusConstant.ENTITY_TYPE_COMMENT) {
            // 二级评论 按时间升序
            wrapper.orderByAsc(Comment::getCreatedTime);
        }
        // 获取评论
        IPage<Comment> commentIPage = baseMapper.selectPage(new Query<Comment>().getPage(pageMap), wrapper);
        // 返回的结果
        PageUtils pageUtils = new PageUtils(commentIPage);
        List<Comment> comments = commentIPage.getRecords();
        // 为空直接返回
        if (comments == null) {
            return pageUtils;
        }
        List<CommentVo> commentVos = comments.stream().map(comment -> {
            // 复制comment 的属性
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            // TODO likeStatus 当前用户的点赞状态,没有登录直接为没点赞
            //commentVo.setLikeStatus();
            // TODO 获取点赞信息 点赞数量
            //commentVo.setPraiseCount();
            // 获取评论的用户的信息 昵称 头像
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getId, comment.getUserId())
                    .select(User::getNickname, User::getAvatarurl, User::getGender));
            commentVo.setNickName(user.getNickname());
            commentVo.setAvatarUrl(user.getAvatarurl());
            // 一级评论要查询评论数
            if (comment.getEntityType() == CampusConstant.ENTITY_TYPE_POST) {
                commentVo.setCommentCount(
                        this.getCommentCount(CampusConstant.ENTITY_TYPE_COMMENT, comment.getId()));
            }
            // 不为空，说明是二级评论
            if (comment.getTargetId() != null) {
                // 设置回复对应人的昵称
                commentVo.setTargetNickName(userMapper.selectOne(new LambdaQueryWrapper<User>()
                        .eq(User::getId, comment.getTargetId())
                        .select(User::getNickname)).getNickname());
            }
            return commentVo;
        }).collect(Collectors.toList());
        pageUtils.setList(commentVos);

        return pageUtils;
    }

}




