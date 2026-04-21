package com.higer.oildataexchange.entity.orderInfoBack;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SUB")
public class OSRP_R {

    /**
     * 必须，订单号
     */
    private String ORDER_NO;

    /**
     * 必须，消息处理状态，S-成功；F-失败
     */
    private String PTST;

    /**
     * 失败原因，当PTST=F时，必须提供；
     */
    private String FRSN;

    /**
     * 消息处理时间, 格式：YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date PDAT;


}
