package com.higer.oildataexchange.entity.credit;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 各机场智慧加油系统从总部平台获取航班动态（含预加油量）信息。当机场智慧加油系统无法对接机场航班动态时，可从总部获取航班动态信息
 */
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BD")
public class CIRQ {

    /**
     * 指定要获取的航班的机场列表（三码列表），若指定，则仅获取机场为指定机场的航班；否则，不限定机场；格式：“PEK,TSN,NNG”
     */
    private String APC3;

    /**
     * 要获取信用的客户代码，指定时，仅返回该客户的信息信息，未制定时，返回所有客户的信用信息。
     */
    private String CSTNO;

    /**
     * 要获取的数据的更新时间戳，数据的更新时间戳必须大于（不含等于）该指定值，若不指定本字段值则表示不限定更新时间戳。在系统第一次对接时，本字段应不设置值，当系统读取到任何数据后，应记录该数据的LUTS字段值并保存，下一次请求以该记录的值为准；接口重启后，也应最后一次获取到的数据的LUTS值开始。格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 本次获取数据的最大数量，服务器所反馈的消息中包含的数据记录数必须不超过该值；且如果查询到的记录数达到或超过该值时，服务器必须返回该值指定的记录数，不能低于该值。
     */
    private Integer LMTN;

}
