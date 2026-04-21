package com.higer.higerservice.entity;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/8/26 15:02
 * @Description:
 */
public class OilSheetNew {

    private String topic;//不知道
    private String data;//不知道
    private String token;//用户
    private String flrcId;    //加油单编号
    private String flrcDate; //加油日期
    private String flrcType; //油单类型（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油）
    private String flrcTypeName;//油单类型中文
    private String flrcAirport;//机场名称（机场名全称）
    private String flrcAirlName;//飞机所属单位（航空公司全称）
    private String flrcFlightNo;//航班号
    private String flrcAircrftNo;//飞机号码
    private String flrcAircrftType;//飞机类型
    private String flrcDeparture;//起始（机场名称+“/”+机场IATA三码）
    private String flrcTransit;//经停（备降）（机场名称+“/”+机场IATA三码）
    private String flrcDest;//终点（机场名称+“/”+机场IATA三码）
    private String flrcTestBillNo;//飞机号码
    private String flrcFuelName;//油品名称
    private Double flrcFuelTemp;//温度（摄氏度）
    private Double flrcFuelDnst;//密度（克/毫升（g/cm³））
    private Long flrcMeterStat;// 计量表开始读数
    private Long flrcMeterFnsh;//计量表结束读数
    private Long flrcFiguars;//加油数量小写（升）
    private String flrcFiguarsWord; //  加油数量大写（升）
    private Long flrcQuantity;   // 加油质量(千克)
    private String flrcHydrtPitNo;//地井编号
    private String flrcVehiNo;//加油车编号
    private String flrcStatTime;//加油开始时间
    private String flrcFnshTime;
    /// 加油结束时间
    private String flrcSign;//签名（JPG图片的base64编码）
    private String flrcDeliverName;//加油员姓名

    private String flrcDeliverId; //加油员id
    private String flrcAirportCode; // 机场区域代码
    private String flrcAirlCode; //    飞机所属单位代码（航空公司ICAO二字码）
    private Double flrcFuelVol; //      体积(升)
    private String taskId; //  任务ID;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFlrcId() {
        return flrcId;
    }

    public void setFlrcId(String flrcId) {
        this.flrcId = flrcId;
    }

    public String getFlrcDate() {
        return flrcDate;
    }

    public void setFlrcDate(String flrcDate) {
        this.flrcDate = flrcDate;
    }

    public String getFlrcType() {
        return flrcType;
    }

    public void setFlrcType(String flrcType) {
        this.flrcType = flrcType;
    }

    public String getFlrcTypeName() {
        return flrcTypeName;
    }

    public void setFlrcTypeName(String flrcTypeName) {
        this.flrcTypeName = flrcTypeName;
    }

    public String getFlrcAirport() {
        return flrcAirport;
    }

    public void setFlrcAirport(String flrcAirport) {
        this.flrcAirport = flrcAirport;
    }

    public String getFlrcAirlName() {
        return flrcAirlName;
    }

    public void setFlrcAirlName(String flrcAirlName) {
        this.flrcAirlName = flrcAirlName;
    }

    public String getFlrcFlightNo() {
        return flrcFlightNo;
    }

    public void setFlrcFlightNo(String flrcFlightNo) {
        this.flrcFlightNo = flrcFlightNo;
    }

    public String getFlrcAircrftNo() {
        return flrcAircrftNo;
    }

    public void setFlrcAircrftNo(String flrcAircrftNo) {
        this.flrcAircrftNo = flrcAircrftNo;
    }

    public String getFlrcAircrftType() {
        return flrcAircrftType;
    }

    public void setFlrcAircrftType(String flrcAircrftType) {
        this.flrcAircrftType = flrcAircrftType;
    }

    public String getFlrcDeparture() {
        return flrcDeparture;
    }

    public void setFlrcDeparture(String flrcDeparture) {
        this.flrcDeparture = flrcDeparture;
    }

    public String getFlrcTransit() {
        return flrcTransit;
    }

    public void setFlrcTransit(String flrcTransit) {
        this.flrcTransit = flrcTransit;
    }

    public String getFlrcDest() {
        return flrcDest;
    }

    public void setFlrcDest(String flrcDest) {
        this.flrcDest = flrcDest;
    }

    public String getFlrcTestBillNo() {
        return flrcTestBillNo;
    }

    public void setFlrcTestBillNo(String flrcTestBillNo) {
        this.flrcTestBillNo = flrcTestBillNo;
    }

    public String getFlrcFuelName() {
        return flrcFuelName;
    }

    public void setFlrcFuelName(String flrcFuelName) {
        this.flrcFuelName = flrcFuelName;
    }

    public Double getFlrcFuelTemp() {
        return flrcFuelTemp;
    }

    public void setFlrcFuelTemp(Double flrcFuelTemp) {
        this.flrcFuelTemp = flrcFuelTemp;
    }

    public Double getFlrcFuelDnst() {
        return flrcFuelDnst;
    }

    public void setFlrcFuelDnst(Double flrcFuelDnst) {
        this.flrcFuelDnst = flrcFuelDnst;
    }

    public Long getFlrcMeterStat() {
        return flrcMeterStat;
    }

    public void setFlrcMeterStat(Long flrcMeterStat) {
        this.flrcMeterStat = flrcMeterStat;
    }

    public Long getFlrcMeterFnsh() {
        return flrcMeterFnsh;
    }

    public void setFlrcMeterFnsh(Long flrcMeterFnsh) {
        this.flrcMeterFnsh = flrcMeterFnsh;
    }

    public Long getFlrcFiguars() {
        return flrcFiguars;
    }

    public void setFlrcFiguars(Long flrcFiguars) {
        this.flrcFiguars = flrcFiguars;
    }

    public String getFlrcFiguarsWord() {
        return flrcFiguarsWord;
    }

    public void setFlrcFiguarsWord(String flrcFiguarsWord) {
        this.flrcFiguarsWord = flrcFiguarsWord;
    }

    public Long getFlrcQuantity() {
        return flrcQuantity;
    }

    public void setFlrcQuantity(Long flrcQuantity) {
        this.flrcQuantity = flrcQuantity;
    }

    public String getFlrcHydrtPitNo() {
        return flrcHydrtPitNo;
    }

    public void setFlrcHydrtPitNo(String flrcHydrtPitNo) {
        this.flrcHydrtPitNo = flrcHydrtPitNo;
    }

    public String getFlrcVehiNo() {
        return flrcVehiNo;
    }

    public void setFlrcVehiNo(String flrcVehiNo) {
        this.flrcVehiNo = flrcVehiNo;
    }

    public String getFlrcStatTime() {
        return flrcStatTime;
    }

    public void setFlrcStatTime(String flrcStatTime) {
        this.flrcStatTime = flrcStatTime;
    }

    public String getFlrcFnshTime() {
        return flrcFnshTime;
    }

    public void setFlrcFnshTime(String flrcFnshTime) {
        this.flrcFnshTime = flrcFnshTime;
    }

    public String getFlrcSign() {
        return flrcSign;
    }

    public void setFlrcSign(String flrcSign) {
        this.flrcSign = flrcSign;
    }

    public String getFlrcDeliverName() {
        return flrcDeliverName;
    }

    public void setFlrcDeliverName(String flrcDeliverName) {
        this.flrcDeliverName = flrcDeliverName;
    }

    public String getFlrcDeliverId() {
        return flrcDeliverId;
    }

    public void setFlrcDeliverId(String flrcDeliverId) {
        this.flrcDeliverId = flrcDeliverId;
    }

    public String getFlrcAirportCode() {
        return flrcAirportCode;
    }

    public void setFlrcAirportCode(String flrcAirportCode) {
        this.flrcAirportCode = flrcAirportCode;
    }

    public String getFlrcAirlCode() {
        return flrcAirlCode;
    }

    public void setFlrcAirlCode(String flrcAirlCode) {
        this.flrcAirlCode = flrcAirlCode;
    }

    public Double getFlrcFuelVol() {
        return flrcFuelVol;
    }

    public void setFlrcFuelVol(Double flrcFuelVol) {
        this.flrcFuelVol = flrcFuelVol;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
