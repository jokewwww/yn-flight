package com.zh.dao.mapper.my;

import com.zh.bean.flight.TPlacecodeType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TPlacecodeTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(TPlacecodeType record);

    int insertSelective(TPlacecodeType record);

    int updateByPrimaryKeySelective(TPlacecodeType record);

    int updateByPrimaryKey(TPlacecodeType record);

    List<TPlacecodeType> getPlacecodeType(TPlacecodeType record);

    TPlacecodeType getPlacecodeTypeOne(String id);
}