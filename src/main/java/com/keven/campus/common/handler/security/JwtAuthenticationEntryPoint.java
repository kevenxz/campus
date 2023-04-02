package com.keven.campus.common.handler.security;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.WebUtils;
import com.keven.campus.common.utils.enums.ResultCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当用户未登录和token过期的情况下访问资源
 *
 * @author Keven
 * @version 1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        R error;

        // 访问没有登录的接口异常InsufficientAuthenticationException
        if (authException instanceof InsufficientAuthenticationException) {
            error = R.error(ResultCode.NeedLogin);
        } else if (authException instanceof BadCredentialsException) {
            error = R.error(ResultCode.LoginError.getCode(), authException.getMessage());
        } else {
            error = R.error(500, "认证或授权失败");
        }
        // 访问登录接口 出现错误的异常
        WebUtils.renderString(response, JSONUtil.toJsonStr(error));
    }
}
