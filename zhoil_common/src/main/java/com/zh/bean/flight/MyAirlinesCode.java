package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 航空公司代码表
 * T_AIRLINES_CODE
 */
public class MyAirlinesCode implements Serializable {
    /**
     * 航空公司二字码
     */
    private String alcdIcaoCode;

    /**
     * 航空公司名称
     */
    private String alcdArlnName;

    /**
     * 航空公司名称
     */
    private String alcdArlnNameS;

    /*
        内航 外航
     */
    private Integer alcdArlnNw;

    /**
     *所属客户的代码
     */
    private String cstmNum;
    public Integer getAlcdArlnNw() {
        return alcdArlnNw;
    }

    public void setAlcdArlnNw(Integer alcdArlnNw) {
        this.alcdArlnNw = alcdArlnNw;
    }

    /**
     * T_AIRLINES_CODE
     */
    private static final long serialVersionUID = 1L;

    /**
     * 航空公司二字码
     * @return alcd_icao_code 航空公司二字码
     */
    public String getAlcdIcaoCode() {
        return alcdIcaoCode;
    }

    /**
     * 航空公司二字码
     * @param alcdIcaoCode 航空公司二字码
     */
    public void setAlcdIcaoCode(String alcdIcaoCode) {
        this.alcdIcaoCode = alcdIcaoCode == null ? null : alcdIcaoCode.trim();
    }

    /**
     * 航空公司名称
     * @return alcd_arln_name 航空公司名称
     */
    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    /**
     * 航空公司名称
     * @param alcdArlnName 航空公司名称
     */
    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName == null ? null : alcdArlnName.trim();
    }

    /**
     * 航空公司名称
     * @return alcd_arln_name_s 航空公司名称
     */
    public String getAlcdArlnNameS() {
        return alcdArlnNameS;
    }

    /**
     * 航空公司名称
     * @param alcdArlnNameS 航空公司名称
     */
    public void setAlcdArlnNameS(String alcdArlnNameS) {
        this.alcdArlnNameS = alcdArlnNameS == null ? null : alcdArlnNameS.trim();
    }

    public String getCstmNum() {
        return cstmNum;
    }

    public void setCstmNum(String cstmNum) {
        this.cstmNum = cstmNum;
    }
}