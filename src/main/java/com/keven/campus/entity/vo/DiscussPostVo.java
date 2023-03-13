package com.keven.campus.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import com.keven.campus.entity.File;
import com.keven.campus.entity.Topic;
import com.keven.campus.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * :::: 首页显示 ::::
 * 帖子
 * 话题名称
 * 帖子的点赞数量
 * 帖子的评论数量
 * 帖子的前三条评论 List 包含热评 评论的用户
 *
 * @author Keven
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPostVo {
    /**
     *
     */
    @NotNull(message = "帖子id 不为空", groups = {UpdateGroup.class})
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发帖用户id
     */
//    @NotBlank(message = "发帖用户id不为空",groups = {AddGroup.class})
    private Long userId;

    private String avatarUrl;

    private String nickName;

    private List<File> files;
    private String topicName;


    private Integer likeStatus;
    /**
     * 帖子分数
     */
    private Double postScore;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 帖子状态 0-正常; 1-精华; 2-拉黑
     */
    private Integer postStatus;

    /**
     * 帖子类型 (0-普通; 1-置顶 )
     */
    private Integer postType;

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不为空", groups = {AddGroup.class, UpdateGroup.class})
    private String postContent;

    /**
     * 话题id
     */
    @NotBlank(message = "话题id不为空", groups = {AddGroup.class})
    private Long topicId;

    /**
     * 帖子标题(以后可以拓展)
     */
    private String postTitle;

    /**
     * 点赞数量
     */
    private Integer praiseCount;

    /**
     * 有无 照片/视频 (0 没有 1 有)
     */
    private Integer postPic;

    /**
     * 帖子的创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date createdTime;

    /**
     * 帖子的修改时间(方便拓展)
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date updatedTime;

    /**
     * 围观数
     */
    private Integer openCount;

}
