package com.zh.service;

import java.util.List;

import com.zh.bean.auto.TestUser;

public interface TestUserService {

	int insert(TestUser record);
	List<TestUser> selectPageTest();
}
