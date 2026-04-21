package com.higer.oildataexchange.entity.flight;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Table(name = "T_FLIGHT")
@Entity
@Data
public class TFlight implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 航班ID（uuid）
     */
    @Id
    @Column(insertable = false, name = "flgt_id", nullable = false)
    private String flgtId;

    /**
     * 航班唯一标识码（对接各航班接口的唯一标识码）
     */
    @Column(name = "flgt_ffid")
    private String flgtFfid;

    /**
     * 所属机场代码
     */
    @Column(name = "flgt_airport_code")
    private String flgtAirportCode;

    /**
     * 所属机场区域代码
     */
    @Column(name = "flgt_aptarea_code")
    private String flgtAptareaCode;

    /**
     * 航班号
     */
    @Column(name = "flgt_flno")
    private String flgtFlno;

    /**
     * 航班日期
     */
    @Column(name = "flgt_flop")
    private Date flgtFlop;

    /**
     * 飞机类型
     */
    @Column(name = "flgt_acname")
    private String flgtAcname;

    /**
     * 飞机号码
     */
    @Column(name = "flgt_regn")
    private String flgtRegn;

    /**
     * 机位
     */
    @Column(name = "flgt_placecode")
    private String flgtPlacecode;

    /**
     * 航空公司二字码
     */
    @Column(name = "flgt_al2c")
    private String flgtAl2c;

    /**
     * 航空公司
     */
    @Column(name = "flgt_alcname")
    private String flgtAlcname;

    /**
     * 航线（中文地名-中文地名）
     */
    @Column(name = "flgt_vialc")
    private String flgtVialc;

    /**
     * 计划到达时间
     */
    @Column(name = "flgt_a_stot")
    private Timestamp flgtAStot;

    /**
     * 预计到达时间
     */
    @Column(name = "flgt_a_etot")
    private Timestamp flgtAEtot;

    /**
     * 实际到达时间（落地时间）
     */
    @Column(name = "flgt_a_atot")
    private Timestamp flgtAAtot;

    /**
     * 计划起飞时间
     */
    @Column(name = "flgt_d_stot")
    private Timestamp flgtDStot;

    /**
     * 预计起飞时间
     */
    @Column(name = "flgt_d_etot")
    private Timestamp flgtDEtot;

    /**
     * 实际起飞时间（离地时间）
     */
    @Column(name = "flgt_d_atot")
    private Timestamp flgtDAtot;

    /**
     * 出发地机场三字码
     */
    @Column(name = "flgt_org3c")
    private String flgtOrg3c;

    /**
     * 出发地机场
     */
    @Column(name = "flgt_orgnm")
    private String flgtOrgnm;

    /**
     * 经停机场三字码1
     */
    @Column(name = "flgt_trs3c1")
    private String flgtTrs3c1;

    /**
     * 经停机场1
     */
    @Column(name = "flgt_trsnm1")
    private String flgtTrsnm1;

    /**
     * 经停机场三字码2
     */
    @Column(name = "flgt_trs3c2")
    private String flgtTrs3c2;

    /**
     * 经停机场2
     */
    @Column(name = "flgt_trsnm2")
    private String flgtTrsnm2;

    /**
     * 经停机场三字码3
     */
    @Column(name = "flgt_trs3c3")
    private String flgtTrs3c3;

    /**
     * 经停机场3
     */
    @Column(name = "flgt_trsnm3")
    private String flgtTrsnm3;

    /**
     * 经停机场三字码4
     */
    @Column(name = "flgt_trs3c4")
    private String flgtTrs3c4;

    /**
     * 经停机场4
     */
    @Column(name = "flgt_trsnm4")
    private String flgtTrsnm4;

    /**
     * 经停机场三字码5
     */
    @Column(name = "flgt_trs3c5")
    private String flgtTrs3c5;

    /**
     * 经停机场5
     */
    @Column(name = "flgt_trsnm5")
    private String flgtTrsnm5;

    /**
     * 目的地机场三字码
     */
    @Column(name = "flgt_des3c")
    private String flgtDes3c;

    /**
     * 目的地机场
     */
    @Column(name = "flgt_desnm")
    private String flgtDesnm;

    /**
     * 进离港（A：进港，D：出港）
     */
    @Column(name = "flgt_adid")
    private String flgtAdid;

    /**
     * 航班任务属性
     */
    @Column(name = "flgt_mission_prop")
    private String flgtMissionProp;

    /**
     * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     */
    @Column(name = "flgt_nature")
    private String flgtNature;

    /**
     * 航班性质细分
     */
    @Column(name = "flgt_sub_nature")
    private String flgtSubNature;

    /**
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    @Column(name = "flgt_flti")
    private String flgtFlti;

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    @Column(name = "flgt_ftyp")
    private String flgtFtyp;

    @Column(name = "flgt_a_errftyp")
    private String flgtAErrftyp;

    @Column(name = "flgt_d_errftyp")
    private String flgtDErrftyp;

    /**
     * 延误原因
     */
    @Column(name = "flgt_delaycode")
    private String flgtDelaycode;

    /**
     * 航空服务代理（YAG（机场代理），CES（东航代理））
     */
    @Column(name = "flgt_proxy")
    private String flgtProxy;

    @Column(name = "flgt_link_ffid")
    private String flgtLinkFfid;

    /**
     * 连接航班号（共享航班号1，共享航班号2）
     */
    @Column(name = "flgt_link_flno")
    private String flgtLinkFlno;

    /**
     * 连接航班日期
     */
    @Column(name = "flgt_link_flop")
    private Date flgtLinkFlop;

    /**
     * 连接航班连接次数
     */
    @Column(name = "flgt_link_repeat")
    private Integer flgtLinkRepeat;

    /**
     * 航班连接次数
     */
    @Column(name = "flgt_repeat")
    private Integer flgtRepeat;

    /**
     * 远近机位（N：近机位，F：远机位）
     */
    @Column(name = "flgt_fnflag")
    private String flgtFnflag;

    /**
     * 是否为货机（Y：货机，N：非货机）
     */
    @Column(name = "flgt_ifsr")
    private String flgtIfsr;

    /**
     * 本场（Y：本场，N：（空））
     */
    @Column(name = "flgt_game")
    private String flgtGame;

    /**
     * 离港跑道
     */
    @Column(name = "flgt_dep_runway")
    private String flgtDepRunway;

    /**
     * 到港跑道
     */
    @Column(name = "flgt_arr_runway")
    private String flgtArrRunway;

    /**
     * 登机门  变成远近机位颜色
     */
    @Column(name = "flgt_gate")
    private String flgtGate;

    /**
     * 机组到位时间
     */
    @Column(name = "flgt_crew_in_place")
    private Timestamp flgtCrewInPlace;

    /**
     * 上轮档时间
     */
    @Column(name = "flgt_chocks_in")
    private Timestamp flgtChocksIn;

    /**
     * 撤轮挡时间
     */
    @Column(name = "flgt_chocks_out")
    private Timestamp flgtChocksOut;

    /**
     * 第一件行李时间
     */
    @Column(name = "flgt_first_lugg")
    private Timestamp flgtFirstLugg;

    /**
     * 最后一件行李时间
     */
    @Column(name = "flgt_last_lugg")
    private Timestamp flgtLastLugg;

    /**
     * 前飞计划到达时间
     */
    @Column(name = "flgt_ptax")
    private Timestamp flgtPtax;

    /**
     * 前飞预计到达时间
     */
    @Column(name = "flgt_etax")
    private Timestamp flgtEtax;

    /**
     * 前飞实际到达时间
     */
    @Column(name = "flgt_atax")
    private Timestamp flgtAtax;

    /**
     * 起飞油量
     */
    @Column(name = "flgt_takeoff_fuel")
    private Integer flgtTakeoffFuel;

    /**
     * 轮挡油量
     */
    @Column(name = "flgt_chock_fuel")
    private Integer flgtChockFuel;

    /**
     * 预计加油量
     */
    @Column(name = "flgt_otat_fuel")
    private Integer flgtOtatFuel;

    /**
     * 要客（人数）
     */
    @Column(name = "flgt_vip")
    private Integer flgtVip;

    /**
     * 序号
     */
    @Column(name = "flgt_num")
    private Integer flgtNum;

    /**
     * 任务下发标识（0：未下发，1：已下发）
     */
    @Column(name = "flgt_task_asign")
    private Integer flgtTaskAsign;

    /**
     * 航班星标（1：是，0：不是）
     */
    @Column(name = "flgt_starmark")
    private Integer flgtStarmark;

    /**
     * 手动修改航班（1：手动，0：非手动）
     */
    @Column(name = "flgt_manual_flg")
    private Integer flgtManualFlg;

    /**
     * 航班合并flg 0 否 1 是
     */
    @Column(name = "flgt_share_no_flg")
    private Integer flgtShareNoFlg;

    /**
     * 航段
     */
    @Column(name = "flgt_otc")
    private String flgtOtc;

    /**
     * 飞机号是否改变  0 未改变  1 已改变
     */
    @Column(name = "flgt_regn_status")
    private Integer flgtRegnStatus;

    /**
     * 机位号是否改变  0 未改变  1 已改变
     */
    @Column(name = "flgt_placecode_status")
    private Integer flgtPlacecodeStatus;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 预加油量（或起飞油量）的机组确认标记：Y-机组已经确认；N-机组未确认
     */
    @Column(name = "flgt_otat")
    private String flgtOtat;

    /**
     * 预加油量发布版本（飞行计划版本号）
     */
    @Column(name = "flgt_olvr")
    private String flgtOlvr;

}