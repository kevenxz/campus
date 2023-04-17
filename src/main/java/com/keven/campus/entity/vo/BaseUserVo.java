package com.keven.campus.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Keven
 * @version 1.0
 */
@Data
public class BaseUserVo {
    private Long id;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;


    /**
     * 头像
     */
    private String avatarurl;

    /**
     *
     */
    private Integer gender;
    private String openId;
    private String email;
    /**
     * 0-普通用户 1-管理员 2-版主
     */
    private Integer type;
    private String phone;

}
