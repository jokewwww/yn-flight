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
@XmlRootElement(name = "BD")
public class OSRP {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;


    /**
     * 必须 所在的机场（即提供加油服务的机场）三码（IATA Code）
     */
    private String APC3;

    /**
     * 必须 订单号
     */
    private String ORDER_NO;

    /**
     * 必须 客户代码
     */
    private String CSTNO;
    /**
     * 客户名称
     */
    private String CSTNM;

    /**
     * 必须，上报的订单状态：0，尚未执行；1，等待执行；2，已经执行；3，未能执行
     */
    private String ORDER_STATUS;

    /**
     * 必须，订单状态变更发生的时间（例如订单实际完成时间）YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date SDAT;

    /**
     * 最后更新时间YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date LSTU;
}
