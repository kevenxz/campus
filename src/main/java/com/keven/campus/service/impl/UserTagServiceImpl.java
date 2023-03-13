package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.entity.UserTag;
import com.keven.campus.service.UserTagService;
import com.keven.campus.mapper.UserTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @description 针对表【t_campus_user_tag(用户-标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:49
 */
@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag>
        implements UserTagService {
    @Override
    public void deleteBatchByTagId(List<Long> tagIds) {
        baseMapper.delete(new LambdaQueryWrapper<UserTag>().in(UserTag::getTagId, tagIds));
    }
}




