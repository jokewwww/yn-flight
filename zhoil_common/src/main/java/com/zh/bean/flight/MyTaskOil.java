package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

public class MyTaskOil implements Serializable {

	/**
	 * 任务油单表
	 */
	private static final long serialVersionUID = 1L;

	/**
     * T_FLIGHT
     * 
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
     * 经停备降机场三字码
     */
    private String flgtTrs3c1;

    /**
     * 经停备降机场
     */
    private String flgtTrsnm1;
    /**
     * 经停备降机场三字码
     */
    private String flgtTrs3c2;
    
    /**
     * 经停备降机场
     */
    private String flgtTrsnm2;
    /**
     * 经停备降机场三字码
     */
    private String flgtTrs3c3;
    
    /**
     * 经停备降机场
     */
    private String flgtTrsnm3;
    /**
     * 经停备降机场三字码
     */
    private String flgtTrs3c4;
    
    /**
     * 经停备降机场
     */
    private String flgtTrsnm4;
    /**
     * 经停备降机场三字码
     */
    private String flgtTrs3c5;
    
    /**
     * 经停备降机场
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
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    private String flgtFlti;

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    private String flgtFtyp;

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
     * 本场（Y：本场，N：（空））
     */
    private String flgtGame;

    /**
     * 上轮档时间
     */
    private Date flgtChocksIn;

    /**
     * 撤轮挡时间
     */
    private Date flgtChocksOut;

    /**
     * 要客（人数）
     */
    private Integer flgtVip;
    
	/**
	 * 任务表
	 * 
     * 任务ID（uuid）
     */
    private String taskId;

    /**
     * 加油员员工ID
     */
    private String taskOpeStaffId;

    /**
     * 加油员员工名称（业务字段）
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
     * 加油到位时间
     */
    private Date taskArriveTime;
    
	/**
	 * 打印油单完成时间
	 */
	private Date taskRcPrintTime;
    
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
     * 创建人员工名称（业务字段）
     */
    private String taskCreStaffName;
    /**
     * 任务星标（1：是，0：不是）默认0
     */
    private Integer taskStarmark;

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
     * 油单表
     * 
     * 加油单ID（uuid）
     */
    private String flrcId;

    /**
     * 油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
     */
    private Integer flrcType;

    /**
     * 加油单编号
     */
    private String flrcNo;

    /**
     * 加油日期
     */
    private Date flrcDate;

    /**
     * 所属机场代码
     */
    private String flrcAirportCode;

    /**
     * 所属机场区域代码
     */
    private String flrcAptareaCode;

    /**
     * 机场名称（机场名称+“/”+机场IATA三码）
     */
    private String flrcAirport;

    /**
     * 飞机所属单位（航司名称+“/”+航司ICAO二码）
     */
    private String flrcAirlName;

    /**
     * 航班号
     */
    private String flrcFlightNo;

    /**
     * 飞机号码
     */
    private String flrcAircrftNo;

    /**
     * 飞机类型
     */
    private String flrcAircrftType;

    /**
     * 起始（机场名称+“/”+机场IATA三码）
     */
    private String flrcDeparture;

    /**
     * 经停（备降）（机场名称+“/”+机场IATA三码）
     */
    private String flrcTransit;

    /**
     * 终点（机场名称+“/”+机场IATA三码）
     */
    private String flrcDest;

    /**
     * 化验单编号
     */
    private String flrcTestBillNo;

    /**
     * 油品名称
     */
    private String flrcFuelName;

    /**
     * 温度（摄氏度）
     */
    private Double flrcFuelTemp;

    /**
     * 密度（克/毫升（g/cm³））
     */
    private Double flrcFuelDnst;

    /**
     * 体积（升）
     */
    private Double flrcFuelVol;

    /**
     * 计量表开始读数
     */
    private Integer flrcMeterStat;

    /**
     * 计量表结束读数
     */
    private Integer flrcMeterFnsh;

    /**
     * 加油数量小写（升）
     */
    private Integer flrcFiguars;

    /**
     * 加油数量大写（升）
     */
    private String flrcFiguarsWord;

    /**
     * 加油质量（千克）
     */
    private Double flrcQuantity;

    /**
     * 加油车编号
     */
    private String flrcVehiNo;

    /**
     * 地井编号
     */
    private String flrcHydrtPitNo;

    /**
     * 加油开始时间
     */
    private Date flrcStatTime;

    /**
     * 加油结束时间
     */
    private Date flrcFnshTime;

    /**
     * 加油员姓名
     */
    private String flrcDeliverName;

    /**
     * 签名（JPG图片的base64编码）
     */
    private String flrcSign;

    /**
     * 手动修改标记（0：自动生成，1：手动修改）
     */
    private Integer flrcManualTag;

    /**
     * 手动修改人员
     */
    private String flrcManualStaff;

    /**
     * 手动修改时间
     */
    private String flrcManualTime;

    /**
     * 审批状态（0：未审批，1：已审批）
     */
    private Integer flrcRevwStatus;

    /**
     * 油单是否回收（0：未回收，1：已回收）
     */
    private Integer flrcTakebackFlg;

    /**
     * 记录创建时间
     */
    private Date flrcRecCreTime;

    /**
     * T_FUEL_RECPT
     * 
     * 加油单ID（uuid）
     * @return flrc_id 加油单ID（uuid）
     */
    public String getFlrcId() {
        return flrcId;
    }

    /**
     * 加油单ID（uuid）
     * @param flrcId 加油单ID（uuid）
     */
    public void setFlrcId(String flrcId) {
        this.flrcId = flrcId == null ? null : flrcId.trim();
    }

    /**
     * 油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
     * @return flrc_type 油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
     */
    public Integer getFlrcType() {
        return flrcType;
    }

    /**
     * 油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
     * @param flrcType 油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
     */
    public void setFlrcType(Integer flrcType) {
        this.flrcType = flrcType;
    }

    /**
     * 加油单编号
     * @return flrc_no 加油单编号
     */
    public String getFlrcNo() {
        return flrcNo;
    }

    /**
     * 加油单编号
     * @param flrcNo 加油单编号
     */
    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo == null ? null : flrcNo.trim();
    }

    /**
     * 加油日期
     * @return flrc_date 加油日期
     */
    public Date getFlrcDate() {
        return flrcDate;
    }

    /**
     * 加油日期
     * @param flrcDate 加油日期
     */
    public void setFlrcDate(Date flrcDate) {
        this.flrcDate = flrcDate;
    }

    /**
     * 所属机场代码
     * @return flrc_airport_code 所属机场代码
     */
    public String getFlrcAirportCode() {
        return flrcAirportCode;
    }

    /**
     * 所属机场代码
     * @param flrcAirportCode 所属机场代码
     */
    public void setFlrcAirportCode(String flrcAirportCode) {
        this.flrcAirportCode = flrcAirportCode == null ? null : flrcAirportCode.trim();
    }

    /**
     * 所属机场区域代码
     * @return flrc_aptarea_code 所属机场区域代码
     */
    public String getFlrcAptareaCode() {
        return flrcAptareaCode;
    }

    /**
     * 所属机场区域代码
     * @param flrcAptareaCode 所属机场区域代码
     */
    public void setFlrcAptareaCode(String flrcAptareaCode) {
        this.flrcAptareaCode = flrcAptareaCode == null ? null : flrcAptareaCode.trim();
    }

    /**
     * 机场名称（机场名称+“/”+机场IATA三码）
     * @return flrc_airport 机场名称（机场名称+“/”+机场IATA三码）
     */
    public String getFlrcAirport() {
        return flrcAirport;
    }

    /**
     * 机场名称（机场名称+“/”+机场IATA三码）
     * @param flrcAirport 机场名称（机场名称+“/”+机场IATA三码）
     */
    public void setFlrcAirport(String flrcAirport) {
        this.flrcAirport = flrcAirport == null ? null : flrcAirport.trim();
    }

    /**
     * 飞机所属单位（航司名称+“/”+航司ICAO二码）
     * @return flrc_airl_name 飞机所属单位（航司名称+“/”+航司ICAO二码）
     */
    public String getFlrcAirlName() {
        return flrcAirlName;
    }

    /**
     * 飞机所属单位（航司名称+“/”+航司ICAO二码）
     * @param flrcAirlName 飞机所属单位（航司名称+“/”+航司ICAO二码）
     */
    public void setFlrcAirlName(String flrcAirlName) {
        this.flrcAirlName = flrcAirlName == null ? null : flrcAirlName.trim();
    }

    /**
     * 航班号
     * @return flrc_flight_no 航班号
     */
    public String getFlrcFlightNo() {
        return flrcFlightNo;
    }

    /**
     * 航班号
     * @param flrcFlightNo 航班号
     */
    public void setFlrcFlightNo(String flrcFlightNo) {
        this.flrcFlightNo = flrcFlightNo == null ? null : flrcFlightNo.trim();
    }

    /**
     * 飞机号码
     * @return flrc_aircrft_no 飞机号码
     */
    public String getFlrcAircrftNo() {
        return flrcAircrftNo;
    }

    /**
     * 飞机号码
     * @param flrcAircrftNo 飞机号码
     */
    public void setFlrcAircrftNo(String flrcAircrftNo) {
        this.flrcAircrftNo = flrcAircrftNo == null ? null : flrcAircrftNo.trim();
    }

    /**
     * 飞机类型
     * @return flrc_aircrft_type 飞机类型
     */
    public String getFlrcAircrftType() {
        return flrcAircrftType;
    }

    /**
     * 飞机类型
     * @param flrcAircrftType 飞机类型
     */
    public void setFlrcAircrftType(String flrcAircrftType) {
        this.flrcAircrftType = flrcAircrftType == null ? null : flrcAircrftType.trim();
    }

    /**
     * 起始（机场名称+“/”+机场IATA三码）
     * @return flrc_departure 起始（机场名称+“/”+机场IATA三码）
     */
    public String getFlrcDeparture() {
        return flrcDeparture;
    }

    /**
     * 起始（机场名称+“/”+机场IATA三码）
     * @param flrcDeparture 起始（机场名称+“/”+机场IATA三码）
     */
    public void setFlrcDeparture(String flrcDeparture) {
        this.flrcDeparture = flrcDeparture == null ? null : flrcDeparture.trim();
    }

    /**
     * 经停（备降）（机场名称+“/”+机场IATA三码）
     * @return flrc_transit 经停（备降）（机场名称+“/”+机场IATA三码）
     */
    public String getFlrcTransit() {
        return flrcTransit;
    }

    /**
     * 经停（备降）（机场名称+“/”+机场IATA三码）
     * @param flrcTransit 经停（备降）（机场名称+“/”+机场IATA三码）
     */
    public void setFlrcTransit(String flrcTransit) {
        this.flrcTransit = flrcTransit == null ? null : flrcTransit.trim();
    }

    /**
     * 终点（机场名称+“/”+机场IATA三码）
     * @return flrc_dest 终点（机场名称+“/”+机场IATA三码）
     */
    public String getFlrcDest() {
        return flrcDest;
    }

    /**
     * 终点（机场名称+“/”+机场IATA三码）
     * @param flrcDest 终点（机场名称+“/”+机场IATA三码）
     */
    public void setFlrcDest(String flrcDest) {
        this.flrcDest = flrcDest == null ? null : flrcDest.trim();
    }

    /**
     * 化验单编号
     * @return flrc_test_bill_no 化验单编号
     */
    public String getFlrcTestBillNo() {
        return flrcTestBillNo;
    }

    /**
     * 化验单编号
     * @param flrcTestBillNo 化验单编号
     */
    public void setFlrcTestBillNo(String flrcTestBillNo) {
        this.flrcTestBillNo = flrcTestBillNo == null ? null : flrcTestBillNo.trim();
    }

    /**
     * 油品名称
     * @return flrc_fuel_name 油品名称
     */
    public String getFlrcFuelName() {
        return flrcFuelName;
    }

    /**
     * 油品名称
     * @param flrcFuelName 油品名称
     */
    public void setFlrcFuelName(String flrcFuelName) {
        this.flrcFuelName = flrcFuelName == null ? null : flrcFuelName.trim();
    }

    /**
     * 温度（摄氏度）
     * @return flrc_fuel_temp 温度（摄氏度）
     */
    public Double getFlrcFuelTemp() {
        return flrcFuelTemp;
    }

    /**
     * 温度（摄氏度）
     * @param flrcFuelTemp 温度（摄氏度）
     */
    public void setFlrcFuelTemp(Double flrcFuelTemp) {
        this.flrcFuelTemp = flrcFuelTemp;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @return flrc_fuel_dnst 密度（克/毫升（g/cm³））
     */
    public Double getFlrcFuelDnst() {
        return flrcFuelDnst;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @param flrcFuelDnst 密度（克/毫升（g/cm³））
     */
    public void setFlrcFuelDnst(Double flrcFuelDnst) {
        this.flrcFuelDnst = flrcFuelDnst;
    }

    /**
     * 体积（升）
     * @return flrc_fuel_vol 体积（升）
     */
    public Double getFlrcFuelVol() {
        return flrcFuelVol;
    }

    /**
     * 体积（升）
     * @param flrcFuelVol 体积（升）
     */
    public void setFlrcFuelVol(Double flrcFuelVol) {
        this.flrcFuelVol = flrcFuelVol;
    }

    /**
     * 计量表开始读数
     * @return flrc_meter_stat 计量表开始读数
     */
    public Integer getFlrcMeterStat() {
        return flrcMeterStat;
    }

    /**
     * 计量表开始读数
     * @param flrcMeterStat 计量表开始读数
     */
    public void setFlrcMeterStat(Integer flrcMeterStat) {
        this.flrcMeterStat = flrcMeterStat;
    }

    /**
     * 计量表结束读数
     * @return flrc_meter_fnsh 计量表结束读数
     */
    public Integer getFlrcMeterFnsh() {
        return flrcMeterFnsh;
    }

    /**
     * 计量表结束读数
     * @param flrcMeterFnsh 计量表结束读数
     */
    public void setFlrcMeterFnsh(Integer flrcMeterFnsh) {
        this.flrcMeterFnsh = flrcMeterFnsh;
    }

    /**
     * 加油数量小写（升）
     * @return flrc_figuars 加油数量小写（升）
     */
    public Integer getFlrcFiguars() {
        return flrcFiguars;
    }

    /**
     * 加油数量小写（升）
     * @param flrcFiguars 加油数量小写（升）
     */
    public void setFlrcFiguars(Integer flrcFiguars) {
        this.flrcFiguars = flrcFiguars;
    }

    /**
     * 加油数量大写（升）
     * @return flrc_figuars_word 加油数量大写（升）
     */
    public String getFlrcFiguarsWord() {
        return flrcFiguarsWord;
    }

    /**
     * 加油数量大写（升）
     * @param flrcFiguarsWord 加油数量大写（升）
     */
    public void setFlrcFiguarsWord(String flrcFiguarsWord) {
        this.flrcFiguarsWord = flrcFiguarsWord == null ? null : flrcFiguarsWord.trim();
    }

    /**
     * 加油质量（千克）
     * @return flrc_quantity 加油质量（千克）
     */
    public Double getFlrcQuantity() {
        return flrcQuantity;
    }

    /**
     * 加油质量（千克）
     * @param flrcQuantity 加油质量（千克）
     */
    public void setFlrcQuantity(Double flrcQuantity) {
        this.flrcQuantity = flrcQuantity;
    }

    /**
     * 加油车编号
     * @return flrc_vehi_no 加油车编号
     */
    public String getFlrcVehiNo() {
        return flrcVehiNo;
    }

    /**
     * 加油车编号
     * @param flrcVehiNo 加油车编号
     */
    public void setFlrcVehiNo(String flrcVehiNo) {
        this.flrcVehiNo = flrcVehiNo == null ? null : flrcVehiNo.trim();
    }

    /**
     * 地井编号
     * @return flrc_hydrt_pit_no 地井编号
     */
    public String getFlrcHydrtPitNo() {
        return flrcHydrtPitNo;
    }

    /**
     * 地井编号
     * @param flrcHydrtPitNo 地井编号
     */
    public void setFlrcHydrtPitNo(String flrcHydrtPitNo) {
        this.flrcHydrtPitNo = flrcHydrtPitNo == null ? null : flrcHydrtPitNo.trim();
    }

    /**
     * 加油开始时间
     * @return flrc_stat_time 加油开始时间
     */
    public Date getFlrcStatTime() {
        return flrcStatTime;
    }

    /**
     * 加油开始时间
     * @param flrcStatTime 加油开始时间
     */
    public void setFlrcStatTime(Date flrcStatTime) {
        this.flrcStatTime = flrcStatTime;
    }

    /**
     * 加油结束时间
     * @return flrc_fnsh_time 加油结束时间
     */
    public Date getFlrcFnshTime() {
        return flrcFnshTime;
    }

    /**
     * 加油结束时间
     * @param flrcFnshTime 加油结束时间
     */
    public void setFlrcFnshTime(Date flrcFnshTime) {
        this.flrcFnshTime = flrcFnshTime;
    }

    /**
     * 加油员姓名
     * @return flrc_deliver_name 加油员姓名
     */
    public String getFlrcDeliverName() {
        return flrcDeliverName;
    }

    /**
     * 加油员姓名
     * @param flrcDeliverName 加油员姓名
     */
    public void setFlrcDeliverName(String flrcDeliverName) {
        this.flrcDeliverName = flrcDeliverName == null ? null : flrcDeliverName.trim();
    }

    /**
     * 签名（JPG图片的base64编码）
     * @return flrc_sign 签名（JPG图片的base64编码）
     */
    public String getFlrcSign() {
        return flrcSign;
    }

    /**
     * 签名（JPG图片的base64编码）
     * @param flrcSign 签名（JPG图片的base64编码）
     */
    public void setFlrcSign(String flrcSign) {
        this.flrcSign = flrcSign == null ? null : flrcSign.trim();
    }

    /**
     * 手动修改标记（0：自动生成，1：手动修改）
     * @return flrc_manual_tag 手动修改标记（0：自动生成，1：手动修改）
     */
    public Integer getFlrcManualTag() {
        return flrcManualTag;
    }

    /**
     * 手动修改标记（0：自动生成，1：手动修改）
     * @param flrcManualTag 手动修改标记（0：自动生成，1：手动修改）
     */
    public void setFlrcManualTag(Integer flrcManualTag) {
        this.flrcManualTag = flrcManualTag;
    }

    /**
     * 手动修改人员
     * @return flrc_manual_staff 手动修改人员
     */
    public String getFlrcManualStaff() {
        return flrcManualStaff;
    }

    /**
     * 手动修改人员
     * @param flrcManualStaff 手动修改人员
     */
    public void setFlrcManualStaff(String flrcManualStaff) {
        this.flrcManualStaff = flrcManualStaff == null ? null : flrcManualStaff.trim();
    }

    /**
     * 手动修改时间
     * @return flrc_manual_time 手动修改时间
     */
    public String getFlrcManualTime() {
        return flrcManualTime;
    }

    /**
     * 手动修改时间
     * @param flrcManualTime 手动修改时间
     */
    public void setFlrcManualTime(String flrcManualTime) {
        this.flrcManualTime = flrcManualTime == null ? null : flrcManualTime.trim();
    }

    /**
     * 审批状态（0：未审批，1：已审批）
     * @return flrc_revw_status 审批状态（0：未审批，1：已审批）
     */
    public Integer getFlrcRevwStatus() {
        return flrcRevwStatus;
    }

    /**
     * 审批状态（0：未审批，1：已审批）
     * @param flrcRevwStatus 审批状态（0：未审批，1：已审批）
     */
    public void setFlrcRevwStatus(Integer flrcRevwStatus) {
        this.flrcRevwStatus = flrcRevwStatus;
    }

    /**
     * 油单是否回收（0：未回收，1：已回收）
     * @return flrc_takeback_flg 油单是否回收（0：未回收，1：已回收）
     */
    public Integer getFlrcTakebackFlg() {
        return flrcTakebackFlg;
    }

    /**
     * 油单是否回收（0：未回收，1：已回收）
     * @param flrcTakebackFlg 油单是否回收（0：未回收，1：已回收）
     */
    public void setFlrcTakebackFlg(Integer flrcTakebackFlg) {
        this.flrcTakebackFlg = flrcTakebackFlg;
    }

    /**
     * 记录创建时间
     * @return flrc_rec_cre_time 记录创建时间
     */
    public Date getFlrcRecCreTime() {
        return flrcRecCreTime;
    }

    /**
     * 记录创建时间
     * @param flrcRecCreTime 记录创建时间
     */
    public void setFlrcRecCreTime(Date flrcRecCreTime) {
        this.flrcRecCreTime = flrcRecCreTime;
    }
    
    /**
     * T_TASK
     * 
     * 任务ID（uuid）
     * @return task_id 任务ID（uuid）
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 任务ID（uuid）
     * @param taskId 任务ID（uuid）
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * 加油员员工ID
     * @return task_ope_staff_id 加油员员工ID
     */
    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    /**
     * 加油员员工ID
     * @param taskOpeStaffId 加油员员工ID
     */
    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId == null ? null : taskOpeStaffId.trim();
    }

    /**
     * 任务内容（0：加油，1：抽油）
     * @return task_content 任务内容（0：加油，1：抽油）
     */
    public Integer getTaskContent() {
        return taskContent;
    }

    /**
     * 任务内容（0：加油，1：抽油）
     * @param taskContent 任务内容（0：加油，1：抽油）
     */
    public void setTaskContent(Integer taskContent) {
        this.taskContent = taskContent;
    }

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     * @return task_status 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）9：不加油）
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     * @param taskStatus 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）9：不加油）
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 任务派发时间
     * @return task_asg_time 任务派发时间
     */
    public Date getTaskAsgTime() {
        return taskAsgTime;
    }

    /**
     * 任务派发时间
     * @param taskAsgTime 任务派发时间
     */
    public void setTaskAsgTime(Date taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    /**
     * 任务接受时间
     * @return task_acc_time 任务接受时间
     */
    public Date getTaskAccTime() {
        return taskAccTime;
    }

    /**
     * 任务接受时间
     * @param taskAccTime 任务接受时间
     */
    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    /**
     * 加油开始时间
     * @return task_chag_sta_time 加油开始时间
     */
    public Date getTaskChagStaTime() {
        return taskChagStaTime;
    }

    /**
     * 加油开始时间
     * @param taskChagStaTime 加油开始时间
     */
    public void setTaskChagStaTime(Date taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    /**
     * 加油完成时间
     * @return task_chag_end_time 加油完成时间
     */
    public Date getTaskChagEndTime() {
        return taskChagEndTime;
    }

    /**
     * 加油完成时间
     * @param taskChagEndTime 加油完成时间
     */
    public void setTaskChagEndTime(Date taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    /**
     * 任务完成时间
     * @return task_done_time 任务完成时间
     */
    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    /**
     * 任务完成时间
     * @param taskDoneTime 任务完成时间
     */
    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    /**
     * 加油单编号
     * @return task_fuel_recpt_no 加油单编号
     */
    public String getTaskFuelRecptNo() {
        return taskFuelRecptNo;
    }

    /**
     * 加油单编号
     * @param taskFuelRecptNo 加油单编号
     */
    public void setTaskFuelRecptNo(String taskFuelRecptNo) {
        this.taskFuelRecptNo = taskFuelRecptNo;
    }

    /**
     * 加油车编号
     * @return task_vehi_no 加油车编号
     */
    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    /**
     * 加油车编号
     * @param taskVehiNo 加油车编号
     */
    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo == null ? null : taskVehiNo.trim();
    }

    /**
     * 创建人员工ID
     * @return task_cre_staff_id 创建人员工ID
     */
    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    /**
     * 创建人员工ID
     * @param taskCreStaffId 创建人员工ID
     */
    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId == null ? null : taskCreStaffId.trim();
    }

    /**
     * 任务星标（1：是，0：不是）默认0
     * @return task_starmark 任务星标（1：是，0：不是）默认0
     */
    public Integer getTaskStarmark() {
        return taskStarmark;
    }

    /**
     * 任务星标（1：是，0：不是）默认0
     * @param taskStarmark 任务星标（1：是，0：不是）默认0
     */
    public void setTaskStarmark(Integer taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    /**
     * 记录创建时间
     * @return task_rec_cre_time 记录创建时间
     */
    public Date getTaskRecCreTime() {
        return taskRecCreTime;
    }

    /**
     * 记录创建时间
     * @param taskRecCreTime 记录创建时间
     */
    public void setTaskRecCreTime(Date taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
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
    /**
  	 * T_TASK
  	 * 
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
    /**
     * 创建人员工名称（业务字段）
     */
    public String getTaskCreStaffName() {
		return taskCreStaffName;
	}

    /**
     * 创建人员工名称（业务字段）
     */
	public void setTaskCreStaffName(String taskCreStaffName) {
		this.taskCreStaffName = taskCreStaffName;
	}

    /**
     * 加油员员工名称（业务字段）
     */
    public String getTaskOpeStaffName() {
		return taskOpeStaffName;
	}

    /**
     * 加油员员工名称（业务字段）
     */
	public void setTaskOpeStaffName(String taskOpeStaffName) {
		this.taskOpeStaffName = taskOpeStaffName;
	}
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

    public Integer getFlgtOtatFuel() {
        return flgtOtatFuel;
    }

    public void setFlgtOtatFuel(Integer flgtOtatFuel) {
        this.flgtOtatFuel = flgtOtatFuel;
    }
}
