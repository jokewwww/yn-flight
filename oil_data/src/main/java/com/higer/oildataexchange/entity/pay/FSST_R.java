package com.higer.oildataexchange.entity.pay;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SUB")
public class FSST_R {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 必须，油单所在的机场（即提供加油服务的机场）三码（IATA Code）
     */
    private String APC3;

    /**
     * 必须，飞机所属单位（结算单位）代码（中国航油内部代码）
     */
    private String CSTNO;

    /**
     * 必须，油单号，目前为固定的13位
     */
    private String FSID;

    /**
     * 必须，油单版本号，初始为“01”，之后每隔版本号递增
     */
    private String VRSN;

    /**
     * 必须，油单日期，一般以飞机开始加油的时间的日期作为油单日期，格式：YYYYMMDD，例如“20190301”
     */
    private String FSOP;

    /**
     * 油单确认/签收状态：N-未确认；S-确认/签收成功；F-确认/签收失败，拒绝签收
     */
    private String RTST;

    /**
     * 收到确认回复的时间（记录确认状态的时间）
     */
    private String RDAT;

    /**
     * 油单确认/签收人（收油人、机组）姓名
     */
    private String RENM;

    /**
     * 确认失败或拒绝签收原因（当RTST=F时，会提供本字段）
     */
    private String RRSN;

    /**
     * 收油人（机组）签收时间
     */
    private String TRSG;

    /**
     * 油单结算状态：N-未支付，S-支付成功；F-支付失败
     */
    private String PTST;

    /**
     * 收到结算结果的时间
     */
    private String PDAT;

    /**
     * 支付失败原因（当PTST=F时，会提供本字段）
     */
    private String PRSN;

    /**
     * 支付时间
     */
    private String TPAY;
}
