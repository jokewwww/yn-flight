package com.zh.service.impl;

import com.zh.bean.auto.TestUser;
import com.zh.dao.mapper.TestUserMapper;
import com.zh.dao.mapper.my.MyTestUserMapper;
import com.zh.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TestUserServiceImpl implements TestUserService {

    @Autowired
    private TestUserMapper mapper;

    @Autowired
    private MyTestUserMapper myMapper;

    @Transactional(rollbackFor = java.lang.Exception.class)
    public int insert(TestUser record) {
//		record.setId(7);
        int count = mapper.insert(record);
//		count = mapper.insert(record);
        return count;
    }

    @Override
    public List<TestUser> selectPageTest() {

        TestUser record = new TestUser();
        record.setName("含有汉字的哦 == " + new Date().toLocaleString());

        mapper.insertSelective(record);

        return myMapper.selectPageTest();
    }
}
