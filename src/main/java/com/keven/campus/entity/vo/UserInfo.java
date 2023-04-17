package com.keven.campus.entity.vo;

import com.keven.campus.entity.Tag;
import lombok.Data;

import java.util.List;

/**
 * 用户个人页面信息
 *
 * @author Keven
 * @version 1.0
 */
@Data
public class UserInfo {
    /**
     * 头像
     */
    private String avatarurl;

    /**
     *
     */
    private Integer gender;
    /**
     * 用户个性标签
     */
    private List<Tag> tags;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户粉丝数量
     */
    private Integer fans;
    /**
     * 用户关注数量
     */
    private Integer follows;
    /**
     * 用户帖子数量
     */
    private Integer posts;
    /**
     * 获赞数量
     */
    private Integer likes;
}
