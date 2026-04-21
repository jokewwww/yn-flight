package com.zh.bean.flight;

import java.io.Serializable;

/**
 *  T_CUSTOM（航空加油客户表）
 */
public class MyCustom implements Serializable {
    /**
     * 购货方编号
     */
    private String cstmNum;

    /**
     * 购货方国家
     */
    private String cstmRegion;

    /**
     * 购货方名称
     */
    private String cstmName;

    /**
     * T_CUSTOM
     */
    private static final long serialVersionUID = 1L;

    /**
     * 购货方编号
     * @return cstm_num 购货方编号
     */
    public String getCstmNum() {
        return cstmNum;
    }

    /**
     * 购货方编号
     * @param cstmNum 购货方编号
     */
    public void setCstmNum(String cstmNum) {
        this.cstmNum = cstmNum == null ? null : cstmNum.trim();
    }

    /**
     * 购货方国家
     * @return cstm_region 购货方国家
     */
    public String getCstmRegion() {
        return cstmRegion;
    }

    /**
     * 购货方国家
     * @param cstmRegion 购货方国家
     */
    public void setCstmRegion(String cstmRegion) {
        this.cstmRegion = cstmRegion == null ? null : cstmRegion.trim();
    }

    /**
     * 购货方名称
     * @return cstm_name 购货方名称
     */
    public String getCstmName() {
        return cstmName;
    }

    /**
     * 购货方名称
     * @param cstmName 购货方名称
     */
    public void setCstmName(String cstmName) {
        this.cstmName = cstmName == null ? null : cstmName.trim();
    }
}