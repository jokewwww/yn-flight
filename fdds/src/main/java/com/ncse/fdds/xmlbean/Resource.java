package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;

public class Resource {


    @XMLValue("flgt_dep_runway")
    private String DepRunway;//离港跑道
    @XMLValue("flgt_arr_runway")
    private String ArrRunway;//到港跑道

    /*
    如果是进港那么离港机位是上一机场 到港是本机场
    如果是离港那么离港机位是本机场 到港是下一机场
     */
    @XMLValue("td_flgt_placecode")
    private String DepStand;//离港机位
    @XMLValue("ta_flgt_placecode")
    private String ArrStand;//到港机位
    @XMLValue("flgt_gate")
    private String Gate;//登机门

}
