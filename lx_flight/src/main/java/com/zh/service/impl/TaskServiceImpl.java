package com.zh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.*;
import com.zh.component.RedisComponent;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.TCustomMapper;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.FlightService;
import com.zh.service.TFuelNoService;
import com.zh.service.TaskService;
import com.zh.util.DateUtil;
import com.zh.util.ModelAssistant;
import com.zh.util.SendMsg2Redis;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
public class TaskServiceImpl implements TaskService {

    private final static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    Prop prop;
    @Value("${task_change_log_topic}")
    private String taskTopic;
    @Autowired
    @Lazy
    private FlightService flightService;
    @Autowired
    private MyFuelRecptMapper fuelRecptMapper;
    @Autowired
    private FlightCodeTemporaryMapper flightCodeTemporaryMapper;
    @Autowired
    private TFuelNoService tFuelNoService;
    @Autowired
    private RedisComponent redis;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private MyStaffVehiTaskMapper vehiTaskMapper;
    @Autowired
    private MyFuelMapper myFuelMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${task_and_flight_lock}")
    private Boolean lock;
    @Autowired
    private TOrderInfoMapper tOrderInfoMapper;
    @Autowired
    private TCreditInfoMapper tCreditInfoMapper;
    @Autowired
    private MyTAirportCodeMapper myTAirportCodeMapper;
    @Autowired
    private TCustomMapper customMapper;
    @Autowired
    private TFlightTroubleMapper tFlightTroubleMapper;
    @Autowired
    private AirportCodeMapper airportCodeMapper;

    /**
     * 去重复
     *
     * @param list
     * @return
     */
    public static List<MyFlightTask> mySort(List<MyFlightTask> list, Boolean lock) {

        /*list.stream().peek(myFlightTask -> {
            if(StringUtils.isEmpty(myFlightTask.getFlgtFtyp()) ){
                myFlightTask.setFlgtFtyp(myFlightTask.getaFlgtFtyp());
            }else if("XX".equals(myFlightTask.getFlgtFtyp()) || "UK".equals(myFlightTask.getFlgtFtyp())){
                myFlightTask.setFlgtFtyp(myFlightTask.getaFlgtFtyp());
            }
        }).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                () -> new TreeSet<>
                                        (Comparator.comparing(MyFlightTask::getFlgtId))
                        ), ArrayList::new)
        );*/


        LinkedHashMap<String, MyFlightTask> tempMap = new LinkedHashMap<>();
        for (MyFlightTask flightTask : list) {
            String key = flightTask.getFlgtId();
            //containsKey(Object key) 该方法判断Map集合对象中是否包含指定的键名。如果Map集合中包含指定的键名，则返回true，否则返回false
            //containsValue(Object value)    value：要查询的Map集合的指定键值对象.如果Map集合中包含指定的键值，则返回true，否则返回false
            if (tempMap.containsKey(key)) {
                /*if (null != flightTask.getTaskRecCreTime() && null != tempMap.get(key).getTaskRecCreTime()) {
                    if (flightTask.getTaskRecCreTime().before(tempMap.get(key).getTaskRecCreTime())) {
                        tempMap.put(key, flightTask);
                    }
                }*/
                if (null != flightTask.getTaskAsgTime() && null != tempMap.get(key).getTaskAsgTime()) {
                    if (null != tempMap.get(key).getTaskAsgTime()) {
                        if (flightTask.getTaskAsgTime().after(tempMap.get(key).getTaskAsgTime())) {
                            if (8 != flightTask.getTaskStatus().intValue() && 9 != flightTask.getTaskStatus().intValue()) {
                                tempMap.put(key, flightTask);
                            }
                        }
                    } else {
                        tempMap.put(key, flightTask);
                    }
                } else if (null != flightTask.getTaskAsgTime() && null == tempMap.get(key).getTaskAsgTime()) {
                    tempMap.put(key, flightTask);
                }
//                if (flightTask.getFlgtAStot() != null && tempMap.get(key).getFlgtAStot() != null
//                        && flightTask.getFlgtAStot().after(tempMap.get(key).getFlgtAStot())) {
//                    // HashMap是不允许key重复的，所以如果有key重复的话，那么前面的value会被后面的value覆盖
//                    if (tempMap.get(key).getTaskStatus() < flightTask.getTaskStatus()) {
//                        tempMap.put(key, flightTask);
//                    }
//                }
            } else {
                tempMap.put(key, flightTask);
            }
        }
        List<MyFlightTask> tempList = new ArrayList<>();
        for (String key : tempMap.keySet()) {
            MyFlightTask myFlightTask = tempMap.get(key);
            //flight.flgt_a_atot AS dFlgtAAtot,
            if (null == myFlightTask.getFlgtAAtot() && null != myFlightTask.getdFlgtAAtot()) {
                myFlightTask.setFlgtAAtot(myFlightTask.getdFlgtAAtot());
            }
            if (!lock) {
                if (null == myFlightTask.getFlgtAStot()) {
                    myFlightTask.setFlgtAAtot(new Date());
                }
                if (StringUtils.isNotEmpty(myFlightTask.getaFlgtFtyp())) {
                    if ("DE".equals(myFlightTask.getaFlgtFtyp())
                            || "FX".equals(myFlightTask.getaFlgtFtyp())
                            || "CX".equals(myFlightTask.getaFlgtFtyp())
                    ) {
                        myFlightTask.setFlgtAAtot(new Date());
                    }
                }
            }
            //单出航班 自动赋值
            if (StringUtils.isEmpty(myFlightTask.getFlgtLinkFlno()) && null == myFlightTask.getFlgtAAtot()) {
                myFlightTask.setFlgtAAtot(new Date());
            }

           /* if (StringUtils.isEmpty(myFlightTask.getFlgtFtyp())) {
                myFlightTask.setFlgtFtyp(myFlightTask.getaFlgtFtyp());
            } else if ("XX".equals(myFlightTask.getFlgtFtyp()) || "UK".equals(myFlightTask.getFlgtFtyp())) {
                myFlightTask.setFlgtFtyp(myFlightTask.getaFlgtFtyp());
            }*/
            tempList.add(myFlightTask);
        }
        return tempList;
    }

    public static void main(String[] args) {
        int a = 1;
        System.out.println(a);
        int b = 2;
        System.out.println(b);
        System.out.println(ObjectUtil.contains("0000120134", "0000120134"));
    }

    /**
     * 航班任务转换
     *
     * @param list
     * @return
     */
    public static List<MyFlightTask> myenum(List<MyFlightTask> list) {
        /*List<MyFlightTask> tempList = new ArrayList<>();
        List<MyFlightTask> enumList = new ArrayList<>();
        //将航班任务空值转成 N/N
        for (MyFlightTask myFlightTask : list) {
            if (myFlightTask.getFlgtMissionProp() == null || "".equals(myFlightTask.getFlgtMissionProp()) || "null".equals(myFlightTask.getFlgtMissionProp())) {
                myFlightTask.setFlgtMissionProp("N/N");
            }
            if (myFlightTask.getFlgtMissionPropIn() == null || "".equals(myFlightTask.getFlgtMissionPropIn()) || "null".equals(myFlightTask.getFlgtMissionPropIn())) {
                myFlightTask.setFlgtMissionPropIn("N/N");
            }
            enumList.add(myFlightTask);
        }
        //将航班任务英文转成中文
        for (MyFlightTask tempMyFlightTask : enumList) {
            switch (tempMyFlightTask.getFlgtMissionProp()) {
                case "W/Z":
                    tempMyFlightTask.setFlgtMissionProp("正班");
                    break;
                case "Z/P":
                    tempMyFlightTask.setFlgtMissionProp("补班");
                    break;
                case "U/H":
                    tempMyFlightTask.setFlgtMissionProp("公务");
                    break;
                case "C/B":
                    tempMyFlightTask.setFlgtMissionProp("加班");
                    break;
                case "L/W":
                    tempMyFlightTask.setFlgtMissionProp("旅包");
                    break;
                case "B/W":
                    tempMyFlightTask.setFlgtMissionProp("专机");
                    break;
                case "A/N":
                    tempMyFlightTask.setFlgtMissionProp("备降");
                    break;
                case "A/A":
                    tempMyFlightTask.setFlgtMissionProp("邮政");
                    break;
                case "R/N":
                    tempMyFlightTask.setFlgtMissionProp("返航");
                    break;
                case "H/G":
                    tempMyFlightTask.setFlgtMissionProp("货包");
                    break;
                case "H/Z":
                    tempMyFlightTask.setFlgtMissionProp("货班");
                    break;
                case "O/F":
                    tempMyFlightTask.setFlgtMissionProp("急救");
                    break;
                case "N/M":
                    tempMyFlightTask.setFlgtMissionProp("调机");
                    break;
                case "M/X":
                    tempMyFlightTask.setFlgtMissionProp("鱼苗");
                    break;
                case "P/A":
                    tempMyFlightTask.setFlgtMissionProp("灭火");
                    break;
                case "ZZ":
                    tempMyFlightTask.setFlgtMissionProp("全部");
                    break;
                case "Q/C":
                    tempMyFlightTask.setFlgtMissionProp("农化");
                    break;
                case "Q/U":
                    tempMyFlightTask.setFlgtMissionProp("摩发");
                    break;
                case "R/W":
                    tempMyFlightTask.setFlgtMissionProp("日航");
                    break;
                case "R/Z":
                    tempMyFlightTask.setFlgtMissionProp("试航");
                    break;
                case "S/F":
                    tempMyFlightTask.setFlgtMissionProp("试飞");
                    break;
                case "S/Q":
                    tempMyFlightTask.setFlgtMissionProp("视察");
                    break;
                case "T/W":
                    tempMyFlightTask.setFlgtMissionProp("地航");
                    break;
                case "U/B":
                    tempMyFlightTask.setFlgtMissionProp("采矿");
                    break;
                case "N/N":
                    tempMyFlightTask.setFlgtMissionProp("未知");
                    break;
            }
            switch (tempMyFlightTask.getFlgtMissionPropIn()) {
                case "W/Z":
                    tempMyFlightTask.setFlgtMissionPropIn("正班");
                    break;
                case "Z/P":
                    tempMyFlightTask.setFlgtMissionPropIn("补班");
                    break;
                case "U/H":
                    tempMyFlightTask.setFlgtMissionPropIn("公务");
                    break;
                case "C/B":
                    tempMyFlightTask.setFlgtMissionPropIn("加班");
                    break;
                case "L/W":
                    tempMyFlightTask.setFlgtMissionPropIn("旅包");
                    break;
                case "B/W":
                    tempMyFlightTask.setFlgtMissionPropIn("专机");
                    break;
                case "A/N":
                    tempMyFlightTask.setFlgtMissionPropIn("备降");
                    break;
                case "A/A":
                    tempMyFlightTask.setFlgtMissionPropIn("邮政");
                    break;
                case "R/N":
                    tempMyFlightTask.setFlgtMissionPropIn("返航");
                    break;
                case "H/G":
                    tempMyFlightTask.setFlgtMissionPropIn("货包");
                    break;
                case "H/Z":
                    tempMyFlightTask.setFlgtMissionPropIn("货班");
                    break;
                case "O/F":
                    tempMyFlightTask.setFlgtMissionPropIn("急救");
                    break;
                case "N/M":
                    tempMyFlightTask.setFlgtMissionPropIn("调机");
                    break;
                case "M/X":
                    tempMyFlightTask.setFlgtMissionPropIn("鱼苗");
                    break;
                case "P/A":
                    tempMyFlightTask.setFlgtMissionPropIn("灭火");
                    break;
                case "ZZ":
                    tempMyFlightTask.setFlgtMissionPropIn("全部");
                    break;
                case "Q/C":
                    tempMyFlightTask.setFlgtMissionPropIn("农化");
                    break;
                case "Q/U":
                    tempMyFlightTask.setFlgtMissionPropIn("摩发");
                    break;
                case "R/W":
                    tempMyFlightTask.setFlgtMissionPropIn("日航");
                    break;
                case "R/Z":
                    tempMyFlightTask.setFlgtMissionPropIn("试航");
                    break;
                case "S/F":
                    tempMyFlightTask.setFlgtMissionPropIn("试飞");
                    break;
                case "S/Q":
                    tempMyFlightTask.setFlgtMissionPropIn("视察");
                    break;
                case "T/W":
                    tempMyFlightTask.setFlgtMissionPropIn("地航");
                    break;
                case "U/B":
                    tempMyFlightTask.setFlgtMissionPropIn("采矿");
                    break;
                case "N/N":
                    tempMyFlightTask.setFlgtMissionPropIn("未知");
                    break;
            }
            tempMyFlightTask.setFlgtMissionProp(tempMyFlightTask.getFlgtMissionProp() + "-" + tempMyFlightTask.getFlgtMissionPropIn());
            tempList.add(tempMyFlightTask);
        }
        return tempList;*/
        return list;
    }

    public void send(RefuelingTask refuelingtask) {
        kafkaTemplate.send("task", JSON.toJSONString(refuelingtask));
        log.debug("这是往kafka中推送消息，消息主题是task，消息内容是：" + JSON.toJSONString(refuelingtask));
    }

    public void sendsSecurity(Integer security) {
        // kafkaTemplate.send("carsecurity", "");
        //log.debug("这是往kafka中推送消息，carsecurity，消息内容是：" +security);
    }

    public void sends(String taskId, String staffId, String staffName, String vehiNo, String vehiPlateNo, String status, String airport, Long timeStamp) {
        kafkaTemplate.send("carstatus", "{\"vehiPlateNo\":\"" + vehiPlateNo + "\",\"staffName\":\"" + staffName + "\",\"staffId\":\"" + staffId + "\",\"taskId\":\"" + taskId + "\",\"vehiNo\":\"" + vehiNo + "\",\"status\":\"" + status + "\",\"airport\":\"" + airport + "\",\"timeStamp\":\"" + timeStamp + "\"}");
        log.debug("这是往kafka中推送消息，消息主题是carstatus，消息内容是：" + "{\"vehiPlateNo\":\"" + vehiPlateNo + "\",\"staffName\":\"" + staffName + "\",\"staffId\":\"" + staffId + "\",\"taskId\":\"" + taskId + "\",\"vehiNo\":\"" + vehiNo + "\",\"status\":\"" + status + "\",\"airport\":\"" + airport + "\",\"timeStamp\":\"" + timeStamp + "\"}");
    }

    public RefuelingTask getFlightInfo(String taskId) {
        MyTask taskInfo = taskMapper.getTaskInfo(taskId);
        MyFlightTask flightById = taskMapper.getFlightById(taskInfo.getTaskFlightId());
        RefuelingTask refuelingtask = new RefuelingTask();
        refuelingtask.setId(taskInfo.getTaskId());
        if (null != flightById) {
            refuelingtask.setBizkey(flightById.getFlgtFfid());
            SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
            String filghtNo = null;
            filghtNo = flightById.getFlgtAl2c() + flightById.getFlgtFlno() + flightById.getFlgtAdid() + sim.format(flightById.getFlgtFlop()) + flightById.getFlgtFlti();
            refuelingtask.setFilghtNo(filghtNo);
        }
        return refuelingtask;
    }

    /**
     * 获取任务保障量（总量，已加，未到，待加，加油中）
     */
    @Override
    public Map<String, Object> getTaskAmount(TStaff staff) {
        Date now = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim.format(beforeDate);
        if (nowStr.equals(beforeDatetr)) {
            nowStr = nowStr;
        } else {
            nowStr = beforeDatetr;
        }
        // 当无参数传值时，token内获取
        if (StringUtils.isBlank(staff.getStaffAirportCode())) {
            staff.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            staff.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        }
        //创建一个map集合用来装查出来的数量
        Map<String, Object> taskAmountMap = new HashMap<String, Object>();
        //以（所属机场，所属区域，记录创建时间＝系统日期）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_总量
        Integer taskSum = taskMapper.getTaskSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode(), nowStr);
        //以（所属机场，所属区域，记录创建时间＝系统日期，加油完成时间≠空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_已加
        Integer taskYetSum = taskMapper.getTaskYetSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以（所属机场，所属区域，记录创建时间＝系统日期，任务派发时间＝空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_未到
        Integer taskNotSum = taskMapper.getTaskNotSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以（所属机场，所属区域，记录创建时间＝系统日期，任务派发时间≠空，任务接受时间＝空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_待加
        Integer taskAwaittSum = taskMapper.getTaskAwaitSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以（所属机场，所属区域，记录创建时间＝系统日期，任务接受时间≠空，加油完成时间＝空,任务完成时间=空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_加油
        Integer taskRefuelSum = taskMapper.getTaskRefuelSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //保障量_总量
        taskAmountMap.put("taskSum", taskSum);
        //保障量_已加
        taskAmountMap.put("taskYetSum", taskYetSum);
        //保障量_未到
        taskAmountMap.put("taskNotSum", taskNotSum);
        //保障量_待加
        taskAmountMap.put("taskAwaittSum", taskAwaittSum);
        //保障量_加油
        taskAmountMap.put("taskRefuelSum", taskRefuelSum);
        return taskAmountMap;
    }

    /**
     * 获取任务保障量（总量，已加，未到，待加，加油中）
     */
    @Override
    public Map<String, Object> getTaskAmountByStatus(TStaff staff) {
        // 当无参数传值时，token内获取
        if (StringUtils.isBlank(staff.getStaffAirportCode())) {
            staff.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            staff.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        }
        //创建一个map集合用来装查出来的数量
        Map<String, Object> taskAmountMap = new HashMap<String, Object>();
        //任务总量 关联查询加油任务表和航显表,任务状态不是 2：申请待批（预留）  6：油单待审核（预留） 8：拒绝（预留）,9：取消加油 的数据
        Integer taskSum = taskMapper.getTaskSumByStatus(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //任务完成 关联查询加油任务表和航显表,任务状态是 7：任务完成 的数据
        Integer taskYetSum = taskMapper.getTaskYetSumByStatus(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //未到  关联查询加油任务表和航显表,任务状态是 0：未下发 的数据
        Integer taskNotSum = taskMapper.getTaskNotSumByStatus(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //待加 关联查询加油任务表和航显表,任务状态是1：待接受 的数据
        Integer taskAwaittSum = taskMapper.getTaskAwaitSumByStatus(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //加油中 关联查询加油任务表和航显表,任务状态是2 3：已接受，4：到位（预留），5：加油完成 的数据
        Integer taskRefuelSum = taskMapper.getTaskRefuelSumByStatus(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //保障量_总量
        taskAmountMap.put("taskSum", taskSum);
        //保障量_已加
        taskAmountMap.put("taskYetSum", taskYetSum);
        //保障量_未到
        taskAmountMap.put("taskNotSum", taskNotSum);
        //保障量_待加
        taskAmountMap.put("taskAwaittSum", taskAwaittSum);
        //保障量_加油
        taskAmountMap.put("taskRefuelSum", taskRefuelSum);
        return taskAmountMap;
    }

    /**
     * PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
     */
    @Override
    public List<MyFlightTask> getTaskAndFlight(MyFlightTask request, MyStaff staff) {
        //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        Date now = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim.format(beforeDate);
        if (nowStr.equals(beforeDatetr)) {
            nowStr = nowStr;
        } else {
            nowStr = beforeDatetr;
        }
        List<MyFlightTask> taskAndFlight = null;
        //比较一下用时
        Date starDate = new Date();
        //调度页面获取航显接口 true 为今天 4:00 - 明天 4:00  false 0-0
        if (lock) {
            taskAndFlight = taskMapper.getTaskAndFlight(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr, request.getFlgtId());
        } else {
            taskAndFlight = taskMapper.getTaskAndFlightLock(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr, request.getFlgtId());
        }
        long ys = new Date().getTime() - starDate.getTime();

        Date starDate1 = new Date();
        taskAndFlight = mySort(taskAndFlight, lock);
        if (!lock) {
            List<MyFlight> aFlight = flightService.getAFlight(staff.getLoginUserIn().getStaffAirportCode());
            if (null != aFlight && aFlight.size() > 0) {
                List<MyFlightTask> collect = aFlight.stream().map(o -> {
                    MyFlightTask myFlightTask = new MyFlightTask();
                    ModelAssistant.copyProperties(o, myFlightTask);
                    myFlightTask.setTaskId(myFlightTask.getFlgtId());
                    myFlightTask.setTaskContent(-1);
                    myFlightTask.setTaskStatus(0);
                    return myFlightTask;
                }).collect(Collectors.toList());
                if (collect.size() > 0) {
                    taskAndFlight.addAll(collect);
                }
            }
        }
        // taskAndFlight = myenum(taskAndFlight);
        long ys1 = new Date().getTime() - starDate1.getTime();
        //垮库查询获取调度员ID
        Date starDate2 = new Date();
        long ys2 = new Date().getTime() - starDate2.getTime();
        Date starDate3 = new Date();
        // taskAndFlight.forEach(this::setFlrcType);
        taskAndFlight.forEach(this::setTaskId);
        long ys3 = new Date().getTime() - starDate3.getTime();

        Date starDate4 = new Date();
        List<MyFlightAlarm> flightAlarm = flightMapper.getFlightAlarm(staff.getLoginUserIn().getStaffId(), null);
        for (MyFlightAlarm myFlightAlarm : flightAlarm) {
            for (MyFlightTask myFlightTask : taskAndFlight) {
                if (myFlightAlarm.getFlalId().equals(myFlightTask.getFlgtId())) {
                    myFlightTask.setFlgtAlarm(1);
                } else {
                    myFlightTask.setFlgtAlarm(0);
                }
            }
        }
        System.out.println("查询getTaskAndFlight用时:" + ys + "mm");
        System.out.println("查询去重复用时:" + Long.toString(ys1) + "mm");
        System.out.println("垮库查询获取调度员ID用时:" + Long.toString(ys2) + "mm");
        System.out.println("处理飞机号用时:" + Long.toString(ys3) + "mm");
        System.out.println("闹钟用时:" + Long.toString(new Date().getTime() - starDate4.getTime()) + "mm");
        System.out.println("共计时:" + Long.toString(new Date().getTime() - starDate.getTime()) + "mm");
        taskAndFlight.forEach(o -> {
            o.setFlrcType(flightService.pingSingleType(o));
        });

        taskAndFlight.forEach(flightInfoById -> {
            /*if (ObjectUtil.isNotEmpty(flightInfoById.getArcrCustomNum())) {
                String arcrCustomNum = flightInfoById.getArcrCustomNum();
                flightInfoById.setArcrCustomNum(arcrCustomNum);
                MyCustom cstmName = flightCodeTemporaryMapper.getCstmName(arcrCustomNum);
                if (ObjectUtil.isNotNull(cstmName) && StringUtils.isNotEmpty(cstmName.getCstmName())) {
                    flightInfoById.setFlgtAlcname(cstmName.getCstmName());//购买航空公司
                }
            } else {*/
            //加油客户编号
            Pair<String, String> pair = pingCountries(flightInfoById.getFlgtRegn(), flightInfoById.getFlgtFlno(), true);
            if (!"未知".equals(pair.getRight())) {
                if (StrUtil.isBlank(flightInfoById.getFlgtAcname()) && StrUtil.isNotBlank(pair.getLeft())) {
                    flightInfoById.setFlgtAcname(pair.getLeft());//类型
                }
                String right = pair.getRight();
                if (StringUtils.isNotEmpty(right)) {
                    String[] $s = right.split("&");
                    if ($s.length == 2) {
                        if (StringUtils.isNotEmpty($s[0])) {
                            flightInfoById.setFlgtAlcname($s[0]);//购买航空公司
                        }
                        flightInfoById.setArcrCustomNum($s[1]);
                    }
                }
            }
            setAirLines(flightInfoById);
            flightInfoById.setFlgtVialc(flightInfoById.getFlgtTrsnm5());
            flightInfoById.setFlightValic(flightInfoById.getFlgtTrsnm4());
        });

        return taskAndFlight;
    }

    //设置中文航线-中文航线中是字母的替换为中文
    @Override
    public void setAirLines(MyFlightTask flightTask) {
        String org3c = flightTask.getFlgtOrg3c();
        if (StrUtil.isNotBlank(org3c) &&
                (StrUtil.isBlank(flightTask.getFlgtOrgnm()) || flightTask.getFlgtOrgnm().matches("^[a-zA-Z]{3}$"))) {
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(org3c);
            flightTask.setFlgtOrgnm(Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(org3c));
        }
        String flgtTrs3c1 = flightTask.getFlgtTrs3c1();
        if (StrUtil.isNotBlank(flgtTrs3c1) &&
                (StrUtil.isBlank(flightTask.getFlgtTrsnm1()) || flightTask.getFlgtTrsnm1().matches("^[a-zA-Z]{3}$"))) {
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(flgtTrs3c1);
            flightTask.setFlgtTrsnm1(Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(flgtTrs3c1));
        }
        String flgtTrs3c2 = flightTask.getFlgtTrs3c2();
        if (StrUtil.isNotBlank(flgtTrs3c2) &&
                (StrUtil.isBlank(flightTask.getFlgtTrsnm2()) || flightTask.getFlgtTrsnm2().matches("^[a-zA-Z]{3}$"))) {
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(flgtTrs3c2);
            flightTask.setFlgtTrsnm2(Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(flgtTrs3c2));
        }
        String flgtTrsnm3 = flightTask.getFlgtTrsnm3();
        String[] flgtTrsnm3Arr = StrUtil.blankToDefault(flgtTrsnm3, StrUtil.EMPTY).split("-");
        flgtTrsnm3 = Arrays.stream(flgtTrsnm3Arr).map(item -> {
            boolean isLetter = item.matches("^[a-zA-Z]{3}$");
            if (!isLetter) {
                return item;
            }
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(item);
            return Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(item);
        }).collect(joining("-"));
        flightTask.setFlgtTrsnm3(flgtTrsnm3);
        String flgtTrsnm4 = StrUtil.blankToDefault(flightTask.getFlgtTrsnm4(), flightTask.getFlightValic());
        String[] flgtTrsnm4Arr = StrUtil.blankToDefault(flgtTrsnm4, StrUtil.EMPTY).split("-");
        flgtTrsnm4 = Arrays.stream(flgtTrsnm4Arr).map(item -> {
            boolean isLetter = item.matches("^[a-zA-Z]{3}$");
            if (!isLetter) {
                return item;
            }
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(item);
            return Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportNameS).orElse(item);
        }).collect(joining("-"));
        flightTask.setFlgtTrsnm4(flgtTrsnm4);
        String flgtTrsnm5 = StrUtil.blankToDefault(flightTask.getFlgtTrsnm5(), flightTask.getFlgtVialc());
        String[] flgtTrsnm5Arr = StrUtil.blankToDefault(flgtTrsnm5, StrUtil.EMPTY).split("-");
        flgtTrsnm5 = Arrays.stream(flgtTrsnm5Arr).map(item -> {
            boolean isLetter = item.matches("^[a-zA-Z]{3}$");
            if (!isLetter) {
                return item;
            }
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(item);
            return Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(item);
        }).collect(joining("-"));
        flightTask.setFlgtTrsnm5(flgtTrsnm5);
        String des3c = flightTask.getFlgtDes3c();
        if (StrUtil.isNotBlank(des3c) &&
                (StrUtil.isBlank(flightTask.getFlgtDesnm()) || flightTask.getFlgtDesnm().matches("^[a-zA-Z]{3}$"))) {
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(des3c);
            flightTask.setFlgtDesnm(Optional.ofNullable(myAirportCode).map(MyAirportCode::getApcdAirportName).orElse(des3c));
        }
    }

    /**
     * 功能描述：根据飞机号和航班号获取当前飞机的航班和任务信息
     *
     * @param airportCode 所属机场代码
     * @param aptareaCode 所属机场区域代码
     * @param date        日期
     * @param flgtRegn    飞机号
     * @param flgtFlno    航班号
     * @return com.zh.bean.flight.MyFlightTask
     * @author zhaojiacan
     * @date 2024/4/12
     */
    @Override
    public MyFlightTask getFlightByRegnAndFlno(String airportCode, String aptareaCode, String date, String flgtRegn, String flgtFlno) {
        MyFlightTask flight = taskMapper.getFlightByRegnAndFlno(null, null, date, flgtRegn, flgtFlno);
        if (flight == null) {
            return null;
        }
        flight.setFlrcType(flightService.pingSingleType(flight));
        Pair<String, String> pair = pingCountries(flight.getFlgtRegn(), flight.getFlgtFlno(), true);
        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(flight.getFlgtAcname()) && StrUtil.isNotBlank(pair.getLeft())) {
                flight.setFlgtAcname(pair.getLeft());//类型
            }
            String right = pair.getRight();
            if (StringUtils.isNotEmpty(right)) {
                String[] $s = right.split("&");
                if ($s.length == 2) {
                    if (StringUtils.isNotEmpty($s[0])) {
                        flight.setFlgtAlcname($s[0]);//购买航空公司
                    }
                    flight.setArcrCustomNum($s[1]);
                }
            }
        }
        return flight;
    }

    private void setTaskId(MyFlightTask myFlightTask) {
        if (StringUtils.isNotEmpty(myFlightTask.getFlgtId())) {
            MyTask taskInfoById = flightMapper.getTaskInfoById(myFlightTask.getFlgtId());
            if (null == taskInfoById) {
                if (StringUtils.isEmpty(myFlightTask.getTaskId())) {
                    String id = UUID.randomUUID().toString();
                    myFlightTask.setTaskId(id);
                    MyTask task = new MyTask();
                    //生成UUID为任务ID
                    //把生成的UUID赋值到任务对象中的任务ID里
                    task.setTaskId(id);
                    //把航班对象中的航班ID赋值到任务对象中的航班ID里
                    task.setTaskFlightId(myFlightTask.getFlgtId());
                    //把航班对象中的航班号赋值到任务对象中的航班号里
                    task.setTaskFlightNo(myFlightTask.getFlgtFlno());
                    //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                    task.setTaskAirportCode(myFlightTask.getFlgtAirportCode());
                    //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                    task.setTaskAptareaCode(myFlightTask.getFlgtAptareaCode());
                    //创建人ID赋值到任务对象中的创建人ID里
                    task.setTaskCreStaffId("9999");
                    //如果任务内容等于空的话赋默认值0
                    task.setTaskContent(-1);
                    //如果任务状态等于空的话赋默认值0
                    task.setTaskStatus(0);
                    //如果任务星标等于空的话赋默认值0
                    task.setTaskStarmark(0);
                    //获取当前系统时间
                    task.setTaskRecCreTime(new Date());
                    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                    if (flightMapper.addTask(task) != 1) {
                        log.error("getTaskAndFlight 创建任务失败了");
                    }
                }
            }
        }
    }

    /**
     * 设置油单类型
     *
     * @param myFlightTask
     */
    private void setFlrcType(MyFlightTask myFlightTask) {
        setTaskId(myFlightTask);
        if (StringUtils.isNotEmpty(myFlightTask.getTaskId())) {
            String cstmRegion = myFlightTask.getCstmRegion();
            String flgtFlno = myFlightTask.getFlgtFlno();
            String flgtLinkFlno = myFlightTask.getFlgtLinkFlno();
            String flgtFltiIn = myFlightTask.getFlgtFltiIn();
            Integer taskContent = myFlightTask.getTaskContent();
            String flgtFlti = myFlightTask.getFlgtFlti();
            if (null != taskContent) {
                if (StringUtils.contains(cstmRegion, "CN")) {
                    //判断是否是经停航班
                    if (StringUtils.isNotEmpty(flgtFlno) && StringUtils.isNotEmpty(flgtLinkFlno)) {
                        // 说明经停
                        if (flgtFlno.equals(flgtLinkFlno)) {
                            if (StringUtils.isNotEmpty(flgtFltiIn)) {
                                //说明国际航班
                                if (!"D".equals(flgtFltiIn)) {
                                    if (taskContent == 1) {
                                        // 5 内航 离境 抽油
                                        myFlightTask.setFlrcType(5);
                                    } else if (taskContent == 0) {
                                        // 2 内航 离境 加油
                                        myFlightTask.setFlrcType(2);
                                    } else {
                                        // 内航 离境 补加油
                                        myFlightTask.setFlrcType(8);
                                    }
                                }
                            }
                        }
                    }
                    // 判断国内国外 0：加油，1：抽油 2补加油
                    if ("D".equals(flgtFlti)) {
                        if (taskContent == 1) {
                            // 6 内航国内抽油
                            myFlightTask.setFlrcType(6);
                        } else if (taskContent == 0) {
                            // 3 内航国内加油
                            myFlightTask.setFlrcType(3);
                        } else {
                            // 7 内航国内补加油
                            myFlightTask.setFlrcType(7);
                        }
                    } else {
                        if (taskContent == 1) {
                            // 5 内航 离境 抽油
                            myFlightTask.setFlrcType(1);
                        } else if (taskContent == 0) {
                            // 2 内航 离境 加油
                            myFlightTask.setFlrcType(2);
                        } else {
                            // 内航 离境 补加油
                            myFlightTask.setFlrcType(8);
                        }
                    }
                    //判断任务类型
                } else {
                    //判断任务类型
                    if (taskContent == 1) {
                        // 4   外行  抽油
                        myFlightTask.setFlrcType(4);
                    } else if (taskContent == 0) {
                        //  1 外行   加油
                        myFlightTask.setFlrcType(1);
                    } else {
                        // 9 外航 补油
                        myFlightTask.setFlrcType(9);
                    }
                }
            }
        }
    }

    /**
     * 任务下发,根据前台传的值更新任务信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public MyFlightTask updateTaskInfo(MyFlightTask task, MyStaff staff, boolean b) {
        MyFlightTask flightInfoById = flightMapper.getFlightInfoById(task.getFlgtId());
        if (flightInfoById == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("该航班在系统内不存在", null));
        }
        Boolean publock = true;
        if (null != task.getFlgtStatus() && 1 == task.getFlgtStatus()) {
            MyTask updateTask = new MyTask();
            updateTask.setTaskId(task.getTaskId());
            updateTask.setTaskStatus(2);
            updateTask.setTaskAccTime(null);
            updateTask.setTaskChagStaTime(null);
            updateTask.setTaskArriveTime(null);
            updateTask.setTaskChagEndTime(null);
            updateTask.setTaskVehiNo(null);
            updateTask.setTaskRcPrintTime(null);
            int i1 = taskMapper.updateTask(updateTask);
            MyFlight myFlight = new MyFlight();
            myFlight.setFlgtId(task.getFlgtId());
            myFlight.setFlgtStatus(2);
            //取消挂起
            int i = flightMapper.updateFlight(myFlight);
            publock = false;
        }
        String flgtRegn = flightInfoById.getFlgtRegn();
        String flgtFlno = flightInfoById.getFlgtFlno();
        String arcrCustomNum = task.getArcrCustomNum();
        if (b) {
            if (StrUtil.isBlank(arcrCustomNum)) {
                throw new CustomException(ReturnMsg.getInstanceNGz("参数错误，请提交加油客户编号", null));
            }
            String flgtFlnoMain = flgtFlno;
            if (StrUtil.isNotBlank(flgtFlnoMain)) {
                String[] split = flgtFlnoMain.split("/");
                if (split.length > 0) {
                    flgtFlnoMain = split[0];
                }
            }
            // 校验飞机号码信息
            MyFlightCode flightCodeInfoByRegnAndFlno = getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtFlnoMain);
            if (flightCodeInfoByRegnAndFlno == null) {
                throw new CustomException(ReturnMsg.getInstanceNGz("该航班没有匹配到加油客户信息，请维护后提交", null));
            }
            if (!flightCodeInfoByRegnAndFlno.getArcrCustomNum().equals(arcrCustomNum)) {
                String errinfo = String.format("填写的客户编号%s与系统匹配到的客户编号%s不一致，请校正后提交", arcrCustomNum, flightCodeInfoByRegnAndFlno.getArcrCustomNum());
                throw new CustomException(ReturnMsg.getInstanceNGz(errinfo, null));
            }
        }
        // pad 端逻辑 大离线版本的问题
//        if(!b){
//            MyTask isGet = taskMapper.getTaskInfo(task.getTaskId());
//            if(ObjectUtil.isNotNull(isGet) && isGet.getTaskStatus() != 0){
//                return new MyFlightTask();
//            }
//        }

        Boolean lockCommon = false;
        /*
         `flgt_takeoff_fuel` int(7) DEFAULT NULL COMMENT '起飞油量',
        ` flgt_chock_fuel` int(7) DEFAULT NULL COMMENT '轮挡油量',
          `flgt_otat_fuel` int(7) DEFAULT NULL COMMENT '预加油量（或起
         */
        if (null != task.getFlgtRegnChangeStatus() && 2 == task.getFlgtRegnChangeStatus()) {
            MyFlight myFlightRegn = new MyFlight();
            myFlightRegn.setFlgtId(task.getFlgtId());
            myFlightRegn.setFlgtRegnChangeStatus(3);
            flightMapper.updateFlight(myFlightRegn);
            task.setFlgtRegnChangeStatus(3);
        }
        MyTask mytask = new MyTask();
        if (b) {
            log.info("pc 派发任务修改 预计加油量等信息 ——》" + JSON.toJSONString(task));
            MyFlight myFlight = new MyFlight();
            myFlight.setFlgtId(task.getFlgtId());
            myFlight.setFlgtTakeoffFuel(task.getFlgtTakeoffFuel());
            myFlight.setFlgtChockFuel(task.getFlgtChockFuel());
            myFlight.setFlgtOtatFuel(task.getFlgtOtatFuel());
            TAirportCode airportCode = myTAirportCodeMapper.selectByCnafAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            TOrderInfo tOrderInfo = new TOrderInfo();
            tOrderInfo.setApc3(airportCode.getApcdIataCode());
            tOrderInfo.setFlno(flightInfoById.getFlgtFlno());
            tOrderInfo.setRegn(flightInfoById.getFlgtRegn());
            List<TOrderInfo> tOrderInfos = tOrderInfoMapper.selectOrderInfoByRegnAndNo(tOrderInfo);
            if (tOrderInfos.size() > 0) {
                myFlight.setFlgtIsOrder(1);
                myFlight.setOrderNo(tOrderInfos.get(0).getOrderNo());
                kafkaTemplate.send("order_change", JSON.toJSONString(tOrderInfos));
                if (StringUtils.isNotEmpty(tOrderInfos.get(0).getCstno())) {
                    log.info("任务派发时, 赋值订单客户编号     " + JSON.toJSONString(task) + " cstno: " + tOrderInfos.get(0).getCstno());
                    mytask.setTaskFlightId(task.getFlgtId());
                    //mytask.setCustomNum(tOrderInfos.get(0).getCstno());
                    taskMapper.updateTaskCstno(mytask);
                }
            }
            flightMapper.updateFlightFuel(myFlight);

            if (task.getTaskContent() == 0) {
                MyTask byflgtId = taskMapper.findByflgtId(task.getFlgtId());
                if (byflgtId != null) {
                    throw new CustomException(ReturnMsg.getInstanceNGz("该航班已经申请过加油任务,请申请补加油", null));
                }
            }
        }
        Integer flrcTypes = null;
        Integer publicLock = null;
        if (task.getTaskContent() == null) {
            task.setTaskContent(0);
            publicLock = 0;
        } else {
            publicLock = task.getTaskContent();
        }
        MyTask publicMyTask = null;
        MyFlightTask myFlightTask = null;

        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //航班号裁切 2019年8月12日17:01:38
        if (null != flightInfoById && StringUtils.isNotEmpty(flightInfoById.getFlgtFlno())) {
            if (StringUtils.isEmpty(flightInfoById.getFlgtFtyp())) {
                flightInfoById.setFlgtFtyp(flightInfoById.getaFlgtFtyp());
            } else if ("XX".equals(flightInfoById.getFlgtFtyp()) || "UK".equals(flightInfoById.getFlgtFtyp())) {
                flightInfoById.setFlgtFtyp(flightInfoById.getaFlgtFtyp());
            }
            String[] split = flgtFlno.split("/");
            if (split.length > 0) {
                flgtFlno = split[0];
                flightInfoById.setFlgtFlno(flgtFlno);
            }
            /*Pair<String, String> pair = pingCountries(flightInfoById.getFlgtRegn(), flightInfoById.getFlgtFlno());
                flightInfoById.setFlgtAcname(pair.getLeft());//类型
                flightInfoById.setFlgtAlcname(pair.getRight());//购买航空公司*/

            //加油客户编号

            if (ObjectUtil.isEmpty(task.getArcrCustomNum()) && ObjectUtil.isEmpty(task.getFlgtRegn())) {
                MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
                if (ObjectUtil.isNotNull(taskInfo)) {
                    task.setArcrCustomNum(taskInfo.getCustomNum());
                    task.setFlgtRegn(taskInfo.getFlgtRegn());
                    task.setFlgtFlno(taskInfo.getTaskFlightNo());
                }
            }

            Pair<String, String> pair = pingCountries(flightInfoById.getFlgtRegn(), flightInfoById.getFlgtFlno(), true);

            if (ObjectUtil.isEmpty(task.getArcrCustomNum())) {
                if (!"未知".equals(pair.getRight())) {
                    String right = pair.getRight();
                    if (StringUtils.isNotEmpty(right)) {
                        String[] $s = right.split("&");
                        if ($s.length == 2) {
                            task.setArcrCustomNum($s[1]);
                        }
                    }
                }
            }

            if (ObjectUtil.isNotEmpty(task.getArcrCustomNum())) {
                MyCustom cstmName = flightCodeTemporaryMapper.getCstmName(task.getArcrCustomNum());
                if (ObjectUtil.isNotNull(cstmName)) {
                    flightInfoById.setFlgtAlcname(cstmName.getCstmName());//购买航空公司
                }
            }

            if (!"未知".equals(pair.getRight())) {
                if (StrUtil.isBlank(flightInfoById.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                    flightInfoById.setFlgtAcname(pair.getLeft());//类型
                }
            }
            flightInfoById.setArcrCustomNum(task.getArcrCustomNum());

            TCreditInfo tCreditInfo = tCreditInfoMapper.selectByPrimaryKey(flightInfoById.getArcrCustomNum());
            if (null != tCreditInfo && StringUtils.isNotEmpty(tCreditInfo.getCitst())) {
                Integer citst = Integer.valueOf(tCreditInfo.getCitst());
                // 发送ws
                if (0 < citst) {
                    lockCommon = true;
                    webMap.put("tCreditInfo", tCreditInfo);
                }
            }

        }
        MyTask newTask = new MyTask();
        newTask.setFuelPubType(task.getFuelPubType());
        Boolean lock = false;
        Boolean hasTask = true;
        if (!StringUtils.isBlank(task.getTaskId())) {
            MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
            //TODO 进港航班派发加油任务 逻辑修改
            if (null == taskInfo && publock) {
                lock = true;
                hasTask = false;
            }
            if (null != taskInfo && !StringUtils.isEmpty(taskInfo.getTaskOpeStaffId()) && publock) {
                lock = true;
            }
            if (null != taskInfo && null != taskInfo.getTaskAccTime() && publock) {
                lock = true;
            }
            if (null != taskInfo && taskInfo.getTaskStatus() < 2 && publock && !b) {
                lock = false;
            }
        } else {
            lock = true;
        }

        if (!b) {
            hasTask = true;
        }

        //判断当前任务id是否为空,为空就去创建任务并下发
        if (lock) {
            //  if (StringUtils.isBlank(task.getTaskId()) || task.getTaskContent() > 0) {
            //生成UUID为任务ID
            String taskId = "";
            if (ObjectUtil.isNotEmpty(task.getTaskId()) && !b && hasTask) {
                taskId = task.getTaskId();
            } else {
                taskId = UUID.randomUUID().toString();
            }

            //把生成的UUID赋值到任务对象中的任务ID里
            newTask.setTaskId(taskId);
            //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
            newTask.setTaskAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
            newTask.setTaskAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
            //创建人ID赋值到任务对象中的创建人ID里
            newTask.setTaskCreStaffId(staff.getLoginUserIn().getStaffId());
            //加油人ID赋值到任务对象中的加油人ID里
            newTask.setTaskOpeStaffId(task.getTaskOpeStaffId());
            //航班ID赋值到任务对象中的航班ID里
            newTask.setTaskFlightId(flightInfoById.getFlgtId());
            //加油人ID赋值到任务对象中的加油人ID里
            newTask.setTaskFlightNo(flightInfoById.getFlgtFlno());
            //将任务状态赋值到任务对象中的任务状态里
            newTask.setTaskStatus(1);
            //TODO 获取前台输入的保税类型
            newTask.setFlrcBwtar(task.getFlrcBwtar());
            //PAD端传输过来的油单类型，需要同步更新到DB,有值才更新，没值默认为空
            newTask.setFlrcType(task.getFlrcType());
            //获取当前系统时间
            Date date = new Date();

            if (ObjectUtil.isNotEmpty(task.getTaskAsgTime())) {
                newTask.setTaskAsgTime(task.getTaskAsgTime());
            } else {
                //因为任务对象中的任务下发时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的任务下发时间中去
                newTask.setTaskAsgTime(date);
            }
            //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
            newTask.setTaskRecCreTime(new Date());

//            //获取当前系统时间
//            Date date = new Date();
//            //创建SimpleDateFormat日期格式化对象
//            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            //接收格式化以后的时间
//            String forMatTime = sim.format(date);
//            try {
//                //因为任务对象中的任务下发时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的任务下发时间中去
//                newTask.setTaskAsgTime(sim.parse(forMatTime));
//                //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
//                newTask.setTaskRecCreTime(sim.parse(forMatTime));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            MyStaffVehi staffVehs = staffMapper.getStaffVehiInfo(flightInfoById.getTaskVehiNo());
            //判断查出来的人员车辆信息是否为空
            if (staffVehs != null) {
                //往任务对象中赋值加油车编号
                newTask.setTaskVehiNo(staffVehs.getSfvhVehiNo());
            }
            //如果任务内容等于空的话赋默认值0
            if (task.getTaskContent() == null) {
                newTask.setTaskContent(0);
            } else {
                //把任务内容赋值进任务对象的任务内容中
                newTask.setTaskContent(task.getTaskContent());
            }
            //如果任务星标等于空的话赋默认值0
            if (task.getTaskStarmark() == null) {
                newTask.setTaskStarmark(0);
            } else {
                newTask.setTaskStarmark(task.getTaskStarmark());
            }
            //添加加油客户信息
            if (StrUtil.isNotBlank(mytask.getCustomNum())) {
                newTask.setCustomNum(mytask.getCustomNum());
            }

            //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addTaskNew(newTask) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            } else {
                //根据任务ID查询任务单条信息
                MyTask taskInfo = taskMapper.getTaskInfo(newTask.getTaskId());
                publicMyTask = taskInfo;
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(newTask.getTaskId());
                ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
                myFlightTasks.add(taskAndFlightById);
                List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
                if (myenum.size() > 0) {
                    taskAndFlightById = myenum.get(0);
                }
                if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                }
                myFlightTask = taskAndFlightById;
                MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
                taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
                Integer flrcType = null;
                flrcType = task.getFlrcType() == null ? flightService.pingSingleType(taskAndFlightById) : task.getFlrcType();
                flrcTypes = flrcType;
                //把判断好的油单类型放进推送的任务对象中
                taskInfo.setFlrcType(flrcType);
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfo);
                //把判断好的油单类型放进推送的任务对象中
                taskAndFlightById.setFlrcType(flrcType);
                taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
                //把要推送的航班任务对象放进Map集合中
                webMap.put("flight", taskAndFlightById);
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                try {
                    RefuelingTask flightInfo = getFlightInfo(newTask.getTaskId());
                    flightInfo.setPulldownTime(date);
                    send(flightInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //获取当前系统时间
            Date date = new Date();

            if (ObjectUtil.isNotEmpty(task.getTaskAsgTime())) {
                newTask.setTaskAsgTime(task.getTaskAsgTime());
            } else {
                //因为任务对象中的任务下发时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的任务下发时间中去
                newTask.setTaskAsgTime(date);
            }
            //把传来的任务ID赋值到任务对象中的任务ID里
            newTask.setTaskId(task.getTaskId());
            //创建人ID赋值到任务对象中的创建人ID里
            newTask.setTaskCreStaffId(staff.getLoginUserIn().getStaffId());
            //加油人ID赋值到任务对象中的加油人ID里
            newTask.setTaskOpeStaffId(task.getTaskOpeStaffId());
            //将任务内容赋值到任务对象中的任务内容里
            newTask.setTaskContent(task.getTaskContent());
            newTask.setTaskStatus(1);
            //TODO 获取前台输入的保税类型
            newTask.setFlrcBwtar(task.getFlrcBwtar());
            //PAD端传输过来的油单类型，需要同步更新到DB,有值才更新，没值默认为空
            newTask.setFlrcType(task.getFlrcType());
            //添加加油客户信息
            if (StrUtil.isNotBlank(mytask.getCustomNum())) {
                newTask.setCustomNum(mytask.getCustomNum());
            }
            //更新任务信息,判断如果返回等于1说明更新成功，否则更新失败
            if (taskMapper.updateTaskInfoNew(newTask) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                //根据任务ID查询任务单条信息
                MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
                publicMyTask = taskInfo;
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
                ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
                myFlightTasks.add(taskAndFlightById);
                List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
                if (myenum.size() > 0) {
                    taskAndFlightById = myenum.get(0);
                }
                if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                }
                myFlightTask = taskAndFlightById;
                MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
                taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
                Integer flrcType = null;
                flrcType = task.getFlrcType() == null ? flightService.pingSingleType(taskAndFlightById) : task.getFlrcType();
                //把判断好的油单类型放进推送的任务对象中
                flrcTypes = flrcType;
                taskInfo.setFlrcType(flrcType);
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfo);
                //把判断好的油单类型放进推送的任务对象中
                taskAndFlightById.setFlrcType(flrcType);
                taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
                //把要推送的航班任务对象放进Map集合中
                webMap.put("flight", taskAndFlightById);
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                try {
                    RefuelingTask flightInfo = getFlightInfo(newTask.getTaskId());
                    flightInfo.setPulldownTime(date);
                    send(flightInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //更新航班信息,判断如果返回等于1说明更新成功，否则更新失败
        if (flightMapper.updateFlightInfoOne(task.getFlgtId(), 1, flightInfoById.getFlgtAlcname()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        // 更新任务客户编码
        //MyTask taskCustom = (MyTask) webMap.get("task");
        //if (ObjectUtil.isNotNull(taskCustom)) {
        //    taskCustom.setCustomNum(task.getArcrCustomNum());
        //    if (ObjectUtil.isNotNull(task.getArcrCustomNum())) {
        //        taskMapper.updateTaskCust(taskCustom);
        //    }
        //    webMap.put("task", taskCustom);
        //}
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        MyFlightTask flight = (MyFlightTask) webMap.get("flight");
        flight.setFlgtOldRegn(task.getFlgtOldRegn());
        flight.setFlgtRegnChangeStatus(task.getFlgtRegnChangeStatus());
        if (null != flight) {
            MyFlightTask myFlightTask1 = pingCstmRegion(flight);
            if (null != myFlightTask1) {
                myFlightTask1.setFlgtAlcname(flightInfoById.getFlgtAlcname());
                webMap.put("flight", myFlightTask1);
            }
        }
        //TODO 订单任务
        if (null != webMap.get("flight") && StringUtils.isNotEmpty(((MyFlightTask) webMap.get("flight")).getOrderNo())) {
            TOrderInfo orderInfo = tOrderInfoMapper.selectByOrderNo(((MyFlightTask) webMap.get("flight")).getOrderNo());
            webMap.put("orderInfo", orderInfo);
        }
        sendToKafkaTask(publicMyTask, myFlightTask);
        if (publicLock > 0) {
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_TASK, webMap);
        } else {
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_TASK_ISSUE, webMap);
        }
        if (lockCommon) {
            //webmap 中增加  tCreditInfo 字段  type 51
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.T_CREDIT_INFO, webMap);
        }
        // 对加油员进行推送
        webMap.remove("task");
        webMap.remove("selfFlight");
        SendMsg2Redis.testDingYue(stringRedisTemplate, task.getTaskOpeStaffId(), Constant.TASKFLIGHT, Constant.PAD_TASK_ISSUE, webMap);
        myFlightTask.setTaskStatus(newTask.getTaskStatus());
        myFlightTask.setTaskContent(newTask.getTaskContent());
        myFlightTask.setFlrcType(flrcTypes);
        myFlightTask.setFlrcBwtar(task.getFlrcBwtar());
        return myFlightTask;
    }

    /**
     * 任务取消,清空任务表的加油员ID和创建人员ID并且状态改为0
     *
     * @return
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Map<String, Object> updateTaskCancel(MyTask task) {
        String pubOldRegn = "";
        Integer pubChangeOldStatud = 1;
        MyTask publicMyTask = null;
        MyFlightTask myFlightTask = null;
        MyFlightTask pubUpDate = null;
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //创建map集合用来装返回信息
        Map<String, Object> map = new HashMap<>();
        //根据任务ID查询任务单条信息
        MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
        publicMyTask = taskInfo;
        Boolean publock = true;
        MyFlightTask flightInfoById = flightMapper.getFlightInfoById(taskInfo.getTaskFlightId());

//        if (StringUtils.isNotEmpty(flightInfoById.getFlgtOil()) && 0 < Integer.valueOf(flightInfoById.getFlgtOil())) {
//            map.put("key", false);
//            map.put("val", "任务存在预计加油量不可取消");
//            return map;
//        }

        if (null != task.getFlgtStatus() && 0 != task.getFlgtStatus()) {
            MyFlight myFlight = new MyFlight();
            myFlight.setFlgtId(taskInfo.getTaskFlightId());
            myFlight.setFlgtStatus(0);
            myFlight.setFlgtHangTask("1");
            //取消挂起
            int i = flightMapper.updateFlight(myFlight);
        }

        if (null != flightInfoById.getFlgtRegnChangeStatus() && 3 == flightInfoById.getFlgtRegnChangeStatus()) {
            pubChangeOldStatud = 2;
            pubOldRegn = flightInfoById.getFlgtOldRegn();
            MyFlight myFlight = new MyFlight();
            myFlight.setFlgtId(flightInfoById.getFlgtId());
            myFlight.setFlgtRegnChangeStatus(2);
            int i = flightMapper.updateFlight(myFlight);
            if (1 != i) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        //用来接收车牌号
        String vehiPlateNo = "";
        //用来接收车辆编号
        String vehiNo = "";
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(taskInfo.getTaskAirportCode());
        staffInfo.setStaffAptareaCode(taskInfo.getTaskAptareaCode());
        MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
        //判断任务状态如果等于0说明任务已经取消或者还未下发taskInfo.getTaskStatus()
        if (taskInfo.getTaskStatus() == 0) {
            map.put("key", false);
            map.put("val", "任务已经取消或未下发");
            return map;
        }
        // 判断任务状态如果大于等于5说明任务已经完成
        if (taskInfo.getTaskStatus() >= 5) {
            map.put("key", false);
            map.put("val", "油单已打印，无法取消");
            return map;
        }
        //判断任务状态如果大于0小于5说明任务正在进行，可以取消
        if (taskInfo.getTaskStatus() > 0 && taskInfo.getTaskStatus() < 7) {
            Boolean lock = true;
            List<MyFlightTask> taskAndFlightByFlightId = taskMapper.getTaskAndFlightByFlightId(taskInfo.getTaskFlightId());
            if (taskAndFlightByFlightId.size() > 0) {

                if (2 <= taskAndFlightByFlightId.size()) {
                    pubUpDate = taskAndFlightByFlightId.get(1);
                }

                if (taskInfo.getTaskId().equals(taskAndFlightByFlightId.get(0).getTaskId())) {
                    //任务取消,清空任务表的加油员ID和创建人员ID并且状态改为0
                    lock = taskMapper.updateTaskCancel(taskInfo.getTaskId(), 0) != 1;
                } else {
                    //TODO
                    lock = taskMapper.updateTaskCancel(taskInfo.getTaskId(), 0) != 1;
                }
            } else {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            if (lock) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfo);
                MyFlightTask taskAndFlightById = null;
                if (null != pubUpDate) {
                    taskAndFlightById = taskMapper.getTaskAndFlightById(pubUpDate.getTaskId());
                } else {
                    taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfo.getTaskId());
                }
                //根据任务ID查出单条任务航班信息

                ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
                myFlightTasks.add(taskAndFlightById);
                List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
                if (myenum.size() > 0) {
                    taskAndFlightById = myenum.get(0);
                }
                if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                }
                myFlightTask = taskAndFlightById;
                taskAndFlightById.setFlgtOldRegn(pubOldRegn);
                taskAndFlightById.setFlgtRegnChangeStatus(pubChangeOldStatud);
                taskAndFlightById.setFlgtStatus(0);
                //把要推送的航班任务对象放进Map集合中  taskAndFlightById
                taskAndFlightById.setRecallTaskId(task.getTaskId());
                webMap.put("flight", taskAndFlightById);
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把查出来的人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffinfo.getStaffId());
                // 把人员id赋值到任务变更中，工作时长时需要使用
                myFlightTask.setTaskOpeStaffId(staffinfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffinfo.getStaffPhone());
                staffTask.setStaffName(staffinfo.getStaffName());
                //根据人员ID查询出单条任务信息
                MyTask taskInfos = fuelRecptMapper.gettaskById(staffinfo.getStaffId());
                //根据加油员ID获取人员车辆表的信息
                MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());
                //判断如果人员车辆信息是否为空
                if (staffVehis != null) {
                    //不为空的话把车辆编号赋值进人员车辆任务对象中
                    staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                }
                //根据加油车编号查出对应的车辆信息
                MyVehi vehi = vehiTaskMapper.getVehiInfo(staffTask.getVehiNo(), staffInfo.getStaffAirportCode());
                //判断查询出来的车辆信息是否为空
                if (vehi != null) {
                    //如果不为空把车辆ID赋值到人员车辆任务对象中
                    staffTask.setVehiId(vehi.getVehiId());
                    //如果不为空把车辆别名赋值到人员车辆任务对象中
                    staffTask.setVehiNickname(vehi.getVehiNickname());
                    //如果不为空把车辆号赋值到人员车辆任务对象中
                    staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                    vehiPlateNo = vehi.getVehiPlateNo();
                    vehiNo = vehi.getVehiNo();
                }
                //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId())) {
                    //如果取到说明人员登录
                    staffTask.setSfvhStaffStatus(1);
                    //判断任务信息如果不为空
                    if (taskInfos != null) {
                        //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                        staffTask.setSfvhStaffStatus(2);
                        //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                        staffTask.setFlgtFlno(taskInfos.getTaskFlightNo());
                    }
                } else {
                    //如果取不到说明人员未登录
                    staffTask.setSfvhStaffStatus(0);
                }
                String AptareaCode = Constant.judgeAptareaCode(staffInfo.getStaffAptareaCode());
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                    staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
                } else {
                    staffTask.setStaffDateIng(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                    staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                } else {
                    staffTask.setStaffDateFree(null);
                }
                //放入人员 对应
                staffTask.setTaskId(null);
                redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), "");
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    int count = (int) redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount");
                    if (count > 0) {
                        redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", count - 1);
                        staffTask.setStaffingTaskCount(count - 1);
                    } else {
                        redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", 0);
                        staffTask.setStaffingTaskCount(0);
                    }
                } else {
                    redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", 0);
                    staffTask.setStaffingTaskCount(0);
                }
                //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
                Date now = new Date();
                SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
                String nowStr = sim1.format(now);
                long time = 60 * 1000 * 60 * 4;// 60秒
                Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
                String beforeDatetr = sim1.format(beforeDate);
                if (nowStr.equals(beforeDatetr)) {
                    nowStr = nowStr;
                } else {
                    nowStr = beforeDatetr;
                }
                List<MyTask> taskEndInfo = taskMapper.getTaskEndInfo(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode(), nowStr);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfoList : taskEndInfo) {
                    if (taskInfoList.getTaskOpeStaffId() != null && taskInfoList.getTaskOpeStaffId().equals(taskInfo.getTaskOpeStaffId())) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }
                //把要推送的对象放进Map集合中
                webMap.put("staff", staffTask);
            }
            map.put("key", true);
            map.put("val", "任务已取消");
        }
        //更新航班信息,判断如果返回等于1说明更新成功，否则更新失败
        if (flightMapper.updateFlightInfoOne(taskInfo.getTaskFlightId(), 0, null) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
        //for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));

        sendToKafkaTask(publicMyTask, myFlightTask);


        if (null != pubUpDate) {
            //webMap.put("flight",pubUpDate);
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_TASK, webMap);
        }
        //推送给调度员
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_TASK_CANCEL, webMap);
        //推送给PC 人员状态改变 (胡)
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PAD_STAFF_VEHI, webMap);

        //推送给加油员
        webMap.remove("task");
        webMap.remove("selfFlight");
        webMap.remove("staff");
        SendMsg2Redis.testDingYue(stringRedisTemplate, taskInfo.getTaskOpeStaffId(), Constant.TASKFLIGHT, Constant.PAD_TASK_CANCEL, webMap);
        sends(task.getTaskId(), taskInfo.getTaskOpeStaffId(), staffinfo.getStaffName(), vehiNo, vehiPlateNo, "1", taskInfo.getTaskAirportCode(), new Date().getTime());
        return map;
    }

    /**
     * 获取已下发的任务列表
     */
    @Override
    public List<MyTask> getTaskAlready(MyStaff staff) {
        List<MyTask> taskAlready = taskMapper.getTaskAlready(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        for (MyTask myFlightTask : taskAlready) {
            MyFlightTask flight = taskMapper.getFlightById(myFlightTask.getTaskFlightId());

            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
//            tasks.setStaffId(myFlightTask.getTaskCreStaffId());
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
            Integer type = null;
            type = flightService.pingSingleType(flight);
            myFlightTask.setFlrcType(type);
        }
        return taskAlready;
    }

    /**
     * 更改任务的状态,任务接受1->3
     *
     * @return
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public String updateTaskState(MyTask task, MyStaff staff) {
        MyTask publicMyTask = null;
        MyFlightTask myFlightTask = null;
        //根据任务ID查询任务单条信息
        MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
        String opsStaffName = "";
        String opsStaffId = "";
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        //存车牌号
        String vehiPlateNo = "";
        // 油单编号前5位
        String flrcNo = "";
        String userId = "";
        if (taskInfo.getTaskStatus() == 1) {
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String taskAccTime = (task.getTaskAccTime() == null) ? null : sim.format(task.getTaskAccTime());
            MyStaffVehi vehiInfo = staffMapper.getStaffVehiInfo(taskInfo.getTaskOpeStaffId());
            String vehiNo = vehiInfo != null ? vehiInfo.getSfvhVehiNo() : "";
            //根据任务ID和任务状态更改任务的状态
            if (taskMapper.updateTaskState(task.getTaskId(), task.getTaskStatus(), taskAccTime, vehiNo) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                //创建Map集合用来推送消息
                Map<String, Object> webMap = new HashMap<String, Object>();
                //根据任务ID查询任务单条信息
                MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
                ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
                myFlightTasks.add(taskAndFlightById);
                List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
                if (myenum.size() > 0) {
                    taskAndFlightById = myenum.get(0);
                }
                if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                }
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                // 油单类型获取
                String airplane = taskInfo.getTaskAirportCode();
                Integer type = null;
                type = flightService.pingSingleType(taskAndFlightById);
                taskInfos.setFlrcType(type);
                taskAndFlightById.setFlrcType(type);
                // 机场代码 + 油单类型
                flrcNo = airplane + type;
                MyStaff staffinfo = staffMapper.getStaffById(taskInfos.getTaskOpeStaffId());
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把查出来的人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffinfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffinfo.getStaffPhone());
                staffTask.setStaffName(staffinfo.getStaffName());
                taskInfos.setTaskOpeStaffName(staffinfo.getStaffName());
                taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
//                //根据加油员ID获取人员车辆表的信息
                MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());
                //判断如果人员车辆信息是否为空
                //     Assert.notNull(staffVehis,String.format("查询不到人员绑定车辆,人员ID：{%s}",staffinfo.getStaffId()));
                //不为空的话把车辆编号赋值进人员车辆任务对象中
                MyVehi vehi = null;
                if (staffVehis != null && StringUtils.isNotEmpty(staffVehis.getSfvhVehiNo())) {
                    staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                    vehi = vehiTaskMapper.getVehiInfo(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
                }
                //判断查询出来的车辆信息是否为空
                if (vehi != null) {
                    //如果不为空把车辆ID赋值到人员车辆任务对象中
                    staffTask.setVehiId(vehi.getVehiId());
                    //如果不为空把车辆别名赋值到人员车辆任务对象中
                    staffTask.setVehiNickname(vehi.getVehiNickname());
                    //如果不为空把车辆号赋值到人员车辆任务对象中
                    staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                    vehiPlateNo = vehi.getVehiPlateNo();
                }
                //根据人员ID查询出单条任务信息
                MyTask taskInfoss = fuelRecptMapper.gettaskById(taskInfos.getTaskOpeStaffId());
                //判断任务信息如果不为空
                if (taskInfoss != null) {
                    //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                    staffTask.setSfvhStaffStatus(2);
                    //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                    staffTask.setFlgtFlno(taskInfos.getTaskFlightNo());
                } else {
                    //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                    if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId())) {
                        //如果取到说明人员登录
                        staffTask.setSfvhStaffStatus(1);
                    } else {
                        //如果取不到说明人员未登录
                        staffTask.setSfvhStaffStatus(0);
                    }
                }
                String AptareaCode = Constant.judgeAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
                String staffDateIng = null;
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                    staffDateIng = String.valueOf(redis.get(Constant.LOGIN_KEY + staff.getLoginUserIn().getStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng"));
                }
                if (null == staffDateIng || "null".equals(staffDateIng)) {
                    //连续工作时间
                    redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng", DateUtil.getCurrentDateTimeStr());
                    staffTask.setStaffDateIng(DateUtil.getCurrentDateTimeStr());
                } else {
                    staffTask.setStaffDateIng(staffDateIng);
                }
                //连续休息时间
                redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime", "null");
                //定义空变量用来接收连续工作数量
                Integer taskCount = 0;
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    taskCount = (Integer) redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount");
                }
                //TODO 放入人员 对应
                redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), task.getTaskId());
                staffTask.setTaskId(task.getTaskId());
                //连续工作数量
                redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", taskCount + 1);
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                    staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                } else {
                    staffTask.setStaffDateFree(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                } else {
                    staffTask.setStaffingTaskCount(0);
                }
                //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
                Date now = new Date();
                SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
                String nowStr = sim1.format(now);
                long time = 60 * 1000 * 60 * 4;// 60秒
                Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
                String beforeDatetr = sim1.format(beforeDate);
                if (nowStr.equals(beforeDatetr)) {
                    nowStr = nowStr;
                } else {
                    nowStr = beforeDatetr;
                }
                List<MyTask> taskEndInfo = taskMapper.getTaskEndInfo(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfoList : taskEndInfo) {
                    if (taskInfoList.getTaskOpeStaffId() != null && taskInfoList.getTaskOpeStaffId().equals(taskInfo.getTaskOpeStaffId())) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }
                // TODO 李荣晨添加逻辑
                if (taskInfos != null && null != taskInfos.getTaskStatus()) {
                    staffTask.setTaskStatus(taskInfos.getTaskStatus());
                }
                //把要推送的对象放进Map集合中
                webMap.put("staff", staffTask);
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfos);
                webMap.put("flight", taskAndFlightById);
                publicMyTask = taskInfo;
                //垮库查询获取调度员ID
                List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
                // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
                userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                opsStaffName = staffTask.getStaffName();
                opsStaffId = staffTask.getSfvhStaffId();
                // 推送给调度员
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                        Constant.PAD_TASK_STATE_UPDATE, webMap);
                //推送给 PC  人员信息改变(胡)
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                        Constant.PAD_STAFF_VEHI, webMap);
            }
            MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfo.getTaskId());
            myFlightTask = taskAndFlightById;
            sendToKafkaTask(publicMyTask, myFlightTask);
            Date date = new Date();
            RefuelingTask flightInfo = getFlightInfo(task.getTaskId());
            flightInfo.setReceviceTime(date);
            send(flightInfo);
            //根据任务ID查询任务单条信息
            MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
            sends(task.getTaskId(), opsStaffId, opsStaffName, taskInfos.getTaskVehiNo(), vehiPlateNo, "2", staff.getLoginUserIn().getStaffAirportCode(), new Date().getTime());
            Object car_security = redis.get("car_security");
            if (null != car_security) {
                redis.set("car_security", (Integer.valueOf(String.valueOf(car_security)) + 1));
            } else {
                redis.set("car_security", 1);
            }
            sendsSecurity(1);
            return flrcNo;
        } else {
            return "任务状态大于任务接受数据不在进行更新";
        }
    }

    /**
     * 更改任务的状态,加油到位3->4
     *
     * @return
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void taskStatusInPlace(MyTask task, MyStaff staff) {
        MyTask publicMyTask = null;
        MyFlightTask myFlightTask = null;
        //根据任务ID查询任务单条信息
        MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        MyStaffVehi staffvehie = myFuelMapper.selectstaffvehi(staff.getLoginUserIn().getStaffId());
        String vehiNo = null;
        if (staffvehie != null) {
            vehiNo = staffvehie.getSfvhVehiNo();
        }
        if (taskInfos.getTaskStatus() == 3) {
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String taskChagStaTime = sim.format(task.getTaskChagStaTime());
            //根据任务ID和任务状态更改任务的状态
            if (taskMapper.updateTaskStates(task.getTaskId(), task.getTaskStatus(), vehiNo, taskChagStaTime) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                //创建Map集合用来推送消息
                Map<String, Object> webMap = new HashMap<String, Object>();
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
                ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
                myFlightTasks.add(taskAndFlightById);
                List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
                if (myenum.size() > 0) {
                    taskAndFlightById = myenum.get(0);
                }
                if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                    taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
                }
                myFlightTask = taskAndFlightById;
                //根据任务ID查询任务单条信息
                MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                Integer type = null;
                type = flightService.pingSingleType(taskAndFlightById);
                taskInfo.setFlrcType(type);
                MyStaff staffinfo = staffMapper.getStaffById(taskInfos.getTaskOpeStaffId());
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把查出来的人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffinfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffinfo.getStaffPhone());
                staffTask.setStaffName(staffinfo.getStaffName());
                taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfo);

                publicMyTask = taskInfo;

                //根据加油员ID获取人员车辆表的信息
                MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());
                //判断如果人员车辆信息是否为空
                MyVehi vehi = null;
                if (staffVehis != null) {
                    //不为空的话把车辆编号赋值进人员车辆任务对象中
                    staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                    vehi = vehiTaskMapper.getVehiInfo(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
                }

                //判断查询出来的车辆信息是否为空
                if (vehi != null) {
                    //如果不为空把车辆ID赋值到人员车辆任务对象中
                    staffTask.setVehiId(vehi.getVehiId());
                    //如果不为空把车辆别名赋值到人员车辆任务对象中
                    staffTask.setVehiNickname(vehi.getVehiNickname());
                    //如果不为空把车辆号赋值到人员车辆任务对象中
                    staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                }
                //根据人员ID查询出单条任务信息
                MyTask taskInfoOne = fuelRecptMapper.gettaskById(staffinfo.getStaffId());
                //判断任务信息如果不为空
                if (taskInfoOne != null) {
                    //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                    staffTask.setSfvhStaffStatus(2);
                    //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                    staffTask.setFlgtFlno(taskInfoOne.getTaskFlightNo());
                } else {
                    //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                    if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId())) {
                        //如果取到说明人员登录
                        staffTask.setSfvhStaffStatus(1);
                    } else {
                        //如果取不到说明人员未登录
                        staffTask.setSfvhStaffStatus(0);
                    }
                }
                String AptareaCode = Constant.judgeAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
                String staffDateIng = null;
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                    staffDateIng = String.valueOf(redis.get(Constant.LOGIN_KEY + staff.getLoginUserIn().getStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng"));
                }
                if (null == staffDateIng || "null".equals(staffDateIng)) {
                    //连续工作时间
                    staffTask.setStaffDateIng(null);
                } else {
                    staffTask.setStaffDateIng(staffDateIng);
                }
                //连续休息时间
                redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime", "");
                //放入人员 对应
                if (!StringUtils.isEmpty(String.valueOf(redis.get("staffAndTask:" + staffinfo.getStaffId())))) {
                    String data = String.valueOf(redis.get("staffAndTask:" + staffinfo.getStaffId()));
                    if (!"null".equals(data)) {
                        String[] split = data.split(",");
                        if (split.length > 0) {
                            staffTask.setTaskId(split[0]);
                        }
                    }
                }
                //连续工作数量
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                    staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                } else {
                    staffTask.setStaffDateFree(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                } else {
                    staffTask.setStaffingTaskCount(0);
                }
                //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
                Date now = new Date();
                SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
                String nowStr = sim1.format(now);
                long time = 60 * 1000 * 60 * 4;// 60秒
                Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
                String beforeDatetr = sim1.format(beforeDate);
                if (nowStr.equals(beforeDatetr)) {
                    nowStr = nowStr;
                } else {
                    nowStr = beforeDatetr;
                }
                List<MyTask> taskEndInfo = taskMapper.getTaskEndInfo(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfoList : taskEndInfo) {
                    if (taskInfoList.getTaskOpeStaffId() != null && taskInfoList.getTaskOpeStaffId().equals(taskInfo.getTaskOpeStaffId())) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }
                // TODO 李荣晨添加逻辑
                if (taskInfo != null && null != taskInfo.getTaskStatus()) {
                    staffTask.setTaskStatus(taskInfo.getTaskStatus());
                }
                //把要推送的对象放进Map集合中
                webMap.put("staff", staffTask);
                taskAndFlightById.setFlrcType(type);
                taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
                webMap.put("flight", taskAndFlightById);
                //垮库查询获取调度员ID
                List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
                // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
                String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                sendToKafkaTask(publicMyTask, myFlightTask);
                // 推送给调度员
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                        Constant.PAD_TASK_STATE_UPDATE, webMap);
                Date date = new Date();
                RefuelingTask flightInfo = getFlightInfo(task.getTaskId());
                flightInfo.setPlaceTime(date);
                flightInfo.setBeginOilTime(date);
                send(flightInfo);
            }
        }
    }

    /**
     * 根据任务ID查询任务单条记录和航班
     */
    @Override
    public MyFlightTask getTaskAndFlightById(MyTask task) {
        MyFlightTask myFlightTask = taskMapper.getTaskAndFlightById(task.getTaskId());
        String taskOpeStaffId = StrUtil.blankToDefault(task.getTaskOpeStaffId(),
                Optional.ofNullable(myFlightTask).map(MyFlightTask::getTaskOpeStaffId).orElse(StrUtil.EMPTY));
        Integer flrcType = 3;
        if (myFlightTask.getFlgtFlti() != null) {
            flrcType = flightService.pingSingleType(myFlightTask);
        }
        myFlightTask.setFlrcType(flrcType);
        if (StrUtil.isNotBlank(taskOpeStaffId)) {
            MyStaff staffinfo = staffMapper.getStaffById(taskOpeStaffId);
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
        }
        if (StrUtil.isNotBlank(myFlightTask.getTaskCreStaffId())) {
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }
        if (StringUtils.isNotEmpty(myFlightTask.getFlgtRegn())) {
            TFlightTrouble tFlightTrouble = new TFlightTrouble();
            tFlightTrouble.setFlgtRegn(myFlightTask.getFlgtRegn());
            myFlightTask.settFlightTroubles(tFlightTroubleMapper.selectByRegn(tFlightTrouble));
        }

        String arcrCustomNum = myFlightTask.getArcrCustomNum();
        Pair<String, String> pair = this.pingCountries(myFlightTask.getFlgtRegn(), myFlightTask.getFlgtFlno(), true);
        if (ObjectUtil.isEmpty(arcrCustomNum)) {
            if (!"未知".equals(pair.getRight())) {
                String right = pair.getRight();
                if (StringUtils.isNotEmpty(right)) {
                    String[] $s = right.split("&");
                    if ($s.length == 2) {
                        myFlightTask.setArcrCustomNum($s[1]);
                        arcrCustomNum = $s[1];
                    }
                }
            }
        }
        MyCustom cstmName = flightCodeTemporaryMapper.getCstmName(arcrCustomNum);
        if (ObjectUtil.isNotNull(cstmName)) {
            myFlightTask.setFlgtAlcname(cstmName.getCstmName());//购买航空公司
        }
        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(myFlightTask.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                myFlightTask.setFlgtAcname(pair.getLeft());//类型
            }
        }
        return myFlightTask;
    }

    /**
     * 任务标星
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateTaskStarmark(MyTask task) {
        //根据任务ID和任务标星更改任务的任务标星字段,等于1说明更新成功
        if (taskMapper.updateTaskStarmark(task.getTaskId(), task.getTaskStarmark()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 根据系统当前日期获取7天的任务列表
     */
    @Override
    public List<MyTask> getTasktSevenDateList(MyStaff staff, String taskDate) {
        if (StringUtils.isEmpty(taskDate)) {
            taskDate = DateUtil.getCurrentDateStr();
        }
        List<MyTask> tasktSevenDateList = Lists.newArrayList();
        List<MyStaff> staffList = Lists.newArrayList();

        if ("0".equals(staff.getLoginUserIn().getStaffType())) {
            tasktSevenDateList = taskMapper.getTasktSevenDateList(null, null, taskDate);
            staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        } else {
            tasktSevenDateList = taskMapper.getTasktSevenDateList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), taskDate);
            staffList = staffMapper.getAllStaffList(staff.getLoginUserIn().getStaffAirportCode(), "", null);
        }

        for (MyTask myFlightTask : tasktSevenDateList) {
            for (MyStaff myStaff : staffList) {
                if (myFlightTask.getTaskOpeStaffId() != null && myStaff.getStaffId().equals(myFlightTask.getTaskOpeStaffId())) {
                    //赋值加油员姓名
                    myFlightTask.setTaskOpeStaffName(myStaff.getStaffName());
                }
                if (myFlightTask.getTaskCreStaffId() != null && myStaff.getStaffId().equals(myFlightTask.getTaskCreStaffId())) {
                    //赋值创建人员姓名
                    myFlightTask.setTaskCreStaffName(myStaff.getStaffName());
                }
            }
        }
        List<TCreditInfo> tCreditInfos = tCreditInfoMapper.selectCreditInfo(new TCreditInfo());

        tasktSevenDateList.stream()
                .forEach(dto -> {
                    tCreditInfos.stream().filter(tCreditInfo ->
                                    ObjectUtil.contains(dto.getCustomNum(), tCreditInfo.getCstno()))
                            .findFirst()
                            .ifPresent(tCreditInfo -> {
                                dto.setCilvl(tCreditInfo.getCilvl());
                                dto.setCitst(tCreditInfo.getCitst());
                                dto.setCirmk(tCreditInfo.getCirmk());
                            });
                });

        return tasktSevenDateList;
    }

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    @Override
    public Object getTasktAndOil(MyTask task) {
        //根据任务ID查询任务单条和航班的记录
        MyFlightTask myFlightTask = taskMapper.getTaskAndFlightById(task.getTaskId());
        //判断如果任务状态是任务完成的就去查对应的油单信息
        if (myFlightTask.getTaskStatus() != null && myFlightTask.getTaskStatus() == 7) {
            //通过油单编号获取油单的信息,使用一个任务油单对象接收油单的信息
            MyTaskOil taskOil = taskMapper.getOilInfo(myFlightTask.getTaskFuelRecptNo());
            if (taskOil != null) {
                //把任务和航班的信息也存进任务油单航班对象中
                taskOil.setTaskId(myFlightTask.getTaskId());
                taskOil.setTaskOpeStaffId(myFlightTask.getTaskOpeStaffId());
                taskOil.setTaskContent(myFlightTask.getTaskContent());
                taskOil.setTaskStatus(myFlightTask.getTaskStatus());
                taskOil.setTaskAsgTime(myFlightTask.getTaskAsgTime());
                taskOil.setTaskAccTime(myFlightTask.getTaskAccTime());
                taskOil.setTaskChagStaTime(myFlightTask.getTaskChagStaTime());
                taskOil.setTaskChagEndTime(myFlightTask.getTaskChagEndTime());
                taskOil.setTaskDoneTime(myFlightTask.getTaskDoneTime());
                taskOil.setTaskFuelRecptNo(myFlightTask.getTaskFuelRecptNo());
                taskOil.setTaskVehiNo(myFlightTask.getTaskVehiNo());
                taskOil.setTaskCreStaffId(myFlightTask.getTaskCreStaffId());
                taskOil.setTaskStarmark(myFlightTask.getTaskStarmark());
                taskOil.setTaskRecCreTime(myFlightTask.getTaskRecCreTime());
                taskOil.setFlgtId(myFlightTask.getFlgtId());
                taskOil.setFlgtFfid(myFlightTask.getFlgtFfid());
                taskOil.setFlgtAirportCode(myFlightTask.getFlgtAirportCode());
                taskOil.setFlgtAptareaCode(myFlightTask.getFlgtAptareaCode());
                taskOil.setFlgtFlno(myFlightTask.getFlgtFlno());
                taskOil.setFlgtFlop(myFlightTask.getFlgtFlop());
                taskOil.setFlgtAcname(myFlightTask.getFlgtAcname());
                taskOil.setFlgtRegn(myFlightTask.getFlgtRegn());
                taskOil.setFlgtPlacecode(myFlightTask.getFlgtPlacecode());
                taskOil.setFlgtAl2c(myFlightTask.getFlgtAl2c());
                taskOil.setFlgtAlcname(myFlightTask.getFlgtAlcname());
                taskOil.setFlgtVialc(myFlightTask.getFlgtVialc());
                taskOil.setFlgtAStot(myFlightTask.getFlgtAStot());
                taskOil.setFlgtAEtot(myFlightTask.getFlgtAEtot());
                taskOil.setFlgtAAtot(myFlightTask.getFlgtAAtot());
                taskOil.setFlgtDStot(myFlightTask.getFlgtDStot());
                taskOil.setFlgtDEtot(myFlightTask.getFlgtDEtot());
                taskOil.setFlgtDAtot(myFlightTask.getFlgtDAtot());
                taskOil.setFlgtOrg3c(myFlightTask.getFlgtOrg3c());
                taskOil.setFlgtOrgnm(myFlightTask.getFlgtOrgnm());
                taskOil.setFlgtTrs3c1(myFlightTask.getFlgtTrs3c1());
                taskOil.setFlgtTrsnm1(myFlightTask.getFlgtTrsnm1());
                taskOil.setFlgtTrs3c2(myFlightTask.getFlgtTrs3c2());
                taskOil.setFlgtTrsnm2(myFlightTask.getFlgtTrsnm2());
                taskOil.setFlgtTrs3c3(myFlightTask.getFlgtTrs3c3());
                taskOil.setFlgtTrsnm3(myFlightTask.getFlgtTrsnm3());
                taskOil.setFlgtTrs3c4(myFlightTask.getFlgtTrs3c4());
                taskOil.setFlgtTrsnm4(myFlightTask.getFlgtTrsnm4());
                taskOil.setFlgtTrs3c5(myFlightTask.getFlgtTrs3c5());
                taskOil.setFlgtTrsnm5(myFlightTask.getFlgtTrsnm5());
                taskOil.setFlgtDes3c(myFlightTask.getFlgtDes3c());
                taskOil.setFlgtDesnm(myFlightTask.getFlgtDesnm());
                taskOil.setFlgtAdid(myFlightTask.getFlgtAdid());
                taskOil.setFlgtFlti(myFlightTask.getFlgtFlti());
                taskOil.setFlgtFtyp(myFlightTask.getFlgtFtyp());
                taskOil.setFlgtProxy(myFlightTask.getFlgtProxy());
                taskOil.setFlgtLinkFlno(myFlightTask.getFlgtLinkFlno());
                taskOil.setFlgtFnflag(myFlightTask.getFlgtFnflag());
                taskOil.setFlgtGame(myFlightTask.getFlgtGame());
                taskOil.setFlgtChocksIn(myFlightTask.getFlgtChocksIn());
                taskOil.setFlgtChocksOut(myFlightTask.getFlgtChocksOut());
                taskOil.setFlgtVip(myFlightTask.getFlgtVip());
                MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
                if (staffinfo != null) {
                    //赋值加油员姓名
                    taskOil.setTaskOpeStaffName(staffinfo.getStaffName());
                }
                MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
                if (staffinfos != null) {
                    //赋值创建人员姓名
                    taskOil.setTaskCreStaffName(staffinfos.getStaffName());
                }
                return taskOil;
            } else {
                MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
                if (staffinfo != null) {
                    //赋值加油员姓名
                    myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
                }
                MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
                if (staffinfos != null) {
                    //赋值创建人员姓名
                    myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
                }
                return myFlightTask;
            }
            //否则只返回任务的信息
        } else {
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
            return myFlightTask;
        }
    }

    /**
     * 任务信息修改
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateTasktInfo(MyTask task) {
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(task.getTaskAirportCode());
        staffInfo.setStaffAptareaCode(task.getTaskAptareaCode());
        //根据任务ID更改任务的信息,等于1说明更新成功
        if (taskMapper.updateTasktInfo(task) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        } else {
            //根据任务ID查询任务单条信息
            MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
            //创建Map集合用来推送消息
            Map<String, Object> webMap = new HashMap<String, Object>();
            //把要推送的任务对象放进Map集合中
            webMap.put("task", taskInfo);

            //根据任务ID查出单条任务航班信息
            MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
            ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
            myFlightTasks.add(taskAndFlightById);
            List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
            if (myenum.size() > 0) {
                taskAndFlightById = myenum.get(0);
            }
            if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
                taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
            } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
                taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
            }
            //把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
            //判断如果航班是本场的话再推送一条本场航班消息
            if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                //把要推送的航班任务对象放进Map集合中
                webMap.put("selfFlight", taskAndFlightById);
            }
            //垮库查询获取调度员ID
            List<MyStaff> staffList = staffMapper.getStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
            // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
            String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
            // 推送给调度员
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PAD_TASK_STATE_UPDATE, webMap);
        }
    }

    /**
     * 根据任务的检索条件进行检索任务表
     */
    @Override
    public List<MyTask> getTasktInfoCondition(MyTask task, MyStaff staff) {
        List<MyTask> tasktInfoCondition = taskMapper.getTasktInfoCondition(task, staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        for (MyTask myFlightTask : tasktInfoCondition) {
            //跨库调用方法获取参数
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }
        return tasktInfoCondition;
    }

    /**
     * 加油员任务检索by任务状态
     */
    @Override
    public List<MyTask> getStaffTasktInfoByStatus(MyStaff staff) {
        List<MyTask> staffTasktInfoCondition = taskMapper.getStaffTasktInfoByStatus(staff.getLoginUserIn().getStaffId());
        for (MyTask myFlightTask : staffTasktInfoCondition) {
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }
        return staffTasktInfoCondition;
    }

    /**
     * 加油员任务检索
     */
    @Override
    public List<MyTask> getStaffTasktInfoCondition(MyTask task, MyStaff staff) {
        List<MyTask> staffTasktInfoCondition = taskMapper.getStaffTasktInfoCondition(task, staff.getLoginUserIn().getStaffId());
        for (MyTask myFlightTask : staffTasktInfoCondition) {
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }
        return staffTasktInfoCondition;
    }

    /**
     * 任务结束把任务状态改为7同时把任务中的任务完成时间更新
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateTaskEnd(MyTask task, String useType) {
        if (null != task.getFlgtStatus() && 2 == task.getFlgtStatus()) {
            MyFlight myFlight = new MyFlight();
            myFlight.setFlgtId(task.getTaskFlightId());
            myFlight.setFlgtStatus(2);
            //取消挂起
            int i = flightMapper.updateFlight(myFlight);
        }
        MyTask publicMyTask = null;
        MyFlightTask myFlightTask = null;
        MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
        taskInfos.setTaskStatus(7);
        if (null != task.getTaskAccTime()) {   //任务接受时间
            taskInfos.setTaskAccTime(task.getTaskAccTime());
        }
        if (null == taskInfos.getTaskAccTime()) {
            taskInfos.setTaskAccTime(new Date());
        }
        if (null == taskInfos.getTaskChagStaTime()) {
            taskInfos.setTaskChagStaTime(new Date());
        }
        if (null == taskInfos.getTaskArriveTime()) {
            taskInfos.setTaskArriveTime(new Date());
        }
        if (null != task.getTaskChagStaTime()) {   // 加油开始时间
            taskInfos.setTaskChagStaTime(task.getTaskChagStaTime());
            taskInfos.setTaskArriveTime(task.getTaskChagStaTime());//加油到位时间与开始时间是一个 于国辉2018-8-29
        }
        /*if(null != task.getTaskArriveTime()){  //加油到位时间
            taskInfos.setTaskArriveTime(task.getTaskChagStaTime());
        }*/
        if (null != task.getTaskChagEndTime()) {  //加油完成时间
            taskInfos.setTaskChagEndTime(task.getTaskChagEndTime());
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != task.getTaskDoneTime()) {  //任务完成时间
            taskInfos.setTaskDoneTime(task.getTaskDoneTime());
        }
        if (!StringUtils.isEmpty(task.getTaskOpeStaffId())) {  //任务完成时间
            taskInfos.setTaskOpeStaffId(task.getTaskOpeStaffId());
        }
        if (1 != taskMapper.updateTasktInfo(taskInfos)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        publicMyTask = taskInfos;
        // 创建人员对象用来存放机场所属代码
        MyStaff staffinfo = staffMapper.getStaffById(taskInfos.getTaskOpeStaffId());
        Assert.notNull(staffinfo, String.format("根据人员{%s}查询失败", taskInfos.getTaskOpeStaffId()));
        // 修改任务状态
        if (fuelRecptMapper.updateFuelstatus(task.getTaskId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        // 修改任务完成时间，加油车编号
       /* if (fuelRecptMapper.updatefueltaskvehi(taskDoneTime, task.getTaskId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }*/
        // 创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        // 根据任务ID查出单条任务航班信息
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
        ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
        myFlightTasks.add(taskAndFlightById);
        List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
        if (myenum.size() > 0) {
            taskAndFlightById = myenum.get(0);
        }
        if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        }
        myFlightTask = taskAndFlightById;
        MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
        if (staffinfo != null) {
            taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
        }
        Integer flrcType = null;
        flrcType = flightService.pingSingleType(taskAndFlightById);
        //把判断好的油单类型放进推送的任务对象中
        taskInfo.setFlrcType(flrcType);
        // 把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfo);
        // 判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            // 把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        if (staffinfo != null) {
            //把查出来的人员ID赋值到人员车辆任务对象中
            staffTask.setSfvhStaffId(staffinfo.getStaffId());
            //把查出来的人员电话赋值到人员车辆任务对象中
            staffTask.setStaffPhone(staffinfo.getStaffPhone());
            staffTask.setStaffName(staffinfo.getStaffName());
        }
        MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(Objects.requireNonNull(staffinfo).getStaffId());
        //判断如果人员车辆信息是否为空
        MyVehi vehi = null;
        if (staffVehis != null) {
            //不为空的话把车辆编号赋值进人员车辆任务对象中
            staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
            vehi = vehiTaskMapper.getVehiInfo(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
        }

        //判断查询出来的车辆信息是否为空
        if (vehi != null) {
            //如果不为空把车辆ID赋值到人员车辆任务对象中
            staffTask.setVehiId(vehi.getVehiId());
            //如果不为空把车辆别名赋值到人员车辆任H务对象中
            staffTask.setVehiNickname(vehi.getVehiNickname());
            //如果不为空把车辆号赋值到人员车辆任务对象中
            staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
        }
        //根据人员ID查询出单条任务信息
        MyTask taskInfoss = fuelRecptMapper.gettaskById(taskInfos.getTaskOpeStaffId());
        //判断任务信息如果不为空
        if (taskInfoss != null) {
            //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
            staffTask.setSfvhStaffStatus(2);
            //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
            staffTask.setFlgtFlno(taskInfoss.getTaskFlightNo());
        } else {
            //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
            if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId())) {
                //如果取到说明人员登录
                staffTask.setSfvhStaffStatus(1);
            } else {
                //如果取不到说明人员未登录
                staffTask.setSfvhStaffStatus(0);
            }
        }
        List<MyTask> taskEndInfo = new ArrayList<MyTask>();
        String AptareaCode = Constant.judgeAptareaCode(staffinfo.getStaffAptareaCode());
        if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
            staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
        } else {
            staffTask.setStaffDateIng(null);
        }
        if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
            staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
        } else {
            staffTask.setStaffDateFree(null);
        }
        if (null != redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
            staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + staffinfo.getStaffId() + ":" + staffinfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
        } else {
            staffTask.setStaffingTaskCount(0);
        }
        //放入人员 对应
        log.info("放入人员 对应前------>taskInfo" + JSON.toJSONString(taskInfo));
        redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), "");
        staffTask.setTaskId(null);

     /*   if (null != redis.get("staffAndTask:" + taskInfo.getTaskOpeStaffId())) {
            String data = String.valueOf(redis.get("staffAndTask:" + taskInfo.getTaskOpeStaffId()));
            log.info("放入人员 对应------>data" + data);
            String[] split = data.split(",");
            if (split.length != 0) {
                //AtomicReference<StringBuffer> newDate = new AtomicReference<>(new StringBuffer());
                String newDate =  Arrays.stream(split).filter(
                        o-> StringUtils.isNotEmpty(o)
                                && !"null".equals(o) &&
                                !taskInfo.getTaskId().equals(o))
                        .collect(joining(","));
                log.info("放入人员 对应------>newDate" + newDate);
                if (StringUtils.isEmpty(newDate)) {
                    redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), "");
                    staffTask.setTaskId(null);
                } else {
                    redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), newDate);
                    String newDate1 = newDate;
                    String[] split1 = newDate1.split(",");
                    if (split1.length > 0) {
                        staffTask.setTaskId(split1[0]);
                    } else {
                        staffTask.setTaskId(null);
                    }
                }
            } else {
                redis.set("staffAndTask:" + taskInfo.getTaskOpeStaffId(), "");
                staffTask.setTaskId(null);
            }
        }*/
        //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        Date now = new Date();
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim1.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim1.format(beforeDate);
        if (nowStr.equals(beforeDatetr)) {
            nowStr = nowStr;
        } else {
            nowStr = beforeDatetr;
        }
        taskEndInfo = taskMapper.getTaskEndInfo(staffinfo.getStaffAirportCode(), staffinfo.getStaffAptareaCode(), nowStr);
        staffTask.setStaffEndTaskCount(0);
        for (MyTask taskInfoList : taskEndInfo) {
            if (taskInfoList.getTaskOpeStaffId() != null && taskInfoList.getTaskOpeStaffId().equals(taskInfo.getTaskOpeStaffId())) {
                staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
            }
        }
        //把要推送的对象放进Map集合中
        // TODO 李荣晨添加逻辑
        if (null != taskInfo.getTaskStatus()) {
            staffTask.setTaskStatus(taskInfo.getTaskStatus());
        }
        webMap.put("staff", staffTask);
        taskAndFlightById.setFlrcType(flrcType);
        taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        taskAndFlightById.setFlgtStatus(2);
        webMap.put("flight", taskAndFlightById);
        // 垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staffinfo.getStaffAirportCode(), staffinfo.getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(joining(","));
        sendToKafkaTask(publicMyTask, myFlightTask);
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_TASK_STATE_UPDATE, webMap);
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, webMap);

        RefuelingTask flightInfo = getFlightInfo(task.getTaskId());
        flightInfo.setEndTime(taskInfos.getTaskDoneTime());
        send(flightInfo);
        String staffName = "";
        String vehiPlateNo = "";
        staffName = staffinfo.getStaffName();
        if (vehi != null) {
            vehiPlateNo = vehi.getVehiPlateNo();
        }
        if (useType.equals("PAD")) {
            System.out.println("任务完成->修改油单->变为已完成" + task.getTaskFuelRecptNo());
            Integer integer = tFuelNoService.updatefuleNoEnd(task.getTaskFuelRecptNo());
            Assert.notNull(integer, "修改油单编号失败");
        }
        sends(task.getTaskId(), taskInfo.getTaskOpeStaffId(), staffName, taskInfo.getTaskVehiNo(), vehiPlateNo, "1", taskInfo.getTaskAirportCode(), new Date().getTime());
        Object car_security = redis.get("car_security");
        if (null != car_security) {
            redis.set("car_security", (Integer.valueOf(String.valueOf(car_security)) - 1));
        } else {
            redis.set("car_security", 0);
        }
        sendsSecurity(2);
    }

    /**
     * 根据机场代码查询所有当天已完成的任务信息（远程调用）
     */
    @Override
    public List<MyTask> getTaskEndInfo(MyStaff staff) {
        //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        Date now = new Date();
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim1.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim1.format(beforeDate);
        if (nowStr.equals(beforeDatetr)) {
            nowStr = nowStr;
        } else {
            nowStr = beforeDatetr;
        }
        return taskMapper.getTaskEndInfo(staff.getStaffAirportCode(), staff.getStaffAptareaCode(), nowStr);
    }

    /**
     * 根据航班ID查询航班任务记录
     */
    @Override
    public List<MyFlightTask> getTaskAndFlightByFlightId(MyTask task) {

        List<MyFlightTask> myFlightTasklist = taskMapper.getTaskAndFlightByFlightId(task.getTaskFlightId());
        // 加油员姓名设定
        for (MyFlightTask myFlightTask : myFlightTasklist) {
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                // 赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                // 赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }
        return myFlightTasklist;
    }

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    @Override
    public MyTaskOil getTaskOilInfo(MyTask task) {
        //通过油单编号获取油单的信息,使用一个任务油单对象接收油单的信息
        MyTaskOil taskOil = taskMapper.getTaskOilInfo(task.getTaskId());
        MyStaff staffinfo = staffMapper.getStaffById(taskOil.getTaskOpeStaffId());
        if (staffinfo != null) {
            //赋值加油员姓名
            taskOil.setTaskOpeStaffName(staffinfo.getStaffName());
        }
        MyStaff staffinfos = staffMapper.getStaffById(taskOil.getTaskCreStaffId());
        if (staffinfos != null) {
            //赋值创建人员姓名
            taskOil.setTaskCreStaffName(staffinfos.getStaffName());
        }
        return taskOil;

    }

    /**
     * 根据人员ID更改任务中的车辆编号（远程调用）
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateTaskVehiNo(MyStaffVehi staffVehi) {
        //先根据人员ID查询该人员是否有任务
        List<MyTask> staffTasktInfoCondition = taskMapper.getStaffTasktInfoCondition(null, staffVehi.getSfvhStaffId());
        //判断如果查出来的集合大小大于0的话说明有任务就去根据人员ID更改任务中的车辆编号
        if (staffTasktInfoCondition.size() > 0) {
            for (MyTask myTask : staffTasktInfoCondition) {
                if (!staffVehi.getSfvhVehiNo().equals(myTask.getTaskVehiNo())) {
                    if (taskMapper.updateTaskVehiNo(staffVehi.getSfvhStaffId(), staffVehi.getSfvhVehiNo()) <= 0) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public List<MyTask> getOldTaskAlready(MyStaff staff) {
        List<MyTask> taskAlready = taskMapper.getOldTaskAlready(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        for (MyTask myFlightTask : taskAlready) {
            MyFlightTask flight = taskMapper.getFlightById(myFlightTask.getTaskFlightId());
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
            Integer type = null;
            type = flightService.pingSingleType(flight);
            myFlightTask.setFlrcType(type);
        }
        return taskAlready;
    }

    @Override
    public int updateTaskInfoOver(MyFlightTask task, MyStaff staff) {
        //判断此加油员今天是否使用手持pad进行加油完成任务
        //判断条件1.没有在线并且当天航班日任务状态都是等待接收任务
        if (!StringUtils.isEmpty(task.getTaskOpeStaffId())) {
            // 首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
            if (null == redis.get(Constant.LOGIN_KEY + task.getTaskOpeStaffId())) {
                int count = taskMapper.getTaskDoing(task);
                if (count < 1) {
                    String staffAndTaskKey = "staffAndTask:" + task.getTaskOpeStaffId();
                    //放入人员 对应
                    if (!StringUtils.isEmpty(String.valueOf(redis.get(staffAndTaskKey)))) {
                        redis.set(staffAndTaskKey, "");
                    }
                    //查询当天航班日加油员等待接收的所以任务
                    List<MyTask> TaskList = taskMapper.getNotDoTaskByTaskOpeStaffId(task);
                    String taskCountStr = Constant.LOGIN_KEY + task.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + "::" + "taskCount";
                    Integer taskCount = (Integer) redis.get(taskCountStr);
                    if (TaskList.size() > 0) {
                        if (null == taskCount) {
                            redis.set(taskCountStr, TaskList.size());
                        } else {
                            redis.set(taskCountStr, taskCount + TaskList.size());
                        }
                    }
                    //修改此加油员当天的任务全部改为已完成
                    TaskList.forEach(t -> {
                        t.setTaskAccTime(new Date());
                        t.setTaskOpeStaffId(task.getTaskOpeStaffId());
                        t.setTaskDoneTime(new Date());
                        updateTaskEnd(t, "PC");
                    });
                }
            }
        }


        return 0;
    }

    public void sendToKafkaTask(MyTask myTask, MyFlightTask myFlight) {
       /* DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        VoTask voTask = new VoTask();

        voTask.setTaskCarNO(StringUtils.isBlank(myTask.getTaskVehiNo()) ? "" : myTask.getTaskVehiNo());
        voTask.setTaskAirportCode(StringUtils.isBlank(myTask.getTaskAirportCode()) ? "" : myTask.getTaskAirportCode());
        voTask.setTaskFlightNo(StringUtils.isBlank(myTask.getTaskFlightNo()) ? "" : myTask.getTaskFlightNo());
        voTask.setTaskState(StringUtils.isBlank(myTask.getTaskStatus().toString()) ? "" : myTask.getTaskStatus().toString());
        voTask.setTaskAccTime(myTask.getTaskAccTime() == null ? "" : dateFormat.format(myTask.getTaskAccTime()));
        voTask.setTaskArriveTime(myTask.getTaskArriveTime() == null ? "" : dateFormat.format(myTask.getTaskArriveTime()));
        voTask.setTaskChagStaTime(myTask.getTaskChagStaTime() == null ? "" : dateFormat.format(myTask.getTaskChagStaTime()));
        voTask.setTaskChagEndTime(myTask.getTaskChagEndTime() == null ? "" : dateFormat.format(myTask.getTaskChagEndTime()));
        if (myFlight != null) {
            String ffidAlias = (
                    (StringUtils.isBlank(myFlight.getFlgtAirportCode()) ? "" : myFlight.getFlgtAirportCode())
                    + "," +   (StringUtils.isBlank(myFlight.getFlgtAl2c()) ? "" : myFlight.getFlgtAl2c()))
                    + "," + (StringUtils.isBlank(myFlight.getFlgtFlno()) ? "" : myFlight.getFlgtFlno())
                    + "," + (StringUtils.isBlank(myFlight.getFlgtAdid()) ? "" : myFlight.getFlgtAdid())
                    + "," + (myFlight.getFlgtFlop() == null ? "" : dateFormat.format(myFlight.getFlgtFlop()))
                    + "," + (StringUtils.isBlank(myFlight.getFlgtFlti()) ? "" : myFlight.getFlgtFlti());
            voTask.setFfidAlias(ffidAlias);
        }*/

        if (myFlight != null) {
            String s = JSON.toJSONString(myFlight);
            log.debug("------------" + s);
            kafkaTemplate.send(taskTopic, s);

            log.debug("这是往kafka中推送消息，消息主题是task_change_log，消息内容是：" + JSON.toJSONString(myTask));
        } else {
            log.debug("消息主题是task_change_log   myTask 是  null ");
        }
    }

    @Override
    public MyTask getTaskById(MyTask task) {
        MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
        if (taskInfos != null) {
            if ("".equals(taskInfos.getTaskFuelRecptNo())) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, "加油单编号为空"));
            }
            if ("".equals(taskInfos.getTaskRcPrintTime())) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, "打印油单完成时间为空"));
            }
        }
        return taskInfos;
    }


    private MyFlightTask pingCstmRegion(MyFlightTask flightTask) {
        String flgtFlno = flightTask.getFlgtFlno();
        if (StringUtils.isNotEmpty(flgtFlno)) {
            String[] split = flgtFlno.split("/");
            if (split.length > 0) {
                flgtFlno = split[0];
                flightTask.setFlgtFlno(flgtFlno);
            }
        }
        Pair<String, String> pair = pingCountries(flightTask.getFlgtRegn(), flightTask.getFlgtFlno(), false);
        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(flightTask.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                flightTask.setFlgtAcname(pair.getLeft());//类型
            }
            flightTask.setFlgtAlcname(pair.getRight());//购买航空公司
        }
        return flightTask;
    }

    @Override
    public String saveTaskChagStaTime(MyTask task) {
        String taskId = task.getTaskId();
        Date taskChagStaTime = task.getTaskChagStaTime();
        if (StringUtils.isEmpty(taskId) || null == taskChagStaTime) {
            return "参数不可为空";
        }
        if (flightMapper.updateTask(task) != 1) {
            return "更新失败";
        }
        return "success";
    }

    private List<MyFlightCode> getCstmNameAndArcrAcnameByArcrRegn(String flgtRegn, String flgtFlno) {
        List<MyFlightCode> mfList = fuelRecptMapper.getCstmNameAndArcrAcnameByArcrRegn(flgtRegn, flgtFlno);
        if (CollUtil.isNotEmpty(mfList)) {
            return mfList;
        }
        mfList = fuelRecptMapper.getCstmNameAndArcrAcnameByArcrRegn(flgtRegn, null);
        if (CollUtil.isNotEmpty(mfList)) {
            return mfList;
        }
        //如果飞机号码没有-则手动加上
        String newFlgtRegn = null;
        if (flgtRegn.startsWith("B") && flgtRegn.indexOf("-") == -1) {
            StringBuffer stringBuilder1 = new StringBuffer(flgtRegn);
            stringBuilder1.insert(1, "-");
            newFlgtRegn = stringBuilder1.toString();
            log.info("查询不到飞机号，带杠:" + newFlgtRegn);
        } else {
            //TODO
            String a = "^([A-Z]+)(\\d*.*)$";
            Pattern compile = Pattern.compile(a);
            Matcher matcher = compile.matcher(flgtRegn);
            if (matcher.matches()) {
                if (StringUtils.isNotEmpty(matcher.group(2)) && StringUtils.isNotEmpty(matcher.group(1))) {
                    newFlgtRegn = matcher.group(1) + "-" + matcher.group(2);
                } else if (StringUtils.isNotEmpty(matcher.group(1))) {
                    newFlgtRegn = matcher.group(1);
                }
                log.info("查询不到飞机号，带杠:" + newFlgtRegn);
            }
        }
        if (StrUtil.isNotBlank(newFlgtRegn)) {
            mfList = fuelRecptMapper.getCstmNameAndArcrAcnameByArcrRegn(newFlgtRegn, flgtFlno);
            if (CollUtil.isNotEmpty(mfList)) {
                return mfList;
            }
            mfList = fuelRecptMapper.getCstmNameAndArcrAcnameByArcrRegn(newFlgtRegn, null);
            if (CollUtil.isNotEmpty(mfList)) {
                return mfList;
            }
        }

        mfList = flightCodeTemporaryMapper.getCstmNameAndArcrAcnameByArcrRegnTemporary(flgtRegn, flgtFlno);
        if (CollUtil.isNotEmpty(mfList)) {
            return mfList;
        }
        mfList = flightCodeTemporaryMapper.getCstmNameAndArcrAcnameByArcrRegnTemporary(flgtRegn, null);
        if (CollUtil.isNotEmpty(mfList)) {
            return mfList;
        }
        if (StrUtil.isNotBlank(newFlgtRegn)) {
            mfList = flightCodeTemporaryMapper.getCstmNameAndArcrAcnameByArcrRegnTemporary(newFlgtRegn, flgtFlno);
            if (CollUtil.isNotEmpty(mfList)) {
                return mfList;
            }
            mfList = flightCodeTemporaryMapper.getCstmNameAndArcrAcnameByArcrRegnTemporary(newFlgtRegn, null);
            return mfList;
        }
        return mfList;
    }

    @Override
    public Pair<String, String> pingCountries(String flgtRegn, String flgtFlno, Boolean lock) {
        String flgtAcname = "未知";
        String flgtAlcname = "未知";
        try {
            //查询一下飞机厂商和飞机类型
            //拼接飞机号
            List<MyFlightCode> mfList = Lists.newArrayList();
            if (StringUtils.isNotBlank(flgtRegn) && flgtRegn.indexOf("-") > -1) {
                String replaceFlgtRegn = StrUtil.replace(flgtRegn, "-", StrUtil.EMPTY);
                mfList = getCstmNameAndArcrAcnameByArcrRegn(replaceFlgtRegn, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtRegn)) {
                mfList = getCstmNameAndArcrAcnameByArcrRegn(flgtRegn, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtFlno) && flgtFlno.indexOf("-") > -1) {
                String replaceFlgtFlno = StrUtil.replace(flgtFlno, "-", StrUtil.EMPTY);
                mfList = getCstmNameAndArcrAcnameByArcrRegn(replaceFlgtFlno, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtFlno)) {
                mfList = getCstmNameAndArcrAcnameByArcrRegn(flgtFlno, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList)) {
                return Pair.of(flgtAcname, flgtAlcname);
            }
            mfList.stream()
                    .sorted(Comparator.comparing((MyFlightCode item) -> item.getCnafUpdateTime(),
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            flgtAcname = mfList.get(0).getArcrName();//类型
            if (lock) {
                flgtAlcname = mfList.get(0).getCstmRegion() + "&" + mfList.get(0).getArcrCustomNum();//购买航空公司
            } else {
                flgtAlcname = mfList.get(0).getCstmRegion();
            }
            return Pair.of(flgtAcname, flgtAlcname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Pair.of(flgtAcname, flgtAlcname);
    }

    @Override
    public MyFlightCode getFlightCodeInfoByRegnAndFlno(String flgtRegn, String flgtFlno) {
        try {
            List<MyFlightCode> mfList = Lists.newArrayList();
            if (StringUtils.isNotBlank(flgtRegn) && flgtRegn.indexOf("-") > -1) {
                String replaceFlgtRegn = StrUtil.replace(flgtRegn, "-", StrUtil.EMPTY);
                mfList = getCstmNameAndArcrAcnameByArcrRegn(replaceFlgtRegn, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtRegn)) {
                mfList = getCstmNameAndArcrAcnameByArcrRegn(flgtRegn, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtFlno) && flgtFlno.indexOf("-") > -1) {
                String replaceFlgtFlno = StrUtil.replace(flgtFlno, "-", StrUtil.EMPTY);
                mfList = getCstmNameAndArcrAcnameByArcrRegn(replaceFlgtFlno, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList) && StringUtils.isNotBlank(flgtFlno)) {
                mfList = getCstmNameAndArcrAcnameByArcrRegn(flgtFlno, flgtFlno);
            }
            if (CollUtil.isEmpty(mfList)) {
                return null;
            }
            mfList.stream()
                    .sorted(Comparator.comparing((MyFlightCode item) -> item.getCnafUpdateTime(),
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            return mfList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MyTask> exportTasktSevenDateList(MyStaff staff) {  //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        //aaaa
        Date now = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim.format(beforeDate);
        if (nowStr.equals(beforeDatetr)) {
            nowStr = nowStr;
        } else {
            nowStr = beforeDatetr;
        }
        List<MyTask> tasktSevenDateList = taskMapper.getTasktSevenDateList(staff.getLoginUserIn().getStaffAirportCode(), null, nowStr);
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        for (MyTask myFlightTask : tasktSevenDateList) {
            for (MyStaff myStaff : staffList) {
                if (myFlightTask.getTaskOpeStaffId() != null && myStaff.getStaffId().equals(myFlightTask.getTaskOpeStaffId())) {
                    //赋值加油员姓名
                    myFlightTask.setTaskOpeStaffName(myStaff.getStaffName());
                }
                if (myFlightTask.getTaskCreStaffId() != null && myStaff.getStaffId().equals(myFlightTask.getTaskCreStaffId())) {
                    //赋值创建人员姓名
                    myFlightTask.setTaskCreStaffName(myStaff.getStaffName());
                }
            }
        }
        return tasktSevenDateList;
    }


    /*
        task.getTaskId()
     */
    @Override
    public ReturnMsg<Object> updateHangFlight(MyFlightTask task, MyStaff staff) {
        /*
            清空缓存油单号等信息
         */
        try {
            MyFuelRecpt myFuelRecpt = new MyFuelRecpt();
            myFuelRecpt.setFlrcNo("1");
            myFuelRecpt.setTaskId(task.getTaskId());
            Integer integer = tFuelNoService.recyclingFlueNoInsert(myFuelRecpt, staff);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyFlight myFlight = new MyFlight();
        myFlight.setFlgtId(task.getFlgtId());
        myFlight.setFlgtStatus(1);
        myFlight.setFlgtOil(task.getFlgtOil());
        myFlight.setFlgtHangTask(task.getTaskId());
        //挂起
        int i = flightMapper.updateFlight(myFlight);
        if (1 != i) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "挂起失败", null);
        }
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //根据任务ID查询任务单条信息
        MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
        //根据任务ID查出单条任务航班信息
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
        ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
        myFlightTasks.add(taskAndFlightById);
        List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
        if (myenum.size() > 0) {
            taskAndFlightById = myenum.get(0);
        }
        if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        }
        //判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            //把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        Integer type = flightService.pingSingleType(taskAndFlightById);
        taskInfos.setFlrcType(type);
        taskAndFlightById.setFlrcType(type);
        // 机场代码 + 油单类型
        MyStaff staffinfo = staffMapper.getStaffById(taskInfos.getTaskOpeStaffId());
        //创建一个人员车辆任务对象用来装要推送的人员信息
        taskInfos.setTaskOpeStaffName(staffinfo.getStaffName());
        taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        //把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfos);
        taskAndFlightById.setFlgtStatus(1);
        taskAndFlightById.setFlgtOil(task.getFlgtOil());
        webMap.put("flight", taskAndFlightById);
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        // 推送给调度员
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                Constant.PC_FLIGHT_UPDATE, webMap);

        taskAndFlightById.setTaskStatus(10);
        sendToKafkaTask(null, taskAndFlightById);

        /*SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                "111", webMap);*/
        return new ReturnMsg<Object>(Constant.CODE_OK, "挂起成功", null);
    }

    @Override
    @Transactional
    public ReturnMsg<Object> updateRestoreFlight(MyFlightTask task, MyStaff staff) {
        /*
            flgtId   taskId
         */
        MyFlight myFlight = new MyFlight();
        myFlight.setFlgtId(task.getFlgtId());
        myFlight.setFlgtStatus(2);
        //取消挂起
        int i = flightMapper.updateFlight(myFlight);
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //根据任务ID查询任务单条信息
        MyTask taskInfos = taskMapper.getTaskInfo(task.getTaskId());
        MyTask updateTask = new MyTask();
        updateTask.setTaskId(task.getTaskId());
        updateTask.setTaskStatus(1);
        updateTask.setTaskAccTime(null);
        updateTask.setTaskChagStaTime(null);
        updateTask.setTaskArriveTime(null);
        updateTask.setTaskChagEndTime(null);
        updateTask.setTaskVehiNo(null);
        updateTask.setTaskRcPrintTime(null);
        updateTask.setFlrcType(null);
            /*
                            task_status = #{task.taskStatus},
                          task_acc_time = #{task.task_acc_time},
                          task_chag_sta_time = #{task.task_chag_sta_time},
                          task_arrive_time = #{task.task_arrive_time},
                          task_rc_print_time = #{task.taskRcPrintTime},
                          task_chag_end_time = #{task.taskChagEndTime},
                          task_vehi_no = #{task.taskVehiNo}
             */

        int i1 = taskMapper.updateTask(updateTask);
        if (1 != i1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }

        //根据任务ID查出单条任务航班信息
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
        ArrayList<MyFlightTask> myFlightTasks = new ArrayList<MyFlightTask>();
        myFlightTasks.add(taskAndFlightById);
        List<MyFlightTask> myenum = TaskServiceImpl.myenum(myFlightTasks);
        if (myenum.size() > 0) {
            taskAndFlightById = myenum.get(0);
        }
        if (StringUtils.isEmpty(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        } else if ("XX".equals(taskAndFlightById.getFlgtFtyp()) || "UK".equals(taskAndFlightById.getFlgtFtyp())) {
            taskAndFlightById.setFlgtFtyp(taskAndFlightById.getaFlgtFtyp());
        }
        //判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            //把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        Integer type = flightService.pingSingleType(taskAndFlightById);
        taskInfos.setFlrcType(type);
        taskAndFlightById.setFlrcType(type);
        taskAndFlightById.setFlgtStatus(2);
        taskAndFlightById.setFlgtOil(task.getFlgtOil());
        //把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfos);
        webMap.put("flight", taskAndFlightById);
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        // 推送给调度员
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                Constant.PC_FLIGHT_UPDATE, webMap);
        SendMsg2Redis.testDingYue(stringRedisTemplate, taskInfos.getTaskOpeStaffId(), Constant.TASKFLIGHT, Constant.PAD_TASK_ISSUE, webMap);
        return new ReturnMsg<Object>(Constant.CODE_OK, "成功", null);
    }

    /**
     * 功能描述：获取加油客户编号（订单>临时飞机号码>飞机号码）
     *
     * @param flgtRegn            飞机号
     * @param flgtFlno            航班号
     * @param apcdCnafAirportCode 机场编码
     * @return com.zh.bean.flight.MyFlightCodeCust
     * @author zhaojiacan
     * @date 2024/4/17
     */
    @Override
    public MyFlightCodeCust getCustomerCode(String flgtRegn, String flgtFlno, String apcdCnafAirportCode) {
        MyFlightCodeCust myFlightCode = new MyFlightCodeCust();
        //if (StrUtil.isNotBlank(apcdCnafAirportCode)) {
        //    TAirportCode airportCode = myTAirportCodeMapper.selectByCnafAirportCode(apcdCnafAirportCode);
        //    TOrderInfo tOrderInfo = new TOrderInfo();
        //    tOrderInfo.setApc3(airportCode.getApcdIataCode());
        //    tOrderInfo.setFlno(flgtFlno);
        //    tOrderInfo.setRegn(flgtRegn);
        //    List<TOrderInfo> tOrderInfos = tOrderInfoMapper.selectOrderInfoByRegnAndNo(tOrderInfo);
        //    if (CollUtil.isNotEmpty(tOrderInfos)) {
        //        String cstno = tOrderInfos.get(0).getCstno();
        //        String cstnm = tOrderInfos.get(0).getCstnm();
        //        if (StringUtils.isNotEmpty(cstno)) {
        //            TCustom tCustom = customMapper.queryById(cstno);
        //            log.info("航班号{}，飞机号{}, 赋值订单客户编号{}，客户名称{}", flgtFlno, flgtRegn, cstno, cstnm);
        //            myFlightCode.setFlgtAlcname(Optional.ofNullable(tCustom).map(TCustom::getCstmName).orElse(cstnm));
        //            myFlightCode.setArcrCustomNum(cstno);
        //            return myFlightCode;
        //        }
        //    }
        //}
        Pair<String, String> pair = this.pingCountries(flgtRegn, flgtFlno, true);

        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(myFlightCode.getFlgtAcname()) && StrUtil.isNotBlank(pair.getLeft())) {
                myFlightCode.setFlgtAcname(pair.getLeft());//类型
            }
            String right = pair.getRight();
            if (StringUtils.isNotEmpty(right)) {
                String[] $s = right.split("&");
                if ($s.length == 2) {
                    myFlightCode.setFlgtAlcname($s[0]);//购买航空公司
                    myFlightCode.setArcrCustomNum($s[1]);
                }
            }
        }
        return myFlightCode;
    }

}
