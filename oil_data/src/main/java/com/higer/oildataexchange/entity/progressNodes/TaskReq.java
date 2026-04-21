package com.higer.oildataexchange.entity.progressNodes;

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
public class TaskReq {

    /**
     * 必须，任务记录号
     */
    @NonNull
    private String TNB;

    /**
     * 当前进程
     */
    private String PCID;

    /**
     * 必须，航班执行日期，一般为航班计划起飞时间的日期，格式：YYYYMMDD，例如“20190301”
     */
    @NonNull
    private String FLOP;

    /**
     * 必须，航班号
     */
    @NonNull
    private String FLNO;

    /**
     * 必须，航班进离港属性，A-进港；D-离港
     */
    private String ADID;

    /**
     * 航班唯一号（前端）
     */
    private String FKEY;

    /**
     * 必须，机号
     */
    @NonNull
    private String REGN;

    /**
     * 必须，机型3码
     */
    @NonNull
    private String AC3C;

    /**
     * 机型名称
     */
    private String ACNAME;

    /**
     * 必须，机场3码
     */
    @NonNull
    private String AP3C;

    /**
     * 必须，航司2码
     */
    @NonNull
    private String AL2C;

    /**
     * 航司中文名
     */
    private String ALCNAME;

    /**
     * 机位
     */
    private String PLACECODE;

    /**
     * 必须，油单号
     */
    @NonNull
    private String BILLNUMBER;

    /**
     * 派发时间 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TDWN;

    /**
     * 收到 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TGET;

    /**
     * 接受 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TACP;

    /**
     * 到位 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TARV;

    /**
     * 开始 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TBEG;

    /**
     * 打印 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TPRT;

    /**
     * 结束 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TEND;

    /**
     * 取消 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TCAL;

    /**
     * 拒绝 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TDEC;

    /**
     * 暂停 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date TSTO;

    /**
     * 服务类型
     */
    private String RTYP;

    /**
     * 服务类型名称
     */
    private String RTNM;

    /**
     * 必须，任务状态/任务节点类型：
     * TACP     接受（加油员接受任务）
     * TARV     到位（加油车到达机位）
     * TBEG     开始（加油开始）
     * TPRT     打印（加油完毕，打印油单）
     * TEND    结束（任务结束，加油员撤离）
     * TCAL     取消（任务被取消）
     * TDEC     拒绝（加油员拒绝任务）
     */
    @NonNull
    private String TTST;

    /**
     * 计划时间 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date STOT;

    /**
     * 变更（预计）时间 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date ETOT;

    /**
     * 实际时间 YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date ATOT;

    /**
     * 必须，航班唯一号
     */
    @NonNull
    private String FNKEY;

    /**
     * 车牌号
     */
    private String VNB;

}
