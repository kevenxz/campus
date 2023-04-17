package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description 针对表【t_campus_user(用户表)】的数据库操作Service
 * @createDate 2023-03-20 23:50:03
 */
public interface UserService extends IService<User> {


    /**
     * 获取用户个人页信息信息
     *
     * @param userId 用户id
     * @return {@link R}
     */
    R getUserInfo(Long userId);

    /**
     * 获取登录用户的基本信息
     *
     * @return {@link R}
     */
    R getBaseInfo();
}
