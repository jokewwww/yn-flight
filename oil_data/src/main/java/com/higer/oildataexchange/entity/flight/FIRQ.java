package com.higer.oildataexchange.entity.flight;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * 各机场智慧加油系统从总部平台获取航班动态（含预加油量）信息。当机场智慧加油系统无法对接机场航班动态时，可从总部获取航班动态信息
 */
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BD")
public class FIRQ {

    /**
     * 指定要获取的航班的机场列表（三码列表），若指定，则仅获取机场为指定机场的航班；否则，不限定机场；格式：“PEK,TSN,NNG”
     */
    private String APC3;

    /**
     * 指定要获取或排除的航班的航空公司列表（二码列表），设置时，可以在列表最前面加符号“-”，表示排除，不加符号“-”表示包含；未指定时，表示不限制航空公司；例如：“CA,MU,CZ”，表示仅获取国航、东航、南航的航班；“-CZ,MU”表示获取除了南航和东航意外的其他航空公司的航班
     */
    private String ALC2;

    /**
     * 指定要获取的航班进离港，取值如下：A-仅获取进港航班，D-仅获取离港航班；未指定时，不限制进离港类型。
     */
    private String ADID;

    /**
     * 要获取的数据的起始时间（取航班计划时间衡量），即航班计划达到/起飞时间必须大于或等于本字段指定时间；不指定时，不限制该时间；格式：YYYY-MM-DD HH:MI:SS。一般来说，每次请求可以将DTFR设置为当天日期前一天的凌晨零点，例如，假设今天是2019年5月5日，则请求消息中，本字段可设置为：2019-05-04 00:00:00
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTFR;

    /**
     * 要获取的数据的截止时间（取航班计划时间衡量），即航班计划达到/起飞时间必须小于或等于本字段指定时间；不指定时，不限制该时间；一般情况下，不设置本字段值。格式：YYYY-MM-DD HH:MI:SS；通过制定DTFR和DTTO可以限定航班的时间段
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date DTTO;

    /**
     * 要获取的数据的更新时间戳，数据的更新时间戳必须大于（不含等于）该指定值，若不指定本字段值则表示不限定更新时间戳。在系统第一次对接时，本字段应不设置值，当系统读取到任何数据后，应记录该数据的LUTS字段值并保存，下一次请求以该记录的值为准；接口重启后，也应最后一次获取到的数据的LUTS值开始。格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 本次获取数据的最大数量，服务器所反馈的消息中包含的数据记录数必须不超过该值；且如果查询到的记录数达到或超过该值时，服务器必须返回该值指定的记录数，不能低于该值。
     */
    private Integer LMTN;

}
