package com.keven.campus.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Keven
 * @version 1.0
 */
@Data
public class CommentVo {
    /**
     *
     */
    private Long id;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 当前用户的点赞状态 (没有登录则显示未点赞)
     */
    private Integer likeStatus;

    /**
     * 评论发起人的id
     */
    private Long userId;
    /**
     * 评论的用户名字
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 评论的目标 （1：帖子 2：评论 3 ：用户）
     */
    private Integer entityType;

    /**
     * 具体评论的目标，帖子id ，评论id
     */
    private Long entityId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论状态  1 删除 or 0 未删除
     */
    private Integer commentStatus;

    /**
     * 评论时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date createdTime;

    /**
     * 评论点赞数量
     */
    private Integer praiseCount;

    /**
     * 评论分数
     */
    private Double commentScore;

    /**
     * 评论的照片
     */
    private String fileUrl;

    /**
     * 指评论具体的某个人的评论
     */
    private Long targetId;

    private String targetNickName;
}
