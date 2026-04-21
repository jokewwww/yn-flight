package com.zh.bean.login;

public class LoginUser extends MyStaff {

	private static final long serialVersionUID = 1L;

	private String token;

	private LoginUser loginUserIn;

	/** 登陆时间戳（毫秒） */
	private Long loginTime;

	/** 过期时间戳 */
	private Long expireTime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public LoginUser getLoginUserIn() {
		return loginUserIn;
	}

	public void setLoginUserIn(LoginUser loginUserIn) {
		this.loginUserIn = loginUserIn;
	}

}
