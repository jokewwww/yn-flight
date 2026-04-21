package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;

import java.util.Date;

public class FlightInfo {
    @XMLValue("flgt_d_stot")
    private Date STD;//计划起飞时间
    @XMLValue("flgt_a_stot")
    private Date STA;//计划到达时间
    @XMLValue("flgt_d_etot")
    private Date ETD;//预计起飞时间
    @XMLValue("flgt_a_etot")
    private Date ETA;//预计到达时间
    @XMLValue("flgt_d_atot")
    private Date ABT;//实际起飞时间
    @XMLValue("flgt_a_atot")
    private Date TDT;//实际到达时间
    @XMLValue("flgt_acname")
    private String ITY;//飞机类型

    private String IFC;//航司 ICAO
    @XMLValue("flgt_al2c")
    private String FLC;//航司 IATA
    @XMLValue("flgt_mission_prop")
    private String CLA;//航班任务属性
    @XMLValue("flgt_nature")
    private String NAT;//航班性质
    @XMLValue("flgt_sub_nature")
    private String FST;//航班性质细分
    @XMLValue("flgt_otc")
    private String OTC;//航段操作类型
    @XMLValue("flgt_proxy")
    private String HDA;//航空服务代理
    @XMLValue("flgt_flti")
    private String TOF;//航班国内/国际
    @XMLValue("flgt_ftyp")
    private String FlightStatus;//航班状态
    @XMLValue("flgt_delaycode")
    private String DelayCode;//延误原因
    @XMLValue("mFlightNo")
    private String MFlightNo;//主航班号
    @XMLValue("flgt_link_flno")
    private String LFlightNo;//连接航班号
    @XMLValue("flgt_link_repeat")
    private String LFlightRepeat;//连接航班连接次数
    @XMLValue(value = "flgt_link_flop", fromDateFormat = "yyyy-MM-dd")
    private Date LFlightDate;//连接航班日期
}
