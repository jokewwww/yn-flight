package com.higer.flightinfo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class TFlightUtils {

    private static final String YYYYMMDD_HHMMSS="yyyy-MM-dd HH:mm:ss";
    private static final String YYYYMMDD="yyyy-MM-dd";

    public static Date dateFormat(String date){
        try {
            if(StringUtils.isEmpty(date)){
                return null;
            }
            return DateUtils.parseDate(date, Locale.CHINA,YYYYMMDD_HHMMSS,YYYYMMDD);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(String.format("日期解析错误:%s",date),e);
        }
        return null;
    }

    public static String transformFtyp(String flightStatus,String flgtAdid) {
        if(StringUtils.isEmpty(flightStatus)){
            return null;
        }
        String flgtFtyp;
        switch (flightStatus){
            case "计划":
                flgtFtyp="SH";
                break;
            case "起飞":
                flgtFtyp=StringUtils.equals(flgtAdid,"A")?"EX":"AB";
                break;
            case "到达":
                flgtFtyp=StringUtils.equals(flgtAdid,"A")?"LD":"NA";
                break;
            case "取消":
                flgtFtyp="CX";
                break;
            case "延误":
                flgtFtyp="YW";
                break;
            case "备降":
            case "正在备降":
            case "改降":
            case "转场":
                flgtFtyp="DV";
                break;
            case "返航":
            case "正在返航":
                flgtFtyp="FH";
                break;
            case "返回停机位":
                flgtFtyp="HH";
                break;
            case "非营运":
                flgtFtyp="FX";
                break;
            default:
                flgtFtyp=null;
                break;
        }
        return flgtFtyp;
    }

    public static String transformFlti(String fcategory) {
        int flti=Integer.parseInt(StringUtils.isNotEmpty(fcategory)?fcategory:"-1");
        String result;
        switch (flti){
            case 0://国内-国内
                result="D";
                break;
            case 1://国内-国际
            case 3://地区-国际
            case 4://国际-国际
                result="I";
                break;
            case 2://国内-地区
                result="R";
                break;
            default://未知
                result="U";

        }
        return result;
    }
}
