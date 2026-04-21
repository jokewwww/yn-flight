package com.higer.flightinfo.entity;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:08
 * @Description:
 */
@Data
@Entity
@Table(name = "t_flight")
public class TFlight implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String flgtId;//UUID
    @Column(length = 60)
    private String flgtFfid;//航班唯一标识码（对接各航班接口的唯一标识码）
    @Column(length = 60)
    private String flgtAirportCode;//所属机场代码
    @Column(length = 60)
    private String flgtAptareaCode;//所属机场区域代码
    @Column(length = 60)
    private String flgtFlno;//航班号
    @Column(length = 60)
    private Date flgtFlop;//航班日期
    @Column(length = 60)
    private String flgtAcname;//飞机类型
    @Column(length = 60)
    private String flgtRegn;//飞机号码
    @Column(length = 60)
    private String flgtPlacecode;//机位
    @Column(length = 60)
    private String flgtAl2C;//航空公司二字码
    @Column(length = 60)
    private String flgtAlcname;//航空公司
    @Column(length = 60)
    private String flgtVialc;//航线（中文地名-中文地名）
    @Column(length = 60)
    private Date flgtAStot;//计划到达时间
    @Column(length = 60)
    private Date flgtAEtot;//预计到达时间
    @Column(length = 60)
    private Date flgtAAtot;//实际到达时间（落地时间）
    @Column(length = 60)
    private Date flgtDStot;//计划起飞时间
    @Column(length = 60)
    private Date flgtDEtot;//预计起飞时间
    @Column(length = 60)
    private Date flgtDAtot;//实际起飞时间（离地时间）
    @Column(length = 60)
    private String flgtOrg3C;//出发地机场三字码
    @Column(length = 60)
    private String flgtOrgnm;//出发地机场
    @Column(length = 60)
    private String flgtTrs3C1;//经停机场三字码1
    @Column(length = 60)
    private String flgtTrsnm1;//经停机场1
    @Column(length = 60)
    private String flgtTrs3C2;//经停机场三字码2
    @Column(length = 60)
    private String flgtTrsnm2;//经停机场2
    @Column(length = 60)
    private String flgtTrs3C3;//经停机场三字码3
    @Column(length = 60)
    private String flgtTrsnm3;//经停机场3
    @Column(length = 60)
    private String flgtTrs3C4;//经停机场三字码4
    @Column(length = 60)
    private String flgtTrsnm4;//经停机场4
    @Column(length = 60)
    private String flgtTrs3C5;//经停机场三字码5
    @Column(length = 60)
    private String flgtTrsnm5;//经停机场5
    @Column(length = 60)
    private String flgtDes3C;//目的地机场三字码
    @Column(length = 60)
    private String flgtDesnm;//目的地机场
    @Column(length = 60)
    private String flgtAdid;//进离港（A：进港，D：出港）
    @Column(length = 60)
    private String flgtMissionProp;//航班任务属性
    @Column(length = 60)
    private String flgtNature;//航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
    @Column(length = 60)
    private String flgtSubNature;//航班性质细分
    @Column(length = 60)private String flgtFlti;//航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
    @Column(length = 60)private String flgtFtyp;//航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
   @Column(length = 60)private String flgtDelaycode;//延误原因
   @Column(length = 60)private String flgtProxy;//航空服务代理（YAG（机场代理），CES（东航代理））
   @Column(length = 60)private String flgtLinkFlno;//连接航班号（共享航班号1，共享航班号2）
   @Column(length = 60)private Date flgtLinkFlop;//连接航班日期
   @Column(length = 60)private Integer flgtLinkRepeat;//连接航班连接次数
   @Column(length = 60)private Integer flgtRepeat;//航班连接次数
   @Column(length = 60)private String flgtFnflag;//远近机位（N：近机位，F：远机位）
   @Column(length = 60)private String flgtIfsr;//是否为货机（Y：货机，N：非货机）
   @Column(length = 60)private String flgtGame;//本场（Y：本场，N：（空））
   @Column(length = 60)private String flgtDepRunway;//离港跑道
   @Column(length = 60)private String flgtArrRunway;//到港跑道
   @Column(length = 60)private String flgtGate;//登机门
   @Column(length = 60)private Date flgtCrewInPlace;//机组到位时间
   @Column(length = 60)private Date flgtChocksIn;//上轮档时间
   @Column(length = 60)private Date flgtChocksOut;//撤轮挡时间
   @Column(length = 60)private Date flgtFirstLugg;//第一件行李时间
   @Column(length = 60)private Date flgtLastLugg;//最后一件行李时间
   @Column(length = 60)private Date flgtPtax;//前飞计划到达时间
   @Column(length = 60)private Date flgtEtax;//前飞预计到达时间
   @Column(length = 60)private Date flgtAtax;//前飞实际到达时间
   @Column(length = 60)private Integer flgtTakeoffFuel;//起飞油量
   @Column(length = 60)private Integer flgtChockFuel;//轮挡油量
   @Column(length = 60)private Integer flgtVip;//要客（人数）
   @Column(length = 60)private Integer flgtNum;//序号
   @Column(length = 60)private Integer flgtTaskAsign;//任务下发标识（0：未下发，1：已下发）
   @Column(length = 60)private Integer flgtStarmark;//航班星标（1：是，0：不是）
   @Column(length = 60)private Integer flgtManualFlg;//手动修改航班（1：手动，0：非手动）
   @Column(length = 60)private Integer flgtShareNoFlg;//航班合并flg 0 否 1 是
   @Column(length = 60)private String flgtOtc;//航段
   @Column(length = 60)private String msgSeqn; //消息序号

}
