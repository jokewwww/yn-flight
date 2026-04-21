package com.higer.statistical.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/26 15:08
 * @Description:
 */
public class DateUtils {
    /**
    * @Description: 获取某个月 下个月 的日期 例如 2018-01 return 2018-02
    * @Param: [repeatDate]
    * @return: java.lang.String
    * @Author: XiuHongXin
    * @Date: 2019/1/26
    */
    public static String getPreMonth(String repeatDate) {
        if(repeatDate.length() != 7){
            return "error";
        }
        int year = Integer.parseInt(repeatDate.substring(0, 4));
        int month = Integer.parseInt(repeatDate.substring(5, 7));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return new SimpleDateFormat("yyyy-MM").format(cal.getTime());
    }

    public static void main(String[] args) {
        String preMonth = getPreMonth("2018-01");
        String preMonth1 = getPreMonth("2018-09");
        String preMonth2 = getPreMonth("2018-12");
        System.out.println(preMonth);
        System.out.println(preMonth1);
        System.out.println(preMonth2);
    }
}
