package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 * 飞机号码表实体类
 */
public class TFlightCode implements Serializable {
    /**
     * 飞机号码
     */
    private String arcrRegn;

    /**
     * 开始日期
     */
    private Date arcrStartDate;

    /**
     * 飞机类型
     */
    private String arcrAcname;

    /**
     * 购货方编号
     */
    private String arcrCustomNum;

    /**
     * 结束日期
     */
    private Date arcrEndDate;

    /**
     * t_flight_code
     */
    private static final long serialVersionUID = 1L;

    /**
     * 飞机号码
     * @return arcr_regn 飞机号码
     */
    public String getArcrRegn() {
        return arcrRegn;
    }

    /**
     * 飞机号码
     * @param arcrRegn 飞机号码
     */
    public void setArcrRegn(String arcrRegn) {
        this.arcrRegn = arcrRegn == null ? null : arcrRegn.trim();
    }

    /**
     * 开始日期
     * @return arcr_start_date 开始日期
     */
    public Date getArcrStartDate() {
        return arcrStartDate;
    }

    /**
     * 开始日期
     * @param arcrStartDate 开始日期
     */
    public void setArcrStartDate(Date arcrStartDate) {
        this.arcrStartDate = arcrStartDate;
    }

    /**
     * 飞机类型
     * @return arcr_acname 飞机类型
     */
    public String getArcrAcname() {
        return arcrAcname;
    }

    /**
     * 飞机类型
     * @param arcrAcname 飞机类型
     */
    public void setArcrAcname(String arcrAcname) {
        this.arcrAcname = arcrAcname == null ? null : arcrAcname.trim();
    }

    /**
     * 购货方编号
     * @return arcr_custom_num 购货方编号
     */
    public String getArcrCustomNum() {
        return arcrCustomNum;
    }

    /**
     * 购货方编号
     * @param arcrCustomNum 购货方编号
     */
    public void setArcrCustomNum(String arcrCustomNum) {
        this.arcrCustomNum = arcrCustomNum == null ? null : arcrCustomNum.trim();
    }

    /**
     * 结束日期
     * @return arcr_end_date 结束日期
     */
    public Date getArcrEndDate() {
        return arcrEndDate;
    }

    /**
     * 结束日期
     * @param arcrEndDate 结束日期
     */
    public void setArcrEndDate(Date arcrEndDate) {
        this.arcrEndDate = arcrEndDate;
    }
}