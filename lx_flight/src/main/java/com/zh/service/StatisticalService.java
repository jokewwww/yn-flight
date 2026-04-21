package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.login.MyStaff;

import java.util.List;
import java.util.Map;

public interface StatisticalService {

    List<Map<String, Object>> getCountByAir(String taskDate, MyStaff staff);

    List<Map<String, Object>> getCountByAirAndBaoShui(String taskDate, MyStaff staff);

    List<Map<String, Object>> distinguishStatisticalTax(String taskDate, MyStaff staff);

    List<Map<String, Object>> getCountByStaff(String taskDate, MyStaff staff);

    List<Map<String, Object>> getFuelrecpt(MyStaff staff, MyFuelRecpt myFuelRecpt);

    List<Map<String, Object>> getCountByVehi(String taskDate, MyStaff staff);

    List<Map<String, Object>> getOilCustomByAir(String taskDate, MyStaff staff);

    ReturnMsg<List<Map<String, Object>>> getHours(String taskDate, MyStaff staff);

    List<Map<String, Object>> exportGetHours(String dataTime, MyStaff staff);


    List<Map<String, Object>> exportCountByAir(String dataTime, MyStaff staff);

}
