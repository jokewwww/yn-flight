package com.zh.bean.flight;

import java.util.Date;

public class TCreditInfo {
    /**
     * 客户代码
     */
    private String cstno;
    /**
     * 客户名称
     */
    private String cstnm;
    /**
     * 客户类型（0-实时结算,1-赊销,2-预收款,3-金融方案）
     */
    private String customerType;
    /**
     * 信用评级
     */
    private String cilvl;
    /**
     * 信用状态：0-正常（默认）；1-提示（低风险）；2-警示（高风险）
     */
    private String citst;
    /**
     * 信用的描述信息
     */
    private String cirmk;
    /**
     * 时间
     */
    private Date createDate;

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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType == null ? null : customerType.trim();
    }

    public String getCilvl() {
        return cilvl;
    }

    public void setCilvl(String cilvl) {
        this.cilvl = cilvl == null ? null : cilvl.trim();
    }

    public String getCitst() {
        return citst;
    }

    public void setCitst(String citst) {
        this.citst = citst == null ? null : citst.trim();
    }

    public String getCirmk() {
        return cirmk;
    }

    public void setCirmk(String cirmk) {
        this.cirmk = cirmk == null ? null : cirmk.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}