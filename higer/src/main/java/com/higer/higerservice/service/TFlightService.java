package com.higer.higerservice.service;

import com.higer.higerservice.entity.oilpro.TFlight;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TFlightService {

    private String FULL_DATE_FORMAT = "yyyyMMddHHmmss";
    private String DATE_FORMAT = "yyyyMMdd";

    public TFlight add(Element msg) {
//        System.out.println("add");
        TFlight tFlight = new TFlight();
        Element dflt = msg.element("DFLT");
        tFlight.setFlgt_ffid(dflt.elementText("FLIGHTNO"));//航班号
        tFlight.setFlgt_flop(this.formatDate(dflt.elementText("FLIGHTDATE"), DATE_FORMAT));//航班日期
//        tFlight.setFlgt_al2c(dflt.elementText("ARLINE"));//航空公司二字码
        tFlight.setFlgt_alcname(dflt.elementText("ARLINE"));//航空公司
        tFlight.setFlgt_mission_prop(dflt.elementText("TASK"));//任务属性
        //航班性质
        if (dflt.elementText("REGIONID") != null) {
            switch (Integer.parseInt(dflt.elementText("REGIONID"))) {
                case 10001://国内
                    tFlight.setFlgt_flti("D");
                    break;
                case 10002://国际
                    tFlight.setFlgt_flti("I");
                    break;
                case 10003://地区
                    tFlight.setFlgt_flti("R");
                    break;
                case 10004://混合
                    tFlight.setFlgt_flti("M");
                    break;
                case 10005://支线
                    tFlight.setFlgt_flti("U");
                    break;
                case 10006://外场
                    tFlight.setFlgt_flti("U");
                    break;
                default://其他
                    tFlight.setFlgt_flti("U");
                    break;
            }
        }
        tFlight.setFlgt_acname(dflt.elementText("CRAFTMODEL"));//机型
        if (dflt.elementText("ABNSTATUS") != null) {
            switch (dflt.elementText("ABNSTATUS")) {//航班异常状态
                case "CAN"://取消
                    tFlight.setFlgt_ftyp("X");
                    break;
                case "ALT"://备降
                    tFlight.setFlgt_ftyp("D");
                    break;
                case "RTN"://返航
                    tFlight.setFlgt_ftyp("R");
                    break;
                case "DLY"://延误
                    tFlight.setFlgt_ftyp("Y");
                    break;
                default:
                    tFlight.setFlgt_ftyp("S");
                    break;
            }
            tFlight.setFlgt_delaycode(dflt.elementText("ABNRSN"));//异常原因
        }
        Element routelst = dflt.element("ROUTELST");
        if (routelst != null) {
            List<Element> routeList = routelst.elements("ROUTE");
            if (routeList.size() > 1) {//出发地机场
                tFlight.setFlgt_org3c(routeList.get(0).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 2) {//目的地机场
                tFlight.setFlgt_des3c(routeList.get(1).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 3) {//经停机场1
                tFlight.setFlgt_trs3c1(routeList.get(2).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 4) {//经停机场2
                tFlight.setFlgt_trs3c2(routeList.get(3).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 5) {//经停机场3
                tFlight.setFlgt_trs3c3(routeList.get(4).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 6) {//经停机场4
                tFlight.setFlgt_trs3c4(routeList.get(5).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 7) {//经停机场5
                tFlight.setFlgt_trs3c5(routeList.get(6).elementText("AIRPORTIATA"));//出发地机场三字码
            }
        }
        Element sharelst = dflt.element("SHARELST");
        if (sharelst != null) {
            //共享航班
            List<Element> shareList = sharelst.elements("SHARE");
            String airlineid = shareList.stream().map(share -> share.elementText("AIRLINEID")).collect(Collectors.joining(","));
            tFlight.setFlgt_link_flno(airlineid);
        }
        return tFlight;
    }

    public TFlight modify(Element msg) {
//        System.out.println("modify");
        TFlight tFlight = new TFlight();
        Element dflt = msg.element("DFLT");
        tFlight.setFlgt_ffid(dflt.elementText("FLIGHTNO"));//航班号
        tFlight.setFlgt_flop(this.formatDate(dflt.elementText("FLIGHTDATE"), DATE_FORMAT));//航班日期
//        tFlight.setFlgt_al2c(dflt.elementText("ARLINE"));//航空公司二字码
        tFlight.setFlgt_alcname(dflt.elementText("ARLINE"));//航空公司
        tFlight.setFlgt_mission_prop(dflt.elementText("TASK"));//任务属性
        //航班性质
        if (dflt.elementText("REGIONID") != null) {
            switch (Integer.parseInt(dflt.elementText("REGIONID"))) {
                case 10001://国内
                    tFlight.setFlgt_flti("D");
                    break;
                case 10002://国际
                    tFlight.setFlgt_flti("I");
                    break;
                case 10003://地区
                    tFlight.setFlgt_flti("R");
                    break;
                case 10004://混合
                    tFlight.setFlgt_flti("M");
                    break;
                case 10005://支线
                    tFlight.setFlgt_flti("U");
                    break;
                case 10006://外场
                    tFlight.setFlgt_flti("U");
                    break;
                default://其他
                    tFlight.setFlgt_flti("U");
                    break;
            }
        }
        tFlight.setFlgt_acname(dflt.elementText("CRAFTMODEL"));//机型
        if (dflt.elementText("ABNSTATUS") != null) {
            switch (dflt.elementText("ABNSTATUS")) {//航班异常状态
                case "CAN"://取消
                    tFlight.setFlgt_ftyp("X");
                    break;
                case "ALT"://备降
                    tFlight.setFlgt_ftyp("D");
                    break;
                case "RTN"://返航
                    tFlight.setFlgt_ftyp("R");
                    break;
                case "DLY"://延误
                    tFlight.setFlgt_ftyp("Y");
                    break;
                default:
                    tFlight.setFlgt_ftyp("S");
                    break;
            }
            tFlight.setFlgt_delaycode(dflt.elementText("ABNRSN"));//异常原因
        }
        Element routelst = dflt.element("ROUTELST");
        if (routelst != null) {
            List<Element> routeList = routelst.elements("ROUTE");
            if (routeList.size() > 1) {//出发地机场
                tFlight.setFlgt_org3c(routeList.get(0).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 2) {//目的地机场
                tFlight.setFlgt_des3c(routeList.get(1).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 3) {//经停机场1
                tFlight.setFlgt_trs3c1(routeList.get(2).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 4) {//经停机场2
                tFlight.setFlgt_trs3c2(routeList.get(3).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 5) {//经停机场3
                tFlight.setFlgt_trs3c3(routeList.get(4).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 6) {//经停机场4
                tFlight.setFlgt_trs3c4(routeList.get(5).elementText("AIRPORTIATA"));//出发地机场三字码
            }
            if (routeList.size() > 7) {//经停机场5
                tFlight.setFlgt_trs3c5(routeList.get(6).elementText("AIRPORTIATA"));//出发地机场三字码
            }
        }
        Element sharelst = dflt.element("SHARELST");
        if (sharelst != null) {
            //共享航班
            List<Element> shareList = sharelst.elements("SHARE");
            String airlineid = shareList.stream().map(share -> share.elementText("AIRLINEID")).collect(Collectors.joining(","));
            tFlight.setFlgt_link_flno(airlineid);
        }
        return tFlight;
    }

    private Date formatDate(String text, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

}
