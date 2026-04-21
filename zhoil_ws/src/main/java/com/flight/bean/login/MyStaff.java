package com.flight.bean.login;

import com.zh.bean.login.PadInfo;

/**
 * 员工表
 * T_STAFF
 */
public class MyStaff extends BaseBean {
    /**
     * T_STAFF
     */
    private static final long serialVersionUID = 1L;
    /**
     * 员工ID
     */
    private String staffId;
    /**
     * 密码
     */
    private String staffPwd;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 员工年龄
     */
    private Integer staffAge;
    /**
     * 员工性别（0：女，1：男）
     */
    private Integer staffGender;
    /**
     * 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员）
     */
    private String staffType;
    /**
     * 电话
     */
    private String staffPhone;
    /**
     * 子公司
     */
    private String staffCompany;
    /**
     * 航空加油站
     */
    private String staffServStation;
    /**
     * 所属机场代码
     */
    private String staffAirportCode;
    /**
     * 所属机场区域代码
     */
    private String staffAptareaCode;
    /**
     * 业务字段（车牌号）
     */
    private String vehiNo;
    /**
     * 业务字段（所属机场名称）
     */
    private String staffAirportName;
    /**
     * 分组ID
     */
    private String staffGroupId;
    /**
     * pad信息
     */
    private PadInfo padInfo;

    /**
     * 分组ID
     */
    public String getStaffGroupId() {
        return staffGroupId;
    }

    /**
     * 分组ID
     */
    public void setStaffGroupId(String staffGroupId) {
        this.staffGroupId = staffGroupId;
    }

    /**
     * 业务字段（所属机场名称）
     */
    public String getStaffAirportName() {
        return staffAirportName;
    }

    /**
     * 业务字段（所属机场名称）
     */
    public void setStaffAirportName(String staffAirportName) {
        this.staffAirportName = staffAirportName;
    }

    /**
     * 业务字段（车牌号）
     */
    public String getVehiNo() {
        return vehiNo;
    }

    /**
     * 业务字段（车牌号）
     */
    public void setVehiNo(String vehiNo) {
        this.vehiNo = vehiNo;
    }

    /**
     * 员工ID
     *
     * @return staff_id 员工ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 员工ID
     *
     * @param staffId 员工ID
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    /**
     * 密码
     *
     * @return staff_pwd 密码
     */
    public String getStaffPwd() {
        return staffPwd;
    }

    /**
     * 密码
     *
     * @param staffPwd 密码
     */
    public void setStaffPwd(String staffPwd) {
        this.staffPwd = staffPwd == null ? null : staffPwd.trim();
    }

    /**
     * 员工姓名
     *
     * @return staff_name 员工姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 员工姓名
     *
     * @param staffName 员工姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName == null ? null : staffName.trim();
    }

    /**
     * 员工年龄
     *
     * @return staff_age 员工年龄
     */
    public Integer getStaffAge() {
        return staffAge;
    }

    /**
     * 员工年龄
     *
     * @param staffAge 员工年龄
     */
    public void setStaffAge(Integer staffAge) {
        this.staffAge = staffAge;
    }

    /**
     * 员工性别（0：女，1：男）
     *
     * @return staff_gender 员工性别（0：女，1：男）
     */
    public Integer getStaffGender() {
        return staffGender;
    }

    /**
     * 员工性别（0：女，1：男）
     *
     * @param staffGender 员工性别（0：女，1：男）
     */
    public void setStaffGender(Integer staffGender) {
        this.staffGender = staffGender;
    }

    /**
     * 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员）
     *
     * @return staff_type 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员）
     */
    public String getStaffType() {
        return staffType;
    }

    /**
     * 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员）
     *
     * @param staffType 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员）
     */
    public void setStaffType(String staffType) {
        this.staffType = staffType == null ? null : staffType.trim();
    }

    /**
     * 电话
     *
     * @return staff_phone 电话
     */
    public String getStaffPhone() {
        return staffPhone;
    }

    /**
     * 电话
     *
     * @param staffPhone 电话
     */
    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone == null ? null : staffPhone.trim();
    }

    /**
     * 子公司
     *
     * @return staff_company 子公司
     */
    public String getStaffCompany() {
        return staffCompany;
    }

    /**
     * 子公司
     *
     * @param staffCompany 子公司
     */
    public void setStaffCompany(String staffCompany) {
        this.staffCompany = staffCompany == null ? null : staffCompany.trim();
    }

    /**
     * 航空加油站
     *
     * @return staff_serv_station 航空加油站
     */
    public String getStaffServStation() {
        return staffServStation;
    }

    /**
     * 航空加油站
     *
     * @param staffServStation 航空加油站
     */
    public void setStaffServStation(String staffServStation) {
        this.staffServStation = staffServStation == null ? null : staffServStation.trim();
    }

    /**
     * 所属机场代码
     *
     * @return staff_airport_code 所属机场代码
     */
    public String getStaffAirportCode() {
        return staffAirportCode;
    }

    /**
     * 所属机场代码
     *
     * @param staffAirportCode 所属机场代码
     */
    public void setStaffAirportCode(String staffAirportCode) {
        this.staffAirportCode = staffAirportCode == null ? null : staffAirportCode.trim();
    }

    /**
     * 所属机场区域代码
     *
     * @return staff_aptarea_code 所属机场区域代码
     */
    public String getStaffAptareaCode() {
        return staffAptareaCode;
    }

    /**
     * 所属机场区域代码
     *
     * @param staffAptareaCode 所属机场区域代码
     */
    public void setStaffAptareaCode(String staffAptareaCode) {
        this.staffAptareaCode = staffAptareaCode == null ? null : staffAptareaCode.trim();
    }

    public PadInfo getPadInfo() {
        return padInfo;
    }

    public void setPadInfo(PadInfo padInfo) {
        this.padInfo = padInfo;
    }
}