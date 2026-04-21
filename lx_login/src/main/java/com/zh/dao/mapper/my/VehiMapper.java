package com.zh.dao.mapper.my;

import com.zh.bean.login.MyVehi;
import com.zh.bean.login.TStaff;
import com.zh.bean.login.TVehiInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehiMapper {

    //以（所属机场，所属区域）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_总数
    Integer getVehiSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场，所属区域，是否可用＝'1：空闲'）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_在线
    Integer getNoLineSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场，所属区域，是否可用＝'2：使用中'）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_保障中
    Integer getAafeguardSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场代码＝输入_机场ID）的条件，取得车辆表
    List<TVehiInfo> getVehiList(@Param("flgtAirportCode") String flgtAirportCode);

    //遍历1中取得的所有记录，以（加油车编号＝1_车辆编号）的条件，取得人员车辆表（DB）中加油员ID字段
    List<TStaff> getStaffVehiList(@Param("vehiNo") String vehiNo);

    //遍历2中取得的所有记录，以（员工ID＝2_加油员ID）的条件，取得员工表（DB）中如下字段，写入输出接
    TStaff getStaffName(@Param("vehiStaffId") String vehiStaffId);

    //添加车辆信息
    int addVehiInfo(@Param("vehi") MyVehi vehi);

    //根据机场代码和车辆号获取车辆油单编号
    String getFuelRecptNo(@Param("vehiAirportCode") String vehiAirportCode, @Param("vehiPlateNo") String vehiPlateNo);

    //根据机场代码和车辆号获取车辆油单编号
    MyVehi getFuelRecptNoAndFuelSno(@Param("vehiAirportCode") String vehiAirportCode, @Param("vehiPlateNo") String vehiPlateNo);

    MyVehi selectoilcode();

    int updateVehiSno(@Param("vehiFuelSno") String vehiFuelSno, @Param("vehiAirportCode") String vehiAirportCode, @Param("vehiPlateNo") String vehiPlateNo);

    int updateVehiInfo(MyVehi myVehi);

    MyVehi findVehiById(@Param("vehiNo") String vehiNo, @Param("airportCode") String airportCode);
}
