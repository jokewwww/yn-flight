package com.higer.statistical.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 统计工具类
 */
@Slf4j
public class CountUtils {

    public static Map<Integer,Map<String,Object>> formatterMonthData(List<Map<String,Object>> list){
        Map<Integer,Map<String,Object>> result=Maps.newHashMap();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = Maps.newHashMap();
            map.put(CountService.OIL_NUM_FIELD,BigDecimal.ZERO);
            map.put(CountService.TASK_NUM_FIELD,BigInteger.ZERO);
            result.put(i,map);
        }
        list.forEach(map -> {
            Map<String, Object> yearMap = result.get(map.get(CountService.MONTH_FIELD));
            yearMap.put(CountService.OIL_NUM_FIELD, map.get(CountService.OIL_NUM_FIELD));
            yearMap.put(CountService.TASK_NUM_FIELD, map.get(CountService.TASK_NUM_FIELD));
        });
        return result;
    }

    /**
     * 计算同比环比公式
     * (a-b)/b
     * @param a 当前统计周期的数据
     * @param b 上一个统计周期的数据
     * @return 分母不为零，返回  **%，分母为零返回 -,保留两位小数
     */
    public static String division(Number a,Number b){
        try {
            log.info("当前周期数据："+a);
            log.info("上个周期数据："+b);
            log.info("计算结果："+(a.doubleValue()-b.doubleValue())/b.doubleValue());
            return (new BigDecimal(a.doubleValue()).subtract(new BigDecimal(b.doubleValue()))).divide(new BigDecimal(b.doubleValue()),2,RoundingMode.HALF_UP)+"%";
        } catch (ArithmeticException e) {
            return "-";
        }
    }

    public static String formatFlrcType(Integer flrcType) {
        switch (flrcType){
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
            default:
                return "-";
        }
    }

    public static Number getPercent(Number a,Number total){
        log.info("分子："+a.doubleValue());
        log.info("分母："+total.doubleValue());
        return new BigDecimal(a.doubleValue()).divide(new BigDecimal(total.doubleValue()),2,RoundingMode.HALF_UP);
    }
}
