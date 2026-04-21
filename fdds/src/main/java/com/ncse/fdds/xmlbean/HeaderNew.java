package com.ncse.fdds.xmlbean;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.*;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/6/30 11:12
 * @Description:
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "MessageSendDateTime",
        "MessageSeqence",
        "ServiceType",
        "MessageType",
        "SourceSystemID",
        "Iata",
        "RequestType"
})
public class HeaderNew {

    private String MessageSendDateTime; //发送消息时间

    private String MessageSeqence; //消息序列号

    private String ServiceType; // 服务类型

    private String MessageType; //消息类型

    private String SourceSystemID; //生成系统ID

    private String Iata; //机场三字码

    private String RequestType; //请求消息类型

}
