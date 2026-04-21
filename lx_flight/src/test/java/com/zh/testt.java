package com.zh;

import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testt {
    public static void main(String[] args) {
        String str = "yulv # 123456 # yulv@21cn.com";
        String d="#";
        int result1 = str.indexOf(d);
        System.out.println(result1);
        Matcher matcher= Pattern.compile(d).matcher(str);
        if(matcher.find()){
            String a =str.substring(matcher.start()+d.length()+1);
            System.out.println(a);
        }else{
            System.out.println("null");
        }
        //String receiverName=str.substring(0,matcher.start()).trim();
        System.out.println(str.substring(1));
    }

    @Test
    public void testTimeCalc(){
        Date now = DateUtil.parse("2024-07-03 03:59:59");
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim.format(beforeDate);
        System.out.println(nowStr);
        System.out.println(beforeDatetr);
    }

    @Test
    public void testStrFormat(){
        // 创建一个BigDecimal对象
        BigDecimal value = new BigDecimal("-1234567");

        // 使用String.format进行格式化
        String formattedValue = String.format("%,.2f", value);
        System.out.println("Formatted Value: " + formattedValue);

        // 更多格式化选项
        String formattedValue2 = String.format("%.4f", value);
        System.out.println("Formatted Value with 4 decimal places: " + formattedValue2);

    }

    @Test
    public void testPatter(){
        String flrcNo="2901300000001";
        String flrcNoPrefix=new StringBuilder("2901").append("3").toString();
        String regex = flrcNoPrefix + "\\d{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(flrcNo);
        System.out.println(matcher.matches());

    }

}