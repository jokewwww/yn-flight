package com.example.jobschedual.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 14:29
 * @Description:
 */
@Entity
@Table(name = "A_JOBSCHEDUAL", schema = "zhoil_login", catalog = "")
@NamedQuery(name = "AJobschedual.findAll", query = "SELECT a FROM AJobschedual a")
public class AJobschedual {
    private int id;
    private String staffId;
    private String staffName;
    private String type;
    private Integer code;
    private Date date;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "staff_id", nullable = true, length = 128)
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Basic
    @Column(name = "staff_name", nullable = true, length = 64)
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 16)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "code", nullable = true)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AJobschedual that = (AJobschedual) o;
        return id == that.id &&
                Objects.equals(staffId, that.staffId) &&
                Objects.equals(staffName, that.staffName) &&
                Objects.equals(type, that.type) &&
                Objects.equals(code, that.code) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, staffId, staffName, type, code, date);
    }
}
