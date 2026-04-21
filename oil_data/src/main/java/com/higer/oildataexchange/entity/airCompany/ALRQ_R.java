package com.higer.oildataexchange.entity.airCompany;

import com.higer.oildataexchange.entity.airUnit.TAirlinesCode;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "SUB")
public class ALRQ_R {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;


    /**
     * 必须，航空公司二码，主键
     */
    private String ALC2;

    /**
     * 必须，航空公司名称
     */
    private String ALCNM;
    /**
     * 必须，航空公司简称
     */
    private String ALCSN;

    /**
     * 航空公公司英文名称
     */
    private String ALCFN;

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

    public TAirlinesCode toAirLinesCode() {
        TAirlinesCode tFlightCode = new TAirlinesCode();
        tFlightCode.setAlcdIcaoCode(ALC2);
        tFlightCode.setAlcdArlnName(ALCNM);
        tFlightCode.setAlcdArlnNameS(ALCSN);
        tFlightCode.setAlcdArlnNw(null);
        tFlightCode.setCstmNum(CSTNO);
        return tFlightCode;
    }
}
