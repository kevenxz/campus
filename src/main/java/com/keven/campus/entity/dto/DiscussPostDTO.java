package com.keven.campus.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.File;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Keven
 * @version 1.0
 */
@Data
public class DiscussPostDTO {

    /**
     * 文件
     */
    private List<File> files;


    /**
     * 发帖用户id
     */
//    @NotBlank(message = "发帖用户id不为空",groups = {AddGroup.class})
    private Long userId;


    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不为空", groups = {AddGroup.class, UpdateGroup.class})
    private String postContent;

    /**
     * 话题id
     */
    @NotNull(message = "话题id不为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long topicId;
    /**
     * 有无 照片/视频 (0 没有 1 有)
     */
    private Integer postPic;
}
