package com.zh.dao.mapper;

import com.zh.bean.auto.BusTask;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusTaskMapper {
    int deleteByPrimaryKey(Integer taskNo);

    int insert(BusTask record);

    int insertSelective(BusTask record);

    BusTask selectByPrimaryKey(Integer taskNo);

    int updateByPrimaryKeySelective(BusTask record);

    int updateByPrimaryKey(BusTask record);

}