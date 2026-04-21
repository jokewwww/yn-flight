package com.higer.oildataexchange.entity.pay;

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
@XmlRootElement(name = "BD")
public class FSST {

    /**
     * 指定要获取的油单状态的机场列表（三码列表），若指定，则仅获取机场为指定机场的油单；否则，不限定机场；格式：“PEK,TSN,NNG”
     */
    private String APC3;

    /**
     * 要获取的数据的起始时间（取加油开始时间衡量），即航班加油开始时间必须大于或等于本字段指定时间；不指定时，不限制该时间；格式：YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTFR;

    /**
     * 要获取的数据的截止时间（取加油开始时间衡量），即航班加油开始时间必须小于或等于本字段指定时间；不指定时，不限制该时间；格式：YYYY-MM-DD HH:MI:SS；通过制定DTFR和DTTO可以限定油单的时间段（以开始时间进行限定）
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTTO;

    /**
     * 要获取的数据的更新时间戳，数据的更新时间戳必须大于该指定值，若不指定本字段值则表示不限定更新时间戳。最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn；
     */
    private String LUTS;

    /**
     * 本次获取数据的最大数量，服务器所反馈的消息中包含的数据记录数必须不超过该值；且如果查询到的记录数达到或超过该值时，必须返回该值指定的记录数，不能低于该值。
     */
    private Integer LMTN;

}
