package com.zhoildataexchange.entity.flight;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:08
 * @Description:
 */
@Entity
@Table(name = "T_FLIGHT")
@NamedQuery(name = "TFlight.findAll", query = "SELECT a FROM TFlight a")
public class TFlight implements Serializable {
    private String flgtId;//UUID
    private String flgtFfid;//航班唯一标识码（对接各航班接口的唯一标识码）
    private String flgtAirportCode;//所属机场代码
    private String flgtAptareaCode;//所属机场区域代码
    
    private String flgtFlno;//航班号
    
    private Date flgtFlop;//航班日期
    
    private String flgtAcname;//飞机类型
    
    private String flgtRegn;//飞机号码
    
    private String flgtPlacecode;//机位
    
    private String flgtAl2C;//航空公司二字码
    
    private String flgtAlcname;//航空公司
    
    private String flgtVialc;//航线（中文地名-中文地名）
    
    private Date flgtAStot;//计划到达时间
    
    private Date flgtAEtot;//预计到达时间
    
    private Date flgtAAtot;//实际到达时间（落地时间）
    
    private Date flgtDStot;//计划起飞时间
    
    private Date flgtDEtot;//预计起飞时间
    
    private Date flgtDAtot;//实际起飞时间（离地时间）
    
    private String flgtOrg3C;//出发地机场三字码
    private String flgtOrgnm;//出发地机场
    private String flgtTrs3C1;//经停机场三字码1
    private String flgtTrsnm1;//经停机场1
    private String flgtTrs3C2;//经停机场三字码2
    private String flgtTrsnm2;//经停机场2
    private String flgtTrs3C3;//经停机场三字码3
    private String flgtTrsnm3;//经停机场3
    private String flgtTrs3C4;//经停机场三字码4
    private String flgtTrsnm4;//经停机场4
    private String flgtTrs3C5;//经停机场三字码5
    private String flgtTrsnm5;//经停机场5
    
    private String flgtDes3C;//目的地机场三字码
    private String flgtDesnm;//目的地机场
    
    private String flgtAdid;//进离港（A：进港，D：出港）
    
    private String flgtMissionProp;//航班任务属性
    
    private String flgtNature;//航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
    
    private String flgtSubNature;//航班性质细分
    
    private String flgtFlti;//航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知, O:外场, Z 支线）
    
    private String flgtFtyp;//航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
    
    private String flgtDelaycode;//延误原因
    private String flgtProxy;//航空服务代理（YAG（机场代理），CES（东航代理））

    private String flgtLinkFlno;    // 连接航班号（共享航班o号1，共享航班号2）
    private Date flgtLinkFlop;      // 连接航班日期
    private Integer flgtLinkRepeat; //  连接航班连接次数
    private Integer flgtRepeat;     //  航班连接次数

    private String flgtFnflag;//远近机位（N：近机位，F：远机位）
    private String flgtIfsr;//是否为货机（Y：货机，N：非货机）
    private String flgtGame;//本场（Y：本场，N：（空））
    private String flgtDepRunway;//离港跑道
    private String flgtArrRunway;//到港跑道
    private String flgtGate;//登机门
    private Date flgtCrewInPlace;//机组到位时间
    private Date flgtChocksIn;//上轮档时间
    private Date flgtChocksOut;//撤轮挡时间
    private Date flgtFirstLugg;//第一件行李时间
    private Date flgtLastLugg;//最后一件行李时间
    private Date flgtPtax;//前飞计划到达时间
    private Date flgtEtax;//前飞预计到达时间
    private Date flgtAtax;//前飞实际到达时间
    private Integer flgtTakeoffFuel;//起飞油量
    private Integer flgtChockFuel;//轮挡油量
    //flgt_expect_fuel
    private Integer flgtOtatFuel;// 预计加油量
    private Integer flgtVip;//要客（人数）
    private Integer flgtNum;//序号
    private Integer flgtTaskAsign;//任务下发标识（0：未下发，1：已下发）
    private Integer flgtStarmark;//航班星标（1：是，0：不是）
    private Integer flgtManualFlg;//手动修改航班（1：手动，0：非手动）
    private Integer flgtShareNoFlg;//航班合并flg 0 否 1 是
    
    private String flgtOtc;//航段
    
    private String flgtLinkFfid;//flgt_link_ffid  关联的FFID
    //flgt_otat
    private String flgtOtat;

    private Integer flgtOlvrIsupdate;

    @Basic
    @Column(name = "flgt_olvr_isupdate")
    public Integer getFlgtOlvrIsupdate() {
        return flgtOlvrIsupdate;
    }

    public void setFlgtOlvrIsupdate(Integer flgtOlvrIsupdate) {
        this.flgtOlvrIsupdate = flgtOlvrIsupdate;
    }

    private String flgtOlvr;
    @Basic
    @Column(name = "flgt_olvr")
    public String getFlgtOlvr() {
        return flgtOlvr;
    }

    public void setFlgtOlvr(String flgtOlvr) {
        this.flgtOlvr = flgtOlvr;
    }

    @Basic
    @Column(name = "flgt_otat")
    public String getFlgtOtat() {
        return flgtOtat;
    }

    public void setFlgtOtat(String flgtOtat) {
        this.flgtOtat = flgtOtat;
    }

    @Basic
    @Column(name = "flgt_otat_fuel")
    public Integer getFlgtOtatFuel() {
        return flgtOtatFuel;
    }

    public void setFlgtOtatFuel(Integer flgtOtatFuel) {
        this.flgtOtatFuel = flgtOtatFuel;
    }




    @Basic
    @Column(name = "flgt_link_ffid")
    public String getFlgtLinkFfid() {
        return flgtLinkFfid;
    }

    public void setFlgtLinkFfid(String flgtLinkFfid) {
        this.flgtLinkFfid = flgtLinkFfid;
    }

    @Id
    @Column(name = "flgt_id")
    public String getFlgtId() {
        return flgtId;
    }

    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId;
    }

    @Basic
    @Column(name = "flgt_ffid")
    public String getFlgtFfid() {
        return flgtFfid;
    }

    public void setFlgtFfid(String flgtFfid) {
        this.flgtFfid = flgtFfid;
    }

    @Basic
    @Column(name = "flgt_airport_code")
    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    @Basic
    @Column(name = "flgt_aptarea_code")
    public String getFlgtAptareaCode() {
        return flgtAptareaCode;
    }

    public void setFlgtAptareaCode(String flgtAptareaCode) {
        this.flgtAptareaCode = flgtAptareaCode;
    }

    @Basic
    @Column(name = "flgt_flno")
    public String getFlgtFlno() {
        return flgtFlno;
    }

    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno;
    }

    @Basic
    @Column(name = "flgt_flop")
    public Date getFlgtFlop() {
        return flgtFlop;
    }

    public void setFlgtFlop(Date flgtFlop) {
        this.flgtFlop = flgtFlop;
    }

    @Basic
    @Column(name = "flgt_acname")
    public String getFlgtAcname() {
        return flgtAcname;
    }

    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname;
    }

    @Basic
    @Column(name = "flgt_regn")
    public String getFlgtRegn() {
        return flgtRegn;
    }

    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn;
    }

    @Basic
    @Column(name = "flgt_placecode")
    public String getFlgtPlacecode() {
        return flgtPlacecode;
    }

    public void setFlgtPlacecode(String flgtPlacecode) {
        this.flgtPlacecode = flgtPlacecode;
    }

    @Basic
    @Column(name = "flgt_al2c")
    public String getFlgtAl2C() {
        return flgtAl2C;
    }

    public void setFlgtAl2C(String flgtAl2C) {
        this.flgtAl2C = flgtAl2C;
    }

    @Basic
    @Column(name = "flgt_alcname")
    public String getFlgtAlcname() {
        return flgtAlcname;
    }

    public void setFlgtAlcname(String flgtAlcname) {
        this.flgtAlcname = flgtAlcname;
    }

    @Basic
    @Column(name = "flgt_vialc")
    public String getFlgtVialc() {
        return flgtVialc;
    }

    public void setFlgtVialc(String flgtVialc) {
        this.flgtVialc = flgtVialc;
    }

    @Basic
    @Column(name = "flgt_a_stot")
    public Date getFlgtAStot() {
        return flgtAStot;
    }

    public void setFlgtAStot(Date flgtAStot) {
        this.flgtAStot = flgtAStot;
    }

    @Basic
    @Column(name = "flgt_a_etot")
    public Date getFlgtAEtot() {
        return flgtAEtot;
    }

    public void setFlgtAEtot(Date flgtAEtot) {
        this.flgtAEtot = flgtAEtot;
    }

    @Basic
    @Column(name = "flgt_a_atot")
    public Date getFlgtAAtot() {
        return flgtAAtot;
    }

    public void setFlgtAAtot(Date flgtAAtot) {
        this.flgtAAtot = flgtAAtot;
    }

    @Basic
    @Column(name = "flgt_d_stot")
    public Date getFlgtDStot() {
        return flgtDStot;
    }

    public void setFlgtDStot(Date flgtDStot) {
        this.flgtDStot = flgtDStot;
    }

    @Basic
    @Column(name = "flgt_d_etot")
    public Date getFlgtDEtot() {
        return flgtDEtot;
    }

    public void setFlgtDEtot(Date flgtDEtot) {
        this.flgtDEtot = flgtDEtot;
    }

    @Basic
    @Column(name = "flgt_d_atot")
    public Date getFlgtDAtot() {
        return flgtDAtot;
    }

    public void setFlgtDAtot(Date flgtDAtot) {
        this.flgtDAtot = flgtDAtot;
    }

    @Basic
    @Column(name = "flgt_org3c")
    public String getFlgtOrg3C() {
        return flgtOrg3C;
    }

    public void setFlgtOrg3C(String flgtOrg3C) {
        this.flgtOrg3C = flgtOrg3C;
    }

    @Basic
    @Column(name = "flgt_orgnm")
    public String getFlgtOrgnm() {
        return flgtOrgnm;
    }

    public void setFlgtOrgnm(String flgtOrgnm) {
        this.flgtOrgnm = flgtOrgnm;
    }

    @Basic
    @Column(name = "flgt_trs3c1")
    public String getFlgtTrs3C1() {
        return flgtTrs3C1;
    }

    public void setFlgtTrs3C1(String flgtTrs3C1) {
        this.flgtTrs3C1 = flgtTrs3C1;
    }

    @Basic
    @Column(name = "flgt_trsnm1")
    public String getFlgtTrsnm1() {
        return flgtTrsnm1;
    }

    public void setFlgtTrsnm1(String flgtTrsnm1) {
        this.flgtTrsnm1 = flgtTrsnm1;
    }

    @Basic
    @Column(name = "flgt_trs3c2")
    public String getFlgtTrs3C2() {
        return flgtTrs3C2;
    }

    public void setFlgtTrs3C2(String flgtTrs3C2) {
        this.flgtTrs3C2 = flgtTrs3C2;
    }

    @Basic
    @Column(name = "flgt_trsnm2")
    public String getFlgtTrsnm2() {
        return flgtTrsnm2;
    }

    public void setFlgtTrsnm2(String flgtTrsnm2) {
        this.flgtTrsnm2 = flgtTrsnm2;
    }

    @Basic
    @Column(name = "flgt_trs3c3")
    public String getFlgtTrs3C3() {
        return flgtTrs3C3;
    }

    public void setFlgtTrs3C3(String flgtTrs3C3) {
        this.flgtTrs3C3 = flgtTrs3C3;
    }

    @Basic
    @Column(name = "flgt_trsnm3")
    public String getFlgtTrsnm3() {
        return flgtTrsnm3;
    }

    public void setFlgtTrsnm3(String flgtTrsnm3) {
        this.flgtTrsnm3 = flgtTrsnm3;
    }

    @Basic
    @Column(name = "flgt_trs3c4")
    public String getFlgtTrs3C4() {
        return flgtTrs3C4;
    }

    public void setFlgtTrs3C4(String flgtTrs3C4) {
        this.flgtTrs3C4 = flgtTrs3C4;
    }

    @Basic
    @Column(name = "flgt_trsnm4")
    public String getFlgtTrsnm4() {
        return flgtTrsnm4;
    }

    public void setFlgtTrsnm4(String flgtTrsnm4) {
        this.flgtTrsnm4 = flgtTrsnm4;
    }

    @Basic
    @Column(name = "flgt_trs3c5")
    public String getFlgtTrs3C5() {
        return flgtTrs3C5;
    }

    public void setFlgtTrs3C5(String flgtTrs3C5) {
        this.flgtTrs3C5 = flgtTrs3C5;
    }

    @Basic
    @Column(name = "flgt_trsnm5")
    public String getFlgtTrsnm5() {
        return flgtTrsnm5;
    }

    public void setFlgtTrsnm5(String flgtTrsnm5) {
        this.flgtTrsnm5 = flgtTrsnm5;
    }

    @Basic
    @Column(name = "flgt_des3c")
    public String getFlgtDes3C() {
        return flgtDes3C;
    }

    public void setFlgtDes3C(String flgtDes3C) {
        this.flgtDes3C = flgtDes3C;
    }

    @Basic
    @Column(name = "flgt_desnm")
    public String getFlgtDesnm() {
        return flgtDesnm;
    }

    public void setFlgtDesnm(String flgtDesnm) {
        this.flgtDesnm = flgtDesnm;
    }

    @Basic
    @Column(name = "flgt_adid")
    public String getFlgtAdid() {
        return flgtAdid;
    }

    public void setFlgtAdid(String flgtAdid) {
        this.flgtAdid = flgtAdid;
    }

    @Basic
    @Column(name = "flgt_mission_prop")
    public String getFlgtMissionProp() {
        return flgtMissionProp;
    }

    public void setFlgtMissionProp(String flgtMissionProp) {
        this.flgtMissionProp = flgtMissionProp;
    }

    @Basic
    @Column(name = "flgt_nature")
    public String getFlgtNature() {
        return flgtNature;
    }

    public void setFlgtNature(String flgtNature) {
        this.flgtNature = flgtNature;
    }

    @Basic
    @Column(name = "flgt_sub_nature")
    public String getFlgtSubNature() {
        return flgtSubNature;
    }

    public void setFlgtSubNature(String flgtSubNature) {
        this.flgtSubNature = flgtSubNature;
    }

    @Basic
    @Column(name = "flgt_flti")
    public String getFlgtFlti() {
        return flgtFlti;
    }

    public void setFlgtFlti(String flgtFlti) {
        this.flgtFlti = flgtFlti;
    }

    @Basic
    @Column(name = "flgt_ftyp")
    public String getFlgtFtyp() {
        return flgtFtyp;
    }

    public void setFlgtFtyp(String flgtFtyp) {
        this.flgtFtyp = flgtFtyp;
    }

    @Basic
    @Column(name = "flgt_delaycode")
    public String getFlgtDelaycode() {
        return flgtDelaycode;
    }

    public void setFlgtDelaycode(String flgtDelaycode) {
        this.flgtDelaycode = flgtDelaycode;
    }

    @Basic
    @Column(name = "flgt_proxy")
    public String getFlgtProxy() {
        return flgtProxy;
    }

    public void setFlgtProxy(String flgtProxy) {
        this.flgtProxy = flgtProxy;
    }

    @Basic
    @Column(name = "flgt_link_flno")
    public String getFlgtLinkFlno() {
        return flgtLinkFlno;
    }

    public void setFlgtLinkFlno(String flgtLinkFlno) {
        this.flgtLinkFlno = flgtLinkFlno;
    }

    @Basic
    @Column(name = "flgt_link_flop")
    public Date getFlgtLinkFlop() {
        return flgtLinkFlop;
    }

    public void setFlgtLinkFlop(Date flgtLinkFlop) {
        this.flgtLinkFlop = flgtLinkFlop;
    }

    @Basic
    @Column(name = "flgt_link_repeat")
    public Integer getFlgtLinkRepeat() {
        return flgtLinkRepeat;
    }

    public void setFlgtLinkRepeat(Integer flgtLinkRepeat) {
        this.flgtLinkRepeat = flgtLinkRepeat;
    }

    @Basic
    @Column(name = "flgt_repeat")
    public Integer getFlgtRepeat() {
        return flgtRepeat;
    }

    public void setFlgtRepeat(Integer flgtRepeat) {
        this.flgtRepeat = flgtRepeat;
    }

    @Basic
    @Column(name = "flgt_fnflag")
    public String getFlgtFnflag() {
        return flgtFnflag;
    }

    public void setFlgtFnflag(String flgtFnflag) {
        this.flgtFnflag = flgtFnflag;
    }

    @Basic
    @Column(name = "flgt_ifsr")
    public String getFlgtIfsr() {
        return flgtIfsr;
    }

    public void setFlgtIfsr(String flgtIfsr) {
        this.flgtIfsr = flgtIfsr;
    }

    @Basic
    @Column(name = "flgt_game")
    public String getFlgtGame() {
        return flgtGame;
    }

    public void setFlgtGame(String flgtGame) {
        this.flgtGame = flgtGame;
    }

    @Basic
    @Column(name = "flgt_dep_runway")
    public String getFlgtDepRunway() {
        return flgtDepRunway;
    }

    public void setFlgtDepRunway(String flgtDepRunway) {
        this.flgtDepRunway = flgtDepRunway;
    }

    @Basic
    @Column(name = "flgt_arr_runway")
    public String getFlgtArrRunway() {
        return flgtArrRunway;
    }

    public void setFlgtArrRunway(String flgtArrRunway) {
        this.flgtArrRunway = flgtArrRunway;
    }

    @Basic
    @Column(name = "flgt_gate")
    public String getFlgtGate() {
        return flgtGate;
    }

    public void setFlgtGate(String flgtGate) {
        this.flgtGate = flgtGate;
    }

    @Basic
    @Column(name = "flgt_crew_in_place")
    public Date getFlgtCrewInPlace() {
        return flgtCrewInPlace;
    }

    public void setFlgtCrewInPlace(Date flgtCrewInPlace) {
        this.flgtCrewInPlace = flgtCrewInPlace;
    }

    @Basic
    @Column(name = "flgt_chocks_in")
    public Date getFlgtChocksIn() {
        return flgtChocksIn;
    }

    public void setFlgtChocksIn(Date flgtChocksIn) {
        this.flgtChocksIn = flgtChocksIn;
    }

    @Basic
    @Column(name = "flgt_chocks_out")
    public Date getFlgtChocksOut() {
        return flgtChocksOut;
    }

    public void setFlgtChocksOut(Date flgtChocksOut) {
        this.flgtChocksOut = flgtChocksOut;
    }

    @Basic
    @Column(name = "flgt_first_lugg")
    public Date getFlgtFirstLugg() {
        return flgtFirstLugg;
    }

    public void setFlgtFirstLugg(Date flgtFirstLugg) {
        this.flgtFirstLugg = flgtFirstLugg;
    }

    @Basic
    @Column(name = "flgt_last_lugg")
    public Date getFlgtLastLugg() {
        return flgtLastLugg;
    }

    public void setFlgtLastLugg(Date flgtLastLugg) {
        this.flgtLastLugg = flgtLastLugg;
    }

    @Basic
    @Column(name = "flgt_ptax")
    public Date getFlgtPtax() {
        return flgtPtax;
    }

    public void setFlgtPtax(Date flgtPtax) {
        this.flgtPtax = flgtPtax;
    }

    @Basic
    @Column(name = "flgt_etax")
    public Date getFlgtEtax() {
        return flgtEtax;
    }

    public void setFlgtEtax(Date flgtEtax) {
        this.flgtEtax = flgtEtax;
    }

    @Basic
    @Column(name = "flgt_atax")
    public Date getFlgtAtax() {
        return flgtAtax;
    }

    public void setFlgtAtax(Date flgtAtax) {
        this.flgtAtax = flgtAtax;
    }

    @Basic
    @Column(name = "flgt_takeoff_fuel")
    public Integer getFlgtTakeoffFuel() {
        return flgtTakeoffFuel;
    }

    public void setFlgtTakeoffFuel(Integer flgtTakeoffFuel) {
        this.flgtTakeoffFuel = flgtTakeoffFuel;
    }

    @Basic
    @Column(name = "flgt_chock_fuel")
    public Integer getFlgtChockFuel() {
        return flgtChockFuel;
    }

    public void setFlgtChockFuel(Integer flgtChockFuel) {
        this.flgtChockFuel = flgtChockFuel;
    }

    @Basic
    @Column(name = "flgt_vip")
    public Integer getFlgtVip() {
        return flgtVip;
    }

    public void setFlgtVip(Integer flgtVip) {
        this.flgtVip = flgtVip;
    }

    @Basic
    @Column(name = "flgt_num")
    public Integer getFlgtNum() {
        return flgtNum;
    }

    public void setFlgtNum(Integer flgtNum) {
        this.flgtNum = flgtNum;
    }

    @Basic
    @Column(name = "flgt_task_asign")
    public Integer getFlgtTaskAsign() {
        return flgtTaskAsign;
    }

    public void setFlgtTaskAsign(Integer flgtTaskAsign) {
        this.flgtTaskAsign = flgtTaskAsign;
    }

    @Basic
    @Column(name = "flgt_starmark")
    public Integer getFlgtStarmark() {
        return flgtStarmark;
    }

    public void setFlgtStarmark(Integer flgtStarmark) {
        this.flgtStarmark = flgtStarmark;
    }

    @Basic
    @Column(name = "flgt_manual_flg")
    public Integer getFlgtManualFlg() {
        return flgtManualFlg;
    }

    public void setFlgtManualFlg(Integer flgtManualFlg) {
        this.flgtManualFlg = flgtManualFlg;
    }

    @Basic
    @Column(name = "flgt_share_no_flg")
    public Integer getFlgtShareNoFlg() {
        return flgtShareNoFlg;
    }

    public void setFlgtShareNoFlg(Integer flgtShareNoFlg) {
        this.flgtShareNoFlg = flgtShareNoFlg;
    }

    @Basic
    @Column(name = "flgt_otc")
    public String getFlgtOtc() {
        return flgtOtc;
    }

    public void setFlgtOtc(String flgtOtc) {
        this.flgtOtc = flgtOtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFlight tFlight = (TFlight) o;
        return Objects.equals(flgtId, tFlight.flgtId) &&
                Objects.equals(flgtFfid, tFlight.flgtFfid) &&
                Objects.equals(flgtAirportCode, tFlight.flgtAirportCode) &&
                Objects.equals(flgtAptareaCode, tFlight.flgtAptareaCode) &&
                Objects.equals(flgtFlno, tFlight.flgtFlno) &&
                Objects.equals(flgtFlop, tFlight.flgtFlop) &&
                Objects.equals(flgtAcname, tFlight.flgtAcname) &&
                Objects.equals(flgtRegn, tFlight.flgtRegn) &&
                Objects.equals(flgtPlacecode, tFlight.flgtPlacecode) &&
                Objects.equals(flgtAl2C, tFlight.flgtAl2C) &&
                Objects.equals(flgtAlcname, tFlight.flgtAlcname) &&
                Objects.equals(flgtVialc, tFlight.flgtVialc) &&
                Objects.equals(flgtAStot, tFlight.flgtAStot) &&
                Objects.equals(flgtAEtot, tFlight.flgtAEtot) &&
                Objects.equals(flgtAAtot, tFlight.flgtAAtot) &&
                Objects.equals(flgtDStot, tFlight.flgtDStot) &&
                Objects.equals(flgtDEtot, tFlight.flgtDEtot) &&
                Objects.equals(flgtDAtot, tFlight.flgtDAtot) &&
                Objects.equals(flgtOrg3C, tFlight.flgtOrg3C) &&
                Objects.equals(flgtOrgnm, tFlight.flgtOrgnm) &&
                Objects.equals(flgtTrs3C1, tFlight.flgtTrs3C1) &&
                Objects.equals(flgtTrsnm1, tFlight.flgtTrsnm1) &&
                Objects.equals(flgtTrs3C2, tFlight.flgtTrs3C2) &&
                Objects.equals(flgtTrsnm2, tFlight.flgtTrsnm2) &&
                Objects.equals(flgtTrs3C3, tFlight.flgtTrs3C3) &&
                Objects.equals(flgtTrsnm3, tFlight.flgtTrsnm3) &&
                Objects.equals(flgtTrs3C4, tFlight.flgtTrs3C4) &&
                Objects.equals(flgtTrsnm4, tFlight.flgtTrsnm4) &&
                Objects.equals(flgtTrs3C5, tFlight.flgtTrs3C5) &&
                Objects.equals(flgtTrsnm5, tFlight.flgtTrsnm5) &&
                Objects.equals(flgtDes3C, tFlight.flgtDes3C) &&
                Objects.equals(flgtDesnm, tFlight.flgtDesnm) &&
                Objects.equals(flgtAdid, tFlight.flgtAdid) &&
                Objects.equals(flgtMissionProp, tFlight.flgtMissionProp) &&
                Objects.equals(flgtNature, tFlight.flgtNature) &&
                Objects.equals(flgtSubNature, tFlight.flgtSubNature) &&
                Objects.equals(flgtFlti, tFlight.flgtFlti) &&
                Objects.equals(flgtFtyp, tFlight.flgtFtyp) &&
                Objects.equals(flgtDelaycode, tFlight.flgtDelaycode) &&
                Objects.equals(flgtProxy, tFlight.flgtProxy) &&
                Objects.equals(flgtLinkFlno, tFlight.flgtLinkFlno) &&
                Objects.equals(flgtLinkFlop, tFlight.flgtLinkFlop) &&
                Objects.equals(flgtLinkRepeat, tFlight.flgtLinkRepeat) &&
                Objects.equals(flgtRepeat, tFlight.flgtRepeat) &&
                Objects.equals(flgtFnflag, tFlight.flgtFnflag) &&
                Objects.equals(flgtIfsr, tFlight.flgtIfsr) &&
                Objects.equals(flgtGame, tFlight.flgtGame) &&
                Objects.equals(flgtDepRunway, tFlight.flgtDepRunway) &&
                Objects.equals(flgtArrRunway, tFlight.flgtArrRunway) &&
                Objects.equals(flgtGate, tFlight.flgtGate) &&
                Objects.equals(flgtCrewInPlace, tFlight.flgtCrewInPlace) &&
                Objects.equals(flgtChocksIn, tFlight.flgtChocksIn) &&
                Objects.equals(flgtChocksOut, tFlight.flgtChocksOut) &&
                Objects.equals(flgtFirstLugg, tFlight.flgtFirstLugg) &&
                Objects.equals(flgtLastLugg, tFlight.flgtLastLugg) &&
                Objects.equals(flgtPtax, tFlight.flgtPtax) &&
                Objects.equals(flgtEtax, tFlight.flgtEtax) &&
                Objects.equals(flgtAtax, tFlight.flgtAtax) &&
                Objects.equals(flgtTakeoffFuel, tFlight.flgtTakeoffFuel) &&
                Objects.equals(flgtChockFuel, tFlight.flgtChockFuel) &&
                Objects.equals(flgtVip, tFlight.flgtVip) &&
                Objects.equals(flgtNum, tFlight.flgtNum) &&
                Objects.equals(flgtTaskAsign, tFlight.flgtTaskAsign) &&
                Objects.equals(flgtStarmark, tFlight.flgtStarmark) &&
                Objects.equals(flgtManualFlg, tFlight.flgtManualFlg) &&
                Objects.equals(flgtShareNoFlg, tFlight.flgtShareNoFlg) &&
                Objects.equals(flgtOtc, tFlight.flgtOtc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(flgtId, flgtFfid, flgtAirportCode, flgtAptareaCode, flgtFlno, flgtFlop, flgtAcname, flgtRegn, flgtPlacecode, flgtAl2C, flgtAlcname, flgtVialc, flgtAStot, flgtAEtot, flgtAAtot, flgtDStot, flgtDEtot, flgtDAtot, flgtOrg3C, flgtOrgnm, flgtTrs3C1, flgtTrsnm1, flgtTrs3C2, flgtTrsnm2, flgtTrs3C3, flgtTrsnm3, flgtTrs3C4, flgtTrsnm4, flgtTrs3C5, flgtTrsnm5, flgtDes3C, flgtDesnm, flgtAdid, flgtMissionProp, flgtNature, flgtSubNature, flgtFlti, flgtFtyp, flgtDelaycode, flgtProxy, flgtLinkFlno, flgtLinkFlop, flgtLinkRepeat, flgtRepeat, flgtFnflag, flgtIfsr, flgtGame, flgtDepRunway, flgtArrRunway, flgtGate, flgtCrewInPlace, flgtChocksIn, flgtChocksOut, flgtFirstLugg, flgtLastLugg, flgtPtax, flgtEtax, flgtAtax, flgtTakeoffFuel, flgtChockFuel, flgtVip, flgtNum, flgtTaskAsign, flgtStarmark, flgtManualFlg, flgtShareNoFlg, flgtOtc);
    }
}
