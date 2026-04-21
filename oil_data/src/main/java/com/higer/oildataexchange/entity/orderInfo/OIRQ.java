package com.higer.oildataexchange.entity.orderInfo;

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
public class OIRQ {

    /**
     * 指定要获取的油单状态的机场列表（三码列表），若指定，则仅获取机场为指定机场的油单；否则，不限定机场；格式：“PEK,TSN,NNG”
     */
    private String APC3;

    /**
     * 指定要获取的供油模式列表，未指定时，表示不限制订单模式；0-机坪加注；1-自提；2-油品配送；3-场外保障供油；4-自助加油；指定多个时，
     * 使用英文逗号“,”分隔，例如：“0,1”，表示获取机坪加注订单、自提订单；目前智慧加油系统仅需传入“01”来获取订单；
     */
    private String ADDOIL_TYPE;

    /**
     * 指定要获取的订单性质，未指定时，表示不限制订单性质；1-外航；2-内航离境；3-内航国内；指定多个时，
     * 使用英文逗号“,”分隔，例如：“1,2”，表示获取外航订单、内航离境订单；目前智慧加油系统无需传入该参数，表示不限制订单性质；
     */
    private String OIL_TYPE;

    /**
     * 指定要获取的订单的状态集合，取值如下：C-创建中，未提交；S-提交；A-审核通过；R-审核未通过；X-取消/作废 ；未指定时，不限制订单状态。
     */
    private String OTST;
    /**
     * 要获取的数据的起始时间（取订单中的用油/供油时间衡量），即用油时间必须大于或等于本字段指定时间；不指定时，不限制该时间；格式：YYYY-MM-DD HH:MI:SS。
     * 一般来说，每次请求可以将DTFR设置为当天日期前一天的凌晨零点，例如，假设今天是2019年5月5日，则请求消息中，本字段可设置为：2019-05-04 00:00:00
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTFR;

    /**
     * 要获取的数据的截止时间（取订单中的用油/供油时间衡量），
     * 即用油时间必须小于或等于本字段指定时间；不指定时，不限制该时间；一般情况下，不设置本字段值。
     * 格式：YYYY-MM-DD HH:MI:SS；通过制定DTFR和DTTO可以限定订单的时间段
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTTO;

    /**
     * 要获取的数据的更新时间戳，数据的更新时间戳必须大于（不含等于）该指定值，若不指定本字段值则表示不限定更新时间戳。
     * 在系统第一次对接时，本字段应不设置值，当系统读取到任何数据后，应记录该数据的LUTS字段值并保存，下一次请求以该记录的值为准；
     * 接口重启后，也应最后一次获取到的数据的LUTS值开始。格式：YYYY-MM-DD HH:MI:SS.nnn；
     */
    private String LUTS;

    /**
     * 本次获取数据的最大数量，服务器所反馈的消息中包含的数据记录数必须不超过该值；
     * 且如果查询到的记录数达到或超过该值时，服务器必须返回该值指定的记录数，不能低于该值。
     */
    private Integer LMTN;

}
