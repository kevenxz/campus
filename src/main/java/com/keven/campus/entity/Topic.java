package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @TableName t_campus_topic
 */
@TableName(value = "t_campus_topic")
@Data
public class Topic implements Serializable {
    /**
     *
     */
    @NotNull(message = "id不为空", groups = {UpdateGroup.class})
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 话题简介
     */
    private String topicRemark;

    /**
     * 话题名字
     */
    @NotBlank(message = "话题名字不为空", groups = {AddGroup.class})
    private String topicName;

    /**
     * 话题中帖子的数量
     */
    private Long discussCount;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}