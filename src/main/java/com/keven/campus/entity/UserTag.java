package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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