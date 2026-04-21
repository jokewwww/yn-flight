package com.ncse.fdds.xmlbean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/2 16:48
 * @Description:
 */
public class MyFlightTask implements Serializable {

    /**
     * T_FLIGHT
     */
    private static final long serialVersionUID = 1L;
    /**
     * 航班ID（uuid）
     */
    private String flgtId;

    /**
     * 航班唯一标识码（对接各航班接口的唯一标识码）
     */
    private String flgtFfid;

    /**
     * 所属机场代码
     */
    private String flgtAirportCode;

    /**
     * 所属机场区域代码
     */
    private String flgtAptareaCode;

    /**
     * 航班号
     */
    private String flgtFlno;

    /**
     * 航班日期（航班执行日期）
     */
    private Date flgtFlop;

    /**
     * 飞机类型
     */
    private String flgtAcname;

    /**
     * 飞机号码
     */
    private String flgtRegn;

    /**
     * 机位
     */
    private String flgtPlacecode;

    /**
     * 航空公司二字码
     */
    private String flgtAl2c;

    /**
     * 航空公司
     */
    private String flgtAlcname;

    /**
     * 航线（中文地名-中文地名（-中文地名））
     */
    private String flgtVialc;

    /**
     * 计划到达时间
     */
    private Date flgtAStot;

    /**
     * 预计到达时间
     */
    private Date flgtAEtot;

    /**
     * 实际到达时间（或称落地时间）
     */
    private Date flgtAAtot;

    /**
     * 计划起飞时间
     */
    private Date flgtDStot;

    /**
     * 预计起飞时间
     */
    private Date flgtDEtot;

    /**
     * 实际起飞时间（或称离地时间）
     */
    private Date flgtDAtot;

    /**
     * 出发地机场三字码
     */
    private String flgtOrg3c;

    /**
     * 出发地机场
     */
    private String flgtOrgnm;

    /**
     * 经停机场三字码1
     */
    private String flgtTrs3c1;

    /**
     * 经停机场1
     */
    private String flgtTrsnm1;

    /**
     * 经停机场三字码2
     */
    private String flgtTrs3c2;

    /**
     * 经停机场2
     */
    private String flgtTrsnm2;

    /**
     * 经停机场三字码3
     */
    private String flgtTrs3c3;

    /**
     * 经停机场3
     */
    private String flgtTrsnm3;

    /**
     * 经停机场三字码4
     */
    private String flgtTrs3c4;

    /**
     * 经停机场4
     */
    private String flgtTrsnm4;

    /**
     * 经停机场三字码5
     */
    private String flgtTrs3c5;

    /**
     * 经停机场5
     */
    private String flgtTrsnm5;

    /**
     * 目的地机场三字码
     */
    private String flgtDes3c;

    /**
     * 目的地机场（中文地名）
     */
    private String flgtDesnm;

    /**
     * 进离港（A：进港，D：出港）
     */
    private String flgtAdid;

    /**
     * 航班任务属性
     */
    private String flgtMissionProp;

    /**
     * 进港航班任务属性
     */
    private String flgtMissionPropIn;

    /**
     * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
     */
    private String flgtNature;

    /**
     * 航班性质细分
     */
    private String flgtSubNature;

    /**
     * 关联计划起飞时间
     *
     */
    private String flgtLinkDStot;
    /**
     * 关联预计起飞时间
     *
     */
    private String flgtLinkDEtot;
    /**
     * 关联实际起飞时间
     */
    private String flgtLinkDAtot;
    /**
     * 关联进出港
     *
     */
    private String flgtLinkAdid;

    /**
     * 关联计划到达时间
     *
     */
    private String flgtLinkAStot;
    /**
     * 关联预计到达时间
     *
     */
    private String flgtLinkAEtot;
    /**
     * 关联实际到达时间
     *
     */
    private String flgtLinkAAtot;
    /**
     * 航班订阅状态
     *
     */
    private String flgtstatus;
    /**
     * 出发机场名全称
     *
     */
    private String apcdSAirportName;
    /**
     * 目的地机场全称
     *
     */
    private String apcdEAirportName;
    /**
     * 出发机场名简称
     *
     */
    private String apcdSAirportNames;
    /**
     * 目的地机场简称
     *
     */
    private String apcdEAirportNames;
    /**
     * 航空公司全称
     *
     */
    private String alcdArlnName;
    /**
     * 航空公司简称
     *
     */
    private String alcdArlnNames;
    /**
     * 航线简称
     *
     */
    private String flightValic;
    /**
     * 进港航班机位
     *
     */
    private String flgtPlacecodeIn;

    /**
     * 业务字段 备注
     */
    private String flgtbezu;

    /**
     * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
     */
    private String flgtFlti;

    /**
     * 航班状态（X：取消，Y：延误，S：正常，R：返航，D：备降，B：滑回）
     */
    private String flgtFtyp;

    /**
     * 延误原因
     */
    private String flgtDelaycode;

    /**
     * 航空服务代理（YAG（机场代理），CES（东航代理））
     */
    private String flgtProxy;

    /**
     * 连接航班号（共享航班号1，共享航班号2）
     */
    private String flgtLinkFlno;

    /**
     * 远近机位（N：近机位，F：远机位）
     */
    private String flgtFnflag;

    /**
     * 是否为货机（Y：货机，N：非货机）
     */
    private String flgtIfsr;

    /**
     * 本场（Y：本场，N：（空））
     */
    private String flgtGame;

    /**
     * 离港跑道
     */
    private String flgtDepRunway;

    /**
     * 到港跑道
     */
    private String flgtArrRunway;

    /**
     * 登机门
     */
    private String flgtGate;

    /**
     * 机组到位时间
     */
    private Date flgtCrewInPlace;

    /**
     * 上轮档时间
     */
    private Date flgtChocksIn;

    /**
     * 撤轮挡时间
     */
    private Date flgtChocksOut;

    /**
     * 第一件行李时间
     */
    private Date flgtFirstLugg;

    /**
     * 最后一件行李时间
     */
    private Date flgtLastLugg;

    /**
     * 前飞计划到达时间
     */
    private Date flgtPtax;

    /**
     * 前飞预计到达时间
     */
    private Date flgtEtax;

    /**
     * 前飞实际到达时间
     */
    private Date flgtAtax;

    /**
     * 起飞油量
     */
    private Integer flgtTakeoffFuel;

    /**
     * 轮挡油量
     */
    private Integer flgtChockFuel;

    /**
     * 要客（人数）
     */
    private Integer flgtVip;

    /**
     * 任务下发标识（0：未下发，1：已下发）
     */
    private Integer flgtTaskAsign;

    /**
     * 航班星标（1：是，0：不是）默认0
     */
    private Integer flgtStarmark;

    /**
     * 手动修改航班（1：手动，0：非手动）
     */
    private Integer flgtManualFlg;

    /**
     * 连接航班日期
     */
    private Date flgtLinkFlop;

    /**
     * 连接航班连接次数
     */
    private Integer flgtLinkRepeat;

    /**
     * 航班连接次数
     */
    private Integer flgtRepeat;

    /**
     * 航班航段
     */
    private String flgtOtc;
    /**
     * 进港航班国际国内
     */
    private String flgtFltiIn;

    /**
     * T_TASK
     * <p>
     * <p>
     * 任务ID（uuid）
     */
    private String taskId;

    /**
     * 加油员员工ID
     */
    private String taskOpeStaffId;

    /**
     * 加油员员工名称
     */
    private String taskOpeStaffName;

    /**
     * 任务内容（0：加油，1：抽油）
     */
    private Integer taskContent;

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     */
    private Integer taskStatus;

    /**
     * 任务派发时间
     */
    private Date taskAsgTime;

    /**
     * 任务接受时间
     */
    private Date taskAccTime;

    /**
     * 加油开始时间
     */
    private Date taskChagStaTime;

    /**
     * 打印油单完成时间
     */
    private Date taskRcPrintTime;
    /**
     * 加油到位时间
     */
    private Date taskArriveTime;
    /**
     * 加油完成时间
     */
    private Date taskChagEndTime;
    /**
     * 任务完成时间
     */
    private Date taskDoneTime;
    /**
     * 加油单编号
     */
    private String taskFuelRecptNo;
    /**
     * 加油车编号
     */
    private String taskVehiNo;
    /**
     * 创建人员工ID
     */
    private String taskCreStaffId;
    /**
     * 创建人员工名称
     */
    private String taskCreStaffName;
    /**
     * 航班序号
     */
    private Integer flgtNum;
    /**
     * 任务星标（1：是，0：不是）默认0
     */
    private Integer taskStarmark;
    /**
     * 航班合并flg （0 否 1 是）
     */
    private Integer flgtShareNoFlg;
    /**
     * 记录创建时间
     */
    private Date taskRecCreTime;
    /**
     * 起飞油量
     */
    private Integer taskTakeoffFuel;
    /**
     * 轮挡油量
     */
    private Integer taskChockFuel;
    /**
     * 应加油量
     */
    private Integer taskTotalFuel;
    /**
     * 仪表类型（KG，LB）
     */
    private String taskMeterType;
    /**
     * 中央邮箱油量
     */
    private Integer taskCentTank;
    /**
     * 左机翼油箱油量
     */
    private Integer taskLeftTank;
    /**
     * 右机翼油箱油量
     */
    private Integer taskRightTank;
    /**
     * 机组签名（JPG图片的base64编码）
     */
    private String taskCrewSign;
    /**
     * 业务字段（油单类型）
     */
    private Integer flrcType;
    private String remark;
    private String aFlgtFtyp;

    /**
     * 打印油单完成时间
     */
    public Date getTaskRcPrintTime() {
        return taskRcPrintTime;
    }

    /**
     * 打印油单完成时间
     */
    public void setTaskRcPrintTime(Date taskRcPrintTime) {
        this.taskRcPrintTime = taskRcPrintTime;
    }

    public String getFlgtId() {
        return flgtId;
    }

    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId;
    }

    public String getFlgtFfid() {
        return flgtFfid;
    }

    public void setFlgtFfid(String flgtFfid) {
        this.flgtFfid = flgtFfid;
    }

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    public String getFlgtAptareaCode() {
        return flgtAptareaCode;
    }

    public void setFlgtAptareaCode(String flgtAptareaCode) {
        this.flgtAptareaCode = flgtAptareaCode;
    }

    public String getFlgtFlno() {
        return flgtFlno;
    }

    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno;
    }

    public Date getFlgtFlop() {
        return flgtFlop;
    }

    public void setFlgtFlop(Date flgtFlop) {
        this.flgtFlop = flgtFlop;
    }

    public String getFlgtAcname() {
        return flgtAcname;
    }

    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname;
    }

    public String getFlgtRegn() {
        return flgtRegn;
    }

    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn;
    }

    public String getFlgtPlacecode() {
        return flgtPlacecode;
    }

    public void setFlgtPlacecode(String flgtPlacecode) {
        this.flgtPlacecode = flgtPlacecode;
    }

    public String getFlgtAl2c() {
        return flgtAl2c;
    }

    public void setFlgtAl2c(String flgtAl2c) {
        this.flgtAl2c = flgtAl2c;
    }

    public String getFlgtAlcname() {
        return flgtAlcname;
    }

    public void setFlgtAlcname(String flgtAlcname) {
        this.flgtAlcname = flgtAlcname;
    }

    public String getFlgtVialc() {
        return flgtVialc;
    }

    public void setFlgtVialc(String flgtVialc) {
        this.flgtVialc = flgtVialc;
    }

    public Date getFlgtAStot() {
        return flgtAStot;
    }

    public void setFlgtAStot(Date flgtAStot) {
        this.flgtAStot = flgtAStot;
    }

    public Date getFlgtAEtot() {
        return flgtAEtot;
    }

    public void setFlgtAEtot(Date flgtAEtot) {
        this.flgtAEtot = flgtAEtot;
    }

    public Date getFlgtAAtot() {
        return flgtAAtot;
    }

    public void setFlgtAAtot(Date flgtAAtot) {
        this.flgtAAtot = flgtAAtot;
    }

    public Date getFlgtDStot() {
        return flgtDStot;
    }

    public void setFlgtDStot(Date flgtDStot) {
        this.flgtDStot = flgtDStot;
    }

    public Date getFlgtDEtot() {
        return flgtDEtot;
    }

    public void setFlgtDEtot(Date flgtDEtot) {
        this.flgtDEtot = flgtDEtot;
    }

    public Date getFlgtDAtot() {
        return flgtDAtot;
    }

    public void setFlgtDAtot(Date flgtDAtot) {
        this.flgtDAtot = flgtDAtot;
    }

    public String getFlgtOrg3c() {
        return flgtOrg3c;
    }

    public void setFlgtOrg3c(String flgtOrg3c) {
        this.flgtOrg3c = flgtOrg3c;
    }

    public String getFlgtOrgnm() {
        return flgtOrgnm;
    }

    public void setFlgtOrgnm(String flgtOrgnm) {
        this.flgtOrgnm = flgtOrgnm;
    }

    public String getFlgtTrs3c1() {
        return flgtTrs3c1;
    }

    public void setFlgtTrs3c1(String flgtTrs3c1) {
        this.flgtTrs3c1 = flgtTrs3c1;
    }

    public String getFlgtTrsnm1() {
        return flgtTrsnm1;
    }

    public void setFlgtTrsnm1(String flgtTrsnm1) {
        this.flgtTrsnm1 = flgtTrsnm1;
    }

    public String getFlgtTrs3c2() {
        return flgtTrs3c2;
    }

    public void setFlgtTrs3c2(String flgtTrs3c2) {
        this.flgtTrs3c2 = flgtTrs3c2;
    }

    public String getFlgtTrsnm2() {
        return flgtTrsnm2;
    }

    public void setFlgtTrsnm2(String flgtTrsnm2) {
        this.flgtTrsnm2 = flgtTrsnm2;
    }

    public String getFlgtTrs3c3() {
        return flgtTrs3c3;
    }

    public void setFlgtTrs3c3(String flgtTrs3c3) {
        this.flgtTrs3c3 = flgtTrs3c3;
    }

    public String getFlgtTrsnm3() {
        return flgtTrsnm3;
    }

    public void setFlgtTrsnm3(String flgtTrsnm3) {
        this.flgtTrsnm3 = flgtTrsnm3;
    }

    public String getFlgtTrs3c4() {
        return flgtTrs3c4;
    }

    public void setFlgtTrs3c4(String flgtTrs3c4) {
        this.flgtTrs3c4 = flgtTrs3c4;
    }

    public String getFlgtTrsnm4() {
        return flgtTrsnm4;
    }

    public void setFlgtTrsnm4(String flgtTrsnm4) {
        this.flgtTrsnm4 = flgtTrsnm4;
    }

    public String getFlgtTrs3c5() {
        return flgtTrs3c5;
    }

    public void setFlgtTrs3c5(String flgtTrs3c5) {
        this.flgtTrs3c5 = flgtTrs3c5;
    }

    public String getFlgtTrsnm5() {
        return flgtTrsnm5;
    }

    public void setFlgtTrsnm5(String flgtTrsnm5) {
        this.flgtTrsnm5 = flgtTrsnm5;
    }

    public String getFlgtDes3c() {
        return flgtDes3c;
    }

    public void setFlgtDes3c(String flgtDes3c) {
        this.flgtDes3c = flgtDes3c;
    }

    public String getFlgtDesnm() {
        return flgtDesnm;
    }

    public void setFlgtDesnm(String flgtDesnm) {
        this.flgtDesnm = flgtDesnm;
    }

    public String getFlgtAdid() {
        return flgtAdid;
    }

    public void setFlgtAdid(String flgtAdid) {
        this.flgtAdid = flgtAdid;
    }

    public String getFlgtMissionProp() {
        return flgtMissionProp;
    }

    public void setFlgtMissionProp(String flgtMissionProp) {
        this.flgtMissionProp = flgtMissionProp;
    }

    public String getFlgtMissionPropIn() {
        return flgtMissionPropIn;
    }

    public void setFlgtMissionPropIn(String flgtMissionPropIn) {
        this.flgtMissionPropIn = flgtMissionPropIn;
    }

    public String getFlgtNature() {
        return flgtNature;
    }

    public void setFlgtNature(String flgtNature) {
        this.flgtNature = flgtNature;
    }

    public String getFlgtSubNature() {
        return flgtSubNature;
    }

    public void setFlgtSubNature(String flgtSubNature) {
        this.flgtSubNature = flgtSubNature;
    }

    public String getFlgtLinkDStot() {
        return flgtLinkDStot;
    }

    public void setFlgtLinkDStot(String flgtLinkDStot) {
        this.flgtLinkDStot = flgtLinkDStot;
    }

    public String getFlgtLinkDEtot() {
        return flgtLinkDEtot;
    }

    public void setFlgtLinkDEtot(String flgtLinkDEtot) {
        this.flgtLinkDEtot = flgtLinkDEtot;
    }

    public String getFlgtLinkDAtot() {
        return flgtLinkDAtot;
    }

    public void setFlgtLinkDAtot(String flgtLinkDAtot) {
        this.flgtLinkDAtot = flgtLinkDAtot;
    }

    public String getFlgtLinkAdid() {
        return flgtLinkAdid;
    }

    public void setFlgtLinkAdid(String flgtLinkAdid) {
        this.flgtLinkAdid = flgtLinkAdid;
    }

    public String getFlgtLinkAStot() {
        return flgtLinkAStot;
    }

    public void setFlgtLinkAStot(String flgtLinkAStot) {
        this.flgtLinkAStot = flgtLinkAStot;
    }

    public String getFlgtLinkAEtot() {
        return flgtLinkAEtot;
    }

    public void setFlgtLinkAEtot(String flgtLinkAEtot) {
        this.flgtLinkAEtot = flgtLinkAEtot;
    }

    public String getFlgtLinkAAtot() {
        return flgtLinkAAtot;
    }

    public void setFlgtLinkAAtot(String flgtLinkAAtot) {
        this.flgtLinkAAtot = flgtLinkAAtot;
    }

    public String getFlgtstatus() {
        return flgtstatus;
    }

    public void setFlgtstatus(String flgtstatus) {
        this.flgtstatus = flgtstatus;
    }

    public String getApcdSAirportName() {
        return apcdSAirportName;
    }

    public void setApcdSAirportName(String apcdSAirportName) {
        this.apcdSAirportName = apcdSAirportName;
    }

    public String getApcdEAirportName() {
        return apcdEAirportName;
    }

    public void setApcdEAirportName(String apcdEAirportName) {
        this.apcdEAirportName = apcdEAirportName;
    }

    public String getApcdSAirportNames() {
        return apcdSAirportNames;
    }

    public void setApcdSAirportNames(String apcdSAirportNames) {
        this.apcdSAirportNames = apcdSAirportNames;
    }

    public String getApcdEAirportNames() {
        return apcdEAirportNames;
    }

    public void setApcdEAirportNames(String apcdEAirportNames) {
        this.apcdEAirportNames = apcdEAirportNames;
    }

    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName;
    }

    public String getAlcdArlnNames() {
        return alcdArlnNames;
    }

    public void setAlcdArlnNames(String alcdArlnNames) {
        this.alcdArlnNames = alcdArlnNames;
    }

    public String getFlightValic() {
        return flightValic;
    }

    public void setFlightValic(String flightValic) {
        this.flightValic = flightValic;
    }

    public String getFlgtPlacecodeIn() {
        return flgtPlacecodeIn;
    }

    public void setFlgtPlacecodeIn(String flgtPlacecodeIn) {
        this.flgtPlacecodeIn = flgtPlacecodeIn;
    }

    public String getFlgtbezu() {
        return flgtbezu;
    }

    public void setFlgtbezu(String flgtbezu) {
        this.flgtbezu = flgtbezu;
    }

    public String getFlgtFlti() {
        return flgtFlti;
    }

    public void setFlgtFlti(String flgtFlti) {
        this.flgtFlti = flgtFlti;
    }

    public String getFlgtFtyp() {
        return flgtFtyp;
    }

    public void setFlgtFtyp(String flgtFtyp) {
        this.flgtFtyp = flgtFtyp;
    }

    public String getFlgtDelaycode() {
        return flgtDelaycode;
    }

    public void setFlgtDelaycode(String flgtDelaycode) {
        this.flgtDelaycode = flgtDelaycode;
    }

    public String getFlgtProxy() {
        return flgtProxy;
    }

    public void setFlgtProxy(String flgtProxy) {
        this.flgtProxy = flgtProxy;
    }

    public String getFlgtLinkFlno() {
        return flgtLinkFlno;
    }

    public void setFlgtLinkFlno(String flgtLinkFlno) {
        this.flgtLinkFlno = flgtLinkFlno;
    }

    public String getFlgtFnflag() {
        return flgtFnflag;
    }

    public void setFlgtFnflag(String flgtFnflag) {
        this.flgtFnflag = flgtFnflag;
    }

    public String getFlgtIfsr() {
        return flgtIfsr;
    }

    public void setFlgtIfsr(String flgtIfsr) {
        this.flgtIfsr = flgtIfsr;
    }

    public String getFlgtGame() {
        return flgtGame;
    }

    public void setFlgtGame(String flgtGame) {
        this.flgtGame = flgtGame;
    }

    public String getFlgtDepRunway() {
        return flgtDepRunway;
    }

    public void setFlgtDepRunway(String flgtDepRunway) {
        this.flgtDepRunway = flgtDepRunway;
    }

    public String getFlgtArrRunway() {
        return flgtArrRunway;
    }

    public void setFlgtArrRunway(String flgtArrRunway) {
        this.flgtArrRunway = flgtArrRunway;
    }

    public String getFlgtGate() {
        return flgtGate;
    }

    public void setFlgtGate(String flgtGate) {
        this.flgtGate = flgtGate;
    }

    public Date getFlgtCrewInPlace() {
        return flgtCrewInPlace;
    }

    public void setFlgtCrewInPlace(Date flgtCrewInPlace) {
        this.flgtCrewInPlace = flgtCrewInPlace;
    }

    public Date getFlgtChocksIn() {
        return flgtChocksIn;
    }

    public void setFlgtChocksIn(Date flgtChocksIn) {
        this.flgtChocksIn = flgtChocksIn;
    }

    public Date getFlgtChocksOut() {
        return flgtChocksOut;
    }

    public void setFlgtChocksOut(Date flgtChocksOut) {
        this.flgtChocksOut = flgtChocksOut;
    }

    public Date getFlgtFirstLugg() {
        return flgtFirstLugg;
    }

    public void setFlgtFirstLugg(Date flgtFirstLugg) {
        this.flgtFirstLugg = flgtFirstLugg;
    }

    public Date getFlgtLastLugg() {
        return flgtLastLugg;
    }

    public void setFlgtLastLugg(Date flgtLastLugg) {
        this.flgtLastLugg = flgtLastLugg;
    }

    public Date getFlgtPtax() {
        return flgtPtax;
    }

    public void setFlgtPtax(Date flgtPtax) {
        this.flgtPtax = flgtPtax;
    }

    public Date getFlgtEtax() {
        return flgtEtax;
    }

    public void setFlgtEtax(Date flgtEtax) {
        this.flgtEtax = flgtEtax;
    }

    public Date getFlgtAtax() {
        return flgtAtax;
    }

    public void setFlgtAtax(Date flgtAtax) {
        this.flgtAtax = flgtAtax;
    }

    public Integer getFlgtTakeoffFuel() {
        return flgtTakeoffFuel;
    }

    public void setFlgtTakeoffFuel(Integer flgtTakeoffFuel) {
        this.flgtTakeoffFuel = flgtTakeoffFuel;
    }

    public Integer getFlgtChockFuel() {
        return flgtChockFuel;
    }

    public void setFlgtChockFuel(Integer flgtChockFuel) {
        this.flgtChockFuel = flgtChockFuel;
    }

    public Integer getFlgtVip() {
        return flgtVip;
    }

    public void setFlgtVip(Integer flgtVip) {
        this.flgtVip = flgtVip;
    }

    public Integer getFlgtTaskAsign() {
        return flgtTaskAsign;
    }

    public void setFlgtTaskAsign(Integer flgtTaskAsign) {
        this.flgtTaskAsign = flgtTaskAsign;
    }

    public Integer getFlgtStarmark() {
        return flgtStarmark;
    }

    public void setFlgtStarmark(Integer flgtStarmark) {
        this.flgtStarmark = flgtStarmark;
    }

    public Integer getFlgtManualFlg() {
        return flgtManualFlg;
    }

    public void setFlgtManualFlg(Integer flgtManualFlg) {
        this.flgtManualFlg = flgtManualFlg;
    }

    public Date getFlgtLinkFlop() {
        return flgtLinkFlop;
    }

    public void setFlgtLinkFlop(Date flgtLinkFlop) {
        this.flgtLinkFlop = flgtLinkFlop;
    }

    public Integer getFlgtLinkRepeat() {
        return flgtLinkRepeat;
    }

    public void setFlgtLinkRepeat(Integer flgtLinkRepeat) {
        this.flgtLinkRepeat = flgtLinkRepeat;
    }

    public Integer getFlgtRepeat() {
        return flgtRepeat;
    }

    public void setFlgtRepeat(Integer flgtRepeat) {
        this.flgtRepeat = flgtRepeat;
    }

    public String getFlgtOtc() {
        return flgtOtc;
    }

    public void setFlgtOtc(String flgtOtc) {
        this.flgtOtc = flgtOtc;
    }

    public String getFlgtFltiIn() {
        return flgtFltiIn;
    }

    public void setFlgtFltiIn(String flgtFltiIn) {
        this.flgtFltiIn = flgtFltiIn;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId;
    }

    public String getTaskOpeStaffName() {
        return taskOpeStaffName;
    }

    public void setTaskOpeStaffName(String taskOpeStaffName) {
        this.taskOpeStaffName = taskOpeStaffName;
    }

    public Integer getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(Integer taskContent) {
        this.taskContent = taskContent;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getTaskAsgTime() {
        return taskAsgTime;
    }

    public void setTaskAsgTime(Date taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    public Date getTaskAccTime() {
        return taskAccTime;
    }

    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    public Date getTaskChagStaTime() {
        return taskChagStaTime;
    }

    public void setTaskChagStaTime(Date taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    public Date getTaskArriveTime() {
        return taskArriveTime;
    }

    public void setTaskArriveTime(Date taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }

    public Date getTaskChagEndTime() {
        return taskChagEndTime;
    }

    public void setTaskChagEndTime(Date taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    public String getTaskFuelRecptNo() {
        return taskFuelRecptNo;
    }

    public void setTaskFuelRecptNo(String taskFuelRecptNo) {
        this.taskFuelRecptNo = taskFuelRecptNo;
    }

    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo;
    }

    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId;
    }

    public String getTaskCreStaffName() {
        return taskCreStaffName;
    }

    public void setTaskCreStaffName(String taskCreStaffName) {
        this.taskCreStaffName = taskCreStaffName;
    }

    public Integer getFlgtNum() {
        return flgtNum;
    }

    public void setFlgtNum(Integer flgtNum) {
        this.flgtNum = flgtNum;
    }

    public Integer getTaskStarmark() {
        return taskStarmark;
    }

    public void setTaskStarmark(Integer taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    public Integer getFlgtShareNoFlg() {
        return flgtShareNoFlg;
    }

    public void setFlgtShareNoFlg(Integer flgtShareNoFlg) {
        this.flgtShareNoFlg = flgtShareNoFlg;
    }

    public Date getTaskRecCreTime() {
        return taskRecCreTime;
    }

    public void setTaskRecCreTime(Date taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
    }

    public Integer getTaskTakeoffFuel() {
        return taskTakeoffFuel;
    }

    public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
        this.taskTakeoffFuel = taskTakeoffFuel;
    }

    public Integer getTaskChockFuel() {
        return taskChockFuel;
    }

    public void setTaskChockFuel(Integer taskChockFuel) {
        this.taskChockFuel = taskChockFuel;
    }

    public Integer getTaskTotalFuel() {
        return taskTotalFuel;
    }

    public void setTaskTotalFuel(Integer taskTotalFuel) {
        this.taskTotalFuel = taskTotalFuel;
    }

    public String getTaskMeterType() {
        return taskMeterType;
    }

    public void setTaskMeterType(String taskMeterType) {
        this.taskMeterType = taskMeterType;
    }

    public Integer getTaskCentTank() {
        return taskCentTank;
    }

    public void setTaskCentTank(Integer taskCentTank) {
        this.taskCentTank = taskCentTank;
    }

    public Integer getTaskLeftTank() {
        return taskLeftTank;
    }

    public void setTaskLeftTank(Integer taskLeftTank) {
        this.taskLeftTank = taskLeftTank;
    }

    public Integer getTaskRightTank() {
        return taskRightTank;
    }

    public void setTaskRightTank(Integer taskRightTank) {
        this.taskRightTank = taskRightTank;
    }

    public String getTaskCrewSign() {
        return taskCrewSign;
    }

    public void setTaskCrewSign(String taskCrewSign) {
        this.taskCrewSign = taskCrewSign;
    }

    public Integer getFlrcType() {
        return flrcType;
    }

    public void setFlrcType(Integer flrcType) {
        this.flrcType = flrcType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getaFlgtFtyp() {
        return aFlgtFtyp;
    }

    public void setaFlgtFtyp(String aFlgtFtyp) {
        this.aFlgtFtyp = aFlgtFtyp;
    }
}
