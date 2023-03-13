package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @description 针对表【t_campus_comment(评论表)】的数据库操作Service
 * @createDate 2023-02-28 16:26:52
 */
public interface CommentService extends IService<Comment> {

    int getCommentCount(Integer entityType, Long entityId);

    R getPageComments(Integer entityType, Long entityId, Integer curPage, Integer limit);

}
