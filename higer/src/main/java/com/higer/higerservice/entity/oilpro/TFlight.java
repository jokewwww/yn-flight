package com.higer.higerservice.entity.oilpro;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 航班表（T_FLIGHT)
 */
@Data
@ToString
public class TFlight {

    /**
     * 航班ID   null
     */
    private String flgt_id;
    /**
     * 航班唯一标识码   航班ID 12.18 航班历史生成消息
     */
    private String flgt_ffid;

    /**
     * 新疆机场三字码
     */
    private String flgt_airport_code;

    private String flgt_aptarea_code;

    /**
     * 航班号      FLIGHTNO
     */
    private String flgh_flno;

    /**
     * 航班日期
     */
    private Date flgt_flop;

    /**
     * 飞机类型 CRAFTTYPE
     */
    private String flgt_acname;

    /**
     * 飞机号码   飞机号  CRAFTNO
     */
    private String flgt_regn;

    /**
     * 机位    SEATNAME
     */
    private String flgt_placecode;

    /**
     * 航空公司二字码 ARLINE
     */
    private String flgt_al2c;

    /**
     * 航空公司  CN
     */
    private String flgt_alcname;

    /**
     * 航线             ROUTELST routeorder排序
     */
    private String flgt_vialc;

    /**
     * 计划到达时间    PLANLANDING
     */
    private Date flgt_a_stot;

    /**
     * 预计到达时间        ALTERLANDING 发送航站消息
     */
    private Date flgt_a_etot;

    /**
     * 实际到达时间    REALLANDING
     */
    private Date flgt_a_atot;

    /**
     * 计划起飞时间  PLANTAKEOFF
     */
    private Date flgt_d_stot;

    /**
     * 预计起飞时间   ALTERTAKEOFF
     */
    private Date flgt_d_etot;

    /**
     * 实际起飞时间  REALTAKEOFF
     */
    private Date flgt_d_atot;

    /**
     * 出发地机场三字码
     */
    private String flgt_org3c;

    /**
     * 出发地机场
     */
    private String flgt_orgnm;

    /**
     * 经停机场三字码1    ROUTELST routeorder排序
     */
    private String flgt_trs3c1;
    /**
     * 经停机场1   ROUTELST routeorder排序
     */
    private String flgt_trsnm1;

    private String flgt_trs3c2;
    private String flgt_trsnm2;
    private String flgt_trs3c3;
    private String flgt_trsnm3;
    private String flgt_trs3c4;
    private String flgt_trsnm4;
    private String flgt_trs3c5;
    private String flgt_trsnm5;

    /**
     * 目的地机场三字码
     */
    private String flgt_des3c;

    /**
     * 目的地机场
     */
    private String flgt_desnm;

    /**
     * 进离港           ISARRFLIGHT  航班历史生成消息
     */
    private String flgt_adid;

    /**
     * 航班任务属性      航班历史生成消息 TASK
     */
    private String flgt_mission_prop;

    /**
     * 航班性质   ???
     */
    private String flgt_nature;

    /**
     * 航班性质细分   ???
     */
    private String flgt_sub_nature;

    /**
     * 航班国内/国际   REGIONID  航站消息  REGION
     */
    private String flgt_flti;

    /**
     * 航班状态   航班延误 ABNSTATUS
     */
    private String flgt_ftyp;

    /**
     * 延误原因    航班延误 ABNSTATUS
     */
    private String flgt_delaycode;

    /**
     * 航空服务代理   ???
     */
    private String flgt_proxy;

    /**
     * 连接航班号   SHARELST   12.14 共享航班消息
     */
    private String flgt_link_flno;

    /**
     * 远近机位   机位分配消息
     */
    private String flgt_fnflag;

    /**
     * 是否为货机   ???
     */
    private String flgt_ifsr;

    /**
     * 本场   ???
     */
    private String flgt_game;

    /**
     * 离港跑道   ???
     */
    private String flgt_dep_runway;

    /**
     * 到港跑道   ???
     */
    private String flgt_arr_runway;

    /**
     * 登机门           10.2 登机口分配事件
     */
    private String flgt_gate;

    /**
     * 机组到位时间   ???
     */
    private Date flgt_crew_in_place;

    /**
     * 上轮档时间        10.23 上轮档
     */
    private Date flgt_chocks_in;

    /**
     * 撤轮挡时间   10.24 撤轮档
     */
    private Date flgt_chocks_out;

    /**
     * 第一件行李时间   ???
     */
    private Date flgt_first_lugg;

    /**
     * 最后一件行李时间   ???
     */
    private Date flgt_last_lugg;

    /**
     * 前飞计划到达时间   ???
     */
    private Date flgt_ptax;

    /**
     * 前飞预计到达时间   ???
     */
    private Date flgt_etax;

    /**
     * 前飞实际到达时间   ???
     */
    private Date flgt_atax;

    /**
     * 起飞油量   ???
     */
    private Integer flgt_takeoff_fuel;

    /**
     * 轮挡油量   ???
     */
    private Integer flgt_chock_fuel;

    /**
     * 要客   ???
     */
    private Integer flgt_vip;

    /**
     * 任务下发标识   ???
     */
    private Integer flgt_task_asign;

    /**
     * 航班星标   ???
     */
    private Integer flgt_starmark;

    /**
     * 手动修改航班   ???
     */
    private Integer flgt_manual_flg;

}
