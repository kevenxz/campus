package com.keven.campus.mapper;

import com.keven.campus.entity.UserTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 针对表【t_campus_user_tag(用户-标签表)】的数据库操作Mapper
 * @createDate 2023-01-12 14:26:49
 * @Entity com.keven.campus.entity.UserTag
 */
@Mapper
public interface UserTagMapper extends BaseMapper<UserTag> {
}




