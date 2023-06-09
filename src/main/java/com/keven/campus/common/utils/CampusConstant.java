package com.keven.campus.common.utils;

/**
 * 常量接口，设置 成功 失败 重复 激活的状态
 *
 * @author Keven
 * @version 1.0
 */
public interface CampusConstant {


    /**
     * 有图片
     */
    int HAS_PIC = 1;

    /**
     * 帖子状态 0-正常; 1-精华; 2-拉黑
     */
    int POST_STATUS_NORMAL = 0;
    int POST_STATUS_PRIME = 1;
    int POST_STATUS_BLOCK = 2;
    /**
     * 消息状态(0 未读 1 已读 2 删除)
     */
    int MESSAGE_STATUS_UNREAD = 0;
    int MESSAGE_STATUS_READ = 1;
    int MESSAGE_STATUS_DELETE = 2;
    /**
     * 删除状态
     */
    int STAUS_DELETE = 1;
    int STAUS_EXIST = 0;
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    /**
     * 记住状态下的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;
    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;
    /**
     * 实体类型：用户
     */
    int ENTITY_TYPE_USER = 3;
    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "comment";
    /**
     * 主题：点赞
     */
    String TOPIC_LIKE = "like";
    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "follow";
    /**
     * 主题：发帖
     */
    String TOPIC_PUBLISH = "publish";
    /**
     * 主题：删帖
     */
    String TOPIC_DELETE = "delete";
    /**
     * 系统用户的id
     */
    long SYSTEM_USER_ID = 1;
    /**
     * 权限：普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限：管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限：版主
     */
    String AUTHORITY_MODERATOR = "moderator";

}
