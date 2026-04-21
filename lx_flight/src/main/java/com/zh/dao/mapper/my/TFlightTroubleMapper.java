package com.zh.dao.mapper.my;


import com.zh.bean.flight.TFlightTrouble;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TFlightTroubleMapper {
    int deleteByPrimaryKey(String id);

    int insert(TFlightTrouble record);

    int insertSelective(TFlightTrouble record);

    TFlightTrouble selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TFlightTrouble record);

    int updateByPrimaryKey(TFlightTrouble record);

    List<TFlightTrouble> selectAll(TFlightTrouble tFlightTrouble);


    List<TFlightTrouble> selectByRegn(TFlightTrouble tFlightTrouble);

}