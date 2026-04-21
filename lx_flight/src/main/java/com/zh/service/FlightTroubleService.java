package com.zh.service;

import com.zh.bean.flight.TFlightTrouble;

import java.util.List;

public interface FlightTroubleService {

    int deleteByPrimaryKey(String id);

    int insert(TFlightTrouble record);

    TFlightTrouble insertSelective(TFlightTrouble record);

    TFlightTrouble selectByPrimaryKey(String id);

    List<TFlightTrouble> selectAll(TFlightTrouble tFlightTrouble);

    TFlightTrouble updateByPrimaryKeySelective(TFlightTrouble record);

    int updateByPrimaryKey(TFlightTrouble record);
}
