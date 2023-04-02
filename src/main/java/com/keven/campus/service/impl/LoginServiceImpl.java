package com.keven.campus.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.keven.campus.common.utils.JwtUtil;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.common.utils.redis.CacheClient;
import com.keven.campus.common.utils.redis.RedisConstants;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.entity.User;
import com.keven.campus.entity.dto.LoginVo;
import com.keven.campus.mapper.UserMapper;
import com.keven.campus.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Keven
 * @version 1.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CacheClient redisCache;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户名 密码登录
     *
     * @param user
     * @return
     */
    @Override
    public R login(User user) {
        log.info("1.开始登录");
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        log.info("2.判断用户是否存在，密码是否正确");
        if (null == userDetails || !passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            return R.error(ResultCode.LoginError);
        }
        if (userDetails.isEnabled()) {
            return R.error(ResultCode.LoginForbid);
        }
        log.info("登录成功,在security对象中存入登陆者信息");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // AuthenticationManger authenticate 进行用户认证


        // 认证通过 ，使用userId生成一个jwt , jwt存入R返回
        log.info("根据登录信息获取token");
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        // 把完整的用户信息 存入redis
        String userId = loginUser.getUser().getId().toString();
        redisCache.set(RedisConstants.LOGIN_USER_KEY + userId, loginUser, 3 * 60 * 60L, TimeUnit.SECONDS);
        String jwt = JwtUtil.createJWT(userId);
        return R.ok("登录成功").put("token", jwt);

    }


    /**
     * 小程序登录登录
     *
     * @param openid openid
     * @return {@link R}
     */
    @Override
    public R miniLogin(String openid) {
        if (StrUtil.isBlank(openid)) {
            return R.error("登录失败，请重试!");
        }
        log.info("1.开始登录");
        LoginUser loginUser;
        loginUser = (LoginUser) userDetailsService.loadUserByUsername(openid);

        if (loginUser.getUser() == null) {
            log.info("没有对应openid小程序用户");
            User user = new User();
            user.setOpenId(openid);
            user.setStatus(0);
            user.setType(0);
            userMapper.insert(user);
        }
        loginUser = (LoginUser) userDetailsService.loadUserByUsername(openid);
        if (loginUser.isEnabled()) {
            return R.error(ResultCode.LoginForbid);
        }
        log.info("微信小程序登录成功,在security对象中存入登陆者信息");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // AuthenticationManger authenticate 进行用户认证


        // 认证通过 ，使用userId生成一个jwt , jwt存入R返回
        log.info("根据登录信息获取token");
        // 把完整的用户信息 存入redis
        String userId = loginUser.getUser().getId().toString();
        redisCache.set(RedisConstants.LOGIN_USER_KEY + userId, loginUser, 3 * 60 * 60L, TimeUnit.SECONDS);
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwt);
        map.put("openid", openid);
        map.put("user", loginUser.getUser());
        return R.ok("登录成功").put(map);
    }

    @Override
    public R logout() {
        // 获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取userId
        Long userId = loginUser.getUser().getId();
        // 删除redis 中的信息
        stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + userId);
        return R.ok();
    }
}
