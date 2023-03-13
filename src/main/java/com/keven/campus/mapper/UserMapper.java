package com.keven.campus.mapper;

import com.keven.campus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 周鑫杰
 * @description 针对表【t_campus_user(用户表)】的数据库操作Mapper
 * @createDate 2023-01-18 17:27:53
 * @Entity com.keven.campus.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




