package com.higer.oildataexchange.entity.acdm;

import lombok.Data;

import java.util.Date;


@Data
public class FligtVo {
    private Date dFlgtAAtot;
    /**
     * 进离港（A：进港，D：出港）
     */
    private String flgtAdid;
    private String flgtAirportCode;
    /**
     * 航空公司二字码
     */
    private String flgtAl2c;
    private Date flgtDEtot;
    private Date flgtDStot;
    private String flgtDes3c;
    /**
     * 航班唯一id
     */
    private String flgtFfid;
    /**
     * 航班号
     */
    private String flgtFlno;
    /**
     * 航班日期
     */
    private Date flgtFlop;
    private String flgtFtyp;
    private String flgtGame;
    private String flgtId;
    private Date flgtLinkFlop;
    private int flgtLinkRepeat;
    private int flgtNum;
    private String flgtOrg3c;
    private String flgtPlacecode;
    private String flgtPlacecodeStatus;
    private String flgtRegn;
    private String flgtRegnStatus;
    private String flgtRepeat;
    private String flgtTaskAsign;
    private String flgtTrs3c1;
    private String flgtTrs3c5;
    private String flgtTrsnm1;
    private String flrcType;
    private String orderNo;
    private Date taskAsgTime;
    private String taskContent;
    private String taskCreStaffId;
    private String taskId;
    private String taskOpeStaffId;
    private String taskOpeStaffName;
    private Date taskRecCreTime;
    private String taskStarmark;
    private int taskStatus;
}
