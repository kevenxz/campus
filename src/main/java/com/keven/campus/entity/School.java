package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @TableName t_campus_school
 */
@TableName(value = "t_campus_school")
@Data
public class School implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String province;

    /**
     *
     */
    private String city;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}