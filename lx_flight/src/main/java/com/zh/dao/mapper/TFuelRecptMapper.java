package com.zh.dao.mapper;

import com.zh.bean.auto.TFuelRecpt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TFuelRecptMapper {
    int deleteByPrimaryKey(String flrcId);

    int insert(TFuelRecpt record);

    TFuelRecpt selectByPrimaryKey(String flrcId);

    List<TFuelRecpt> selectAll();

    int updateByPrimaryKey(TFuelRecpt record);
}
