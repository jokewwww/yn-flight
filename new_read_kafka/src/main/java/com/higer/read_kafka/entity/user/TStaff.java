package com.higer.read_kafka.entity.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:26
 * @Description:
 */
@Entity
@Table(name = "T_STAFF")
@NamedQuery(name = "TStaff.findAll", query = "SELECT a FROM TStaff a")
public class TStaff implements Serializable {
    private String staffId;
    private String staffPwd;
    private String staffName;
    private Integer staffAge;
    private Integer staffGender;
    private String staffType;
    private String staffPhone;
    private String staffCompany;
    private String staffServStation;
    private String staffAirportCode;
    private String staffAptareaCode;
    private String staffGroupId;

    @Id
    @Column(name = "staff_id")
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Basic
    @Column(name = "staff_pwd")
    public String getStaffPwd() {
        return staffPwd;
    }

    public void setStaffPwd(String staffPwd) {
        this.staffPwd = staffPwd;
    }

    @Basic
    @Column(name = "staff_name")
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Basic
    @Column(name = "staff_age")
    public Integer getStaffAge() {
        return staffAge;
    }

    public void setStaffAge(Integer staffAge) {
        this.staffAge = staffAge;
    }

    @Basic
    @Column(name = "staff_gender")
    public Integer getStaffGender() {
        return staffGender;
    }

    public void setStaffGender(Integer staffGender) {
        this.staffGender = staffGender;
    }

    @Basic
    @Column(name = "staff_type")
    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    @Basic
    @Column(name = "staff_phone")
    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    @Basic
    @Column(name = "staff_company")
    public String getStaffCompany() {
        return staffCompany;
    }

    public void setStaffCompany(String staffCompany) {
        this.staffCompany = staffCompany;
    }

    @Basic
    @Column(name = "staff_serv_station")
    public String getStaffServStation() {
        return staffServStation;
    }

    public void setStaffServStation(String staffServStation) {
        this.staffServStation = staffServStation;
    }

    @Basic
    @Column(name = "staff_airport_code")
    public String getStaffAirportCode() {
        return staffAirportCode;
    }

    public void setStaffAirportCode(String staffAirportCode) {
        this.staffAirportCode = staffAirportCode;
    }

    @Basic
    @Column(name = "staff_aptarea_code")
    public String getStaffAptareaCode() {
        return staffAptareaCode;
    }

    public void setStaffAptareaCode(String staffAptareaCode) {
        this.staffAptareaCode = staffAptareaCode;
    }

    @Basic
    @Column(name = "staff_group_id")
    public String getStaffGroupId() {
        return staffGroupId;
    }

    public void setStaffGroupId(String staffGroupId) {
        this.staffGroupId = staffGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TStaff tStaff = (TStaff) o;
        return Objects.equals(staffId, tStaff.staffId) &&
                Objects.equals(staffPwd, tStaff.staffPwd) &&
                Objects.equals(staffName, tStaff.staffName) &&
                Objects.equals(staffAge, tStaff.staffAge) &&
                Objects.equals(staffGender, tStaff.staffGender) &&
                Objects.equals(staffType, tStaff.staffType) &&
                Objects.equals(staffPhone, tStaff.staffPhone) &&
                Objects.equals(staffCompany, tStaff.staffCompany) &&
                Objects.equals(staffServStation, tStaff.staffServStation) &&
                Objects.equals(staffAirportCode, tStaff.staffAirportCode) &&
                Objects.equals(staffAptareaCode, tStaff.staffAptareaCode) &&
                Objects.equals(staffGroupId, tStaff.staffGroupId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(staffId, staffPwd, staffName, staffAge, staffGender, staffType, staffPhone, staffCompany, staffServStation, staffAirportCode, staffAptareaCode, staffGroupId);
    }
}
