package com.keven.campus.entity.vo;

import lombok.Data;

/**
 * @author Keven
 * @version 1.0
 */
@Data
public class MessageVo {
    private Long id;

    private Long userId;
    private String nickname;
    private String avatarurl;

    /**
     * 判断是不是当前的用户发的信息，方便前端区分
     */
    private Boolean current;

    /**
     * 会话id
     */
    private String conversationId;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息状态(0 未读 1 已读 2 删除)
     */
    private Integer msgStatus;

    /**
     * 照片视屏 等的id
     */
    private Long fileId;
}
