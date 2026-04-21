package com.zh.bean.auto;

import java.io.Serializable;
import java.util.Date;

/**
 * t_flight
 */
public class TFlight implements Serializable {
    /**
     * 航班ID（uuid）
     */
    private String flgtId;

    /**
     * 进港航班号
     */
    private String flgtAFlno;

    /**
     * 出港航班号
     */
    private String flgtDFlno;

    /**
     * 进港日期
     */
    private Date flgtAFlop;

    /**
     * 出港日期
     */
    private Date flgtDFlop;

    /**
     * 飞机类型
     */
    private String flgtDAcname;

    /**
     * 飞机号码
     */
    private String flgtDRegn;

    /**
     * 机位
     */
    private String flgtDPlacecode;

    /**
     * 航空公司二字码
     */
    private String flgtDAl2c;

    /**
     * 航空公司
     */
    private String flgtDAlcname;

    /**
     * 航线（中文地名-中文地名（-中文地名））
     */
    private String flgtDVialc;

    /**
     * 计划到达时间
     */
    private Date flgtAStot;

    /**
     * 预计到达时间
     */
    private Date flgtAEtot;

    /**
     * 实际到达时间（或称落地时间）
     */
    private Date flgtAAtot;

    /**
     * 计划起飞时间
     */
    private Date flgtDStot;

    /**
     * 预计起飞时间
     */
    private Date flgtDEtot;

    /**
     * 实际起飞时间（或称离地时间）
     */
    private Date flgtDAtot;

    /**
     * 出发地机场三字码
     */
    private String flgtAOrg3c;

    /**
     * 出发地机场
     */
    private String flgtAOrgnm;

    /**
     * 经停备降机场
     */
    private String flgtDTrsnm;

    /**
     * 目的地机场三字码
     */
    private String flgtDDes3c;

    /**
     * 目的地机场（中文地名）
     */
    private String flgtDDesnm;

    /**
     * 进离港（A：进港，D：出港，T：经停）
     */
    private String flgtDAdid;

    /**
     * 进港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    private String flgtAFlti;

    /**
     * 出港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    private String flgtDFlti;

    /**
     * 进港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    private String flgtAFtyp;

    /**
     * 出港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    private String flgtDFtyp;

    /**
     * 远近机位（N：近机位，F：远机位）
     */
    private String flgtDFnflag;

    /**
     * 是否为货机（Y：货机，N：非货机）
     */
    private String flgtDIfsr;

    /**
     * 本场（Y：本场，N：（空））
     */
    private String flgtDGame;

    /**
     * 前飞计划到达时间
     */
    private Date flgtDPtax;

    /**
     * 前飞预计到达时间
     */
    private Date flgtDEtax;

    /**
     * 前飞实际到达时间
     */
    private Date flgtDAtax;

    /**
     * 航班星标（1：是）
     */
    private Integer flgtStarmark;

    /**
     * 执行日期
     */
    private Date flgtExedate;
    
    /**
     * 业务字段(是否重要  0：不重要 1：重要)
     */
    private Integer flg;

    /**
     * t_flight
     */
    private static final long serialVersionUID = 1L;

    /**
     * 航班ID（uuid）
     * @return flgt_id 航班ID（uuid）
     */
    public String getFlgtId() {
        return flgtId;
    }

    /**
     * 航班ID（uuid）
     * @param flgtId 航班ID（uuid）
     */
    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId == null ? null : flgtId.trim();
    }

    /**
     * 进港航班号
     * @return flgt_a_flno 进港航班号
     */
    public String getFlgtAFlno() {
        return flgtAFlno;
    }

    /**
     * 进港航班号
     * @param flgtAFlno 进港航班号
     */
    public void setFlgtAFlno(String flgtAFlno) {
        this.flgtAFlno = flgtAFlno == null ? null : flgtAFlno.trim();
    }

    /**
     * 出港航班号
     * @return flgt_d_flno 出港航班号
     */
    public String getFlgtDFlno() {
        return flgtDFlno;
    }

    /**
     * 出港航班号
     * @param flgtDFlno 出港航班号
     */
    public void setFlgtDFlno(String flgtDFlno) {
        this.flgtDFlno = flgtDFlno == null ? null : flgtDFlno.trim();
    }

    /**
     * 进港日期
     * @return flgt_a_flop 进港日期
     */
    public Date getFlgtAFlop() {
        return flgtAFlop;
    }

    /**
     * 进港日期
     * @param flgtAFlop 进港日期
     */
    public void setFlgtAFlop(Date flgtAFlop) {
        this.flgtAFlop = flgtAFlop;
    }

    /**
     * 出港日期
     * @return flgt_d_flop 出港日期
     */
    public Date getFlgtDFlop() {
        return flgtDFlop;
    }

    /**
     * 出港日期
     * @param flgtDFlop 出港日期
     */
    public void setFlgtDFlop(Date flgtDFlop) {
        this.flgtDFlop = flgtDFlop;
    }

    /**
     * 飞机类型
     * @return flgt_d_acname 飞机类型
     */
    public String getFlgtDAcname() {
        return flgtDAcname;
    }

    /**
     * 飞机类型
     * @param flgtDAcname 飞机类型
     */
    public void setFlgtDAcname(String flgtDAcname) {
        this.flgtDAcname = flgtDAcname == null ? null : flgtDAcname.trim();
    }

    /**
     * 飞机号码
     * @return flgt_d_regn 飞机号码
     */
    public String getFlgtDRegn() {
        return flgtDRegn;
    }

    /**
     * 飞机号码
     * @param flgtDRegn 飞机号码
     */
    public void setFlgtDRegn(String flgtDRegn) {
        this.flgtDRegn = flgtDRegn == null ? null : flgtDRegn.trim();
    }

    /**
     * 机位
     * @return flgt_d_placecode 机位
     */
    public String getFlgtDPlacecode() {
        return flgtDPlacecode;
    }

    /**
     * 机位
     * @param flgtDPlacecode 机位
     */
    public void setFlgtDPlacecode(String flgtDPlacecode) {
        this.flgtDPlacecode = flgtDPlacecode == null ? null : flgtDPlacecode.trim();
    }

    /**
     * 航空公司二字码
     * @return flgt_d_al2c 航空公司二字码
     */
    public String getFlgtDAl2c() {
        return flgtDAl2c;
    }

    /**
     * 航空公司二字码
     * @param flgtDAl2c 航空公司二字码
     */
    public void setFlgtDAl2c(String flgtDAl2c) {
        this.flgtDAl2c = flgtDAl2c == null ? null : flgtDAl2c.trim();
    }

    /**
     * 航空公司
     * @return flgt_d_alcname 航空公司
     */
    public String getFlgtDAlcname() {
        return flgtDAlcname;
    }

    /**
     * 航空公司
     * @param flgtDAlcname 航空公司
     */
    public void setFlgtDAlcname(String flgtDAlcname) {
        this.flgtDAlcname = flgtDAlcname == null ? null : flgtDAlcname.trim();
    }

    /**
     * 航线（中文地名-中文地名（-中文地名））
     * @return flgt_d_vialc 航线（中文地名-中文地名（-中文地名））
     */
    public String getFlgtDVialc() {
        return flgtDVialc;
    }

    /**
     * 航线（中文地名-中文地名（-中文地名））
     * @param flgtDVialc 航线（中文地名-中文地名（-中文地名））
     */
    public void setFlgtDVialc(String flgtDVialc) {
        this.flgtDVialc = flgtDVialc == null ? null : flgtDVialc.trim();
    }

    /**
     * 计划到达时间
     * @return flgt_a_stot 计划到达时间
     */
    public Date getFlgtAStot() {
        return flgtAStot;
    }

    /**
     * 计划到达时间
     * @param flgtAStot 计划到达时间
     */
    public void setFlgtAStot(Date flgtAStot) {
        this.flgtAStot = flgtAStot;
    }

    /**
     * 预计到达时间
     * @return flgt_a_etot 预计到达时间
     */
    public Date getFlgtAEtot() {
        return flgtAEtot;
    }

    /**
     * 预计到达时间
     * @param flgtAEtot 预计到达时间
     */
    public void setFlgtAEtot(Date flgtAEtot) {
        this.flgtAEtot = flgtAEtot;
    }

    /**
     * 实际到达时间（或称落地时间）
     * @return flgt_a_atot 实际到达时间（或称落地时间）
     */
    public Date getFlgtAAtot() {
        return flgtAAtot;
    }

    /**
     * 实际到达时间（或称落地时间）
     * @param flgtAAtot 实际到达时间（或称落地时间）
     */
    public void setFlgtAAtot(Date flgtAAtot) {
        this.flgtAAtot = flgtAAtot;
    }

    /**
     * 计划起飞时间
     * @return flgt_d_stot 计划起飞时间
     */
    public Date getFlgtDStot() {
        return flgtDStot;
    }

    /**
     * 计划起飞时间
     * @param flgtDStot 计划起飞时间
     */
    public void setFlgtDStot(Date flgtDStot) {
        this.flgtDStot = flgtDStot;
    }

    /**
     * 预计起飞时间
     * @return flgt_d_etot 预计起飞时间
     */
    public Date getFlgtDEtot() {
        return flgtDEtot;
    }

    /**
     * 预计起飞时间
     * @param flgtDEtot 预计起飞时间
     */
    public void setFlgtDEtot(Date flgtDEtot) {
        this.flgtDEtot = flgtDEtot;
    }

    /**
     * 实际起飞时间（或称离地时间）
     * @return flgt_d_atot 实际起飞时间（或称离地时间）
     */
    public Date getFlgtDAtot() {
        return flgtDAtot;
    }

    /**
     * 实际起飞时间（或称离地时间）
     * @param flgtDAtot 实际起飞时间（或称离地时间）
     */
    public void setFlgtDAtot(Date flgtDAtot) {
        this.flgtDAtot = flgtDAtot;
    }

    /**
     * 出发地机场三字码
     * @return flgt_a_org3c 出发地机场三字码
     */
    public String getFlgtAOrg3c() {
        return flgtAOrg3c;
    }

    /**
     * 出发地机场三字码
     * @param flgtAOrg3c 出发地机场三字码
     */
    public void setFlgtAOrg3c(String flgtAOrg3c) {
        this.flgtAOrg3c = flgtAOrg3c == null ? null : flgtAOrg3c.trim();
    }

    /**
     * 出发地机场
     * @return flgt_a_orgnm 出发地机场
     */
    public String getFlgtAOrgnm() {
        return flgtAOrgnm;
    }

    /**
     * 出发地机场
     * @param flgtAOrgnm 出发地机场
     */
    public void setFlgtAOrgnm(String flgtAOrgnm) {
        this.flgtAOrgnm = flgtAOrgnm == null ? null : flgtAOrgnm.trim();
    }

    /**
     * 经停备降机场
     * @return flgt_d_trsnm 经停备降机场
     */
    public String getFlgtDTrsnm() {
        return flgtDTrsnm;
    }

    /**
     * 经停备降机场
     * @param flgtDTrsnm 经停备降机场
     */
    public void setFlgtDTrsnm(String flgtDTrsnm) {
        this.flgtDTrsnm = flgtDTrsnm == null ? null : flgtDTrsnm.trim();
    }

    /**
     * 目的地机场三字码
     * @return flgt_d_des3c 目的地机场三字码
     */
    public String getFlgtDDes3c() {
        return flgtDDes3c;
    }

    /**
     * 目的地机场三字码
     * @param flgtDDes3c 目的地机场三字码
     */
    public void setFlgtDDes3c(String flgtDDes3c) {
        this.flgtDDes3c = flgtDDes3c == null ? null : flgtDDes3c.trim();
    }

    /**
     * 目的地机场（中文地名）
     * @return flgt_d_desnm 目的地机场（中文地名）
     */
    public String getFlgtDDesnm() {
        return flgtDDesnm;
    }

    /**
     * 目的地机场（中文地名）
     * @param flgtDDesnm 目的地机场（中文地名）
     */
    public void setFlgtDDesnm(String flgtDDesnm) {
        this.flgtDDesnm = flgtDDesnm == null ? null : flgtDDesnm.trim();
    }

    /**
     * 进离港（A：进港，D：出港，T：经停）
     * @return flgt_d_adid 进离港（A：进港，D：出港，T：经停）
     */
    public String getFlgtDAdid() {
        return flgtDAdid;
    }

    /**
     * 进离港（A：进港，D：出港，T：经停）
     * @param flgtDAdid 进离港（A：进港，D：出港，T：经停）
     */
    public void setFlgtDAdid(String flgtDAdid) {
        this.flgtDAdid = flgtDAdid == null ? null : flgtDAdid.trim();
    }

    /**
     * 进港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @return flgt_a_flti 进港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public String getFlgtAFlti() {
        return flgtAFlti;
    }

    /**
     * 进港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @param flgtAFlti 进港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public void setFlgtAFlti(String flgtAFlti) {
        this.flgtAFlti = flgtAFlti == null ? null : flgtAFlti.trim();
    }

    /**
     * 出港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @return flgt_d_flti 出港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public String getFlgtDFlti() {
        return flgtDFlti;
    }

    /**
     * 出港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @param flgtDFlti 出港航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public void setFlgtDFlti(String flgtDFlti) {
        this.flgtDFlti = flgtDFlti == null ? null : flgtDFlti.trim();
    }

    /**
     * 进港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @return flgt_a_ftyp 进港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public String getFlgtAFtyp() {
        return flgtAFtyp;
    }

    /**
     * 进港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @param flgtAFtyp 进港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public void setFlgtAFtyp(String flgtAFtyp) {
        this.flgtAFtyp = flgtAFtyp == null ? null : flgtAFtyp.trim();
    }

    /**
     * 出港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @return flgt_d_ftyp 出港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public String getFlgtDFtyp() {
        return flgtDFtyp;
    }

    /**
     * 出港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @param flgtDFtyp 出港航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public void setFlgtDFtyp(String flgtDFtyp) {
        this.flgtDFtyp = flgtDFtyp == null ? null : flgtDFtyp.trim();
    }

    /**
     * 远近机位（N：近机位，F：远机位）
     * @return flgt_d_fnflag 远近机位（N：近机位，F：远机位）
     */
    public String getFlgtDFnflag() {
        return flgtDFnflag;
    }

    /**
     * 远近机位（N：近机位，F：远机位）
     * @param flgtDFnflag 远近机位（N：近机位，F：远机位）
     */
    public void setFlgtDFnflag(String flgtDFnflag) {
        this.flgtDFnflag = flgtDFnflag == null ? null : flgtDFnflag.trim();
    }

    /**
     * 是否为货机（Y：货机，N：非货机）
     * @return flgt_d_ifsr 是否为货机（Y：货机，N：非货机）
     */
    public String getFlgtDIfsr() {
        return flgtDIfsr;
    }

    /**
     * 是否为货机（Y：货机，N：非货机）
     * @param flgtDIfsr 是否为货机（Y：货机，N：非货机）
     */
    public void setFlgtDIfsr(String flgtDIfsr) {
        this.flgtDIfsr = flgtDIfsr == null ? null : flgtDIfsr.trim();
    }

    /**
     * 本场（Y：本场，N：（空））
     * @return flgt_d_game 本场（Y：本场，N：（空））
     */
    public String getFlgtDGame() {
        return flgtDGame;
    }

    /**
     * 本场（Y：本场，N：（空））
     * @param flgtDGame 本场（Y：本场，N：（空））
     */
    public void setFlgtDGame(String flgtDGame) {
        this.flgtDGame = flgtDGame == null ? null : flgtDGame.trim();
    }

    /**
     * 前飞计划到达时间
     * @return flgt_d_ptax 前飞计划到达时间
     */
    public Date getFlgtDPtax() {
        return flgtDPtax;
    }

    /**
     * 前飞计划到达时间
     * @param flgtDPtax 前飞计划到达时间
     */
    public void setFlgtDPtax(Date flgtDPtax) {
        this.flgtDPtax = flgtDPtax;
    }

    /**
     * 前飞预计到达时间
     * @return flgt_d_etax 前飞预计到达时间
     */
    public Date getFlgtDEtax() {
        return flgtDEtax;
    }

    /**
     * 前飞预计到达时间
     * @param flgtDEtax 前飞预计到达时间
     */
    public void setFlgtDEtax(Date flgtDEtax) {
        this.flgtDEtax = flgtDEtax;
    }

    /**
     * 前飞实际到达时间
     * @return flgt_d_atax 前飞实际到达时间
     */
    public Date getFlgtDAtax() {
        return flgtDAtax;
    }

    /**
     * 前飞实际到达时间
     * @param flgtDAtax 前飞实际到达时间
     */
    public void setFlgtDAtax(Date flgtDAtax) {
        this.flgtDAtax = flgtDAtax;
    }

    /**
     * 航班星标（1：是）
     * @return flgt_starmark 航班星标（1：是）
     */
    public Integer getFlgtStarmark() {
        return flgtStarmark;
    }

    /**
     * 航班星标（1：是）
     * @param flgtStarmark 航班星标（1：是）
     */
    public void setFlgtStarmark(Integer flgtStarmark) {
        this.flgtStarmark = flgtStarmark;
    }

    /**
     * 执行日期
     * @return flgt_exedate 执行日期
     */
    public Date getFlgtExedate() {
        return flgtExedate;
    }

    /**
     * 执行日期
     * @param flgtExedate 执行日期
     */
    public void setFlgtExedate(Date flgtExedate) {
        this.flgtExedate = flgtExedate;
    }

	public Integer getFlg() {
		return flg;
	}

	public void setFlg(Integer flg) {
		this.flg = flg;
	}
    
}