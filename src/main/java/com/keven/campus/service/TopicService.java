package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author
 * @description 针对表【t_campus_topic】的数据库操作Service
 * @createDate 2023-01-12 14:26:49
 */
public interface TopicService extends IService<Topic> {


    R getInfoAndDisPosts(Long topicId, Integer curPage, Integer limit);

}
