package com.zh.bean.flight;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 飞机号码表实体类
 */
public class TFlightCodeTemporary implements Serializable {

    private Integer id;

    /**
     * 飞机号码
     */
    private String arcrRegn;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arcrEndDate;

    /**
     * 指定的航班号
     */
    private String flno;

    /**
     * 购货商名称（业务字段）
     */
    private String arcrName;

    /**
     * 购货商国家（业务字段）
     */
    private String cstmRegion;

    private String newCstmRegion;

    public String getArcrName() {
        return arcrName;
    }

    public void setArcrName(String arcrName) {
        this.arcrName = arcrName;
    }

    public String getCstmRegion() {
        return cstmRegion;
    }

    public void setCstmRegion(String cstmRegion) {
        this.cstmRegion = cstmRegion;
    }

    public String getNewCstmRegion() {
        return newCstmRegion;
    }

    public void setNewCstmRegion(String newCstmRegion) {
        this.newCstmRegion = newCstmRegion;
    }

    /**
     * t_flight_code
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

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