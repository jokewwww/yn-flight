package com.zh.service;

import com.zh.bean.flight.MyAirportCode;

import java.util.List;
import java.util.Map;

public interface AirportCodeService {

    Integer insertAirportCode(MyAirportCode airportCode);

    List<MyAirportCode> selectAirportCode();

    void updateAirportCode(MyAirportCode airportCode);

    void deleteAirportCode(MyAirportCode airportCode);

    MyAirportCode selectAirportCodeFind(MyAirportCode airportCode);

    Map<String, Map<String, Map<String, Object>>> selectAirportCodeCascade();

    List<MyAirportCode> selectAirportCodePropD();

    List<Map<String, Object>> selectAirportCodeCascadeAndroid();
}
