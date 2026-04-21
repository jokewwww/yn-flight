package com.zh.bean.auto;

import java.io.Serializable;

/**
 * t_staff
 */
public class TStaff implements Serializable {
    /**
     * 员工id
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
     * 岗位（调度员，加油员，管理员）
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
     * t_staff
     */
    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     * @return staff_id 员工id
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 员工id
     * @param staffId 员工id
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    /**
     * 密码
     * @return staff_pwd 密码
     */
    public String getStaffPwd() {
        return staffPwd;
    }

    /**
     * 密码
     * @param staffPwd 密码
     */
    public void setStaffPwd(String staffPwd) {
        this.staffPwd = staffPwd == null ? null : staffPwd.trim();
    }

    /**
     * 员工姓名
     * @return staff_name 员工姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 员工姓名
     * @param staffName 员工姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName == null ? null : staffName.trim();
    }

    /**
     * 员工年龄
     * @return staff_age 员工年龄
     */
    public Integer getStaffAge() {
        return staffAge;
    }

    /**
     * 员工年龄
     * @param staffAge 员工年龄
     */
    public void setStaffAge(Integer staffAge) {
        this.staffAge = staffAge;
    }

    /**
     * 员工性别（0：女，1：男）
     * @return staff_gender 员工性别（0：女，1：男）
     */
    public Integer getStaffGender() {
        return staffGender;
    }

    /**
     * 员工性别（0：女，1：男）
     * @param staffGender 员工性别（0：女，1：男）
     */
    public void setStaffGender(Integer staffGender) {
        this.staffGender = staffGender;
    }

    /**
     * 岗位（调度员，加油员，管理员）
     * @return staff_type 岗位（调度员，加油员，管理员）
     */
    public String getStaffType() {
        return staffType;
    }

    /**
     * 岗位（调度员，加油员，管理员）
     * @param staffType 岗位（调度员，加油员，管理员）
     */
    public void setStaffType(String staffType) {
        this.staffType = staffType == null ? null : staffType.trim();
    }

    /**
     * 电话
     * @return staff_phone 电话
     */
    public String getStaffPhone() {
        return staffPhone;
    }

    /**
     * 电话
     * @param staffPhone 电话
     */
    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone == null ? null : staffPhone.trim();
    }

    /**
     * 子公司
     * @return staff_company 子公司
     */
    public String getStaffCompany() {
        return staffCompany;
    }

    /**
     * 子公司
     * @param staffCompany 子公司
     */
    public void setStaffCompany(String staffCompany) {
        this.staffCompany = staffCompany == null ? null : staffCompany.trim();
    }

    /**
     * 航空加油站
     * @return staff_serv_station 航空加油站
     */
    public String getStaffServStation() {
        return staffServStation;
    }

    /**
     * 航空加油站
     * @param staffServStation 航空加油站
     */
    public void setStaffServStation(String staffServStation) {
        this.staffServStation = staffServStation == null ? null : staffServStation.trim();
    }
}