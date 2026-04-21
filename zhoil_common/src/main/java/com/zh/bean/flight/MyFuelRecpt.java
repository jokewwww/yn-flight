package com.zh.bean.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zh.format.YYYYMMDD;
import com.zh.format.YYYYMMDD_HHMM;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 油单表
 * T_FUEL_RECPT
 */
public class MyFuelRecpt implements Serializable {
    /**
     * T_FUEL_RECPT
     */
    private static final long serialVersionUID = 1L;

    /**
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
    @JsonDeserialize(using = YYYYMMDD.class)
    private Date flrcDate;
    /*
        加抽油日期
     */
    @JsonDeserialize(using = YYYYMMDD.class)
    private Date flrcWkopDate;


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
     * 飞机所属单位代码（航空公司ICAO二字码）
     */
    private String flrcAirlCode;

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
     *              flrcDeparture3c
     */
    private String flrcDeparture;

    private String flrcDeparture3c;

    /**
     * 经停（备降）（机场名称+“/”+机场IATA三码）
     */
    private String flrcTransit;


    private String flrcTransit3c;
    /**
     * 终点（机场名称+“/”+机场IATA三码）
     */
    private String flrcDest;

    private String flrcDest3c;

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
    private BigDecimal flrcFuelTemp;

    /**
     * 密度（克/毫升（g/cm³））
     */
    private BigDecimal flrcFuelDnst;

    /**
     * 体积（升）
     */
    private BigDecimal flrcFuelVol;

    /**
     * 计量表开始读数
     */
    private BigDecimal flrcMeterStat;

    /**
     * 计量表结束读数
     */
    private BigDecimal flrcMeterFnsh;

    /**
     * 加油数量小写（升）
     */
    private BigDecimal flrcFiguars;

    /**
     * 加油数量大写（升）
     */
    private String flrcFiguarsWord;

    /**
     * 加油质量（千克）
     */
    private BigDecimal flrcQuantity;

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
    @JsonDeserialize(using = YYYYMMDD_HHMM.class)
    private Date flrcStatTime;

    /**
     * 加油结束时间
     */
    @JsonDeserialize(using = YYYYMMDD_HHMM.class)
    private Date flrcFnshTime;

    /**
     * 加油员姓名
     */
    private String flrcDeliverName;


    /**
     * 加油员ID
     */
    private String flrcDeliverId;

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
     * pad油单生成时间
     */
    @JsonDeserialize(using = YYYYMMDD_HHMM.class)
    private Date flrcGenerateTime;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 业务字段 加油时间
     */
    private Long timeAll;
    /**
     * 业务字段 飞机单位
     */
    private String flightUnit;

    //生成油单的时候未
    private String oldFlrcNo;


    private String flrcVehiNum;



    private String flrcBwtar;

    private Integer flrcStatus;

    private String arcrCustomNum;

    private String flrcUpdateErrMsg;

    private Integer flrcConfirmStatus;
    private String flrcConfirmErrMsg;
    private Integer flrcPayStatus;
    private String flrcPayErrMsg;
    private String flgtAdid;
    private String flgtPlacecode;
    private Integer flrcVersion;
    private Integer flrcPrintCount;

    private Integer flrcIsExport;

    private String orderId;

    private String flgtOlvr;

    private String flrcSingle;// 电子油单图片

    private String flrcSingleNew;

    private String flrcDefuelReason; //抽油原因必填

    /*
    抽油原因代码（抽油单必须），取值如下：
        Z01 试发
        Z02 称重
        Z03 清洗油箱
        Z04 加多油
        Z05 排故
        Z09 其他
        Z10 抗震救灾
     */
    private String flrcDefuelReasonCode;

    private String flrcDefuelTest; //抽油时是否进行油品检测（仅抽油单使用）：Y - 已检测（要收取检测费）；N-不检测（不收检测费），默认N；

    private String flrcDefuelRefruel; //抽油后是否重新加注（仅抽油单使用）：Y-直接加注（收取重新注费）；N - 不加注（不收取重新加注费），默认N；

    private String flrcDefuelStorage; //抽油保存天数，0 - 不保管（不收保管费）;>0 -保管的天数（要收保管费），默认N

    /**
     * 供油类型 0 加注 1 自提 2 配送
     */
    private Integer flrcSupplyFuelType;

    /**
     * 备注信息
     */
    private String flrcRemark;

    /**
     * 收油机场（去向机场）三码，收油机场，自提、配送的目的地机场三码
     */
    private String flrcRapc3;

    /**
     * 收油机场（去向机场）名称，收油机场，自提、配送的目的地机场名称
     */
    private String flrcRapcn;

    /**
     * 运油车号（仅自提、配送方式需要）
     */
    private String flrcTankerNo;

    /**
     * 运输里程，单位：公里（仅配送方式需要）
     */
    private Integer flrcTransportMileage;

    /**
     * 业务类型：1-通航业务，2-运输航业务，如果没有值默认为2-运输航业务
     */
    private Integer flrcBusType;

    /**
     * 代结算机场三码（即销售结算的记账机场），每个机场固定配置，默认与供油机场相同；主要用于系统的销售记账
     */
    private String settlementRapc3;

    /**
     * 代结算机场名称
     */
    private String settlementRapcn;
    /**
     * 手工油单（1：手工创建；2：其它）
     */
    private Integer manual;

    private String flgtId;

    //接口修改返回数据增加航班日期、进出港类型、机位

    private Date flgtFlop;

    //staff_scheduling_id
    private String staffSchedulingId;

    private Integer flgtStatus;

    private String flgtOil;

    public Date getFlrcGenerateTime() {
        return flrcGenerateTime;
    }

    public void setFlrcGenerateTime(Date flrcGenerateTime) {
        this.flrcGenerateTime = flrcGenerateTime;
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

    public String getStaffSchedulingId() {
        return staffSchedulingId;
    }

    public void setStaffSchedulingId(String staffSchedulingId) {
        this.staffSchedulingId = staffSchedulingId;
    }

    public Date getFlgtFlop() {
        return flgtFlop;
    }

    public void setFlgtFlop(Date flgtFlop) {
        this.flgtFlop = flgtFlop;
    }

    public String getFlgtId() {
        return flgtId;
    }

    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId;
    }

    public Date getFlrcWkopDate() {
        return flrcWkopDate;
    }

    public void setFlrcWkopDate(Date flrcWkopDate) {
        this.flrcWkopDate = flrcWkopDate;
    }



    public Integer getManual() {
        return manual;
    }

    public void setManual(Integer manual) {
        this.manual = manual;
    }

    public String getFlrcDefuelReason() {
        return flrcDefuelReason;
    }

    public void setFlrcDefuelReason(String flrcDefuelReason) {
        this.flrcDefuelReason = flrcDefuelReason;
    }

    public String getFlrcDefuelReasonCode() {
        return flrcDefuelReasonCode;
    }

    public void setFlrcDefuelReasonCode(String flrcDefuelReasonCode) {
        this.flrcDefuelReasonCode = flrcDefuelReasonCode;
    }

    public String getFlrcDefuelTest() {
        return flrcDefuelTest;
    }

    public void setFlrcDefuelTest(String flrcDefuelTest) {
        this.flrcDefuelTest = flrcDefuelTest;
    }

    public String getFlrcDefuelRefruel() {
        return flrcDefuelRefruel;
    }

    public void setFlrcDefuelRefruel(String flrcDefuelRefruel) {
        this.flrcDefuelRefruel = flrcDefuelRefruel;
    }

    public String getFlrcDefuelStorage() {
        return flrcDefuelStorage;
    }

    public void setFlrcDefuelStorage(String flrcDefuelStorage) {
        this.flrcDefuelStorage = flrcDefuelStorage;
    }

    public String getFlrcSingleNew() {
        return flrcSingleNew;
    }

    public void setFlrcSingleNew(String flrcSingleNew) {
        this.flrcSingleNew = flrcSingleNew;
    }

    public String getFlrcSingle() {
        return flrcSingle;
    }

    public void setFlrcSingle(String flrcSingle) {
        this.flrcSingle = flrcSingle;
    }

    public String getFlgtOlvr() {
        return flgtOlvr;
    }

    public void setFlgtOlvr(String flgtOlvr) {
        this.flgtOlvr = flgtOlvr;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFlrcDest3c() {
        return flrcDest3c;
    }

    public void setFlrcDest3c(String flrcDest3c) {
        this.flrcDest3c = flrcDest3c;
    }

    public String getFlrcDeparture3c() {
        return flrcDeparture3c;
    }

    public void setFlrcDeparture3c(String flrcDeparture3c) {
        this.flrcDeparture3c = flrcDeparture3c;
    }

    public String getFlrcTransit3c() {
        return flrcTransit3c;
    }

    public void setFlrcTransit3c(String flrcTransit3c) {
        this.flrcTransit3c = flrcTransit3c;
    }

    public Integer getFlrcIsExport() {
        return flrcIsExport;
    }

    public void setFlrcIsExport(Integer flrcIsExport) {
        this.flrcIsExport = flrcIsExport;
    }

    public String getArcrCustomNum() {
        return arcrCustomNum;
    }

    public void setArcrCustomNum(String arcrCustomNum) {
        this.arcrCustomNum = arcrCustomNum;
    }

    public String getFlrcUpdateErrMsg() {
        return flrcUpdateErrMsg;
    }

    public void setFlrcUpdateErrMsg(String flrcUpdateErrMsg) {
        this.flrcUpdateErrMsg = flrcUpdateErrMsg;
    }

    public Integer getFlrcStatus() {
        return flrcStatus;
    }

    public void setFlrcStatus(Integer flrcStatus) {
        this.flrcStatus = flrcStatus;
    }

    public String getFlrcBwtar() {
        return flrcBwtar;
    }

    public void setFlrcBwtar(String flrcBwtar) {
        this.flrcBwtar = flrcBwtar;
    }

    public String getFlrcVehiNum() {
        return flrcVehiNum;
    }

    public void setFlrcVehiNum(String flrcVehiNum) {
        this.flrcVehiNum = flrcVehiNum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOldFlrcNo() {
        return oldFlrcNo;
    }

    public void setOldFlrcNo(String oldFlrcNo) {
        this.oldFlrcNo = oldFlrcNo;
    }

    /**
     * 业务字段 飞机单位
     */
    public String getFlightUnit() {
        return flightUnit;
    }

    /**
     * 业务字段 飞机单位
     */
    public void setFlightUnit(String flightUnit) {
        this.flightUnit = flightUnit;
    }

    /**
     * 业务字段 加油时间
     */
    public Long getTimeAll() {
        return timeAll;
    }

    /**
     * 业务字段 加油时间
     */
    public void setTimeAll(Long timeAll) {
        this.timeAll = timeAll;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    /**
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
        this.flrcNo = flrcNo;
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
    public BigDecimal getFlrcFuelTemp() {
        return flrcFuelTemp;
    }

    /**
     * 温度（摄氏度）
     * @param flrcFuelTemp 温度（摄氏度）
     */
    public void setFlrcFuelTemp(BigDecimal flrcFuelTemp) {
        this.flrcFuelTemp = flrcFuelTemp;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @return flrc_fuel_dnst 密度（克/毫升（g/cm³））
     */
    public BigDecimal getFlrcFuelDnst() {
        return flrcFuelDnst;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @param flrcFuelDnst 密度（克/毫升（g/cm³））
     */
    public void setFlrcFuelDnst(BigDecimal flrcFuelDnst) {
        this.flrcFuelDnst = flrcFuelDnst;
    }

    /**
     * 体积（升）
     * @return flrc_fuel_vol 体积（升）
     */
    public BigDecimal getFlrcFuelVol() {
        return flrcFuelVol;
    }

    /**
     * 体积（升）
     * @param flrcFuelVol 体积（升）
     */
    public void setFlrcFuelVol(BigDecimal flrcFuelVol) {
        this.flrcFuelVol = flrcFuelVol;
    }

    /**
     * 计量表开始读数
     * @return flrc_meter_stat 计量表开始读数
     */
    public BigDecimal getFlrcMeterStat() {
        return flrcMeterStat;
    }

    /**
     * 计量表开始读数
     * @param flrcMeterStat 计量表开始读数
     */
    public void setFlrcMeterStat(BigDecimal flrcMeterStat) {
        this.flrcMeterStat = flrcMeterStat;
    }

    /**
     * 计量表结束读数
     * @return flrc_meter_fnsh 计量表结束读数
     */
    public BigDecimal getFlrcMeterFnsh() {
        return flrcMeterFnsh;
    }

    /**
     * 计量表结束读数
     * @param flrcMeterFnsh 计量表结束读数
     */
    public void setFlrcMeterFnsh(BigDecimal flrcMeterFnsh) {
        this.flrcMeterFnsh = flrcMeterFnsh;
    }

    /**
     * 加油数量小写（升）
     * @return flrc_figuars 加油数量小写（升）
     */
    public BigDecimal getFlrcFiguars() {
        return flrcFiguars;
    }

    /**
     * 加油数量小写（升）
     * @param flrcFiguars 加油数量小写（升）
     */
    public void setFlrcFiguars(BigDecimal flrcFiguars) {
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
    public BigDecimal getFlrcQuantity() {
        return flrcQuantity;
    }

    /**
     * 加油质量（千克）
     * @param flrcQuantity 加油质量（千克）
     */
    public void setFlrcQuantity(BigDecimal flrcQuantity) {
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
    public void date2(Date flrcStatTime) {
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

    public String getFlrcDeliverId() {
        return flrcDeliverId;
    }

    public void setFlrcDeliverId(String flrcDeliverId) {
        this.flrcDeliverId = flrcDeliverId;
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

    public void setFlrcStatTime(Date flrcStatTime) {
        this.flrcStatTime = flrcStatTime;
    }

    /**
     * 飞机所属单位代码（航空公司ICAO二字码）
     * @return flrc_airl_code 飞机所属单位代码（航空公司ICAO二字码）
     */
    public String getFlrcAirlCode() {
        return flrcAirlCode;
    }

    /**
     * 飞机所属单位代码（航空公司ICAO二字码）
     * @param flrcAirlCode 飞机所属单位代码（航空公司ICAO二字码）
     */
    public void setFlrcAirlCode(String flrcAirlCode) {
        this.flrcAirlCode = flrcAirlCode == null ? null : flrcAirlCode.trim();
    }

    public Integer getFlrcConfirmStatus() {
        return flrcConfirmStatus;
    }

    public void setFlrcConfirmStatus(Integer flrcConfirmStatus) {
        this.flrcConfirmStatus = flrcConfirmStatus;
    }

    public String getFlrcConfirmErrMsg() {
        return flrcConfirmErrMsg;
    }

    public void setFlrcConfirmErrMsg(String flrcConfirmErrMsg) {
        this.flrcConfirmErrMsg = flrcConfirmErrMsg;
    }

    public Integer getFlrcPayStatus() {
        return flrcPayStatus;
    }

    public void setFlrcPayStatus(Integer flrcPayStatus) {
        this.flrcPayStatus = flrcPayStatus;
    }

    public String getFlrcPayErrMsg() {
        return flrcPayErrMsg;
    }

    public void setFlrcPayErrMsg(String flrcPayErrMsg) {
        this.flrcPayErrMsg = flrcPayErrMsg;
    }

    public String getFlgtAdid() {
        return flgtAdid;
    }

    public void setFlgtAdid(String flgtAdid) {
        this.flgtAdid = flgtAdid;
    }

    public String getFlgtPlacecode() {
        return flgtPlacecode;
    }

    public void setFlgtPlacecode(String flgtPlacecode) {
        this.flgtPlacecode = flgtPlacecode;
    }

    public Integer getFlrcVersion() {
        return flrcVersion;
    }

    public void setFlrcVersion(Integer flrcVersion) {
        this.flrcVersion = flrcVersion;
    }

    public Integer getFlrcPrintCount() {
        return flrcPrintCount;
    }

    public void setFlrcPrintCount(Integer flrcPrintCount) {
        this.flrcPrintCount = flrcPrintCount;
    }

    public Integer getFlrcSupplyFuelType() {
        return flrcSupplyFuelType;
    }

    public void setFlrcSupplyFuelType(Integer flrcSupplyFuelType) {
        this.flrcSupplyFuelType = flrcSupplyFuelType;
    }

    public String getFlrcRemark() {
        return flrcRemark;
    }

    public void setFlrcRemark(String flrcRemark) {
        this.flrcRemark = flrcRemark;
    }

    public String getFlrcRapc3() {
        return flrcRapc3;
    }

    public void setFlrcRapc3(String flrcRapc3) {
        this.flrcRapc3 = flrcRapc3;
    }

    public String getFlrcRapcn() {
        return flrcRapcn;
    }

    public void setFlrcRapcn(String flrcRapcn) {
        this.flrcRapcn = flrcRapcn;
    }

    public String getFlrcTankerNo() {
        return flrcTankerNo;
    }

    public void setFlrcTankerNo(String flrcTankerNo) {
        this.flrcTankerNo = flrcTankerNo;
    }

    public Integer getFlrcTransportMileage() {
        return flrcTransportMileage;
    }

    public void setFlrcTransportMileage(Integer flrcTransportMileage) {
        this.flrcTransportMileage = flrcTransportMileage;
    }

    public Integer getFlrcBusType() {
        return flrcBusType;
    }

    public void setFlrcBusType(Integer flrcBusType) {
        this.flrcBusType = flrcBusType;
    }

    public String getSettlementRapc3() {
        return settlementRapc3;
    }

    public void setSettlementRapc3(String settlementRapc3) {
        this.settlementRapc3 = settlementRapc3;
    }

    public String getSettlementRapcn() {
        return settlementRapcn;
    }

    public void setSettlementRapcn(String settlementRapcn) {
        this.settlementRapcn = settlementRapcn;
    }


    //===================查询参数==========================
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    //===================扩展参数==========================

    //同一天，同一个航班，不校验重复
    private Boolean noCheckRepetition = false;

    //是否签名（1 是  0否）
    private Integer flrcSigned=0;

    //油单类型名称
    private String flrcTypeName;

    public Boolean getNoCheckRepetition() {
        return this.noCheckRepetition;
    }

    public void setNoCheckRepetition(final Boolean noCheckRepetition) {
        this.noCheckRepetition = noCheckRepetition;
    }

    public Integer getFlrcSigned() {
        return this.flrcSigned;
    }

    public void setFlrcSigned(final Integer flrcSigned) {
        this.flrcSigned = flrcSigned;
    }

    public String getFlrcTypeName() {
        return this.flrcTypeName;
    }

    public void setFlrcTypeName(final String flrcTypeName) {
        this.flrcTypeName = flrcTypeName;
    }

    public void setFlrcTypeNameInfo(){
        if(flrcType==null){
            return;
        }
        //1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油 ）
        switch (flrcType){
            case 1:
                flrcTypeName="外航加油";
                break;
            case 2:
                flrcTypeName="内航离境加油";
                break;
            case 3:
                flrcTypeName="内航国内加油";
                break;
            case 4:
                flrcTypeName="外航抽油";
                break;
            case 5:
                flrcTypeName="内航离境抽油";
                break;
            case 6:
                flrcTypeName="内航国内抽油";
                break;
            default:
                flrcTypeName="未知";
        }
    }
}