package com.keven.campus.mapper;

import com.keven.campus.entity.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 周鑫杰
 * @description 针对表【t_campus_school】的数据库操作Mapper
 * @createDate 2023-03-20 11:23:14
 * @Entity com.keven.campus.entity.School
 */
@Mapper
public interface SchoolMapper extends BaseMapper<School> {

}




