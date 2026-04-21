package com.higer.statistical.service;

import com.google.common.collect.Maps;
import com.higer.statistical.repository.CountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class CountService {

    public final static String MONTH_FIELD="month";
    public final static String TASK_NUM_FIELD="taskNum";
    public final static String OIL_NUM_FIELD="oilNum";
    public final static String FLRC_AIRL_NAME_FIELD="flrc_airl_name";
    public final static String FLRC_TYPE_FIELD="flrc_type";

    public final static String COMPARE_TASK_WITH_YEAR_FIELD="compareTaskWithYear";
    public final static String COMPARE_TASK_WITH_MONTH_FIELD="compareTaskWithMonth";
    public final static String COMPARE_OIL_WITH_YEAR_FIELD="compareOilWithYear";
    public final static String COMPARE_OIL_WITH_MONTH_FIELD="compareOilWithMonth";

    @Autowired
    private CountRepository countRepository;


    /**
     * 峰值（当前时间一小时之内的所有已完成的航班的加油任务） 加油量和任务数
     * @param airportCode
     * @param instance
     * @return
     */
    public Map<String, Object> countCrestValue(String airportCode, Calendar instance) {
        String sql="SELECT COUNT(t.`task_id`) taskNum,SUM(tt.`flrc_figuars`) oilNum FROM T_TASK t LEFT JOIN T_FUEL_RECPT tt ON t.`task_fuel_recpt_no`=tt.`flrc_no` WHERE t.task_status=7 AND t.task_airport_code =:code AND t.task_done_time>:start AND t.task_done_time<:end ";
        Map<String,Object> param=Maps.newHashMap();
        Date start=instance.getTime();
        instance.set(Calendar.YEAR,instance.get(Calendar.YEAR)-1);
        Date end=instance.getTime();
        param.put("code",airportCode);
        param.put("start",start);
        param.put("end",end);
        Map<String, Object> objectMap = countRepository.executeForOne(sql, param);
        objectMap.put(OIL_NUM_FIELD,Optional.ofNullable(objectMap.get(OIL_NUM_FIELD)).orElse(0));
        return objectMap;

    }

    /**
     * 统计（排序条件）：任务量和加油量排名前五的航空公司+其他
     * @param airportCode
     * @return
     */
    public Map<String,List<Map<String, Object>>> countRanking5(String airportCode) {
        Map<String,List<Map<String, Object>>> data=Maps.newHashMap();
        String sql="SELECT flrc_airl_name,SUM(flrc_figuars) oilNum FROM `T_FUEL_RECPT` t  LEFT JOIN `T_TASK` tt ON t.flrc_no=tt.task_fuel_recpt_no WHERE tt.task_Status=7 AND t.flrc_airport_code=:code  GROUP BY flrc_airl_name ORDER BY oilNum DESC";
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("code",airportCode);
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, paramMap);
        List<Map<String, Object>> oilResult = maps.stream().limit(5).collect(Collectors.toList());
        Integer total = maps.stream().map(map -> ((Number) map.get(OIL_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        Integer ranking = oilResult.stream().map(map -> ((Number) map.get(OIL_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        System.out.println(total);
        Map<String,Object> another=Maps.newHashMap();
        another.put(FLRC_AIRL_NAME_FIELD,"其他");
        another.put(OIL_NUM_FIELD,total-ranking);
        oilResult.add(another);

        sql="SELECT tt.flrc_airl_name,COUNT(t.task_id) taskNum FROM `T_TASK` t LEFT JOIN `t_fuel_recpt` tt ON t.task_fuel_recpt_no=tt.flrc_no WHERE t.`task_status`=7 AND t.task_airport_code=:code GROUP BY tt.flrc_airl_name ORDER BY taskNum DESC";
        maps = countRepository.executeSqlForList(sql, paramMap);
        List<Map<String, Object>> taskResult = maps.stream().limit(5).collect(Collectors.toList());
        total = maps.stream().map(map -> ((Number) map.get(TASK_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        ranking = taskResult.stream().map(map -> ((Number) map.get(TASK_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        another=Maps.newHashMap();
        another.put(FLRC_AIRL_NAME_FIELD,"其他");
        another.put(TASK_NUM_FIELD,total-ranking);
        taskResult.add(another);


        data.put("oilData",oilResult);
        data.put("taskData",taskResult);
        return data;
    }

    /**
     * 按月统计：任务数、共加多少油（同比、环比
     * @param airportCode
     * @return
     */
    public Map<Integer, Map<String, Object>> countByMonth(String airportCode) {
        String sql="SELECT MONTH(t.`task_done_time`) month,COUNT(t.`task_id`) taskNum,SUM(tt.`flrc_figuars`) oilNum FROM T_TASK t LEFT JOIN T_FUEL_RECPT tt ON t.`task_fuel_recpt_no`=tt.`flrc_no` WHERE t.task_status=7 AND YEAR(t.`task_done_time`)=:year AND  t.task_airport_code=:code GROUP BY MONTH(t.`task_done_time`)";
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("code",airportCode);
        paramMap.put("year",Calendar.getInstance().get(Calendar.YEAR));
        Map<Integer, Map<String, Object>> thisYear = CountUtils.formatterMonthData(countRepository.executeSqlForList(sql, paramMap));
        Map<Integer, Map<String, Object>> lastYear;
        if(Calendar.getInstance().get(Calendar.YEAR)==2019){
            //2018年假数据
            lastYear=CountUtils.formatterMonthData(TestService.getLastYearCountNum());
        }else{
            //2019年数据
            paramMap.put("year",Calendar.getInstance().get(Calendar.YEAR)-1);
            lastYear=CountUtils.formatterMonthData(countRepository.executeSqlForList(sql, paramMap));
        }
        //计算环比同比
        thisYear.forEach((k,v) -> {
            log.info("当前月："+k);
            log.info("任务数："+v.get(TASK_NUM_FIELD));
            log.info("加油量："+v.get(OIL_NUM_FIELD));
            log.info("----------任务同比----------");
            v.put(COMPARE_TASK_WITH_YEAR_FIELD,CountUtils.division((BigInteger)v.get(TASK_NUM_FIELD),(BigInteger)lastYear.get(k).get(TASK_NUM_FIELD)));
            log.info("----------油量同比----------");
            v.put(COMPARE_OIL_WITH_YEAR_FIELD,CountUtils.division((BigDecimal)v.get(OIL_NUM_FIELD),(BigDecimal)lastYear.get(k).get(OIL_NUM_FIELD)));
            log.info("----------任务环比----------");
            v.put(COMPARE_TASK_WITH_MONTH_FIELD,CountUtils.division((BigInteger)v.get(TASK_NUM_FIELD),k-1>0?(BigInteger)thisYear.get(k-1).get(TASK_NUM_FIELD):(BigInteger)lastYear.get(lastYear.size()-1).get(TASK_NUM_FIELD)));
            log.info("----------油量环比----------");
            v.put(COMPARE_OIL_WITH_MONTH_FIELD,CountUtils.division((BigDecimal)v.get(OIL_NUM_FIELD),k-1>0?(BigDecimal)thisYear.get(k-1).get(OIL_NUM_FIELD):(BigDecimal)lastYear.get(lastYear.size()-1).get(OIL_NUM_FIELD)));
        });

        return thisYear;
    }

    /**
     * 不同油单类型的任务数量和加油升数
     * @param airportCode
     * @return
     */
    public List<Map<String, Object>> countFlrcType(String airportCode) {
        String sql="SELECT t.`flrc_type`,SUM(t.flrc_figuars) oilNum,COUNT(tt.`task_id`) taskNum FROM `T_FUEL_RECPT` t LEFT JOIN `T_TASK` tt ON t.flrc_no=tt.task_fuel_recpt_no WHERE tt.`task_status`=7 AND t.flrc_airport_code=:code GROUP BY t.`flrc_type`";
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("code",airportCode);
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, paramMap);
        maps.forEach(map -> {
            map.put(FLRC_TYPE_FIELD,CountUtils.formatFlrcType((int)map.get(FLRC_TYPE_FIELD)));
        });
        return maps;
    }

    /**
     * 所有机场的任务量和加油量占比
     * @return
     */
    public List<Map<String, Object>> countByRatio() {
        String sql="SELECT tt.`flrc_airl_name`,COUNT(t.task_id) taskNum,SUM(tt.flrc_figuars) oilNum FROM `T_TASK` t LEFT JOIN `T_FUEL_RECPT` tt ON t.`task_fuel_recpt_no`= tt.`flrc_no` WHERE t.`task_status`=7 GROUP BY tt.`flrc_airl_name`";
        List<Map<String, Object>> list = countRepository.executeSqlForList(sql, Maps.newHashMap());
        Integer totalOil = list.stream().map(map -> ((Number) map.get(OIL_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        Integer totalTask = list.stream().map(map -> ((Number) map.get(TASK_NUM_FIELD)).intValue()).reduce(Integer::sum).orElse(0);
        list.forEach(map -> {
            map.put(OIL_NUM_FIELD,CountUtils.getPercent((Number) map.get(OIL_NUM_FIELD),totalOil));
            map.put(TASK_NUM_FIELD,CountUtils.getPercent((Number) map.get(TASK_NUM_FIELD),totalTask));
        });
        return list;
    }
}
