package com.keven.campus.common.handler.security;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.keven.campus.common.utils.JwtUtil;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.WebUtils;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.common.utils.redis.CacheClient;
import com.keven.campus.common.utils.redis.RedisConstants;
import com.keven.campus.entity.LoginUser;
import com.keven.campus.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * token认证
 * 在接口访问前进行过滤
 *
 * @author Keven
 * @version 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private CacheClient cacheClient;

    /**
     * 请求前获取头部信息token
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1、获取token
        String token = request.getHeader("token");
        // 2、判断token是否存在

        if (StrUtil.isBlank(token)) {
            //说明没有携带token，放行
            filterChain.doFilter(request, response);
            return;
        }
        // 3、解析获取userId
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // 因为统一异常在控制层处理
            // 处理异常
            e.printStackTrace();
            // token 超时，token非法
            // 响应告诉前端重新登录
            R error = R.error(ResultCode.NeedLogin);
            WebUtils.renderString(response, JSONUtil.toJsonStr(error));
            return;
        }
        String userId = claims.getSubject();

        // 4、从redis获取信息
        LoginUser loginUser = cacheClient.getCacheObject(RedisConstants.LOGIN_USER_KEY + userId, LoginUser.class);
        if (Objects.isNull(loginUser.getUser())) {
            // 说明登录过期 提示重新登录
            R error = R.error(ResultCode.NeedLogin);
            WebUtils.renderString(response, JSONUtil.toJsonStr(error));
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 过滤器放行
        filterChain.doFilter(request, response);
    }

}
