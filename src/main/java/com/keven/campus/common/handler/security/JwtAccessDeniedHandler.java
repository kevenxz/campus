package com.keven.campus.common.handler.security;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.WebUtils;
import com.keven.campus.common.utils.enums.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有权限时返回结果
 *
 * @author Keven
 * @version 1.0
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        R error = R.error(ResultCode.NoOperatorAuth);
        WebUtils.renderString(response, JSONUtil.toJsonStr(error));
    }
}
