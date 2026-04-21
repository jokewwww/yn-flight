package com.zh.service;

import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:25
 * @Description:
 */
public interface TFuelNoService {

    String generateNo(MyStaff staff, String start, String end, Integer type);

    ResponseObject<Object> selectAll(MyStaff staff, InTFuelNo tFuelNo);

    TFuelNo getNewNo(Integer type, String aptareaCode, Integer status);

    String updateFule(String oldNO, Integer type, String aptareaCode);

    List<TFuelNo> getFuelNoByType(MyStaff staff);

    Integer updateFuleStatus(String nos);

    Integer updatefuleNoEnd(String no);

    String test();

    Map<Integer, String> getMaxFuelNo(MyStaff staff);

    ReturnMsg<String> generateFuelNo(MyStaff staff, InTFuelNo tFuelNo);

    ReturnMsg<String> recyclingNo(MyTask task);

    TFuelNo getNewNoRemark(String remark, Integer type, String aptareaCode, Integer status);

    List<TFuelNo> getFuelNoByTypeRemark(String remark, MyStaff staff);

    List<TFuelNo> padGetFuelNoByType(TFuelDistribution tFuelDistribution);

    Integer recyclingFlueNo(MyFuelRecpt fuelRecpt);

    Integer recyclingFlueNoInsert(MyFuelRecpt fuelRecpt, MyStaff staff);

    List<Map<String, Object>> padGetNo(TFuelNo tFuelNo, MyStaff staff);
}
