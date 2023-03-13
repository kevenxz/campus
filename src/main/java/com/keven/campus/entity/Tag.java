package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户各性标签表
 *
 * @TableName t_campus_tag
 */
@TableName(value = "t_campus_tag")
@Data
public class Tag implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签内容(不超过十个字)
     */
    @NotBlank(message = "标签内容不为空")
    @Length(min = 1, max = 10)
    private String tagContent;

    /**
     * 标签所属组的id
     */
    private Long groupId;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}