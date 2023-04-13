package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description 针对表【t_campus_follow】的数据库操作Service
 * @createDate 2023-04-04 11:23:31
 */
public interface FollowService extends IService<Follow> {

    /**
     * 关注
     *
     * @param follow
     * @param isFollow
     * @return
     */
    R follow(Follow follow, Boolean isFollow);

    /**
     * 判断是否关注
     *
     * @param entityId
     * @param entityType
     * @return
     */
    R isFollow(Long entityId, Integer entityType);

    /**
     * 获取粉丝列表
     *
     * @param entityType
     * @param entityId
     * @param curPage
     * @param limit
     * @return
     */
    R getFans(Integer entityType, Long entityId, Integer curPage, Integer limit);

    /**
     * 获取关注列表
     *
     * @param entityType
     * @param entityId
     * @param curPage
     * @param limit
     * @return
     */
    R getFollows(Integer entityType, Long entityId, Integer curPage, Integer limit);
}
