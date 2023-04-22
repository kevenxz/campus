package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

/**
 * 私聊信息表
 *
 * @TableName t_campus_message
 */
@TableName(value = "t_campus_message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}