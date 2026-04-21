package com.higer.oildataexchange.entity.oil;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BD")
public class FSSD {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    @NonNull
    private String IUD;

    /**
     * 最后更新时间YYYY-MM-DD HH:MI:SS
     */
    @NonNull
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date LSTU;

    /**
     * 必须，油单所在的机场（即提供加油服务的机场）三码（IATA Code）
     */
    @NonNull
    private String APC3;

    /**
     * 必须，飞机所属单位（结算单位）代码（中国航油内部代码）
     */
    private String ERP_CODE;

    /**
     * 航空公司二码
     */
    @NonNull
    private String ALC2;

    /**
     * 必须，油单号，目前为固定的13位
     */
    @NonNull
    private String FSID;

    /**
     * 必须，油单版本号，初始为“01”，之后每隔版本号递增
     */
    @NonNull
    private String VRSN;

    /**
     * 必须，油单日期，一般以飞机开始加油的时间的日期作为油单日期，格式：YYYY-MM-DD，例如“2019-03-01”
     */
    @NonNull
    private String FSOP;

    /**
     * 必须，加抽油日期
     */
    @NonNull
    private String WKOP;

    /**
     * 必须，油单类型，1-外航加油；2-内航离境加油；3-内航国内加油；4-外航抽油；5-内航离境抽油；6-内航国内抽油
     */
    @NonNull
    private String FSTP;

    /**
     * 必必须，保税类型：B -保税；FB、YB、N非保税
     */
    private String IFBS;

    /**
     * 航班执行日期，一般为航班计划起飞时间的日期，格式：YYYYMMDD，例如“20190301”
     */
    private String FLOP;

    /**
     * 必须，航班号
     */
    private String FLNO;

    /**
     * 必须，航班进离港属性，A-进港；D-离港
     */
    private String ADID;

    /**
     * 必须，机号
     */
    private String REGN;

    /**
     * 必须，机型
     */
    private String ACTN;

    /**
     * 必须，机位
     */
    private String PSN;

    /**
     * 必须，始发机场代码（IATA Code）
     */
    private String ORG3;

    /**
     * 始发机场名称
     */
    private String ORGNM;

    /**
     * 经停机场代码（IATA Code）
     */
    private String VIA3;

    /**
     * 经停机场名称
     */
    private String VIANM;

    /**
     * 必须，目的机场代码（IATA Code）
     */
    private String DES3;

    /**
     * 必须，目的机场名称
     */
    private String DESNM;

    /**
     * 化验单号
     */
    private String OTBN;

    /**
     * 必须，油品
     */
    private String OTYP;

    /**
     * 必须，温度，单位：摄氏度
     */
    private Double OTMP;

    /**
     * 必须，密度，代为：千克/升
     */
    private Double ODEN;

    /**
     * 油表开始读数
     */
    private Integer OMSL;

    /**
     * 油表结束读数
     */
    private Integer OMFL;

    /**
     * 必须，加油升数
     */
    private Integer OLLT;

    /**
     * 必须，加油重量
     */
    private Integer OLKG;

    /**
     * 地井编号
     */
    private String OHPN;

    /**
     * 加油车号
     */
    private String VNB;

    /**
     * 必须，加油开始时间
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TFBE;

    /**
     * 必须，加油结束时间
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TFEN;

    /**
     * 加油员姓名
     */
    private String TENM;
    private String TENM2;
    private String TENM3;
    private String TENM4;

    /**
     * 油单创建时间
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date CDAT;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 确认标识Y
     */
    private String SIGN_FLG;

    /**
     * 电子油单（PDF）数据，采用BASE64编码，对于有手写签名的油单，必须生成PDF格式的电子油单，然后上传。
     */
    private String EFS;

    /**
     * 补充-订单号
     */
    private String ORDER_NO;
    /**
     * LST
     * 补充-运油车号
     */
    private String TANKER_NO;
    /**
     * 补充-运输里程，单位：公里
     */
    private Integer TRANSPORT_MILEAGE;
    /**
     * 补充-费用调整额，>0：增加的费用，需要加入到油单总金额；<0 扣除的费用，需要从油单总金额中扣除
     */
    private Integer CHANGE_FEE;
    /**
     * 补充-备注信息，由加油员手动填写的备注内容
     */
    private String REMARK;
    /**
     * 补充-业务类型：1-通航业务，2-运输航业务，如果没有值默认为2-运输航业务
     */
    private String BUS_TYPE;
    /**
     * 补充-任务的唯一记录号
     */
    private String TNB;

    /**
     * 加油员的工号
     */
    private String TENB;

    /**
     * 加油员的工号
     */
    private String TENB2;

    /**
     * 加油员的工号
     */
    private String TENB3;

    /**
     * 加油员的工号
     */
    private String TENB4;

    /**
     * 必须，飞机所属单位名称
     */
    private String CSTNM;

    /**
     * 预加油量版本（飞行计划版本号）
     */
    private String OLVR;
    /**
     * 必须，供油部门代码，例如：“11”，每个机场/供应站固定配置
     */
    private String DPID;
    /**
     * 必须，供油工厂（油库）代码，例如：“2221”，每个机场/供应站固定配置
     */
    private String FACTORY;
    /**
     * 必须，机场（装运点）代码，例如：“2220”，每个机场/供应站固定配置
     */
    private String LOADID;
    /**
     * 必须，油罐（库存地点）代码，每个机场/供应站固定配置
     */
    private String INVENTORY;
    /**
     * 必须，物料类型，默认1，每个机场/供应站固定配置
     */
    private String MATERIAL;
    /**
     * 抽油原因代码（抽油单必须），取值如下：
     * Z01 试发
     * Z02 称重
     * Z03 清洗油箱
     * Z04 加多油
     * Z05 排故
     * Z09 其他
     * Z10 抗震救灾
     */
    private String DEFUEL_REASON_CODE;
    /**
     * 抽油原因（抽油单必须），取值见“抽油原因代码”字段
     */
    private String DEFUEL_REASON;
    /**
     * 抽油时是否进行油品检测（仅抽油单使用）：Y - 已检测（要收取检测费）；N-不检测（不收检测费），默认N；
     */
    private String DEFUEL_TEST;
    /**
     * 抽油后是否重新加注（仅抽油单使用）：Y-直接加注（收取重新注费）；N - 不加注（不收取重新加注费），默认N；
     */
    private String DEFUEL_REFRUEL;
    /**
     * 抽油保存天数，0 - 不保管（不收保管费）;>0 -保管的天数（要收保管费），默认N；
     */
    private String DEFUEL_STORAGE;
    /**
     * 必须，机型三码
     */
    private String ACT3;
    /**
     * 代结算站点代码（运点代码）
     */
    private String SAPC3;
    /**
     * 去向机场三码，自提、配送的目的地机场
     */
    private String DAPC3;
    /**
     * 代结算机场站点代码（运点代码），每个机场固定配置，默认与本地机场运点代码相同。
     */
    private String SLDID;
    /**
     * 收油机场（去向机场）三码，收油机场，自提、配送的目的地机场三码
     */
    private String RAPC3;
    /**
     * 收油机场（去向机场）名称，自提、配送的目的地机场名称
     */
    private String RAPCN;
    /**
     * 供油服务模式：
     * 0-机坪加注；1-自提；2-配送；
     */
    private String ADDOIL_TYPE;
}
