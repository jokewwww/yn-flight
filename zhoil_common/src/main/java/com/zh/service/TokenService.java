package com.zh.service;

import com.zh.bean.login.LoginUser;
import com.zh.bean.login.Token;

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
}
