package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.keven.campus.common.validator.group.UpdateGroup;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户表
 *
 * @TableName t_campus_user
 */
@TableName(value = "t_campus_user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId
    @NotNull(message = "用户id不为空", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**
     * 城市
     */
    private Integer city;

    /**
     * 头像
     */
    private String avatarurl;

    /**
     *
     */
    private String province;

    /**
     *
     */
    private Integer gender;

    /**
     *
     */
    private String phone;

    /**
     * 0-普通用户 1-管理员 2-版主
     */
    private Integer type;

    /**
     *
     */
    private Date createdTime;

    /**
     *
     */
    private Date modifyTime;

    /**
     *
     */
    private String openId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}