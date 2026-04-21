package com.zh.dao.mapper;

import com.zh.bean.auto.TRoleUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRoleUserMapper {
    int deleteByPrimaryKey(@Param("userid") String userid, @Param("roleid") Integer roleid);

    int insert(TRoleUser record);

    List<TRoleUser> selectAll();
}
