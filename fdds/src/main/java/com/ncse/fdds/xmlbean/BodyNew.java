package com.ncse.fdds.xmlbean;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.*;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/1 09:14
 * @Description:
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class BodyNew {

    @XmlElement(name = "FlightIdentity")
    private FlightIdentityNew FlightIdentity;

    @XmlElement(name = "ProcessNodeNew")
    private ProcessNodeNew ProcessNode;

}
