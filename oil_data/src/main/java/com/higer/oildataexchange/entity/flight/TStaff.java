package com.higer.oildataexchange.entity.flight;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "T_STAFF")
@Entity
@Data
public class TStaff implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "staff_id", nullable = false)
    private String staffId;

    @Column(name = "staff_pwd")
    private String staffPwd;

    /**
     * 员工姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 员工年龄
     */
    @Column(name = "staff_age")
    private Integer staffAge;

    /**
     * 员工性别（0：女，1：男）
     */
    @Column(name = "staff_gender")
    private Integer staffGender = 1;

    /**
     * 岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员，4加油员队长，5加油员副队长）
     */
    @Column(name = "staff_type", nullable = false)
    private String staffType;

    /**
     * 电话
     */
    @Column(name = "staff_phone")
    private String staffPhone;

    /**
     * 子公司
     */
    @Column(name = "staff_company")
    private String staffCompany;

    /**
     * 航空加油站
     */
    @Column(name = "staff_serv_station")
    private String staffServStation;

    /**
     * 所属机场代码
     */
    @Column(name = "staff_airport_code")
    private String staffAirportCode;

    /**
     * 所属机场区域代码
     */
    @Column(name = "staff_aptarea_code")
    private String staffAptareaCode;

    /**
     * 分组ID（uuid）
     */
    @Column(name = "staff_group_id")
    private String staffGroupId;

    /**
     * 排序字段
     */
    @Column(name = "staff_level")
    private Integer staffLevel = 0;


}