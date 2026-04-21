package com.zhoildataexchange.service;

import com.alibaba.fastjson.JSON;
import com.zhoildataexchange.component.KafkaServiet;
import com.zhoildataexchange.constant.Constant;
import com.zhoildataexchange.entity.flight.TAirportCode;
import com.zhoildataexchange.entity.flight.TFlight;
import com.zhoildataexchange.entity.flight.TTask;
import com.zhoildataexchange.entity.flight.in.InTFlight;
import com.zhoildataexchange.entity.user.TStaff;
import com.zhoildataexchange.repository.flight.TAirportCodeRepository;
import com.zhoildataexchange.repository.flight.TFlightRepository;
import com.zhoildataexchange.repository.flight.TTaskRepository;
import com.zhoildataexchange.repository.user.TStaffRepository;
import com.zhoildataexchange.util.DateUtil;
import com.zhoildataexchange.util.SendMsg2Redis;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:34
 * @Description:
 */
@Service
public class KafkaService {

    private final static Logger log = LoggerFactory.getLogger(KafkaService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PersistenceContext
    private EntityManager entityManagerFlight;

    @Autowired
    private StringRedisTemplate staRedis;

    @Autowired
    private KafkaServiet kafkaServiet;

    @Autowired
    private TFlightRepository tFlightRepository;

    @Autowired
    private TAirportCodeRepository tAirportCodeRepository;

    @Autowired
    private TTaskRepository tTaskRepository;

    @Autowired
    private TStaffRepository tStaffRepository;


    /**
     * @Description: 南航航班逻辑处理
     * @Param: [inTFlight]
     * @return: int
     * @Author: XiuHongXin
     * @Date: 2020/3/18
     *
     * {"flgtAcname":"","flgtAdid":"D","flgtAl2C":"CZ","flgtAlcname":"中国南方航空股份有限公司","flgtChockFuel":1000,"flgtDes3C":"PVG
     * ","flgtDesnm":"上海浦东机场","flgtFfid":"1121","flgtFlno":"CZ6501","flgtFlop":1608134400000,
     * "flgtFlti":"D","flgtFtyp":"","flgtMissionProp":"W/Z","flgtOrg3C":"SHE","flgtOrgnm":"沈阳桃仙国际机场","flgtPlacecode":"117",
     * "flgtRegn":"B6817","flgtTakeoffFuel":2000,"flgtTrs3C1":"","flgtTrsnm1":"","flgtVialc":""}
     *
     *
     */

    public synchronized void flightUpdateOrAddOrDelete(InTFlight inTFlight) {
        Boolean lock = false;

        System.out.println("南航原数据----" + JSON.toJSONString(inTFlight));

        if(StringUtils.isNotEmpty(inTFlight.getFlgtOlvr())){
            inTFlight.setFlgtOtat("Y");
        }

        // 取机场的 三字码  航班号  航班日  进出港 确定唯一航班
        if (StringUtils.isNotEmpty(inTFlight.getFlgtOrg3C())
                && null != inTFlight.getFlgtFlop()
                && StringUtils.isNotEmpty(inTFlight.getFlgtFlno())
                && StringUtils.isNotEmpty(inTFlight.getFlgtAdid())
                && inTFlight.getFlgtAdid().equals("D")
                && null != inTFlight.getFlgtTakeoffFuel() && 0 != inTFlight.getFlgtTakeoffFuel()
                ) {
            TAirportCode one = tAirportCodeRepository.findByApcdIataCode(inTFlight.getFlgtOrg3C());
            if (null != one && StringUtils.isNotEmpty(one.getApcdCnafAirportCode())) {
                TFlight findOne = tFlightRepository.findByFlgtFlnoLikeAndFlgtFlopAndFlgtAirportCodeAndFlgtAdid(
                        inTFlight.getFlgtFlno(),
                        inTFlight.getFlgtFlop(),
                        one.getApcdCnafAirportCode(),
                        inTFlight.getFlgtAdid()
                );
                Map webMap = new HashMap<String, Object>();
                SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //进入赋值流程
                if (null != findOne) {
                    if(StringUtils.isNotEmpty(findOne.getFlgtOlvr()) && StringUtils.isNotEmpty(inTFlight.getFlgtOlvr())){
                        if(!findOne.getFlgtOlvr().equals(inTFlight.getFlgtOlvr())){
                            findOne.setFlgtOlvrIsupdate(1);
                            lock = true;
                        }
                    }

                    findOne.setFlgtTakeoffFuel(inTFlight.getFlgtTakeoffFuel());
                    if(StringUtils.isNotEmpty(inTFlight.getFlgtOtat())){
                        findOne.setFlgtOtat(inTFlight.getFlgtOtat());
                    }else{
                        findOne.setFlgtOtat("N");
                    }
                    findOne.setFlgtChockFuel(inTFlight.getFlgtChockFuel());
                    findOne.setFlgtOtatFuel(inTFlight.getFlgtOtatFuel());
                    findOne.setFlgtOlvr(inTFlight.getFlgtOlvr());
                    TFlight newFlight = save(findOne);
                    List<TTask> save = tTaskRepository.findByTaskFlightId(newFlight.getFlgtId());
                    if (save.size() > 0) {
                        Boolean finalLock = lock;
                        save.forEach(tTask -> {
                            // 航班更新redis 推送信息
                            // 根据任务ID查出单条任务航班信息
                            Map<String, Object> taskAndFlightById = getTaskAndFlightById(tTask.getTaskId());
                            if (taskAndFlightById != null) {
                                // 垮库查询获取调度员ID
                                List<TStaff> tStaffs1 = tStaffRepository.findByStaffAirportCode(one.getApcdCnafAirportCode());
                                String userId = tStaffs1.stream().map(TStaff::getStaffId).collect(Collectors.joining(","));
                                Date flgtAAtot = newFlight.getFlgtAAtot();
                                Object flgtAAtot1 = taskAndFlightById.get("flgtAAtot");
                                if (flgtAAtot1 == null && flgtAAtot != null) {
                                    taskAndFlightById.put("flgtAAtot", flgtAAtot);
                                    newFlight.setFlgtAAtot(newFlight.getFlgtAAtot());
                                    TFlight newFlight1 = save(newFlight);
                                }
                                taskAndFlightById.put("flrcType", 3);
                                // 把要推送的航班任务对象放进Map集合中
                                webMap.put("flight", taskAndFlightById);
                                // 判断如果航班是本场的话再推送一条本场航班消息
                                if (Constant.FLGT_DGAME.equals(taskAndFlightById.get("flgtGame"))) {
                                    // 把要推送的航班任务对象放进Map集合中
                                    webMap.put("selfFlight", taskAndFlightById);
                                }
                                Object flgtDStot = taskAndFlightById.get("flgtDStot");
                                if (null != flgtDStot) {
                                    try {
                                        if (DateUtil.isEffectiveDate(sim1.parse(String.valueOf(flgtDStot)))) {
                                            // 当天航班推送消息
                                           // if (taskAndFlightById.get("flgtNum") != null && Integer.valueOf(String.valueOf(taskAndFlightById.get("flgtNum"))) > 0) {
                                                // 推送给调度员
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                        Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---879L---" + JSON.toJSONString(webMap));
                                                // 推送给加油员
                                                if (1 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                        || 3 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                        || 4 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                        || 5 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))) {
                                                    SendMsg2Redis.testDingYue(stringRedisTemplate, String.valueOf(taskAndFlightById.get("taskOpeStaffId")), Constant.TASKFLIGHT,
                                                            Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                }
                                            if(finalLock){
                                                System.out.println("===ConstantConstantConstantConstant==" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---飞行版本号改变---" + JSON.toJSONString(webMap));
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, String.valueOf(taskAndFlightById.get("taskOpeStaffId")), Constant.TASKFLIGHT,
                                                        "201", webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                        "10", webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


                                            }
                                                // 航班进出港等于进港的时候WS推送
                                                if (Constant.CLEAR_A_PORT.equals(newFlight.getFlgtAdid())) {
                                                    if (!org.springframework.util.StringUtils.isEmpty(userId)) {
                                                        if (taskAndFlightById != null && (!org.springframework.util.StringUtils.isEmpty(taskAndFlightById.get("flgtFlno")))) {
                                                   //         if (taskAndFlightById.get("flgtNum") != null && Integer.valueOf(String.valueOf(taskAndFlightById.get("flgtNum"))) > 0) {
                                                                if (taskAndFlightById != null) {
                                                                    taskAndFlightById.put("flrcType", 3);
                                                                    if (!org.springframework.util.StringUtils.isEmpty(String.valueOf(taskAndFlightById.get("taskOpeStaffId")))) {
                                                                        TStaff taskOpeStaffId = tStaffRepository.findByStaffId(String.valueOf(taskAndFlightById.get("taskOpeStaffId")));
                                                                        if (taskOpeStaffId != null) {
                                                                            taskAndFlightById.put("taskOpeStaffName", taskOpeStaffId.getStaffName());
                                                                        }
                                                                    }
                                                                }
                                                                // 把要推送的航班任务对象放进Map集合中
                                                                webMap.put("flight", taskAndFlightById);
                                                                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "18",
                                                                        webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                                System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---963L---" + JSON.toJSONString(webMap));
                                                                // 推送给加油员
                                                                // 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
                                                                if (1 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                        || 3 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                        || 4 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                        || 5 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))) {
                                                                    SendMsg2Redis.testDingYue(stringRedisTemplate, String.valueOf(taskAndFlightById.get("taskOpeStaffId")), Constant.TASKFLIGHT,
                                                                            Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                                    System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---975L---" + JSON.toJSONString(webMap));
                                                                }
                                                           // }
                                                        }
                                                    }
                                                }
                                                // 地图WS信息推送
                                                if (newFlight.getFlgtNum() != null && newFlight.getFlgtNum() > 0) {
                                                    webMap.put("flight", newFlight);
                                                    SendMsg2Redis.testDingYue(stringRedisTemplate, null, newFlight.getFlgtAirportCode() + ":" + newFlight.getFlgtAptareaCode(),
                                                            Constant.PC_FLIGHT_ADD, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                    System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---988L---" + JSON.toJSONString(webMap));
                                                }
                                         //   }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Transactional
    TFlight save(TFlight tFlight) {
        return tFlightRepository.saveAndFlush(tFlight);
    }

    /**
     * @Description: 根据任务ID查询任务单条记录和航班
     * @Param: [taskId]
     * @Author: XiuHongXin
     * @Date: 2019/2/20
     */
    private Map<String, Object> getTaskAndFlightById(String taskId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT\n" +
                    "\ttask.task_id AS taskId,\n" +
                    "\ttask.task_ope_staff_id AS taskOpeStaffId,\n" +
                    "\ttask.task_content AS taskContent,\n" +
                    "\ttask.task_status AS taskStatus,\n" +
                    "\ttask.task_asg_time AS taskAsgTime,\n" +
                    "\ttask.task_acc_time AS taskAccTime,\n" +
                    "\ttask.task_chag_sta_time AS taskChagStaTime,\n" +
                    "\ttask.task_chag_end_time AS taskChagEndTime,\n" +
                    "\ttask.task_done_time AS taskDoneTime,\n" +
                    "\ttask.task_fuel_recpt_no AS taskFuelRecptNo,\n" +
                    "\ttask.task_vehi_no AS taskVehiNo,\n" +
                    "\ttask.task_cre_staff_id AS taskCreStaffId,\n" +
                    "\ttask.task_starmark AS taskStarmark,\n" +
                    "\ttask.task_rec_cre_time AS taskRecCreTime,\n" +
                    "\tflight.flgt_id AS flgtId,\n" +
                    "\tflight.flgt_ffid AS flgtFfid,\n" +
                    "\tflight.flgt_airport_code AS flgtAirportCode,\n" +
                    "\tflight.flgt_aptarea_code AS flgtAptareaCode,\n" +
                    "\tflight.flgt_flno AS flgtFlno,\n" +
                    "\tflight.flgt_flop AS flgtFlop,\n" +
                    "\tflight.flgt_acname AS flgtAcname,\n" +
                    "\tflight.flgt_regn AS flgtRegn,\n" +
                    "\tflight.flgt_placecode AS flgtPlacecode,\n" +
                    "\tflight.flgt_al2c AS flgtAl2c,\n" +
                    "\tflight.flgt_alcname AS flgtAlcname,\n" +
                    "\tflight.flgt_d_stot AS flgtDStot,\n" +
                    "\tflight.flgt_d_etot AS flgtDEtot,\n" +
                    "\tflight.flgt_d_atot AS flgtDAtot,\n" +
                    "\tflight.flgt_org3c AS flgtOrg3c,\n" +
                    "\tflight.flgt_orgnm AS flgtOrgnm,\n" +
                    "\tflight.flgt_trs3c1 AS flgtTrs3c1,\n" +
                    "\tflight.flgt_trs3c2 AS flgtTrs3c2,\n" +
                    "\tflight.flgt_trs3c3 AS flgtTrs3c3,\n" +
                    "\tflight.flgt_trs3c4 AS flgtTrs3c4,\n" +
                    "\tflight.flgt_trs3c5 AS flgtTrs3c5,\n" +
                    "\tflight.flgt_trsnm1 AS flgtTrsnm1,\n" +
                    "\tflight.flgt_trsnm2 AS flgtTrsnm2,\n" +
                    "\tflight.flgt_trsnm3 AS flgtTrsnm3,\n" +
                    "\tflight.flgt_trsnm4 AS flightValic,\n" +
                    "\tflight.flgt_trsnm5 AS flgtVialc,\n" +
                    "\tflight.flgt_des3c AS flgtDes3c,\n" +
                    "\tflight.flgt_desnm AS flgtDesnm,\n" +
                    "\tflight.flgt_adid AS flgtAdid,\n" +
                    "\tflight.flgt_takeoff_fuel AS flgtTakeoffFuel,\n" +
                    "\tflight.flgt_chock_fuel AS flgtChockFuel,\n" +
                    "\tflight.flgt_otat_fuel AS flgtOtatFuel,\n" +
                    "\tflight.flgt_otat AS flgtOtat,\n" +
                    "\tflight.flgt_olvr AS flgtOlvr,\n" +
                    "\tflight.flgt_olvr_isupdate AS flgtOlvrIsupdate,\n" +
                    "\tflight.flgt_flti AS flgtFlti,\n" +
                    "\tfl.flgt_ftyp AS aFlgtFtyp,\n" +
                    "\tflight.flgt_ftyp AS flgtFtyp,\n" +
                    "\tflight.flgt_proxy AS flgtProxy,\n" +
                    "\tfl.flgt_flno AS flgtLinkFlno,\n" +
                    "\tflight.flgt_fnflag AS flgtFnflag,\n" +
                    "\tflight.flgt_game AS flgtGame,\n" +
                    "\tflight.flgt_chocks_in AS flgtChocksIn,\n" +
                    "\tflight.flgt_chocks_out AS flgtChocksOut,\n" +
                    "\tflight.flgt_vip AS flgtVip,\n" +
                    "\tflight.flgt_num AS flgtNum,\n" +
                    "\ttainscode.alcd_arln_name AS alcdArlnName,\n" +
                    "\ttainscode.alcd_arln_name_s AS alcdArlnNames,\n" +
                    "\ttaport.apcd_airport_name AS apcdSAirportName,\n" +
                    "\ttaport.apcd_airport_name_s AS apcdSAirportNames,\n" +
                    "\ttaport1.apcd_airport_name AS apcdEAirportName,\n" +
                    "\ttaport1.apcd_airport_name_s AS apcdEAirportNames,\n" +
                    "\tfl.flgt_org3c AS beorg3c,\n" +
                    "\tfl.flgt_a_stot AS flgtAStot,\n" +
                    "\tfl.flgt_a_etot AS flgtAEtot,\n" +
                    "\tfl.flgt_a_atot AS flgtAAtot,\n" +
                    "\tfl.flgt_placecode AS flgtPlacecodeIn\n" +
                    "FROM\n" +
                    "\tT_TASK AS task,\n" +
                    "\tT_FLIGHT AS flight\n" +
                    "LEFT JOIN T_AIRLINES_CODE tainscode ON tainscode.alcd_icao_code = flight.flgt_al2c\n" +
                    "LEFT JOIN T_AIRPORT_CODE taport ON taport.apcd_iata_code = flight.flgt_org3c\n" +
                    "LEFT JOIN T_AIRPORT_CODE taport1 ON taport1.apcd_iata_code = flight.flgt_des3c\n" +
                    "LEFT JOIN T_FLIGHT fl\n" +
                    "        ON fl.flgt_adid = 'A'\n" +
                    "        AND flight.flgt_link_ffid = fl.flgt_ffid\n" +
                    "WHERE\n" +
                    "\tflight.flgt_adid = \"D\" and\n" +
                    "\ttask.task_flight_id = flight.flgt_id\n");
            if (!org.springframework.util.StringUtils.isEmpty(taskId)) {
                sql.append("and task.task_id =\"" + taskId + "\"");
            }
            sql.append(
                    " order by flgtAAtot desc limit 1\n");

            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
