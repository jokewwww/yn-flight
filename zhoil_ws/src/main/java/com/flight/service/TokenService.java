package com.flight.service;

import com.flight.bean.login.Token;
import com.zh.bean.login.LoginUser;

/**
 * Token管理器
 */
public interface TokenService {

    // 保存token
    Token saveToken(LoginUser loginUser);

    // 刷新token
    void refresh(LoginUser loginUser);

    // 登录(根据token登录)
    LoginUser getLoginUser(String token);

    // 删除token
    boolean deleteToken(String token);

    /**
     * 获取uuid
     */
    String getUUIDFromJWT(String token);
}
