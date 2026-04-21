package com.example.contrast_entity.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/12 15:42
 * @Description:
 */
@Entity
@Table(name = "T_FLIGHT_CHANGE_LOG", schema = "zhoil_flight", catalog = "")
public class TFlightChangeLog {
    private Integer id;
    private String ffid;
    private String flgtFlno;
    private String jsonData;
    private Date createTime;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ffid", nullable = true, length = 256)
    public String getFfid() {
        return ffid;
    }

    public void setFfid(String ffid) {
        this.ffid = ffid;
    }

    @Basic
    @Column(name = "flgt_flno", nullable = true, length = 256)
    public String getFlgtFlno() {
        return flgtFlno;
    }

    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno;
    }

    @Basic
    @Column(name = "json_data", nullable = true, length = -1)
    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFlightChangeLog that = (TFlightChangeLog) o;
        return id == that.id &&
                Objects.equals(ffid, that.ffid) &&
                Objects.equals(flgtFlno, that.flgtFlno) &&
                Objects.equals(jsonData, that.jsonData) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ffid, flgtFlno, jsonData, createTime);
    }
}
