package com.zh.dao.mapper.my;


import com.zh.bean.flight.TVehiFuel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TVehiFuelMapper {
    int deleteByPrimaryKey(String id);

    int insert(TVehiFuel record);

    int insertSelective(TVehiFuel record);

    TVehiFuel selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TVehiFuel record);

    int updateByPrimaryKey(TVehiFuel record);

    List<TVehiFuel> selectByTaskId(@Param("taskId") String taskId);
}