package com.zh.dao.mapper.my;


import com.zh.bean.login.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffMapper {

    //通过人员id查询人员信息（供地图使用）
    TStaff getStaffById(@Param("staffId") String staffId);

    //通过人员ID去查询出该人员的信息
    MyStaff getStaffInfo(@Param("staffId") String staffId);

    //获取调度员ID
    List<MyStaff> getStaffList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //获取人员列表
    List<MyStaff> getAllStaffList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("staffType") Integer staffType);

    //人员车辆绑定
    int staffAndVehi(@Param("sfvhStaffId") String sfvhStaffId, @Param("sfvhVehiNo") String sfvhVehiNo);

    //根据人员id去查询人员车辆表，是否绑定车辆
    MyStaffVehi getStaffVehiInfo(@Param("staffId") String staffId);

    //根据车辆编号去查询车辆是否被绑定
    List<MyStaffVehi> getVehiBindInfo(@Param("vehiNo") String vehiNo, @Param("airportCode") String airportCode);

    //根据车辆编号去获取车牌号
    String getVehiNo(@Param("sfvhVehiNo") String sfvhVehiNo);

    //获取所有人员ID（供远程调用使用）
    List<MyStaff> getStaffLists(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //登出是删除人车表中信息
    int deleteStaffVehi(@Param("staffId") String staffId);

    //根据机场代码获取人员的分组名
    List<MyStaff> getStaffGroupName(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    int updateGruop(@Param("sfvhStaffId") String sfvhStaffId, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("staffGroupId") String staffGroupId);


    int updateGruopAndLevel(@Param("sfvhStaffId") String sfvhStaffId, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("staffGroupId") String staffGroupId, @Param("staffLevel") Integer staffLevel);


    //根据角色类型查出该角色的常用消息
    List<MyMessage> getStaffMessageByType(@Param("staffType") String staffType, @Param("staffAirportCode") String staffAirportCode);

    //根据机场代码查出该角色的常用消息
    List<MyMessage> getStaffMessageByCode(@Param("staffAirportCode") String staffAirportCode);

    //根据消息ID删除消息
    int deleteStaffMessage(@Param("msgId") String msgId);

    //新增消息内容
    int addStaffMessage(@Param("msgId") String msgId, @Param("staffType") String staffType, @Param("staffAirportCode") String staffAirportCode, @Param("msgContent") String msgContent);

    //查询销售信息
    List<MySalesInfo> getSalesInfo(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //删除销售信息
    int deleteSalesInfo(@Param("salesId") String salesId);

    //新增销售信息
    int addSalesInfo(@Param("salesInfo") MySalesInfo salesInfo);

    //销售信息详情
    MySalesInfo getSalesInfoById(@Param("salesId") String salesId);

    //修改销售信息
    int updateSalesInfo(@Param("salesInfo") MySalesInfo salesInfo);

    //根据机场代码获取所有的加油员信息（供远程调用）
    List<MyStaff> getStaffInfoList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //更新人员车辆绑定
    int updateStaffAndVehi(@Param("sfvhStaffId") String sfvhStaffId, @Param("sfvhVehiNo") String sfvhVehiNo);

    //根据车辆编号查询此车是否已经被绑定
    MyStaffVehi getVehiInfo(@Param("vehiNo") String vehiNo, @Param("airPortCode") String airPortCode);

    Integer findMaxLevel(@Param("staffGroupId") String staffGroupId);
}
