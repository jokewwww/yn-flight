package com.zh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.*;
import com.zh.component.RedisComponent;
import com.zh.component.RedissonDistributedLocker;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.quartz.QuartzManager;
import com.zh.service.FlightService;
import com.zh.service.FuelRecptService;
import com.zh.service.TaskService;
import com.zh.util.ModelAssistant;
import com.zh.util.SendMsg2Redis;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private final static Logger log = LoggerFactory.getLogger(FlightServiceImpl.class);

    /**
     * 获取航班序号 分布式锁key
     */
    public final String FLIGHT_NUM_DISTRIBUTED_LOCK_KEY = "FLIGHT_NUM_LOCK_KEY";
    @Autowired
    Prop prop;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private MyFuelRecptMapper fuelRecptMapper;
    @Autowired
    private RedisComponent redis;
    @Autowired
    private AirlinesCodeMapper airlinesCodeMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private FuelRecptService fuelRecptService;
    @Autowired
    private TPlacecodeMapper placecodeMapper;
    @Autowired
    private TPlacecodeTypeMapper placecodeTypeMapper;
    @Autowired
    private TCreditInfoMapper tCreditInfoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${task_and_flight_lock}")
    private Boolean lock;
    @Value("${fuel_type_lock}")
    private Boolean fuelTypeLock;
    @Autowired
    private TaskService taskService;
    @Value("${staff_task_lock}")
    private Boolean staffTaskLock;
    @Autowired
    private TForeignairportCodeMapper tForeignairportCodeMapper;
    @Autowired
    private AirportCodeMapper airportCodeMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private MyStaffVehiTaskMapper vehiTaskMapper;
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private TOrderInfoMapper tOrderInfoMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MyTFlightCodeMapper myTFlightCodeMapper;
    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    /**
     * 去重复
     *
     * @param list
     * @return
     */
    public static List<MyFlightTask> mySort(List<MyFlightTask> list, Boolean lock) {
        LinkedHashMap<String, MyFlightTask> tempMap = new LinkedHashMap<>();
        for (MyFlightTask flightTask : list) {
            String key = flightTask.getFlgtId();
            // containsKey(Object key)
            // 该方法判断Map集合对象中是否包含指定的键名。如果Map集合中包含指定的键名，则返回true，否则返回false
            // containsValue(Object value)
            // value：要查询的Map集合的指定键值对象.如果Map集合中包含指定的键值，则返回true，否则返回false
            if (tempMap.containsKey(key)) {
                if (flightTask.getFlgtAStot() != null && tempMap.get(key).getFlgtAStot() != null
                        && flightTask.getFlgtAStot().after(tempMap.get(key).getFlgtAStot())) {
                    // HashMap是不允许key重复的，所以如果有key重复的话，那么前面的value会被后面的value覆盖
                    tempMap.put(key, flightTask);
                }
            } else {
                if (lock) {
                    if (StringUtils.isNotEmpty(flightTask.getFlgtFfid())) {
                        tempMap.put(key, flightTask);
                    }
                } else {
                    tempMap.put(key, flightTask);
                }
            }
        }
        List<MyFlightTask> tempList = new ArrayList<>();
        for (String key : tempMap.keySet()) {
            tempList.add(tempMap.get(key));
        }
        return tempList;
    }

    public static void main(String[] args) {

        //String s2 = "00ACD103";
        //String substring = s2.substring(s2.length() - 6, s2.length());
        //System.out.println(substring);

        System.out.println(DigestUtil.md5Hex(StrUtil.EMPTY, CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 获取航班量（总量，已离，未到，停场）
     */
    @Override
    public Map<String, Object> getFlightAmount(TStaff staff) {
        //创建一个map集合用来装查出来的数量
        Map<String, Object> flightAmountMap = new HashMap<String, Object>();
//		以航班日期=当前系统时间从DB查询出航班量_总数
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
        if (lock) {
            taskAndFlight = taskMapper.getTaskAndFlight(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr, null);
        } else {
            taskAndFlight = taskMapper.getTaskAndFlightLock(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr, null);
        }
        long ys = new Date().getTime() - starDate.getTime();

        Date starDate1 = new Date();
        taskAndFlight = mySort(taskAndFlight, false);
        List<MyFlight> aFlight = getAFlight(staff.getLoginUserIn().getStaffAirportCode());
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

//		Integer flightSum = flightMapper.getFlightSum(staff.getStaffAirportCode(),staff.getStaffAptareaCode());
        //以航班日期=当前系统，并且进离港=出港，实际起飞时间!=空为条件从DB查询出航班量_已离
        Integer leaveFlightSum = flightMapper.getleaveFlightSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以航班日期=当前系统，并且进离港=进港，实际到达时间!=空为条件从DB查询出航班量_未到
        Integer nonArrivalFlightSum = flightMapper.getnonArrivalFlightSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
//		以航班日期=当前系统，并且进离港=出港，实际起飞时间==空为条件从DB查询出航班量_停场
//		Integer stopFlightSum = flightMapper.getstopFlightSum(staff.getStaffAirportCode(),staff.getStaffAptareaCode());
        //  Integer stopFlightSum = flightMapper.getstopFlightSumNew(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        long count = taskAndFlight.stream().filter(o -> o.getFlgtDAtot() == null).count();
        //航班量_已离
        flightAmountMap.put("leaveFlightSum", leaveFlightSum);
        //航班量_未到
        flightAmountMap.put("nonArrivalFlightSum", nonArrivalFlightSum);
        //航班量_停场
        flightAmountMap.put("stopFlightSum", count);
        //航班量_总数
        flightAmountMap.put("flightSum", taskAndFlight.size());
        return flightAmountMap;
    }

    /**
     * 飞机信息接口
     * 取得当前在场航班信息，并将一进一出航班配对。
     */
    @Override
    public List<TFlightInfoList> getFlightList(TFlight flight) {
        //创建SimpleDateFormat日期格式化对象
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat flopform = new SimpleDateFormat("yyyy-MM-dd");
        List<TFlightInfoList> flightList = new ArrayList<TFlightInfoList>();
        //单进在场：以（所属机场代码＝输入_机场代码，航班日期＝系统日期，进离港＝进港，实际到达时间≠空）的条件，取得航班表（DB）中的对象记录
        List<TFlight> flightListTwo = flightMapper.getFlightListTwo(flight.getFlgtAirportCode());
        List<TFlight> flightListOne = flightMapper.getFlightList(flight.getFlgtAirportCode());
        flightListTwo.addAll(flightListOne);
        //遍历flightListTwo集合所有记录
        for (TFlight tFlightTwo : flightListTwo) {
            TFlight flightListThree = null;
            if (tFlightTwo.getFlgtLinkFlop() != null) {
                // 航班号等于 链接航班号 ，航班日期为系统日期 进离港为 出港 为条件查询
                flightListThree = flightMapper.getFlightListThree(tFlightTwo.getFlgtLinkFlno(),
                        flopform.format(tFlightTwo.getFlgtLinkFlop()), tFlightTwo.getFlgtLinkRepeat());
            }
            // 一进一出
            if (flightListThree != null) {
                if (null == flightListThree.getFlgtDAtot()) {
                    // 创建飞机信息接口对象
                    TFlightInfoList flightInfo = new TFlightInfoList();
                    // 往创建的飞机信息接口对象中赋值
                    flightInfo.setTakeOffFlightN(flightListThree.getFlgtFlno());
                    flightInfo.setAircraftID(flightListThree.getFlgtId());
                    flightInfo.setAircraftNum(flightListThree.getFlgtRegn());
                    flightInfo.setSeatID(flightListThree.getFlgtPlacecode());
                    flightInfo.setLandFlightN(tFlightTwo.getFlgtFlno());
                    flightInfo.setFarNear(flightListThree.getFlgtFnflag());
                    flightInfo.setModel(flightListThree.getFlgtAcname());
                    if (flightListThree.getFlgtVip() != null) {
                        flightInfo.setVip(flightListThree.getFlgtVip().toString());
                    }
                    flightInfo.setFlightState(flightListThree.getFlgtFtyp());
                    if (flightListThree.getFlgtDAtot() != null) {
                        flightInfo.setFlgtDAtot(sim.format(flightListThree.getFlgtDAtot()));
                    }
                    if (tFlightTwo.getFlgtAAtot() != null) {
                        flightInfo.setFlgtAAtot(sim.format(tFlightTwo.getFlgtAAtot()));
                    }
                    if (tFlightTwo.getFlgtAStot() != null) {
                        flightInfo.setFlgtAStot(sim.format(tFlightTwo.getFlgtAStot()));
                    }
                    if (flightListThree.getFlgtDStot() != null) {
                        flightInfo.setFlgtDStot(sim.format(flightListThree.getFlgtDStot()));
                    }
                    flightInfo.setCompanyName(flightListThree.getFlgtAlcname());
                    MyTask taskInfoById = flightMapper.getTaskInfoById(flightListThree.getFlgtId());
                    if (taskInfoById != null && taskInfoById.getTaskStatus() != null) {
                        flightInfo.setTaskStatus(taskInfoById.getTaskStatus().toString());
                    }
                    flightList.add(flightInfo);
                }
            } else {
                // 单进航班
                // 创建飞机信息接口对象
                TFlightInfoList flightInfo = new TFlightInfoList();
                // 往创建的飞机信息接口对象中赋值
                flightInfo.setTakeOffFlightN("");
                flightInfo.setAircraftID(tFlightTwo.getFlgtId());
                flightInfo.setAircraftNum(tFlightTwo.getFlgtRegn());
                flightInfo.setSeatID(tFlightTwo.getFlgtPlacecode());
                flightInfo.setLandFlightN(tFlightTwo.getFlgtFlno());
                flightInfo.setFarNear(tFlightTwo.getFlgtFnflag());
                flightInfo.setModel(tFlightTwo.getFlgtAcname());
                if (tFlightTwo.getFlgtVip() != null) {
                    flightInfo.setVip(tFlightTwo.getFlgtVip().toString());
                }
                flightInfo.setFlightState(tFlightTwo.getFlgtFtyp());

                flightInfo.setFlgtDAtot("");

                if (tFlightTwo.getFlgtAAtot() != null) {
                    flightInfo.setFlgtAAtot(sim.format(tFlightTwo.getFlgtAAtot()));
                }
                if (tFlightTwo.getFlgtAStot() != null) {
                    flightInfo.setFlgtAStot(sim.format(tFlightTwo.getFlgtAStot()));
                }

                flightInfo.setFlgtDStot("");

                flightInfo.setCompanyName(tFlightTwo.getFlgtAlcname());
                MyTask taskInfoById = flightMapper.getTaskInfoById(tFlightTwo.getFlgtId());
                if (taskInfoById != null && taskInfoById.getTaskStatus() != null) {
                    flightInfo.setTaskStatus(taskInfoById.getTaskStatus().toString());
                }
                flightList.add(flightInfo);
            }

        }
        return flightList;
    }

    /**
     * 手动创建航班的同时创建任务
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public MyFlightTask addFlightAndTask(MyFlight flight, MyStaff staff, Integer taskContent, Integer taskStatus) {
        //生成UUID为航班ID
        String flightIdD = UUID.randomUUID().toString();
        //生成UUID为航班ID
        String flightAId = UUID.randomUUID().toString();
        Integer isOrder = flight.getFlgtIsOrder();
        String flgtAcname = flight.getFlgtAcname();
        //通过飞机号获取飞机类型和航空公司
        if (StringUtils.isNotEmpty(flight.getFlgtRegn())) {
            Pair<String, String> pair = taskService.pingCountries(flight.getFlgtRegn(), flight.getFlgtFlno(), false);
            if (!"未知".equals(pair.getRight())) {
                if (StrUtil.isBlank(flgtAcname) && StringUtils.isNotEmpty((pair.getLeft()))) {
                    flight.setFlgtAcname(pair.getLeft());//类型
                }
                flight.setFlgtAlcname(pair.getRight());//购买航空公司
            }
        } else {
            throw new CustomException(ReturnMsg.getInstanceNGz("新增航班飞机号不可为空", null));
        }
        String flgtReg = flight.getFlgtRegn();
        String flgtFlno = flight.getFlgtFlno();
        log.info("新建航班获取飞机信息：飞机号-{}，航班号-{}", flgtReg, flgtFlno);
        //获取飞机号归属信息
        MyFlightCode myFlightCode = taskService.getFlightCodeInfoByRegnAndFlno(flgtReg, flgtFlno);
        if (StrUtil.isBlank(flgtAcname) && null != myFlightCode && StrUtil.isNotBlank(myFlightCode.getArcrAcname())) {
            flight.setFlgtAcname(myFlightCode.getArcrAcname());
        }

        taskContent = -1;
        MyFlightTask taskAndFlightById = null;
        Integer maxFlgtRepeat = 0;
        //+ 1
        //通过数据库查询最大的航班序号，自增+1
        Integer maxNum = this.getFlightMaxNum();
        //数据库查询航班连接次数，自增+1
        try {
            maxFlgtRepeat = flightMapper.findMaxFlgtRepeat();
            if (maxFlgtRepeat == null) {
                maxFlgtRepeat = 0;
            }
            if (maxFlgtRepeat == 0) {
                maxFlgtRepeat = 1;
            } else {
                maxFlgtRepeat = maxFlgtRepeat + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        String lockKey = new StringBuffer("flightAddLock:").append(DateUtil.today()).append(":")
                .append(StrUtil.blankToDefault(flight.getFlgtRegn(), StrUtil.EMPTY)).append("-")
                .append(StrUtil.blankToDefault(flight.getFlgtFlno(), StrUtil.EMPTY)).append("-")
                .append(StrUtil.blankToDefault(flight.getFlgtAdid(), StrUtil.EMPTY)).append("-")
                .append(StrUtil.blankToDefault(staff.getLoginUserIn().getStaffAirportCode(), StrUtil.EMPTY)).append("-")
                .append(StrUtil.blankToDefault(staff.getLoginUserIn().getStaffAptareaCode(), StrUtil.EMPTY)).toString();

        // 尝试获取锁，获取不到，立即返回结果
        if (!redissonDistributedLocker.tryLock(lockKey)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("航班创建失败，请勿重复操作，请稍后再试", null));
        }
        try {
            //根据航班号，航班日期，进离港检索航班表（DB）
            List<MyFlight> flightInfos = flightMapper.getFlightInfos(
                    flight, staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
            MyFlight flightInfo = CollUtil.isNotEmpty(flightInfos) ? flightInfos.get(0) : null;
            //判断如果查出来的航班对象如果等于NUll说明没有航班，就新建一个航班
            if (flightInfo == null) {
                if (!StringUtils.isBlank(flight.getFlgtAdid()) && "D".equals(flight.getFlgtAdid())) {
                    flight.setFlgtNum(maxNum);
                }
                //把UUID赋值到航班ID中
                flight.setFlgtId(flightIdD);
                //把UUID赋值到航班唯一标识中
                flight.setFlgtFfid(flightIdD);
                flight.setFlgtLinkFfid(flightAId);
                //往flight对象中的所属机场代码字段赋值
                flight.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
                //往flight对象中的所属机场区域代码字段赋值
                flight.setFlgtAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
                //判断航班性质是否为CGO 判断是否货运
                if ("CGO".equals(flight.getFlgtNature())) {
                    //如果是CGO的话就往flight对象中的是否为货机字段赋值Y
                    flight.setFlgtIfsr("Y");
                } else {
                    //如果不是CGO的话就往flight对象中的是否为货机字段赋值N
                    flight.setFlgtIfsr("N");
                }

                //判断 flgt_org3c 经停机场三字码  			//判断 flgt_org3c 出发地机场三字码
                if (StringUtils.isBlank(flight.getFlgtTrs3c1())) {
                    //把拼接好的航线赋值进航班的航线字段中
                    flight.setFlgtVialc(flight.getFlgtOrg3c() + "-" + flight.getFlgtDes3c());
                } else {
                    flight.setFlgtTrsnm1(flightMapper.getAirportName(flight.getFlgtTrs3c1()));
                    //把拼接好的航线赋值进航班的航线字段中
                    flight.setFlgtVialc(flight.getFlgtOrg3c() + "-" + flight.getFlgtTrs3c1() + "-" + flight.getFlgtDes3c());
                }
                //flight.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtTrs3c1()));
                //flight.setFlgtOrg3c(flight.getFlgtTrs3c1());ss
                flight.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtOrg3c()));
                flight.setFlgtOrg3c(flight.getFlgtOrg3c());
                // 根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
                flight.setFlgtDesnm(flightMapper.getAirportName(flight.getFlgtDes3c()));
                // 创建一个空字符串用来装航线机场全名称
                String flgtVialcName = null;
                // 创建一个空字符串用来装航线机场简称
                String flgtVialcNames = null;
                // 把航线按照-分割成每一个机场三字码
                String[] split = flight.getFlgtVialc().split("-");
                // for循环航线数组
                for (int i = 0; i < split.length; i++) {
                    // 循环航线，根据航线中的机场三字码查询每个机场
                    String airportName = flightMapper.getAirportName(split[i]);
                    // 循环航线，根据航线中的机场三字码查询每个机场简称
                    String airportNames = flightMapper.getAirportNamess(split[i]);
                    // 如果i等于0说明是第一次进来直接赋值就行
                    if (i == 0 && !StringUtils.isBlank(airportName)) {
                        flgtVialcName = airportName;
                    }
                    if (i == 0 && StringUtils.isBlank(airportName)) {
                        flgtVialcName = split[i];
                    }
                    // 如果i等于0说明是第一次进来直接赋值就行
                    if (i == 0 && !StringUtils.isBlank(airportNames)) {
                        flgtVialcNames = airportNames;
                    }
                    if (i == 0 && StringUtils.isBlank(airportNames)) {
                        flgtVialcNames = split[i];
                    }
                    if (i > 0 && i < split.length && StringUtils.isBlank(airportName)) {
                        flgtVialcName = flgtVialcName + "-" + split[i];
                    }
                    if (i > 0 && i < split.length && !StringUtils.isBlank(airportName)) {
                        flgtVialcName = flgtVialcName + "-" + airportName;
                    }
                    // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                    if (i > 0 && i < split.length && !StringUtils.isBlank(airportNames)) {
                        // 拼接简称
                        flgtVialcNames = flgtVialcNames + "-" + airportNames;
                    }
                    if (i > 0 && i < split.length && StringUtils.isBlank(airportNames)) {
                        // 拼接简称
                        flgtVialcNames = flgtVialcNames + "-" + split[i];
                    }
                }
                // 把拼接好的航线全称放进航班对象中
                flight.setFlgtTrsnm3(flgtVialcName);
                // 把拼接好的航线全称放进航班对象中
                flight.setFlgtTrsnm5(flgtVialcName);
                // 把拼接好的航线三字码放进航班对象中
                flight.setFlgtTrs3c5(flight.getFlgtVialc());
                // 把拼接完成的航线简称赋值到将要添加的航班中
                flight.setFlgtTrsnm4(flgtVialcNames);
                flight.setFlgtGame(Constant.FLGT_DGAME);
                //任务下发标识如果等于空就赋默认值0
                if (flight.getFlgtTaskAsign() == null) {
                    flight.setFlgtTaskAsign(0);
                }
                //航班星标如果等于空就赋默认值0
                if (flight.getFlgtStarmark() == null) {
                    flight.setFlgtStarmark(0);
                }
                //手动修改航班如果等于空就赋默认值0
                if (flight.getFlgtManualFlg() == null) {
                    flight.setFlgtManualFlg(0);
                }
                flight.setFlgtLinkFlno(flight.getFlgtFlno());
                flight.setFlgtLinkFlop(flight.getFlgtFlop());
                flight.setFlgtLinkRepeat(maxFlgtRepeat);
                flight.setFlgtRepeat(maxFlgtRepeat);
                flight.setFlgtFtyp("OT");
                flight.setFlgtAl2c(flight.getFlgtFlno().substring(0, 2));
                flight.setFlgtAlcname(flight.getFlgtAlcname());
                //根据飞机号码查询飞机类型
                flight.setFlgtAcname(flight.getFlgtAcname());
                flight.setFlgtIsOrder(isOrder);
                //添加航班信息,判断如果返回等于1说明添加成功，否则添加失败
                if (flightMapper.addFlight(flight) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                } else {
                    flightInfo = flight;
                }
                //判断航班进出港标识等于出港的话去添加进港航班
                if (Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid())) {
                    MyFlight flightA = new MyFlight();

                    //把UUID赋值到航班ID中
                    flightA.setFlgtId(flightAId);
                    //把UUID赋值到航班唯一标识中
                    flightA.setFlgtFfid(flightAId);
                    //往flight对象中的所属机场代码字段赋值
                    flightA.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
                    //往flight对象中的所属机场区域代码字段赋值
                    flightA.setFlgtAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
                    flightA.setFlgtFlno(flight.getFlgtFlno());
                    flightA.setFlgtFlop(flight.getFlgtFlop());
                    flightA.setFlgtLinkFlno(flight.getFlgtFlno());
                    flightA.setFlgtLinkFlop(flight.getFlgtFlop());
                    flightA.setFlgtLinkRepeat(maxFlgtRepeat);
                    flightA.setFlgtRepeat(maxFlgtRepeat);
                    flightA.setFlgtGame(Constant.FLGT_DGAME);
                    flightA.setFlgtRegn(flight.getFlgtRegn());
                    flightA.setFlgtAlcname(flight.getFlgtAlcname());
                    //根据飞机号码查询飞机类型
                    flightA.setFlgtAcname(flight.getFlgtAcname());
                    flightA.setFlgtAdid("A");
                    flightA.setFlgtTaskAsign(0);
                    flightA.setFlgtStarmark(0);
                    flightA.setFlgtManualFlg(0);
                    flightA.setFlgtFlop(flight.getFlgtFlop());
                    flightA.setFlgtFtyp("OT");
                    flightA.setFlgtAStot(flight.getFlgtAStot());
                    flightA.setFlgtAAtot(flight.getFlgtAAtot());
                    flightA.setFlgtAEtot(flight.getFlgtAEtot());
                    flightA.setFlgtIsOrder(isOrder);
                    flightA.setFlgtLinkFfid(flight.getFlgtFfid());
                    //添加进港航班信息
                    if (flightMapper.addFlight(flightA) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    }
                }
                //判断航班进出港标识等于进港的话去添加进港航班(进港也需要添加)
                MyFlight flightSave = null;
                if (Constant.CLEAR_A_ENTER.equals(flight.getFlgtAdid())) {
                    MyFlight flightA = new MyFlight();
                    //生成UUID为航班ID
                    //把UUID赋值到航班ID中
                    flightA.setFlgtId(flightAId);
                    //把UUID赋值到航班唯一标识中
                    flightA.setFlgtFfid(flightAId);
                    flightA.setFlgtLinkFfid(flightIdD);
                    //往flight对象中的所属机场代码字段赋值
                    flightA.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
                    //往flight对象中的所属机场区域代码字段赋值
                    flightA.setFlgtAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
                    flightA.setFlgtPlacecode(flight.getFlgtPlacecode());
                    flightA.setFlgtFlno(flight.getFlgtFlno());
                    flightA.setFlgtFlop(flight.getFlgtFlop());
                    flightA.setFlgtLinkFlno(flight.getFlgtFlno());
                    flightA.setFlgtLinkFlop(flight.getFlgtFlop());
                    flightA.setFlgtLinkRepeat(maxFlgtRepeat);
                    flightA.setFlgtFlti(flight.getFlgtFlti());
                    flightA.setFlgtRepeat(maxFlgtRepeat);
                    flightA.setFlgtGame(Constant.FLGT_DGAME);
                    flightA.setFlgtRegn(flight.getFlgtRegn());
                    flightA.setFlgtAlcname(flight.getFlgtAlcname());
                    //根据飞机号码查询飞机类型
                    flightA.setFlgtAcname(flight.getFlgtAcname());
                    flightA.setFlgtAdid("D");
                    flightA.setFlgtNature(flight.getFlgtNature());
                    flightA.setFlgtNum(maxNum);
                    //判断航班性质是否为CGO
                    if ("CGO".equals(flight.getFlgtNature())) {
                        //如果是CGO的话就往flight对象中的是否为货机字段赋值Y
                        flightA.setFlgtIfsr("Y");
                    } else {
                        //如果不是CGO的话就往flight对象中的是否为货机字段赋值N
                        flightA.setFlgtIfsr("N");
                    }
                    //判断 flgt_org3c 经停机场三字码  			//判断 flgt_org3c 出发地机场三字码
                    if (StringUtils.isBlank(flight.getFlgtTrs3c1())) {
                        //把拼接好的航线赋值进航班的航线字段中
                        flightA.setFlgtVialc(flight.getFlgtOrg3c() + "-" + flight.getFlgtDes3c());
                    } else {
                        flightA.setFlgtTrs3c1(flight.getFlgtTrs3c1());
                        flightA.setFlgtTrsnm1(flight.getFlgtTrsnm1());
                        //把拼接好的航线赋值进航班的航线字段中
                        flightA.setFlgtVialc(flight.getFlgtOrg3c() + "-" + flight.getFlgtTrs3c1() + "-" + flight.getFlgtDes3c());
                    }
                    //flight.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtTrs3c1()));
                    //flight.setFlgtOrg3c(flight.getFlgtTrs3c1());ss
                    flightA.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtOrg3c()));
                    flightA.setFlgtOrg3c(flight.getFlgtOrg3c());
                    // 根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
                    flightA.setFlgtDes3c(flight.getFlgtDes3c());
                    flightA.setFlgtDesnm(flightMapper.getAirportName(flight.getFlgtDes3c()));
                    // 创建一个空字符串用来装航线机场全名称
                    String flgtVialcName1 = null;
                    // 创建一个空字符串用来装航线机场简称
                    String flgtVialcNames1 = null;
                    // 把航线按照-分割成每一个机场三字码
                    String[] split1 = flight.getFlgtVialc().split("-");
                    // for循环航线数组------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                    for (int i = 0; i < split1.length; i++) {
                        // 循环航线，根据航线中的机场三字码查询每个机场
                        String airportName = flightMapper.getAirportName(split1[i]);
                        // 循环航线，根据航线中的机场三字码查询每个机场简称
                        String airportNames = flightMapper.getAirportNamess(split1[i]);
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportName)) {
                            flgtVialcName1 = airportName;
                        }
                        if (i == 0 && StringUtils.isBlank(airportName)) {
                            flgtVialcName1 = split1[i];
                        }
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportNames)) {
                            flgtVialcNames1 = airportNames;
                        }
                        if (i == 0 && StringUtils.isBlank(airportNames)) {
                            flgtVialcNames1 = split1[i];
                        }
                        if (i > 0 && i < split1.length && StringUtils.isBlank(airportName)) {
                            flgtVialcName1 = flgtVialcName1 + "-" + split1[i];
                        }
                        if (i > 0 && i < split1.length && !StringUtils.isBlank(airportName)) {
                            flgtVialcName1 = flgtVialcName1 + "-" + airportName;
                        }
                        // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                        if (i > 0 && i < split1.length && !StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames1 = flgtVialcNames1 + "-" + airportNames;
                        }
                        if (i > 0 && i < split1.length && StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames1 = flgtVialcNames1 + "-" + split1[i];
                        }
                    }
                    // 把拼接好的航线全称放进航班对象中
                    flightA.setFlgtTrsnm3(flgtVialcName1);
                    // 把拼接好的航线全称放进航班对象中
                    flightA.setFlgtTrsnm5(flgtVialcName1);
                    // 把拼接好的航线三字码放进航班对象中
                    flightA.setFlgtTrs3c5(flight.getFlgtVialc());
                    // 把拼接完成的航线简称赋值到将要添加的航班中
                    flightA.setFlgtTrsnm4(flgtVialcNames1);
                    flightA.setFlgtGame(Constant.FLGT_DGAME);
                    //任务下发标识如果等于空就赋默认值0
                    if (flight.getFlgtTaskAsign() == null) {
                        flightA.setFlgtTaskAsign(0);
                    }
                    //航班星标如果等于空就赋默认值0
                    if (flight.getFlgtStarmark() == null) {
                        flightA.setFlgtStarmark(0);
                    }
                    //手动修改航班如果等于空就赋默认值0
                    if (flight.getFlgtManualFlg() == null) {
                        flightA.setFlgtManualFlg(0);
                    }
                    flightA.setFlgtFtyp("OT");
                    flightA.setFlgtAl2c(flight.getFlgtFlno().substring(0, 2));
                    flightA.setFlgtAlcname(flightMapper.getCompanyName(flight.getFlgtAl2c()));
                    //根据飞机号码查询飞机类型
                    flightA.setFlgtAcname(flightMapper.getFlgtAcname(flight.getFlgtRegn()));

                    //放入  关于起飞的三个时间
                    flightA.setFlgtDStot(flight.getFlgtDStot());
                    flightA.setFlgtDAtot(flight.getFlgtDAtot());
                    flightA.setFlgtDEtot(flight.getFlgtDEtot());

                    flightA.setFlgtAStot(flight.getFlgtAStot());
                    flightA.setFlgtAAtot(flight.getFlgtAAtot());
                    flightA.setFlgtAEtot(flight.getFlgtAEtot());
                    flightA.setFlgtIsOrder(isOrder);

                    //添加进港航班信息
                    if (flightMapper.addFlight(flightA) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    } else {
                        flightSave = flightA;
                        flightInfo = flightA;
                    }
                }

                //航班进出港等于出港的时候才创建任务
                if ((Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid()) && taskStatus != 9)) {
                    //创建任务对象为添加任务做准备
                    MyTask task = new MyTask();
                    //生成UUID为任务ID
                    String taskId = UUID.randomUUID().toString();
                    //把生成的UUID赋值到任务对象中的任务ID里
                    task.setTaskId(taskId);
                    //把航班对象中的航班ID赋值到任务对象中的航班ID里
                    task.setTaskFlightId(flight.getFlgtId());
                    //把航班对象中的航班号赋值到任务对象中的航班号里
                    task.setTaskFlightNo(flight.getFlgtFlno());
                    //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                    task.setTaskAirportCode(flight.getFlgtAirportCode());
                    //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                    task.setTaskAptareaCode(flight.getFlgtAptareaCode());
                    //创建人ID赋值到任务对象中的创建人ID里
                    task.setTaskCreStaffId(staff.getLoginUserIn().getStaffId());
                    //如果任务内容等于空的话赋默认值0
                    if (taskContent == null) {
                        task.setTaskContent(-1);
                    } else {
                        //把任务内容赋值进任务对象的任务内容中
                        task.setTaskContent(taskContent);
                    }
                    //如果任务状态等于空的话赋默认值0
                    if (task.getTaskStatus() == null) {
                        task.setTaskStatus(0);
                    }
                    //如果任务星标等于空的话赋默认值0
                    if (task.getTaskStarmark() == null) {
                        task.setTaskStarmark(0);
                    }
                    //获取当前系统时间
                    Date date = new Date();
                    //创建SimpleDateFormat日期格式化对象
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //接收格式化以后的时间
                    String forMatTime = sim.format(date);
                    try {
                        //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                        task.setTaskRecCreTime(sim.parse(forMatTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                    if (flightMapper.addTask(task) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    } else {
                        //根据任务ID查出单条任务航班信息
                        taskAndFlightById = taskMapper.getTaskAndFlightByIds(task.getTaskId());
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("flight", taskAndFlightById);
                        //判断如果航班是本场的话再推送一条本场航班消息
                        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                            //把要推送的航班任务对象放进Map集合中
                            webMap.put("selfFlight", taskAndFlightById);
                        }
                    }
                } else if (flightSave != null) {
                    //创建任务对象为添加任务做准备
                    MyTask task = new MyTask();
                    //生成UUID为任务ID
                    String taskId = UUID.randomUUID().toString();
                    //把生成的UUID赋值到任务对象中的任务ID里
                    task.setTaskId(taskId);
                    //把航班对象中的航班ID赋值到任务对象中的航班ID里
                    task.setTaskFlightId(flightSave.getFlgtId());
                    //把航班对象中的航班号赋值到任务对象中的航班号里
                    task.setTaskFlightNo(flightSave.getFlgtFlno());
                    //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                    task.setTaskAirportCode(flightSave.getFlgtAirportCode());
                    //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                    task.setTaskAptareaCode(flightSave.getFlgtAptareaCode());
                    //创建人ID赋值到任务对象中的创建人ID里
                    task.setTaskCreStaffId(staff.getLoginUserIn().getStaffId());
                    //如果任务内容等于空的话赋默认值0
                    if (taskContent == null) {
                        task.setTaskContent(-1);
                    } else {
                        //把任务内容赋值进任务对象的任务内容中
                        task.setTaskContent(taskContent);
                    }
                    //如果任务状态等于空的话赋默认值0
                    if (task.getTaskStatus() == null) {
                        task.setTaskStatus(0);
                    }
                    //如果任务星标等于空的话赋默认值0
                    if (task.getTaskStarmark() == null) {
                        task.setTaskStarmark(0);
                    }
                    //获取当前系统时间
                    Date date = new Date();
                    //创建SimpleDateFormat日期格式化对象
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //接收格式化以后的时间
                    String forMatTime = sim.format(date);
                    try {
                        //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                        task.setTaskRecCreTime(sim.parse(forMatTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                    if (flightMapper.addTask(task) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    } else {
                        //根据任务ID查出单条任务航班信息
                        taskAndFlightById = taskMapper.getTaskAndFlightByIds(task.getTaskId());
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("flight", taskAndFlightById);
                        //判断如果航班是本场的话再推送一条本场航班消息
                        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                            //把要推送的航班任务对象放进Map集合中
                            webMap.put("selfFlight", taskAndFlightById);
                        }
                    }
                }
            }
            //判断如果查出来的航班对象如果不等于NUll说明有航班，再判断是否创建任务
            if (flightInfo != null) {
                //根据航班ID查任务信息
                MyTask taskInfoById = flightMapper.getTaskInfoById(flightInfo.getFlgtId());
                if (!StringUtils.isEmpty(flightInfo.getFlgtAdid()) && "A".equals(flightInfo.getFlgtAdid())) {
                    //查询对应的出港航班的 flgtId
                    String dFlgtId = flightMapper.selectInFlightIds(flightInfo.getFlgtAirportCode(), flightInfo.getFlgtAptareaCode(), flightInfo.getFlgtId());
                    if (!StringUtils.isEmpty(dFlgtId)) {
                        taskInfoById = flightMapper.getTaskInfoById(dFlgtId);
                    }
                }
                //如果不存在任务就创建任务
                if (taskInfoById == null) {
                    //航班进出港等于出港的时候才创建任务
                    if (Constant.CLEAR_A_PORT.equals(flightInfo.getFlgtAdid()) && taskStatus != 9) {
                        //创建任务对象为添加任务做准备
                        MyTask task = new MyTask();
                        //生成UUID为任务ID
                        String taskId = UUID.randomUUID().toString();
                        //把生成的UUID赋值到任务对象中的任务ID里
                        task.setTaskId(taskId);
                        //把航班对象中的航班ID赋值到任务对象中的航班ID里
                        task.setTaskFlightId(flight.getFlgtId());
                        //把航班对象中的航班号赋值到任务对象中的航班号里
                        task.setTaskFlightNo(flight.getFlgtFlno());
                        //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                        task.setTaskAirportCode(flight.getFlgtAirportCode());
                        //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                        task.setTaskAptareaCode(flight.getFlgtAptareaCode());
                        //创建人ID赋值到任务对象中的创建人ID里
                        task.setTaskCreStaffId(staff.getLoginUserIn().getStaffId());
                        //获取当前系统时间
                        Date date = new Date();
                        //创建SimpleDateFormat日期格式化对象
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //接收格式化以后的时间
                        String forMatTime = sim.format(date);
                        try {
                            //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                            task.setTaskRecCreTime(sim.parse(forMatTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //如果任务内容等于空的话赋默认值0
                        if (taskContent == null) {
                            task.setTaskContent(-1);
                        } else {
                            //把任务内容赋值进任务对象的任务内容中
                            task.setTaskContent(taskContent);
                        }
                        //如果任务状态等于空的话赋默认值0
                        if (task.getTaskStatus() == null) {
                            task.setTaskStatus(0);
                        }
                        //如果任务星标等于空的话赋默认值0
                        if (task.getTaskStarmark() == null) {
                            task.setTaskStarmark(0);
                        }
                        //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                        if (flightMapper.addTask(task) != 1) {
                            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                        } else {
                            //根据任务ID查出单条任务航班信息
                            taskAndFlightById = taskMapper.getTaskAndFlightByIds(task.getTaskId());
                            //TODO 为空先不查
//						if(taskAndFlightById!=null){
                            //把要推送的航班任务对象放进Map集合中
                            webMap.put("flight", taskAndFlightById);
                            //判断如果航班是本场的话再推送一条本场航班消息
                       /* if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                            //把要推送的航班任务对象放进Map集合中
                            webMap.put("selfFlight", taskAndFlightById);
                        }*/
//						}
                        }
                    } else {
                        //根据任务ID查出单条任务航班信息
                        taskAndFlightById = taskMapper.getTaskAndFlightByIds(taskInfoById.getTaskId());
                    }
                } else {
                    //根据任务ID查出单条任务航班信息
                    taskAndFlightById = taskMapper.getTaskAndFlightByIds(taskInfoById.getTaskId());
                }
            } else {
                System.out.println("=======航班手动新增=======flightInfo 为 null =======");
            }
            //垮库查询获取调度员ID
            List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
            String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
            System.out.println("Constant.PC_FLIGHT_ADD状态为8 推送信息 ---------> " + JSON.toJSONString(webMap));
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_ADD, webMap);
            try {
                if (taskAndFlightById != null) {
                    String substring = "";
                    if (StringUtils.isNotEmpty(taskAndFlightById.getFlgtFlno())) {
                        substring = taskAndFlightById.getFlgtFlno().substring(0, taskAndFlightById.getFlgtFlno().length() - 4);
                    }
                    MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(substring);
                    //6位取前2  7位取前3  减去后4位
                    //	String alcdArlnNames; //航空公司简称
                    if (myAirlinesCode != null && !StringUtils.isEmpty(myAirlinesCode.getAlcdArlnNameS())) {
                        taskAndFlightById.setAlcdArlnNames(myAirlinesCode.getAlcdArlnNameS());//简称
                    } else {
                        taskAndFlightById.setAlcdArlnNames("");
                    }

                    Pair<String, String> pair = taskService.pingCountries(taskAndFlightById.getFlgtRegn(), taskAndFlightById.getFlgtFlno(), true);
                    if (!"未知".equals(pair.getRight())) {
                        if (StrUtil.isBlank(taskAndFlightById.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                            taskAndFlightById.setFlgtAcname(pair.getLeft());//类型
                        }
                        String right = pair.getRight();
                        if (StringUtils.isNotEmpty(right)) {
                            String[] $s = right.split("&");
                            if ($s.length == 2) {
                                taskAndFlightById.setFlgtAlcname($s[0]);//购买航空公司
                                taskAndFlightById.setArcrCustomNum($s[1]);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            // 无条件释放锁
            redissonDistributedLocker.unlock(lockKey);
        }


        //Integer integer = pingSingleType(taskAndFlightById);
        //taskAndFlightById.setFlrcType(integer);
        return taskAndFlightById;
    }

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前1天，当天，后1天的航班所有信息
     */
    @Override
    public List<MyFlight> getFlightThreeDateList(MyStaff staff) {
        //取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前1天，当天，后1天的航班所有信息
        return flightMapper.getFlightThreeDateList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
    }

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前7天的航班所有信息
     */
    @Override
    public List<MyFlight> getFlightSevenDateList(MyStaff staff) {
        //取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前7天的航班所有信息
        return flightMapper.getFlightSevenDateList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
    }

    /**
     * 修改航班信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateFlightInfo(MyFlight flight, MyTask tasks) {
        String flgtId = flight.getFlgtId();
        //根据航班ID查询任务信息
        MyTask taskInfoById = flightMapper.getTaskInfoById(flgtId);
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(flight.getFlgtAirportCode());
        staffInfo.setStaffAptareaCode(flight.getFlgtAptareaCode());
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //根据航班ID查出航班单条记录
        MyFlightTask flightInfo = flightMapper.getFlightInfoById(flight.getFlgtId());
        // 计划到达时间为空，实际起飞时间为空
        if (flight.getFlgtDAtot() != null) {
            // 为本场航班
            flight.setFlgtGame(Constant.NO_FLGT_DGAME);
        }
        // 如果修改了航班的实际落地时间，是否加油，机位三项时，就把手动修改字段改为1手动修改
        if ((flight.getFlgtAAtot() != null && !flight.getFlgtAAtot().equals(flightInfo.getFlgtAAtot()))
                && (flight.getFlgtAdid() != null && !flight.getFlgtAdid().equals(flightInfo.getFlgtAdid()))
                && (flight.getFlgtPlacecode() != null
                && !flight.getFlgtPlacecode().equals(flightInfo.getFlgtPlacecode()))) {
            flight.setFlgtManualFlg(1);
        }
        // 更新时间
        // 如果传过来的时间在数据库之前则不允许更新
        Date flgtUpdateTime = flight.getFlgtUpdateTime();
        if (ObjectUtil.isNotNull(flgtUpdateTime)) {
            if (ObjectUtil.isNotNull(flightInfo.getFlgtUpdateTime())) {
                if (flightInfo.getFlgtUpdateTime().after(flgtUpdateTime)) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                }
            }
        } else {
            flight.setFlgtUpdateTime(new Date());
        }
        //更新航班信息,判断如果返回等于1说明更新成功，否则更新失败
        if (flightMapper.updateFlight(flight) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //判断航班如果从进港改为出港的话就是创建一条任务
        if (Constant.CLEAR_A_ENTER.equals(flightInfo.getFlgtAdid()) && Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid())) {
            //创建任务对象为添加任务做准备
            MyTask task = new MyTask();
            //生成UUID为任务ID
            String taskId = UUID.randomUUID().toString();
            //把生成的UUID赋值到任务对象中的任务ID里
            task.setTaskId(taskId);
            //把航班对象中的航班ID赋值到任务对象中的航班ID里
            task.setTaskFlightId(flight.getFlgtId());
            //把航班对象中的航班号赋值到任务对象中的航班号里
            task.setTaskFlightNo(flight.getFlgtFlno());
            //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
            task.setTaskAirportCode(flight.getFlgtAirportCode());
            //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
            task.setTaskAptareaCode(flight.getFlgtAptareaCode());
            //获取当前系统时间
            Date date = new Date();
            //创建SimpleDateFormat日期格式化对象
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //接收格式化以后的时间
            String forMatTime = sim.format(date);
            try {
                //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                task.setTaskRecCreTime(sim.parse(forMatTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //如果任务内容等于空的话赋默认值0
            if (task.getTaskContent() == null) {
                task.setTaskContent(0);
            }
            //如果任务状态等于空的话赋默认值0
            if (task.getTaskStatus() == null) {
                task.setTaskStatus(0);
            }
            //如果任务星标等于空的话赋默认值0
            if (task.getTaskStarmark() == null) {
                task.setTaskStarmark(0);
            }
            //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addTask(task) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
        }
        //判断修改之前的航班如果为进港的话说明已经有任务，就去修改任务中的航班号
        if (Constant.CLEAR_A_PORT.equals(flightInfo.getFlgtAdid())) {
            // 修改任务中的航班号
            if (flightMapper.updateTaskInfo(flight.getFlgtId(), flight.getFlgtFlno(), tasks.getTaskContent(),
                    tasks.getTaskStatus()) < 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        MyFlightTask taskAndFlightById = new MyFlightTask();
        if (taskInfoById != null) {
            // 根据任务ID查出单条任务航班信息
            taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfoById.getTaskId());
        } else {
            // 根据航班ID查出航班单条记录
            taskAndFlightById = flightMapper.getFlightInfoById(flight.getFlgtId());
        }
        taskAndFlightById.setFlgtUpdateTime(flight.getFlgtUpdateTime());
        //把要推送的航班任务对象放进Map集合中
        webMap.put("flight", taskAndFlightById);
        //判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            //把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffLists(taskAndFlightById.getFlgtAirportCode(), taskAndFlightById.getFlgtAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        // 推送给调度员
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE, webMap);
        // 推送给加油员
        // 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
        if (1 == taskAndFlightById.getTaskStatus() || 3 == taskAndFlightById.getTaskStatus() || 4 == taskAndFlightById.getTaskStatus() || 5 == taskAndFlightById.getTaskStatus()) {
            SendMsg2Redis.testDingYue(stringRedisTemplate, taskAndFlightById.getTaskOpeStaffId(), Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE, webMap);
        }
    }

    /**
     * 根据航班ID查询航班详细信息
     */
    @Override
    public MyFlightTask getFlightInfoById(MyFlight flight) {
        MyFlightTask flightInfoById = flightMapper.getFlightInfoById(flight.getFlgtId());
        //加油客户编号
        Pair<String, String> pair = taskService.pingCountries(flightInfoById.getFlgtRegn(), flightInfoById.getFlgtFlno(), true);
        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(flightInfoById.getFlgtAcname()) && StrUtil.isNotBlank(pair.getLeft())) {
                flightInfoById.setFlgtAlcname(pair.getLeft());//类型
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
        taskService.setAirLines(flightInfoById);
        flightInfoById.setFlgtVialc(flightInfoById.getFlgtTrsnm5());
        flightInfoById.setFlightValic(flightInfoById.getFlgtTrsnm4());
        if (flightInfoById != null) {
            List<MyFlightTask> objects = Lists.newArrayList();
            objects.add(flightInfoById);
            // TaskServiceImpl.myenum(objects);
            return objects.get(0);
        } else {
            return null;
        }

    }

    /**
     * 根据航班号查询航班详细信息
     */
    @Override
    public List<MyFlight> getFlightInfoByFlightNo(MyFlight flight) {

        if (!StringUtils.isEmpty(flight.getFlgtAirportCode()) && !StringUtils.isEmpty(flight.getFlgtFlno())) {
            return flightMapper.getFlightInfoByFlightNo(flight);
        }

        return null;
    }

    /**
     * 根据航班字段进行检索
     */
    @Override
    public List<MyFlight> getFlightListCondition(MyFlightCondition flight) {
        return flightMapper.getFlightListCondition(flight);
    }

    /**
     * pad航班新建
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void padAddFlightAndTask(MyFlightTask flight, MyStaff staff) {
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //创建一个String对象用来存储是否推送小时标识
        String flg = null;
        //创建SimpleDateFormat日期格式化对象
        SimpleDateFormat sims = new SimpleDateFormat("yyyy-MM-dd");
        //接收格式化以后的时间
        String forMatTimes = sims.format(flight.getFlgtFlop());


        //航班飞机号添加-
        //如果飞机号码没有-则手动加上
        try {
            String flgtRegn = flight.getFlgtRegn();
            if (StringUtils.isNotEmpty(flgtRegn)) {
                //如果飞机号码没有-则手动加上
                //if (flgtRegn.startsWith("B") && flgtRegn.indexOf("-") == -1) {
                //    StringBuffer stringBuilder1 = new StringBuffer(flgtRegn);
                //    stringBuilder1.insert(1, "-");
                //    flgtRegn = stringBuilder1.toString();
                //    System.out.println("飞机号:" + flgtRegn);
                //} else {
                //    //TODO
                //    String a = "^([A-Z]+)(\\d*.*)$";
                //    Pattern compile = Pattern.compile(a);
                //    Matcher matcher = compile.matcher(flgtRegn);
                //    if (matcher.matches()) {
                //        if (StringUtils.isNotEmpty(matcher.group(2)) && StringUtils.isNotEmpty(matcher.group(1))) {
                //            flgtRegn = matcher.group(1) + "-" + matcher.group(2);
                //        } else if (StringUtils.isNotEmpty(matcher.group(1))) {
                //            flgtRegn = matcher.group(1);
                //        }
                //    }
                //}
                flight.setFlgtRegn(flgtRegn);
                System.out.println("飞机号:" + flgtRegn);
            }
            /*if (!StringUtils.isEmpty(flight.getFlgtRegn()) && flight.getFlgtRegn().indexOf("-") == -1) {
                StringBuffer stringBuilder1 = new StringBuffer(flight.getFlgtRegn());
                stringBuilder1.insert(1, "-");

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据航班号和航班时间查询是否存在此航班
        MyFlight flightInfo = flightMapper.getFlightExist(flight.getFlgtFlno(), forMatTimes, flight.getFlgtRegn());
        //不存在，向航班表插入一条新记录，并生成新的任务信息
        if (flightInfo == null) {
            //创建一个新的航班对象
            MyFlight newFlight = new MyFlight();
            //生成UUID为航班ID
            String flightId = UUID.randomUUID().toString();
            //把UUID赋值到航班唯一标识中
            flight.setFlgtFfid(flightId);
            //把UUID赋值到航班ID中
            flight.setFlgtId(flightId);
            //往flight对象中的所属机场代码字段赋值
            flight.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            //往flight对象中的所属机场区域代码字段赋值
            flight.setFlgtAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
            //根据出发地机场三字码去DB中查出对应的出发地机场名称，赋值到flight对象中
            flight.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtOrg3c()));
            //根据经停备降机场三字码1去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm1(flightMapper.getAirportName(flight.getFlgtTrs3c1()));
            //根据经停备降机场三字码2去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm2(flightMapper.getAirportName(flight.getFlgtTrs3c2()));
            //根据经停备降机场三字码3去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm3(flightMapper.getAirportName(flight.getFlgtTrs3c3()));
            //根据经停备降机场三字码4去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm4(flightMapper.getAirportName(flight.getFlgtTrs3c4()));
            //根据经停备降机场三字码5去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm5(flightMapper.getAirportName(flight.getFlgtTrs3c5()));
            //根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
            flight.setFlgtDesnm(flightMapper.getAirportName(flight.getFlgtDes3c()));
            //根据航空公司二字码去DB中查出对应的航空公司名称，赋值到flight对象中
            flight.setFlgtAlcname(flightMapper.getCompanyName(flight.getFlgtAl2c()));
            //把出发地机场，经停备降机场，目的地机场使用-拼接
            String flgtVialc = flight.getFlgtOrg3c();
            //判断经停备降机场1不为空的话拼接进航线
            if (flight.getFlgtTrs3c1() != null && !"".equals(flight.getFlgtTrs3c1())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrs3c1();
            }
            //判断经停备降机场2不为空的话拼接进航线
            if (flight.getFlgtTrs3c2() != null && !"".equals(flight.getFlgtTrs3c2())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrs3c2();
            }
            //判断经停备降机场3不为空的话拼接进航线
            if (flight.getFlgtTrs3c3() != null && !"".equals(flight.getFlgtTrs3c3())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrs3c1();
            }
            //判断经停备降机场4不为空的话拼接进航线
            if (flight.getFlgtTrs3c4() != null && !"".equals(flight.getFlgtTrs3c4())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrs3c4();
            }
            //判断经停备降机场5不为空的话拼接进航线
            if (flight.getFlgtTrs3c5() != null && !"".equals(flight.getFlgtTrs3c5())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrs3c5();
            }
            //把目的地机场名拼接进航线
            flgtVialc = flgtVialc + "-" + flight.getFlgtDesnm();
            //把拼接好的航线赋值进航班的航线字段中
            flight.setFlgtVialc(flgtVialc);
            // 创建一个空字符串用来装航线机场全名称
            String flgtVialcName = null;
            // 把航线按照-分割成每一个机场三字码
            String[] split = flight.getFlgtVialc().split("-");
            // for循环航线数组
            for (int i = 0; i < split.length; i++) {
                // 循环航线，根据航线中的机场三字码查询每个机场
                String airportName = flightMapper.getAirportName(split[i]);
                // 如果i等于0说明是第一次进来直接赋值就行
                if (i == 0 && !StringUtils.isBlank(airportName)) {
                    flgtVialcName = airportName;
                }
                if (i == 0 && StringUtils.isBlank(airportName)) {
                    flgtVialcName = split[i];
                }
                // 如果i>0小于数组的长度的话说明是不是第一次进来就开始拼接
                if (i > 0 && i < split.length && !StringUtils.isBlank(airportName)) {
                    // 经停机场拼接
                    if (split.length == 3 && i == 1) {
                        flight.setFlgtTrs3c1(split[i]);
                        flight.setFlgtTrsnm1(airportName);
                    }
                    if (split.length == 4 && i == 2) {
                        flight.setFlgtTrs3c2(split[i]);
                        flight.setFlgtTrsnm2(airportName);
                    }
                    flgtVialcName = flgtVialcName + "-" + airportName;
                }
                if (i > 0 && i < split.length && StringUtils.isBlank(airportName)) {
                    flgtVialcName = flgtVialcName + "-" + split[i];
                }
            }
            // 把拼接好的航线全称放进航班对象中
            flight.setFlgtTrsnm3(flgtVialcName);
            flight.setFlgtGame(Constant.FLGT_DGAME);
            //把前台传过来的航班信息放到一个新的航班对象中
            newFlight.setFlgtId(flight.getFlgtId());
            newFlight.setFlgtFfid(flight.getFlgtFfid());
            newFlight.setFlgtAirportCode(flight.getFlgtAirportCode());
            newFlight.setFlgtAptareaCode(flight.getFlgtAptareaCode());
            newFlight.setFlgtFlno(flight.getFlgtFlno());
            newFlight.setFlgtFlop(flight.getFlgtFlop());
            newFlight.setFlgtAcname(flight.getFlgtAcname());
            newFlight.setFlgtRegn(flight.getFlgtRegn());
            newFlight.setFlgtPlacecode(flight.getFlgtPlacecode());
            newFlight.setFlgtAl2c(flight.getFlgtAl2c());
            newFlight.setFlgtAlcname(flight.getFlgtAlcname());
            newFlight.setFlgtVialc(flight.getFlgtVialc());
            newFlight.setFlgtAStot(flight.getFlgtAStot());
            newFlight.setFlgtAEtot(flight.getFlgtAEtot());
            newFlight.setFlgtAAtot(flight.getFlgtAAtot());
            newFlight.setFlgtDStot(flight.getFlgtDStot());
            newFlight.setFlgtDEtot(flight.getFlgtDEtot());
            newFlight.setFlgtDAtot(flight.getFlgtDAtot());
            newFlight.setFlgtOrg3c(flight.getFlgtOrg3c());
            newFlight.setFlgtOrgnm(flight.getFlgtOrgnm());
            newFlight.setFlgtTrs3c1(flight.getFlgtTrs3c1());
            newFlight.setFlgtTrsnm1(flight.getFlgtTrsnm1());
            newFlight.setFlgtTrs3c2(flight.getFlgtTrs3c2());
            newFlight.setFlgtTrsnm2(flight.getFlgtTrsnm2());
            newFlight.setFlgtTrs3c3(flight.getFlgtTrs3c3());
            newFlight.setFlgtTrsnm3(flight.getFlgtTrsnm3());
            newFlight.setFlgtTrs3c4(flight.getFlgtTrs3c4());
            newFlight.setFlgtTrsnm4(flight.getFlgtTrsnm4());
            newFlight.setFlgtTrs3c5(flight.getFlgtTrs3c5());
            newFlight.setFlgtTrsnm5(flight.getFlgtTrsnm5());
            newFlight.setFlgtDes3c(flight.getFlgtDes3c());
            newFlight.setFlgtDesnm(flight.getFlgtDesnm());
            newFlight.setFlgtAdid(flight.getFlgtAdid());
            newFlight.setFlgtFlti(flight.getFlgtFlti());
            newFlight.setFlgtFtyp(flight.getFlgtFtyp());
            newFlight.setFlgtProxy(flight.getFlgtProxy());
            newFlight.setFlgtLinkFlno(flight.getFlgtLinkFlno());
            newFlight.setFlgtFnflag(flight.getFlgtFnflag());
            newFlight.setFlgtGame(flight.getFlgtGame());
            newFlight.setFlgtChocksIn(flight.getFlgtChocksIn());
            newFlight.setFlgtChocksOut(flight.getFlgtChocksOut());
            newFlight.setFlgtVip(flight.getFlgtVip());
            //任务下发标识如果等于空就赋默认值0
            if (newFlight.getFlgtTaskAsign() == null) {
                newFlight.setFlgtTaskAsign(0);
            }
            //航班星标如果等于空就赋默认值0
            if (newFlight.getFlgtStarmark() == null) {
                newFlight.setFlgtStarmark(0);
            }
            //手动修改航班如果等于空就赋默认值0
            if (newFlight.getFlgtManualFlg() == null) {
                newFlight.setFlgtManualFlg(0);
            }
            //添加航班信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addFlight(newFlight) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            //航班进出港等于出港的时候才创建任务
            if (Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid()) && flight.getTaskStatus() != 9) {
                //创建任务对象为添加任务做准备
                MyTask task = new MyTask();
                //生成UUID为任务ID
                String taskId = UUID.randomUUID().toString();
                //把生成的UUID赋值到任务对象中的任务ID里
                task.setTaskId(taskId);
                //把航班对象中的航班ID赋值到任务对象中的航班ID里
                task.setTaskFlightId(newFlight.getFlgtId());
                //把航班对象中的航班号赋值到任务对象中的航班号里
                task.setTaskFlightNo(newFlight.getFlgtFlno());
                //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                task.setTaskAirportCode(newFlight.getFlgtAirportCode());
                //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                task.setTaskAptareaCode(newFlight.getFlgtAptareaCode());
                //获取当前系统时间
                Date date = new Date();
                //创建SimpleDateFormat日期格式化对象
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //接收格式化以后的时间
                String forMatTime = sim.format(date);
                try {
                    //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                    task.setTaskRecCreTime(sim.parse(forMatTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //如果任务内容等于空的话赋默认值0
                if (flight.getTaskContent() == null) {
                    task.setTaskContent(0);
                } else {
                    task.setTaskContent(flight.getTaskContent());
                }
                //如果任务状态等于空的话赋默认值0
                if (task.getTaskStatus() == null) {
                    task.setTaskStatus(0);
                }
                //如果任务星标等于空的话赋默认值0
                if (task.getTaskStarmark() == null) {
                    task.setTaskStarmark(0);
                }
                //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                if (flightMapper.addTask(task) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                } else {
                    //根据任务ID查出单条任务航班信息
                    MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
                    Integer integer = pingSingleType(taskAndFlightById);
                    taskAndFlightById.setFlrcType(integer);
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("flight", taskAndFlightById);
                    //判断如果航班是本场的话再推送一条本场航班消息
                    if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("selfFlight", taskAndFlightById);
                    }
                    flg = "1";
                }
            }
        } else {
            //创建一个新的航班对象
            MyFlight newFlight = new MyFlight();
            //把前台传过来的航班信息放到一个新的航班对象中
            newFlight.setFlgtId(flight.getFlgtId());
            newFlight.setFlgtFfid(flight.getFlgtFfid());
            newFlight.setFlgtAirportCode(flight.getFlgtAirportCode());
            newFlight.setFlgtAptareaCode(flight.getFlgtAptareaCode());
            newFlight.setFlgtFlno(flight.getFlgtFlno());
            newFlight.setFlgtFlop(flight.getFlgtFlop());
            newFlight.setFlgtAcname(flight.getFlgtAcname());
            newFlight.setFlgtRegn(flight.getFlgtRegn());
            newFlight.setFlgtPlacecode(flight.getFlgtPlacecode());
            newFlight.setFlgtAl2c(flight.getFlgtAl2c());
            newFlight.setFlgtAlcname(flight.getFlgtAlcname());
            newFlight.setFlgtVialc(flight.getFlgtVialc());
            newFlight.setFlgtAStot(flight.getFlgtAStot());
            newFlight.setFlgtAEtot(flight.getFlgtAEtot());
            newFlight.setFlgtAAtot(flight.getFlgtAAtot());
            newFlight.setFlgtDStot(flight.getFlgtDStot());
            newFlight.setFlgtDEtot(flight.getFlgtDEtot());
            newFlight.setFlgtDAtot(flight.getFlgtDAtot());
            newFlight.setFlgtOrg3c(flight.getFlgtOrg3c());
            newFlight.setFlgtOrgnm(flight.getFlgtOrgnm());
            newFlight.setFlgtTrs3c1(flight.getFlgtTrs3c1());
            newFlight.setFlgtTrsnm1(flight.getFlgtTrsnm1());
            newFlight.setFlgtTrs3c2(flight.getFlgtTrs3c2());
            newFlight.setFlgtTrsnm2(flight.getFlgtTrsnm2());
            newFlight.setFlgtTrs3c3(flight.getFlgtTrs3c3());
            newFlight.setFlgtTrsnm3(flight.getFlgtTrsnm3());
            newFlight.setFlgtTrs3c4(flight.getFlgtTrs3c4());
            newFlight.setFlgtTrsnm4(flight.getFlgtTrsnm4());
            newFlight.setFlgtTrs3c5(flight.getFlgtTrs3c5());
            newFlight.setFlgtTrsnm5(flight.getFlgtTrsnm5());
            newFlight.setFlgtDes3c(flight.getFlgtDes3c());
            newFlight.setFlgtDesnm(flight.getFlgtDesnm());
            newFlight.setFlgtAdid(flight.getFlgtAdid());
            newFlight.setFlgtFlti(flight.getFlgtFlti());
            newFlight.setFlgtFtyp(flight.getFlgtFtyp());
            newFlight.setFlgtProxy(flight.getFlgtProxy());
            newFlight.setFlgtLinkFlno(flight.getFlgtLinkFlno());
            newFlight.setFlgtFnflag(flight.getFlgtFnflag());
            newFlight.setFlgtGame(flight.getFlgtGame());
            newFlight.setFlgtChocksIn(flight.getFlgtChocksIn());
            newFlight.setFlgtChocksOut(flight.getFlgtChocksOut());
            newFlight.setFlgtVip(flight.getFlgtVip());
            //更新航班信息
            if (flightMapper.updateFlight(newFlight) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            //创建一个新的任务对象
            MyTask newTask = new MyTask();
            //把前台传过来的任务信息放到一个新的任务对象中
            newTask.setTaskId(flight.getTaskId());
            newTask.setTaskOpeStaffId(flight.getTaskOpeStaffId());
            newTask.setTaskContent(flight.getTaskContent());
            newTask.setTaskStatus(flight.getTaskStatus());
            newTask.setTaskAsgTime(flight.getTaskAsgTime());
            newTask.setTaskAccTime(flight.getTaskAccTime());
            newTask.setTaskChagStaTime(flight.getTaskChagStaTime());
            newTask.setTaskChagEndTime(flight.getTaskChagEndTime());
            newTask.setTaskDoneTime(flight.getTaskDoneTime());
            newTask.setTaskFuelRecptNo(flight.getTaskFuelRecptNo());
            newTask.setTaskVehiNo(flight.getTaskVehiNo());
            newTask.setTaskCreStaffId(flight.getTaskCreStaffId());
            newTask.setTaskStarmark(flight.getTaskStarmark());
            newTask.setTaskRecCreTime(flight.getTaskRecCreTime());
            //更新任务信息
            if (flightMapper.updateTask(newTask) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            //根据航班ID查询任务信息
            MyTask taskInfoById = flightMapper.getTaskInfoById(flight.getFlgtId());
            //根据任务ID查出单条任务航班信息
            MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfoById.getTaskId());

            Integer integer = pingSingleType(taskAndFlightById);
            taskAndFlightById.setFlrcType(integer);
            //把要推送的航班任务对象放进Map集合中
            webMap.put("flight", taskAndFlightById);
            //判断如果航班是本场的话再推送一条本场航班消息
            if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                //把要推送的航班任务对象放进Map集合中
                webMap.put("selfFlight", taskAndFlightById);
            }
            flg = "2";
        }
        List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));

        if ("1".equals(flg)) {
            System.out.println("Constant.PC_FLIGHT_ADD状态为8 推送信息 1390---------> " + JSON.toJSONString(webMap));
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PAD_FLIGHT_ADD,
                    webMap);
        }
        if ("2".equals(flg)) {
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE,
                    webMap);
        }
    }

    /**
     * pad任务申领
     * 根据航班号，航班时间查出航班ID，再根据航班ID更新任务表里的加油员ID和任务状态
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Map<String, Object> padUpdateTaskState(MyFlight flight, MyTask task) {
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //创建Map集合用来存储错误信息
        Map<String, Object> map = new HashMap<String, Object>();
        //创建SimpleDateFormat日期格式化对象
        SimpleDateFormat sims = new SimpleDateFormat("yyyy-MM-dd");
        //接收格式化以后的时间
        String forMatTimes = sims.format(flight.getFlgtFlop());
        //根据航班号和航班时间查询此航班
        MyFlight flightInfo = flightMapper.getFlightExist(flight.getFlgtFlno(), forMatTimes, null);
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(flightInfo.getFlgtAirportCode());
        staffInfo.setStaffAptareaCode(flightInfo.getFlgtAptareaCode());
        //根据航班ID查任务
        MyTask taskInfoById = flightMapper.getTaskInfoById(flightInfo.getFlgtId());
        //判断查出来的任务是否等于NULL，如果为空说明不存在任务，就创建任务
        if (taskInfoById == null) {
            //创建任务对象为添加任务做准备
            MyTask newTask = new MyTask();
            //生成UUID为任务ID
            String taskId = UUID.randomUUID().toString();
            //把生成的UUID赋值到任务对象中的任务ID里
            newTask.setTaskId(taskId);
            //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
            newTask.setTaskAirportCode(flightInfo.getFlgtAirportCode());
            //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
            newTask.setTaskAptareaCode(flightInfo.getFlgtAptareaCode());
            //创建人ID赋值到任务对象中的创建人ID里
            newTask.setTaskCreStaffId(task.getTaskOpeStaffId());
            //加油人ID赋值到任务对象中的加油人ID里
            newTask.setTaskOpeStaffId(task.getTaskOpeStaffId());
            //航班ID赋值到任务对象中的航班ID里
            newTask.setTaskFlightId(flightInfo.getFlgtId());
            //航班号赋值到任务对象中的航班号里
            newTask.setTaskFlightNo(flightInfo.getFlgtFlno());
            //获取当前系统时间
            Date date = new Date();
            //创建SimpleDateFormat日期格式化对象
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //接收格式化以后的时间
            String forMatTime = sim.format(date);
            try {
                //因为任务对象中的任务下发时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的任务下发时间中去
                newTask.setTaskAsgTime(sim.parse(forMatTime));
                //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                newTask.setTaskRecCreTime(sim.parse(forMatTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //如果任务内容等于空的话赋默认值0
            if (task.getTaskContent() == null) {
                newTask.setTaskContent(0);
            } else {
                newTask.setTaskContent(task.getTaskContent());
            }
            //如果任务状态等于空的话赋默认值0
            if (task.getTaskStatus() == null) {
                newTask.setTaskStatus(0);
            } else {
                newTask.setTaskStatus(task.getTaskStatus());
            }
            //如果任务星标等于空的话赋默认值0
            if (task.getTaskStarmark() == null) {
                newTask.setTaskStarmark(0);
            } else {
                newTask.setTaskStarmark(task.getTaskStarmark());
            }
            //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addTask(newTask) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            } else {
                //根据任务ID查询任务单条信息
                MyTask taskInfo = taskMapper.getTaskInfo(newTask.getTaskId());
                //把要推送的任务对象放进Map集合中
                webMap.put("task", taskInfo);
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(newTask.getTaskId());
                //把要推送的航班任务对象放进Map集合中
                webMap.put("flight", taskAndFlightById);
                //判断如果航班是本场的话再推送一条本场航班消息
                if (taskAndFlightById != null && Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                MyStaff staffinfo = staffMapper.getStaffById(newTask.getTaskOpeStaffId());
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把查出来的人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffinfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffinfo.getStaffPhone());
                staffTask.setStaffName(staffinfo.getStaffName());
                //根据加油员ID获取人员车辆表的信息
                MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());
                //判断如果人员车辆信息是否为空
                if (staffVehis != null) {
                    //不为空的话把车辆编号赋值进人员车辆任务对象中
                    staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                }
                //根据加油车编号查出对应的车辆信息
                MyVehi vehi = vehiTaskMapper.getVehiInfo(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
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
                MyTask taskInfos = fuelRecptMapper.gettaskById(staffinfo.getStaffId());
                //判断任务信息如果不为空
                if (taskInfos != null) {
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
                //把要推送的对象放进Map集合中
                webMap.put("staff", staffTask);
            }
            map.put("true", "任务申领成功");
            //否则就修改任务状态
        } else {
            //如果任务状态等于0的话说明未下发
            if (taskInfoById.getTaskStatus() == 0) {
                //获取当前系统时间
                Date date = new Date();
                //创建SimpleDateFormat日期格式化对象
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //接收格式化以后的时间
                String forMatTime = sim.format(date);
                //再根据任务ID更新任务表里的加油员ID和任务状态
                //更新任务信息
                if (flightMapper.updateTaskState(taskInfoById.getTaskId(), task.getTaskOpeStaffId(), task.getTaskStatus(), forMatTime) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                } else {
                    //根据任务ID查询任务单条信息
                    MyTask taskInfo = taskMapper.getTaskInfo(taskInfoById.getTaskId());
                    //把要推送的任务对象放进Map集合中
                    webMap.put("task", taskInfo);
                    //根据任务ID查出单条任务航班信息
                    MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfoById.getTaskId());
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("flight", taskAndFlightById);
                    //判断如果航班是本场的话再推送一条本场航班消息
                    if (taskAndFlightById != null && Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("selfFlight", taskAndFlightById);
                    }
                    MyStaff tasks = new MyStaff();
                    tasks.setStaffId(taskAndFlightById.getTaskOpeStaffId());
                    MyStaff staffinfo = staffMapper.getStaffById(taskAndFlightById.getTaskOpeStaffId());
                    //创建一个人员车辆任务对象用来装要推送的人员信息
                    MyStaffVehiTask staffTask = new MyStaffVehiTask();
                    //把查出来的人员ID赋值到人员车辆任务对象中
                    staffTask.setSfvhStaffId(staffinfo.getStaffId());
                    //把查出来的人员电话赋值到人员车辆任务对象中
                    staffTask.setStaffPhone(staffinfo.getStaffPhone());
                    staffTask.setStaffName(staffinfo.getStaffName());
                    MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());

                    //判断如果人员车辆信息是否为空
                    if (staffVehis != null) {
                        //不为空的话把车辆编号赋值进人员车辆任务对象中
                        staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                    }
                    MyVehi vehi = vehiTaskMapper.getVehiInfo(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
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
                    MyTask taskInfos = fuelRecptMapper.gettaskById(staffinfo.getStaffId());
                    //判断任务信息如果不为空
                    if (taskInfos != null) {
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
                    //把要推送的对象放进Map集合中
                    webMap.put("staff", staffTask);
                }
                map.put("true", "任务申领成功");
            } else {
                map.put("false", "任务申领失败，请确认任务状态");
            }
        }
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PAD_TASK_STATE_UPDATE, webMap);

        return map;
    }

    /**
     * pad航班新建和油单
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public String padAddFlightAndTaskAndOil(MyFlightTask flight, MyFuelRecpt fuelRecpt, MyStaff staff) {
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //创建一个String对象用来存储是否推送小时标识
        String flg = null;
        //创建SimpleDateFormat日期格式化对象
        SimpleDateFormat sims = new SimpleDateFormat("yyyy-MM-dd");
        //接收格式化以后的时间
        String forMatTimes = sims.format(flight.getFlgtFlop());
        //根据航班号和航班时间查询是否存在此航班
        MyFlight flightInfo = flightMapper.getFlightExist(flight.getFlgtFlno(), forMatTimes, flight.getFlgtRegn());
        //不存在，向航班表插入一条新记录，并生成新的任务信息
        if (flightInfo == null) {
            //创建一个新的航班对象
            MyFlight newFlight = new MyFlight();
            //生成UUID为航班ID
            String flightId = UUID.randomUUID().toString();
            //把UUID赋值到航班ID中
            flight.setFlgtId(flightId);
            //往flight对象中的所属机场代码字段赋值
            flight.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            //往flight对象中的所属机场区域代码字段赋值
            flight.setFlgtAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
            //根据出发地机场三字码去DB中查出对应的出发地机场名称，赋值到flight对象中
            flight.setFlgtOrgnm(flightMapper.getAirportName(flight.getFlgtOrg3c()));
            //根据经停备降机场三字码1去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm1(flightMapper.getAirportName(flight.getFlgtTrs3c1()));
            //根据经停备降机场三字码2去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm2(flightMapper.getAirportName(flight.getFlgtTrs3c2()));
            //根据经停备降机场三字码3去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm3(flightMapper.getAirportName(flight.getFlgtTrs3c3()));
            //根据经停备降机场三字码4去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm4(flightMapper.getAirportName(flight.getFlgtTrs3c4()));
            //根据经停备降机场三字码5去DB中查出对应的经停备降机场名称，赋值到flight对象中
            flight.setFlgtTrsnm5(flightMapper.getAirportName(flight.getFlgtTrs3c5()));
            //根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
            flight.setFlgtDesnm(flightMapper.getAirportName(flight.getFlgtDes3c()));
            //根据航空公司二字码去DB中查出对应的航空公司名称，赋值到flight对象中
            flight.setFlgtAlcname(flightMapper.getCompanyName(flight.getFlgtAl2c()));
            //把出发地机场，经停备降机场，目的地机场使用-拼接
            String flgtVialc = flight.getFlgtOrgnm();
            //判断经停备降机场1不为空的话拼接进航线
            if (flight.getFlgtTrsnm1() != null && !"".equals(flight.getFlgtTrsnm1())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrsnm1();
            }
            //判断经停备降机场2不为空的话拼接进航线
            if (flight.getFlgtTrsnm2() != null && !"".equals(flight.getFlgtTrsnm2())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrsnm2();
            }
            //判断经停备降机场3不为空的话拼接进航线
            if (flight.getFlgtTrsnm3() != null && !"".equals(flight.getFlgtTrsnm3())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrsnm3();
            }
            //判断经停备降机场4不为空的话拼接进航线
            if (flight.getFlgtTrsnm4() != null && !"".equals(flight.getFlgtTrsnm4())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrsnm4();
            }
            //判断经停备降机场5不为空的话拼接进航线
            if (flight.getFlgtTrsnm5() != null && !"".equals(flight.getFlgtTrsnm5())) {
                flgtVialc = flgtVialc + "-" + flight.getFlgtTrsnm5();
            }
            //把目的地机场名拼接进航线
            flgtVialc = flgtVialc + "-" + flight.getFlgtDesnm();
            //把拼接好的航线赋值进航班的航线字段中
            flight.setFlgtVialc(flgtVialc);
            flight.setFlgtGame(Constant.FLGT_DGAME);
            //把前台传过来的航班信息放到一个新的航班对象中
            newFlight.setFlgtId(flight.getFlgtId());
            newFlight.setFlgtFfid(flight.getFlgtFfid());
            newFlight.setFlgtAirportCode(flight.getFlgtAirportCode());
            newFlight.setFlgtAptareaCode(flight.getFlgtAptareaCode());
            newFlight.setFlgtFlno(flight.getFlgtFlno());
            newFlight.setFlgtFlop(flight.getFlgtFlop());
            newFlight.setFlgtAcname(flight.getFlgtAcname());
            newFlight.setFlgtRegn(flight.getFlgtRegn());
            newFlight.setFlgtPlacecode(flight.getFlgtPlacecode());
            newFlight.setFlgtAl2c(flight.getFlgtAl2c());
            newFlight.setFlgtAlcname(flight.getFlgtAlcname());
            newFlight.setFlgtVialc(flight.getFlgtVialc());
            newFlight.setFlgtAStot(flight.getFlgtAStot());
            newFlight.setFlgtAEtot(flight.getFlgtAEtot());
            newFlight.setFlgtAAtot(flight.getFlgtAAtot());
            newFlight.setFlgtDStot(flight.getFlgtDStot());
            newFlight.setFlgtDEtot(flight.getFlgtDEtot());
            newFlight.setFlgtDAtot(flight.getFlgtDAtot());
            newFlight.setFlgtOrg3c(flight.getFlgtOrg3c());
            newFlight.setFlgtOrgnm(flight.getFlgtOrgnm());
            newFlight.setFlgtTrs3c1(flight.getFlgtTrs3c1());
            newFlight.setFlgtTrsnm1(flight.getFlgtTrsnm1());
            newFlight.setFlgtTrs3c2(flight.getFlgtTrs3c2());
            newFlight.setFlgtTrsnm2(flight.getFlgtTrsnm2());
            newFlight.setFlgtTrs3c3(flight.getFlgtTrs3c3());
            newFlight.setFlgtTrsnm3(flight.getFlgtTrsnm3());
            newFlight.setFlgtTrs3c4(flight.getFlgtTrs3c4());
            newFlight.setFlgtTrsnm4(flight.getFlgtTrsnm4());
            newFlight.setFlgtTrs3c5(flight.getFlgtTrs3c5());
            newFlight.setFlgtTrsnm5(flight.getFlgtTrsnm5());
            newFlight.setFlgtDes3c(flight.getFlgtDes3c());
            newFlight.setFlgtDesnm(flight.getFlgtDesnm());
            newFlight.setFlgtAdid(flight.getFlgtAdid());
            newFlight.setFlgtFlti(flight.getFlgtFlti());
            newFlight.setFlgtFtyp(flight.getFlgtFtyp());
            newFlight.setFlgtProxy(flight.getFlgtProxy());
            newFlight.setFlgtLinkFlno(flight.getFlgtLinkFlno());
            newFlight.setFlgtFnflag(flight.getFlgtFnflag());
            newFlight.setFlgtGame(flight.getFlgtGame());
            newFlight.setFlgtChocksIn(flight.getFlgtChocksIn());
            newFlight.setFlgtChocksOut(flight.getFlgtChocksOut());
            newFlight.setFlgtVip(flight.getFlgtVip());
            //任务下发标识如果等于空就赋默认值0
            if (newFlight.getFlgtTaskAsign() == null) {
                newFlight.setFlgtTaskAsign(0);
            }
            //航班星标如果等于空就赋默认值0
            if (newFlight.getFlgtStarmark() == null) {
                newFlight.setFlgtStarmark(0);
            }
            //手动修改航班如果等于空就赋默认值0
            if (newFlight.getFlgtManualFlg() == null) {
                newFlight.setFlgtManualFlg(0);
            }
            //添加航班信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addFlight(newFlight) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            //航班进出港等于出港的时候才创建任务
            if (Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid())) {
                //创建任务对象为添加任务做准备
                MyTask task = new MyTask();
                //生成UUID为任务ID
                String taskId = UUID.randomUUID().toString();
                //把生成的UUID赋值到任务对象中的任务ID里
                task.setTaskId(taskId);
                //把航班对象中的航班ID赋值到任务对象中的航班ID里
                task.setTaskFlightId(newFlight.getFlgtId());
                //把航班对象中的航班号赋值到任务对象中的航班号里
                task.setTaskFlightNo(newFlight.getFlgtFlno());
                //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                task.setTaskAirportCode(newFlight.getFlgtAirportCode());
                //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                task.setTaskAptareaCode(newFlight.getFlgtAptareaCode());
                //获取当前系统时间
                Date date = new Date();
                //创建SimpleDateFormat日期格式化对象
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //接收格式化以后的时间
                String forMatTime = sim.format(date);
                try {
                    //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                    task.setTaskRecCreTime(sim.parse(forMatTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //如果任务内容等于空的话赋默认值0
                if (flight.getTaskContent() == null) {
                    task.setTaskContent(0);
                } else {
                    task.setTaskContent(flight.getTaskContent());
                }
                //如果任务状态等于空的话赋默认值0
                if (task.getTaskStatus() == null) {
                    task.setTaskStatus(0);
                }
                //如果任务星标等于空的话赋默认值0
                if (task.getTaskStarmark() == null) {
                    task.setTaskStarmark(0);
                }
                //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                if (flightMapper.addTask(task) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                } else {
                    //根据任务ID查出单条任务航班信息
                    MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("flight", taskAndFlightById);
                    //判断如果航班是本场的话再推送一条本场航班消息
                    if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("selfFlight", taskAndFlightById);
                    }
                    flg = "1";
                }
            }
        } else {
            //创建一个新的航班对象
            MyFlight newFlight = new MyFlight();
            //把前台传过来的航班信息放到一个新的航班对象中
            newFlight.setFlgtId(flight.getFlgtId());
            newFlight.setFlgtFfid(flight.getFlgtFfid());
            newFlight.setFlgtAirportCode(flight.getFlgtAirportCode());
            newFlight.setFlgtAptareaCode(flight.getFlgtAptareaCode());
            newFlight.setFlgtFlno(flight.getFlgtFlno());
            newFlight.setFlgtFlop(flight.getFlgtFlop());
            newFlight.setFlgtAcname(flight.getFlgtAcname());
            newFlight.setFlgtRegn(flight.getFlgtRegn());
            newFlight.setFlgtPlacecode(flight.getFlgtPlacecode());
            newFlight.setFlgtAl2c(flight.getFlgtAl2c());
            newFlight.setFlgtAlcname(flight.getFlgtAlcname());
            newFlight.setFlgtVialc(flight.getFlgtVialc());
            newFlight.setFlgtAStot(flight.getFlgtAStot());
            newFlight.setFlgtAEtot(flight.getFlgtAEtot());
            newFlight.setFlgtAAtot(flight.getFlgtAAtot());
            newFlight.setFlgtDStot(flight.getFlgtDStot());
            newFlight.setFlgtDEtot(flight.getFlgtDEtot());
            newFlight.setFlgtDAtot(flight.getFlgtDAtot());
            newFlight.setFlgtOrg3c(flight.getFlgtOrg3c());
            newFlight.setFlgtOrgnm(flight.getFlgtOrgnm());
            newFlight.setFlgtTrs3c1(flight.getFlgtTrs3c1());
            newFlight.setFlgtTrsnm1(flight.getFlgtTrsnm1());
            newFlight.setFlgtTrs3c2(flight.getFlgtTrs3c2());
            newFlight.setFlgtTrsnm2(flight.getFlgtTrsnm2());
            newFlight.setFlgtTrs3c3(flight.getFlgtTrs3c3());
            newFlight.setFlgtTrsnm3(flight.getFlgtTrsnm3());
            newFlight.setFlgtTrs3c4(flight.getFlgtTrs3c4());
            newFlight.setFlgtTrsnm4(flight.getFlgtTrsnm4());
            newFlight.setFlgtTrs3c5(flight.getFlgtTrs3c5());
            newFlight.setFlgtTrsnm5(flight.getFlgtTrsnm5());
            newFlight.setFlgtDes3c(flight.getFlgtDes3c());
            newFlight.setFlgtDesnm(flight.getFlgtDesnm());
            newFlight.setFlgtAdid(flight.getFlgtAdid());
            newFlight.setFlgtFlti(flight.getFlgtFlti());
            newFlight.setFlgtFtyp(flight.getFlgtFtyp());
            newFlight.setFlgtProxy(flight.getFlgtProxy());
            newFlight.setFlgtLinkFlno(flight.getFlgtLinkFlno());
            newFlight.setFlgtFnflag(flight.getFlgtFnflag());
            newFlight.setFlgtGame(flight.getFlgtGame());
            newFlight.setFlgtChocksIn(flight.getFlgtChocksIn());
            newFlight.setFlgtChocksOut(flight.getFlgtChocksOut());
            newFlight.setFlgtVip(flight.getFlgtVip());

            //更新航班信息
            if (flightMapper.updateFlight(newFlight) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            //创建一个新的任务对象
            MyTask newTask = new MyTask();
            //把前台传过来的任务信息放到一个新的任务对象中
            newTask.setTaskId(flight.getTaskId());
            newTask.setTaskOpeStaffId(flight.getTaskOpeStaffId());
            newTask.setTaskContent(flight.getTaskContent());
            newTask.setTaskStatus(flight.getTaskStatus());
            newTask.setTaskAsgTime(flight.getTaskAsgTime());
            newTask.setTaskAccTime(flight.getTaskAccTime());
            newTask.setTaskChagStaTime(flight.getTaskChagStaTime());
            newTask.setTaskChagEndTime(flight.getTaskChagEndTime());
            newTask.setTaskDoneTime(flight.getTaskDoneTime());
            newTask.setTaskFuelRecptNo(flight.getTaskFuelRecptNo());
            newTask.setTaskVehiNo(flight.getTaskVehiNo());
            newTask.setTaskCreStaffId(flight.getTaskCreStaffId());
            newTask.setTaskStarmark(flight.getTaskStarmark());
            newTask.setTaskRecCreTime(flight.getTaskRecCreTime());
            //更新任务信息
            if (flightMapper.updateTask(newTask) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                //根据任务ID查出单条任务航班信息
                MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(newTask.getTaskId());
                //把要推送的航班任务对象放进Map集合中
                webMap.put("flight", taskAndFlightById);
                //判断如果航班是本场的话再推送一条本场航班消息
                if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("selfFlight", taskAndFlightById);
                }
                flg = "2";
            }
        }
        //垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        if ("1".equals(flg)) {
            System.out.println("Constant.PC_FLIGHT_ADD状态为8 推送信息 --------->1911 " + JSON.toJSONString(webMap));
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PAD_FLIGHT_ADD,
                    webMap);
        }
        if ("2".equals(flg)) {
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE,
                    webMap);
        }
        if (flight.getTaskFuelRecptNo().equals(fuelRecpt.getFlrcNo())) {
            Date date = new Date();
            MyTask task = new MyTask();
            //生成UUID为油单ID
            String uuid = UUID.randomUUID().toString();
            fuelRecpt.setFlrcId(uuid);
            task.setTaskDoneTime(date);
            fuelRecptService.UpFuelRecpt(fuelRecpt, staff);
        } else {
            return "数据有问题！";
        }
        return null;
    }

    /**
     * PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场和前方起飞分开的
     */
    @Override
    public List<MyFlightTask> getFlightDateList(MyStaff staff) {
        //根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场和前方起飞分开的
        List<MyFlightTask> flightDateList = flightMapper.getFlightDateList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        /*for (MyFlightTask myFlightTask : flightDateList) {
            HttpHeaders headersb = new HttpHeaders();
            //跨库调用方法获取参数
            MyStaff staffinfo = staffMapper.getStaffById(myFlightTask.getTaskOpeStaffId());
            if (staffinfo != null) {
                //赋值加油员姓名
                myFlightTask.setTaskOpeStaffName(staffinfo.getStaffName());
            }
            //跨库调用方法获取参数
            MyStaff staffinfos = staffMapper.getStaffById(myFlightTask.getTaskCreStaffId());
            if (staffinfos != null) {
                //赋值创建人员姓名
                myFlightTask.setTaskCreStaffName(staffinfos.getStaffName());
            }
        }*/
        return flightDateList;
    }

    /**
     * 获取当天的本场航班的航班信息,按照预计起飞时间排序
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MyFlightTask> getFlightTaskDateThis(MyStaff staff) {
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
        //根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场查询,按照预计起飞时间排序
        List<MyFlightTask> flightTaskDateThis = flightMapper.selectFlightOnlyIn(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr);
        MyStaff staffAll = new MyStaff();
        staffAll.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffAll.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        //跨库调用方法获取参数
        List<MyStaff> staffinfo = staffMapper.getAllStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), 2);
        for (MyFlightTask myFlightTask : flightTaskDateThis) {

            // 赋值加油员姓名
            myFlightTask.setTaskOpeStaffName(
                    staffinfo.stream()
                            .filter(_staff -> StringUtils.equals(_staff.getStaffId(), myFlightTask.getTaskOpeStaffId()))
                            .map(MyStaff::getStaffName)
                            .findAny().orElse(null));

            // 赋值创建人员姓名
            myFlightTask.setTaskCreStaffName(staffinfo.stream()
                    .filter(_staff -> StringUtils.equals(_staff.getStaffId(), myFlightTask.getTaskCreStaffId()))
                    .map(MyStaff::getStaffName)
                    .findAny().orElse(null));
        }
        return flightTaskDateThis;
    }

    /**
     * 根据航班公司二字码获取航班公司名
     */
    @Override
    public Map<String, Object> getFlightName(MyAirlinesCode myAirlinesCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alcdArlnNameS", flightMapper.getCompanyName(myAirlinesCode.getAlcdIcaoCode()));
        return map;
    }

    /**
     * 根据机场三字码获取机场名
     */
    @Override
    public Map<String, Object> getFlightNames(MyAirportCode airportCode) {
        String companyName = flightMapper.getAirportName(airportCode.getApcdIataCode());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("airportName", companyName);
        return map;
    }

    /**
     * 根据航班号查询预创建航班表
     */
    @Override
    public List<MyFlightTemp> getFlightTemp(MyStaff staff, MyFlightTemp myFlightTemp) {
        return flightMapper.getFlightTemp(myFlightTemp.getFlgtFlno(), staff.getLoginUserIn().getStaffAirportCode());
    }

    /**
     * PAD 获取当天的航班的航班信息,按照预计起飞时间排序
     */
    @Override
    public List<MyFlightTask> getCurrentFlight(MyStaff staff) {
        //根据当前系统时间查询航班列表一起输出,按照预计起飞时间排序
        List<MyFlightTask> flightTaskDateThis = flightMapper.getCurrentFlight(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), staff.getLoginUserIn().getStaffId());
        return flightTaskDateThis;
    }

    /**
     * 根据机场码获取机场名（远程调用）
     */
    @Override
    public MyAirportCode getAirportName(MyAirportCode airportCode) {
        String companyName = flightMapper.getAirportNames(airportCode.getApcdIcaoCode());
        MyAirportCode airportCodeInfo = new MyAirportCode();
        airportCodeInfo.setApcdAirportName(companyName);
        return airportCodeInfo;
    }

    /**
     * Excele 导出
     */
    @Override
    public List<MyFlightTask> selectFlightDateList(MyStaff staff) {
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
        if (!lock) {
            taskAndFlight = flightMapper.selectFlightDateList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr);
        } else {
            taskAndFlight = flightMapper.selectFlightDateListLock(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), nowStr);
        }
        taskAndFlight = mySort(taskAndFlight, true);
        System.out.println(JSON.toJSONString(taskAndFlight));
        for (MyFlightTask myFlightTask : taskAndFlight) {
            if (myFlightTask.getFlgtFlno() != null && !"".equals(myFlightTask.getFlgtFlno())
                    && myFlightTask.getFlgtFlno().indexOf("/") > 0) {
                myFlightTask.setFlgtFlno(myFlightTask.getFlgtFlno().split("/")[0]);
            }
            log.debug(myFlightTask.getFlgtFlno());
            String flgtFlno = myFlightTask.getFlgtFlno();
            String flgtFlti = myFlightTask.getFlgtFlti();
            String flgtLinkFlno = myFlightTask.getFlgtLinkFlno();
            String flgtFltiIn = myFlightTask.getFlgtFltiIn();
            String flgtRegn = myFlightTask.getFlgtRegn();
            String flgtAl2c = myFlightTask.getFlgtAl2c();
            String flgtbezu = "";
            //判断是否是经停航班
            if (StringUtils.isNotEmpty(flgtFlno) && StringUtils.isNotEmpty(flgtLinkFlno)) {
                // 说明经停
                if (flgtFlno.equals(flgtLinkFlno)) {
                    if (StringUtils.isNotEmpty(flgtFltiIn)) {
                        //说明国际航班
                        if (!"D".equals(flgtFltiIn)) {
                            // 离境
                            flgtbezu = "离境";
                        }
                    }
                }
            }
            // 判断国内国外 0：加油，1：抽油 2补加油
            if (!"D".equals(flgtFlti)) {
                // 离境
                flgtbezu = "离境";
            }
            if (StringUtils.isNotEmpty(flgtAl2c)) {
                MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(flgtAl2c);
                if (null != myAirlinesCode && myAirlinesCode.getAlcdArlnNw() != null) {
                    if (1 == myAirlinesCode.getAlcdArlnNw()) {
                        //外航
                        flgtbezu = "外航";
                    }
                }

                TForeignairportCode tForeignairportCode = tForeignairportCodeMapper.selectByAlcdIcaoCode(flgtAl2c);
                if (null != tForeignairportCode) {
                    //外航
                    flgtbezu = "外航";
                }
            }
            //TODO 隆余逻辑
            if (StringUtils.isNotEmpty(flgtRegn)) {
                log.info("excel导出获取飞机信息：飞机号-{}，航班号-{}", flgtRegn, flgtFlno);
                //获取飞机号归属信息
                MyFlightCode myFlightCode = taskService.getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtFlno);

                if (myFlightCode != null) {
                    String cstmRegion = myFlightCode.getNewCstmRegion();  //购买航空公司
                    // TODO 优先处理特殊航班的情况
                    //判断是否包含CN
                    if (!cstmRegion.contains("CN")) {
                        //外航
                        flgtbezu = "外航";
                    }
                } else {
                    if ("B".equals(myFlightTask.getFlgtRegn().substring(0, 1))) {
                        // 台湾立荣航空公司
                        if ("B7".equals(myFlightTask.getFlgtAl2c())) {
                            flgtbezu = "外航";
                        }
                    } else {
                        flgtbezu = "外航";
                    }
                }
            } else {
                // TODO 昆明特殊处理
                if ("OD".equals(myFlightTask.getFlgtAl2c())) {
                    flgtbezu = "外航";
                }
                // TODO 昆明特殊处理
                if ("LQ".equals(myFlightTask.getFlgtAl2c())) {
                    flgtbezu = "外航";
                }
                // TODO 昆明特殊处理
                if ("Z2".equals(myFlightTask.getFlgtAl2c())) {
                    flgtbezu = "外航";
                }

                // 飞机号为空
                // flgtbezu = "飞机号为空无法判断";
            }
            myFlightTask.setFlgtbezu(flgtbezu);
        }
        return taskAndFlight;
    }

    /**
     * 查询飞机号码表
     */
    @Override
    public List<MyFlightCode> getFLIGHTCODE() {
        return flightMapper.getFLIGHTCODE();
    }

    /**
     * 查询航空加油客户表
     */
    @Override
    public List<MyCustom> getCUSTOM() {
        return flightMapper.getCUSTOM();
    }

    /**
     * 删除飞机号码表信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteFLIGHTCODE(MyFlightCode flightCode) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sim.format(flightCode.getArcrStartDate());
        if (flightMapper.deleteFLIGHTCODE(flightCode.getArcrRegn(), startDate) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 删除航空加油客户表信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteCUSTOM(MyCustom custom) {
        if (flightMapper.deleteCUSTOM(custom.getCstmNum()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 新增飞机号码表信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Integer addFLIGHTCODE(MyFlightCode flightCode) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sim.format(flightCode.getArcrStartDate());
        //添加前先根据飞机号和开始日期查询是否已经存在，不存在添加，已存在不添加
        MyFlightCode flightcodeFind = flightMapper.getFLIGHTCODEFind(flightCode.getArcrRegn(), startDate, flightCode.getFlno());
        if (flightcodeFind == null) {
            if (flightMapper.addFLIGHTCODE(flightCode) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 新增航空加油客户表信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Integer addCUSTOM(MyCustom custom) {
        //添加前先根据购货方编号查询是否已经存在，不存在添加，已存在不添加
        MyCustom customFind = flightMapper.getCUSTOMFind(custom.getCstmNum());
        if (customFind == null) {
            if (flightMapper.addCUSTOM(custom) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 修改飞机号码表信息
     */
    @Override
    public void updateFLIGHTCODE(MyFlightCode flightCode) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sim.format(flightCode.getArcrStartDate());
        if (flightMapper.updateFLIGHTCODE(flightCode, startDate) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 修改航空加油客户表信息
     */
    @Override
    public void updateCUSTOM(MyCustom custom) {
        if (flightMapper.updateCUSTOM(custom) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 查询飞机号码表详情
     */
    @Override
    public MyFlightCode getFLIGHTCODEFind(MyFlightCode flightCode) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sim.format(flightCode.getArcrStartDate());
        return flightMapper.getFLIGHTCODEFind(flightCode.getArcrRegn(), startDate, flightCode.getFlno());
    }

    /**
     * 查询航空加油客户表详情
     */
    @Override
    public MyCustom getCUSTOMFind(MyCustom custom) {
        return flightMapper.getCUSTOMFind(custom.getCstmNum());
    }

    /**
     * 获取规划数据
     */
    @Override
    public TProjectData getProjectData(MyFlight flight) {
        TProjectData projectData = flightMapper.getProjectData(flight.getFlgtFfid(), flight.getFlgtAirportCode(), flight.getFlgtAptareaCode());
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
        List<MyTask> taskEndInfo = taskMapper.getTaskEndInfo(flight.getFlgtAirportCode(), flight.getFlgtAptareaCode(), nowStr);
        MyStaff staff = new MyStaff();
        staff.setStaffAirportCode(flight.getFlgtAirportCode());
        staff.setStaffAptareaCode(flight.getFlgtAptareaCode());
        HttpHeaders header = new HttpHeaders();
        MediaType types = MediaType.parseMediaType("application/json; charset=UTF-8");
        header.setContentType(types);
        header.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MyStaff> formEntitys = new HttpEntity<MyStaff>(staff, header);
        //跨库查询后使用人员List
        ResponseEntity<List<MyStaff>> rateResponse = restTemplate.exchange(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffInfoList", HttpMethod.POST, formEntitys, new ParameterizedTypeReference<List<MyStaff>>() {
        });
        List<MyStaff> staffList = rateResponse.getBody();
        //用来装工作时长
        Map<String, Long> timeMap = new HashMap<String, Long>();
        //用来装完成的工作数量
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (MyStaff myStaff : staffList) {
            String AptareaCode = Constant.judgeAptareaCode(flight.getFlgtAptareaCode());
            if (null != redis.get(Constant.LOGIN_KEY + myStaff.getStaffId() + ":" + flight.getFlgtAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                timeMap.put(myStaff.getStaffId(), (Long) redis.get(Constant.LOGIN_KEY + myStaff.getStaffId() + ":" + flight.getFlgtAirportCode() + ":" + AptareaCode + ":" + "staffDateIng"));
            }
            Integer endCount = 0;
            for (MyTask myTask : taskEndInfo) {
                if (myTask.getTaskOpeStaffId() != null && myTask.getTaskOpeStaffId().equals(myStaff.getStaffId())) {
                    endCount = endCount + 1;
                }
            }
            countMap.put(myStaff.getStaffId(), endCount);
        }
        projectData.setWorkTime(timeMap);
        projectData.setTaskCount(countMap);
        return projectData;
    }

    /**
     * 航显页面修改航班信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateOnlyFlightInfo(MyFlight flight, MyStaff staff) {
        System.out.println("航显页面修改航班信息" + JSON.toJSONString(flight));
        //根据航班ID查询任务信息
        MyTask taskInfoById = flightMapper.getTaskInfoById(flight.getFlgtId());
        // 创建人员对象用来存放机场所属代码
   /*     MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());*/
        // 创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        // 根据航班ID查出航班单条记录
        MyFlightTask flightInfo = flightMapper.getFlightInfoById(flight.getFlgtId());
        // 根据航班ID查出关联航班的航班ID
        String inFlightID = flightMapper.selectInFlightID(staff.getLoginUserIn().getStaffAirportCode(),
                staff.getLoginUserIn().getStaffAptareaCode(), flight.getFlgtId());
        // 如果修改了航班的实际落地时间，进出港，机位三项时，就把手动修改字段改为1手动修改
        if ((flight.getFlgtAAtot() != null && !flight.getFlgtAAtot().equals(flightInfo.getFlgtAAtot()))
                || (flight.getFlgtAdid() != null && !flight.getFlgtAdid().equals(flightInfo.getFlgtAdid()))
                || (flight.getFlgtPlacecode() != null
                && !flight.getFlgtPlacecode().equals(flightInfo.getFlgtPlacecode()))) {
            flight.setFlgtManualFlg(1);
        }
        //修改航线
        if (StringUtils.isNotEmpty(flight.getFlgtVialc())) {
            flight.setFlgtTrsnm5(flight.getFlgtVialc());
            if (StringUtils.isNotEmpty(flight.getFlgtOrg3c())) {
                MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(flight.getFlgtOrg3c());
                if (myAirportCode != null) {
                    String flgtVialc = flight.getFlgtVialc();
                    String trsnm4 = flightInfo.getFlightValic();
                    String trs3c5 = flightInfo.getFlgtTrs3c5();
                    String airportName = "";
                    String airportNames = "";
                    if (StringUtils.isNotEmpty(myAirportCode.getApcdAirportName())) {
                        airportName = myAirportCode.getApcdAirportName();
                    }
                    if (StringUtils.isNotEmpty(myAirportCode.getApcdAirportNameS())) {
                        airportNames = myAirportCode.getApcdAirportNameS();
                    }
                    int result1 = flgtVialc.indexOf(airportName);
                    String latername = "";//出发地之后的航线
                    String beforenames = "";//出发地及之前的简称
                    String beforecode = "";//出发地及之前的三字码
                    if (result1 != -1) {//出发地中文在航线中
                        if (StringUtils.isNotEmpty(airportName)) {
                            Matcher matcher = Pattern.compile(airportName).matcher(flgtVialc);//截取航线
                            if (matcher.find()) {
                                if (flgtVialc.length() > (matcher.start() + airportName.length())) {
                                    latername = flgtVialc.substring(matcher.start() + airportName.length() + 1);
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(airportNames)) {
                            Matcher matchertrsnm4 = Pattern.compile(airportNames).matcher(trsnm4);//截取简称
                            if (matchertrsnm4.find()) {
                                if (trsnm4.length() > (matchertrsnm4.start() + airportNames.length())) {
                                    beforenames = trsnm4.substring(0, matchertrsnm4.start() + airportNames.length());
                                } else {
                                    beforenames = trsnm4;
                                }
                            } else {
                                beforenames = trsnm4;
                            }
                        }
                        if (StringUtils.isNotEmpty(trs3c5)) {
                            Matcher matchertrs3c5 = Pattern.compile(flight.getFlgtOrg3c()).matcher(trs3c5);//截取三字码
                            if (matchertrs3c5.find()) {
                                if (trs3c5.length() > (matchertrs3c5.start() + flight.getFlgtOrg3c().length())) {
                                    beforecode = trs3c5.substring(0, matchertrs3c5.start() + flight.getFlgtOrg3c().length());
                                } else {
                                    beforecode = trs3c5;
                                }
                            } else {
                                beforecode = trs3c5;
                            }
                        }
                        //当前机场之后的航线进行截取并查找对应的简称三字码
                        if (latername.length() > 0) {
                            String[] names = latername.split("-");
                            for (int i = 0; i < names.length; i++) {
                                //通过名称去查询机场
                                MyAirportCode myAirportByName = airportCodeMapper.selectAirportByName(names[i]);
                                if (myAirportByName != null) {
                                    beforenames += "-" + myAirportByName.getApcdAirportNameS();
                                    beforecode += "-" + myAirportByName.getApcdIataCode();
                                } else {
                                    beforenames += "-" + names[i];
                                    beforecode += "-" + names[i];
                                }
                            }
                            //目的机场赋值
                            if (names.length > 0) {
                                MyAirportCode lastAirport = airportCodeMapper.selectAirportByName(names[names.length - 1]);
                                if (lastAirport != null) {
                                    flight.setFlgtDes3c(lastAirport.getApcdIataCode());
                                    flight.setFlgtDesnm(lastAirport.getApcdAirportName());
                                }
                            }
                            //简称，三字码赋值
                            flight.setFlgtTrsnm4(beforenames);
                            flight.setFlgtTrs3c5(beforecode);
                        }
                    } else {//出发地中文不在在航线中直接截取并赋值
                        String[] names = flgtVialc.split("-");
                        for (int i = 0; i < names.length; i++) {
                            //通过名称去查询机场
                            MyAirportCode myAirportByName = airportCodeMapper.selectAirportByName(names[i]);
                            if (myAirportByName != null) {
                                beforenames += "-" + myAirportByName.getApcdAirportNameS();
                                beforecode += "-" + myAirportByName.getApcdIataCode();
                            } else {
                                beforenames += "-" + names[i];
                                beforecode += "-" + names[i];
                            }
                            beforenames = beforenames.substring(1);
                            beforecode = beforecode.substring(1);
                        }
                        //目的机场赋值
                        if (names.length > 0) {
                            MyAirportCode lastAirport = airportCodeMapper.selectAirportByName(names[names.length - 1]);
                            if (lastAirport != null) {
                                flight.setFlgtDes3c(lastAirport.getApcdIataCode());
                                flight.setFlgtDesnm(lastAirport.getApcdAirportName());
                            }
                        }
                        //简称，三字码赋值
                        flight.setFlgtTrsnm4(beforenames);
                        flight.setFlgtTrs3c5(beforecode);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(flight.getFlgtMissionProp())) {
            if (flight.getFlgtMissionProp().contains("-")) {
                String[] split = flight.getFlgtMissionProp().split("-");
                if (split.length > 0) {
                    flight.setFlgtMissionProp(compileEnum(split[split.length - 1]));
                }
            }
        }
        flight.setFlgtUpdateTime(new Date());
        // 更新出港航班信息,判断如果返回等于1说明更新成功，否则更新失败
        if (flightMapper.updateFlight(flight) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        if (!StringUtils.isBlank(inFlightID)) {
            // 进港航班情报
            MyFlight inFlight = new MyFlight();
            inFlight.setFlgtAAtot(flight.getFlgtAAtot());
            inFlight.setFlgtAEtot(flight.getFlgtAEtot());
            inFlight.setFlgtAStot(flight.getFlgtAStot());
            inFlight.setFlgtId(inFlightID);
            inFlight.setFlgtPlacecode(flight.getFlgtPlacecode());
            inFlight.setFlgtAcname(flight.getFlgtAcname());
            inFlight.setFlgtRegn(flight.getFlgtRegn());
            inFlight.setFlgtGame(flight.getFlgtGame());
            inFlight.setFlgtUpdateTime(flight.getFlgtUpdateTime());

            // 更新出港航班信息,判断如果返回等于1说明更新成功，否则更新失败
            if (flightMapper.updateInFlightInfo(inFlight) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        // 判断航班如果从进港改为出港的话就是创建一条任务
        if (Constant.CLEAR_A_ENTER.equals(flightInfo.getFlgtAdid())
                && Constant.CLEAR_A_PORT.equals(flight.getFlgtAdid())) {
            // 创建任务对象为添加任务做准备
            MyTask task = new MyTask();
            // 生成UUID为任务ID
            String taskId = UUID.randomUUID().toString();
            // 把生成的UUID赋值到任务对象中的任务ID里
            task.setTaskId(taskId);
            // 把航班对象中的航班ID赋值到任务对象中的航班ID里
            task.setTaskFlightId(flight.getFlgtId());
            // 把航班对象中的航班号赋值到任务对象中的航班号里
            task.setTaskFlightNo(flight.getFlgtFlno());
            // 把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
            task.setTaskAirportCode(flight.getFlgtAirportCode());
            // 把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
            task.setTaskAptareaCode(flight.getFlgtAptareaCode());
            // 获取当前系统时间
            Date date = new Date();
            // 创建SimpleDateFormat日期格式化对象
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 接收格式化以后的时间
            String forMatTime = sim.format(date);
            try {
                // 因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                task.setTaskRecCreTime(sim.parse(forMatTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 如果任务内容等于空的话赋默认值0
            if (task.getTaskContent() == null) {
                task.setTaskContent(0);
            }
            // 如果任务状态等于空的话赋默认值0
            if (task.getTaskStatus() == null) {
                task.setTaskStatus(0);
            }
            // 如果任务星标等于空的话赋默认值0
            if (task.getTaskStarmark() == null) {
                task.setTaskStarmark(0);
            }
            // 添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
            if (flightMapper.addTask(task) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
        }
        // 判断修改之前的航班如果为进港的话说明已经有任务，就去修改任务中的航班号
        if (Constant.CLEAR_A_PORT.equals(flightInfo.getFlgtAdid())) {
            // 修改任务中的航班号
            flightMapper.updateTaskInfo(flight.getFlgtId(), flight.getFlgtFlno(), null, null);
        }
	/*	MyFlightTask taskAndFlightById = flightMapper.getFlightInfoById(flight.getFlgtId());
		// 把要推送的航班任务对象放进Map集合中
		webMap.put("flight", taskAndFlightById);
		// 判断如果航班是本场的话再推送一条本场航班消息
		if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
			// 把要推送的航班任务对象放进Map集合中
			webMap.put("selfFlight", taskAndFlightById);
		}*/
        MyTask taskInfo = new MyTask();
        MyFlightTask taskAndFlightById = new MyFlightTask();
        if (taskInfoById != null) {
            //根据任务ID查询任务单条信息
            taskInfo = taskMapper.getTaskInfo(taskInfoById.getTaskId());
            //根据任务ID查出单条任务航班信息
            taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfoById.getTaskId());
            if (taskAndFlightById != null) {
                taskAndFlightById.setFlgtUpdateTime(flight.getFlgtUpdateTime());
            }
        }
        MyStaff taskss = new MyStaff();
        taskss.setStaffId(taskInfo.getTaskOpeStaffId());
        MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
        if (staffinfo != null) {
            taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
            taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        }

        Integer flrcType = null;
        flrcType = pingSingleType(taskAndFlightById);
        //把判断好的油单类型放进推送的任务对象中
        taskInfo.setFlrcType(flrcType);
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        MyStaffVehi staffVehis = null;
        if (staffinfo != null) {
            //把查出来的人员ID赋值到人员车辆任务对象中
            staffTask.setSfvhStaffId(staffinfo.getStaffId());
            //把查出来的人员电话赋值到人员车辆任务对象中
            staffTask.setStaffPhone(staffinfo.getStaffPhone());
            staffTask.setStaffName(staffinfo.getStaffName());
            staffVehis = staffMapper.getStaffVehiInfo(staffinfo.getStaffId());
        }
        MyVehi vehi = null;
        //判断如果人员车辆信息是否为空
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
        if (staffinfo != null) {
            //根据人员ID查询出单条任务信息
            MyTask taskInfos = fuelRecptMapper.gettaskById(staffinfo.getStaffId());
            //判断任务信息如果不为空
            if (taskInfos != null) {
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
        }
        //把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfo);
        taskAndFlightById.setFlrcType(flrcType);
        if (null == taskAndFlightById.getFlgtAAtot() && null != taskAndFlightById.getdFlgtAAtot()) {
            taskAndFlightById.setFlgtAAtot(taskAndFlightById.getdFlgtAAtot());
        }
        //把要推送的航班任务对象放进Map集合中
        webMap.put("flight", taskAndFlightById);
        //把要推送的对象放进Map集合中
        webMap.put("staff", staffTask);
        //判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            //把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        // 垮库查询获取调度员ID
       /* HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MyStaff> formEntityss = new HttpEntity<MyStaff>(staffInfo, headers);
        // 跨库查询后使用人员List接受
        ResponseEntity<List<MyStaff>> rateResponse = restTemplate.exchange(
                Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffLists",
                HttpMethod.POST, formEntityss, new ParameterizedTypeReference<List<MyStaff>>() {
                });*/

        List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = "";
        int i = 0;
        for (MyStaff myStaff : staffList) {
            if (i == 0) {
                userId = myStaff.getStaffId();
            }
            if (i > 0) {
                userId = userId + "," + myStaff.getStaffId();
            }
            i++;
        }
        // 推送给调度员
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE, webMap);

        // 推送给加油员
        // 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
        if (taskAndFlightById != null && null != taskAndFlightById.getTaskStatus()) {
            if (1 == taskAndFlightById.getTaskStatus() || 3 == taskAndFlightById.getTaskStatus() || 4 == taskAndFlightById.getTaskStatus() || 5 == taskAndFlightById.getTaskStatus()) {
                SendMsg2Redis.testDingYue(stringRedisTemplate, taskAndFlightById.getTaskOpeStaffId(), Constant.TASKFLIGHT, Constant.PC_FLIGHT_UPDATE, webMap);
            }
        }
    }

    /**
     * 新建航班闹钟提示
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public MyFlightAlarm addOrUpdateFlightAlarm(MyFlightAlarm flightAlarm, MyStaff staff) {
        //首先根据人员ID和航班ID去查询闹钟信息
        List<MyFlightAlarm> flightAlarmList = flightMapper.getFlightAlarm(staff.getLoginUserIn().getStaffId(), flightAlarm.getFlalId());
        //判断如果查出来的集合不为空并且集合的大小等于1的话说明已经存在就是修改，否则就是新增
        if (null != flightAlarmList && flightAlarmList.size() == 1) {
            flightAlarm.setFlalStaffId(staff.getLoginUserIn().getStaffId());
            if (flightMapper.updateFlightAlarm(flightAlarm) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }

        } else {
            //从token中获取人员信息放进航班闹钟对象中
            flightAlarm.setFlalAirportRcode(staff.getLoginUserIn().getStaffAirportCode());
            flightAlarm.setFlalAptareaRcode(staff.getLoginUserIn().getStaffAptareaCode());
            flightAlarm.setFlalStaffId(staff.getLoginUserIn().getStaffId());
            if (flightMapper.addFlightAlarm(flightAlarm) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
        }

        String jobName = String.format("%s||%s", flightAlarm.getFlalId(), flightAlarm.getFlalStaffId());
        MyFlightTask flightInfoById = flightMapper.getFlightInfoById(flightAlarm.getFlalId());
        Field field = ReflectionUtils.findField(flightInfoById.getClass(), flightAlarm.getFlalSetCol(), Date.class);
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, flightInfoById);
        Assert.notNull(value, "不能对空值设置闹钟");
        Date time = (Date) value;
        time = DateUtils.addMinutes(time, -flightAlarm.getFlalSetTime());
        JobDataMap data = new JobDataMap();
        data.put("flight", flightInfoById);
        data.put("alarm", flightAlarm);
        if (quartzManager.exist(jobName)) {
            quartzManager.modifySimpleTriggerJob(jobName, FlightAlarmServiceImpl.class, time, 5, data);
        } else {
            quartzManager.addSimpleTriggerJob(jobName, FlightAlarmServiceImpl.class, time, 5, data);
        }
        return flightAlarm;
    }

    /**
     * 删除航班闹钟提示
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteFlightAlarm(MyFlightAlarm flightAlarm, MyStaff staff) {
        String jobName = String.format("%s||%s", flightAlarm.getFlalId(), staff.getLoginUserIn().getStaffId());
        quartzManager.removeJob(jobName);
        if (flightMapper.deleteFlightAlarm(flightAlarm.getFlalId(), staff.getLoginUserIn().getStaffId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 查询航班闹钟提示
     */
    @Override
    public List<MyFlightAlarm> getFlightAlarm(MyStaff staff, MyFlightAlarm flightAlarm) {
        //判断航班ID不为空的话就查询单条航班闹钟提示，否则就查询该人员的所有航班闹钟提示
        if (null != flightAlarm && null != flightAlarm.getFlalId() && !"".equals(flightAlarm.getFlalId())) {
            return flightMapper.getFlightAlarm(staff.getLoginUserIn().getStaffId(), flightAlarm.getFlalId());
        } else {
            return flightMapper.getFlightAlarm(staff.getLoginUserIn().getStaffId(), null);
        }
    }

    @Override
    public ResponseObject<Object> getPageFlightList(MyNewFlight flight) {
        Integer pages = 0;
        //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        List<MyFlightTask> myFlightTasks = null;
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
        LoginUser loginUserIn = flight.getLoginUserIn();
        if (flight.getPaginate() != null && flight.getPageSize() != null) {
            Page<Object> objects = PageHelper.startPage(flight.getPaginate(), flight.getPageSize());
            if (loginUserIn != null) {
                try {
                    myFlightTasks = flightMapper.getPageTaskAndFlight(loginUserIn.getStaffAirportCode(), loginUserIn.getStaffAptareaCode(), flight.getQueryString(), nowStr);
                    myFlightTasks = mySort(myFlightTasks, false);
                    if (staffTaskLock) {
                        myFlightTasks.forEach(flightTask -> {
                            if (!StringUtils.isBlank(flightTask.getFlgtFlno()) && !StringUtils.isBlank(flightTask.getFlgtLinkFlno())) {
                                //说明是经停的时候
                                if (flightTask.getFlgtFlno().equals(flightTask.getFlgtLinkFlno())) {
                                    MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodes(loginUserIn.getStaffAirportCode());
                                    if (myAirportCode != null) {
                                        String flgtTrs3c5 = flightTask.getFlgtTrs3c5();
                                        String flgtTrsnm5 = flightTask.getFlgtTrsnm5();
                                        if (!StringUtils.isBlank(flgtTrs3c5) && !StringUtils.isBlank(flgtTrsnm5)) {
                                            String[] split = flgtTrs3c5.split("-");
                                            String[] split1 = flgtTrsnm5.split("-");
                                            if (split.length == split1.length && split.length == 3) {
                                                //判断第一位
                                                MyAirportCode myAirportCodeOne = airportCodeMapper.selectAirportCodes(split[0]);
                                                if (null != myAirportCodeOne && myAirportCodeOne.getApcdAirportProp().equals("I")) {
                                                    // 例子 判断当前登录人是不是昆明   a - > 昆明  -> b
                                                    if (myAirportCode.getApcdIataCode().equals(split[1])) {
                                                        flightTask.setFlgtOrg3c(split[0]);
                                                        flightTask.setFlgtOrgnm(split1[0]);
                                                        flightTask.setFlgtTrs3c1(split[1]);
                                                        flightTask.setFlgtTrsnm1(split1[1]);
                                                        flightTask.setFlgtDes3c(split[2]);
                                                        flightTask.setFlgtDesnm(split1[2]);
                                                    }
                                                }
                                            }
                                            /*else if (split.length == split1.length && split.length == 4) {
                                                // 例子 判断当前登录人是不是昆明   a - > 昆明 ->大理 -> b
                                                if (myAirportCode.getApcdIataCode().equals(split[1])) {
                                                    flightTask.setFlgtOrg3c(split[0]);
                                                    flightTask.setFlgtOrgnm(split1[0]);
                                                    flightTask.setFlgtTrs3c1(split[1]);
                                                    flightTask.setFlgtTrsnm1(split1[1]);
                                                    flightTask.setFlgtDes3c(split[2]);
                                                    flightTask.setFlgtDesnm(split1[2]);
                                                }
                                                // 例子 判断当前登录人是不是昆明   a - > 昆明->大理  -> b
                                                if (myAirportCode.getApcdIataCode().equals(split[2])) {
                                                    flightTask.setFlgtOrg3c(split[1]);
                                                    flightTask.setFlgtOrgnm(split1[1]);
                                                    flightTask.setFlgtTrs3c1(split[2]);
                                                    flightTask.setFlgtTrsnm1(split1[2]);
                                                    flightTask.setFlgtDes3c(split[3]);
                                                    flightTask.setFlgtDesnm(split1[3]);
                                                }
                                            }*/
                                        }
                                    }
                                }
                            }
                            Pair<String, String> pair = taskService.pingCountries(flightTask.getFlgtRegn(), flightTask.getFlgtFlno(), true);
                            if (!"未知".equals(pair.getRight())) {
                                if (StrUtil.isBlank(flightTask.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                                    flightTask.setFlgtAcname(pair.getLeft());//类型
                                }
                                String right = pair.getRight();
                                if (StringUtils.isNotEmpty(right)) {
                                    String[] $s = right.split("&");
                                    if ($s.length == 2) {
                                        flightTask.setFlgtAlcname($s[0]);//购买航空公司
                                        flightTask.setArcrCustomNum($s[1]);
                                    }
                                }
                            }
                        });
                    }
                    pages = flightMapper.getPageTaskAndFlightCount(loginUserIn.getStaffAirportCode(), loginUserIn.getStaffAptareaCode(), flight.getQueryString(), nowStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new CustomException(ReturnMsg.getInstanceNGz("获取不到登录信息", null));
            }
        } else {
            throw new CustomException(ReturnMsg.getInstanceNGz("分页信息不可为空", null));
        }
        PageInfo<MyFlightTask> pageInfo = new PageInfo<MyFlightTask>(myFlightTasks);
        if (pages != 0) {
            BigDecimal counts = new BigDecimal(pages);
            BigDecimal pagesize = new BigDecimal(flight.getPageSize());
            BigDecimal bigDecimal = counts.divide(pagesize).setScale(0, RoundingMode.UP);
            pageInfo.setPageSize(bigDecimal.intValue());
        }
        myFlightTasks = myFlightTasks != null ? myFlightTasks.stream()
                                                .peek(flightInfoById -> {
                                                    //航班号裁切 2019年8月12日17:01:38
                                                    if (StringUtils.isNotEmpty(flightInfoById.getFlgtFlno())) {
                                                        String flgtFlno = flightInfoById.getFlgtFlno();
                                                        Arrays.stream(
                                                                flgtFlno.split("/")
                                                        ).filter(no ->
                                                                StringUtils.contains(no, flightInfoById.getFlgtAl2c()))
                                                        .findAny().ifPresent(flightInfoById::setFlgtFlno);
                                                    }
                                                }).collect(Collectors.toList()) : null;
        return new ResponseObject<Object>("0", "", myFlightTasks, pageInfo);
    }

    /**
     * @Description: flgtRegn飞机号   flgtFlti国内国际 taskContent任务类型
     * @Param: [flgtRegn, flgtFlti, taskContent]
     * @return: java.lang.Integer
     * @Author: XiuHongXin
     * @Date: 2019/6/2
     * 1 外航   加油
     * 2 内航 离境 加油
     * 3 内航国内加油
     * 4   外航  抽油
     * 5 内航 离境 抽油
     * 6 内航国内加油
     * 7 内航国内补加油 -> 3
     * 8 内航 离境 补加油 -> 2
     * 9 外航 补油 -> 1
     */
    @Override
    public Integer pingSingleType(MyFlightTask myFlightTask) {
        if (myFlightTask.getFlrcType() != null) {
            return myFlightTask.getFlrcType();
        }
        try {
            String flgtRegn = myFlightTask.getFlgtRegn();
            String flgtFlti = myFlightTask.getFlgtFlti();
            Integer taskContent = myFlightTask.getTaskContent();
            String flgtFltiIn = myFlightTask.getFlgtFltiIn();
            String flgtFlno = myFlightTask.getFlgtFlno();
            String flgtLinkFlno = myFlightTask.getFlgtLinkFlno();
            String flgtAl2c = myFlightTask.getFlgtAl2c();
            if (StringUtils.isNotEmpty(flgtAl2c)) {
                TForeignairportCode tForeignairportCode = tForeignairportCodeMapper.selectByAlcdIcaoCode(flgtAl2c);
                if (tForeignairportCode != null) {
                    //判断任务类型
                    if (taskContent == 1) {
                        // 4   外航  抽油
                        return 4;
                    } else if (taskContent == 0) {
                        //  1 外航   加油
                        return 1;
                    } else {
                        // 9 外航 补油 -> 1
                        return 1;
                    }
                }
            }
            MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(flgtAl2c);
            if (myAirlinesCode != null && myAirlinesCode.getAlcdArlnNw() != null) {
                // 内
                if (myAirlinesCode.getAlcdArlnNw() == 0) {
                    //外航
                } else if (myAirlinesCode.getAlcdArlnNw() == 1) {
                    //TODO 任务类型可能为空 暂不做处理
                    //判断任务类型
                    if (taskContent == 1) {
                        // 4   外航  抽油
                        return 4;
                    } else if (taskContent == 0) {
                        //  1 外航   加油
                        return 1;
                    } else {
                        // 9 外航 补油 ->
                        return 1;
                    }
                }
            }
            //判断是否是经停航班
            if (StringUtils.isNotEmpty(flgtFlno) && StringUtils.isNotEmpty(flgtLinkFlno)) {
                // 说明经停
                if (flgtFlno.equals(flgtLinkFlno)) {
                    if (StringUtils.isNotEmpty(flgtFltiIn)) {
                        //说明国际航班
                        if (!"D".equals(flgtFltiIn)) {
                            if (taskContent == 1) {
                                // 5 内航 离境 抽油
                                return 5;
                            } else if (taskContent == 0) {
                                // 2 内航 离境 加油
                                return 2;
                            } else {
                                // 内航 离境 补加油 -> 2
                                return 2;
                            }
                        }
                    }
                }
            }
            // 判断国内国外 0：加油，1：抽油 2补加油
            if ("D".equals(flgtFlti)) {
                if (taskContent == 1) {
                    // 6 内航国内抽油
                    return 6;
                } else if (taskContent == 0) {
                    // 3 内航国内加油
                    return 3;
                } else {
                    // 7 内航国内补加油 -> 3
                    return 3;
                }
            } else {
                if (taskContent == 1) {
                    // 5 内航 离境 抽油
                    return 5;
                } else if (taskContent == 0) {
                    // 2 内航 离境 加油
                    return 2;
                } else {
                    // 内航 离境 补加油 -> 2
                    return 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认
        if (fuelTypeLock) {
            return 3;
        } else {
            return 1;
        }
    }

    @Override
    public MyFlight copyNewFlight(MyFlight flight) {
        Date now = new Date();
        if (StringUtils.isEmpty(flight.getFlgtFfid()))
            throw new CustomException(ReturnMsg.getInstanceNGz("ffid不存在", flight));
        MyFlight myFlight = flightMapper.findMyFlightByFfid(flight.getFlgtFfid());
        if (myFlight == null)
            throw new CustomException(ReturnMsg.getInstanceNGz("根据ffid查询错误，该航班不存在", flight));
        if (StringUtils.equals(DateFormatUtils.format(myFlight.getFlgtFlop(), Constant.YYYY_MM_DD), DateFormatUtils.format(new Date(), Constant.YYYY_MM_DD)))
            throw new CustomException(ReturnMsg.getInstanceNGz("当天该航班已存在", flight));
        if (StringUtils.isNotEmpty(myFlight.getFlgtLinkFlno())) {
            MyFlight linkFlight = flightMapper.findLinkFlight(myFlight.getFlgtFlno(), myFlight.getFlgtFlop(), myFlight.getFlgtLinkRepeat(), myFlight.getFlgtRepeat());
            if (linkFlight == null) throw new CustomException(ReturnMsg.getInstanceNGz("关联航班查询错误", flight));
            linkFlight.setFlgtFlop(now);
            linkFlight.setFlgtLinkFlop(now);
            linkFlight.setFlgtFfid(UUID.randomUUID().toString());
            linkFlight.setFlgtId(UUID.randomUUID().toString());
        }
        myFlight.setFlgtId(UUID.randomUUID().toString());
        myFlight.setFlgtFfid(UUID.randomUUID().toString());
        myFlight.setFlgtFlop(now);
        myFlight.setFlgtLinkFlop(now);
        //添加进港航班信息
        if (flightMapper.addFlight(myFlight) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, myFlight));
        }
        return myFlight;
    }

    @Override
    public MyFlight insertFlightRemark(MyFlight flight) {
        if (StringUtils.isBlank(flight.getFlgtFfid()))
            throw new CustomException(ReturnMsg.getInstanceNGz("主键ID不可为空"));
        MyFlight myFlightByFfid = flightMapper.findMyFlightByFfid(flight.getFlgtFfid());
        if (myFlightByFfid == null) throw new CustomException(ReturnMsg.getInstanceNGz("不存在该航班"));
        try {
            myFlightByFfid.setRemark(flight.getRemark());
            flightMapper.updateFlightRemark(myFlightByFfid);
        } catch (Exception e) {
            throw new CustomException(ReturnMsg.getInstanceNGz("更新失败"));
        }
        return myFlightByFfid;
    }

    @Override
    public List<TPlacecode> getPlaceCode(TPlacecode placeCode) {
        List<TPlacecode> plist = placecodeMapper.getPlaceCodeList(placeCode);
        plist.forEach(
                placeCode1 -> {
                    String flrcHydrtPitNo = placeCode1.getFlrcHydrtPitNo();
                    String[] split = flrcHydrtPitNo.split("-");
                    if (split.length == 1) {
                        placeCode1.setFlrcHydrtPitNo1(split[0]);
                    } else if (split.length == 2) {
                        placeCode1.setFlrcHydrtPitNo1(split[0]);
                        placeCode1.setFlrcHydrtPitNo2(split[1]);
                    } else if (split.length == 3) {
                        placeCode1.setFlrcHydrtPitNo1(split[0]);
                        placeCode1.setFlrcHydrtPitNo2(split[1]);
                        placeCode1.setFlrcHydrtPitNo3(split[2]);
                    } else if (split.length == 4) {
                        placeCode1.setFlrcHydrtPitNo1(split[0]);
                        placeCode1.setFlrcHydrtPitNo2(split[1]);
                        placeCode1.setFlrcHydrtPitNo3(split[2]);
                        placeCode1.setFlrcHydrtPitNo4(split[3]);
                    }
                }
        );
        return plist;
    }


    @Override
    public Integer addOrUpdatePlaceCode(TPlacecode placeCode) {
        String codes = Lists.newArrayList(placeCode.getFlrcHydrtPitNo1(), placeCode.getFlrcHydrtPitNo2(), placeCode.getFlrcHydrtPitNo3(), placeCode.getFlrcHydrtPitNo4()).stream().filter(Objects::nonNull).collect(Collectors.joining("-"));
        System.out.println("FlightServiceImpl.addOrUpdatePlaceCode: " + codes);
        placeCode.setFlrcHydrtPitNo(codes);
        TPlacecode code = placecodeMapper.getPlaceCode(placeCode);
        if (code != null) {
            return placecodeMapper.updateByPrimaryKeySelective(placeCode);
        } else {
            return placecodeMapper.insertSelective(placeCode);
        }
    }

    @Override
    public Integer deletePlaceCode(TPlacecodeKey key) {
        return placecodeMapper.deleteByPrimaryKey(key);
    }

    @Override
    public List<TPlacecodeType> getPlaceCodeType(TPlacecodeType placeCodeType) {
        return placecodeTypeMapper.getPlacecodeType(placeCodeType);
    }

    @Override
    public Integer addOrUpdatePlaceCodeType(TPlacecodeType placeCodeType) {
        TPlacecodeType placecodeTypeOne = placecodeTypeMapper.getPlacecodeTypeOne(placeCodeType.getId());
        if (placecodeTypeOne != null) {
            return placecodeTypeMapper.updateByPrimaryKeySelective(placeCodeType);
        } else {
            return placecodeTypeMapper.insertSelective(placeCodeType);
        }
    }

    @Override
    public Integer deletePlaceCodeType(String id) {
        return placecodeTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TPlacecode getOnePlaceCode(TPlacecodeKey key) {
        return placecodeMapper.getPlaceCode(key);
    }

    @Override
    public List<MyFlight> getAFlight(String flgtAirportCode) {
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
        //比较一下用时
        // List<MyFlight> myFlights = taskMapper.groupAirportCode();
        String finalNowStr = nowStr;
        List<MyFlight> taskAndFlight = null;
        if (StringUtils.isNotEmpty(flgtAirportCode)) {
            if (lock) {
                taskAndFlight = taskMapper.getAFlight(flgtAirportCode, finalNowStr);
            } else {
                taskAndFlight = taskMapper.getAFlightLock(flgtAirportCode, finalNowStr);
            }
              /*  List<MyStaff> staffList = staffMapper.getStaffLists(flgtAirportCode, null);
                // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
                String userId = "";
                int i = 0;
                for (MyStaff myStaff : staffList) {
                    if (i == 0) {
                        userId = myStaff.getStaffId();
                    }
                    if (i > 0) {
                        userId = userId + "," + myStaff.getStaffId();
                    }
                    i++;
                }
                // 推送给调度员
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_A, taskAndFlight);
           */
        }
        return taskAndFlight;
    }

    @Override
    public void test() {
        List<MyFlightTask> testFlight = flightMapper.getTestFlight();
        List<MyAirlinesCode> myAirlinesCodes = airlinesCodeMapper.selectAirlinesCode();
        List<TForeignairportCode> tForeignairportCodes = tForeignairportCodeMapper.selectAll();
        String collect = myAirlinesCodes.stream().map(m -> m.getAlcdIcaoCode()).collect(Collectors.joining(","));
        String collect1 = tForeignairportCodes.stream().map(m -> m.getAlcdIcaoCode()).collect(Collectors.joining(","));
        testFlight.forEach(myFlightTask -> {
            if (null != myFlightTask && StringUtils.isNotEmpty(myFlightTask.getFlgtAl2c())) {
                if (!collect.contains(myFlightTask.getFlgtAl2c())) {
                    System.out.println(myFlightTask.getFlgtAl2c());
                }
                ;
            }
        });
    }

    @Override
    public void test1() {
        PageHelper.startPage(1, 10);
        PageHelper.orderBy("alcdArlnName,alcdArlnNameS");
        List<MyAirlinesCode> myAirlinesCodes = airlinesCodeMapper.selectAirlinesCode();
        PageInfo<MyAirlinesCode> pageInfo = new PageInfo<MyAirlinesCode>(myAirlinesCodes);
        System.out.println(JSON.toJSONString(pageInfo));
    }

    @Override
    public List<MyFlightTask> orderToFlight(List<TOrderInfo> tOrderInfos, MyStaff staff) {
        try {
            List<MyFlightTask> resp = new ArrayList<MyFlightTask>();
            List<MyFlight> lists = orderInfoToMyFlightTask(tOrderInfos);//MyFlight
            lists.forEach(myFlight -> {
                MyFlightTask myFlightTask = addFlightAndTask(myFlight, staff, 0, 0);
                resp.add(myFlightTask);
            });
            tOrderInfos.forEach(o -> {
                o.setStatus(1);
                tOrderInfoMapper.updateByPrimaryKeySelective(o);
            });
            kafkaTemplate.send("order_change", JSON.toJSONString(tOrderInfos));
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MyCustom getFlightCodeNew(MyFlightCode flightCode) {
        String custom = "";
        try {
            Integer integer = Integer.valueOf(flightCode.getArcrCustomNum());
            custom = integer.toString();
        } catch (Exception e) {
            if (flightCode.getArcrCustomNum().length() == 6) {
                custom = flightCode.getArcrCustomNum();
            } else {
                String substring = flightCode.getArcrCustomNum().substring(flightCode.getArcrCustomNum().length() - 6, flightCode.getArcrCustomNum().length());
                custom = substring;
            }
        }
        MyCustom myCustom = flightMapper.getFlightCodeNew(StrUtil.fill(custom, '0', 6, true));
        return myCustom;
    }

    @Override
    public ReturnMsg validCustomNum(MyFlightTask task) {
        MyFlightTask flightInfoById = flightMapper.getFlightInfoById(task.getFlgtId());
        if (flightInfoById == null) {
            return new ReturnMsg(Constant.CODE_ERR, "该航班在系统内不存在", null);
        }
        String flgtRegn = flightInfoById.getFlgtRegn();
        String flgtFlno = flightInfoById.getFlgtFlno();
        String arcrCustomNum = task.getArcrCustomNum();
        if (StrUtil.isBlank(arcrCustomNum)) {
            return new ReturnMsg(Constant.CODE_ERR, "参数错误，请提交加油客户编号", null);
        }
        // 校验飞机号码信息
        MyFlightCode flightCodeInfoByRegnAndFlno = taskService.getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtFlno);
        if (flightCodeInfoByRegnAndFlno == null) {
            return new ReturnMsg(Constant.CODE_ERR, "该航班没有匹配到加油客户信息，请维护后提交", null);
        }
        if (!flightCodeInfoByRegnAndFlno.getArcrCustomNum().equals(arcrCustomNum)) {
            String errinfo = String.format("填写的客户编号%s与系统匹配到的客户编号%s不一致，请校正后提交", arcrCustomNum, flightCodeInfoByRegnAndFlno.getArcrCustomNum());
            return new ReturnMsg(Constant.CODE_ERR, errinfo, null);
        }
        return new ReturnMsg(Constant.CODE_OK, null, null);
    }


    private List<MyFlight> orderInfoToMyFlightTask(List<TOrderInfo> tOrderInfos) {
        List<MyFlight> lists = new ArrayList<MyFlight>();
        tOrderInfos.forEach(tOrderInfo -> {
            MyFlight myFlightTask = new MyFlight();
            myFlightTask.setOrderNo(String.valueOf(tOrderInfo.getOrderId()));
            myFlightTask.setFlgtFlno(tOrderInfo.getFlno());
            myFlightTask.setFlgtAdid("D");// TODO 默认放入
            // 航班号为空 就放入订单号 作为航班号
            if (StringUtils.isNotEmpty(tOrderInfo.getFlno())) {
                myFlightTask.setFlgtFlno(tOrderInfo.getFlno());
            } else {
                myFlightTask.setFlgtFlno(tOrderInfo.getOrderNo());
            }
            if (StringUtils.isNotEmpty(tOrderInfo.getRegn())) {
                myFlightTask.setFlgtRegn(tOrderInfo.getRegn());
            }
            if (null != tOrderInfo.getOilType()) {
                if (1 == tOrderInfo.getOilType()) {
                    myFlightTask.setFlgtFlti("I");
                } else {
                    myFlightTask.setFlgtFlti("D");
                }
            }

            // 进港默认放入当前时间
            myFlightTask.setFlgtAAtot(new Date());
            myFlightTask.setFlgtAStot(new Date());
            myFlightTask.setFlgtAEtot(new Date());
            myFlightTask.setFlgtGame("Y");
            //estimatedTime
            myFlightTask.setFlgtDStot(new Date());
            myFlightTask.setFlgtDEtot(new Date());
            myFlightTask.setFlgtAdid("D");
            myFlightTask.setFlgtNature("GEN");
            myFlightTask.setFlgtPlacecode("-1");
            myFlightTask.setFlgtOrg3c(tOrderInfo.getOrg3());
            myFlightTask.setFlgtOrgnm(tOrderInfo.getOrgnm());
            myFlightTask.setFlgtDes3c(tOrderInfo.getDes3());
            myFlightTask.setFlgtDesnm(tOrderInfo.getDesnm());
            myFlightTask.setFlgtTrs3c1(tOrderInfo.getVia3());
            myFlightTask.setFlgtTrsnm1(tOrderInfo.getVianm());
            myFlightTask.setFlgtFlop(new Date());
            myFlightTask.setFlgtIsOrder(1); // 订单任务
            myFlightTask.setOrderNo(tOrderInfo.getOrderId().toString());
            lists.add(myFlightTask);
        });
        return lists;

            /*
                {
                  flgtFlno=HDD45,
                  taskContent=0,
                  flgtAStot=2020-11-1916: 22: 21,
                  flgtGame=Y,
                  flgtFlti=D,
                  flgtDEtot=2020-11-1921: 38: 25,
                  taskStatus=0,
                  flgtNature=PAX,

                  flgtAdid=D,
                  flgtPlacecode=3,
                  flgtAEtot=2020-11-19 17: 32: 25,
                  flgtDes3c=AOG,
                  flgtDStot=2020-11-19 21: 32: 25,
                  flgtFlop=2020-11-19 00: 00: 00,
                  flgtRegn=b552,
                  flgtOrg3c=AAT,
                  flgtTrs3c1=RHT
                }
             */
        //myFlightTask.setFlgtRegn();


    }


    public String compileEnum(String flgtMissionProp) {
        switch (flgtMissionProp) {
            case "正班":
                return "W/Z";
            case "补班":
                return "Z/P";
            case "公务":
                return "U/H";
            case "加班":
                return "C/B";
            case "旅包":
                return "L/W";
            case "专机":
                return "B/W";
            case "备降":
                return "A/N";
            case "邮政":
                return "A/A";
            case "返航":
                return "R/N";
            case "货包":
                return "H/G";
            case "货班":
                return "H/Z";
            case "急救":
                return "O/F";
            case "调机":
                return "N/M";
            case "鱼苗":
                return "M/X";
            case "灭火":
                return "P/A";
            case "全部":
                return "ZZ";
            case "农化":
                return "Q/C";
            case "摩发":
                return "Q/U";
            case "日航":
                return "R/W";
            case "试航":
                return "R/Z";
            case "试飞":
                return "S/F";
            case "视察":
                return "S/Q";
            case "地航":
                return "T/W";
            case "采矿":
                return "U/B";
            case "未知":
                return "N/N";
        }
        return "";
    }

    @Override
    public ResponseObject<Object> getFlightListByNum(MyNewFlight flight) {
        LoginUser loginUserIn = flight.getLoginUserIn();
        if (loginUserIn == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("获取不到登录信息", null));
        }
        String staffAirportCode = loginUserIn.getStaffAirportCode();
        if (StrUtil.isBlank(staffAirportCode)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("机场编码不能为空", null));
        }
        flight.setStaffAirportCode(staffAirportCode);
        flight.setStaffAptareaCode(loginUserIn.getStaffAptareaCode());
        Result result = getFlightAndTaskForPadByPage(flight);
        return new ResponseObject<Object>("0", "", result.myFlightTasks, result.pageInfo);
    }

    @Override
    public ResponseObject<Object> getFlightAndTaskForPad(MyNewFlight flight) {
        LoginUser loginUserIn = flight.getLoginUserIn();
        if (loginUserIn == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("获取不到登录信息", null));
        }
        String staffAirportCode = loginUserIn.getStaffAirportCode();
        if (StrUtil.isBlank(staffAirportCode)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("机场编码不能为空", null));
        }
        flight.setStaffAirportCode(staffAirportCode);
        flight.setStaffAptareaCode(loginUserIn.getStaffAptareaCode());
        return queryFlightAndTaskForPad(flight);
    }

    /**
     * 功能描述：pad端查询飞机和任务信息列表
     *
     * @param flight 查询参数
     * @return com.zh.bean.ResponseObject<java.lang.Object>
     * @author zhaojiacan
     * @date 2024/5/17
     */
    @Override
    public ResponseObject<Object> queryFlightAndTaskForPad(MyNewFlight flight) {
        String staffAirportCode = flight.getStaffAirportCode();
        String version = flight.getVersion();
        String airportFlightHashKey = new StringBuilder("FLIGHT:TASK:LIST:HASH:").append(staffAirportCode).toString();
        String airportFlightDataKey = new StringBuilder("FLIGHT:TASK:LIST:DATA:").append(staffAirportCode).toString();
        String hashVersionCurrent = stringRedisTemplate.opsForValue().get(airportFlightHashKey);
        //如果不携带hash版本号则直接从数据库查询，并存入版本号
        if (StrUtil.isBlank(version) || StrUtil.isBlank(hashVersionCurrent)) {
            ResultNoPage result = getFlightAndTaskForPadByPageFromDb(flight, airportFlightHashKey, airportFlightDataKey);
            return new ResponseObject<>("0", "", result, null);
        }
        String myFlightTasksJsonStr = stringRedisTemplate.opsForValue().get(airportFlightDataKey);
        //如果携带了hash版本号则对比是否一致，并且缓存数据不为空，一致则直接返回空数据
        if (version.equals(hashVersionCurrent)) {
            if (StrUtil.isBlank(myFlightTasksJsonStr)) {
                ResultNoPage result = getFlightAndTaskForPadByPageFromDb(flight, airportFlightHashKey, airportFlightDataKey);
                return new ResponseObject<>("0", "", result, null);
            }
            return new ResponseObject<>("0", "", null, null);
        } else {
            //如果携带了hash版本号则对比是否一致，不一致则直接返回缓存中的数据,并更新版本
            return new ResponseObject<>("0", "", new ResultNoPage(JSONUtil.toList(myFlightTasksJsonStr, MyFlightTask.class), hashVersionCurrent), null);
        }
    }

    /**
     * 功能描述：缓存飞机和任务信息
     *
     * @param flight 查询参数
     * @return void
     * @author zhaojiacan
     * @date 2024/5/17
     */
    @Override
    public void cacheFlightAndTaskForPad(MyNewFlight flight) {
        String staffAirportCode = flight.getStaffAirportCode();
        if (StrUtil.isBlank(staffAirportCode)) {
            return;
        }
        String airportFlightHashKey = new StringBuilder("FLIGHT:TASK:LIST:HASH:").append(staffAirportCode).toString();
        String airportFlightDataKey = new StringBuilder("FLIGHT:TASK:LIST:DATA:").append(staffAirportCode).toString();
        String hashVersionCurrent = stringRedisTemplate.opsForValue().get(airportFlightHashKey);
        String hashVersionNew = DigestUtil.md5Hex(StrUtil.EMPTY, CharsetUtil.CHARSET_UTF_8);
        String myFlightTasksJsonStr = StrUtil.EMPTY;
        Result result = getFlightAndTaskForPadByPage(flight);
        List<MyFlightTask> myFlightTasks = result.myFlightTasks;
        if (CollUtil.isNotEmpty(myFlightTasks)) {
            myFlightTasksJsonStr = JSONUtil.toJsonStr(myFlightTasks);
            hashVersionNew = DigestUtil.md5Hex(myFlightTasksJsonStr, CharsetUtil.CHARSET_UTF_8);
        }
        if (hashVersionCurrent == null || !hashVersionNew.equals(hashVersionCurrent)) {
            stringRedisTemplate.opsForValue().set(airportFlightHashKey, hashVersionNew);
            stringRedisTemplate.opsForValue().set(airportFlightDataKey, myFlightTasksJsonStr);
        }
    }

    /**
     * 功能描述： 异步缓存pad端航班列表
     *
     * @param airportCode
     * @return void
     * @author zhaojiacan
     * @date 2024/5/22
     */
    @Async
    @Override
    public void cacheFlightAndTaskForPad(String airportCode) {
        MyNewFlight myNewFlight = new MyNewFlight();
        myNewFlight.setStaffAirportCode(airportCode);
        myNewFlight.setPageSize(1000);
        myNewFlight.setPaginate(1);
        cacheFlightAndTaskForPad(myNewFlight);
    }


    private ResultNoPage getFlightAndTaskForPadByPageFromDb(MyNewFlight flight, String airportFlightHashKey, String airportFlightDataKey) {
        Result result = getFlightAndTaskForPadByPage(flight);
        List<MyFlightTask> myFlightTasks = result.myFlightTasks;
        String myFlightTasksJsonStr = StrUtil.EMPTY;
        String hashVersion = DigestUtil.md5Hex(StrUtil.EMPTY, CharsetUtil.CHARSET_UTF_8);
        if (CollUtil.isNotEmpty(myFlightTasks)) {
            myFlightTasksJsonStr = JSONUtil.toJsonStr(myFlightTasks);
            hashVersion = DigestUtil.md5Hex(myFlightTasksJsonStr, CharsetUtil.CHARSET_UTF_8);
        }
        stringRedisTemplate.opsForValue().set(airportFlightHashKey, hashVersion);
        stringRedisTemplate.opsForValue().set(airportFlightDataKey, myFlightTasksJsonStr);
        return new ResultNoPage(result.myFlightTasks, hashVersion);
    }

    private Result getFlightAndTaskForPadByPage(MyNewFlight flight) {
        String staffAirportCode = flight.getStaffAirportCode();
        if (StrUtil.isBlank(staffAirportCode)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("机场编码不能为空", null));
        }
        TimeInterval timer = DateUtil.timer();
        //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
        List<MyFlightTask> myFlightTasks = null;
        Date now = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sim.format(now);
        long time = 60 * 1000 * 60 * 4;// 60秒
        Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
        String beforeDatetr = sim.format(beforeDate);
        if (!nowStr.equals(beforeDatetr)) {
            nowStr = beforeDatetr;
        }
        System.out.println("查询航班列表 时间 " + nowStr + "   " + JSON.toJSONString(flight));
        if (flight.getPaginate() != null && flight.getPageSize() != null) {
            PageHelper.startPage(flight.getPaginate(), flight.getPageSize());
            try {
                // 客户信用逻辑
//                    List<TCreditInfo> tCreditInfos = tCreditInfoMapper.selectCreditInfo(new TCreditInfo());
                myFlightTasks = flightMapper.getFlightListByNum(staffAirportCode, flight.getStaffAptareaCode(), flight.getQueryString(), nowStr, flight.getPageSize());
                System.out.println("分页获取获取航班信息结果  - >  " + myFlightTasks + "    " + JSON.toJSONString(flight));
                log.info("查询航班耗时：{}", timer.intervalRestart());
                myFlightTasks = mySort(myFlightTasks, false);
                System.out.println("分页获取获取航班信息结果mySort  - >  " + myFlightTasks + "    " + JSON.toJSONString(flight));
                log.info("排序航班耗时：{}", timer.intervalRestart());
                myFlightTasks = myFlightTasks != null ? myFlightTasks.stream()
                                                        .filter(o -> o.getOrderNo() == null)
                                                        .peek(flightInfoById -> {
                                                            //航班号裁切 2019年8月12日17:01:38
                                                            if (StringUtils.isNotEmpty(flightInfoById.getFlgtFlno())) {
                                                                String flgtFlno = flightInfoById.getFlgtFlno();
                                                                Arrays.stream(
                                                                        flgtFlno.split("/")).filter(no ->
                                                                        StringUtils.contains(no, flightInfoById.getFlgtAl2c()))
                                                                .findAny().ifPresent(flightInfoById::setFlgtFlno);
                                                            }
                                                        }).collect(Collectors.toList()) : null;
                myFlightTasks.forEach(flightTask -> {
                    // Integer type = pingSingleType(flightTask);
                    // flightTask.setFlrcType(type);
                    if (staffTaskLock) {
                        if (!StringUtils.isBlank(flightTask.getFlgtFlno()) && !StringUtils.isBlank(flightTask.getFlgtLinkFlno())) {
                            //说明是经停的时候
                            if (flightTask.getFlgtFlno().equals(flightTask.getFlgtLinkFlno())) {
                                MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodes(staffAirportCode);
                                if (myAirportCode != null) {
                                    String flgtTrs3c5 = flightTask.getFlgtTrs3c5();// 三字码
                                    String flgtTrsnm5 = flightTask.getFlgtTrsnm5();// 中文
                                    if (!StringUtils.isBlank(flgtTrs3c5) && !StringUtils.isBlank(flgtTrsnm5)) {
                                        String[] split = flgtTrs3c5.split("-");// 三字码
                                        String[] split1 = flgtTrsnm5.split("-");// 中文
                                        if (split.length == split1.length && split.length == 3) {
                                            //判断第一位
                                            MyAirportCode myAirportCodeOne = airportCodeMapper.selectAirportCodes(split[0]);
                                            if (null != myAirportCodeOne && myAirportCodeOne.getApcdAirportProp().equals("I")) {
                                                // 例子 判断当前登录人是不是昆明   a - > 昆明  -> b
                                                if (myAirportCode.getApcdIataCode().equals(split[1])) {
                                                    flightTask.setFlgtOrg3c(split[0]);
                                                    flightTask.setFlgtOrgnm(split1[0]);
                                                    flightTask.setFlgtTrs3c1(split[1]);
                                                    flightTask.setFlgtTrsnm1(split1[1]);
                                                    flightTask.setFlgtDes3c(split[2]);
                                                    flightTask.setFlgtDesnm(split1[2]);
                                                }
                                            }
                                        }
                                            /*else if (split.length == split1.length && split.length == 4) {
                                                // 例子 判断当前登录人是不是昆明   a - > 昆明 ->大理 -> b
                                                if (myAirportCode.getApcdIataCode().equals(split[1])) {
                                                    flightTask.setFlgtOrg3c(split[0]);
                                                    flightTask.setFlgtOrgnm(split1[0]);
                                                    flightTask.setFlgtTrs3c1(split[1]);
                                                    flightTask.setFlgtTrsnm1(split1[1]);
                                                    flightTask.setFlgtDes3c(split[2]);
                                                    flightTask.setFlgtDesnm(split1[2]);
                                                }
                                                // 例子 判断当前登录人是不是昆明   a - > 昆明->大理  -> b
                                                if (myAirportCode.getApcdIataCode().equals(split[2])) {
                                                    flightTask.setFlgtOrg3c(split[1]);
                                                    flightTask.setFlgtOrgnm(split1[1]);
                                                    flightTask.setFlgtTrs3c1(split[2]);
                                                    flightTask.setFlgtTrsnm1(split1[2]);
                                                    flightTask.setFlgtDes3c(split[3]);
                                                    flightTask.setFlgtDesnm(split1[3]);
                                                }
                                            }*/
                                    }
                                }
                            }
                        }
                    }
                    Pair<String, String> pair = taskService.pingCountries(flightTask.getFlgtRegn(), flightTask.getFlgtFlno(), true);
                    if (!"未知".equals(pair.getRight())) {
                        if (StrUtil.isBlank(flightTask.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                            flightTask.setFlgtAcname(pair.getLeft());//类型
                        }
                        String right = pair.getRight();
                        if (StringUtils.isNotEmpty(right)) {
                            String[] $s = right.split("&");
                            if ($s.length == 2) {
                                flightTask.setFlgtAlcname($s[0]);//购买航空公司
                                flightTask.setArcrCustomNum($s[1]);
                            }
                        }
                    }
                    taskService.setAirLines(flightTask);

//                        if (StringUtils.isNotEmpty(flightTask.getArcrCustomNum())) {
//                            tCreditInfos.stream().filter(dto->
//                                    ObjectUtil.contains(flightTask.getArcrCustomNum(),dto.getCstno()))
//                                    .findFirst()
//                                    .ifPresent(dto->{
//                                        flightTask.setCilvl(dto.getCilvl());
//                                        flightTask.setCitst(dto.getCitst());
//                                        flightTask.setCirmk(dto.getCirmk());
//                                    });
//                        }
                });
                log.info("填充航班信息耗时：{}", timer.intervalRestart());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new CustomException(ReturnMsg.getInstanceNGz("分页信息不可为空", null));
        }

        PageInfo<MyFlightTask> pageInfo = new PageInfo<MyFlightTask>(myFlightTasks);

        System.out.println("分页获取获取航班信息结果pageInfo  - >  " + pageInfo + "       " + JSON.toJSONString(myFlightTasks) + "    " + JSON.toJSONString(flight));
        log.info("分页信息耗时：{}", timer.intervalRestart());
        Result result = new Result(myFlightTasks, pageInfo);
        return result;
    }

    /**
     * 从redis获取航班序号
     *
     * @return
     */
    @Override
    public int getFlightMaxNum() {

        try {

            if (!redissonDistributedLocker.tryLock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY, TimeUnit.SECONDS, 2, 2)) {
                //分布式缩 加锁失败 返回默认值
                return 0;
            }
            String currentDateStr = DateUtil.format(new Date(), "yyyy-MM-dd");
            String incrKey = "sequenceKey:" + currentDateStr;
            Long increment = stringRedisTemplate.opsForValue().increment(incrKey, 1);
            stringRedisTemplate.expire(incrKey, 86400 * 7, TimeUnit.SECONDS);//7天过期


            redissonDistributedLocker.unlock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY);
            return increment.intValue();

        } catch (Exception e) {
            log.error("从redis里获取航班序号，发生异常，原因：" + e.getMessage());
        } finally {
            redissonDistributedLocker.unlock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY);
        }

        return 0;
    }

    private static class Result {
        public final List<MyFlightTask> myFlightTasks;
        public final PageInfo<MyFlightTask> pageInfo;

        public Result(List<MyFlightTask> myFlightTasks, PageInfo<MyFlightTask> pageInfo) {
            this.myFlightTasks = myFlightTasks;
            this.pageInfo = pageInfo;
        }
    }

    private static class ResultNoPage {
        public final List<MyFlightTask> myFlightTasks;
        public final String version;

        public ResultNoPage(List<MyFlightTask> myFlightTasks, String version) {
            this.myFlightTasks = myFlightTasks;
            this.version = version;
        }
    }
}