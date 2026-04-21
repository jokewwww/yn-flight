package com.zh.dao.mapper.my;


import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.MyVehi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MyStaffVehiTaskMapper {


    List<MyStaffVehiTask> findStaffvehi(@Param("taskOpeStaffId") String taskOpeStaffId, @Param("taskAirportCode") String taskAirportCode, @Param("taskAptareaCode") String taskAptareaCode);

    List<MyVehi> findListVehi(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<MyStaff> findStafflists(@Param("staffAptareaCode") String staffAptareaCode, @Param("staffAirportCode") String staffAirportCode);

    MyVehi getVehiInfo(@Param("taskVehiNo") String taskVehiNo, @Param("staffAirportCode") String staffAirportCode);

    MyVehi getVehiInfoAndVehiAirportCode(@Param("taskVehiNo") String taskVehiNo, @Param("vehiAirportCode") String vehiAirportCode);

    int insertstaff(MyStaff staff);

    int updatestaff(MyStaff staff);

    int deletestaff(MyStaff staff);

    int updatevehi(MyVehi vehi);

    int deletevehi(MyVehi vehi);

    List<MyStaffVehiTask> findStafflist(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    MyStaff staffonefind(@Param("staffId") String staffId);

    MyVehi vehionefind(@Param("vehiId") String vehiId);

    int selectstaffname(@Param("staffName") String staffName);

    //根据车牌号查询车辆信息
    MyVehi getVehiInfos(@Param("vehiPlateNo") String vehiPlateNo);

    //根据车牌号查询车辆信息
    MyVehi getVehiInfosByNo(@Param("vehiNo") String vehiNo, @Param("airportCode") String airportCode);

    int addSettingType1(@Param("type1uuid") String type1uuid, @Param("staffId") String staffId, @Param("staffAirportCode") String staffAirportCode, @Param("type1") String type1);

    int addSettingType2(@Param("type2uuid") String type2uuid, @Param("staffId") String staffId, @Param("staffAirportCode") String staffAirportCode, @Param("type2") String type2);

    List<MyVehi> getVehiInfoList(@Param("taskVehiNo") String taskVehiNo, @Param("staffAirportCode") String staffAirportCode);
}
