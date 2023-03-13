package com.keven.campus.service;

import com.keven.campus.entity.UserTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author
 * @description 针对表【t_campus_user_tag(用户-标签表)】的数据库操作Service
 * @createDate 2023-01-12 14:26:49
 */
public interface UserTagService extends IService<UserTag> {

    void deleteBatchByTagId(List<Long> tagIds);
}
