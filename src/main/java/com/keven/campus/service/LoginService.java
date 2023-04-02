package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.User;

/**
 * @author Keven
 * @version 1.0
 */
public interface LoginService {
    R login(User user);

    R logout();

    R miniLogin(String openid);

}
