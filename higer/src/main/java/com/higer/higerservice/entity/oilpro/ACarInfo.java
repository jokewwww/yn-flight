package com.higer.higerservice.entity.oilpro;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/12/3 08:22
 * @Description:
 */
@Entity
@Table(name = "a_car_info")
@NamedQuery(name = "ACarInfo.findAll", query = "SELECT a FROM ACarInfo a")
public class ACarInfo {
    private int id;
    private String hp;
    private String airport;
    private String carNum;
    private String remark;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "airport", nullable = true, length = 32)
    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Basic
    @Column(name = "car_num", nullable = true, length = 8)
    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = -1)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACarInfo aCarInfo = (ACarInfo) o;
        return id == aCarInfo.id &&
                Objects.equals(hp, aCarInfo.hp) &&
                Objects.equals(airport, aCarInfo.airport) &&
                Objects.equals(carNum, aCarInfo.carNum) &&
                Objects.equals(remark, aCarInfo.remark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, hp, airport, carNum, remark);
    }
}
