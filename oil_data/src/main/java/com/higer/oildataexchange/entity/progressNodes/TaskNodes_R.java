package com.higer.oildataexchange.entity.progressNodes;

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
public class TaskNodes_R {

    /**
     * 必须，任务记录号
     */
    private String TNB;

    /**
     * 必须，任务状态/任务节点类型
     */
    private String TTST;

    /**
     * 必须,航班号
     */
    private String FLNO;

    /**
     * 必须，S-成功；F-失败
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
