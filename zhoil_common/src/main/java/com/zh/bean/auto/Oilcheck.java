package com.zh.bean.auto;

import java.io.Serializable;

/**
 * oildan
 */
public class Oilcheck implements Serializable {
    /**
     * 油单编号
     */
    private String id;

    /**
     * 航班id
     */
    private String hbid;

    /**
     * 航班号
     */
    private String filghtno;

    /**
     * 化验单号
     */
    private String testbillno;

    /**
     * 油品名称
     */
    private String descriptionandgrade;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 实际密度
     */
    private String actualdensity;

    /**
     * 计量表开始读数
     */
    private String meterstart;

    /**
     * 计量表结束读数
     */
    private String meterfinish;

    /**
     * 加油数量
     */
    private String quantity;

    /**
     * 审核状态
     */
    private String spstatus;

    /**
     * 日期
     */
    private String date;

    /**
     * 签名
     */
    private String signphoto;

    /**
     * oildan
     */
    private static final long serialVersionUID = 1L;

    /**
     * 油单编号
     * @return Id 油单编号
     */
    public String getId() {
        return id;
    }

    /**
     * 油单编号
     * @param id 油单编号
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 航班id
     * @return hbid 航班id
     */
    public String getHbid() {
        return hbid;
    }

    /**
     * 航班id
     * @param hbid 航班id
     */
    public void setHbid(String hbid) {
        this.hbid = hbid == null ? null : hbid.trim();
    }

    /**
     * 航班号
     * @return filghtNo 航班号
     */
    public String getFilghtno() {
        return filghtno;
    }

    /**
     * 航班号
     * @param filghtno 航班号
     */
    public void setFilghtno(String filghtno) {
        this.filghtno = filghtno == null ? null : filghtno.trim();
    }

    /**
     * 化验单号
     * @return testBillNo 化验单号
     */
    public String getTestbillno() {
        return testbillno;
    }

    /**
     * 化验单号
     * @param testbillno 化验单号
     */
    public void setTestbillno(String testbillno) {
        this.testbillno = testbillno == null ? null : testbillno.trim();
    }

    /**
     * 油品名称
     * @return descriptionAndGrade 油品名称
     */
    public String getDescriptionandgrade() {
        return descriptionandgrade;
    }

    /**
     * 油品名称
     * @param descriptionandgrade 油品名称
     */
    public void setDescriptionandgrade(String descriptionandgrade) {
        this.descriptionandgrade = descriptionandgrade == null ? null : descriptionandgrade.trim();
    }

    /**
     * 温度
     * @return temperature 温度
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * 温度
     * @param temperature 温度
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature == null ? null : temperature.trim();
    }

    /**
     * 实际密度
     * @return actualDensity 实际密度
     */
    public String getActualdensity() {
        return actualdensity;
    }

    /**
     * 实际密度
     * @param actualdensity 实际密度
     */
    public void setActualdensity(String actualdensity) {
        this.actualdensity = actualdensity == null ? null : actualdensity.trim();
    }

    /**
     * 计量表开始读数
     * @return meterStart 计量表开始读数
     */
    public String getMeterstart() {
        return meterstart;
    }

    /**
     * 计量表开始读数
     * @param meterstart 计量表开始读数
     */
    public void setMeterstart(String meterstart) {
        this.meterstart = meterstart == null ? null : meterstart.trim();
    }

    /**
     * 计量表结束读数
     * @return meterFinish 计量表结束读数
     */
    public String getMeterfinish() {
        return meterfinish;
    }

    /**
     * 计量表结束读数
     * @param meterfinish 计量表结束读数
     */
    public void setMeterfinish(String meterfinish) {
        this.meterfinish = meterfinish == null ? null : meterfinish.trim();
    }

    /**
     * 加油数量
     * @return quantity 加油数量
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * 加油数量
     * @param quantity 加油数量
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    /**
     * 审核状态
     * @return spstatus 审核状态
     */
    public String getSpstatus() {
        return spstatus;
    }

    /**
     * 审核状态
     * @param spstatus 审核状态
     */
    public void setSpstatus(String spstatus) {
        this.spstatus = spstatus == null ? null : spstatus.trim();
    }

    /**
     * 日期
     * @return date 日期
     */
    public String getDate() {
        return date;
    }

    /**
     * 日期
     * @param date 日期
     */
    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    /**
     * 签名
     * @return signPhoto 签名
     */
    public String getSignphoto() {
        return signphoto;
    }

    /**
     * 签名
     * @param signphoto 签名
     */
    public void setSignphoto(String signphoto) {
        this.signphoto = signphoto == null ? null : signphoto.trim();
    }
}