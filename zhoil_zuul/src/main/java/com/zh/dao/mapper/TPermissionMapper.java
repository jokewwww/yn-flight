package com.zh.dao.mapper;

import com.zh.bean.auto.TPermission;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    TPermission selectByPrimaryKey(Integer id);

    List<TPermission> selectAll();

    int updateByPrimaryKey(TPermission record);
}
