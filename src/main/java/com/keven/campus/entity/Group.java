package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 标签组(可拓展)
 *
 * @TableName t_campus_group
 */
@TableName(value = "t_campus_group")
@Data
public class Group implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签组的名字
     */
    @NotBlank(message = "标签组的名字不为空")
    private String groupName;

    /**
     * 标签组简介
     */
    private String groupRemark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}