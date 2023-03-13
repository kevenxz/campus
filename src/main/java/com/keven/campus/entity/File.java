package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 文件表
 *
 * @TableName t_campus_file
 */
@TableName(value = "t_campus_file")
@Data
@Builder
public class File implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件名字
     */
    private String fileName;

    /**
     * 文件类型(后缀)
     */
    private String fileCategory;

    /**
     *
     */
    private String fileSize;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 文件其他信息
     */
    private String fileOtherInfo;

    /**
     * 可以关联其他信息 (帖子id ，评论id ，用户id ，会话id)
     */
    private Long businessId;

    /**
     * 实体的类型 --> (1: 帖子  2:评论 3:用户 )
     */
    private Integer entityType;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}