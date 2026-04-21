package com.zh.dao.mapper.my;

import com.zh.bean.login.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MyFuelMapper {

    TFuel findfuel(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<MyFuel> findFuel(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    int setFuel(@Param("fuel") MyFuel fuel, @Param("staffName") String staffName);

    TStaff getStaffById(@Param("taskOpeStaffId") String taskOpeStaffId);

    List<TFuel> findfuels(@Param("taskAirportCode") String taskAirportCode, @Param("taskAptareaCode") String taskAptareaCode);

    TFuel findfuels(@Param("staffAirportCode") String staffAirportCode);

    int updatestaff(MyStaff staff);

    int updateAirprtcodeServ(@Param("staffAptareaCode") String staffAptareaCode, @Param("taskAptareaCode") String taskAptareaCode);

    int updateAirprtcodeStaff(@Param("staffAptareaCode") String staffAptareaCode, @Param("taskAptareaCode") String taskAptareaCode);

    int updateAirprtcodeVehi(@Param("staffAptareaCode") String staffAptareaCode, @Param("taskAptareaCode") String taskAptareaCode);

    MyStaffVehi selectstaffvehi(@Param("sfvhStaffId") String sfvhStaffId);

    //根据化验单编号查询是否存在
    MyFuel getFuel(@Param("fuelTestBillNo") String fuelTestBillNo);

    void updatefuelLogicDelFlg(@Param("fuelTestBillNo") String fuelTestBillNo);


}
