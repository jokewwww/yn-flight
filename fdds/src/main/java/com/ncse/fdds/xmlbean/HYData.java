package com.ncse.fdds.xmlbean;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/6/30 10:31
 * @Description:
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope")
public class HYData {

    @XmlElement(name = "Header")
    private HeaderNew header;

    @XmlElement(name = "Body")
    private BodyNew body;
}
