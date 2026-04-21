package com.higer.oildataexchange.entity.credit;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "SUB")
public class CIRQ_R {

    /**
     * 必须，客户代码
     */
    private String CSTNO;

    /**
     * 必须，客户名称
     */
    private String CSTNM;

    /**
     * 必须，客户类型（0-实时结算,1-赊销,2-预收款,3-金融方案）
     */
    private String CUSTOMER_TYPE;

    /**
     * 必须，信用评级
     */
    private String CILVL;

    /**
     * 必须，信用状态：0-正常（默认）；1-提示（低风险）；2-警示（高风险）
     */
    private String CITST;

    /**
     * 必须，信用的描述信息
     */
    private String CIRMK;


    public TCreditInfo toCreditInfo() {
        TCreditInfo creditInfo = new TCreditInfo();
        creditInfo.setCstno(CSTNO);
        creditInfo.setCstnm(CSTNM);
        creditInfo.setCilvl(CILVL);
        creditInfo.setCirmk(CIRMK);
        creditInfo.setCitst(CITST);
        creditInfo.setCustomerType(CUSTOMER_TYPE);
        creditInfo.setCreateDate(new Date());
        return creditInfo;
    }
}
