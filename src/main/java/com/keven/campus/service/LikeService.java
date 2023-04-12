package com.keven.campus.service;

import com.keven.campus.common.utils.R;

/**
 * 点赞类
 *
 * @author Keven
 * @version 1.0
 */
public interface LikeService {

    R like(Integer entityType, Long entityId, Long entityUserId);
}
