package com.zh.bean.flight;

import java.util.Date;

public class TOrderInfo {
    private Long orderId;
    //'数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”',
    private String iud;
    //'最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn'
    private String luts;
    //订单号
    private String orderNo;
    //供油服务模式（五种模式之一）：备注信息：0-机坪加注，1-自提，2-油品配送，3-野外保障加油，4-自助
    private String addoilType;
    //'订单用油/供油机场',
    private String apc3;
    //详细地址（场外保障供油或油品配送时可能有）
    private String address;
    //订单日期（用油日期）YYYYMMDD'
    private Date orderDate;
    //'客户代码
    private String cstno;
    //'客户名称'
    private String cstnm;
    //'必须，计划加油量（单位是：千克）
    private Integer estimatedVolume;
    //计划用油/供油时间YYYY-MM-DD HH:MI:SS',
    private Date estimatedTime;
    //订单性质，3-国内；2-离境；1-国际'
    private Integer oilType;
    //'订单状态：0-尚未执行；1-等待执行；2-已经执行；3-未能执行'
    private Integer orderStatus;
    //'提油人/用油人姓名',
    private String oilUserNm;
    //'提油人/用油人联系电话'
    private String oilUserTel;
    //'运油车号/车牌号（自提油运油车编号/牌号）
    private String tankerNo;
    //'航班号：组成格式是航空公司二码+航班号，例如：CZ8887
    private String flno;
    //航班性质，例如：W/Z，H/Z等',
    private String ttyp;
    // '机号',
    private String regn;
    //'机型3码'
    private String act3;
    //机型代码（完整代码）',
    private String actn;
    //'航空公司二码',
    private String alc2;
    //'航空公司名称',
    private String alcnm;
    //'始发地机场3码',
    private String org3;
    //'始发地机场名称'
    private String orgnm;
    //'目的地机场3码'
    private String des3;
    //'目的地机场名称',
    private String desnm;
    //经停机场3码
    private String via3;
    //'经停机场名称'
    private String vianm;
    //记录创建时间:YYYY-MM-DD HH:MI:SS'
    private Date cdat;
    //记录最后更新时间，一般是指航班业务动态的变更时间，不包含程序进行其他处理而导致更新的时间，与LUTS含义不同，取值可能会不一样。YYYY-MM-DD HH:MI:SS',
    private Date lstu;
    //'指定要获取的订单的状态集合，取值如下：C-创建中，未提交；S-提交；A-审核通过；R-审核未通过；X-取消/作废；未指定时，不限制订单状态。'
    private String otst;
    // 状态 未转换0   已转换 1
    private Integer status;
    //加油开始日期：YYYY-MM-dd HH:mi:ss
    private Date planDateBegin;
    //加油结束日期：YYYY-MM-dd HH:mi:ss
    private Date planDateEnd;
    //关闭说明
    private String closeContent;
    //油品
    private String otyp;
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getIud() {
        return iud;
    }

    public void setIud(String iud) {
        this.iud = iud == null ? null : iud.trim();
    }

    public String getLuts() {
        return luts;
    }

    public void setLuts(String luts) {
        this.luts = luts == null ? null : luts.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getAddoilType() {
        return addoilType;
    }

    public void setAddoilType(String addoilType) {
        this.addoilType = addoilType == null ? null : addoilType.trim();
    }

    public String getApc3() {
        return apc3;
    }

    public void setApc3(String apc3) {
        this.apc3 = apc3 == null ? null : apc3.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate == null ? null : orderDate;
    }

    public String getCstno() {
        return cstno;
    }

    public void setCstno(String cstno) {
        this.cstno = cstno == null ? null : cstno.trim();
    }

    public String getCstnm() {
        return cstnm;
    }

    public void setCstnm(String cstnm) {
        this.cstnm = cstnm == null ? null : cstnm.trim();
    }

    public Integer getEstimatedVolume() {
        return estimatedVolume;
    }

    public void setEstimatedVolume(Integer estimatedVolume) {
        this.estimatedVolume = estimatedVolume;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getOilType() {
        return oilType;
    }

    public void setOilType(Integer oilType) {
        this.oilType = oilType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOilUserNm() {
        return oilUserNm;
    }

    public void setOilUserNm(String oilUserNm) {
        this.oilUserNm = oilUserNm == null ? null : oilUserNm.trim();
    }

    public String getOilUserTel() {
        return oilUserTel;
    }

    public void setOilUserTel(String oilUserTel) {
        this.oilUserTel = oilUserTel == null ? null : oilUserTel.trim();
    }

    public String getTankerNo() {
        return tankerNo;
    }

    public void setTankerNo(String tankerNo) {
        this.tankerNo = tankerNo == null ? null : tankerNo.trim();
    }

    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno == null ? null : flno.trim();
    }

    public String getTtyp() {
        return ttyp;
    }

    public void setTtyp(String ttyp) {
        this.ttyp = ttyp == null ? null : ttyp.trim();
    }

    public String getRegn() {
        return regn;
    }

    public void setRegn(String regn) {
        this.regn = regn == null ? null : regn.trim();
    }

    public String getAct3() {
        return act3;
    }

    public void setAct3(String act3) {
        this.act3 = act3 == null ? null : act3.trim();
    }

    public String getActn() {
        return actn;
    }

    public void setActn(String actn) {
        this.actn = actn == null ? null : actn.trim();
    }

    public String getAlc2() {
        return alc2;
    }

    public void setAlc2(String alc2) {
        this.alc2 = alc2 == null ? null : alc2.trim();
    }

    public String getAlcnm() {
        return alcnm;
    }

    public void setAlcnm(String alcnm) {
        this.alcnm = alcnm == null ? null : alcnm.trim();
    }

    public String getOrg3() {
        return org3;
    }

    public void setOrg3(String org3) {
        this.org3 = org3 == null ? null : org3.trim();
    }

    public String getOrgnm() {
        return orgnm;
    }

    public void setOrgnm(String orgnm) {
        this.orgnm = orgnm == null ? null : orgnm.trim();
    }

    public String getDes3() {
        return des3;
    }

    public void setDes3(String des3) {
        this.des3 = des3 == null ? null : des3.trim();
    }

    public String getDesnm() {
        return desnm;
    }

    public void setDesnm(String desnm) {
        this.desnm = desnm == null ? null : desnm.trim();
    }

    public String getVia3() {
        return via3;
    }

    public void setVia3(String via3) {
        this.via3 = via3 == null ? null : via3.trim();
    }

    public String getVianm() {
        return vianm;
    }

    public void setVianm(String vianm) {
        this.vianm = vianm == null ? null : vianm.trim();
    }

    public Date getCdat() {
        return cdat;
    }

    public void setCdat(Date cdat) {
        this.cdat = cdat;
    }

    public Date getLstu() {
        return lstu;
    }

    public void setLstu(Date lstu) {
        this.lstu = lstu;
    }

    public String getOtst() {
        return otst;
    }

    public void setOtst(String otst) {
        this.otst = otst == null ? null : otst.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status == null ? null : status;
    }

    public Date getPlanDateBegin() {
        return planDateBegin;
    }

    public void setPlanDateBegin(Date planDateBegin) {
        this.planDateBegin = planDateBegin;
    }

    public Date getPlanDateEnd() {
        return planDateEnd;
    }

    public void setPlanDateEnd(Date planDateEnd) {
        this.planDateEnd = planDateEnd;
    }

    public String getCloseContent() {
        return closeContent;
    }

    public void setCloseContent(String closeContent) {
        this.closeContent = closeContent;
    }

    public String getOtyp() {
        return otyp;
    }

    public void setOtyp(String otyp) {
        this.otyp = otyp;
    }
}