package com.keven.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * (匿名问答表) -- 默认提问为(无名氏)
 *
 * @TableName t_campus_que_ans
 */
@TableName(value = "t_campus_que_ans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueAns implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 提问的id
     */
    @NotNull(message = "提问的id不能为空", groups = AddGroup.class)
    @Positive(message = "提问id不为负数", groups = AddGroup.class)
    private Long questionId;

    /**
     * 回答问题的id
     */
    @NotNull(message = "回答的id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Positive(message = "回答问题的id不为负数", groups = {AddGroup.class, UpdateGroup.class})
    private Long answerId;

    /**
     * 提问的匿名
     */
    private String questionNickname;

    /**
     * 提问的时间
     */
    private Date questionTime;

    /**
     * 回答的时间
     */
    private Date answerTime;

    /**
     * 逻辑删除 (删除之后个人主页不显示) 互动中显示
     */
    private Integer isDeleted;

    /**
     * 提问内容
     */
    @NotBlank(message = "提问内容不能为空", groups = AddGroup.class)
    private String questionContent;

    /**
     * 回答内容
     */
    @NotBlank(message = "回答内容不能为空", groups = UpdateGroup.class)
    private String answerContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}