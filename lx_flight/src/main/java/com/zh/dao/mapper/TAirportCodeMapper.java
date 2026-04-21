package com.zh.dao.mapper;

import com.zh.bean.flight.TAirportCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TAirportCodeMapper {
    int deleteByPrimaryKey(String apcdIataCode);

    int insert(TAirportCode record);

    TAirportCode selectByPrimaryKey(String apcdIataCode);

    List<TAirportCode> selectAll();

    int updateByPrimaryKey(TAirportCode record);
}
