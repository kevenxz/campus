package com.keven.campus.config;

import com.keven.campus.common.handler.security.JwtAccessDeniedHandler;
import com.keven.campus.common.handler.security.JwtAuthenticationEntryPoint;
import com.keven.campus.common.handler.security.JwtAuthenticationFilter;
import com.keven.campus.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Keven
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    /**
     * 配置白名单
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/user/login");
    }

    /**
     * security 的核心配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1、使用jwt，首先关闭跨域攻击
        http.csrf().disable();
        // 2、关闭session(因为默认Security会创建session)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 3、请求都需要进行认证之后才能访问，除白名单以外的资源
        http.authorizeRequests().anyRequest().authenticated();
        // 4、关闭缓存 因为每次都是前端本地的请求头token
        http.headers().cacheControl();
        // 5、token过滤器，校验token
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 6、没有登录、没有权限访问资源自定义返回结果
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        // 允许匿名访问
        http.antMatcher("/user/login").anonymous();
        http.antMatcher("/comment/list/**").antMatcher("/user/logout");
        // 除上面的，其他请求不需要认证
    }

    /**
     * 自定义登录逻辑的配置
     * 也就是配置Security中进行认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
