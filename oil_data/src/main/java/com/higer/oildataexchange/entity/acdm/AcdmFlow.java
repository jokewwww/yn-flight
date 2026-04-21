package com.higer.oildataexchange.entity.acdm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 各机场智慧加油系统从总部平台获取航班动态（含预加油量）信息。当机场智慧加油系统无法对接机场航班动态时，可从总部获取航班动态信息
 */
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "INFO")
public class AcdmFlow {
    /**
     * 航班标识 CA123-24MAR06   航班号-航班日期
     */
    @NonNull
    private String FFID;

    /**
     * 保障批次 第一批填写1，第二批填写2
     */
    @NonNull
    private String BATCH;
    /**
     * 当前保障环节的状态 取值可为：begin：表示环节开始  underway：正在进行  end：环节结束  exception：环节异常
     */
    private String STATUS;

    /**
     * 保障环节异常原因 当<STATUS>标签值为exception时，此处应填具体值
     */
    private String EXPREASON;
    /**
     * 保障执行人 保障具体执行人名单，以|号分割，例子：WANG PENG|LIU HONG
     */
    private String OPERATOR;

    /**
     * 执行人部门  车辆部
     */
    private String DEPT;

    /**
     * 保障环节计划开始时间
     */
    private String SCHDBEGINTM;

    /**
     * 保障环节计划结束时间
     */
    private String SCHDENDTM;

    /**
     * 保障环节开始时间 20160925105430
     */
    private String BEGINTM;
    /**
     * 保障环节结束时间 20160925105430
     */
    private String ENDTM;

    /**
     * 监督人
     */
    private String SUPERVISOR;

    /**
     * 监督人记录的环节开始时间 20160925105430
     */
    private String SVBEGINTM;

    /**
     * 监督人记录的环节结束时间 20160925105430
     */
    private String SVENDTM;

    /**
     * 本保障环节的到位时间，比如客梯车保障过程中的，客梯车到位时刻。 20160925105430
     */
    private String INPLACETM;
}
