package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.entity.User;
import com.keven.campus.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"用户基本操作"})
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    @ApiOperation(value = "创建用户")
    public R create(@RequestBody User user) {
        userService.save(user);
        return R.ok(Msg.MSG_CREATE.getMsg());
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新用户信息")
    public R update(@RequestBody User user) {

        userService.updateById(user);
        return R.ok(Msg.MSG_UPDATE.getMsg());
    }

    @GetMapping("/info/{userId}")
    @ApiOperation(value = "获取用户信息")
    public R info(@PathVariable("userId") Long userId) {

        User user = userService.getInfo(userId);
        return R.ok().put(user);
    }

    @PostMapping("/delete/{userId}")
    @ApiOperation(value = "注销用户")
    public R delete(@PathVariable Long userId) {
        if (userId == null || userId < 0) {
            return R.error("请输入正确的用户id");
        }
        userService.removeById(userId);
        return R.ok(Msg.MSG_DELETE.getMsg());
    }
}
