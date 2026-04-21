package com.higer.oildataexchange.entity;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@XmlType(propOrder = {"MT", "MS", "SSN", "RSN", "RMS", "STM"})
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Hd {


    /**
     * 必须，消息类型（名称），每一类消息均有一个唯一的类型名称；一般来说，响应消息的消息名称以对应的“请求消息名称”加“-R”后缀构成
     */
    private String MT;

    /**
     * 必须，消息编号, 用于识别每一条消息，由发送方定义和写入，可用于收发双方核对数据使用。
     */
    private String MS;

    /**
     * 消息发送者, 对于中国航油发送的请求或响应消息，SSN始终为“CNAF”，对于南航发送的请求或响应消息，SSN始终为“CZ”，可忽略
     */
    private String SSN;

    /**
     * 消息发送者, 对于中国航油发送的请求或响应消息，RSN始终为“CZ”，对于南航发送的请求或响应消息，SSN始终为“CNAF”，可忽略
     */
    private String RSN;

    /**
     * 应答消息号，在应答消息（响应消息）中必须设置，取值来自被应答消息的消息号。
     */
    private String RMS;

    /**
     * 消息发送时间，格式：YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date STM;

}
