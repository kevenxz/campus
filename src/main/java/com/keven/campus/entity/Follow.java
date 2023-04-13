package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @TableName t_campus_follow
 */
@TableName(value = "t_campus_follow")
@Data
public class Follow implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注的实体类id
     */
    @ApiModelProperty("被关注实体id")
    @NotNull(message = "关注实体id不能为空", groups = {AddGroup.class})
    private Long entityId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 关注的实体类型（1、帖子 2、评论 3、用户）
     */
    @ApiModelProperty("被关注实体的类型")
    @NotNull(message = "关注实体类型不能为空", groups = {AddGroup.class})
    private Integer entityType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}