package com.zh.dao.mapper.my;

import com.zh.bean.flight.TPlacecode;
import com.zh.bean.flight.TPlacecodeKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TPlacecodeMapper {
    int deleteByPrimaryKey(TPlacecodeKey key);

    int insert(TPlacecode record);

    int insertSelective(TPlacecode record);

    int updateByPrimaryKeySelective(TPlacecode record);

    int updateByPrimaryKey(TPlacecode record);

    List<TPlacecode> getPlaceCodeList(TPlacecode placeCode);

    TPlacecode getPlaceCode(TPlacecodeKey key);
}