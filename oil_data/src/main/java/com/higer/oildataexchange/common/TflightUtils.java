package com.higer.oildataexchange.common;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class TflightUtils {

    public static Date parseDate(String date) {
        try {
            if (StringUtils.isNotEmpty(date)) {
                return DateUtils.parseDate(date, Constant.YYYY_MM_DD_HH_MM_SS, Constant.YYYY_MM_DD, Constant.YYYYMMDD);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //     * （1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油 7:  内航国内补加油 8: 内航 离境 补加油 9 : 外航 补油）
    public static String transformFlrcType(@NonNull Integer flrcType) {
        switch (flrcType) {
            case 1:
                return "外航加油";
            case 2:
                return "内航离境加油";
            case 3:
                return "内航国内加油";
            case 4:
                return "外航抽油";
            case 5:
                return "内航离境抽油";
            case 6:
                return "内航国内抽油";
            case 7:
                return "内航国内补加油";
            case 8:
                return "内航离境补加油";
            case 9:
                return "外航补油";
            default:
                return "未知";
        }
    }
}
