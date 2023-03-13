package com.keven.campus.mapper;

import com.keven.campus.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 针对表【t_campus_tag(用户各性标签表)】的数据库操作Mapper
 * @createDate 2023-01-12 14:26:49
 * @Entity com.keven.campus.entity.Tag
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




