package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName t_campus_user
 */
@TableName(value = "t_campus_user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
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

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private String salt;

    /**
     *
     */
    private String email;

    /**
     * 是否1删除0未删除
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}