package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;
import lombok.ToString;

import java.util.Date;

@ToString
public class Header {
    @XMLValue("timestamp")
    private Date MessageSendDateTime;//时间戳

    @XMLValue("flgt_airport_code")
    private String Iata;//所属机场代码

    @XMLValue("opration")
    private String RequestType;//操作


}
