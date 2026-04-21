package com.higer.oildataexchange.entity.airUnit;

import cn.hutool.core.date.DateUtil;
import com.higer.oildataexchange.entity.flight.TFlightCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "SUB")
public class RGRQ_R {

    /**
     * ID必须
     */
    private String ID;

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 必须，飞机注册号（机号）
     */
    private String REGN;

    /**
     * 必须，飞机注册号（机号）
     */
    private String FLNO;

    /**
     * 必须，开始生效时间，YYYY-MM-DD HH:MI:SS
     */
    private Date VAFR;

    /**
     * 必须，结束生效时间，YYYY-MM-DD HH:MI:SS
     */
    private Date VATO;

    /**
     * 必须，所属客户的代码
     */
    private String CSTNO;

    /**
     * 记录创建时间YYYY-MM-DD HH:MI:SS
     */
    private Date CDAT;

    /**
     * 记录最后更新时间，一般是指航班业务动态的变更时间，不包含程序进行其他处理而导致更新的时间，与LUTS含义不同，取值可能会不一样。YYYY-MM-DD HH:MI:SS
     */
    private Date LSTU;

    /**
     * 所属客户的名称
     */
    private String CSTNM;

    /**
     * 机型名称
     */
    private String ACTN;

    /**
     * 机型三码
     */
    private String ACT3;

    public TFlightCode toFlightCode() {
        TFlightCode tFlightCode = new TFlightCode();
        tFlightCode.setId(Integer.valueOf(ID));
        tFlightCode.setArcrRegn(REGN);
        tFlightCode.setArcrCustomNum(CSTNO);
        tFlightCode.setArcrStartDate(VAFR);
        tFlightCode.setArcrEndDate(VATO);
        tFlightCode.setFlno(FLNO);
//        tFlightCode.setCustomName(CSTNM);
        if (StringUtils.isNotEmpty(ACTN)) {
            tFlightCode.setArcrAcname(ACTN);
        }
//        tFlightCode.setAcname3c(ACT3);
        tFlightCode.setIud(IUD);
        tFlightCode.setCnafCreateTime(CDAT);
        tFlightCode.setCnafUpdateTime(DateUtil.parseDateTime(LUTS));

        return tFlightCode;
    }
}
