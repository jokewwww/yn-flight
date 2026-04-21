package com.higer.statistical.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.higer.statistical.service.CountService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TestService {

    /**
     * 测试数据,返回去年油量信息，统计用
     * @return
     */
    public static List<Map<String,Object>> getLastYearCountNum(){
        List<Map<String,Object>> result=Lists.newArrayList();
        List<Object[]> list= Lists.newArrayList();
        list.add(new Object[]{1, BigInteger.valueOf(14904), BigDecimal.valueOf(73932697)});
        list.add(new Object[]{2,BigInteger.valueOf(14530),BigDecimal.valueOf(71304213)});
        list.add(new Object[]{3,BigInteger.valueOf(15371),BigDecimal.valueOf(77545923)});
        list.add(new Object[]{4,BigInteger.valueOf(15011),BigDecimal.valueOf(75351519)});
        list.add(new Object[]{5,BigInteger.valueOf(15244),BigDecimal.valueOf(78792569)});
        list.add(new Object[]{6,BigInteger.valueOf(14769),BigDecimal.valueOf(77944327)});
        list.add(new Object[]{7,BigInteger.valueOf(15722),BigDecimal.valueOf(86128638)});
        list.add(new Object[]{8,BigInteger.valueOf(15916),BigDecimal.valueOf(87109088)});
        list.add(new Object[]{9,BigInteger.valueOf(15013),BigDecimal.valueOf(78859163)});
        list.add(new Object[]{10,BigInteger.valueOf(15506),BigDecimal.valueOf(77790964)});
        list.add(new Object[]{11,BigInteger.valueOf(14716),BigDecimal.valueOf(75332265)});
        list.add(new Object[]{12,BigInteger.valueOf(14460),BigDecimal.valueOf(72020759)});
        list.forEach(objects -> {
            Map<String,Object> map= Maps.newHashMap();
            map.put(CountService.MONTH_FIELD,objects[0]);
            map.put(CountService.TASK_NUM_FIELD,objects[1]);
            map.put(CountService.OIL_NUM_FIELD,objects[2]);
            result.add(map);
        });
        return result;
    }

    public static void setOldData(List<Map<String, Object>> maps) {
        Optional<Map<String, Object>> airoprtCode = maps.stream().filter(map -> StringUtils.equals("2901", Optional.ofNullable(map.get("AirportCode")).orElse("").toString())).findAny();
        if(airoprtCode.isPresent()){
            airoprtCode.get().put("LatestOilYear",getLastYearCountNum().get(Calendar.getInstance().get(Calendar.MONTH)).get(CountService.OIL_NUM_FIELD));
            airoprtCode.get().put("LatestTaskYear",getLastYearCountNum().get(Calendar.getInstance().get(Calendar.MONTH)).get(CountService.TASK_NUM_FIELD));
        }
    }

    public Map<String,Number> getByYearAndMonth(int month,int year){
        Map<String,Number> result=Maps.newHashMap();
        if(year==2018){
            Map<String, Object> map = getLastYearCountNum().get(month);
            result.put("oil", (Number) map.get(CountService.OIL_NUM_FIELD));
            result.put("task", (Number) map.get(CountService.TASK_NUM_FIELD));
        }else{
            result.put("oil",0);
            result.put("task",0);
        }
        return result;
    }
}
