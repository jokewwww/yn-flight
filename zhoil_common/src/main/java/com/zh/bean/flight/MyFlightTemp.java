package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 * T_FLIGHT_TEMP（预创建航班表）
 */
public class MyFlightTemp implements Serializable {
    /**
     * 航班号
     */
    private String flgtFlno;

    /**
     * 航班日期
     */
    private Date flgtFlop;
    
    /**
     * 计划起飞时间
     */
    private String flgtDStot;
    
    /**
     * 飞机类型
     */
    private String flgtAcname;

    /**
     * 飞机号码
     */
    private String flgtRegn;

    /**
     * 机位
     */
    private String flgtPlacecode;

    /**
     * 航空公司二字码
     */
    private String flgtAl2c;

    /**
     * 航空公司
     */
    private String flgtAlcname;

    /**
     * 航线（中文地名-中文地名）
     */
    private String flgtVialc;

    /**
     * 计划到达时间
     */
    private String flgtAStot;

    /**
     * 出发地机场三字码
     */
    private String flgtOrg3c;

    /**
     * 出发地机场
     */
    private String flgtOrgnm;

    /**
     * 目的地机场三字码
     */
    private String flgtDes3c;

    /**
     * 目的地机场（中文地名）
     */
    private String flgtDesnm;

    /**
     * 进离港（A：进港，D：出港）
     */
    private String flgtAdid;

    /**
     * 有效日期开始
     */
    private Date flgtStartDate;

    /**
     * 有效日期结束
     */
    private Date flgtEndDate;

    /**
     * 所属机场代码
     */
    private String flgtAirportCode;

    /**
     * 经停机场三字码
     */
    private String flgtTrs3c;

    /**
     * 经停机场
     */
    private String flgtTrsnm;

    /**
     * T_FLIGHT_TEMP
     */
    private static final long serialVersionUID = 1L;

    /**
     * 航班号
     * @return flgt_flno 航班号
     */
    public String getFlgtFlno() {
        return flgtFlno;
    }

    /**
     * 航班号
     * @param flgtFlno 航班号
     */
    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno == null ? null : flgtFlno.trim();
    }

    /**
     * 计划起飞时间
     * @return flgt_d_stot 计划起飞时间
     */
    public String getFlgtDStot() {
        return flgtDStot;
    }

    /**
     * 计划起飞时间
     * @param flgtDStot 计划起飞时间
     */
    public void setFlgtDStot(String flgtDStot) {
        this.flgtDStot = flgtDStot;
    }
    
    /**
     * 飞机类型
     * @return flgt_acname 飞机类型
     */
    public String getFlgtAcname() {
        return flgtAcname;
    }

    /**
     * 飞机类型
     * @param flgtAcname 飞机类型
     */
    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname == null ? null : flgtAcname.trim();
    }

    /**
     * 飞机号码
     * @return flgt_regn 飞机号码
     */
    public String getFlgtRegn() {
        return flgtRegn;
    }

    /**
     * 飞机号码
     * @param flgtRegn 飞机号码
     */
    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn == null ? null : flgtRegn.trim();
    }

    /**
     * 机位
     * @return flgt_placecode 机位
     */
    public String getFlgtPlacecode() {
        return flgtPlacecode;
    }

    /**
     * 机位
     * @param flgtPlacecode 机位
     */
    public void setFlgtPlacecode(String flgtPlacecode) {
        this.flgtPlacecode = flgtPlacecode == null ? null : flgtPlacecode.trim();
    }

    /**
     * 航空公司二字码
     * @return flgt_al2c 航空公司二字码
     */
    public String getFlgtAl2c() {
        return flgtAl2c;
    }

    /**
     * 航空公司二字码
     * @param flgtAl2c 航空公司二字码
     */
    public void setFlgtAl2c(String flgtAl2c) {
        this.flgtAl2c = flgtAl2c == null ? null : flgtAl2c.trim();
    }

    /**
     * 航空公司
     * @return flgt_alcname 航空公司
     */
    public String getFlgtAlcname() {
        return flgtAlcname;
    }

    /**
     * 航空公司
     * @param flgtAlcname 航空公司
     */
    public void setFlgtAlcname(String flgtAlcname) {
        this.flgtAlcname = flgtAlcname == null ? null : flgtAlcname.trim();
    }

    /**
     * 航线（中文地名-中文地名）
     * @return flgt_vialc 航线（中文地名-中文地名）
     */
    public String getFlgtVialc() {
        return flgtVialc;
    }

    /**
     * 航线（中文地名-中文地名）
     * @param flgtVialc 航线（中文地名-中文地名）
     */
    public void setFlgtVialc(String flgtVialc) {
        this.flgtVialc = flgtVialc == null ? null : flgtVialc.trim();
    }

    /**
     * 计划到达时间
     * @return flgt_a_stot 计划到达时间
     */
    public String getFlgtAStot() {
        return flgtAStot;
    }

    /**
     * 计划到达时间
     * @param flgtAStot 计划到达时间
     */
    public void setFlgtAStot(String flgtAStot) {
        this.flgtAStot = flgtAStot;
    }

    /**
     * 出发地机场三字码
     * @return flgt_org3c 出发地机场三字码
     */
    public String getFlgtOrg3c() {
        return flgtOrg3c;
    }

    /**
     * 出发地机场三字码
     * @param flgtOrg3c 出发地机场三字码
     */
    public void setFlgtOrg3c(String flgtOrg3c) {
        this.flgtOrg3c = flgtOrg3c == null ? null : flgtOrg3c.trim();
    }

    /**
     * 出发地机场
     * @return flgt_orgnm 出发地机场
     */
    public String getFlgtOrgnm() {
        return flgtOrgnm;
    }

    /**
     * 出发地机场
     * @param flgtOrgnm 出发地机场
     */
    public void setFlgtOrgnm(String flgtOrgnm) {
        this.flgtOrgnm = flgtOrgnm == null ? null : flgtOrgnm.trim();
    }

    /**
     * 目的地机场三字码
     * @return flgt_des3c 目的地机场三字码
     */
    public String getFlgtDes3c() {
        return flgtDes3c;
    }

    /**
     * 目的地机场三字码
     * @param flgtDes3c 目的地机场三字码
     */
    public void setFlgtDes3c(String flgtDes3c) {
        this.flgtDes3c = flgtDes3c == null ? null : flgtDes3c.trim();
    }

    /**
     * 目的地机场（中文地名）
     * @return flgt_desnm 目的地机场（中文地名）
     */
    public String getFlgtDesnm() {
        return flgtDesnm;
    }

    /**
     * 目的地机场（中文地名）
     * @param flgtDesnm 目的地机场（中文地名）
     */
    public void setFlgtDesnm(String flgtDesnm) {
        this.flgtDesnm = flgtDesnm == null ? null : flgtDesnm.trim();
    }

    /**
     * 进离港（A：进港，D：出港）
     * @return flgt_adid 进离港（A：进港，D：出港）
     */
    public String getFlgtAdid() {
        return flgtAdid;
    }

    /**
     * 进离港（A：进港，D：出港）
     * @param flgtAdid 进离港（A：进港，D：出港）
     */
    public void setFlgtAdid(String flgtAdid) {
        this.flgtAdid = flgtAdid == null ? null : flgtAdid.trim();
    }

    /**
     * 有效日期开始
     * @return flgt_start_date 有效日期开始
     */
    public Date getFlgtStartDate() {
        return flgtStartDate;
    }

    /**
     * 有效日期开始
     * @param flgtStartDate 有效日期开始
     */
    public void setFlgtStartDate(Date flgtStartDate) {
        this.flgtStartDate = flgtStartDate;
    }

    /**
     * 有效日期结束
     * @return flgt_end_date 有效日期结束
     */
    public Date getFlgtEndDate() {
        return flgtEndDate;
    }

    /**
     * 有效日期结束
     * @param flgtEndDate 有效日期结束
     */
    public void setFlgtEndDate(Date flgtEndDate) {
        this.flgtEndDate = flgtEndDate;
    }

    /**
     * 所属机场代码
     * @return flgt_airport_code 所属机场代码
     */
    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    /**
     * 所属机场代码
     * @param flgtAirportCode 所属机场代码
     */
    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode == null ? null : flgtAirportCode.trim();
    }

    /**
     * 经停机场三字码
     * @return flgt_trs3c 经停机场三字码
     */
    public String getFlgtTrs3c() {
        return flgtTrs3c;
    }

    /**
     * 经停机场三字码
     * @param flgtTrs3c 经停机场三字码
     */
    public void setFlgtTrs3c(String flgtTrs3c) {
        this.flgtTrs3c = flgtTrs3c == null ? null : flgtTrs3c.trim();
    }

    /**
     * 经停机场
     * @return flgt_trsnm 经停机场
     */
    public String getFlgtTrsnm() {
        return flgtTrsnm;
    }

    /**
     * 经停机场
     * @param flgtTrsnm 经停机场
     */
    public void setFlgtTrsnm(String flgtTrsnm) {
        this.flgtTrsnm = flgtTrsnm == null ? null : flgtTrsnm.trim();
    }
    
    /**
     * 航班日期
     * @return flgt_flop 航班日期
     */
    public Date getFlgtFlop() {
        return flgtFlop;
    }

    /**
     * 航班日期
     * @param flgtFlop 航班日期
     */
    public void setFlgtFlop(Date flgtFlop) {
        this.flgtFlop = flgtFlop;
    }
}