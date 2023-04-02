package com.keven.campus.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.entity.User;
import com.keven.campus.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Keven
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询用户是否存在 ，或者对应的openid
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).or().eq(User::getOpenId, username);
        User user = userMapper.selectOne(wrapper);
        // 如果没有查询到用户
        if (ObjUtil.isNull(user)) {
            throw new RRException("用户名或者密码错误");
        }
        // TODO 查询对应的权限信息

        // 把数据封装userDetail返回

        return new LoginUser(user);
    }
}
