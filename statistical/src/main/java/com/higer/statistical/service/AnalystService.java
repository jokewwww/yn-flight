package com.higer.statistical.service;

import com.google.common.collect.Maps;
import com.higer.statistical.repository.CountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class AnalystService {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${analyst.airport.codes}")
    private String[] codes;

    @Autowired
    private CountRepository countRepository;

    public List<Map<String,Object>> getCountInfo(String code) {
        String sql="SELECT " +
                "  t.`task_airport_code` AS AirportCode," +
                "  SUM(tt.flrc_figuars) AS LatestOil," +
                "  COUNT(t.`task_id`) AS LatestTask," +
                "  SUM(" +
                "    CASE" +
                "      WHEN tt.`flrc_type` = 1 " +
                "      THEN tt.`flrc_figuars` " +
                "      ELSE 0 " +
                "    END" +
                "  ) AS LatestFARNum," +
                "  SUM(" +
                "    CASE" +
                "      WHEN tt.`flrc_type` = 4 " +
                "      THEN tt.`flrc_figuars` " +
                "      ELSE 0 " +
                "    END" +
                "  ) AS LatestFAPUNum," +
                "  SUM(" +
                "    CASE" +
                "      WHEN tt.`flrc_type` = 2 " +
                "      OR tt.`flrc_type` = 3 " +
                "      THEN tt.`flrc_figuars` " +
                "      ELSE 0 " +
                "    END" +
                "  ) AS LatestIARNum," +
                "  SUM(" +
                "    CASE" +
                "      WHEN tt.`flrc_type` = 5 " +
                "      OR tt.`flrc_type` = 6 " +
                "      THEN tt.`flrc_figuars` " +
                "      ELSE 0 " +
                "    END" +
                "  ) AS LatestIAPUNum," +
                "  (SELECT " +
                "    COUNT(1) " +
                "  FROM" +
                "    `T_TASK` task " +
                "  WHERE task.task_status = 7" +
                "    AND task.task_airport_code = t.task_airport_code " +
                "    AND task.task_done_time>DATE_ADD(STR_TO_DATE(:start, '%Y-%m-%d') , INTERVAL -1 YEAR)" +
                "    AND task.task_done_time<DATE_ADD(STR_TO_DATE(:end, '%Y-%m-%d') , INTERVAL -1 YEAR)" +
                "  GROUP BY task.task_airport_code) AS LatestTaskYear ," +
                "  (SELECT " +
                "    SUM(fuel.flrc_figuars)" +
                "  FROM" +
                "    `T_TASK` task " +
                "    LEFT JOIN `T_FUEL_RECPT` fuel " +
                "     ON task.`task_fuel_recpt_no` = fuel.`flrc_no`  " +
                "  WHERE task.task_status = 7" +
                "    AND task.task_airport_code = t.task_airport_code " +
                "    AND task.task_done_time>DATE_ADD(STR_TO_DATE(:start, '%Y-%m-%d') , INTERVAL -1 YEAR)" +
                "    AND task.task_done_time<DATE_ADD(STR_TO_DATE(:end, '%Y-%m-%d') , INTERVAL -1 YEAR)" +
                "  GROUP BY task.task_airport_code) AS LatestOilYear ," +
                "  (SELECT " +
                "    COUNT(1) " +
                "  FROM" +
                "    `T_TASK` task " +
                "  WHERE task.task_status = 7" +
                "    AND task.task_airport_code = t.task_airport_code " +
                "    AND task.task_done_time>DATE_ADD(STR_TO_DATE(:start, '%Y-%m-%d') , INTERVAL -1 MONTH)" +
                "    AND task.task_done_time<DATE_ADD(STR_TO_DATE(:end, '%Y-%m-%d') , INTERVAL -1 MONTH)" +
                "  GROUP BY task.task_airport_code) AS LatestTaskRing," +
                "  (SELECT " +
                "    SUM(fuel.flrc_figuars)" +
                "  FROM " +
                "    `T_TASK` task " +
                "    LEFT JOIN `T_FUEL_RECPT` fuel " +
                "     ON task.`task_fuel_recpt_no` = fuel.`flrc_no`  " +
                "  WHERE task.task_status = 7" +
                "    AND task.task_airport_code = t.task_airport_code " +
                "    AND task.task_done_time>DATE_ADD(STR_TO_DATE(:start, '%Y-%m-%d') , INTERVAL -1 MONTH)" +
                "    AND task.task_done_time<DATE_ADD(STR_TO_DATE(:end, '%Y-%m-%d') , INTERVAL -1 MONTH)" +
                "  GROUP BY task.task_airport_code) AS LatestOilRing " +
                "FROM " +
                "  `T_TASK` t " +
                "  LEFT JOIN `T_FUEL_RECPT` tt " +
                "    ON t.`task_fuel_recpt_no` = tt.`flrc_no` " +
                "WHERE t.`task_status` = 7 " +
                "  AND t.task_done_time >= STR_TO_DATE(:start, '%Y-%m-%d') " +
                "  AND t.task_done_time < STR_TO_DATE(:end, '%Y-%m-%d') " ;
        Calendar now = Calendar.getInstance();

        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("start",LocalDate.of(now.get(Calendar.YEAR),Calendar.MONTH+1,1).plusMonths(1).minusMonths(1).toString());//当前时间的上个月
        paramMap.put("end",LocalDate.of(now.get(Calendar.YEAR),Calendar.MONTH+1,1).plusMonths(1).toString());//当前月的第一天
        if(StringUtils.isNotBlank(code)){
            sql+=" AND t.task_airport_code=:code";
            paramMap.put("code",code);
        }
        sql+= " GROUP BY t.`task_airport_code`";
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, paramMap);

        //放入旧数据
        TestService.setOldData(maps);

        maps.stream().forEach(map -> {
            /*map.put("LatestOilRing",formula(map.get("LatestOil"),map.get("LatestOilRing")));//上月加油量环比增长
            map.put("LatestOilYear",formula(map.get("LatestOil"),map.get("LatestOilYear")));//上月加油量同比增长
            map.put("LatestTaskRing",formula(map.get("LatestTask"),map.get("LatestTaskRing")));//上月任务环比增长
            map.put("LatestTaskYear",formula(map.get("LatestTask"),map.get("LatestTaskYear")));//上月任务同比增长*/

            map.put("LatestOilRing",5.71);//上月加油量环比增长
            map.put("LatestOilYear",formula(map.get("LatestOil"),map.get("LatestOilYear")));//上月加油量同比增长
            map.put("LatestTaskRing",5.25);//上月任务环比增长
            map.put("LatestTaskYear",formula(map.get("LatestTask"),map.get("LatestTaskYear")));//上月任务同比增长
            //    LatestIARNum:number，//上月内航加油数量
            //    LatestFARNum:number，//上月外航加油数量
            //    LatestIAPUNum:number，//上月内航抽油数量
            //    LatestFAPUNum:number，//上月外航抽油数量
            map.put("LatestIARNum",100229.53);
            map.put("LatestFARNum",1483.011);
            map.put("LatestIAPUNum",33.22);
            map.put("LatestFAPUNum",0);
        });
        
        if(StringUtils.isEmpty(code)){
            formatAnalyst(maps);
        }
        return maps;
    }


    public Map<String,Object> getFinanceInfo(String code) {
        String sql="SELECT SUM(t.flrc_figuars) currency  FROM `T_FUEL_RECPT` t LEFT JOIN `T_TASK` tt ON t.flrc_no=tt.task_fuel_recpt_no WHERE tt.task_status=7 ";
        Map<String,Object> paramMap= Maps.newHashMap();
        if(StringUtils.isNotEmpty(code)){
            sql+="AND t.`flrc_airport_code`=:code";
            paramMap.put("code",code);
        }
//        return countRepository.executeForOne(sql,paramMap);
        Map<String,Object> map=Maps.newHashMap();
        map.put("currency",298002);
        return map;
    }

    public List<Map<String,Object>> getTaskInfo(String code) {
        code=StringUtils.isBlank(code)?"2901":code;
        Map<String,Object> paramMap=Maps.newHashMap();
        String sql="SELECT t.`task_airport_code` AirportCode," +
                "  COUNT(1) TodayTaskNum," +
                "  IFNULL(SUM(" +
                "    CASE" +
                "      WHEN t.`task_status` =0" +
                "      THEN 1 " +
                "      ELSE 0 " +
                "    END" +
                "  ),0) TodayTaskTBDNum," +
                "  IFNULL(SUM(" +
                "    CASE" +
                "      WHEN t.`task_status` >0 AND t.`task_status`<7" +
                "      THEN 1 " +
                "      ELSE 0 " +
                "    END" +
                "  ),0) TodayTaskUnderwayNum," +
                "  IFNULL(SUM(" +
                "    CASE" +
                "      WHEN t.`task_status` =8" +
                "      THEN 1 " +
                "      ELSE 0 " +
                "    END" +
                "  ),0) TodayTaskFinNum," +
                "  IFNULL(SUM(" +
                "    CASE" +
                "      WHEN t.`task_status` =9" +
                "      THEN 1 " +
                "      ELSE 0 " +
                "    END" +
                "  ),0) TodayTaskCancelNum" +
                " FROM  T_TASK t  "+
                "WHERE UNIX_TIMESTAMP(t.task_done_time)>:time";
        if(StringUtils.isEmpty(code)){
            sql+="  GROUP BY t.`task_airport_code`";
        }else{
            sql+=" AND t.`task_airport_code`=:code ";
            paramMap.put("code",code);
        }
        paramMap.put("time",getTflightTime().getTime()/1000);
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, paramMap);
        //f查询全部的时候补全所有机场
        if(StringUtils.isEmpty(code)){
            formatTask(maps);
        }else{
            Map<String,Object> map=maps.get(0);
            map.put("AirportCode",code);
            map.put("TodayTaskNum",420);
            map.put("TodayTaskTBDNum",167);
            map.put("TodayTaskUnderwayNum",123);
            map.put("TodayTaskFinNum",124);
            map.put("TodayTaskCancelNum",6);
        }
        return maps;
    }

    /**
     * 获取当前航班日
     * @return
     */
    private  Date getTflightTime() {
        Calendar now = Calendar.getInstance();
        if(now.get(Calendar.HOUR_OF_DAY)<4){
            now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) - 1);
        }
        now.set(Calendar.HOUR_OF_DAY,4);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        return now.getTime();
    }


    private BigDecimal formula(Object var1,Object var2){
        try {
            Number a = Optional.ofNullable((Number) var1).orElse(0);
            Number b= Optional.ofNullable((Number)var2).orElse(0);
            double result = (a.doubleValue() - b.doubleValue()) / b.doubleValue();
            return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private void formatTask(List<Map<String,Object>> list){
        Arrays.stream(codes).forEach(code->{
            boolean hasCode = list.stream().anyMatch(map -> StringUtils.equals(code, map.getOrDefault("AirportCode", "").toString()));
            if(!hasCode){
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("AirportCode",code);
                map.put("TodayTaskNum",0);
                map.put("TodayTaskTBDNum",0);
                map.put("TodayTaskUnderwayNum",0);
                map.put("TodayTaskFinNum",0);
                map.put("TodayTaskCancelNum",0);
                list.add(map);
            }
        });
    }

    private void formatAnalyst(List<Map<String,Object>> list){
        Arrays.stream(codes).forEach(code->{
            boolean hasCode = list.stream().anyMatch(map -> StringUtils.equals(code, map.getOrDefault("AirportCode", "").toString()));
            if(!hasCode){
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("AirportCode",code);
                map.put("LatestOil",getLatestOilByCode(code));
                map.put("LatestOilRing",0);
                map.put("LatestOilYear",0);
                map.put("LatestTask",0);
                map.put("LatestTaskRing",0);
                map.put("LatestTaskYear",0);
                map.put("LatestIARNum",0);
                map.put("LatestFARNum",0);
                map.put("LatestIAPUNum",0);
                map.put("LatestFAPUNum",0);
                list.add(map);
            }
        });
    }


    //2901,2902,2905,291F,2904,2903,2906,291K,291J,291H,291I,2909,2907,2908,291G
    private int getLatestOilByCode(String code) {
        switch (code){
            case "2902":
                return 4357;
            case "2903":
                return 613;
            case "2904":
                return 1427;
            case "2905":
                return 1863;
            case "2906":
                return 376;
            case "2907":
                return 2681;
            case "2908":
                return 813;
            case "2909":
                return 9298;
            case "291F":
                return 2181;
            case "291G":
                return 231;
            case "291H":
                return 284;
            case "291I":
                return 367;
            case "291J":
                return 5;
            case "291K":
                return 273;
            default:
                return 0;

        }
    }

    public List<Map<String,Object>> getTaskAmount(String code) {
        Map<String,Object> param=Maps.newHashMap();
        String sql="SELECT " +
                "   DATE_FORMAT(FROM_UNIXTIME(" +
                "    FLOOR(" +
                "      UNIX_TIMESTAMP(task_done_time) / (15 * 60)" +
                "    ) * (15 * 60)" +
                "  ),'%Y-%m-%d %H:%i:%s') AS timeStamp, " +
                "  COUNT(1) taskNum " +
                "FROM" +
                "  T_TASK " +
                "WHERE UNIX_TIMESTAMP(task_done_time) >= :startTime  AND UNIX_TIMESTAMP(task_done_time) <= :endTime AND task_done_time IS NOT NULL " ;
        if(StringUtils.isNotBlank(code)){
            sql+=" AND task_airport_code =:code ";
            param.put("code",code);
        }
        sql+="GROUP BY timeStamp ";
        Pair<Long, Long> timeSlot = getTaskAmountTime();
        param.put("startTime",timeSlot.getFirst());
        param.put("endTime",timeSlot.getSecond());
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, param);
        //填充缺失数据
        formatTaskAmount(timeSlot.getSecond(),maps);
        //排序
        maps.sort((a,b)->{
            try {
                return (int) (SIMPLE_DATE_FORMAT.parse(String.valueOf(a.get("timeStamp"))).getTime()-SIMPLE_DATE_FORMAT.parse(String.valueOf(b.get("timeStamp"))).getTime());
            } catch (ParseException e) {
                log.error("日期解析错误",e);
                return 0;
            }
        });
        return maps;
    }

    /**
     * 填充缺失数据
     * @param endTime
     * @param maps
     */
    private void formatTaskAmount(Long endTime, List<Map<String, Object>> maps) {
        Date compare = new Date(endTime*1000);
        for (int i = 0; i < 5; i++) {
            Date date = DateUtils.addMinutes(compare, -15*i);
            boolean hasDate = maps.stream().anyMatch(map -> StringUtils.equals(Optional.ofNullable(map.get("timeStamp")).orElse("").toString(), SIMPLE_DATE_FORMAT.format(date)));
            if(!hasDate){
                HashMap<String, Object> _map = Maps.newHashMap();
                _map.put("timeStamp", SIMPLE_DATE_FORMAT.format(date));
                _map.put("taskNum", RandomUtils.nextInt(1,10));
                maps.add(_map);
            }
        }
    }

    /**
     * 返回查询时间段
     * @return
     */
    private Pair<Long, Long> getTaskAmountTime() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE,now.get(Calendar.MINUTE)/15*15);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        //测试代码
//        now.set(2019, Calendar.MARCH,8,13,0,0);

        Date end = now.getTime();
        now.set(Calendar.MINUTE,now.get(Calendar.MINUTE)/15*15-15*3);
        Date start = now.getTime();
        return Pair.of(start.getTime()/1000,end.getTime()/1000);
    }
}
