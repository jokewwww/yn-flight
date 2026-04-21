package com.higer.higerservice.util;

import com.pro.util.CommonUtil;

import java.util.Date;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/8/26 09:52
 * @Description:
 */
public class Test {
    public class OilSheet {
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

        public OilSheet() {
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

        public void checkData() {
            if (this.flrcAirportCode == null) {
                this.flrcAirportCode = "--";
            }
            if (this.flrcAirlCode == null) {
                this.flrcAirlCode = "--";
            }
            if (this.flrcFuelVol == null) {
                this.flrcFuelVol = 0D;
            }
            if (this.flrcDate == null) {
                Date now = new Date();
                this.flrcDate = CommonUtil.dateToStrDate(now);
            }

            if (this.flrcId == null) {
                this.flrcId = "--";
            }

            if (this.flrcType == null) {
                this.flrcType = "--";
            }

            if (this.flrcTypeName == null) {
                this.flrcTypeName = "--";
            }

            if (this.flrcAirport == null) {
                this.flrcAirport = "--";
            }

            if (this.flrcAirlName == null) {
                this.flrcAirlName = "--";
            }

            if (this.flrcFlightNo == null) {
                this.flrcFlightNo = "--";
            }

            if (this.flrcAircrftNo == null) {
                this.flrcAircrftNo = "--";
            }

            if (this.flrcAircrftType == null) {
                this.flrcAircrftType = "--";
            }

            if (this.flrcDeparture == null) {
                this.flrcDeparture = "--";
            }

            if (this.flrcTransit == null) {
                this.flrcTransit = "--";
            }

            if (this.flrcDest == null) {
                this.flrcDest = "--";
            }

            if (this.flrcTestBillNo == null) {
                this.flrcTestBillNo = "--";
            }

            if (this.flrcFuelName == null) {
                this.flrcFuelName = "--";
            }

            if (this.flrcFiguarsWord == null) {
                this.flrcFiguarsWord = "--";
            }

            if (this.flrcHydrtPitNo == null) {
                this.flrcHydrtPitNo = "--";
            }

            if (this.flrcVehiNo == null) {
                this.flrcVehiNo = "--";
            }

            if (this.flrcStatTime == null) {
                this.flrcStatTime = "--";
            }

            if (this.flrcFnshTime == null) {
                this.flrcFnshTime = "--";
            }

            if (this.flrcDeliverName == null) {
                this.flrcDeliverName = "";
            }

            if (this.flrcFuelTemp == null) {
                this.flrcFuelTemp = new Double(0.0D);
            }

            if (this.flrcFuelDnst == null) {
                this.flrcFuelDnst = new Double(0.0D);
            }

            if (this.flrcMeterStat == null) {
                this.flrcMeterStat = new Long(0L);
            }

            if (this.flrcMeterFnsh == null) {
                this.flrcMeterFnsh = new Long(0L);
            }

            if (this.flrcFiguars == null) {
                this.flrcFiguars = new Long(0L);
            }

            if (this.flrcQuantity == null) {
                this.flrcQuantity = new Long(0L);
            }

        }

        public void makeDefaultData() {
            this.flrcId = "2220390024053";
            Date now = new Date();
            this.flrcDate = CommonUtil.dateToStrDate(now);
            this.flrcType = "5";
            this.flrcTypeName = "内航";
            this.flrcAirport = "沈阳桃仙机场";
            this.flrcAirlName = "厦门航空有限公司";
            this.flrcFlightNo = "MF8806";
            this.flrcAircrftNo = "B5151";
            this.flrcAircrftType = "B738";
            this.flrcDeparture = "DLC 大连";
            this.flrcTransit = "--";
            this.flrcDest = "HZH 杭州";
            this.flrcTestBillNo = "2017-354";
            this.flrcFuelName = "3号喷气燃油";
            this.flrcFuelTemp = 22.0D;
            this.flrcFuelDnst = 0.792D;
            this.flrcMeterStat = 6915L;
            this.flrcMeterFnsh = 7015L;
            this.flrcFiguars = 100L;
            this.flrcFiguarsWord = "壹佰";
            this.flrcQuantity = 79L;
            this.flrcHydrtPitNo = "79.1";
            this.flrcVehiNo = "615";
            this.flrcStatTime = "2017-06-19 06:40";
            this.flrcFnshTime = "2017-06-19 06:54";
            this.flrcDeliverName = "李健";
        }

        public String getTopic() {
            return this.topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getData() {
            return this.data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getFlrcId() {
            return this.flrcId;
        }

        public void setFlrcId(String flrcId) {
            this.flrcId = flrcId;
        }

        public String getFlrcDate() {
            return this.flrcDate;
        }

        public void setFlrcDate(String flrcDate) {
            this.flrcDate = flrcDate;
        }

        public String getFlrcType() {
            return this.flrcType;
        }

        public void setFlrcType(String flrcType) {
            this.flrcType = flrcType;
        }

        public String getFlrcTypeName() {
            return this.flrcTypeName;
        }

        public void setFlrcTypeName(String flrcTypeName) {
            this.flrcTypeName = flrcTypeName;
        }

        public String getFlrcAirport() {
            return this.flrcAirport;
        }

        public void setFlrcAirport(String flrcAirport) {
            this.flrcAirport = flrcAirport;
        }

        public String getFlrcAirlName() {
            return this.flrcAirlName;
        }

        public void setFlrcAirlName(String flrcAirlName) {
            this.flrcAirlName = flrcAirlName;
        }

        public String getFlrcFlightNo() {
            return this.flrcFlightNo;
        }

        public void setFlrcFlightNo(String flrcFlightNo) {
            this.flrcFlightNo = flrcFlightNo;
        }

        public String getFlrcAircrftNo() {
            return this.flrcAircrftNo;
        }

        public void setFlrcAircrftNo(String flrcAircrftNo) {
            this.flrcAircrftNo = flrcAircrftNo;
        }

        public String getFlrcAircrftType() {
            return this.flrcAircrftType;
        }

        public void setFlrcAircrftType(String flrcAircrftType) {
            this.flrcAircrftType = flrcAircrftType;
        }

        public String getFlrcDeparture() {
            return this.flrcDeparture;
        }

        public void setFlrcDeparture(String flrcDeparture) {
            this.flrcDeparture = flrcDeparture;
        }

        public String getFlrcTransit() {
            return this.flrcTransit;
        }

        public void setFlrcTransit(String flrcTransit) {
            this.flrcTransit = flrcTransit;
        }

        public String getFlrcDest() {
            return this.flrcDest;
        }

        public void setFlrcDest(String flrcDest) {
            this.flrcDest = flrcDest;
        }

        public String getFlrcTestBillNo() {
            return this.flrcTestBillNo;
        }

        public void setFlrcTestBillNo(String flrcTestBillNo) {
            this.flrcTestBillNo = flrcTestBillNo;
        }

        public String getFlrcFuelName() {
            return this.flrcFuelName;
        }

        public void setFlrcFuelName(String flrcFuelName) {
            this.flrcFuelName = flrcFuelName;
        }

        public Double getFlrcFuelTemp() {
            return this.flrcFuelTemp;
        }

        public void setFlrcFuelTemp(Double flrcFuelTemp) {
            this.flrcFuelTemp = flrcFuelTemp;
        }

        public Double getFlrcFuelDnst() {
            return this.flrcFuelDnst;
        }

        public void setFlrcFuelDnst(Double flrcFuelDnst) {
            this.flrcFuelDnst = flrcFuelDnst;
        }

        public Long getFlrcMeterStat() {
            return this.flrcMeterStat;
        }

        public void setFlrcMeterStat(Long flrcMeterStat) {
            this.flrcMeterStat = flrcMeterStat;
        }

        public Long getFlrcMeterFnsh() {
            return this.flrcMeterFnsh;
        }

        public void setFlrcMeterFnsh(Long flrcMeterFnsh) {
            this.flrcMeterFnsh = flrcMeterFnsh;
        }

        public Long getFlrcFiguars() {
            return this.flrcFiguars;
        }

        public void setFlrcFiguars(Long flrcFiguars) {
            this.flrcFiguars = flrcFiguars;
        }

        public String getFlrcFiguarsWord() {
            return this.flrcFiguarsWord;
        }

        public void setFlrcFiguarsWord(String flrcFiguarsWord) {
            this.flrcFiguarsWord = flrcFiguarsWord;
        }

        public Long getFlrcQuantity() {
            return this.flrcQuantity;
        }

        public void setFlrcQuantity(Long flrcQuantity) {
            this.flrcQuantity = flrcQuantity;
        }

        public String getFlrcHydrtPitNo() {
            return this.flrcHydrtPitNo;
        }

        public void setFlrcHydrtPitNo(String flrcHydrtPitNo) {
            this.flrcHydrtPitNo = flrcHydrtPitNo;
        }

        public String getFlrcVehiNo() {
            return this.flrcVehiNo;
        }

        public void setFlrcVehiNo(String flrcVehiNo) {
            this.flrcVehiNo = flrcVehiNo;
        }

        public String getFlrcStatTime() {
            return this.flrcStatTime;
        }

        public void setFlrcStatTime(String flrcStatTime) {
            this.flrcStatTime = flrcStatTime;
        }

        public String getFlrcFnshTime() {
            return this.flrcFnshTime;
        }

        public void setFlrcFnshTime(String flrcFnshTime) {
            this.flrcFnshTime = flrcFnshTime;
        }

        public String getFlrcSign() {
            return this.flrcSign;
        }

        public void setFlrcSign(String flrcSign) {
            this.flrcSign = flrcSign;
        }

        public String getFlrcDeliverName() {
            return this.flrcDeliverName;
        }

        public void setFlrcDeliverName(String flrcDeliverName) {
            this.flrcDeliverName = flrcDeliverName;
        }
    }
}
