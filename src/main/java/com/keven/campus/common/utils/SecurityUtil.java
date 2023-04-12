package com.keven.campus.common.utils;

import com.keven.campus.entity.LoginUser;
import com.keven.campus.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用于获取当前登录用户的基本信息
 *
 * @author Keven
 * @version 1.0
 */
public class SecurityUtil {

    /**
     * 从Security主体信息中获取用户信息
     *
     * @return
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取认证
     *
     * @return {@link Authentication}
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin() {
        Long id = getUserId();
        return id != null && id == 1L;
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUser().getId();
    }
}
