package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.User;
import com.keven.campus.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"系统的登录"})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "系统登录")
    public R login(@RequestBody User user) {

        // 登录
        return loginService.login(user);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录")
    public R logout() {
        return loginService.logout();
    }
}
