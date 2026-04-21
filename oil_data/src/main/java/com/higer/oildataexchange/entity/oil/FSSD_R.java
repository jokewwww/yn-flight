package com.higer.oildataexchange.entity.oil;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@XmlRootElement(name = "BD")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class FSSD_R {

    /**
     * 必须，油单号，目前为固定的13位，来自请求消息
     */
    private String FSID;

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
