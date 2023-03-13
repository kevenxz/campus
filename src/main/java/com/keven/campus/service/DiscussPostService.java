package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keven.campus.entity.dto.DiscussPostDTO;

import java.util.Map;

/**
 * @author
 * @description 针对表【t_campus_discuss_post(帖子表)】的数据库操作Service
 * @createDate 2023-01-12 14:26:48
 */
public interface DiscussPostService extends IService<DiscussPost> {

    void updateStatus(Long topicId, Long discussPostId, Integer status);

    R queryPage(Long topicId, Map<String, Object> params);

    R getPostDetail(Long discussPostId);

    R add(DiscussPostDTO discussPostDTO);
}
