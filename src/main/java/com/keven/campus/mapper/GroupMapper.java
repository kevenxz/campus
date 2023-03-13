package com.keven.campus.mapper;

import com.keven.campus.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 针对表【t_campus_group(标签组(可拓展))】的数据库操作Mapper
 * @createDate 2023-01-12 14:26:48
 * @Entity com.keven.campus.entity.Group
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}




