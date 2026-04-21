package com.higer.oildataexchange.entity.orderInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "T_ORDER_INFO", schema = "flight_login", catalog = "")
public class TOrderInfo {
    private Long orderId;
    private String iud;
    private String luts;
    private String orderNo;
    private String addoilType;
    private String apc3;
    private String address;
    private Date orderDate;
    private String cstno;
    private String cstnm;
    private Integer estimatedVolume;
    private java.util.Date estimatedTime;
    private Integer oilType;
    private Integer orderStatus;
    private String oilUserNm;
    private String oilUserTel;
    private String tankerNo;
    private String flno;
    private String ttyp;
    private String regn;
    private String act3;
    private String actn;
    private String alc2;
    private String alcnm;
    private String org3;
    private String orgnm;
    private String des3;
    private String desnm;
    private String via3;
    private String vianm;
    private java.util.Date cdat;
    private java.util.Date lstu;
    private java.util.Date planDateBegin;
    private java.util.Date planDateEnd;
    private String closeContent;
    private String otyp;
    /**
     * 必须，订单状态变更发生的时间（例如订单实际完成时间）YYYY-MM-DD HH:MI:SS
     */
    private Date sdat;

    @Basic
    @Column(name = "otyp")
    public String getOtyp() {
        return otyp;
    }

    public void setOtyp(String otyp) {
        this.otyp = otyp;
    }

    @Basic
    @Column(name = "plan_date_begin")
    public Date getPlanDateBegin() {
        return planDateBegin;
    }

    public void setPlanDateBegin(Date planDateBegin) {
        this.planDateBegin = planDateBegin;
    }

    @Basic
    @Column(name = "plan_date_end")
    public Date getPlanDateEnd() {
        return planDateEnd;
    }

    public void setPlanDateEnd(Date planDateEnd) {
        this.planDateEnd = planDateEnd;
    }

    @Basic
    @Column(name = "close_content")
    public String getCloseContent() {
        return closeContent;
    }

    public void setCloseContent(String closeContent) {
        this.closeContent = closeContent;
    }

    @Transient
    public Date getSdat() {
        return sdat;
    }

    public void setSdat(Date sdat) {
        this.sdat = sdat;
    }

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "iud")
    public String getIud() {
        return iud;
    }

    public void setIud(String iud) {
        this.iud = iud;
    }

    @Basic
    @Column(name = "luts")
    public String getLuts() {
        return luts;
    }

    public void setLuts(String luts) {
        this.luts = luts;
    }

    @Basic
    @Column(name = "order_no")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "addoil_type")
    public String getAddoilType() {
        return addoilType;
    }

    public void setAddoilType(String addoilType) {
        this.addoilType = addoilType;
    }

    @Basic
    @Column(name = "apc3")
    public String getApc3() {
        return apc3;
    }

    public void setApc3(String apc3) {
        this.apc3 = apc3;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "order_date")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "cstno")
    public String getCstno() {
        return cstno;
    }

    public void setCstno(String cstno) {
        this.cstno = cstno;
    }

    @Basic
    @Column(name = "cstnm")
    public String getCstnm() {
        return cstnm;
    }

    public void setCstnm(String cstnm) {
        this.cstnm = cstnm;
    }

    @Basic
    @Column(name = "estimated_volume")
    public Integer getEstimatedVolume() {
        return estimatedVolume;
    }

    public void setEstimatedVolume(Integer estimatedVolume) {
        this.estimatedVolume = estimatedVolume;
    }

    @Basic
    @Column(name = "estimated_time")
    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Basic
    @Column(name = "oil_type")
    public Integer getOilType() {
        return oilType;
    }

    public void setOilType(Integer oilType) {
        this.oilType = oilType;
    }

    @Basic
    @Column(name = "order_status")
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Basic
    @Column(name = "oil_user_nm")
    public String getOilUserNm() {
        return oilUserNm;
    }

    public void setOilUserNm(String oilUserNm) {
        this.oilUserNm = oilUserNm;
    }

    @Basic
    @Column(name = "oil_user_tel")
    public String getOilUserTel() {
        return oilUserTel;
    }

    public void setOilUserTel(String oilUserTel) {
        this.oilUserTel = oilUserTel;
    }

    @Basic
    @Column(name = "tanker_no")
    public String getTankerNo() {
        return tankerNo;
    }

    public void setTankerNo(String tankerNo) {
        this.tankerNo = tankerNo;
    }

    @Basic
    @Column(name = "flno")
    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    @Basic
    @Column(name = "ttyp")
    public String getTtyp() {
        return ttyp;
    }

    public void setTtyp(String ttyp) {
        this.ttyp = ttyp;
    }

    @Basic
    @Column(name = "regn")
    public String getRegn() {
        return regn;
    }

    public void setRegn(String regn) {
        this.regn = regn;
    }

    @Basic
    @Column(name = "act3")
    public String getAct3() {
        return act3;
    }

    public void setAct3(String act3) {
        this.act3 = act3;
    }

    @Basic
    @Column(name = "actn")
    public String getActn() {
        return actn;
    }

    public void setActn(String actn) {
        this.actn = actn;
    }

    @Basic
    @Column(name = "alc2")
    public String getAlc2() {
        return alc2;
    }

    public void setAlc2(String alc2) {
        this.alc2 = alc2;
    }

    @Basic
    @Column(name = "alcnm")
    public String getAlcnm() {
        return alcnm;
    }

    public void setAlcnm(String alcnm) {
        this.alcnm = alcnm;
    }

    @Basic
    @Column(name = "org3")
    public String getOrg3() {
        return org3;
    }

    public void setOrg3(String org3) {
        this.org3 = org3;
    }

    @Basic
    @Column(name = "orgnm")
    public String getOrgnm() {
        return orgnm;
    }

    public void setOrgnm(String orgnm) {
        this.orgnm = orgnm;
    }

    @Basic
    @Column(name = "des3")
    public String getDes3() {
        return des3;
    }

    public void setDes3(String des3) {
        this.des3 = des3;
    }

    @Basic
    @Column(name = "desnm")
    public String getDesnm() {
        return desnm;
    }

    public void setDesnm(String desnm) {
        this.desnm = desnm;
    }

    @Basic
    @Column(name = "via3")
    public String getVia3() {
        return via3;
    }

    public void setVia3(String via3) {
        this.via3 = via3;
    }

    @Basic
    @Column(name = "vianm")
    public String getVianm() {
        return vianm;
    }

    public void setVianm(String vianm) {
        this.vianm = vianm;
    }

    @Basic
    @Column(name = "cdat")
    public Date getCdat() {
        return cdat;
    }

    public void setCdat(Date cdat) {
        this.cdat = cdat;
    }

    @Basic
    @Column(name = "lstu")
    public Date getLstu() {
        return lstu;
    }

    public void setLstu(Date lstu) {
        this.lstu = lstu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TOrderInfo that = (TOrderInfo) o;
        return orderId == that.orderId &&
                Objects.equals(iud, that.iud) &&
                Objects.equals(luts, that.luts) &&
                Objects.equals(orderNo, that.orderNo) &&
                Objects.equals(addoilType, that.addoilType) &&
                Objects.equals(apc3, that.apc3) &&
                Objects.equals(address, that.address) &&
                Objects.equals(orderDate, that.orderDate) &&
                Objects.equals(cstno, that.cstno) &&
                Objects.equals(cstnm, that.cstnm) &&
                Objects.equals(estimatedVolume, that.estimatedVolume) &&
                Objects.equals(estimatedTime, that.estimatedTime) &&
                Objects.equals(oilType, that.oilType) &&
                Objects.equals(orderStatus, that.orderStatus) &&
                Objects.equals(oilUserNm, that.oilUserNm) &&
                Objects.equals(oilUserTel, that.oilUserTel) &&
                Objects.equals(tankerNo, that.tankerNo) &&
                Objects.equals(flno, that.flno) &&
                Objects.equals(ttyp, that.ttyp) &&
                Objects.equals(regn, that.regn) &&
                Objects.equals(act3, that.act3) &&
                Objects.equals(actn, that.actn) &&
                Objects.equals(alc2, that.alc2) &&
                Objects.equals(alcnm, that.alcnm) &&
                Objects.equals(org3, that.org3) &&
                Objects.equals(orgnm, that.orgnm) &&
                Objects.equals(des3, that.des3) &&
                Objects.equals(desnm, that.desnm) &&
                Objects.equals(via3, that.via3) &&
                Objects.equals(vianm, that.vianm) &&
                Objects.equals(cdat, that.cdat) &&
                Objects.equals(lstu, that.lstu);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId, iud, luts, orderNo, addoilType, apc3, address, orderDate, cstno, cstnm, estimatedVolume, estimatedTime, oilType, orderStatus, oilUserNm, oilUserTel, tankerNo, flno, ttyp, regn, act3, actn, alc2, alcnm, org3, orgnm, des3, desnm, via3, vianm, cdat, lstu);
    }

}
