package com.example.contrast_entity.service;

import com.alibaba.fastjson.JSON;
import com.example.contrast_entity.entity.TFlight;
import com.example.contrast_entity.entity.TFlightChangeLog;
import com.example.contrast_entity.repository.TFlightChangeLogRepository;
import com.example.contrast_entity.repository.TFlightRepository;
import com.example.contrast_entity.util.*;
import com.google.common.collect.Maps;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/8 13:29
 * @Description:
 */
@Service
public class FlightChangeLogsService {
    private static final Logger logger = LogManager.getLogger(FlightChangeLogsService.class);
    @Autowired
    private TFlightChangeLogRepository tFlightChangeLogRepository;

    @Autowired
    private TFlightRepository tFlightRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PersistenceContext
    private EntityManager entityManagerFlight;

    @Transient
    public void saveFlightChangeLogs(String userId, String oldData, String newData, String record) {
        logger.info("oldData->"+oldData);
        logger.info("-------------");
        logger.info("newData->"+newData);
        try {
            Thread.sleep(2000);
            Map<String, Object> map = new HashMap<String, Object>();
            TFlight oldDataTFlight = null;
            TFlight newDataTFlight = null;
            oldDataTFlight = JSON.parseObject(oldData, TFlight.class);
            newDataTFlight = JSON.parseObject(newData, TFlight.class);
            if (oldDataTFlight != null && newDataTFlight != null) {
                String flgtPlacecode1 = oldDataTFlight.getFlgtPlacecode();
                String flgtPlacecode = newDataTFlight.getFlgtPlacecode();
                logger.info("oldDataTFlight->--"+flgtPlacecode1+"newDataTFlight->"+flgtPlacecode);
                Boolean lockData = !StringUtils.isEmpty(oldDataTFlight.getFlgtAdid()) && "D".equals(oldDataTFlight.getFlgtAdid());
                Boolean lockNewData = !StringUtils.isEmpty(newDataTFlight.getFlgtAdid()) && "D".equals(newDataTFlight.getFlgtAdid());
                Boolean aLockData = !StringUtils.isEmpty(oldDataTFlight.getFlgtAdid()) && "A".equals(oldDataTFlight.getFlgtAdid());
                Boolean aLockNewData = !StringUtils.isEmpty(newDataTFlight.getFlgtAdid()) && "A".equals(newDataTFlight.getFlgtAdid());

                map.put("flgtFlno", oldDataTFlight.getFlgtFlno() == null ? "" : oldDataTFlight.getFlgtFlno());
                map.put("flgtFfid", oldDataTFlight.getFlgtFfid() == null ? "" : oldDataTFlight.getFlgtFfid());
//                if (!StringUtils.isEmpty(oldDataTFlight.getFlgtId())) {
//                    TTask byTaskFlightId = tTaskRepository.findByTaskFlightId(oldDataTFlight.getFlgtId());
//                    if (byTaskFlightId != null) {
//                        map.put("taskId", byTaskFlightId.getTaskId());
//                    } else {
//                        map.put("taskId", "");
//                    }
//                }
                List<Map<String, Object>> compare = CompareObjectUtil.compare(oldDataTFlight, newDataTFlight);
                List<Map<String, Object>> compareStatus = CompareObjectUtil.compareStatus(oldDataTFlight, newDataTFlight);
                //当存在数据差异的时候 才发送 redis  和存库
                if (null != compare && compare.size() > 0) {
                    map.put("flightchangelogs", compare);
                    String jsonData = JSON.toJSONString(map);
                    TFlightChangeLog tFlightChangeLog = new TFlightChangeLog();
                    tFlightChangeLog.setFfid(oldDataTFlight.getFlgtFfid());
                    tFlightChangeLog.setFlgtFlno(oldDataTFlight.getFlgtFlno());
                    tFlightChangeLog.setJsonData(jsonData);
                    tFlightChangeLog.setCreateTime(new Date());
                    if(lockData || lockNewData){
                        if ( DateUtil.isNow(oldDataTFlight.getFlgtFlop()) && oldDataTFlight.getFlgtFfid() != null && oldDataTFlight.getFlgtFlno() != null) {
                            if (map.size() > 0) {
//                            if (StringUtils.isEmpty(oldDataTFlight.getFlgtId())) {
//                                TTask byTaskFlightId = tTaskRepository.findByTaskFlightId(oldDataTFlight.getFlgtId());
//                                if (byTaskFlightId != null && !StringUtils.isEmpty(byTaskFlightId.getTaskOpeStaffId())) {
//                                    SendMsg2Redis.testDingYue(redisTemplate, byTaskFlightId.getTaskOpeStaffId(), "",
//                                            Constant.FLIGHT_CHANGE_LOG, map);
//                                }
//                            } else {
                                SendMsg2Redis.testDingYue(redisTemplate, userId, "",
                                        Constant.FLIGHT_CHANGE_LOG, map);
//                            }
                            }
                            TFlightChangeLog tFlightChangeLog1 = tFlightChangeLogRepository.saveAndFlush(tFlightChangeLog);
                            logger.info("flight_change_log的  ID是  " + tFlightChangeLog1.getId() + "   当前推送的航显原数据是 ------------" + record);
                        }
                    }
                    if(aLockData && aLockNewData){
                        if ( DateUtil.isNow(oldDataTFlight.getFlgtFlop()) && oldDataTFlight.getFlgtFfid() != null && oldDataTFlight.getFlgtFlno() != null) {
                            if (map.size() > 0) {
                                if(!StringUtils.isEmpty(oldDataTFlight.getFlgtLinkFlno())){
                                    SendMsg2Redis.testDingYue(redisTemplate, userId, "",
                                            Constant.FLIGHT_CHANGE_LOG, map);
                                }
                            }
                            TFlightChangeLog tFlightChangeLog1 = tFlightChangeLogRepository.saveAndFlush(tFlightChangeLog);
                            logger.info("A    flight_change_log的  ID是  " + tFlightChangeLog1.getId() + "   当前推送的航显原数据是 ------------" + record);
                        }
                    }
                }
                if (null != compareStatus && compareStatus.size() > 0) {
                    if(lockData || lockNewData){
                        if(!StringUtils.isEmpty(oldDataTFlight.getFlgtFfid()) &&  DateUtil.isNow(oldDataTFlight.getFlgtFlop())){
                            //标识
                            AtomicReference<Boolean> regn = new AtomicReference<>(false);
                            AtomicReference<Boolean> placecode = new AtomicReference<>(false);
                            TFlight byFlgtFfid = tFlightRepository.findByFlgtFfid(oldDataTFlight.getFlgtFfid());
                            if(byFlgtFfid != null){
                                TFlight newFlight = new TFlight();
                                ModelAssistant.copyProperties(byFlgtFfid,newFlight);
                                Map<String, Object> map1 = pingSql(oldDataTFlight.getFlgtFfid());
                                if(map1 != null && map1.size() > 0){
                                    TFlight finalNewDataTFlight = newDataTFlight;
                                    TFlight finalOldDataTFlight = oldDataTFlight;
                                    compareStatus.forEach(maps->{
                                        if("flgtRegn".equals(maps.get("fieldName"))){
                                            map1.put("flgtRegn", finalNewDataTFlight.getFlgtRegn());
                                            regn.set(true);
                                            newFlight.setFlgtRegnStatus(1);
                                            newFlight.setFlgtRegn(finalNewDataTFlight.getFlgtRegn());
                                            map1.put("flgtRegnStatus",1);
                                        }else{
                                            newFlight.setFlgtRegn(finalOldDataTFlight.getFlgtRegn());
                                            newFlight.setFlgtRegnStatus(0);
                                            map1.put("flgtRegnStatus",0);
                                        }
                                        if("flgtPlacecode".equals(maps.get("fieldName"))){
                                            logger.info("finalNewDataTFlight.getFlgtPlacecode()------------------------------------------------"+finalNewDataTFlight.getFlgtPlacecode());
                                            map1.put("flgtPlacecode",finalNewDataTFlight.getFlgtPlacecode());
                                            map1.put("flgtPlacecodeIn",finalNewDataTFlight.getFlgtPlacecode());
                                            placecode.set(true);
                                            newFlight.setFlgtPlacecodeStatus(1);
                                            newFlight.setFlgtPlacecode(finalNewDataTFlight.getFlgtPlacecode());
                                            map1.put("flgtPlacecodeStatus",1);
                                        }else{
                                            newFlight.setFlgtPlacecode(finalOldDataTFlight.getFlgtPlacecode());
                                            newFlight.setFlgtPlacecodeStatus(0);
                                            map1.put("flgtPlacecodeStatus",0);
                                        }
                                    });
                                    if(placecode.get() || regn.get()){
                                        logger.info(newFlight.getFlgtFfid() +" newFlight.getFlgtRegnStatus();_>"+newFlight.getFlgtRegnStatus());
                                        logger.info(newFlight.getFlgtFfid() +" newFlight.getFlgtRegn();_>"+newFlight.getFlgtRegn());
                                        logger.info(newFlight.getFlgtFfid() +" newFlight.getFlgtPlacecodeStatus()_>"+newFlight.getFlgtPlacecodeStatus());
                                        logger.info(newFlight.getFlgtFfid() +" newFlight.getFlgtPlacecode()_>"+newFlight.getFlgtPlacecode());
                                        logger.info(newFlight.getFlgtFfid() +" 保存之前的 flight对象 机位号 ---->  "+newFlight.getFlgtFlno()+"   机位号  ----->"+newFlight.getFlgtPlacecode() + "飞机号 ----->" +finalNewDataTFlight.getFlgtRegn() +"ffid----->"+newFlight.getFlgtFfid() );

                                        int update = tFlightRepository.update(newFlight.getFlgtPlacecodeStatus(), newFlight.getFlgtRegnStatus(),newFlight.getFlgtRegn(),newFlight.getFlgtPlacecode(), newFlight.getFlgtFfid());
                                        if(update != 1){
                                            logger.info("update机位变更失败");
                                        }


                                        logger.info("修改之后的flightId "+newFlight.getFlgtFlno()+"---"+newFlight.getFlgtAdid()+"--->"+newFlight.getFlgtPlacecode()+"---->" + newFlight.getFlgtPlacecodeStatus()+"---->"+newFlight.getFlgtRegn()+"---->"+newFlight.getFlgtRegnStatus());
                                        TFlight tflightChangeLog = tFlightRepository.findTflightChangeLog(
                                                newFlight.getFlgtLinkFlno(),
                                                newFlight.getFlgtLinkFlop(),
                                                newFlight.getFlgtLinkRepeat(),
                                                newFlight.getFlgtRepeat()
                                        );
                                        if(tflightChangeLog!= null){
                                            tflightChangeLog.setFlgtPlacecodeStatus(newFlight.getFlgtPlacecodeStatus());
                                            tflightChangeLog.setFlgtRegnStatus(newFlight.getFlgtRegnStatus());
                                            int update1 = tFlightRepository.update(tflightChangeLog.getFlgtPlacecodeStatus(), tflightChangeLog.getFlgtRegnStatus(),tflightChangeLog.getFlgtRegn(),tflightChangeLog.getFlgtPlacecode(), tflightChangeLog.getFlgtFfid());
                                            if(update1 != 1){
                                                logger.info("tflightChangeLogupdate机位变更失败");
                                            }
                                        }
                                        Map<String,Object> maps = Maps.newHashMap();
                                        logger.info("推送前flgtPlacecodeInflgtPlacecodeInflgtPlacecodeIn-----------------flgtPlacecodeInflgtPlacecodeInflgtPlacecodeIn--------"+map1.get("flgtPlacecode")  + map1.get("flgtPlacecodeIn")  );
                                        maps.put("flight",map1);
                                        SendMsg2Redis.testDingYue(redisTemplate, userId, Constant.TASKFLIGHT, "18",
                                                maps);
                                        SendMsg2Redis.testDingYue(redisTemplate, userId, Constant.TASKFLIGHT, "58",
                                                maps);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseObject getFlightChangeLogs(String ffid, Integer type) {
        try {
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("flgtAdid", type);
            if (StringUtils.isEmpty(ffid)) {
                return ResponseObject.error("ffid 不可为空");
            }
            List<TFlightChangeLog> byFfid = tFlightChangeLogRepository.findByFfid(ffid);
            List<Object> list = new ArrayList<Object>();

           /* List<Object>  a =  byFfid.parallelStream()
                    .filter(tFlightChangeLog -> null != JSON.parse(tFlightChangeLog.getJsonData()) )
                    .map(tFlightChangeLog -> JSON.parse(tFlightChangeLog.getJsonData())).collect(toList());*/

            byFfid.forEach(tFlightChangeLog -> {
                Object parse = JSON.parse(tFlightChangeLog.getJsonData());
                if (parse != null) {
                    list.add(parse);
                }
            });
            map.put("data", list);
            lists.add(map);
            return ResponseObject.success(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error");
    }

    public ResponseObject getFlightChangeLogsLink(String ffid) {
        try {
            // 关联航班
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> map1 = new HashMap<String, Object>();
            //出港航班
            List<TFlightChangeLog> cacheData = tFlightChangeLogRepository.findByFfid(ffid);
            map.put("flgtAdid", 1);
            if (cacheData.size() > 0) {
                List<Object> list = new ArrayList<Object>();
                cacheData.forEach(tFlightChangeLog -> {
                    Object parse = JSON.parse(tFlightChangeLog.getJsonData());
                    if (parse != null) {
                        list.add(parse);
                    }
                });
                map.put("data", list);
                TFlightChangeLog tFlightChangeLog = cacheData.get(0);
                if (!StringUtils.isEmpty(tFlightChangeLog.getFfid())) {
                    TFlight byFlgtFfid = tFlightRepository.findByFlgtFfid(tFlightChangeLog.getFfid());
                    if (byFlgtFfid != null) {
                        // 关联航班日期 , 当天关联次数 , 自身关联次数 航班号
                        /**
                         *   private String flgtFlno;      // 航班号
                         *     private Date flgtLinkFlop;      // 连接航班日期
                         *     private Integer flgtLinkRepeat; //  连接航班连接次数
                         *     private Integer flgtRepeat;     //  航班连接次数
                         */
                        TFlight tflightChangeLog = tFlightRepository.findTflightChangeLog(
                                byFlgtFfid.getFlgtLinkFlno(),
                                byFlgtFfid.getFlgtLinkFlop(),
                                byFlgtFfid.getFlgtLinkRepeat(),
                                byFlgtFfid.getFlgtRepeat()
                        );
                        //进港航班
                        List<TFlightChangeLog> byFfid1 = tFlightChangeLogRepository.findByFfid(tflightChangeLog.getFlgtFfid());
                        map1.put("flgtAdid", 0);
                        if (byFfid1.size() > 0) {
                            List<Object> lists1 = new ArrayList<Object>();
                            byFfid1.forEach(tFlightChangeLog1 -> {
                                Object parse = JSON.parse(tFlightChangeLog1.getJsonData());
                                if (parse != null) {
                                    lists1.add(parse);
                                }
                            });
                            map1.put("data", lists1);
                        } else {
                            map1.put("data", "");
                        }
                    }
                }
            } else {
                map.put("data", "");
            }
            lists.add(map);
            lists.add(map1);
            return ResponseObject.success(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error");
    }



    private Map<String,Object> pingSql(String ffid){
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n" +
                "\t\ttask.task_id AS taskId,\n" +
                "\t\ttask.task_ope_staff_id AS taskOpeStaffId,\n" +
                "\t\ttask.task_content AS taskContent,\n" +
                "\t\ttask.task_status AS taskStatus,\n" +
                "\t\ttask.task_asg_time AS taskAsgTime,\n" +
                "\t\ttask.task_acc_time AS taskAccTime,\n" +
                "\t\ttask.task_chag_sta_time AS taskChagStaTime,\n" +
                "\t\ttask.task_chag_end_time AS taskChagEndTime,\n" +
                "\t\ttask.task_done_time AS taskDoneTime,\n" +
                "\t\ttask.task_fuel_recpt_no AS taskFuelRecptNo,\n" +
                "\t\ttask.task_vehi_no AS taskVehiNo,\n" +
                "\t\ttask.task_cre_staff_id AS taskCreStaffId,\n" +
                "\t\ttask.task_starmark AS taskStarmark,\n" +
                "\t\ttask.task_rec_cre_time AS taskRecCreTime,\n" +
                "\t\tflight.flgt_id AS flgtId,\n" +
                "\t\tflight.flgt_ffid AS flgtFfid,\n" +
                "\t\tflight.flgt_airport_code AS flgtAirportCode,\n" +
                "\t\tflight.flgt_aptarea_code AS flgtAptareaCode,\n" +
                "\t\tflight.flgt_flno AS flgtFlno,\n" +
                "\t\tflight.flgt_flop AS flgtFlop,\n" +
                "\t\tflight.flgt_acname AS flgtAcname,\n" +
                "\t\tflight.flgt_regn AS flgtRegn,\n" +
                "\t\tflight.flgt_placecode AS flgtPlacecode,\n" +
                "\t\tflight.flgt_al2c AS flgtAl2c,\n" +
                "\t\tflight.flgt_alcname AS flgtAlcname,\n" +
                "\t\tflight.flgt_d_stot AS flgtDStot,\n" +
                "\t\tflight.flgt_d_etot AS flgtDEtot,\n" +
                "\t\tflight.flgt_d_atot AS flgtDAtot,\n" +
                "\t\tflight.flgt_org3c AS flgtOrg3c,\n" +
                "\t\tflight.flgt_orgnm AS flgtOrgnm,\n" +
                "\t\tflight.flgt_trs3c1 AS flgtTrs3c1,\n" +
                "\t\tflight.flgt_trs3c2 AS flgtTrs3c2,\n" +
                "\t\tflight.flgt_trs3c3 AS flgtTrs3c3,\n" +
                "\t\tflight.flgt_trs3c4 AS flgtTrs3c4,\n" +
                "\t\tflight.flgt_trs3c5 AS flgtTrs3c5,\n" +
                "\t\tflight.flgt_trsnm1 AS flgtTrsnm1,\n" +
                "\t\tflight.flgt_trsnm2 AS flgtTrsnm2,\n" +
                "\t\tflight.flgt_trsnm3 AS flgtTrsnm3,\n" +
                "\t\tflight.flgt_trsnm4 AS flightValic,\n" +
                "\t\tflight.flgt_trsnm5 AS flgtVialc,\n" +
                "\t\tflight.flgt_des3c AS flgtDes3c,\n" +
                "\t\tflight.flgt_desnm AS flgtDesnm,\n" +
                "\t\tflight.flgt_adid AS flgtAdid,\n" +
                "\t\tflight.flgt_flti AS flgtFlti,\n" +
                "\t\tflight.flgt_ftyp AS flgtFtyp,\n" +
                "\t\tflight.flgt_proxy AS flgtProxy,\n" +
                "\t\tflight.flgt_fnflag AS flgtFnflag,\n" +
                "\t\tflight.flgt_game AS flgtGame,\n" +
                "\t\tflight.flgt_chocks_in AS flgtChocksIn,\n" +
                "\t\tflight.flgt_chocks_out AS flgtChocksOut,\n" +
                "\t\tflight.flgt_vip AS flgtVip,\n" +
                "\t\tflight.flgt_num AS flgtNum,\n" +
                "\t\tflight.flgt_repeat AS flgtRepeat, \n" +
                "\t\tflight.flgt_link_flop AS flgtLinkFlop, \n" +
                "\t\tflight.flgt_link_repeat AS flgtLinkRepeat, \n" +
                "\t\tflight.flgt_otc AS flgtOtc, \n" +
                "\t\ttf2.flgt_flno AS flgtLinkFlno,\n" +
                "\t\ttf2.flgt_org3c AS beorg3c,\n" +
                "\t\ttf2.flgt_a_stot AS flgtAStot,\n" +
                "\t\ttf2.flgt_a_etot AS flgtAEtot,\n" +
                "\t\ttf2.flgt_a_atot AS flgtAAtot,\n" +
                "\t\ttf2.flgt_placecode AS flgtPlacecodeIn\n" +
                "\t\tFROM T_FLIGHT tf2\n" +
                "\t\tLEFT JOIN T_FLIGHT flight\n" +
                "\t\t\tON flight.flgt_adid = 'D'\n" +
                "\t\t\tAND flight.flgt_airport_code = tf2.flgt_airport_code \n" +
                "\t\t\tAND ((flight.flgt_link_flno IS NOT NULL AND flight.flgt_link_flno <> '' AND tf2.flgt_flno LIKE CONCAT('%',flight.flgt_link_flno,'%'))) \n" +
                "\t\t\tAND ((tf2.flgt_link_flno IS NOT NULL AND tf2.flgt_link_flno <> '' AND flight.flgt_flno LIKE CONCAT('%',tf2.flgt_link_flno,'%'))) \n" +
                "\t\t\tAND flight.flgt_regn = tf2.flgt_regn\n" +
                "\t\t\tAND flight.flgt_link_flop = tf2.flgt_flop\n" +
                "\t\t\tAND flight.flgt_flop = tf2.flgt_link_flop\n" +
                "\t\t\tAND flight.flgt_repeat = tf2.flgt_link_repeat\n" +
                "\t\t\tAND flight.flgt_link_repeat = tf2.flgt_repeat\n" +
                "\t\tLEFT JOIN T_TASK task ON task.task_flight_id = flight.flgt_id\n" +
                "\t\tWHERE \n" +
                "\t\t tf2.flgt_adid = 'A' AND flight.flgt_ffid = \""+ ffid+"\"");
       // logger.info("---------------------------"+sql.toString());
        Query querys = entityManagerFlight.createNativeQuery(sql.toString());
        querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = querys.getResultList();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : rows) {
            Map row = (Map) obj;
            list.add(row);
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public ResponseObject test() {
        try {
            List<TFlight> alls = tFlightRepository.findAlls();
            alls.forEach(tFlight -> {
                if(StringUtils.isEmpty(tFlight.getFlgtPlacecode())){
                    List<TFlightChangeLog> byFfid = tFlightChangeLogRepository.findByFfid(tFlight.getFlgtFfid());
                    byFfid.forEach(tFlightChangeLog -> {
                        Object parse = JSON.parse(tFlightChangeLog.getJsonData());
                        Map map = (Map) parse;
                        Object parse1 = JSON.parse(String.valueOf(map.get("flightchangelogs")));
                        List<Map> a1 = (List<Map>) parse1;
                        if("flgtPlacecode".equals(String.valueOf(a1.get(0).get("fieldName")))){
                            if(!StringUtils.isEmpty(String.valueOf(a1.get(0).get("newValue")))){
                                tFlight.setFlgtPlacecode(String.valueOf(a1.get(0).get("newValue")));
                                tFlightRepository.saveAndFlush(tFlight);
                            }
                        }
                    });
                }
            });
            return ResponseObject.success("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error");
    }

    public static void main(String[] args) {
        String s = "{\"flgtFlno\":\"MU5748\",\"flightchangelogs\":[{\"timeStamp\":\"2019-06-19 10:30:27\",\"newValue\":\"143\",\"fieldName\":\"flgtPlacecode\",\"oldValue\":\"\"}],\"flgtFfid\":\"68f42dee57cb44afb595a77f91ab7260\"}";


        Object parse = JSON.parse(s);
        Map a = (Map) parse;
        Object parse1 = JSON.parse(String.valueOf(a.get("flightchangelogs")));
        List<Map> a1 = (List<Map>) parse1;
        logger.info(a1.get(0).get("fieldName"));
       // logger.info(a1.get("fieldName"));
    }
}
