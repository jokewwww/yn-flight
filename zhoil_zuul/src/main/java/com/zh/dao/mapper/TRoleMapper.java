package com.zh.dao.mapper;

import com.zh.bean.auto.TRole;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    TRole selectByPrimaryKey(Integer id);

    List<TRole> selectAll();

    int updateByPrimaryKey(TRole record);
}
