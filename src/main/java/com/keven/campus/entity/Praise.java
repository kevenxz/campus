package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 点赞表
 *
 * @TableName t_campus_praise
 */
@TableName(value = "t_campus_praise")
@Data
public class Praise implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 点赞的目标(1: 帖子 2:评论 3:用户)
     */
    private Integer entityType;

    /**
     * 具体点赞的目标  帖子id 评论id
     */
    private Long entityId;

    /**
     * 主动点赞的id
     */
    private Long userId;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     *
     */
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * 0 未点 1 点
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}