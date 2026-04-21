package com.zh.dao.mapper.my;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.zh.bean.auto.TRoleUser;

@Mapper
public interface MyTRoleUserMapper {
    List<TRoleUser> selectAllManager();
}
