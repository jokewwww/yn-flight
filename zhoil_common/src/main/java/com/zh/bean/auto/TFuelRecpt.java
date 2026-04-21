package com.zh.bean.auto;

import java.io.Serializable;
import java.util.Date;

/**
 * 油单表实体类
 */
public class TFuelRecpt implements Serializable {
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
     * 机场名称（机场名全称）
     */
    private String flrcAirport;

    /**
     * 飞机所属单位代码（航空公司ICAO二字码）
     */
    private String flrcAirlCode;

    /**
     * 飞机所属单位（航空公司全称）
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
     * t_fuel_recpt
     */
    private static final long serialVersionUID = 1L;

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
     * 机场名称（机场名全称）
     * @return flrc_airport 机场名称（机场名全称）
     */
    public String getFlrcAirport() {
        return flrcAirport;
    }

    /**
     * 机场名称（机场名全称）
     * @param flrcAirport 机场名称（机场名全称）
     */
    public void setFlrcAirport(String flrcAirport) {
        this.flrcAirport = flrcAirport == null ? null : flrcAirport.trim();
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

    /**
     * 飞机所属单位（航空公司全称）
     * @return flrc_airl_name 飞机所属单位（航空公司全称）
     */
    public String getFlrcAirlName() {
        return flrcAirlName;
    }

    /**
     * 飞机所属单位（航空公司全称）
     * @param flrcAirlName 飞机所属单位（航空公司全称）
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
}