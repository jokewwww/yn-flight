package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 航班表和任务表 T_FLIGHT和T_TASK
 */
public class MyFlightTask implements Serializable {

    
	/**
	 * T_FLIGHT
	 */
	private static final long serialVersionUID = 1L;
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
	 * 预计到达时间
	 */
	private Date flgtAEtot;

	/**
	 * 实际到达时间（或称落地时间）
	 */
	private Date flgtAAtot;

	/**
	 * 实际到达时间（或称落地时间）
	 */
	private Date dFlgtAAtot;

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
	 * 进港航班任务属性
	 */
	private String  flgtMissionPropIn;

	/**
	 * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
	 */
	private String flgtNature;

	/**
	 * 航班性质细分
	 */
	private String flgtSubNature;

	/**
	 * 关联计划起飞时间
	 * 
	 */
	private String flgtLinkDStot;
	/**
	 * 关联预计起飞时间
	 * 
	 */
	private String flgtLinkDEtot;
	/**
	 * 关联实际起飞时间
	 */
	private String flgtLinkDAtot;
	/**
	 * 关联进出港
	 * 
	 */
	private String flgtLinkAdid;

	/**
	 * 关联计划到达时间
	 * 
	 */
	private String flgtLinkAStot;
	/**
	 * 关联预计到达时间
	 * 
	 */
	private String flgtLinkAEtot;
	/**
	 * 关联实际到达时间
	 * 
	 */
	private String flgtLinkAAtot;
	/**
	 * 航班订阅状态
	 * 
	 */
	private String flgtstatus;
	/**
	 * 出发机场名全称
	 * 
	 */
	private String apcdSAirportName;
	/**
	 * 目的地机场全称
	 * 
	 */
	private String apcdEAirportName;
	/**
	 * 出发机场名简称
	 * 
	 */
	private String apcdSAirportNames;
	/**
	 * 目的地机场简称
	 * 
	 */
	private String apcdEAirportNames;
	/**
	 * 航空公司全称
	 * 
	 */
	private String alcdArlnName;
	/**
	 * 航空公司简称
	 * 
	 */
	private String alcdArlnNames;
	/**
	 * 航线简称
	 * 
	 */
	private String flightValic;
	/**
	 * 进港航班机位
	 * 
	 */
	private String flgtPlacecodeIn;

	/**
	 * 业务字段 备注
	 */
	private String flgtbezu;
	
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
	 * 上轮档时间
	 */
	private Date flgtChocksIn;

	/**
	 * 撤轮挡时间
	 */
	private Date flgtChocksOut;

	/**
	 * 第一件行李时间
	 */
	private Date flgtFirstLugg;

	/**
	 * 最后一件行李时间
	 */
	private Date flgtLastLugg;

	/**
	 * 前飞计划到达时间
	 */
	private Date flgtPtax;

	/**
	 * 前飞预计到达时间
	 */
	private Date flgtEtax;

	/**
	 * 前飞实际到达时间
	 */
	private Date flgtAtax;

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
     * 连接航班日期
     */
    private Date flgtLinkFlop;
    
    /**
     * 连接航班连接次数
     */
    private Integer flgtLinkRepeat;
    
    /**
     * 航班连接次数
     */
    private Integer flgtRepeat;
    
    /**
     * 航班航段
     */
    private String flgtOtc;
    /**
     * 进港航班国际国内
     */
    private String flgtFltiIn;

	/**
	 * T_TASK
	 * 
	 * 
	 * 任务ID（uuid）
	 */
	private String taskId;

    /**
     * 取消的任务ID
     */
	private String recallTaskId;

	/**
	 * 加油员员工ID
	 */
	private String taskOpeStaffId;

	/**
	 * 加油员员工名称
	 */
	private String taskOpeStaffName;

	/**
	 * 任务内容（0：加油，1：抽油）
	 */
	private Integer taskContent;

	/**
	 * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
	 */
	private Integer taskStatus;

	/**
	 * 任务派发时间
	 */
	private Date taskAsgTime;

	/**
	 * 任务接受时间
	 */
	private Date taskAccTime;

	/**
	 * 加油开始时间
	 */
	private Date taskChagStaTime;

	/**
	 * 打印油单完成时间
	 */
	private Date taskRcPrintTime;
	
	/**
	 * 打印油单完成时间
	 */
	public Date getTaskRcPrintTime() {
		return taskRcPrintTime;
	}

	/**
	 * 打印油单完成时间
	 */
	public void setTaskRcPrintTime(Date taskRcPrintTime) {
		this.taskRcPrintTime = taskRcPrintTime;
	}

	/**
     * 加油到位时间
     */
    private Date taskArriveTime;
	
	/**
	 * 加油完成时间
	 */
	private Date taskChagEndTime;

	/**
	 * 任务完成时间
	 */
	private Date taskDoneTime;

	/**
	 * 加油单编号
	 */
	private String taskFuelRecptNo;

	/**
	 * 加油车编号
	 */
	private String taskVehiNo;

	/**
	 * 创建人员工ID
	 */
	private String taskCreStaffId;

	/**
	 * 创建人员工名称
	 */
	private String taskCreStaffName;

	/**
	 * 航班序号
	 */
	private Integer flgtNum;

	/**
	 * 任务星标（1：是，0：不是）默认0
	 */
	private Integer taskStarmark;

	/**
     * 航班合并flg （0 否 1 是）
     */
    private Integer flgtShareNoFlg;
	
	/**
	 * 记录创建时间
	 */
	private Date taskRecCreTime;

    /**
     * 起飞油量
     */
    private Integer taskTakeoffFuel;

    /**
     * 轮挡油量
     */
    private Integer taskChockFuel;

    /**
     * 应加油量
     */
    private Integer taskTotalFuel;

	/**
	 * 预加油量
	 */
	private Integer flgtOtatFuel;

    /**
     * 仪表类型（KG，LB）
     */
    private String taskMeterType;

    /**
     * 中央邮箱油量
     */
    private Integer taskCentTank;

    /**
     * 左机翼油箱油量
     */
    private Integer taskLeftTank;

    /**
     * 右机翼油箱油量
     */
    private Integer taskRightTank;

    /**
     * 机组签名（JPG图片的base64编码）
     */
    private String taskCrewSign;
    
    /**
     * 业务字段（油单类型）
     */
    private Integer flrcType;

    private String remark;

    private String aFlgtFtyp;

	/**
	 * 购买方国家
	 */
    private String cstmRegion;
	/**
	 * 购货方编号
	 */
	private String cstmNum;

	/**
	 * 航班日期（航班执行日期）
	 */
	private String flgtFlopString;

    /**
     * 航班是否有闹钟（0：没有，1：有）
     */
    private Integer flgtAlarm;

	private Integer flgtRegnStatus;
	private Integer flgtPlacecodeStatus;

	private String flgtAErrftyp;

	private String flgtDErrftyp;

	private String arcrCustomNum;
    /**
     * 信用评级
     */
    private String cilvl;

    /**
     * 信用状态：0-正常（默认）；1-提示（低风险）；2-警示（高风险）
     */
    private String citst;

    /**
     * 信用的描述信息
     */
    private String cirmk;

	//订单号
	private String orderNo;
	//供油服务模式（五种模式之一）：备注信息：0-机坪加注，1-自提，2-油品配送，3-野外保障加油，4-自助
	private String addoilType;


	private String  flgtOtat; //Y-机组已经确认；N-机组未确认。

	//flgt_is_order
	private Integer flgtIsOrder;

	private String flgtOlvr;

	// '保税B 非保税 FB'
	private String flrcBwtar;

	private Integer fuelPubType; // 油料类型 1 单 2 双 3 航气


	private String flgtOldRegn;

	private Integer flgtRegnChangeStatus;

	private String flgtOil;

	private Integer flgtStatus;

	private String flgtHangTask;

    private Date flgtUpdateTime;

//flgt_olvr_isupdate
	private String flgtOlvrIsupdate;

    // 机障信息
    List<TFlightTrouble> tFlightTroubles;

	public String getFlgtOlvrIsupdate() {
		return flgtOlvrIsupdate;
	}

	public void setFlgtOlvrIsupdate(String flgtOlvrIsupdate) {
		this.flgtOlvrIsupdate = flgtOlvrIsupdate;
	}

	public String getRecallTaskId() {
        return recallTaskId;
    }

    public void setRecallTaskId(String recallTaskId) {
        this.recallTaskId = recallTaskId;
    }

    public Date getFlgtUpdateTime() {
        return flgtUpdateTime;
    }

    public void setFlgtUpdateTime(Date flgtUpdateTime) {
        this.flgtUpdateTime = flgtUpdateTime;
    }

    public String getCilvl() {
        return cilvl;
    }

    public void setCilvl(String cilvl) {
        this.cilvl = cilvl;
    }

    public String getCitst() {
        return citst;
    }

    public void setCitst(String citst) {
        this.citst = citst;
    }

    public String getCirmk() {
        return cirmk;
    }

    public void setCirmk(String cirmk) {
        this.cirmk = cirmk;
    }

    public String getFlgtHangTask() {
		return flgtHangTask;
	}

	public void setFlgtHangTask(String flgtHangTask) {
		this.flgtHangTask = flgtHangTask;
	}

	public Integer getFlgtStatus() {
		return flgtStatus;
	}

	public void setFlgtStatus(Integer flgtStatus) {
		this.flgtStatus = flgtStatus;
	}

	public String getFlgtOil() {
		return flgtOil;
	}

	public void setFlgtOil(String flgtOil) {
		this.flgtOil = flgtOil;
	}

	public String getFlgtOldRegn() {
		return flgtOldRegn;
	}

	public void setFlgtOldRegn(String flgtOldRegn) {
		this.flgtOldRegn = flgtOldRegn;
	}

	public Integer getFlgtRegnChangeStatus() {
		return flgtRegnChangeStatus;
	}

	public void setFlgtRegnChangeStatus(Integer flgtRegnChangeStatus) {
		this.flgtRegnChangeStatus = flgtRegnChangeStatus;
	}

	public Integer getFuelPubType() {
		return fuelPubType;
	}

	public void setFuelPubType(Integer fuelPubType) {
		this.fuelPubType = fuelPubType;
	}

	public String getFlrcBwtar() {
		return flrcBwtar;
	}

	public void setFlrcBwtar(String flrcBwtar) {
		this.flrcBwtar = flrcBwtar;
	}

	public String getFlgtFlopString() {
		return flgtFlopString;
	}

	public void setFlgtFlopString(String flgtFlopString) {
		this.flgtFlopString = flgtFlopString;
	}

	public String getFlgtOlvr() {
		return flgtOlvr;
	}

	public void setFlgtOlvr(String flgtOlvr) {
		this.flgtOlvr = flgtOlvr;
	}

	public String getFlgtOtat() {
		return flgtOtat;
	}

	public void setFlgtOtat(String flgtOtat) {
		this.flgtOtat = flgtOtat;
	}

	public Integer getFlgtIsOrder() {
		return flgtIsOrder;
	}

	public void setFlgtIsOrder(Integer flgtIsOrder) {
		this.flgtIsOrder = flgtIsOrder;
	}

	public String getAddoilType() {
		return addoilType;
	}

	public void setAddoilType(String addoilType) {
		this.addoilType = addoilType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getaFlgtFtyp() {
		return aFlgtFtyp;
	}

	public void setaFlgtFtyp(String aFlgtFtyp) {
		this.aFlgtFtyp = aFlgtFtyp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 业务字段（油单类型）
	 */
	public Integer getFlrcType() {
		return flrcType;
	}

	public String getArcrCustomNum() {
		return arcrCustomNum;
	}

	public void setArcrCustomNum(String arcrCustomNum) {
		this.arcrCustomNum = arcrCustomNum;
	}

	public String getFlgtAErrftyp() {
		return flgtAErrftyp;
	}

	public void setFlgtAErrftyp(String flgtAErrftyp) {
		this.flgtAErrftyp = flgtAErrftyp;
	}

	public String getFlgtDErrftyp() {
		return flgtDErrftyp;
	}

	public void setFlgtDErrftyp(String flgtDErrftyp) {
		this.flgtDErrftyp = flgtDErrftyp;
	}

	public Date getdFlgtAAtot() {
		return dFlgtAAtot;
	}

	public void setdFlgtAAtot(Date dFlgtAAtot) {
		this.dFlgtAAtot = dFlgtAAtot;
	}

	public Integer getFlgtRegnStatus() {
		return flgtRegnStatus;
	}

	public void setFlgtRegnStatus(Integer flgtRegnStatus) {
		this.flgtRegnStatus = flgtRegnStatus;
	}

	public Integer getFlgtPlacecodeStatus() {
		return flgtPlacecodeStatus;
	}

	public void setFlgtPlacecodeStatus(Integer flgtPlacecodeStatus) {
		this.flgtPlacecodeStatus = flgtPlacecodeStatus;
	}

	/**
     * 航班是否有闹钟（0：没有，1：有）
     */
    public Integer getFlgtAlarm() {
		return flgtAlarm;
	}

    /**
     * 航班是否有闹钟（0：没有，1：有）
     */
	public void setFlgtAlarm(Integer flgtAlarm) {
		this.flgtAlarm = flgtAlarm;
	}
    
    /**
     * 业务字段（油单类型）
     */
	public void setFlrcType(Integer flrcType) {
		this.flrcType = flrcType;
	}
	
	/**
	 * 创建人员工名称
	 */
	public String getTaskCreStaffName() {
		return taskCreStaffName;
	}

	/**
	 * 创建人员工名称
	 */
	public void setTaskCreStaffName(String taskCreStaffName) {
		this.taskCreStaffName = taskCreStaffName;
	}

	/**
	 * 航班日期（航班执行日期）
	 * 
	 * @return flgt_flop 航班日期（航班执行日期）
	 */
	public Date getFlgtFlop() {
		return flgtFlop;
	}

	/**
	 * 航班日期（航班执行日期）
	 * 
	 * @param flgtFlop
	 *            航班日期（航班执行日期）
	 */
	public void setFlgtFlop(Date flgtFlop) {
		this.flgtFlop = flgtFlop;
	}

	/**
	 * 计划到达时间
	 * 
	 * @return flgt_a_stot 计划到达时间
	 */
	public Date getFlgtAStot() {
		return flgtAStot;
	}

	/**
	 * 计划到达时间
	 * 
	 * @param flgtAStot
	 *            计划到达时间
	 */
	public void setFlgtAStot(Date flgtAStot) {
		this.flgtAStot = flgtAStot;
	}

	/**
	 * 预计到达时间
	 * 
	 * @return flgt_a_etot 预计到达时间
	 */
	public Date getFlgtAEtot() {
		return flgtAEtot;
	}

	/**
	 * 预计到达时间
	 * 
	 * @param flgtAEtot
	 *            预计到达时间
	 */
	public void setFlgtAEtot(Date flgtAEtot) {
		this.flgtAEtot = flgtAEtot;
	}

	/**
	 * 实际到达时间（或称落地时间）
	 * 
	 * @return flgt_a_atot 实际到达时间（或称落地时间）
	 */
	public Date getFlgtAAtot() {
		return flgtAAtot;
	}

	/**
	 * 实际到达时间（或称落地时间）
	 * 
	 * @param flgtAAtot
	 *            实际到达时间（或称落地时间）
	 */
	public void setFlgtAAtot(Date flgtAAtot) {
		this.flgtAAtot = flgtAAtot;
	}

	/**
	 * 计划起飞时间
	 * 
	 * @return flgt_d_stot 计划起飞时间
	 */
	public Date getFlgtDStot() {
		return flgtDStot;
	}

	/**
	 * 计划起飞时间
	 * 
	 * @param flgtDStot
	 *            计划起飞时间
	 */
	public void setFlgtDStot(Date flgtDStot) {
		this.flgtDStot = flgtDStot;
	}

	/**
	 * 预计起飞时间
	 * 
	 * @return flgt_d_etot 预计起飞时间
	 */
	public Date getFlgtDEtot() {
		return flgtDEtot;
	}

	/**
	 * 预计起飞时间
	 * 
	 * @param flgtDEtot
	 *            预计起飞时间
	 */
	public void setFlgtDEtot(Date flgtDEtot) {
		this.flgtDEtot = flgtDEtot;
	}

	/**
	 * 实际起飞时间（或称离地时间）
	 * 
	 * @return flgt_d_atot 实际起飞时间（或称离地时间）
	 */
	public Date getFlgtDAtot() {
		return flgtDAtot;
	}

	/**
	 * 实际起飞时间（或称离地时间）
	 * 
	 * @param flgtDAtot
	 *            实际起飞时间（或称离地时间）
	 */
	public void setFlgtDAtot(Date flgtDAtot) {
		this.flgtDAtot = flgtDAtot;
	}

	/**
	 * 上轮档时间
	 * 
	 * @return flgt_chocks_in 上轮档时间
	 */
	public Date getFlgtChocksIn() {
		return flgtChocksIn;
	}

	/**
	 * 上轮档时间
	 * 
	 * @param flgtChocksIn
	 *            上轮档时间
	 */
	public void setFlgtChocksIn(Date flgtChocksIn) {
		this.flgtChocksIn = flgtChocksIn;
	}

	/**
	 * 撤轮挡时间
	 * 
	 * @return flgt_chocks_out 撤轮挡时间
	 */
	public Date getFlgtChocksOut() {
		return flgtChocksOut;
	}

	/**
	 * 撤轮挡时间
	 * 
	 * @param flgtChocksOut
	 *            撤轮挡时间
	 */
	public void setFlgtChocksOut(Date flgtChocksOut) {
		this.flgtChocksOut = flgtChocksOut;
	}

	/**
	 * 要客（人数）
	 * 
	 * @return flgt_vip 要客（人数）
	 */
	public Integer getFlgtVip() {
		return flgtVip;
	}

	/**
	 * 要客（人数）
	 * 
	 * @param flgtVip
	 *            要客（人数）
	 */
	public void setFlgtVip(Integer flgtVip) {
		this.flgtVip = flgtVip;
	}

	/**
	 * T_TASK
	 */

	/**
	 * 任务内容（0：加油，1：抽油）
	 * 
	 * @return task_content 任务内容（0：加油，1：抽油）
	 */
	public Integer getTaskContent() {
		return taskContent;
	}

	/**
	 * 任务内容（0：加油，1：抽油）
	 * 
	 * @param taskContent
	 *            任务内容（0：加油，1：抽油）
	 */
	public void setTaskContent(Integer taskContent) {
		this.taskContent = taskContent;
	}

	/**
	 * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
	 * 
	 * @return task_status
	 *         任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）
	 *         9：不加油）
	 */
	public Integer getTaskStatus() {
		return taskStatus;
	}

	/**
	 * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
	 * 
	 * @param taskStatus
	 *            任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）
	 *            9：不加油）
	 */
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * 任务派发时间
	 * 
	 * @return task_asg_time 任务派发时间
	 */
	public Date getTaskAsgTime() {
		return taskAsgTime;
	}

	/**
	 * 任务派发时间
	 * 
	 * @param taskAsgTime
	 *            任务派发时间
	 */
	public void setTaskAsgTime(Date taskAsgTime) {
		this.taskAsgTime = taskAsgTime;
	}

	/**
	 * 任务接受时间
	 * 
	 * @return task_acc_time 任务接受时间
	 */
	public Date getTaskAccTime() {
		return taskAccTime;
	}

	/**
	 * 任务接受时间
	 * 
	 * @param taskAccTime
	 *            任务接受时间
	 */
	public void setTaskAccTime(Date taskAccTime) {
		this.taskAccTime = taskAccTime;
	}

	/**
	 * 加油开始时间
	 * 
	 * @return task_chag_sta_time 加油开始时间
	 */
	public Date getTaskChagStaTime() {
		return taskChagStaTime;
	}

	/**
	 * 加油开始时间
	 * 
	 * @param taskChagStaTime
	 *            加油开始时间
	 */
	public void setTaskChagStaTime(Date taskChagStaTime) {
		this.taskChagStaTime = taskChagStaTime;
	}

	/**
	 * 加油完成时间
	 * 
	 * @return task_chag_end_time 加油完成时间
	 */
	public Date getTaskChagEndTime() {
		return taskChagEndTime;
	}

	/**
	 * 加油完成时间
	 * 
	 * @param taskChagEndTime
	 *            加油完成时间
	 */
	public void setTaskChagEndTime(Date taskChagEndTime) {
		this.taskChagEndTime = taskChagEndTime;
	}

	/**
	 * 任务完成时间
	 * 
	 * @return task_done_time 任务完成时间
	 */
	public Date getTaskDoneTime() {
		return taskDoneTime;
	}

	/**
	 * 任务完成时间
	 * 
	 * @param taskDoneTime
	 *            任务完成时间
	 */
	public void setTaskDoneTime(Date taskDoneTime) {
		this.taskDoneTime = taskDoneTime;
	}

	/**
	 * 加油单编号
	 * 
	 * @return task_fuel_recpt_no 加油单编号
	 */
	public String getTaskFuelRecptNo() {
		return taskFuelRecptNo;
	}

	/**
	 * 加油单编号
	 * 
	 * @param taskFuelRecptNo
	 *            加油单编号
	 */
	public void setTaskFuelRecptNo(String taskFuelRecptNo) {
		this.taskFuelRecptNo = taskFuelRecptNo;
	}

	/**
	 * 任务星标（1：是，0：不是）默认0
	 * 
	 * @return task_starmark 任务星标（1：是，0：不是）默认0
	 */
	public Integer getTaskStarmark() {
		return taskStarmark;
	}

	/**
	 * 任务星标（1：是，0：不是）默认0
	 * 
	 * @param taskStarmark
	 *            任务星标（1：是，0：不是）默认0
	 */
	public void setTaskStarmark(Integer taskStarmark) {
		this.taskStarmark = taskStarmark;
	}

	/**
	 * 记录创建时间
	 * 
	 * @return task_rec_cre_time 记录创建时间
	 */
	public Date getTaskRecCreTime() {
		return taskRecCreTime;
	}

	/**
	 * 记录创建时间
	 * 
	 * @param taskRecCreTime
	 *            记录创建时间
	 */
	public void setTaskRecCreTime(Date taskRecCreTime) {
		this.taskRecCreTime = taskRecCreTime;
	}

	public String getFlgtTrs3c1() {
		return flgtTrs3c1;
	}

	public void setFlgtTrs3c1(String flgtTrs3c1) {
		this.flgtTrs3c1 = flgtTrs3c1;
	}

	public String getFlgtTrsnm1() {
		return flgtTrsnm1;
	}

	public void setFlgtTrsnm1(String flgtTrsnm1) {
		this.flgtTrsnm1 = flgtTrsnm1;
	}

	public String getFlgtTrs3c2() {
		return flgtTrs3c2;
	}

	public void setFlgtTrs3c2(String flgtTrs3c2) {
		this.flgtTrs3c2 = flgtTrs3c2;
	}

	public String getFlgtTrsnm2() {
		return flgtTrsnm2;
	}

	public void setFlgtTrsnm2(String flgtTrsnm2) {
		this.flgtTrsnm2 = flgtTrsnm2;
	}

	public String getFlgtTrs3c3() {
		return flgtTrs3c3;
	}

	public void setFlgtTrs3c3(String flgtTrs3c3) {
		this.flgtTrs3c3 = flgtTrs3c3;
	}

	public String getFlgtTrsnm3() {
		return flgtTrsnm3;
	}

	public void setFlgtTrsnm3(String flgtTrsnm3) {
		this.flgtTrsnm3 = flgtTrsnm3;
	}

	public String getFlgtTrs3c4() {
		return flgtTrs3c4;
	}

	public void setFlgtTrs3c4(String flgtTrs3c4) {
		this.flgtTrs3c4 = flgtTrs3c4;
	}

	public String getFlgtTrsnm4() {
		return flgtTrsnm4;
	}

	public void setFlgtTrsnm4(String flgtTrsnm4) {
		this.flgtTrsnm4 = flgtTrsnm4;
	}

	public String getFlgtTrs3c5() {
		return flgtTrs3c5;
	}

	public void setFlgtTrs3c5(String flgtTrs3c5) {
		this.flgtTrs3c5 = flgtTrs3c5;
	}

	public String getFlgtTrsnm5() {
		return flgtTrsnm5;
	}

	public void setFlgtTrsnm5(String flgtTrsnm5) {
		this.flgtTrsnm5 = flgtTrsnm5;
	}

	public String getFlgtId() {
		return flgtId;
	}

	public void setFlgtId(String flgtId) {
		this.flgtId = flgtId;
	}

	public String getFlgtFfid() {
		return flgtFfid;
	}

	public void setFlgtFfid(String flgtFfid) {
		this.flgtFfid = flgtFfid;
	}

	public String getFlgtAirportCode() {
		return flgtAirportCode;
	}

	public void setFlgtAirportCode(String flgtAirportCode) {
		this.flgtAirportCode = flgtAirportCode;
	}

	public String getFlgtAptareaCode() {
		return flgtAptareaCode;
	}

	public void setFlgtAptareaCode(String flgtAptareaCode) {
		this.flgtAptareaCode = flgtAptareaCode;
	}

	public String getFlgtFlno() {
		return flgtFlno;
	}

	public void setFlgtFlno(String flgtFlno) {
		this.flgtFlno = flgtFlno;
	}

	public String getFlgtAcname() {
		return flgtAcname;
	}

	public void setFlgtAcname(String flgtAcname) {
		this.flgtAcname = flgtAcname;
	}

	public String getFlgtRegn() {
		return flgtRegn;
	}

	public void setFlgtRegn(String flgtRegn) {
		this.flgtRegn = flgtRegn;
	}

	public String getFlgtPlacecode() {
		return flgtPlacecode;
	}

	public void setFlgtPlacecode(String flgtPlacecode) {
		this.flgtPlacecode = flgtPlacecode;
	}

	public String getFlgtAl2c() {
		return flgtAl2c;
	}

	public void setFlgtAl2c(String flgtAl2c) {
		this.flgtAl2c = flgtAl2c;
	}

	public String getFlgtAlcname() {
		return flgtAlcname;
	}

	public void setFlgtAlcname(String flgtAlcname) {
		this.flgtAlcname = flgtAlcname;
	}

	public String getFlgtVialc() {
		return flgtVialc;
	}

	public void setFlgtVialc(String flgtVialc) {
		this.flgtVialc = flgtVialc;
	}

	public String getFlgtOrg3c() {
		return flgtOrg3c;
	}

	public void setFlgtOrg3c(String flgtOrg3c) {
		this.flgtOrg3c = flgtOrg3c;
	}

	public String getFlgtOrgnm() {
		return flgtOrgnm;
	}

	public void setFlgtOrgnm(String flgtOrgnm) {
		this.flgtOrgnm = flgtOrgnm;
	}

	public String getFlgtDes3c() {
		return flgtDes3c;
	}

	public void setFlgtDes3c(String flgtDes3c) {
		this.flgtDes3c = flgtDes3c;
	}

	public String getFlgtDesnm() {
		return flgtDesnm;
	}

	public void setFlgtDesnm(String flgtDesnm) {
		this.flgtDesnm = flgtDesnm;
	}

	public String getFlgtAdid() {
		return flgtAdid;
	}

	public void setFlgtAdid(String flgtAdid) {
		this.flgtAdid = flgtAdid;
	}

	public String getFlgtFlti() {
		return flgtFlti;
	}

	public void setFlgtFlti(String flgtFlti) {
		this.flgtFlti = flgtFlti;
	}

	public String getFlgtFtyp() {
		return flgtFtyp;
	}

	public void setFlgtFtyp(String flgtFtyp) {
		this.flgtFtyp = flgtFtyp;
	}

	public String getFlgtProxy() {
		return flgtProxy;
	}

	public void setFlgtProxy(String flgtProxy) {
		this.flgtProxy = flgtProxy;
	}

	public String getFlgtLinkFlno() {
		return flgtLinkFlno;
	}

	public void setFlgtLinkFlno(String flgtLinkFlno) {
		this.flgtLinkFlno = flgtLinkFlno;
	}

	public String getFlgtFnflag() {
		return flgtFnflag;
	}

	public void setFlgtFnflag(String flgtFnflag) {
		this.flgtFnflag = flgtFnflag;
	}

	public String getFlgtGame() {
		return flgtGame;
	}

	public void setFlgtGame(String flgtGame) {
		this.flgtGame = flgtGame;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskVehiNo() {
		return taskVehiNo;
	}

	public void setTaskVehiNo(String taskVehiNo) {
		this.taskVehiNo = taskVehiNo;
	}

	public String getTaskCreStaffId() {
		return taskCreStaffId;
	}

	public void setTaskCreStaffId(String taskCreStaffId) {
		this.taskCreStaffId = taskCreStaffId;
	}

	/**
	 * @return the flgtNum
	 */
	public Integer getFlgtNum() {
		return flgtNum;
	}

	/**
	 * @param flgtNum the flgtNum to set
	 */
	public void setFlgtNum(Integer flgtNum) {
		this.flgtNum = flgtNum;
	}
	/**
	 * @return the flgtbezu
	 */
	public String getFlgtbezu() {
		return flgtbezu;
	}

	/**
	 * @param flgtbezu the flgtbezu to set
	 */
	public void setFlgtbezu(String flgtbezu) {
		this.flgtbezu = flgtbezu;
	}

	public String getFlgtPlacecodeIn() {
		return flgtPlacecodeIn;
	}

	/**
	 * @param flgtPlacecodeIn
	 *            the flgtPlacecodeIn to set
	 */
	public void setFlgtPlacecodeIn(String flgtPlacecodeIn) {
		this.flgtPlacecodeIn = flgtPlacecodeIn;
	}

	public String getFlightValic() {
		return flightValic;
	}

	public void setFlightValic(String flightValic) {
		this.flightValic = flightValic;
	}

	public String getApcdSAirportName() {
		return apcdSAirportName;
	}

	public void setApcdSAirportName(String apcdSAirportName) {
		this.apcdSAirportName = apcdSAirportName;
	}

	public String getApcdEAirportName() {
		return apcdEAirportName;
	}

	public void setApcdEAirportName(String apcdEAirportName) {
		this.apcdEAirportName = apcdEAirportName;
	}

	public String getApcdSAirportNames() {
		return apcdSAirportNames;
	}

	public void setApcdSAirportNames(String apcdSAirportNames) {
		this.apcdSAirportNames = apcdSAirportNames;
	}

	public String getApcdEAirportNames() {
		return apcdEAirportNames;
	}

	public void setApcdEAirportNames(String apcdEAirportNames) {
		this.apcdEAirportNames = apcdEAirportNames;
	}

	public String getAlcdArlnName() {
		return alcdArlnName;
	}

	public void setAlcdArlnName(String alcdArlnName) {
		this.alcdArlnName = alcdArlnName;
	}

	public String getAlcdArlnNames() {
		return alcdArlnNames;
	}

	public void setAlcdArlnNames(String alcdArlnNames) {
		this.alcdArlnNames = alcdArlnNames;
	}

	public String getFlgtstatus() {
		return flgtstatus;
	}

	public void setFlgtstatus(String flgtstatus) {
		this.flgtstatus = flgtstatus;
	}

	public String getFlgtLinkAStot() {
		return flgtLinkAStot;
	}

	public void setFlgtLinkAStot(String flgtLinkAStot) {
		this.flgtLinkAStot = flgtLinkAStot;
	}

	public String getFlgtLinkAEtot() {
		return flgtLinkAEtot;
	}

	public void setFlgtLinkAEtot(String flgtLinkAEtot) {
		this.flgtLinkAEtot = flgtLinkAEtot;
	}

	public String getFlgtLinkAAtot() {
		return flgtLinkAAtot;
	}

	public void setFlgtLinkAAtot(String flgtLinkAAtot) {
		this.flgtLinkAAtot = flgtLinkAAtot;
	}

	public String getFlgtLinkDStot() {
		return flgtLinkDStot;
	}

	public void setFlgtLinkDStot(String flgtLinkDStot) {
		this.flgtLinkDStot = flgtLinkDStot;
	}

	public String getFlgtLinkDEtot() {
		return flgtLinkDEtot;
	}

	public void setFlgtLinkDEtot(String flgtLinkDEtot) {
		this.flgtLinkDEtot = flgtLinkDEtot;
	}

	public String getFlgtLinkDAtot() {
		return flgtLinkDAtot;
	}

	public void setFlgtLinkDAtot(String flgtLinkDAtot) {
		this.flgtLinkDAtot = flgtLinkDAtot;
	}

	public String getFlgtLinkAdid() {
		return flgtLinkAdid;
	}

	public void setFlgtLinkAdid(String flgtLinkAdid) {
		this.flgtLinkAdid = flgtLinkAdid;
	}

	public String getFlgtMissionProp() {
		return flgtMissionProp;
	}

	public void setFlgtMissionProp(String flgtMissionProp) {
		this.flgtMissionProp = flgtMissionProp;
	}

	public String getFlgtMissionPropIn() {
		return flgtMissionPropIn;
	}

	public void setFlgtMissionPropIn(String flgtMissionPropIn) {
		this.flgtMissionPropIn = flgtMissionPropIn;
	}

	public String getFlgtNature() {
		return flgtNature;
	}

	public void setFlgtNature(String flgtNature) {
		this.flgtNature = flgtNature;
	}

	public String getFlgtSubNature() {
		return flgtSubNature;
	}

	public void setFlgtSubNature(String flgtSubNature) {
		this.flgtSubNature = flgtSubNature;
	}

	public String getFlgtDelaycode() {
		return flgtDelaycode;
	}

	public void setFlgtDelaycode(String flgtDelaycode) {
		this.flgtDelaycode = flgtDelaycode;
	}

	public String getFlgtIfsr() {
		return flgtIfsr;
	}

	public void setFlgtIfsr(String flgtIfsr) {
		this.flgtIfsr = flgtIfsr;
	}

	public String getFlgtDepRunway() {
		return flgtDepRunway;
	}

	public void setFlgtDepRunway(String flgtDepRunway) {
		this.flgtDepRunway = flgtDepRunway;
	}

	public String getFlgtArrRunway() {
		return flgtArrRunway;
	}

	public void setFlgtArrRunway(String flgtArrRunway) {
		this.flgtArrRunway = flgtArrRunway;
	}

	public String getFlgtGate() {
		return flgtGate;
	}

	public void setFlgtGate(String flgtGate) {
		this.flgtGate = flgtGate;
	}

	public Date getFlgtCrewInPlace() {
		return flgtCrewInPlace;
	}

	public void setFlgtCrewInPlace(Date flgtCrewInPlace) {
		this.flgtCrewInPlace = flgtCrewInPlace;
	}

	public Date getFlgtFirstLugg() {
		return flgtFirstLugg;
	}

	public void setFlgtFirstLugg(Date flgtFirstLugg) {
		this.flgtFirstLugg = flgtFirstLugg;
	}

	public Date getFlgtLastLugg() {
		return flgtLastLugg;
	}

	public void setFlgtLastLugg(Date flgtLastLugg) {
		this.flgtLastLugg = flgtLastLugg;
	}

	public Date getFlgtPtax() {
		return flgtPtax;
	}

	public void setFlgtPtax(Date flgtPtax) {
		this.flgtPtax = flgtPtax;
	}

	public Date getFlgtEtax() {
		return flgtEtax;
	}

	public void setFlgtEtax(Date flgtEtax) {
		this.flgtEtax = flgtEtax;
	}

	public Date getFlgtAtax() {
		return flgtAtax;
	}

	public void setFlgtAtax(Date flgtAtax) {
		this.flgtAtax = flgtAtax;
	}

	public Integer getFlgtTakeoffFuel() {
		return flgtTakeoffFuel;
	}

	public void setFlgtTakeoffFuel(Integer flgtTakeoffFuel) {
		this.flgtTakeoffFuel = flgtTakeoffFuel;
	}

	public Integer getFlgtChockFuel() {
		return flgtChockFuel;
	}

	public void setFlgtChockFuel(Integer flgtChockFuel) {
		this.flgtChockFuel = flgtChockFuel;
	}

	public Integer getFlgtTaskAsign() {
		return flgtTaskAsign;
	}

	public void setFlgtTaskAsign(Integer flgtTaskAsign) {
		this.flgtTaskAsign = flgtTaskAsign;
	}

	public Integer getFlgtStarmark() {
		return flgtStarmark;
	}

	public void setFlgtStarmark(Integer flgtStarmark) {
		this.flgtStarmark = flgtStarmark;
	}

	public Integer getFlgtManualFlg() {
		return flgtManualFlg;
	}

	public void setFlgtManualFlg(Integer flgtManualFlg) {
		this.flgtManualFlg = flgtManualFlg;
	}
	/**
	 * 加油员员工名称
	 */
	public String getTaskOpeStaffName() {
		return taskOpeStaffName;
	}

	/**
	 * 加油员员工名称
	 */
	public void setTaskOpeStaffName(String taskOpeStaffName) {
		this.taskOpeStaffName = taskOpeStaffName;
	}

	/**
	 * 加油员员工ID
	 */
	public String getTaskOpeStaffId() {
		return taskOpeStaffId;
	}

	/**
	 * 加油员员工ID
	 */
	public void setTaskOpeStaffId(String taskOpeStaffId) {
		this.taskOpeStaffId = taskOpeStaffId;
	}

	/**
	 * @return the flgtLinkFlop
	 */
	public Date getFlgtLinkFlop() {
		return flgtLinkFlop;
	}

	/**
	 * @param flgtLinkFlop the flgtLinkFlop to set
	 */
	public void setFlgtLinkFlop(Date flgtLinkFlop) {
		this.flgtLinkFlop = flgtLinkFlop;
	}

	/**
	 * @return the flgtLinkRepeat
	 */
	public Integer getFlgtLinkRepeat() {
		return flgtLinkRepeat;
	}

	/**
	 * @param flgtLinkRepeat the flgtLinkRepeat to set
	 */
	public void setFlgtLinkRepeat(Integer flgtLinkRepeat) {
		this.flgtLinkRepeat = flgtLinkRepeat;
	}

	/**
	 * @return the flgtRepeat
	 */
	public Integer getFlgtRepeat() {
		return flgtRepeat;
	}

	/**
	 * @param flgtRepeat the flgtRepeat to set
	 */
	public void setFlgtRepeat(Integer flgtRepeat) {
		this.flgtRepeat = flgtRepeat;
	}

	/**
	 * @return the flgtOtc
	 */
	public String getFlgtOtc() {
		return flgtOtc;
	}

	/**
	 * @param flgtOtc the flgtOtc to set
	 */
	public void setFlgtOtc(String flgtOtc) {
		this.flgtOtc = flgtOtc;
	}

	/**
	 * @return the flgtFltiIn
	 */
	public String getFlgtFltiIn() {
		return flgtFltiIn;
	}

	/**
	 * @param flgtFltiIn the flgtFltiIn to set
	 */
	public void setFlgtFltiIn(String flgtFltiIn) {
		this.flgtFltiIn = flgtFltiIn;
	}
	
	/**
	 * @return the flgtShareNoFlg
	 */
	public Integer getFlgtShareNoFlg() {
		return flgtShareNoFlg;
	}

	/**
	 * @param flgtShareNoFlg the flgtShareNoFlg to set
	 */
	public void setFlgtShareNoFlg(Integer flgtShareNoFlg) {
		this.flgtShareNoFlg = flgtShareNoFlg;
	}
	
	/**
     * 起飞油量
     * @return task_takeoff_fuel 起飞油量
     */
    public Integer getTaskTakeoffFuel() {
        return taskTakeoffFuel;
    }

    /**
     * 起飞油量
     * @param taskTakeoffFuel 起飞油量
     */
    public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
        this.taskTakeoffFuel = taskTakeoffFuel;
    }
    
    /**
     * 轮挡油量
     * @return task_chock_fuel 轮挡油量
     */
    public Integer getTaskChockFuel() {
        return taskChockFuel;
    }

    /**
     * 轮挡油量
     * @param taskChockFuel 轮挡油量
     */
    public void setTaskChockFuel(Integer taskChockFuel) {
        this.taskChockFuel = taskChockFuel;
    }

    /**
     * 应加油量
     * @return task_total_fuel 应加油量
     */
    public Integer getTaskTotalFuel() {
        return taskTotalFuel;
    }

    /**
     * 应加油量
     * @param taskTotalFuel 应加油量
     */
    public void setTaskTotalFuel(Integer taskTotalFuel) {
        this.taskTotalFuel = taskTotalFuel;
    }

    /**
     * 仪表类型（KG，LB）
     * @return task_meter_type 仪表类型（KG，LB）
     */
    public String getTaskMeterType() {
        return taskMeterType;
    }

    /**
     * 仪表类型（KG，LB）
     * @param taskMeterType 仪表类型（KG，LB）
     */
    public void setTaskMeterType(String taskMeterType) {
        this.taskMeterType = taskMeterType == null ? null : taskMeterType.trim();
    }

    /**
     * 中央邮箱油量
     * @return task_cent_tank 中央邮箱油量
     */
    public Integer getTaskCentTank() {
        return taskCentTank;
    }

    /**
     * 中央邮箱油量
     * @param taskCentTank 中央邮箱油量
     */
    public void setTaskCentTank(Integer taskCentTank) {
        this.taskCentTank = taskCentTank;
    }

    /**
     * 左机翼油箱油量
     * @return task_left_tank 左机翼油箱油量
     */
    public Integer getTaskLeftTank() {
        return taskLeftTank;
    }

    /**
     * 左机翼油箱油量
     * @param taskLeftTank 左机翼油箱油量
     */
    public void setTaskLeftTank(Integer taskLeftTank) {
        this.taskLeftTank = taskLeftTank;
    }

    /**
     * 右机翼油箱油量
     * @return task_right_tank 右机翼油箱油量
     */
    public Integer getTaskRightTank() {
        return taskRightTank;
    }

    /**
     * 右机翼油箱油量
     * @param taskRightTank 右机翼油箱油量
     */
    public void setTaskRightTank(Integer taskRightTank) {
        this.taskRightTank = taskRightTank;
    }

    /**
     * 机组签名（JPG图片的base64编码）
     * @return task_crew_sign 机组签名（JPG图片的base64编码）
     */
    public String getTaskCrewSign() {
        return taskCrewSign;
    }

    /**
     * 机组签名（JPG图片的base64编码）
     * @param taskCrewSign 机组签名（JPG图片的base64编码）
     */
    public void setTaskCrewSign(String taskCrewSign) {
        this.taskCrewSign = taskCrewSign == null ? null : taskCrewSign.trim();
    }
    
    /**
     * 加油到位时间
     * @return task_arrive_time 加油到位时间
     */
    public Date getTaskArriveTime() {
        return taskArriveTime;
    }

    /**
     * 加油到位时间
     * @param taskArriveTime 加油到位时间
     */
    public void setTaskArriveTime(Date taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }

	public String getCstmRegion() {
		return cstmRegion;
	}

	public void setCstmRegion(String cstmRegion) {
		this.cstmRegion = cstmRegion;
	}

	public String getCstmNum() {
		return cstmNum;
	}

	public void setCstmNum(String cstmNum) {
		this.cstmNum = cstmNum;
	}

	public Integer getFlgtOtatFuel() {
		return flgtOtatFuel;
	}

	public void setFlgtOtatFuel(Integer flgtOtatFuel) {
		this.flgtOtatFuel = flgtOtatFuel;
	}

    public List<TFlightTrouble> gettFlightTroubles() {
        return tFlightTroubles;
    }

    public void settFlightTroubles(List<TFlightTrouble> tFlightTroubles) {
        this.tFlightTroubles = tFlightTroubles;
    }
}