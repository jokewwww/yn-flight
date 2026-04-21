package com.zh.dao.mapper;

import com.zh.bean.auto.TRolePermission;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRolePermissionMapper {
    int deleteByPrimaryKey(@Param("roleid") Integer roleid, @Param("permissionid") Integer permissionid);

    int insert(TRolePermission record);

    List<TRolePermission> selectAll();
}
