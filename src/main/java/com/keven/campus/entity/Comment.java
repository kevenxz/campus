package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 评论表
 *
 * @TableName t_campus_comment
 */
@TableName(value = "t_campus_comment")
@Data
public class Comment implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论发起人的id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long userId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}