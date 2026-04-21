package com.higer.higerservice.entity.oilpro;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/20 15:33
 * @Description:
 */
@Entity
@Table(name = "a_gps_records")
@NamedQuery(name = "AGpsRecords.findAll", query = "SELECT a FROM AGpsRecords a")
public class AGpsRecords {
    private int id;
    private String hp;
    private Date createTime;
    private String datas;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "hp", nullable = true, length = 64)
    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "datas", nullable = true, length = -1)
    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AGpsRecords that = (AGpsRecords) o;
        return id == that.id &&
                Objects.equals(hp, that.hp) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(datas, that.datas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hp, createTime, datas);
    }
}
