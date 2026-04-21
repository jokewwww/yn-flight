package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;
import lombok.ToString;

import java.util.Date;


@ToString
public class FlightIdentity {

    @XMLValue("flgt_ffid")
    private String FID;//32 位航班唯一识别码
    @XMLValue("flgt_adid")
    private String Direction;//到离港标识
    @XMLValue("flgt_flno")
    private String FlightNo;//航班号
    @XMLValue(value = "flgt_flop", fromDateFormat = "yyyy-MM-dd")
    private Date FlightDate;//航班日期
    @XMLValue("flgt_regn")
    private String AirNum;//机号
    @XMLValue("flgt_org3c")
    private String DepCode;//起飞机场

    private String ArrCode;//落地机场
    @XMLValue("flgt_repeat")
    private String FlightRepeat;//航班连接次数


}
