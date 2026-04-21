package com.zh.dao.mapper.my;

import com.zh.bean.auto.TestUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyTestUserMapper {
    List<TestUser> selectPageTest();
}