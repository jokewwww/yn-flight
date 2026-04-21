package com.zh.dao.mapper;

import com.zh.bean.auto.TestUser;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TestUser record);

    int insertSelective(TestUser record);

    TestUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestUser record);

    int updateByPrimaryKey(TestUser record);
}
