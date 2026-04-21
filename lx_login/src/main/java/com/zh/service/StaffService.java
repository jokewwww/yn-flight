package com.zh.service;

import com.zh.bean.login.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface StaffService {

    /**
     * 通过人员id查询人员信息（供地图使用）
     */
    TStaff getStaffById(String staffId);

    /**
     * 平板端人员登录
     */
    Map<String, Object> staffLogin(String staffId, String staffPwd);

    /**
     * 平板端人员登出(注销)
     */
    void removeStaff(MyStaff staff);

    /**
     * 平板端人员登出(注销)
     */
    void cleanStaff(MyStaff staff);

    /**
     * 获取所有调度员ID（供远程调用使用）
     *
     * @param string2
     * @param string
     */
    List<MyStaff> getStaffList(String staffAirportCode, String staffAptareaCode);

    List<MyStaff> getAllStaffList(String staffAirportCode, String staffAptareaCode);

    /**
     * 人员车辆绑定
     *
     * @return
     */
    Integer staffAndVehi(MyStaffVehi staffVehi);

    /**
     * 根据加油员Id获取人员详细信息如果已经绑定车辆显示车辆车牌号
     */
    MyStaff getStaffInfoAndVehiNo(String staffId);

    /**
     * PC端人员登出(注销)
     */
    void PCremoveStaff(MyStaff staff);

    /**
     * PC端人员登录
     */
    Map<String, Object> PCstaffLogin(String staffId, String staffPwd);

    /**
     * PC端管理系统人员登录
     */
    Map<String, Object> PCManagestaffLogin(String staffId, String staffPwd);

    /**
     * 根据加油员ID获取人员车辆表的信息（供远程调用）
     */
    MyStaffVehi getStaffVehiInfo(String taskOpeStaffId);

    /**
     * 根据加油车编号查出对应的车辆信息（供远程调用）
     */
    MyVehi getVehiInfo(MyStaffVehiTask staffTask);

    /**
     * 获取所有人员ID（供远程调用使用）
     */
    List<MyStaff> getStaffLists(String staffAirportCode, String staffAptareaCode);

    /**
     * 人车解绑
     */
    void deleteStaffVehi(MyStaff staff);

    /**
     * 根据机场代码获取人员的分组名
     */
    ArrayList<Object> getStaffGroupName(MyStaff staff);

    void updateGruop(ArrayList<MyStaffVehiTask> staffvehi, MyStaff staff);

    /**
     * 根据角色类型查出该角色的常用消息
     */
    List<MyMessage> getStaffMessageByType(MyStaff staff);

    /**
     * 根据机场代码查出该角色的常用消息
     */
    List<MyMessage> getStaffMessageByCode(MyStaff staff);

    /**
     * 根据消息ID删除消息
     */
    void deleteStaffMessage(MyMessage message);

    /**
     * 新增消息内容
     */
    void addStaffMessage(MyStaff staff, MyMessage message);

    /**
     * 查询销售信息
     *
     * @param staff
     */
    List<MySalesInfo> getSalesInfo(MyStaff staff);

    /**
     * 删除销售信息
     */
    void deleteSalesInfo(MySalesInfo salesInfo);

    /**
     * 新增销售信息
     */
    void addSalesInfo(MyStaff staff, MySalesInfo salesInfo);

    /**
     * 销售信息详情
     */
    MySalesInfo getSalesInfoById(MySalesInfo salesInfo);

    /**
     * 修改销售信息
     */
    void updateSalesInfo(MySalesInfo salesInfo);

    /**
     * 根据机场代码获取所有的加油员信息（供远程调用）
     */
    List<MyStaff> getStaffInfoList(MyStaff staff);

    void deleteToken(MyStaff staff);

    MyStaffVehi judgeStaffAndVehi(MyVehi tVehiInfo, MyStaff staff);

    /**
     * 功能描述：根据车牌编号是否被绑定
     *
     * @param tVehiInfo 车辆信息
     * @param staff     操作员
     * @return com.zh.bean.login.MyStaffVehi
     * @author zhaojiacan
     * @date 2024/4/24
     */
    MyStaffVehi judgeVehiIsBind(MyVehi tVehiInfo, MyStaff staff);
}
