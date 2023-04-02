package com.keven.campus.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.WechatUtils;
import com.keven.campus.service.LoginService;
import com.keven.campus.service.impl.LoginServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/miniwx")
@Api(tags = "小程序相关接口")
public class WXController {

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "小程序登录")
    @PostMapping("/login")
    public R get(@RequestBody String code) {
        String co;
        if (code == null) {
            return R.error("登录失败，请重试!");
        }
        JSONObject entries = JSONUtil.parseObj(code);
        co = entries.getStr("code");
        if (StrUtil.isBlank(co)) {
            return R.error("登录失败，请重试!");
        }
        JSONObject openId = wechatUtils.getOpenId(co);
        return loginService.miniLogin(openId.getStr("openid"));
//        return R.ok().put("登录成功").put(openId);
    }
}
