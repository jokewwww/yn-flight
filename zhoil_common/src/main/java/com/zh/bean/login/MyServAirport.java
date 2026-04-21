package com.zh.bean.login;

import java.io.Serializable;

/**
 * 服务机场表
 * T_SERV_AIRPORT
 */
public class MyServAirport implements Serializable {
    /**
     * 机场代码
     */
    private String svapAirportCode;

    /**
     * 机场区域代码
     */
    private String svapAptareaCode;

    /**
     * 机位号
     */
    private String svapAptplacNo;

    /**
     * 地井编号
     */
    private String svapHydrtPitNo;

    /**
     * T_SERV_AIRPORT
     */
    private static final long serialVersionUID = 1L;

    /**
     * 机场代码
     * @return svap_airport_code 机场代码
     */
    public String getSvapAirportCode() {
        return svapAirportCode;
    }

    /**
     * 机场代码
     * @param svapAirportCode 机场代码
     */
    public void setSvapAirportCode(String svapAirportCode) {
        this.svapAirportCode = svapAirportCode == null ? null : svapAirportCode.trim();
    }

    /**
     * 机场区域代码
     * @return svap_aptarea_code 机场区域代码
     */
    public String getSvapAptareaCode() {
        return svapAptareaCode;
    }

    /**
     * 机场区域代码
     * @param svapAptareaCode 机场区域代码
     */
    public void setSvapAptareaCode(String svapAptareaCode) {
        this.svapAptareaCode = svapAptareaCode == null ? null : svapAptareaCode.trim();
    }

    /**
     * 机位号
     * @return svap_aptplac_no 机位号
     */
    public String getSvapAptplacNo() {
        return svapAptplacNo;
    }

    /**
     * 机位号
     * @param svapAptplacNo 机位号
     */
    public void setSvapAptplacNo(String svapAptplacNo) {
        this.svapAptplacNo = svapAptplacNo == null ? null : svapAptplacNo.trim();
    }

    /**
     * 地井编号
     * @return svap_hydrt_pit_no 地井编号
     */
    public String getSvapHydrtPitNo() {
        return svapHydrtPitNo;
    }

    /**
     * 地井编号
     * @param svapHydrtPitNo 地井编号
     */
    public void setSvapHydrtPitNo(String svapHydrtPitNo) {
        this.svapHydrtPitNo = svapHydrtPitNo == null ? null : svapHydrtPitNo.trim();
    }
}