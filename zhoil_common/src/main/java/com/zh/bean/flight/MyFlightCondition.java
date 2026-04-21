package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 * 航班表（条件检索）
 * T_FLIGHT
 */
public class MyFlightCondition implements Serializable {
    /**
     * 航班ID（uuid）
     */
    private String flgtId;

    /**
     * 航班唯一标识码（对接各航班接口的唯一标识码）
     */
    private String flgtFfid;

    /**
     * 所属机场代码
     */
    private String flgtAirportCode;

    /**
     * 所属机场区域代码
     */
    private String flgtAptareaCode;

    /**
     * 航班号
     */
    private String flgtFlno;

    /**
     * 航班日期（航班执行日期）
     */
    private Date flgtFlop;
    
    /**
     * 航班日期开始时间（航班执行日期）
     */
    private Date flgtFlopStart;
    
    /**
     * 航班日期结束时间（航班执行日期）
     */
    private Date flgtFlopEnd;
    
    /**
     * 航班日期开始时间（航班执行日期）
     */
    public Date getFlgtFlopStart() {
		return flgtFlopStart;
	}

    /**
     * 航班日期开始时间（航班执行日期）
     */
	public void setFlgtFlopStart(Date flgtFlopStart) {
		this.flgtFlopStart = flgtFlopStart;
	}

	/**
     * 航班日期结束时间（航班执行日期）
     */
	public Date getFlgtFlopEnd() {
		return flgtFlopEnd;
	}

	/**
     * 航班日期结束时间（航班执行日期）
     */
	public void setFlgtFlopEnd(Date flgtFlopEnd) {
		this.flgtFlopEnd = flgtFlopEnd;
	}

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
     * 航线（中文地名-中文地名（-中文地名））
     */
    private String flgtVialc;

    /**
     * 计划到达时间
     */
    private Date flgtAStot;
    
    /**
     * 计划到达时间开始时间
     */
    private Date flgtAStotStart;
    
    /**
     * 计划到达时间结束时间
     */
    private Date flgtAStotEnd;

    /**
     * 计划到达时间开始时间
     */
    public Date getFlgtAStotStart() {
		return flgtAStotStart;
	}

    /**
     * 计划到达时间开始时间
     */
	public void setFlgtAStotStart(Date flgtAStotStart) {
		this.flgtAStotStart = flgtAStotStart;
	}

	/**
     * 计划到达时间结束时间
     */
	public Date getFlgtAStotEnd() {
		return flgtAStotEnd;
	}

	/**
     * 计划到达时间结束时间
     */
	public void setFlgtAStotEnd(Date flgtAStotEnd) {
		this.flgtAStotEnd = flgtAStotEnd;
	}

	/**
     * 预计到达时间
     */
    private Date flgtAEtot;
    
    /**
     * 预计到达时间开始时间
     */
    private Date flgtAEtotStart;
    
    /**
     * 预计到达时间结束时间
     */
    private Date flgtAEtotEnd;

    /**
     * 预计到达时间开始时间
     */
	public Date getFlgtAEtotStart() {
		return flgtAEtotStart;
	}

	/**
     * 预计到达时间开始时间
     */
	public void setFlgtAEtotStart(Date flgtAEtotStart) {
		this.flgtAEtotStart = flgtAEtotStart;
	}

	/**
     * 预计到达时间结束时间
     */
	public Date getFlgtAEtotEnd() {
		return flgtAEtotEnd;
	}

	/**
     * 预计到达时间结束时间
     */
	public void setFlgtAEtotEnd(Date flgtAEtotEnd) {
		this.flgtAEtotEnd = flgtAEtotEnd;
	}

	/**
     * 实际到达时间（或称落地时间）
     */
    private Date flgtAAtot;
    
    /**
     * 实际到达时间（或称落地时间）开始
     */
    private Date flgtAAtotStart;
    
    /**
     * 实际到达时间（或称落地时间）结束
     */
    private Date flgtAAtotEnd;

    /**
     * 实际到达时间（或称落地时间）开始
     */
    public Date getFlgtAAtotStart() {
		return flgtAAtotStart;
	}

    /**
     * 实际到达时间（或称落地时间）开始
     */
	public void setFlgtAAtotStart(Date flgtAAtotStart) {
		this.flgtAAtotStart = flgtAAtotStart;
	}

	/**
     * 实际到达时间（或称落地时间）结束
     */
	public Date getFlgtAAtotEnd() {
		return flgtAAtotEnd;
	}

	/**
     * 实际到达时间（或称落地时间）结束
     */
	public void setFlgtAAtotEnd(Date flgtAAtotEnd) {
		this.flgtAAtotEnd = flgtAAtotEnd;
	}
	
    /**
     * 计划起飞时间
     */
    private Date flgtDStot;
    
    /**
     * 计划起飞时间开始
     */
    private Date flgtDStotStart;
    
    /**
     * 计划起飞时间结束
     */
    private Date flgtDStotEnd;

    /**
     * 计划起飞时间开始
     */
    public Date getFlgtDStotStart() {
		return flgtDStotStart;
	}

    /**
     * 计划起飞时间开始
     */
	public void setFlgtDStotStart(Date flgtDStotStart) {
		this.flgtDStotStart = flgtDStotStart;
	}

	/**
     * 计划起飞时间结束
     */
	public Date getFlgtDStotEnd() {
		return flgtDStotEnd;
	}

	/**
     * 计划起飞时间结束
     */
	public void setFlgtDStotEnd(Date flgtDStotEnd) {
		this.flgtDStotEnd = flgtDStotEnd;
	}

	/**
     * 预计起飞时间
     */
    private Date flgtDEtot;
    
    /**
     * 预计起飞时间开始
     */
    private Date flgtDEtotStart;
    
    /**
     * 预计起飞时间结束
     */
    private Date flgtDEtotEnd;

    /**
     * 预计起飞时间开始
     */
    public Date getFlgtDEtotStart() {
		return flgtDEtotStart;
	}

    /**
     * 预计起飞时间开始
     */
	public void setFlgtDEtotStart(Date flgtDEtotStart) {
		this.flgtDEtotStart = flgtDEtotStart;
	}

	/**
     * 预计起飞时间结束
     */
	public Date getFlgtDEtotEnd() {
		return flgtDEtotEnd;
	}

	/**
     * 预计起飞时间结束
     */
	public void setFlgtDEtotEnd(Date flgtDEtotEnd) {
		this.flgtDEtotEnd = flgtDEtotEnd;
	}

	/**
     * 实际起飞时间（或称离地时间）
     */
    private Date flgtDAtot;
    
    /**
     * 实际起飞时间（或称离地时间）开始
     */
    private Date flgtDAtotStart;
    
    /**
     * 实际起飞时间（或称离地时间）结束
     */
    private Date flgtDAtotEnd;

    /**
     * 实际起飞时间（或称离地时间）开始
     */
    public Date getFlgtDAtotStart() {
		return flgtDAtotStart;
	}

    /**
     * 实际起飞时间（或称离地时间）开始
     */
	public void setFlgtDAtotStart(Date flgtDAtotStart) {
		this.flgtDAtotStart = flgtDAtotStart;
	}

	/**
     * 实际起飞时间（或称离地时间）结束
     */
	public Date getFlgtDAtotEnd() {
		return flgtDAtotEnd;
	}

	/**
     * 实际起飞时间（或称离地时间）结束
     */
	public void setFlgtDAtotEnd(Date flgtDAtotEnd) {
		this.flgtDAtotEnd = flgtDAtotEnd;
	}

	/**
     * 出发地机场三字码
     */
    private String flgtOrg3c;

    /**
     * 出发地机场
     */
    private String flgtOrgnm;

    /**
     * 经停机场三字码1
     */
    private String flgtTrs3c1;

    /**
     * 经停机场1
     */
    private String flgtTrsnm1;

    /**
     * 经停机场三字码2
     */
    private String flgtTrs3c2;

    /**
     * 经停机场2
     */
    private String flgtTrsnm2;

    /**
     * 经停机场三字码3
     */
    private String flgtTrs3c3;

    /**
     * 经停机场3
     */
    private String flgtTrsnm3;

    /**
     * 经停机场三字码4
     */
    private String flgtTrs3c4;

    /**
     * 经停机场4
     */
    private String flgtTrsnm4;

    /**
     * 经停机场三字码5
     */
    private String flgtTrs3c5;

    /**
     * 经停机场5
     */
    private String flgtTrsnm5;

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
     * 航班任务属性
     */
    private String flgtMissionProp;

    /**
     * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     */
    private String flgtNature;

    /**
     * 航班性质细分
     */
    private String flgtSubNature;

    /**
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    private String flgtFlti;

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    private String flgtFtyp;

    /**
     * 延误原因
     */
    private String flgtDelaycode;

    /**
     * 航空服务代理（YAG（机场代理），CES（东航代理））
     */
    private String flgtProxy;

    /**
     * 连接航班号（共享航班号1，共享航班号2）
     */
    private String flgtLinkFlno;

    /**
     * 远近机位（N：近机位，F：远机位）
     */
    private String flgtFnflag;

    /**
     * 是否为货机（Y：货机，N：非货机）
     */
    private String flgtIfsr;

    /**
     * 本场（Y：本场，N：（空））
     */
    private String flgtGame;

    /**
     * 离港跑道
     */
    private String flgtDepRunway;

    /**
     * 到港跑道
     */
    private String flgtArrRunway;

    /**
     * 登机门
     */
    private String flgtGate;

    /**
     * 机组到位时间
     */
    private Date flgtCrewInPlace;
    
    /**
     * 机组到位时间开始
     */
    private Date flgtCrewInPlaceStart;
    
    /**
     * 机组到位时间结束
     */
    private Date flgtCrewInPlaceEnd;

    /**
     * 机组到位时间开始
     */
    public Date getFlgtCrewInPlaceStart() {
		return flgtCrewInPlaceStart;
	}

    /**
     * 机组到位时间开始
     */
	public void setFlgtCrewInPlaceStart(Date flgtCrewInPlaceStart) {
		this.flgtCrewInPlaceStart = flgtCrewInPlaceStart;
	}

	/**
     * 机组到位时间结束
     */
	public Date getFlgtCrewInPlaceEnd() {
		return flgtCrewInPlaceEnd;
	}

	/**
     * 机组到位时间结束
     */
	public void setFlgtCrewInPlaceEnd(Date flgtCrewInPlaceEnd) {
		this.flgtCrewInPlaceEnd = flgtCrewInPlaceEnd;
	}

	/**
     * 上轮档时间
     */
    private Date flgtChocksIn;
    
    /**
     * 上轮档时间开始
     */
    private Date flgtChocksInStart;
    
    /**
     * 上轮档时间结束
     */
    private Date flgtChocksInEnd;

    /**
     * 上轮档时间开始
     */
    public Date getFlgtChocksInStart() {
		return flgtChocksInStart;
	}

    /**
     * 上轮档时间开始
     */
	public void setFlgtChocksInStart(Date flgtChocksInStart) {
		this.flgtChocksInStart = flgtChocksInStart;
	}

	/**
     * 上轮档时间结束
     */
	public Date getFlgtChocksInEnd() {
		return flgtChocksInEnd;
	}

	/**
     * 上轮档时间结束
     */
	public void setFlgtChocksInEnd(Date flgtChocksInEnd) {
		this.flgtChocksInEnd = flgtChocksInEnd;
	}

	/**
     * 撤轮挡时间
     */
    private Date flgtChocksOut;
    
    /**
     * 撤轮挡时间开始
     */
    private Date flgtChocksOutStart;
    
    /**
     * 撤轮挡时间结束
     */
    private Date flgtChocksOutEnd;

    /**
     * 撤轮挡时间开始
     */
    public Date getFlgtChocksOutStart() {
		return flgtChocksOutStart;
	}

    /**
     * 撤轮挡时间开始
     */
	public void setFlgtChocksOutStart(Date flgtChocksOutStart) {
		this.flgtChocksOutStart = flgtChocksOutStart;
	}

	/**
     * 撤轮挡时间结束
     */
	public Date getFlgtChocksOutEnd() {
		return flgtChocksOutEnd;
	}

	/**
     * 撤轮挡时间结束
     */
	public void setFlgtChocksOutEnd(Date flgtChocksOutEnd) {
		this.flgtChocksOutEnd = flgtChocksOutEnd;
	}

	/**
     * 第一件行李时间
     */
    private Date flgtFirstLugg;
    
    /**
     * 第一件行李时间开始
     */
    private Date flgtFirstLuggStart;
    
    /**
     * 第一件行李时间结束
     */
    private Date flgtFirstLuggEnd;

    /**
     * 第一件行李时间开始
     */
    public Date getFlgtFirstLuggStart() {
		return flgtFirstLuggStart;
	}

    /**
     * 第一件行李时间开始
     */
	public void setFlgtFirstLuggStart(Date flgtFirstLuggStart) {
		this.flgtFirstLuggStart = flgtFirstLuggStart;
	}

	/**
     * 第一件行李时间结束
     */
	public Date getFlgtFirstLuggEnd() {
		return flgtFirstLuggEnd;
	}

	/**
     * 第一件行李时间结束
     */
	public void setFlgtFirstLuggEnd(Date flgtFirstLuggEnd) {
		this.flgtFirstLuggEnd = flgtFirstLuggEnd;
	}

	/**
     * 最后一件行李时间
     */
    private Date flgtLastLugg;
    
    /**
     * 最后一件行李时间开始
     */
    private Date flgtLastLuggStart;
    
    /**
     * 最后一件行李时间结束
     */
    private Date flgtLastLuggEnd;

    /**
     * 最后一件行李时间开始
     */
    public Date getFlgtLastLuggStart() {
		return flgtLastLuggStart;
	}

    /**
     * 最后一件行李时间开始
     */
	public void setFlgtLastLuggStart(Date flgtLastLuggStart) {
		this.flgtLastLuggStart = flgtLastLuggStart;
	}

	/**
     * 最后一件行李时间结束
     */
	public Date getFlgtLastLuggEnd() {
		return flgtLastLuggEnd;
	}

	/**
     * 最后一件行李时间结束
     */
	public void setFlgtLastLuggEnd(Date flgtLastLuggEnd) {
		this.flgtLastLuggEnd = flgtLastLuggEnd;
	}

	/**
     * 前飞计划到达时间
     */
    private Date flgtPtax;
    
    /**
     * 前飞计划到达时间开始
     */
    private Date flgtPtaxStart;
    
    /**
     * 前飞计划到达时间结束
     */
    private Date flgtPtaxEnd;

    /**
     * 前飞计划到达时间开始
     */
    public Date getFlgtPtaxStart() {
		return flgtPtaxStart;
	}

    /**
     * 前飞计划到达时间开始
     */
	public void setFlgtPtaxStart(Date flgtPtaxStart) {
		this.flgtPtaxStart = flgtPtaxStart;
	}

	/**
     * 前飞计划到达时间结束
     */
	public Date getFlgtPtaxEnd() {
		return flgtPtaxEnd;
	}

	/**
     * 前飞计划到达时间结束
     */
	public void setFlgtPtaxEnd(Date flgtPtaxEnd) {
		this.flgtPtaxEnd = flgtPtaxEnd;
	}

	/**
     * 前飞预计到达时间
     */
    private Date flgtEtax;
    
    /**
     * 前飞预计到达时间开始
     */
    private Date flgtEtaxStart;
    
    /**
     * 前飞预计到达时间结束
     */
    private Date flgtEtaxEnd;

    /**
     * 前飞预计到达时间开始
     */
    public Date getFlgtEtaxStart() {
		return flgtEtaxStart;
	}

    /**
     * 前飞预计到达时间开始
     */
	public void setFlgtEtaxStart(Date flgtEtaxStart) {
		this.flgtEtaxStart = flgtEtaxStart;
	}

	/**
     * 前飞预计到达时间结束
     */
	public Date getFlgtEtaxEnd() {
		return flgtEtaxEnd;
	}

	/**
     * 前飞预计到达时间结束
     */
	public void setFlgtEtaxEnd(Date flgtEtaxEnd) {
		this.flgtEtaxEnd = flgtEtaxEnd;
	}

	/**
     * 前飞实际到达时间
     */
    private Date flgtAtax;
    
    /**
     * 前飞实际到达时间开始
     */
    private Date flgtAtaxStart;
    
    /**
     * 前飞实际到达时间结束
     */
    private Date flgtAtaxEnd;

    /**
     * 前飞实际到达时间开始
     */
    public Date getFlgtAtaxStart() {
		return flgtAtaxStart;
	}

    /**
     * 前飞实际到达时间开始
     */
	public void setFlgtAtaxStart(Date flgtAtaxStart) {
		this.flgtAtaxStart = flgtAtaxStart;
	}

	/**
     * 前飞实际到达时间结束
     */
	public Date getFlgtAtaxEnd() {
		return flgtAtaxEnd;
	}

	/**
     * 前飞实际到达时间结束
     */
	public void setFlgtAtaxEnd(Date flgtAtaxEnd) {
		this.flgtAtaxEnd = flgtAtaxEnd;
	}

	/**
     * 起飞油量
     */
    private Integer flgtTakeoffFuel;

    /**
     * 轮挡油量
     */
    private Integer flgtChockFuel;

    /**
     * 要客（人数）
     */
    private Integer flgtVip;

    /**
     * 任务下发标识（0：未下发，1：已下发）
     */
    private Integer flgtTaskAsign;
    
    /**
     * 航班星标（1：是，0：不是）默认0
     */
    private Integer flgtStarmark;
    
    /**
     * 手动修改航班（1：手动，0：非手动）
     */
    private Integer flgtManualFlg;
    
    /**
     * T_FLIGHT
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
     * 航班唯一标识码（对接各航班接口的唯一标识码）
     * @return flgt_ffid 航班唯一标识码（对接各航班接口的唯一标识码）
     */
    public String getFlgtFfid() {
        return flgtFfid;
    }

    /**
     * 航班唯一标识码（对接各航班接口的唯一标识码）
     * @param flgtFfid 航班唯一标识码（对接各航班接口的唯一标识码）
     */
    public void setFlgtFfid(String flgtFfid) {
        this.flgtFfid = flgtFfid == null ? null : flgtFfid.trim();
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
     * 所属机场区域代码
     * @return flgt_aptarea_code 所属机场区域代码
     */
    public String getFlgtAptareaCode() {
        return flgtAptareaCode;
    }

    /**
     * 所属机场区域代码
     * @param flgtAptareaCode 所属机场区域代码
     */
    public void setFlgtAptareaCode(String flgtAptareaCode) {
        this.flgtAptareaCode = flgtAptareaCode == null ? null : flgtAptareaCode.trim();
    }

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
     * 航班日期（航班执行日期）
     * @return flgt_flop 航班日期（航班执行日期）
     */
    public Date getFlgtFlop() {
        return flgtFlop;
    }

    /**
     * 航班日期（航班执行日期）
     * @param flgtFlop 航班日期（航班执行日期）
     */
    public void setFlgtFlop(Date flgtFlop) {
        this.flgtFlop = flgtFlop;
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
     * 航线（中文地名-中文地名（-中文地名））
     * @return flgt_vialc 航线（中文地名-中文地名（-中文地名））
     */
    public String getFlgtVialc() {
        return flgtVialc;
    }

    /**
     * 航线（中文地名-中文地名（-中文地名））
     * @param flgtVialc 航线（中文地名-中文地名（-中文地名））
     */
    public void setFlgtVialc(String flgtVialc) {
        this.flgtVialc = flgtVialc == null ? null : flgtVialc.trim();
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
     * 经停机场三字码1
     * @return flgt_trs3c1 经停机场三字码1
     */
    public String getFlgtTrs3c1() {
        return flgtTrs3c1;
    }

    /**
     * 经停机场三字码1
     * @param flgtTrs3c1 经停机场三字码1
     */
    public void setFlgtTrs3c1(String flgtTrs3c1) {
        this.flgtTrs3c1 = flgtTrs3c1 == null ? null : flgtTrs3c1.trim();
    }

    /**
     * 经停机场1
     * @return flgt_trsnm1 经停机场1
     */
    public String getFlgtTrsnm1() {
        return flgtTrsnm1;
    }

    /**
     * 经停机场1
     * @param flgtTrsnm1 经停机场1
     */
    public void setFlgtTrsnm1(String flgtTrsnm1) {
        this.flgtTrsnm1 = flgtTrsnm1 == null ? null : flgtTrsnm1.trim();
    }

    /**
     * 经停机场三字码2
     * @return flgt_trs3c2 经停机场三字码2
     */
    public String getFlgtTrs3c2() {
        return flgtTrs3c2;
    }

    /**
     * 经停机场三字码2
     * @param flgtTrs3c2 经停机场三字码2
     */
    public void setFlgtTrs3c2(String flgtTrs3c2) {
        this.flgtTrs3c2 = flgtTrs3c2 == null ? null : flgtTrs3c2.trim();
    }

    /**
     * 经停机场2
     * @return flgt_trsnm2 经停机场2
     */
    public String getFlgtTrsnm2() {
        return flgtTrsnm2;
    }

    /**
     * 经停机场2
     * @param flgtTrsnm2 经停机场2
     */
    public void setFlgtTrsnm2(String flgtTrsnm2) {
        this.flgtTrsnm2 = flgtTrsnm2 == null ? null : flgtTrsnm2.trim();
    }

    /**
     * 经停机场三字码3
     * @return flgt_trs3c3 经停机场三字码3
     */
    public String getFlgtTrs3c3() {
        return flgtTrs3c3;
    }

    /**
     * 经停机场三字码3
     * @param flgtTrs3c3 经停机场三字码3
     */
    public void setFlgtTrs3c3(String flgtTrs3c3) {
        this.flgtTrs3c3 = flgtTrs3c3 == null ? null : flgtTrs3c3.trim();
    }

    /**
     * 经停机场3
     * @return flgt_trsnm3 经停机场3
     */
    public String getFlgtTrsnm3() {
        return flgtTrsnm3;
    }

    /**
     * 经停机场3
     * @param flgtTrsnm3 经停机场3
     */
    public void setFlgtTrsnm3(String flgtTrsnm3) {
        this.flgtTrsnm3 = flgtTrsnm3 == null ? null : flgtTrsnm3.trim();
    }

    /**
     * 经停机场三字码4
     * @return flgt_trs3c4 经停机场三字码4
     */
    public String getFlgtTrs3c4() {
        return flgtTrs3c4;
    }

    /**
     * 经停机场三字码4
     * @param flgtTrs3c4 经停机场三字码4
     */
    public void setFlgtTrs3c4(String flgtTrs3c4) {
        this.flgtTrs3c4 = flgtTrs3c4 == null ? null : flgtTrs3c4.trim();
    }

    /**
     * 经停机场4
     * @return flgt_trsnm4 经停机场4
     */
    public String getFlgtTrsnm4() {
        return flgtTrsnm4;
    }

    /**
     * 经停机场4
     * @param flgtTrsnm4 经停机场4
     */
    public void setFlgtTrsnm4(String flgtTrsnm4) {
        this.flgtTrsnm4 = flgtTrsnm4 == null ? null : flgtTrsnm4.trim();
    }

    /**
     * 经停机场三字码5
     * @return flgt_trs3c5 经停机场三字码5
     */
    public String getFlgtTrs3c5() {
        return flgtTrs3c5;
    }

    /**
     * 经停机场三字码5
     * @param flgtTrs3c5 经停机场三字码5
     */
    public void setFlgtTrs3c5(String flgtTrs3c5) {
        this.flgtTrs3c5 = flgtTrs3c5 == null ? null : flgtTrs3c5.trim();
    }

    /**
     * 经停机场5
     * @return flgt_trsnm5 经停机场5
     */
    public String getFlgtTrsnm5() {
        return flgtTrsnm5;
    }

    /**
     * 经停机场5
     * @param flgtTrsnm5 经停机场5
     */
    public void setFlgtTrsnm5(String flgtTrsnm5) {
        this.flgtTrsnm5 = flgtTrsnm5 == null ? null : flgtTrsnm5.trim();
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
     * 航班任务属性
     * @return flgt_mission_prop 航班任务属性
     */
    public String getFlgtMissionProp() {
        return flgtMissionProp;
    }

    /**
     * 航班任务属性
     * @param flgtMissionProp 航班任务属性
     */
    public void setFlgtMissionProp(String flgtMissionProp) {
        this.flgtMissionProp = flgtMissionProp == null ? null : flgtMissionProp.trim();
    }

    /**
     * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     * @return flgt_nature 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     */
    public String getFlgtNature() {
        return flgtNature;
    }

    /**
     * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     * @param flgtNature 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     */
    public void setFlgtNature(String flgtNature) {
        this.flgtNature = flgtNature == null ? null : flgtNature.trim();
    }

    /**
     * 航班性质细分
     * @return flgt_sub_nature 航班性质细分
     */
    public String getFlgtSubNature() {
        return flgtSubNature;
    }

    /**
     * 航班性质细分
     * @param flgtSubNature 航班性质细分
     */
    public void setFlgtSubNature(String flgtSubNature) {
        this.flgtSubNature = flgtSubNature == null ? null : flgtSubNature.trim();
    }

    /**
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @return flgt_flti 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public String getFlgtFlti() {
        return flgtFlti;
    }

    /**
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     * @param flgtFlti 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    public void setFlgtFlti(String flgtFlti) {
        this.flgtFlti = flgtFlti == null ? null : flgtFlti.trim();
    }

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @return flgt_ftyp 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public String getFlgtFtyp() {
        return flgtFtyp;
    }

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     * @param flgtFtyp 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    public void setFlgtFtyp(String flgtFtyp) {
        this.flgtFtyp = flgtFtyp == null ? null : flgtFtyp.trim();
    }

    /**
     * 延误原因
     * @return flgt_delaycode 延误原因
     */
    public String getFlgtDelaycode() {
        return flgtDelaycode;
    }

    /**
     * 延误原因
     * @param flgtDelaycode 延误原因
     */
    public void setFlgtDelaycode(String flgtDelaycode) {
        this.flgtDelaycode = flgtDelaycode == null ? null : flgtDelaycode.trim();
    }

    /**
     * 航空服务代理（YAG（机场代理），CES（东航代理））
     * @return flgt_proxy 航空服务代理（YAG（机场代理），CES（东航代理））
     */
    public String getFlgtProxy() {
        return flgtProxy;
    }

    /**
     * 航空服务代理（YAG（机场代理），CES（东航代理））
     * @param flgtProxy 航空服务代理（YAG（机场代理），CES（东航代理））
     */
    public void setFlgtProxy(String flgtProxy) {
        this.flgtProxy = flgtProxy == null ? null : flgtProxy.trim();
    }

    /**
     * 连接航班号（共享航班号1，共享航班号2）
     * @return flgt_link_flno 连接航班号（共享航班号1，共享航班号2）
     */
    public String getFlgtLinkFlno() {
        return flgtLinkFlno;
    }

    /**
     * 连接航班号（共享航班号1，共享航班号2）
     * @param flgtLinkFlno 连接航班号（共享航班号1，共享航班号2）
     */
    public void setFlgtLinkFlno(String flgtLinkFlno) {
        this.flgtLinkFlno = flgtLinkFlno == null ? null : flgtLinkFlno.trim();
    }

    /**
     * 远近机位（N：近机位，F：远机位）
     * @return flgt_fnflag 远近机位（N：近机位，F：远机位）
     */
    public String getFlgtFnflag() {
        return flgtFnflag;
    }

    /**
     * 远近机位（N：近机位，F：远机位）
     * @param flgtFnflag 远近机位（N：近机位，F：远机位）
     */
    public void setFlgtFnflag(String flgtFnflag) {
        this.flgtFnflag = flgtFnflag == null ? null : flgtFnflag.trim();
    }

    /**
     * 是否为货机（Y：货机，N：非货机）
     * @return flgt_ifsr 是否为货机（Y：货机，N：非货机）
     */
    public String getFlgtIfsr() {
        return flgtIfsr;
    }

    /**
     * 是否为货机（Y：货机，N：非货机）
     * @param flgtIfsr 是否为货机（Y：货机，N：非货机）
     */
    public void setFlgtIfsr(String flgtIfsr) {
        this.flgtIfsr = flgtIfsr == null ? null : flgtIfsr.trim();
    }

    /**
     * 本场（Y：本场，N：（空））
     * @return flgt_game 本场（Y：本场，N：（空））
     */
    public String getFlgtGame() {
        return flgtGame;
    }

    /**
     * 本场（Y：本场，N：（空））
     * @param flgtGame 本场（Y：本场，N：（空））
     */
    public void setFlgtGame(String flgtGame) {
        this.flgtGame = flgtGame == null ? null : flgtGame.trim();
    }

    /**
     * 离港跑道
     * @return flgt_dep_runway 离港跑道
     */
    public String getFlgtDepRunway() {
        return flgtDepRunway;
    }

    /**
     * 离港跑道
     * @param flgtDepRunway 离港跑道
     */
    public void setFlgtDepRunway(String flgtDepRunway) {
        this.flgtDepRunway = flgtDepRunway == null ? null : flgtDepRunway.trim();
    }

    /**
     * 到港跑道
     * @return flgt_arr_runway 到港跑道
     */
    public String getFlgtArrRunway() {
        return flgtArrRunway;
    }

    /**
     * 到港跑道
     * @param flgtArrRunway 到港跑道
     */
    public void setFlgtArrRunway(String flgtArrRunway) {
        this.flgtArrRunway = flgtArrRunway == null ? null : flgtArrRunway.trim();
    }

    /**
     * 登机门
     * @return flgt_gate 登机门
     */
    public String getFlgtGate() {
        return flgtGate;
    }

    /**
     * 登机门
     * @param flgtGate 登机门
     */
    public void setFlgtGate(String flgtGate) {
        this.flgtGate = flgtGate == null ? null : flgtGate.trim();
    }

    /**
     * 机组到位时间
     * @return flgt_crew_in_place 机组到位时间
     */
    public Date getFlgtCrewInPlace() {
        return flgtCrewInPlace;
    }

    /**
     * 机组到位时间
     * @param flgtCrewInPlace 机组到位时间
     */
    public void setFlgtCrewInPlace(Date flgtCrewInPlace) {
        this.flgtCrewInPlace = flgtCrewInPlace;
    }

    /**
     * 上轮档时间
     * @return flgt_chocks_in 上轮档时间
     */
    public Date getFlgtChocksIn() {
        return flgtChocksIn;
    }

    /**
     * 上轮档时间
     * @param flgtChocksIn 上轮档时间
     */
    public void setFlgtChocksIn(Date flgtChocksIn) {
        this.flgtChocksIn = flgtChocksIn;
    }

    /**
     * 撤轮挡时间
     * @return flgt_chocks_out 撤轮挡时间
     */
    public Date getFlgtChocksOut() {
        return flgtChocksOut;
    }

    /**
     * 撤轮挡时间
     * @param flgtChocksOut 撤轮挡时间
     */
    public void setFlgtChocksOut(Date flgtChocksOut) {
        this.flgtChocksOut = flgtChocksOut;
    }

    /**
     * 第一件行李时间
     * @return flgt_first_lugg 第一件行李时间
     */
    public Date getFlgtFirstLugg() {
        return flgtFirstLugg;
    }

    /**
     * 第一件行李时间
     * @param flgtFirstLugg 第一件行李时间
     */
    public void setFlgtFirstLugg(Date flgtFirstLugg) {
        this.flgtFirstLugg = flgtFirstLugg;
    }

    /**
     * 最后一件行李时间
     * @return flgt_last_lugg 最后一件行李时间
     */
    public Date getFlgtLastLugg() {
        return flgtLastLugg;
    }

    /**
     * 最后一件行李时间
     * @param flgtLastLugg 最后一件行李时间
     */
    public void setFlgtLastLugg(Date flgtLastLugg) {
        this.flgtLastLugg = flgtLastLugg;
    }

    /**
     * 前飞计划到达时间
     * @return flgt_ptax 前飞计划到达时间
     */
    public Date getFlgtPtax() {
        return flgtPtax;
    }

    /**
     * 前飞计划到达时间
     * @param flgtPtax 前飞计划到达时间
     */
    public void setFlgtPtax(Date flgtPtax) {
        this.flgtPtax = flgtPtax;
    }

    /**
     * 前飞预计到达时间
     * @return flgt_etax 前飞预计到达时间
     */
    public Date getFlgtEtax() {
        return flgtEtax;
    }

    /**
     * 前飞预计到达时间
     * @param flgtEtax 前飞预计到达时间
     */
    public void setFlgtEtax(Date flgtEtax) {
        this.flgtEtax = flgtEtax;
    }

    /**
     * 前飞实际到达时间
     * @return flgt_atax 前飞实际到达时间
     */
    public Date getFlgtAtax() {
        return flgtAtax;
    }

    /**
     * 前飞实际到达时间
     * @param flgtAtax 前飞实际到达时间
     */
    public void setFlgtAtax(Date flgtAtax) {
        this.flgtAtax = flgtAtax;
    }

    /**
     * 起飞油量
     * @return flgt_takeoff_fuel 起飞油量
     */
    public Integer getFlgtTakeoffFuel() {
        return flgtTakeoffFuel;
    }

    /**
     * 起飞油量
     * @param flgtTakeoffFuel 起飞油量
     */
    public void setFlgtTakeoffFuel(Integer flgtTakeoffFuel) {
        this.flgtTakeoffFuel = flgtTakeoffFuel;
    }

    /**
     * 轮挡油量
     * @return flgt_chock_fuel 轮挡油量
     */
    public Integer getFlgtChockFuel() {
        return flgtChockFuel;
    }

    /**
     * 轮挡油量
     * @param flgtChockFuel 轮挡油量
     */
    public void setFlgtChockFuel(Integer flgtChockFuel) {
        this.flgtChockFuel = flgtChockFuel;
    }

    /**
     * 要客（人数）
     * @return flgt_vip 要客（人数）
     */
    public Integer getFlgtVip() {
        return flgtVip;
    }

    /**
     * 要客（人数）
     * @param flgtVip 要客（人数）
     */
    public void setFlgtVip(Integer flgtVip) {
        this.flgtVip = flgtVip;
    }

    /**
     * 任务下发标识（0：未下发，1：已下发）
     * @return flgt_task_asign 任务下发标识（0：未下发，1：已下发）
     */
    public Integer getFlgtTaskAsign() {
        return flgtTaskAsign;
    }

    /**
     * 任务下发标识（0：未下发，1：已下发）
     * @param flgtTaskAsign 任务下发标识（0：未下发，1：已下发）
     */
    public void setFlgtTaskAsign(Integer flgtTaskAsign) {
        this.flgtTaskAsign = flgtTaskAsign;
    }

    /**
     * 航班星标（1：是，0：不是）默认0
     * @return flgt_starmark 航班星标（1：是，0：不是）默认0
     */
    public Integer getFlgtStarmark() {
        return flgtStarmark;
    }

    /**
     * 航班星标（1：是，0：不是）默认0
     * @param flgtStarmark 航班星标（1：是，0：不是）默认0
     */
    public void setFlgtStarmark(Integer flgtStarmark) {
        this.flgtStarmark = flgtStarmark;
    }
    
    /**
     * 手动修改航班（1：手动，0：非手动）
     * @return flgt_manual_flg 手动修改航班（1：手动，0：非手动）
     */
    public Integer getFlgtManualFlg() {
        return flgtManualFlg;
    }

    /**
     * 手动修改航班（1：手动，0：非手动）
     * @param flgtManualFlg 手动修改航班（1：手动，0：非手动）
     */
    public void setFlgtManualFlg(Integer flgtManualFlg) {
        this.flgtManualFlg = flgtManualFlg;
    }
}