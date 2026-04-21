package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;

import java.util.Date;

public class ProcessNode {

    @XMLValue("flgt_chocks_in")
    private Date ATA;//上轮档时间
    @XMLValue("flgt_chocks_out")
    private Date ATD;//撤轮挡时间
    @XMLValue("flgt_first_lugg")
    private Date FBT;//第一件行李时间
    @XMLValue("flgt_last_lugg")
    private Date LBT;//最后一件行李时间
}
