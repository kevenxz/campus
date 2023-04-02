package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户-标签表
 *
 * @TableName t_campus_user_tag
 */
@TableName(value = "t_campus_user_tag")
@Data
public class UserTag implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long userId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 关联的状态 0 无关联 1 关联
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}