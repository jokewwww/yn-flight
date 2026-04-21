package com.zh.dao.mapper.my;


import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.flight.MyFuelRecptVo;
import com.zh.vo.FuelTotalExcelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface StatisticalMapper {
    List<MyFuelRecpt> getCountByAirAndBaoShui(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<MyFuelRecpt> getCountByAir(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<MyFuelRecpt> getCountByStaff(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<MyFuelRecpt> getCountByVehi(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<MyFuelRecptVo> getOilCustomByAir(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<MyFuelRecpt> distinguishStatisticalTax(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);

    List<Map<String, Object>> getHours(@Param("date") String taskDate, @Param("staffAirportCode") String staffAirportCode);

    List<FuelTotalExcelVo> getExcelData(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("staffAirportCode") String staffAirportCode);
}
