package com.keven.campus.entity.vo;

import com.keven.campus.entity.Tag;
import com.keven.campus.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ::: 用户信息 :::
 * 用户基本信息
 * 用户的所有个性标签
 *
 * @author Keven
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
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
    private List<Tag> tags;
}
