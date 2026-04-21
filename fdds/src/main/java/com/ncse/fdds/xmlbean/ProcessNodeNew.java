package com.ncse.fdds.xmlbean;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/3 16:31
 * @Description:
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "StartFuelTime",
        "EndFuelTime"
})
public class ProcessNodeNew {
    private String StartFuelTime;  //	油车到位（加油开始）

    private String EndFuelTime;   //	加油结束

}
