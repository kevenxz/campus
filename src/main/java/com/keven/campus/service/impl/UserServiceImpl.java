package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.entity.User;
import com.keven.campus.service.UserService;
import com.keven.campus.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author 周鑫杰
 * @description 针对表【t_campus_user(用户表)】的数据库操作Service实现
 * @createDate 2023-03-20 23:50:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




