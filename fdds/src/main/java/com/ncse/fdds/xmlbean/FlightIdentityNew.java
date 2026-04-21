package com.ncse.fdds.xmlbean;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/3 16:29
 * @Description:
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "FID",
        "Direction",
        "FlightNo",
        "FlightDate",
        "AirNum",
        "DepCode",
        "ArrCode",
        "ProcessNode"
})
public class FlightIdentityNew {

    private String FID; //32位航班唯一识别码

    private String Direction; //	到离港标识

    private String FlightNo; //航班号

    private String FlightDate; //航班日期

    private String AirNum; //机号

    private String DepCode; //起飞机场

    private String ArrCode; //落地机场

    private String ProcessNode; //	保障节点信息


}
