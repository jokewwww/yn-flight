package com.zh.dao.mapper.my;

import com.zh.bean.flight.TForeignairportCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TForeignairportCodeMapper {
    int deleteByPrimaryKey(String id);

    int insert(TForeignairportCode record);

    int insertSelective(TForeignairportCode record);

    TForeignairportCode selectByPrimaryKey(String id);

    TForeignairportCode selectByAlcdIcaoCode(String alcdIcaoCode);

    int updateByPrimaryKeySelective(TForeignairportCode record);

    int updateByPrimaryKey(TForeignairportCode record);

    List<TForeignairportCode> selectAll();

    int getMaxId();
}