package com.example.jobschedual.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/4/24 19:30
 * @Description:
 */
@Entity
@NamedQuery(name = "TSetting.findAll", query = "SELECT a FROM TSetting a")
@Table(name = "T_SETTING")
public class TSetting {
    private String settOptionId;
    private String settStaffId;
    private String settAirportCode;
    private String settInfo;
    private String settType;
    private Integer settStatus;
    private Integer settRoworcol;
    private Integer settPriority;

    @Id
    @Column(name = "sett_option_id", nullable = false, length = 40)
    public String getSettOptionId() {
        return settOptionId;
    }

    public void setSettOptionId(String settOptionId) {
        this.settOptionId = settOptionId;
    }

    @Basic
    @Column(name = "sett_staff_id", nullable = false, length = 20)
    public String getSettStaffId() {
        return settStaffId;
    }

    public void setSettStaffId(String settStaffId) {
        this.settStaffId = settStaffId;
    }

    @Basic
    @Column(name = "sett_airport_code", nullable = false, length = 4)
    public String getSettAirportCode() {
        return settAirportCode;
    }

    public void setSettAirportCode(String settAirportCode) {
        this.settAirportCode = settAirportCode;
    }

    @Basic
    @Column(name = "sett_info", nullable = true, length = 9999)
    public String getSettInfo() {
        return settInfo;
    }

    public void setSettInfo(String settInfo) {
        this.settInfo = settInfo;
    }

    @Basic
    @Column(name = "sett_type", nullable = false, length = 1)
    public String getSettType() {
        return settType;
    }

    public void setSettType(String settType) {
        this.settType = settType;
    }

    @Basic
    @Column(name = "sett_status", nullable = false)
    public Integer getSettStatus() {
        return settStatus;
    }

    public void setSettStatus(Integer settStatus) {
        this.settStatus = settStatus;
    }

    @Basic
    @Column(name = "sett_roworcol", nullable = false)
    public Integer getSettRoworcol() {
        return settRoworcol;
    }

    public void setSettRoworcol(Integer settRoworcol) {
        this.settRoworcol = settRoworcol;
    }

    @Basic
    @Column(name = "sett_priority", nullable = false)
    public Integer getSettPriority() {
        return settPriority;
    }

    public void setSettPriority(Integer settPriority) {
        this.settPriority = settPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSetting tSetting = (TSetting) o;
        return settStatus == tSetting.settStatus &&
                settRoworcol == tSetting.settRoworcol &&
                settPriority == tSetting.settPriority &&
                Objects.equals(settOptionId, tSetting.settOptionId) &&
                Objects.equals(settStaffId, tSetting.settStaffId) &&
                Objects.equals(settAirportCode, tSetting.settAirportCode) &&
                Objects.equals(settInfo, tSetting.settInfo) &&
                Objects.equals(settType, tSetting.settType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(settOptionId, settStaffId, settAirportCode, settInfo, settType, settStatus, settRoworcol, settPriority);
    }
}
