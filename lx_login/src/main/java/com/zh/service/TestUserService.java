package com.zh.service;

import com.zh.bean.auto.TestUser;

import java.util.List;

public interface TestUserService {

    int insert(TestUser record);

    List<TestUser> selectPageTest();
}
