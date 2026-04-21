package com.higer.oildataexchange.entity.oil;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "T_FUEL_RECPT")
public class TFuelRecpt implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 加油单ID（uuid）
     */
    @Id
    @Column(name = "flrc_id", insertable = false, nullable = false)
    private String flrcId;

    /**
     * （1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油 7:  内航国内补加油 8: 内航 离境 补加油 9 : 外航 补油）
     */
    @Column(name = "flrc_type", nullable = false)
    private Integer flrcType;

    /**
     * 加油单编号
     */
    @Column(name = "flrc_no", nullable = false)
    private String flrcNo;

    /**
     * 加油日期
     */
    @Column(name = "flrc_date", nullable = false)
    private Date flrcDate;

    /**
     * 加抽油日期
     */
    @Column(name = "flrc_wkop_date", nullable = false)
    private Date flrcWkopDate;

    /**
     * 所属机场代码
     */
    @Column(name = "flrc_airport_code")
    private String flrcAirportCode;

    /**
     * 所属机场区域代码
     */
    @Column(name = "flrc_aptarea_code")
    private String flrcAptareaCode;

    /**
     * 机场名称（机场名全称）
     */
    @Column(name = "flrc_airport")
    private String flrcAirport;

    /**
     * 飞机所属单位代码（航空公司ICAO二字码）
     */
    @Column(name = "flrc_airl_code")
    private String flrcAirlCode;

    /**
     * 飞机所属单位（航空公司全称）
     */
    @Column(name = "flrc_airl_name")
    private String flrcAirlName;

    /**
     * 航班号
     */
    @Column(name = "flrc_flight_no", nullable = false)
    private String flrcFlightNo;

    /**
     * 飞机号码
     */
    @Column(name = "flrc_aircrft_no")
    private String flrcAircrftNo;

    /**
     * 飞机类型
     */
    @Column(name = "flrc_aircrft_type")
    private String flrcAircrftType;

    /**
     * 起始（机场名称+“/”+机场IATA三码）
     */
    @Column(name = "flrc_departure")
    private String flrcDeparture;

    /**
     * 经停（备降）（机场名称+“/”+机场IATA三码）
     */
    @Column(name = "flrc_transit")
    private String flrcTransit;

    /**
     * 终点（机场名称+“/”+机场IATA三码）
     */
    @Column(name = "flrc_dest")
    private String flrcDest;

    /**
     * 化验单编号
     */
    @Column(name = "flrc_test_bill_no")
    private String flrcTestBillNo;

    /**
     * 油品名称
     */
    @Column(name = "flrc_fuel_name")
    private String flrcFuelName;

    /**
     * 温度（摄氏度）
     */
    @Column(name = "flrc_fuel_temp")
    private Double flrcFuelTemp;

    /**
     * 密度（克/毫升（g/cm³））
     */
    @Column(name = "flrc_fuel_dnst")
    private Double flrcFuelDnst;

    /**
     * 体积（升）
     */
    @Column(name = "flrc_fuel_vol")
    private Double flrcFuelVol;

    /**
     * 计量表开始读数
     */
    @Column(name = "flrc_meter_stat")
    private Integer flrcMeterStat;

    /**
     * 计量表结束读数
     */
    @Column(name = "flrc_meter_fnsh")
    private Integer flrcMeterFnsh;

    /**
     * 加油数量小写（升）
     */
    @Column(name = "flrc_figuars", nullable = false)
    private Integer flrcFiguars;

    /**
     * 加油数量大写（升）
     */
    @Column(name = "flrc_figuars_word", nullable = false)
    private String flrcFiguarsWord;

    /**
     * 加油质量（千克）
     */
    @Column(name = "flrc_quantity")
    private Double flrcQuantity;

    /**
     * 加油车 车牌号
     */
    @Column(name = "flrc_vehi_no")
    private String flrcVehiNo;

    /**
     * 加油车编号
     */
    @Column(name = "flrc_vehi_num")
    private String flrcVehiNum;

    /**
     * 地井编号
     */
    @Column(name = "flrc_hydrt_pit_no")
    private String flrcHydrtPitNo;

    /**
     * 加油开始时间
     */
    @Column(name = "flrc_stat_time")
    private Timestamp flrcStatTime;

    /**
     * 加油结束时间
     */
    @Column(name = "flrc_fnsh_time")
    private Timestamp flrcFnshTime;

    /**
     * 加油员ID
     */
    @Column(name = "flrc_deliver_id")
    private String flrcDeliverId;

    /**
     * 加油员姓名
     */
    @Column(name = "flrc_deliver_name")
    private String flrcDeliverName;

    /**
     * 签名（JPG图片的base64编码）
     */
    @Column(name = "flrc_sign")
    private String flrcSign;

    /**
     * 手动修改标记（0：自动生成，1：手动修改）
     */
    @Column(name = "flrc_manual_tag")
    private Integer flrcManualTag = 0;

    /**
     * 手动修改人员
     */
    @Column(name = "flrc_manual_staff")
    private String flrcManualStaff;

    /**
     * 手动修改时间
     */
    @Column(name = "flrc_manual_time")
    private String flrcManualTime;

    /**
     * 审批状态（0：未审批，1：已审批）
     */
    @Column(name = "flrc_revw_status")
    private Integer flrcRevwStatus = 0;

    /**
     * 油单是否回收（0：未回收，1：已回收）
     */
    @Column(name = "flrc_takeback_flg")
    private Integer flrcTakebackFlg = 0;

    /**
     * 记录创建时间
     */
    @Column(name = "flrc_rec_cre_time", nullable = false)
    private Date flrcRecCreTime;

    /**
     * 是否导出
     */
    @Column(name = "flrc_is_export")
    private Integer flrcIsExport = 0;

    /**
     * 0未上传  1 上传失败 2 上传成功 3 新增 4 修改
     */
    @Column(name = "flrc_status")
    private Integer flrcStatus = 0;

    @Column(name = "flrc_update_err_msg")
    private String flrcUpdateErrMsg;

    /**
     * 1 成功  2 失败
     */
    @Column(name = "flrc_confirm_status")
    private Integer flrcConfirmStatus;

    @Column(name = "flrc_confirm_err_msg")
    private String flrcConfirmErrMsg;

    /**
     * 1 成功 2 失败
     */
    @Column(name = "flrc_pay_status")
    private Integer flrcPayStatus;

    @Column(name = "flrc_pay_err_msg")
    private String flrcPayErrMsg;

    /**
     * 保税 B  非保税 FB
     */
    @Column(name = "flrc_bwtar")
    private String flrcBwtar;

    /**
     * 购买方编号
     */
    @Column(name = "arcr_custom_num")
    private String arcrCustomNum;

    /**
     * 航班进出港属性
     */
    @Column(name = "flgt_adid")
    private String flgtAdid;

    /**
     * 航班机位属性
     */
    @Column(name = "flgt_placecode")
    private String flgtPlacecode;

    /**
     * 版本号
     */
    @Column(name = "flrc_version")
    private Integer flrcVersion = 0;

    /**
     * 电子油单base64
     */
    @Column(name = "flrc_single")
    private String flrcSingle;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private Long orderId;

    @Transient
    private String flgtOlvr;

    /**
     * 抽油原因（抽油单必须），取值见“抽油原因代码”字段
     */
    @Column(name = "flrc_defuel_reason")
    private String flrcDefuelReason;
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
    @Column(name = "flrc_defuel_reason_code")
    private String flrcDefuelReasonCode;

    /**
     * 抽油时是否进行油品检测（仅抽油单使用）：Y - 已检测（要收取检测费）；N-不检测（不收检测费），默认N；
     */
    @Column(name = "flrc_defuel_test")
    private String flrcDefuelTest;

    /**
     * 抽油后是否重新加注（仅抽油单使用）：Y-直接加注（收取重新注费）；N - 不加注（不收取重新加注费），默认N；
     */
    @Column(name = "flrc_defuel_refruel")
    private String flrcDefuelRefruel;

    /**
     * 抽油保存天数，0 - 不保管（不收保管费）;>0 -保管的天数（要收保管费），默认N；
     */
    @Column(name = "flrc_defuel_storage")
    private String flrcDefuelStorage;

    @Transient
    private String taskId;

    /**
     * 供油类型 0 加注 1 自提 2 配送
     */
    @Column(name = "flrc_supply_fuel_type")
    private Integer flrcSupplyFuelType;

    /**
     * 备注信息
     */
    @Column(name = "flrc_remark")
    private String flrcRemark;

    /**
     * 收油机场（去向机场）三码，收油机场，自提、配送的目的地机场三码
     */
    @Column(name = "flrc_rapc3")
    private String flrcRapc3;

    /**
     * 收油机场（去向机场）名称，收油机场，自提、配送的目的地机场名称
     */
    @Column(name = "flrc_rapcn")
    private String flrcRapcn;

    /**
     * 运油车号（仅自提、配送方式需要）
     */
    @Column(name = "flrc_tanker_no")
    private String flrcTankerNo;

    /**
     * 运输里程，单位：公里（仅配送方式需要）
     */
    @Column(name = "flrc_transport_mileage")
    private Integer flrcTransportMileage;

    /**
     * 业务类型：1-通航业务，2-运输航业务，如果没有值默认为2-运输航业务
     */
    @Column(name = "flrc_bus_type")
    private Integer flrcBusType;

    /**
     * 代结算机场三码（即销售结算的记账机场），每个机场固定配置，默认与供油机场相同；主要用于系统的销售记账
     */
    @Column(name = "settlement_rapc3")
    private String settlementRapc3;

    /**
     * 代结算机场名称
     */
    @Column(name = "settlement_rapcn")
    private String settlementRapcn;
}