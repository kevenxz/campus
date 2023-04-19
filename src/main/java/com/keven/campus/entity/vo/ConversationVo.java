package com.keven.campus.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回会话实体类
 *
 * @author Keven
 * @version 1.0
 */
@Data
public class ConversationVo {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送的id
     */
    private Long fromId;

    /**
     * 接收的id
     */
    private Long toId;

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

    /**
     * 消息的创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 未读信息的数量
     */
    private Integer unReadCount;
}
