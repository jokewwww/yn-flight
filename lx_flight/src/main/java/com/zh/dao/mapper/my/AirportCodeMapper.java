package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyAirportCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AirportCodeMapper {

    int insertAirportCode(MyAirportCode airportCode);

    List<MyAirportCode> selectAirportCode();

    int updateAirportCode(MyAirportCode airportCode);

    int deleteAirportCode(@Param("apcdIataCode") String apcdIataCode, @Param("apcdIcaoCode") String apcdIcaoCode);

    MyAirportCode selectAirportCodeFind(@Param("apcdIataCode") String apcdIataCode);

    MyAirportCode selectAirportCodes(@Param("apcdCnafAirportCode") String apcdCnafAirportCode);

    List<MyAirportCode> selectAirportCodePropD();

    MyAirportCode selectAirportByName(@Param("apcdAirportName") String apcdAirportName);

    MyAirportCode selectAirportLikeName(@Param("apcdAirportName") String apcdAirportName);
}
