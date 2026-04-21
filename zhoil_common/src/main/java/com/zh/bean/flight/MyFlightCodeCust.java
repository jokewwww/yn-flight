package com.zh.bean.flight;


import java.io.Serializable;

/**
 *  飞机所属单位
 */
public class MyFlightCodeCust implements Serializable {
    /**
     * 飞机号码
     */
    private String arcrRegn;

    /**
     * 指定航班号
     */
    private String flno;

    /**
     * 购货方编号
     */
    private String arcrCustomNum;

    /**
     * 购货方
     */
    private String flgtAlcname;

    /**
     * 飞机类型
     */
    private String flgtAcname;

    public String getArcrRegn() {
        return arcrRegn;
    }

    public void setArcrRegn(String arcrRegn) {
        this.arcrRegn = arcrRegn;
    }

    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    public String getArcrCustomNum() {
        return arcrCustomNum;
    }

    public void setArcrCustomNum(String arcrCustomNum) {
        this.arcrCustomNum = arcrCustomNum;
    }

    public String getFlgtAlcname() {
        return flgtAlcname;
    }

    public void setFlgtAlcname(String flgtAlcname) {
        this.flgtAlcname = flgtAlcname;
    }

    public String getFlgtAcname() {
        return flgtAcname;
    }

    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname;
    }
}