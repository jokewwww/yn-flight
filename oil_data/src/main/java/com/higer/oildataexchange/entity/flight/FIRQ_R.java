package com.higer.oildataexchange.entity.flight;

import com.higer.oildataexchange.common.TflightUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "SUB")
public class FIRQ_R {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 必须，航班唯一记录号
     */
    private String FKEY;

    /**
     * 必须，航班所在机场三码
     */
    private String APC3;

    /**
     * 必须，航班日期
     */
    private String FLOP;

    /**
     * 必须，航班的星期数
     */
    private String WDAY;

    /**
     * 必须，航班号
     */
    private String FLNO;

    /**
     * 必须，进离港
     */
    private String ADID;

    /**
     * 必须，航班性质，例如：W/Z，H/Z等
     */
    private String TTYP;

    /**
     * 必须，航线性质，D-国内；I-国际；R-地区（港澳台）、M-混合（航线包含国内航段以及国际或地区航段）
     */
    private String FLTI;

    /**
     * 航班状态，X-取消；Y-延误，其他正常
     */
    private String FTYP;

    /**
     * 机号
     */
    private String REGN;

    /**
     * 机型3码
     */
    private String ACT3;

    /**
     * 机型代码（完整代码）
     */
    private String ACTN;

    /**
     * 航空公司二码
     */
    private String ALC2;

    /**
     * 航空公司名称
     */
    private String ALCNM;

    /**
     * 必须，始发地机场3码
     */
    private String ORG3;

    /**
     * 始发地机场名称
     */
    private String ORGNM;

    /**
     * 必须，目的地机场3码
     */
    private String DES3;

    /**
     * 目的地机场名称
     */
    private String DESNM;

    /**
     * 经停机场3码
     */
    private String VIA3;

    /**
     * 经停机场名称
     */
    private String VIANM;

    /**
     * 航线代码列表
     */
    private String VIAL;

    /**
     * 航线中文名称列表
     */
    private String VIALC;

    /**
     * 备降机场3码
     */
    private String BJA3;

    /**
     * 备降机场名称
     */
    private String BJANM;

    /**
     * 机位
     */
    private String PSN;

    /**
     * 机位所在的区域代码（机坪划区）
     */
    private String AREA;

    /**
     * 航站楼标识，例如T1，T2
     */
    private String TERMN;

    /**
     * 航后机位
     */
    private String PSTW;

    /**
     * 是否为货机
     */
    private String IFSR;

    /**
     * VIP类型
     */
    private String VIPS;

    /**
     * VIP标志
     */
    private String VIPM;

    /**
     * 共享航班
     */
    private String SFLN;

    /**
     * 必须，航班计划到达/起飞时间
     */
    private String STOT;

    /**
     * 航班变更到达/起飞时间
     */
    private String ETOT;

    /**
     * 航班实际到达/起飞时间
     */
    private String ATOT;

    /**
     * 实际或变更时间与计划时间误差
     */
    private String MODT;

    /**
     * 计划上轮挡/入位时间
     */
    private String STIP;

    /**
     * 预计上轮档/入位时间
     */
    private String ETIP;

    /**
     * 实际上轮档/入位时间
     */
    private String ATIP;

    /**
     * 计划撤轮挡/推出时间
     */
    private String STPO;

    /**
     * 预计撤轮档/推出时间
     */
    private String ETPO;

    /**
     * 实际撤轮档/推出时间
     */
    private String ATPO;

    /**
     * 关舱门时间
     */
    private String FDCT;

    /**
     * 开舱门时间
     */
    private String FDOT;

    /**
     * 飞机靠桥时间
     */
    private String BBAA;

    /**
     * 飞机撤桥时间
     */
    private String BBFA;

    /**
     * 进走廊时间
     */
    private String ICHN;

    /**
     * 滑出（跑道）时间
     */
    private String SOUT;

    /**
     * CDM时间
     */
    private String CTOT;

    /**
     * 前站计划起飞时间
     */
    private String OSTD;

    /**
     * 前站变更起飞时间
     */
    private String OETD;

    /**
     * 前站实际起飞时间
     */
    private String OTKF;

    /**
     * 预计到达下站时间
     */
    private String DETA;

    /**
     * 实际到达下站时间
     */
    private String DLND;

    /**
     * 航班运行状态，例如：LND-落地；CKI-开始值机、CKO-值机结束、BRD-开始登机等
     */
    private String STAT;

    /**
     * 国内值机柜台号
     */
    private String DCIC;

    /**
     * 国际值机柜台号
     */
    private String ICIC;

    /**
     * 国内柜台打开时间
     */
    private String DOIT;

    /**
     * 国际柜台打开时间
     */
    private String IOIT;

    /**
     * 国内柜台关闭时间
     */
    private String DCIT;

    /**
     * 国际柜台关闭时间
     */
    private String ICIT;

    /**
     * 国内上客时间
     */
    private String DPBT;

    /**
     * 国际上客时间
     */
    private String IPBT;

    /**
     * 国内客齐时间
     */
    private String DPOT;

    /**
     * 国际客齐时间
     */
    private String IPOT;

    /**
     * 航班国内出站口（进港））/登机口（出港）
     */
    private String DGAT;

    /**
     * 航班国际出站口（进港））/登机口（出港）
     */
    private String IGAT;

    /**
     * 国内分拣口号（Baggage sorting port）
     */
    private String DBSP;

    /**
     * 国际分拣口号
     */
    private String IBSP;

    /**
     * 国内行李转盘
     */
    private String DBLT;

    //TODO 国际行李转盘

    /**
     * 起飞油量/总油量（千克）
     */
    private Integer OLTT;

    /**
     * 剩余油量/轮挡油量（千克）
     */
    private Integer OLTL;

    /**
     * 预加油量（千克）
     */
    private Integer OEST;

    /**
     * 延误或取消原因
     */
    private String DRSN;

    /**
     * 航班取消时间
     */
    private String CXDT;

    /**
     * 航班记录是否删除：Y-删除；N或空-未删除
     */
    private String IFDEL;

    /**
     * 配对航班标识（具有相同配对标识的航班为连班）
     */
    private String RKEY;

    /**
     * 记录创建时间
     */
    private String CDAT;

    /**
     * 预加油量（或起飞油量）的机组确认标记：Y-机组已经确认；N-机组未确认。
     */
    private String OTAT;
    /**
     * 记录最后更新时间，一般是指航班业务动态的变更时间，不包含程序进行其他处理而导致更新的时间，与LUTS含义不同，取值可能会不一样。
     */
    private String LSTU;

    /**
     * 预加油量版本（飞行计划版本号）
     */
    private String OLVR;


    public TFlight toFlight() {
        TFlight tFlight = new TFlight();
        tFlight.setFlgtFfid(FKEY);
        tFlight.setFlgtFlop(TflightUtils.parseDate(FLOP));
        tFlight.setFlgtFlno(FLNO);
        tFlight.setFlgtAdid(ADID);
        tFlight.setFlgtMissionProp(TTYP);
        tFlight.setFlgtFlti(FLTI);
        tFlight.setFlgtFtyp(FTYP);
        tFlight.setFlgtRegn(REGN);
        tFlight.setFlgtAcname(ACT3);
        tFlight.setFlgtAl2c(ALC2);
        tFlight.setFlgtAlcname(ALCNM);
        tFlight.setFlgtOrg3c(ORG3);
        tFlight.setFlgtOrgnm(ORGNM);
        tFlight.setFlgtDes3c(DES3);
        tFlight.setFlgtDesnm(DESNM);
        tFlight.setFlgtTrs3c1(VIA3);
        tFlight.setFlgtTrsnm1(VIANM);
        tFlight.setFlgtVialc(VIAL);
        tFlight.setFlgtPlacecode(PSN);
        tFlight.setFlgtTakeoffFuel(OLTT);//起飞油量
        tFlight.setFlgtChockFuel(OLTL);//轮档油量
        tFlight.setFlgtOtatFuel(OEST);//预加油量
        tFlight.setFlgtOtat(OTAT);
        tFlight.setFlgtOlvr(OLVR);
        return tFlight;
    }
}
