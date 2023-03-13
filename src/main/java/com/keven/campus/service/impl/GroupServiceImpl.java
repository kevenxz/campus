package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.entity.Group;
import com.keven.campus.service.GroupService;
import com.keven.campus.mapper.GroupMapper;
import org.springframework.stereotype.Service;

/**
 * @author
 * @description 针对表【t_campus_group(标签组(可拓展))】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
        implements GroupService {

}




