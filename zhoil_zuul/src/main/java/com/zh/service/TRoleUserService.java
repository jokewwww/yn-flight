package com.zh.service;

import java.util.List;

import com.zh.bean.auto.TRoleUser;

public interface TRoleUserService {

	List<TRoleUser> selectAll();
	List<TRoleUser> selectAllManager();
}
