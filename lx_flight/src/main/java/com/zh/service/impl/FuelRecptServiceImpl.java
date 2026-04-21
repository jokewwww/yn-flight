package com.zh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.*;
import com.zh.component.DistributedLocker;
import com.zh.component.RedisComponent;
import com.zh.component.RedissonDistributedLocker;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.FlightService;
import com.zh.service.FuelRecptService;
import com.zh.service.TFuelNoService;
import com.zh.util.*;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FuelRecptServiceImpl implements FuelRecptService {


    private final static Logger log = LoggerFactory.getLogger(FuelRecptServiceImpl.class);
    @Autowired
    Prop prop;
    @Autowired
    @Lazy
    private FlightService flightService;
    @Autowired
    private MyFuelRecptMapper fuelRecptMapper;
    @Autowired
    private TOrderInfoMapper tOrderInfoMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private FlightCodeTemporaryMapper flightCodeTemporaryMapper;
    @Autowired
    private TaskServiceImpl taskserviceimpl;
    @Autowired
    private VehiMapper vehiMapper;
    @Autowired
    private MyStaffVehiTaskMapper vehitaskMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TFlightTroubleMapper tFlightTroubleMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private RedisComponent redis;
    @Autowired
    private TFuelDistributionMapper tFuelDistributionMapper;
    @Autowired
    private TFuelNoMapper tFuelNoMapper;
    @Value("${staff_task_lock}")
    private Boolean staffTaskLock;
    @Autowired
    private AirportCodeMapper airportCodeMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private TFuelNoService tFuelNoService;
    @Autowired
    private NewFuelMapper fuelMapper;
    @Autowired
    private DistributedLocker distributedLocker;
    @Autowired
    private MyFuelRecptOldMapper myFuelRecptOldMapper;
    @Autowired
    private TCreditInfoMapper tCreditInfoMapper;
    @Autowired
    private MyTAirportCodeMapper myTAirportCodeMapper;
    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    public static void main(String[] args) {
        String s = "{\n" +
                "\t\"padId\":\"aimei866264037106692\",\n" +
                "\t\"staffId\":\"28560\",\n" +
                "\t\"staffPwd\":\"6842571\",\n" +
                "\t\"tFuelNo\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000323\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000324\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000325\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000326\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000327\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000328\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000329\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000330\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000331\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037160000332\",\n" +
                "\t\t\t\"fuelType\":1,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000321\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000322\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000323\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000324\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000325\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000326\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000327\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000328\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000329\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037260000330\",\n" +
                "\t\t\t\"fuelType\":2,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004748\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004749\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004750\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004751\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004752\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004753\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004755\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004756\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004757\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"flrcNo\":\"3037360004758\",\n" +
                "\t\t\t\"fuelType\":3,\n" +
                "\t\t\t\"id\":\"\",\n" +
                "\t\t\t\"remark\":\"\",\n" +
                "\t\t\t\"status\":0\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(s);
        String fuels = jsonObject.getString("tFuelNo");
        List<Map> list = JSON.parseArray(fuels, Map.class);
        List<TFuelNo> tFuelNoList = list.stream()
                .map(o -> {
                            return JSON.parseObject(JSON.toJSONString(o), TFuelNo.class);
                        }
                ).collect(Collectors.toList());
        List<String> fuelNoList = tFuelNoList.stream().filter(fil -> fil.getStatus() == 0).map(s1 -> s1.getFlrcNo()).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(fuelNoList));
    }

    /**
     * @ 根据员工id 查询油单总数，国内 国际 离境 数量
     * @duquanhong
     */
    @Override
    public List<TfuelTotal> findRecptCont(TStaff tStaff) {
        return fuelRecptMapper.findRecptCont(tStaff.getStaffAirportCode(), tStaff.getStaffAptareaCode());
    }

    /**
     * 根据员工id查询 加油量多少升 多少吨
     */
    @Override
    public List<TfuelFight> findfightoil(TStaff tStaff) {
        return fuelRecptMapper.findfightoil(tStaff.getStaffAirportCode(), tStaff.getStaffAptareaCode());

    }

    /**
     * 根据输入机位号 机场ID 获取机位信息
     */
    @Override
    public List<TFlightPlace> findflight(TFlight tFlight) {
        return fuelRecptMapper.findflight(tFlight);
    }

    /**
     * 飞机详细信息
     */
    @Override
    public TFlightInfo finddetails(TFlightInfo tFlightInfoList) {
        TFlightInfo info = new TFlightInfo();
        // 如果输入落地航班号不为空
        if (tFlightInfoList.getFlightNA() != null) {
            // 查询加油信息
            MyFlightTask ftaks = fuelRecptMapper.flightask(tFlightInfoList);

            if (ftaks != null && ftaks.getFlgtAAtot() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(ftaks.getFlgtAAtot());
                info.setTimeA(time);
                info.setGateD(ftaks.getFlgtGate());
                if (ftaks.getFlgtAAtot() != null) {
                    info.setStatusA("实际");
                }
            }
            // 如果预计时间不为空
            if (ftaks != null && ftaks.getFlgtDEtot() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(ftaks.getFlgtDEtot());
                // 输出预计起飞时间
                info.setTimeD(time);
            }
            // 如果预计起飞时间不为空
            if (ftaks != null && ftaks.getFlgtDEtot() != null) {
                // 输出预计
                info.setStatusD(StatusConstant.CONS_YJ);

            } else {
                // 输出计划
                info.setStatusD(StatusConstant.CONS_JH);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            // 派发
            String time = "0";
            // 接收
            String time1 = "0";
            // 到位
            String time2 = "0";
            // 开始
            String time3 = "0";
            // 结束
            String time4 = "0";
            // 完成
            String time5 = "0";
            // 派发、接收、到位、开始、结束、完成不为空
            if (ftaks != null && ftaks.getTaskAsgTime() != null) {
                time = simpleDateFormat.format(ftaks.getTaskAsgTime());
            }
            if (ftaks != null && ftaks.getTaskAccTime() != null) {
                time1 = simpleDateFormat.format(ftaks.getTaskAccTime());
            }
            if (ftaks != null && ftaks.getTaskArriveTime() != null) {
                time2 = simpleDateFormat.format(ftaks.getTaskArriveTime());
            }
            if (ftaks != null && ftaks.getTaskChagStaTime() != null) {
                time3 = simpleDateFormat.format(ftaks.getTaskChagStaTime());
            }
            if (ftaks != null && ftaks.getTaskChagEndTime() != null) {
                time4 = simpleDateFormat.format(ftaks.getTaskChagEndTime());
            }
            if (ftaks != null && ftaks.getTaskDoneTime() != null) {
                time5 = simpleDateFormat.format(ftaks.getTaskDoneTime());
            }
            info.setRefuelInfo(time + "," + time1 + "," + time2 + "," + time3 + "," + time4 + "," + time5);
            info.setStatusA(StatusConstant.CONS_SJ);
            info.setFlightNA(tFlightInfoList.getFlightNA());
            // TODO 航班信息无转盘口
            info.setGateA(tFlightInfoList.getGateA());

            // 如果输入起飞航班号为空 则输出孔放弃编辑
            if (ftaks != null && tFlightInfoList.getFlightND() == null) {
                info.setFlightND(ftaks.getFlgtFlno());
            }
            // 已输入的机场id 进离港 为进港 航班号为落地航班号查询航班信息
            TFlightInfo flight = fuelRecptMapper.flightdeta(tFlightInfoList);
            if (flight != null) {
                info.setAircraftID(flight.getAircraftID());
                info.setAirRoute(flight.getAirRoute());
                info.setRangeA(flight.getRangeA());
                info.setRangeD(flight.getRangeD());
            }

        }
        if (tFlightInfoList.getFlightND() != null) {

            // 查询加油信息
            MyFlightTask ftaks = fuelRecptMapper.flightaskl(tFlightInfoList);

            if (null == info) {
                info = new TFlightInfo();
            }

            // 如果预计起飞时间不为空
            if (ftaks != null && ftaks.getFlgtDEtot() != null) {
                // 输出预计
                info.setStatusD(StatusConstant.CONS_YJ);

            } else {
                // 输出计划
                info.setStatusD(StatusConstant.CONS_JH);
            }
            // 如果预计时间不为空
            if (ftaks != null && ftaks.getFlgtDEtot() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(ftaks.getFlgtDEtot());
                // 输出预计起飞时间
                info.setTimeD(ftaks.getFlgtDEtot().toString());
            }
            if (ftaks != null && ftaks.getFlgtDEtot() == null && ftaks.getFlgtDStot() != null) {
                // 输入计划起飞时间
                info.setTimeD(ftaks.getFlgtDStot().toString());
            }
            if (ftaks != null && ftaks.getFlgtAAtot() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(ftaks.getFlgtAAtot());
                info.setTimeA(time);
                if (ftaks.getFlgtAAtot() != null) {
                    info.setStatusA("实际");
                }
            }
            // 如果输入起飞航班号为空 则输出孔放弃编辑
            if (ftaks != null && tFlightInfoList.getFlightNA() == null) {
                if (null != ftaks) {
                    info.setFlightNA(ftaks.getFlgtFlno());
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            // 派发
            String time = "0";
            // 接收
            String time1 = "0";
            // 到位
            String time2 = "0";
            // 开始
            String time3 = "0";
            // 结束
            String time4 = "0";
            // 完成
            String time5 = "0";
            // 派发、接收、到位、开始、结束、完成不为空
            if (ftaks != null && ftaks.getTaskAsgTime() != null) {
                time = simpleDateFormat.format(ftaks.getTaskAsgTime());
            }
            if (ftaks != null && ftaks.getTaskAccTime() != null) {
                time1 = simpleDateFormat.format(ftaks.getTaskAccTime());
            }
            if (ftaks != null && ftaks.getTaskArriveTime() != null) {
                time2 = simpleDateFormat.format(ftaks.getTaskArriveTime());
            }
            if (ftaks != null && ftaks.getTaskChagStaTime() != null) {
                time3 = simpleDateFormat.format(ftaks.getTaskChagStaTime());
            }
            if (ftaks != null && ftaks.getTaskChagEndTime() != null) {
                time4 = simpleDateFormat.format(ftaks.getTaskChagEndTime());
            }
            if (ftaks != null && ftaks.getTaskDoneTime() != null) {
                time5 = simpleDateFormat.format(ftaks.getTaskDoneTime());
            }
            info.setRefuelInfo(time + "," + time1 + "," + time2 + "," + time3 + "," + time4 + "," + time5);
            // 将输入的起飞航班号放入
            info.setFlightND(tFlightInfoList.getFlightND());
            TFlightInfo flight = fuelRecptMapper.flightdetails(tFlightInfoList);
            if (flight != null) {
                info.setAircraftID(flight.getAircraftID());
                info.setAirRoute(flight.getAirRoute());
                info.setRangeA(flight.getRangeA());
                info.setRangeD(flight.getRangeD());
            }
        }
        return info;

    }

    /**
     * 根据加油员ID查询所有下发给加油员的任务
     */
    @Override
    public List<Map<String, Object>> findStaffTask(MyStaff staff) {
        List<MyFlightTask> staffTaskList = fuelRecptMapper.findStaffTask(staff.getLoginUserIn().getStaffId());
        if (staffTaskList != null && staffTaskList.size() > 0) {
            for (int i = 0; i < staffTaskList.size(); i++) {
                String flgtFlno = staffTaskList.get(i).getFlgtFlno();
                if (StringUtils.isNotEmpty(flgtFlno)) {
                    String[] split = flgtFlno.split("/");
                    if (split.length > 0) {
                        staffTaskList.get(i).setFlgtFlno(split[0]);
                    }
                }
                //如果任务状态是7则把它改成6前端判断为6认为油单未上传但任务结束
                if (staffTaskList.get(i).getTaskStatus() == 7) {
                    staffTaskList.get(i).setTaskStatus(6);
                }
                String arcrCustomNum = staffTaskList.get(i).getArcrCustomNum();
                Pair<String, String> pair = taskserviceimpl.pingCountries(staffTaskList.get(i).getFlgtRegn(), staffTaskList.get(i).getFlgtFlno(), true);
                if (ObjectUtil.isEmpty(arcrCustomNum)) {
                    if (!"未知".equals(pair.getRight())) {
                        String right = pair.getRight();
                        if (StringUtils.isNotEmpty(right)) {
                            String[] $s = right.split("&");
                            if ($s.length == 2) {
                                staffTaskList.get(i).setArcrCustomNum($s[1]);
                            }
                        }
                    }
                }
                arcrCustomNum = staffTaskList.get(i).getArcrCustomNum();
                MyCustom cstmName = flightCodeTemporaryMapper.getCstmName(arcrCustomNum);
                if (ObjectUtil.isNotNull(cstmName)) {
                    staffTaskList.get(i).setFlgtAlcname(cstmName.getCstmName());//购买航空公司
                }
                if (!"未知".equals(pair.getRight())) {
                    if (StrUtil.isBlank(staffTaskList.get(i).getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                        staffTaskList.get(i).setFlgtAcname(pair.getLeft());//类型
                    }
                }
                if (!StringUtils.isEmpty(staffTaskList.get(i).getFlgtTrsnm4())) {
                    staffTaskList.get(i).setFlightValic(staffTaskList.get(i).getFlgtTrsnm4());
                }
                if ("2909".equals(staff.getLoginUserIn().getStaffAirportCode())) {
                    String flgtTrs3c1 = staffTaskList.get(i).getFlgtTrs3c1();
                    String flgtTrsnm1 = staffTaskList.get(i).getFlgtTrsnm1();
                    if (StringUtils.isNotEmpty(flgtTrs3c1) && StringUtils.isNotEmpty(flgtTrsnm1)) {
                        staffTaskList.get(i).setFlgtDes3c(flgtTrs3c1);
                        staffTaskList.get(i).setFlgtDesnm(flgtTrsnm1);
                    }
                }
            }
        }
        // 加入新逻辑昆明地区处理 经停航班
        if (staffTaskLock) {
            staffTaskList.stream().map(o -> {
                if (null != o.getFlgtOtatFuel() && 0 != o.getFlgtOtatFuel()) {
                    o.setTaskTotalFuel(o.getFlgtOtatFuel());
                }
                return o;
            }).forEach(staffTask -> {
                if (!StringUtils.isBlank(staffTask.getFlgtFlno()) && !StringUtils.isBlank(staffTask.getFlgtLinkFlno())) {
                    //说明是经停的时候
                    if (staffTask.getFlgtFlno().equals(staffTask.getFlgtLinkFlno())) {
                        MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodes(staff.getLoginUserIn().getStaffAirportCode());
                        if (myAirportCode != null) {
                            String flgtTrs3c5 = staffTask.getFlgtTrs3c5();
                            String flgtTrsnm5 = staffTask.getFlgtTrsnm5();
                            if (!StringUtils.isBlank(flgtTrs3c5) && !StringUtils.isBlank(flgtTrsnm5)) {
                                String[] split = flgtTrs3c5.split("-");
                                String[] split1 = flgtTrsnm5.split("-");
                                if (split.length == split1.length && split.length == 3) {
                                    //判断第一位
                                    MyAirportCode myAirportCodeOne = airportCodeMapper.selectAirportCodes(split[0]);
                                    if (null != myAirportCodeOne && myAirportCodeOne.getApcdAirportProp().equals("I")) {
                                        // 例子 判断当前登录人是不是昆明   a - > 昆明  -> b
                                        if (myAirportCode.getApcdIataCode().equals(split[1])) {
                                            staffTask.setFlgtOrg3c(split[0]);
                                            staffTask.setFlgtOrgnm(split1[0]);
                                            staffTask.setFlgtTrs3c1(split[1]);
                                            staffTask.setFlgtTrsnm1(split1[1]);
                                            staffTask.setFlgtDes3c(split[2]);
                                            staffTask.setFlgtDesnm(split1[2]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
        //t_flight_trouble

        for (MyFlightTask myFlightTask : staffTaskList) {
            Integer flrcType = 3;
            if (myFlightTask.getFlgtFlti() != null) {
                flrcType = flightService.pingSingleType(myFlightTask);
            }
            myFlightTask.setFlrcType(flrcType);
        }

        // 客户信用逻辑
        List<TCreditInfo> tCreditInfos = tCreditInfoMapper.selectCreditInfo(new TCreditInfo());


        return staffTaskList != null ? staffTaskList.stream()
                                       .peek(flightInfoById -> {
                                           //航班号裁切 2019年8月12日17:01:38
                                           if (StringUtils.isNotEmpty(flightInfoById.getFlgtFlno())) {
                                               String flgtFlno = flightInfoById.getFlgtFlno();
                                               Arrays.stream(flgtFlno.split("/")).filter(no -> StringUtils.contains(no, flightInfoById.getFlgtAl2c())).findAny().ifPresent(flightInfoById::setFlgtFlno);
                                           }
                                       }).map(o -> {
            Map<String, Object> objectHashMap = Maps.newHashMap();
            objectHashMap.put("MyTaskFlight", o);
            if (StringUtils.isNotEmpty(o.getTaskFuelRecptNo())) {
                MyFuelRecpt fuelByNo = fuelRecptMapper.findFuelByNo(o.getTaskFuelRecptNo());
                if (null != fuelByNo) {
                    objectHashMap.put("MyFuel", fuelByNo);
                } else {
                    objectHashMap.put("MyFuel", null);
                }
            }
            if (StringUtils.isNotEmpty(o.getOrderNo())) {

                TOrderInfo tOrderInfo = tOrderInfoMapper.selectByOrderNo(o.getOrderNo());
//                        tOrderInfoMapper.selectByPrimaryKey(Long.valueOf(o.getOrderNo()));
                if (null != tOrderInfo) {
                    objectHashMap.put("orderInfo", tOrderInfo);
                }
            }

            if (StringUtils.isNotEmpty(o.getFlgtRegn())) {
                TFlightTrouble tFlightTrouble = new TFlightTrouble();
                tFlightTrouble.setFlgtRegn(o.getFlgtRegn());
                List<TFlightTrouble> tFlightTroubles = tFlightTroubleMapper.selectByRegn(tFlightTrouble);
                if (tFlightTroubles.size() > 0) {
                    objectHashMap.put("tFlightTroubles", tFlightTroubles);
                } else {
                    objectHashMap.put("tFlightTroubles", null);
                }
            }
            if (StringUtils.isNotEmpty(o.getArcrCustomNum())) {
                tCreditInfos.stream().filter(dto ->
                        ObjectUtil.contains(o.getArcrCustomNum(), dto.getCstno()))
                .findFirst()
                .ifPresent(dto -> {
                    objectHashMap.put("tCreditInfo", dto);
                });
            }
            return objectHashMap;
        }).collect(Collectors.toList()) : null;
    }

    /**
     * 查询最近七天的油单数据
     */
    @Override
    public List<MyFuelRecpt> findfuelrecpt(MyStaff staff, MyFuelRecpt myFuelRecpt) {
        String flrcDate = myFuelRecpt.getFlrcDate() == null ? null : cn.hutool.core.date.DateUtil.formatDate(myFuelRecpt.getFlrcDate());
        List<MyFuelRecpt> findfuelrecpt = Lists.newArrayList();
        if ("0".equals(staff.getLoginUserIn().getStaffType())) {
            findfuelrecpt = fuelRecptMapper.findfuelrecpt(null, null, flrcDate);
        } else {
            findfuelrecpt = fuelRecptMapper.findfuelrecpt(staff.getLoginUserIn().getStaffAirportCode(),
                    staff.getLoginUserIn().getStaffAptareaCode(), flrcDate);
        }
        findfuelrecpt.forEach(f -> f.setFlrcSigned(StrUtil.isBlank(f.getFlrcSign()) ? 0 : 1));
        return findfuelrecpt;
    }

    /**
     * 查询当天的油单数据
     */
    @Override
    public List<MyFuelRecpt> findtodayfuelrecpt(MyStaff staff) {
        if ("0".equals(staff.getLoginUserIn().getStaffType())) {
            return fuelRecptMapper.findtodayfuelrecpt(null, null);
        } else {
            return fuelRecptMapper.findtodayfuelrecpt(staff.getLoginUserIn().getStaffAirportCode(),
                    staff.getLoginUserIn().getStaffAptareaCode());
        }
        // flrcType = flightService.pingSingleType(taskAndFlightInfo.getFlgtRegn(),taskAndFlightInfo.getFlgtFlti(),taskAndFlightInfo.getTaskContent());
    }
//		for(MyFlightTask mytask:taskList){
//			HashMap<String,Object> taskAndRecptmap=new HashMap<String, Object>();
//			MyFuelRecpt myFuelRecpt=fuelRecptMapper.findFuelByNo(mytask.getTaskFuelRecptNo());
//			if (myFuelRecpt!=null&&myFlight!=null){
//				if(!StringUtils.isEmpty(mytask.getTaskId())){
//					myFuelRecpt.setTaskId(mytask.getTaskId());
//				}else{
//					myFuelRecpt.setTaskId("");
//				}
//				taskAndRecptmap.put("tTask",mytask);
//				taskAndRecptmap.put("tFuelRecpt",myFuelRecpt);
//				taskAndRecptmap.put("tFlight",myFlight);
//				taskAndRecptList.add(taskAndRecptmap);
//			}
//		}
//	}

    /**
     * 查询当天的油单数据
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectTaskAndRecpt(MyStaff staff, Integer dayType) {
        dayType = dayType == null ? 0 : dayType;
        LocalDate now = LocalDate.now().minusDays(dayType);
        List<MyFuelRecpt> myFuelRecpts = fuelRecptMapper.findFuelByStaffId(staff.getLoginUserIn().getStaffId(), now.format(DateTimeFormatter.BASIC_ISO_DATE));
        return myFuelRecpts.stream()
                .filter(mytask -> StringUtils.isNotEmpty(mytask.getFlrcNo()))
                .map(mytask -> {
                    Map<String, Object> result = Maps.newHashMap();
                    MyFlightTask myFlightTask = taskMapper.selectTaskByRecptNo(mytask.getFlrcNo());
                    if (myFlightTask != null) {
                        if (!StringUtils.isEmpty(myFlightTask.getTaskId())) {
                            mytask.setTaskId(myFlightTask.getTaskId());
                        } else {
                            mytask.setTaskId("");
                        }
                        Integer ftyp = flightService.pingSingleType(myFlightTask);
                        myFlightTask.setFlrcType(ftyp);
                        result.put("MyFuel", mytask);
                        result.put("MyTaskFlight", myFlightTask);
                    }
                    return result;
                }).collect(Collectors.toList());
    }

    /**
     * 修改油单信息手动修改标记，人员，时间
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updatefuel(MyFuelRecpt myFuelRecpt, MyStaff staff) {
        MyFuelRecpt myFuelRecpt1 = new MyFuelRecpt();
        myFuelRecpt1.setFlrcId(myFuelRecpt.getFlrcId());
        String publicFlrcNo = null;
        List<MyFuelRecpt> findfuelretirevelist = fuelRecptMapper.findfuelretirevelist(myFuelRecpt1);
        if (findfuelretirevelist.size() > 0) {
            if (null != findfuelretirevelist.get(0).getFlrcType() && null != myFuelRecpt.getFlrcType()) {
                if (findfuelretirevelist.get(0).getFlrcType() != myFuelRecpt.getFlrcType()) {
                    String fule = tFuelNoService.updateFule(findfuelretirevelist.get(0).getFlrcNo(),
                            myFuelRecpt.getFlrcType(),
                            staff.getLoginUserIn().getStaffAptareaCode());
                    Assert.notNull(fule, "油单编号获取失败!");
                    publicFlrcNo = fule;
                    myFuelRecpt.setFlrcNo(fule);
                }
            }
        }


        if (fuelRecptMapper.updatefuel(myFuelRecpt) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        Integer integer = tFuelNoService.updatefuleNoEnd(publicFlrcNo);
        Assert.notNull(integer, "油单编号获取失败!");

    }

    /**
     * 根据油单id对油单回收
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void uprecyclefuel(MyFuelRecpt myFuelRecpt) {
        if (fuelRecptMapper.uprecyclefuel(myFuelRecpt) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }

    }

    /**
     * 根据油单id进行标记油单
     */
    @Override
    public ReturnMsg<Object> mark(MyFuelMark myFuelMark) {

        MyFuelRecpt fuelRecpt = new MyFuelRecpt();
        fuelRecpt.setFlrcId(myFuelMark.getFlrcId());
        fuelRecpt.setFlrcRevwStatus(myFuelMark.getFlrcRevwStatus());
        int i = fuelRecptMapper.updateByPrimaryKeySelective(fuelRecpt);
        if (i == 1) {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, fuelRecpt);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_ERR, null, fuelRecpt);
        }
    }

    /**
     * 根据油单Id对油单进行审核
     */
    @Override
    //@Transactional(rollbackFor = java.lang.Exception.class)
    public ReturnMsg<List<String>> auditfuel(MyFuelId flrcId) {
        List<String> myFuelRecpts = Lists.newArrayList();
        String nofindcode = "";
        String noFlrcQuantity = "";
        String error = "";
        for (int i = 0; i < flrcId.getFlrcId().length; i++) {
            MyFuelRecpt temp = new MyFuelRecpt();
            temp.setFlrcId(flrcId.getFlrcId()[i]);
            MyFuelRecpt myFuelRecpt = fuelRecptMapper.findFuellist(temp);
            if (myFuelRecpt != null && StringUtils.isNotEmpty(myFuelRecpt.getFlrcAircrftNo())) {
                String flgtRegn = myFuelRecpt.getFlrcAircrftNo();
                String flgtNo = myFuelRecpt.getFlrcFlightNo();
                if (StringUtils.isNotEmpty(flgtRegn)) {
                    if (null != myFuelRecpt.getFlrcQuantity() && myFuelRecpt.getFlrcQuantity().intValue() != 0
                            && null != myFuelRecpt.getFlrcFuelVol() && myFuelRecpt.getFlrcFuelVol().intValue() != 0
                            && null != myFuelRecpt.getFlrcFuelDnst() && myFuelRecpt.getFlrcFuelDnst().doubleValue() != 0
                    ) {
                        BigDecimal bigDecimal = myFuelRecpt.getFlrcFuelVol().multiply(myFuelRecpt.getFlrcFuelDnst()).setScale(0, BigDecimal.ROUND_HALF_UP);
                        if (bigDecimal.compareTo(myFuelRecpt.getFlrcQuantity()) != 0) {
                            //说明不相符
                            error += "," + flgtRegn;
                            break;
                        }

                    }

                    log.info("油单审核校验获取飞机信息：飞机号-{}，航班号-{}", flgtRegn, flgtNo);
                    //获取飞机号归属信息
                    MyFlightCode myFlightCode = taskserviceimpl.getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtNo);
                    if (myFlightCode != null) {
                        //flrcFuelVol;flrcFiguars  flrcQuantity
                        if (null == myFuelRecpt.getFlrcQuantity() || myFuelRecpt.getFlrcQuantity().intValue() == 0
                                || null == myFuelRecpt.getFlrcFuelVol() || myFuelRecpt.getFlrcFuelVol().intValue() == 0
                                || null == myFuelRecpt.getFlrcFiguars() || myFuelRecpt.getFlrcFiguars().intValue() == 0
                        ) {
                            noFlrcQuantity += "," + flgtRegn;
                        } else {
                            if (fuelRecptMapper.auditfuel(flrcId.getFlrcId()[i], 1) != 1) {
                                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                            } else {
                                myFuelRecpts.add(flrcId.getFlrcId()[i]);
                            }
                        }

                    } else if (StringUtils.isNotEmpty(myFuelRecpt.getFlrcFlightNo())) {
                        MyFlightCode myFlightCodes = flightMapper.getFlightCodeByflgtRegn(myFuelRecpt.getFlrcFlightNo());
                        if (myFlightCodes != null) {
                            //flrcFuelVol;flrcFiguars  flrcQuantity
                            if (null == myFuelRecpt.getFlrcQuantity() || myFuelRecpt.getFlrcQuantity().intValue() == 0
                                    || null == myFuelRecpt.getFlrcFuelVol() || myFuelRecpt.getFlrcFuelVol().intValue() == 0
                                    || null == myFuelRecpt.getFlrcFiguars() || myFuelRecpt.getFlrcFiguars().intValue() == 0
                            ) {
                                noFlrcQuantity += "," + flgtRegn;
                            } else {
                                if (myFuelRecpt.getFlrcFuelDnst() != null) {
                                    BigDecimal multiply = myFuelRecpt.getFlrcFuelVol().multiply(myFuelRecpt.getFlrcFuelDnst());
                                    BigDecimal bigDecimal = multiply.setScale(0, BigDecimal.ROUND_UP);
                                    BigDecimal result2 = myFuelRecpt.getFlrcQuantity().subtract(bigDecimal).abs();
                                    if (3 < result2.intValue()) {
                                        noFlrcQuantity += "," + flgtRegn;
                                    }
                                }
                                if (fuelRecptMapper.auditfuel(flrcId.getFlrcId()[i], 1) != 1) {
                                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                                } else {
                                    myFuelRecpts.add(flrcId.getFlrcId()[i]);
                                }
                            }
                        } else {
                            nofindcode += "," + myFuelRecpt.getFlrcFlightNo();
                        }
                    } else {
                        nofindcode += "," + flgtRegn;
                    }
                } else if (StringUtils.isNotEmpty(myFuelRecpt.getFlrcFlightNo())) {
                    MyFlightCode myFlightCode = flightMapper.getFlightCodeByflgtRegn(myFuelRecpt.getFlrcFlightNo());
                    if (myFlightCode != null) {
                        if (null == myFuelRecpt.getFlrcQuantity() || myFuelRecpt.getFlrcQuantity().intValue() == 0
                                || null == myFuelRecpt.getFlrcFuelVol() || myFuelRecpt.getFlrcFuelVol().intValue() == 0
                                || null == myFuelRecpt.getFlrcFiguars() || myFuelRecpt.getFlrcFiguars().intValue() == 0
                        ) {
                            noFlrcQuantity += "," + flgtRegn;
                        } else {
                            if (myFuelRecpt.getFlrcFuelDnst() != null) {
                                BigDecimal multiply = myFuelRecpt.getFlrcFuelVol().multiply(myFuelRecpt.getFlrcFuelDnst());
                                BigDecimal bigDecimal = multiply.setScale(0, BigDecimal.ROUND_UP);
                                BigDecimal result2 = myFuelRecpt.getFlrcQuantity().subtract(bigDecimal).abs();
                                if (3 < result2.intValue()) {
                                    noFlrcQuantity += "," + flgtRegn;
                                }
                            }
                            if (fuelRecptMapper.auditfuel(flrcId.getFlrcId()[i], 1) != 1) {
                                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                            } else {
                                myFuelRecpts.add(flrcId.getFlrcId()[i]);
                            }
                        }
                    } else {
                        nofindcode += "," + myFuelRecpt.getFlrcFlightNo();
                    }
                } else {
                    nofindcode += "," + flgtRegn;
                }
                // flrc_quantity    noFlrcQuantity
            }
        }
        String msg = "";
        String msgNoFlrcQuantity = "";
        if (nofindcode.length() > 0) {
            nofindcode = nofindcode.substring(1);
            msg = "以下飞机号未找到购买方(" + nofindcode + ")";
        }
        if (error.length() > 0) {
            error = error.substring(1);
            msg = "以下飞机号加油体积,质量,密度存在异常(" + error + ")";
        }
        if (noFlrcQuantity.length() > 0) {
            noFlrcQuantity = noFlrcQuantity.substring(1);
            msgNoFlrcQuantity = "以下飞机号加油质量为0(" + noFlrcQuantity + ")";
        }

        if (msg != null && !"".equals(msg)) {
            if (msgNoFlrcQuantity != null && !"".equals(msgNoFlrcQuantity)) {
                throw new CustomException(new ReturnMsg<List<String>>(Constant.CODE_ERR, msg + msgNoFlrcQuantity, myFuelRecpts));
            } else {
                throw new CustomException(new ReturnMsg<List<String>>(Constant.CODE_ERR, msg, myFuelRecpts));
            }
        } else {
            if (msgNoFlrcQuantity != null && !"".equals(msgNoFlrcQuantity)) {
                throw new CustomException(new ReturnMsg<List<String>>(Constant.CODE_ERR, msgNoFlrcQuantity, myFuelRecpts));
            } else {
                return new ReturnMsg<List<String>>(Constant.CODE_OK, null, myFuelRecpts);
            }
        }
    }

    /**
     * 根据油单id获取油单详情
     */
    @Override
    public MyFuelRecpt findFuellist(MyFuelRecpt myFuelRecpt) {
        return fuelRecptMapper.findFuellist(myFuelRecpt);
    }

    @Override
    public List<MyFuelRecpt> findfuelretirevelist(MyFuelRecpt myFuelRecpt) {
        return fuelRecptMapper.findfuelretirevelist(myFuelRecpt);
    }

    /**
     * 根据加油员id查询任务信息（垮库）
     */
    @Override
    public MyTask gettaskById(String sfvhStaffId) {
        return fuelRecptMapper.gettaskById(sfvhStaffId);
    }

    /**
     * 根据加油员id查询任务信息（垮库）
     */
    @Override
    public List<MyTask> gettaskListById(List<MyStaffVehiTask> list) {
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
        return fuelRecptMapper.gettaskListById(list, nowStr);
    }

    /**
     * 油单生成
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void findFuelrecptlist(MyFuelRecpt myFuelRecpt, MyTask task, MyStaff staff) {
        // 创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String taskChagEndTime = sim.format(task.getTaskChagEndTime());
        /*MyTask myTask = taskMapper.getTaskInfo(task.getTaskId());
        if (myTask != null && myTask.getTaskStatus() != null) {
            if (task != null && task.getTaskStatus() != null) {
                if (myTask.getTaskStatus() > task.getTaskStatus()) {
                    task.setTaskStatus(myTask.getTaskStatus());
                }
            }
        }*/
        // 修改任务表
        if (fuelRecptMapper.updateFueltask(myFuelRecpt.getFlrcNo(), taskChagEndTime, task.getTaskId(),
                task.getTaskStatus()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        // 创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        // 根据任务ID查询任务单条信息
        MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
        MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        //把查出来的人员ID赋值到人员车辆任务对象中
        staffTask.setSfvhStaffId(staffinfo.getStaffId());
        //把查出来的人员电话赋值到人员车辆任务对象中
        staffTask.setStaffPhone(staffinfo.getStaffPhone());
        staffTask.setStaffName(staffinfo.getStaffName());
        taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
        // 把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfo);

        String AptareaCode = Constant.judgeAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        String staffDateIng = null;
        if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
            staffDateIng = String.valueOf(redis.get(Constant.LOGIN_KEY + staff.getLoginUserIn().getStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng"));
        }

        //连续工作时间
        if (null == staffDateIng || "null".equals(staffDateIng)) {
            staffTask.setStaffDateIng(null);
        } else {
            staffTask.setStaffDateIng(staffDateIng);
        }
        //连续休息时间
        redis.set(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime", "");
        //定义空变量用来接收连续工作数量
        Integer taskCount = 0;
        if (null != redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
            taskCount = (Integer) redis.get(Constant.LOGIN_KEY + taskInfo.getTaskOpeStaffId() + ":" + staff.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount");
            staffTask.setStaffingTaskCount(taskCount);
        }
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
        // TODO 死胖子添加逻辑
        if (null != taskInfo.getTaskStatus() && null != taskInfo.getTaskOpeStaffId()) {
            staffTask.setSfvhStaffId(taskInfo.getTaskOpeStaffId());
            staffTask.setTaskStatus(taskInfo.getTaskStatus());
        }
        staffTask.setSfvhStaffStatus(2);
        //把要推送的对象放进Map集合中
        webMap.put("staff", staffTask);
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
        taskserviceimpl.sendToKafkaTask(taskInfo, taskAndFlightById);

        // 判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            // 把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        Integer type = null;
        type = flightService.pingSingleType(taskAndFlightById);
        taskAndFlightById.setFlrcType(type);
        taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        webMap.put("flight", taskAndFlightById);
        // 垮库查询获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffList(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_TASK_STATE_UPDATE, webMap);
        Date date = new Date();
        RefuelingTask flightInfo = taskserviceimpl.getFlightInfo(task.getTaskId());
        flightInfo.setEndOilTime(date);
        taskserviceimpl.send(flightInfo);
    }

    // 自动回收
    public void automaticRecover(String flno) {
        tFuelNoMapper.automaticRecover(flno, 2);
    }

    /**
     * PAD上传油单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer UpFuelRecpt(MyFuelRecpt fuelRecpt, MyStaff staff) {
        if (null == fuelRecpt.getFlrcBusType() || 0 == fuelRecpt.getFlrcBusType()) {
            fuelRecpt.setFlrcBusType(2);
        }

        //staff_scheduling_id flgtStatus = 2 的时候 重置成 0 取消任务也重置
        Integer flrcType = compareType(fuelRecpt.getFlrcType());
        fuelRecpt.setFlrcType(flrcType);
        if (StringUtils.isEmpty(fuelRecpt.getFlrcVehiNum())) {
            fuelRecpt.setFlrcVehiNum(fuelRecpt.getFlrcVehiNo());
        }
        if (null != fuelRecpt.getFlrcDate()) {
            fuelRecpt.setFlrcWkopDate(fuelRecpt.getFlrcDate());
        }
        MyTask taskInfo = taskMapper.getTaskInfo(fuelRecpt.getTaskId());

        fuelRecpt.setStaffSchedulingId(taskInfo.getTaskCreStaffId());

        //校验加油量
        validFuelQuantity(fuelRecpt, staff);
        //校验油单号格式
        validFuelNoFormat(fuelRecpt, staff);
        //TODO 未处理重复上传
        //校验是否存一个任务出现多次油单上传，校验一个航班是否存在多次加油任务
        Boolean noCheckRepetition = fuelRecpt.getNoCheckRepetition();
        if (!noCheckRepetition) {
            checkRepetition(fuelRecpt, taskInfo, staff);
        }
        Objects.requireNonNull(fuelRecpt.getFlrcNo(), "加油单号不能为空");
        MyFuelRecpt myFuelRecpt = new MyFuelRecpt();
        myFuelRecpt.setFlrcNo(fuelRecpt.getFlrcNo());
        List<MyFuelRecpt> findfuelretirevelist = fuelRecptMapper.findfuelretirevelist(myFuelRecpt);
        if (findfuelretirevelist.size() > 0) {
            System.out.println("油单编号重复请查询后在新增!" + fuelRecpt.getFlrcNo());
            throw new CustomException(ReturnMsg.getInstanceNGz("408", "油单编号重复请查询后在新增!", findfuelretirevelist.get(0), HttpStatus.REQUEST_TIMEOUT.value()));
        }

        MyTask taskInfoCommon = taskMapper.getTaskInfo(fuelRecpt.getTaskId());
        System.out.println("----------------进来啦----------------------");
        System.out.println("pad调用油单新增 油单号:" + fuelRecpt.getFlrcNo() + "Json:" + JSON.toJSONString(staff));
//        String fuelNoLock = "fuelNoLockNew";
//        RLock lock = null;
//        try {
//            //分布式锁
//            lock = distributedLocker.lock(fuelNoLock, 10L);
//            System.out.println("--------------加锁-----" + Thread.currentThread().getId());
//            boolean res = lock.tryLock(25, 10, TimeUnit.SECONDS);
//            if (res) {
//                System.out.println("--------------持有锁-----" + Thread.currentThread().getId());
//                try {
//                    // 回收
//                    tFuelNoService.recyclingFlueNoInsert(fuelRecpt, staff);
//                } finally {
//                    System.out.println("--------------解锁-----" + Thread.currentThread().getId());
//                    if(lock.isLocked()){ // 是否还是锁定状态
//                        if(lock.isHeldByCurrentThread()){ // 时候是当前执行线程的锁
//                            lock.unlock(); // 释放锁
//                        }
//                    }
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // 日期格式化
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format2 = format.format(date);
        // 油单ID
        fuelRecpt.setFlrcId(UUIDUitl.getUUID());
        fuelRecpt.setFlrcAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        fuelRecpt.setFlrcAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        fuelRecpt.setFlrcDeliverName(staff.getLoginUserIn().getStaffName());
        fuelRecpt.setFlrcDeliverId(staff.getLoginUserIn().getStaffId());
        fuelRecpt.setFlrcFuelVol(fuelRecpt.getFlrcFiguars());
        if (fuelRecpt.getFlrcFnshTime() == null) {
            fuelRecpt.setFlrcFnshTime(new Date());
        }
        if (fuelRecpt.getFlrcStatTime() == null) {
            Calendar nowTime1 = Calendar.getInstance();
            nowTime1.add(Calendar.MINUTE, -30);
            fuelRecpt.setFlrcStatTime(nowTime1.getTime());
        }
        MyTask taskss = new MyTask();
        taskss.setTaskOpeStaffId(staff.getLoginUserIn().getStaffId());
        //获取加油单序列号
        if (findfuelretirevelist.size() > 0) {
            MyFuelRecpt myFuelRecpt1 = findfuelretirevelist.get(0);
            if (
                    myFuelRecpt1.getFlrcFlightNo().equals(myFuelRecpt.getFlrcFlightNo())
                            && myFuelRecpt1.getFlrcVehiNo().equals(myFuelRecpt.getFlrcVehiNo())
                            && myFuelRecpt1.getFlrcAircrftNo().equals(myFuelRecpt1.getFlrcAircrftNo())
            ) {
                return 1;
            }
            System.out.println("油单编号重复请查询后在新增!" + fuelRecpt.getFlrcNo());
            throw new CustomException(ReturnMsg.getInstanceNGz("408", "油单编号重复请查询后在新增!", null, HttpStatus.REQUEST_TIMEOUT.value()));
        } else {
            if (null != taskInfoCommon && StringUtils.isNotEmpty(taskInfoCommon.getFlrcBwtar())) {
                fuelRecpt.setFlrcBwtar(taskInfoCommon.getFlrcBwtar());
            }
            //flrcFiguars
            if (fuelRecpt.getFlrcFiguars().doubleValue() < 0D) {
                fuelRecpt.setFlrcBusType(0);
                fuelRecpt.setFlrcUpdateErrMsg("加油量为负数,重置保税类型");
            }
            validFuelRecpt(fuelRecpt, staff);
            // 油单插入
            int i = fuelRecptMapper.upFuelrecpt(fuelRecpt);
            // 回收油单号
            automaticRecover(fuelRecpt.getFlrcNo());
            if (i != 1) {
                System.out.println("插入油单数据失败:返回值:" + i + "Json:" + JSON.toJSONString(staff));
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            // 油单历史记录
            pcUpdateFuelLog(fuelRecpt, staff);
            MyTask myTask = new MyTask();
            myTask.setTaskId(fuelRecpt.getTaskId());
            myTask.setTaskRcPrintTime(new Date());
            myTask.setTaskFuelRecptNo(fuelRecpt.getFlrcNo());
            //如果是getFlrcManualTag==1 说明调用的是异常油单修改之后的上传所以任务不需要修改
            //把任务状态改成6 -油单已经上传
            if (fuelRecpt.getFlrcManualTag() == 0)
                myTask.setTaskStatus(6);
            //更改对应的任务信息中的油单上传时间
            int i1 = flightMapper.updateTask(myTask);
            //int i1 = fuelRecptMapper.updateTask(fuelRecpt.getTaskId(), format2);
            if (i1 != 1) {
                System.out.println("更改对应的任务信息中的油单上传时间失败:返回值:" + i1 + "fuelRecpt.getTaskId():" + fuelRecpt.getTaskId() + "format2:" + format2);
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            RefuelingTask flightInfo = taskserviceimpl.getFlightInfo(fuelRecpt.getTaskId());
            flightInfo.setCompleteTime(date);
            // 根据任务ID查询任务单条信息

            // 根据任务ID查出单条任务航班信息
            MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(fuelRecpt.getTaskId());
            taskserviceimpl.sendToKafkaTask(taskInfo, taskAndFlightById);
            taskserviceimpl.send(flightInfo);
            if (null != taskAndFlightById) {
                fuelRecpt.setFlgtOlvr(taskAndFlightById.getFlgtOlvr());
            }
            //校验油单，油单上传状态不正常，则不上传油单
            if (Integer.valueOf(1).equals(fuelRecpt.getFlrcStatus())) {
                log.error(fuelRecpt.getFlrcUpdateErrMsg());
                return 0;
            }
            // 注册事务同步回调
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    // 这里是在事务提交后执行的操作
                    CompletableFuture.runAsync(() -> {
                        try {
                            //TODO 可能有bug 无法推送
                            // kafka给ERP推送
                            //推送到kafka，上传总部
                            send2kafkaByTopicZsdOilGeneration(fuelRecpt);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
        return 0;
    }

    /**
     * 功能描述：校验油单号格式
     *
     * @param fuelRecpt
     * @param staff
     * @return void
     * @author zhaojiacan
     * @date 2024/9/9
     */
    private void validFuelNoFormat(MyFuelRecpt fuelRecpt, MyStaff staff) {
        String apcdCnafAirportCode = staff == null ? StrUtil.EMPTY : Optional.ofNullable(staff.getLoginUserIn()).map(LoginUser::getStaffAirportCode).orElse(StrUtil.EMPTY);
        String flrcNo = fuelRecpt.getFlrcNo();
        if (StrUtil.isBlank(flrcNo)) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号为空，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate())
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        if (flrcNo.length() != 13) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号长度错误，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate())
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        String flrcNoPrefix = new StringBuilder(apcdCnafAirportCode).append(fuelRecpt.getFlrcType()).toString();
        String regex = flrcNoPrefix + "\\d{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(flrcNo);
        if (!matcher.matches()) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号格式错误，油单前缀是【%s】，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    flrcNoPrefix
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
    }

    /**
     * 功能描述：校验加油量
     *
     * @param fuelRecpt 上传的油单信息
     * @return void
     * @author zhaojiacan
     * @date 2024/9/9
     */
    private void validFuelQuantity(MyFuelRecpt fuelRecpt, MyStaff staff) {
        String apcdCnafAirportCode = staff == null ? StrUtil.EMPTY : Optional.ofNullable(staff.getLoginUserIn()).map(LoginUser::getStaffAirportCode).orElse(StrUtil.EMPTY);
        BigDecimal flrcFuelVol = fuelRecpt.getFlrcFuelVol();
        if (flrcFuelVol == null) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号【%s】，加油升数为空，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    fuelRecpt.getFlrcNo()
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        if (flrcFuelVol.compareTo(BigDecimal.ZERO) <= 0) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号【%s】，加油升数为负数或0【%.4f】升，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    fuelRecpt.getFlrcNo(),
                    flrcFuelVol
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        BigDecimal flrcQuantity = fuelRecpt.getFlrcQuantity();
        if (flrcQuantity == null) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号【%s】，加油重量为空，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    fuelRecpt.getFlrcNo()
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        if (flrcQuantity.compareTo(BigDecimal.ZERO) < 0) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，油单号【%s】，加油重量为负数【%.4f】KG，请修正",
                    fuelRecpt.getFlrcAircrftNo(),
                    fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    fuelRecpt.getFlrcNo(),
                    flrcQuantity
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
    }

    /**
     * kafka给ERP推送，topic为：【ZSD_OIL_GENERATION】
     */

    /**
     * 功能描述：校验油单是否重复上传
     *
     * @param fuelRecpt 上传的油单信息
     * @param taskInfo  任务信息
     * @param staff     加油员信息
     * @return void
     * @author zhaojiacan
     * @date 2024/5/31
     */
    private void checkRepetition(MyFuelRecpt fuelRecpt, MyTask taskInfo, MyStaff staff) {
        if (taskInfo == null) {
            return;
        }
        String apcdCnafAirportCode = staff == null ? StrUtil.EMPTY : Optional.ofNullable(staff.getLoginUserIn()).map(LoginUser::getStaffAirportCode).orElse(StrUtil.EMPTY);
        //判断任务上的油单号是否和新上传的一致，不一致，则同一个任务出现多个油单号-不允许
        if (StrUtil.isNotBlank(taskInfo.getTaskFuelRecptNo()) && !fuelRecpt.getFlrcNo().equals(taskInfo.getTaskFuelRecptNo())) {
            String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】，同一个任务出现多个油单号：%s，任务ID：%s，请勿重复上传",
                    fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo(),
                    apcdCnafAirportCode,
                    cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                    taskInfo.getTaskFuelRecptNo(),
                    taskInfo.getTaskId()
            );
            throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        }
        List<MyTask> taskList = taskMapper.findTasksByflgtId(fuelRecpt.getFlgtId());
        if (Integer.valueOf(0).equals(taskInfo.getTaskContent())) {
            //判断同一个飞机是否存在多次加油任务
            Optional<MyTask> taskListOptional = taskList.stream().filter(
                    task -> !task.getTaskId().equals(taskInfo.getTaskId())
                            && Integer.valueOf(0).equals(task.getTaskContent())
                            && StrUtil.isNotBlank(task.getTaskFuelRecptNo())
                            && (task.getTaskStatus().equals(6) || task.getTaskStatus().equals(7))
            ).findAny();
            if (taskListOptional.isPresent()) {
                String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】已经存在加油任务，任务油单号：%s，请勿重复进行加油任务",
                        fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo(),
                        apcdCnafAirportCode,
                        cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                        taskListOptional.get().getTaskFuelRecptNo()
                );
                throw new CustomException(ReturnMsg.getInstanceNGz("424", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
            }
        } else if (Integer.valueOf(2).equals(taskInfo.getTaskContent())) {
            //判断同一个飞机是否存在多次补油任务
            List<MyTask> supplementOilTasks = taskList.stream().filter(
                    task -> !task.getTaskId().equals(taskInfo.getTaskId())
                            && Integer.valueOf(2).equals(task.getTaskContent())
                            && StrUtil.isNotBlank(task.getTaskFuelRecptNo())
                            && (task.getTaskStatus().equals(6) || task.getTaskStatus().equals(7))
            ).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(supplementOilTasks)) {
                for (MyTask supplementOilTask : supplementOilTasks) {
                    String taskFuelRecptNo = supplementOilTask.getTaskFuelRecptNo();
                    if (StrUtil.isBlank(taskFuelRecptNo)) {
                        continue;
                    }
                    MyFuelRecpt tempFuelRecpt = fuelRecptMapper.findFuelByNo(taskFuelRecptNo);
                    BigDecimal flrcFuelVol = Optional.ofNullable(tempFuelRecpt).map(MyFuelRecpt::getFlrcFuelVol).orElse(BigDecimal.ZERO);
                    //两次补油任务加油量相差在5L及以内，认为是一个油单
                    if (flrcFuelVol.subtract(Optional.ofNullable(fuelRecpt.getFlrcFuelVol()).orElse(BigDecimal.ZERO)).abs().compareTo(new BigDecimal("5")) <= 0) {
                        String message = String.format("飞机号【%s】，航班号【%s】，加油机场【%s】，加油日期【%s】存在一个加油量和此次油单加油量相似的油单，油单号：%s，请确认是否是同一个加油任务，是否继续上传？",
                                fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo(),
                                apcdCnafAirportCode,
                                cn.hutool.core.date.DateUtil.formatDate(fuelRecpt.getFlrcDate()),
                                supplementOilTask.getTaskFuelRecptNo()
                        );
                        throw new CustomException(ReturnMsg.getInstanceNGz("423", message, null, HttpStatus.UNPROCESSABLE_ENTITY.value()));
                    }
                }
            }
        }
    }

    /**
     * kafka给ERP推送，topic为：【ZSD_OIL_GENERATION】
     */
    @Async
    protected void send2kafkaByTopicZsdOilGeneration(MyFuelRecpt fuelRecpt) {
        // TAirportCode tAirportCode = myTAirportCodeMapper.selectByCnafAirportCode(fuelRecpt.getFlrcAirportCode());
        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcAirportCode())) {
            /*int flrcType = 3; // 默认为 国内
            if (null != fuelRecpt.getFlrcType()) {
                flrcType = compareType(fuelRecpt.getFlrcType());
            }
            if ("2901".equals(fuelRecpt.getFlrcAirportCode()) || "2210".equals(fuelRecpt.getFlrcAirportCode())) {
                if (flrcType == 3) {
                    fuelRecpt.setFlrcBwtar("FB");
                } else {
                    fuelRecpt.setFlrcBwtar("B");
                }
            } else {
                fuelRecpt.setFlrcBwtar("FB");
            }*/
        }
        fuelRecpt.setFlrcStatus(3);
        kafkaTemplate.send("upload_fuel", JSON.toJSONString(fuelRecpt));
        log.debug("这是往kafka中推送消息，消息主题是【upload_fuel】，消息内容是：" + JSON.toJSONString(fuelRecpt));
    }

    /**
     * 中控机上传油单
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void upFuelIn(MyFUel fuel, MyFuelRecpt fuelRecpt) {
        // 创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(fuelRecpt.getFlrcAirportCode());
        staffInfo.setStaffAptareaCode(fuelRecpt.getFlrcAptareaCode());
        // 日期格式化
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        if (!StringUtils.isEmpty(fuel.getDate())) {
            try {
                date1 = format.parse(fuel.getDate());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        Date date3 = null;
        if (!StringUtils.isEmpty(fuel.getTimeStart())) {
            try {
                date3 = format.parse(fuel.getTimeStart());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date datet = null;
        if (!StringUtils.isEmpty(fuel.getTimeFinish())) {
            try {
                datet = format.parse(fuel.getTimeFinish());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 加油单ID
        fuelRecpt.setFlrcId(UUIDUitl.getUUID());
        //获取加油单编号
        String maxflrcNo = fuelRecptMapper.getMaxflrcNo();
        if (null != maxflrcNo && !"".equals(maxflrcNo)) {
            //把获取之后的加油单编号赋值到油单对象中
            fuelRecpt.setFlrcNo(maxflrcNo);
        } else {
            //把获取之后的加油单编号赋值到油单对象中
            fuelRecpt.setFlrcNo("0000000000001");
        }
        // 油单类型
        fuelRecpt.setFlrcType(fuel.getFlrcType());
        // 加油日期
        fuelRecpt.setFlrcDate(date1);
        // 机场名称
        fuelRecpt.setFlrcAirport(fuel.getAirport());
        // 飞机所属单位
        fuelRecpt.setFlrcAirlName(fuel.getDelivered());
        // 航班号
        fuelRecpt.setFlrcFlightNo(fuel.getFilghtNo());
        // 飞机号
        fuelRecpt.setFlrcAircrftNo(fuel.getAircraftNo());
        // 飞机类型
        fuelRecpt.setFlrcAircrftType(fuel.getAircraftType());
        // 起始
        fuelRecpt.setFlrcDeparture(fuel.getDeparture());
        // 经停
        fuelRecpt.setFlrcTransit(fuel.getTransitStop());
        // 终点
        fuelRecpt.setFlrcDest(fuel.getDestination());
        // 化验单编号
        fuelRecpt.setFlrcTestBillNo(fuel.getTestBillNo());
        // 油品名称
        fuelRecpt.setFlrcFuelName(fuel.getDescriptionAndGrade());
        // 温度
        fuelRecpt.setFlrcFuelTemp(BigDecimal.valueOf(fuel.getTemperature() != null ? fuel.getTemperature() : 0.0));
        // 密度
        fuelRecpt.setFlrcFuelDnst(BigDecimal.valueOf(fuel.getActualDensity() != null ? fuel.getActualDensity() : 0.0));
        // 计量表开始读数
        fuelRecpt.setFlrcMeterStat(BigDecimal.valueOf(fuel.getMeterStart().doubleValue()));
        // 计量表结束读数
        fuelRecpt.setFlrcMeterFnsh(BigDecimal.valueOf(fuel.getMeterFinish().doubleValue()));
        // 如果加油总量不为空
        if (fuel.getQuantity() != null) {
            fuelRecpt.setFlrcQuantity(BigDecimal.valueOf(fuel.getQuantity()));
        }
        // 加油数量小写
        fuelRecpt.setFlrcFiguars(BigDecimal.valueOf(fuel.getFigures().doubleValue()));
        // 加油数量大写
        fuelRecpt.setFlrcFiguarsWord(fuel.getFiguresWords());
        // 地井编号
        fuelRecpt.setFlrcHydrtPitNo(fuel.getHydrantPitNo());
        if (fuel.getVehicleTypeAndNo() != null) {
            // 加油车编号
            fuelRecpt.setFlrcVehiNo(fuel.getVehicleTypeAndNo());
        }
        // 加油开始时间
        fuelRecpt.setFlrcStatTime(date3);
        // 加油结束时间
        fuelRecpt.setFlrcFnshTime(datet);
        // 签名
        fuelRecpt.setFlrcSign(fuel.getSignPhoto());
        // 加油员姓名
        fuelRecpt.setFlrcDeliverName(fuel.getSignName());
        fuelRecpt.setFlrcDeliverId(fuel.getToken());
        // 油单上传
        if (fuelRecptMapper.upfuelIn(fuelRecpt) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
        // 根据加油单编号获取单条任务信息
        MyTask taskInfo = fuelRecptMapper.getTaskInfo(fuelRecpt.getFlrcNo());
        // 修改任务状态
        fuelRecptMapper.updateFuelstatus(taskInfo.getTaskId());
        Date date = new Date();
        String format2 = format.format(date);
        //更改对应的任务信息中的油单上传时间
        if (fuelRecptMapper.updateTask(fuelRecpt.getTaskId(), format2) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        // 创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
//		HttpHeaders headersb = new HttpHeaders();
//		MediaType type3 = MediaType.parseMediaType("application/json; charset=UTF-8");
//		headersb.setContentType(type3);
//		headersb.add("Accept", MediaType.APPLICATION_JSON.toString());
//		MyStaff tasks = new MyStaff();
//		tasks.setStaffId(taskInfo.getTaskOpeStaffId());
//		HttpEntity<String> formEntityv = new HttpEntity<String>(JsonHelper.object2str(tasks).getData(), headersb);
        //跨库调用方法获取参数
//		MyStaff staffinfo = restTemplate.postForObject(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffById", formEntityv, MyStaff.class);
        MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        //把查出来的人员ID赋值到人员车辆任务对象中
        staffTask.setSfvhStaffId(staffinfo.getStaffId());
        //把查出来的人员电话赋值到人员车辆任务对象中
        staffTask.setStaffPhone(staffinfo.getStaffPhone());
        staffTask.setStaffName(staffinfo.getStaffName());
        taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
        // 把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfo);
        // 根据任务ID查出单条任务航班信息
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfo.getTaskId());
        // 判断如果航班是本场的话再推送一条本场航班消息
        if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
            // 把要推送的航班任务对象放进Map集合中
            webMap.put("selfFlight", taskAndFlightById);
        }
        Integer type = null;
        type = flightService.pingSingleType(taskAndFlightById);
        taskAndFlightById.setFlrcType(type);
        taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        webMap.put("flight", taskAndFlightById);
        // 垮库查询获取调度员ID
        HttpHeaders headers = new HttpHeaders();
        MediaType types = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(types);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MyStaff> formEntity = new HttpEntity<MyStaff>(staffInfo, headers);
        // 跨库查询后使用人员List接受
        ResponseEntity<List<MyStaff>> rateResponse = restTemplate.exchange(
                Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffList",
                HttpMethod.POST, formEntity, new ParameterizedTypeReference<List<MyStaff>>() {
                });
        List<MyStaff> staffList = rateResponse.getBody();
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
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_TASK_STATE_UPDATE, webMap);
        RefuelingTask flightInfo = taskserviceimpl.getFlightInfo(taskInfo.getTaskId());
        flightInfo.setCompleteTime(date);
        taskserviceimpl.send(flightInfo);
    }

    /**
     * 油单检索
     */

    @Override
    public List<MyFuelRecpt> fuelretirevelist(MyFuelRecpt fuelRecpt) {
        return fuelRecptMapper.findfuelretirevelist(fuelRecpt);
    }

    /**
     * 保障任务查询
     */
    @Override
    public Object finsbzstaff(String staffId) {
        // 航班号
        String fno = fuelRecptMapper.findflightno(staffId);
        // 任务总数
        Integer staffcount = fuelRecptMapper.findstaffcount(staffId);
        // 任务完成数
        Integer stafffinsh = fuelRecptMapper.findstafffinsh(staffId);
        HashedMap<String, Object> map = new HashedMap<String, Object>();
        map.put("FlightNo", fno);
        map.put("StaffCount", staffcount);
        map.put("StaffComplete", stafffinsh);
        return map;
    }

    /**
     * 调度员强制修改任务状态为已接受
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void ForiceUpdateTaskStatus(MyTask task) {
        if (fuelRecptMapper.ForiceUpdateTaskStatus(task.getTaskId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 修改任务状态
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public Map<String, Object> upstatus(MyFlightTask task) {
        //根据任务ID查询任务单条信息
        MyTask taskInfoOne = taskMapper.getTaskInfo(task.getTaskId());
        if (fuelRecptMapper.upstatus(task.getTaskId(), task.getTaskStatus()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(taskInfoOne.getTaskAirportCode());
        staffInfo.setStaffAptareaCode(taskInfoOne.getTaskAptareaCode());
        //创建map集合用来装返回信息
        Map<String, Object> map = new HashMap<>();
        if (task.getTaskStatus() == 0) {
            map.put("true", "任务已经取消或未下发");
        } else if (task.getTaskStatus() == 1) {
            map.put("true", "任务待接受");
        } else if (task.getTaskStatus() == 2) {
            map.put("true", "任务申请待批");
        } else if (task.getTaskStatus() == 3) {
            map.put("true", "任务已接受");
        } else if (task.getTaskStatus() == 4) {
            map.put("true", "已到位");
        } else if (task.getTaskStatus() == 5) {
            map.put("true", "加油完成");
        } else if (task.getTaskStatus() == 6) {
            map.put("true", "油单待审核");
        } else if (task.getTaskStatus() == 7) {
            map.put("true", "任务完成");
        } else if (task.getTaskStatus() == 8) {
            map.put("true", "任务已决绝");
        } else if (task.getTaskStatus() == 9) {
            map.put("true", "不加油");
        }
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //根据任务ID查出单条任务航班信息
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(task.getTaskId());
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
        HttpHeaders headersb = new HttpHeaders();
//		MediaType type3 = MediaType.parseMediaType("application/json; charset=UTF-8");
//		headersb.setContentType(type3);
//		headersb.add("Accept", MediaType.APPLICATION_JSON.toString());
//		MyStaff tasks = new MyStaff();
//		tasks.setStaffId(taskInfo.getTaskOpeStaffId());
//		HttpEntity<String> formEntityv = new HttpEntity<String>(JsonHelper.object2str(tasks).getData(), headersb);
        //跨库调用方法获取参数
//		MyStaff staffinfo = restTemplate.postForObject(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffById", formEntityv, MyStaff.class);
        MyStaff staffinfo = staffMapper.getStaffById(taskInfo.getTaskOpeStaffId());
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        if (staffinfo != null) {
            //把查出来的人员ID赋值到人员车辆任务对象中
            staffTask.setSfvhStaffId(staffinfo.getStaffId());
            //把查出来的人员电话赋值到人员车辆任务对象中
            staffTask.setStaffPhone(staffinfo.getStaffPhone());
            staffTask.setStaffName(staffinfo.getStaffName());
            taskInfo.setTaskOpeStaffName(staffinfo.getStaffName());
            taskAndFlightById.setTaskOpeStaffName(staffinfo.getStaffName());
        }
        //把要推送的任务对象放进Map集合中
        webMap.put("task", taskInfo);
        taskAndFlightById.setFlrcType(type);
        webMap.put("flight", taskAndFlightById);
//		HttpEntity<String> formEntity = new HttpEntity<String>(JsonHelper.object2str(taskInfo).getData(), headersb);
        //根据加油员ID获取人员车辆表的信息
//		MyStaffVehi staffVehis = restTemplate.postForObject(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffVehiInfo", formEntity, MyStaffVehi.class);
        MyStaffVehi staffVehis = staffMapper.getStaffVehiInfo(taskInfo.getTaskOpeStaffId());
        //判断如果人员车辆信息是否为空
        MyVehi vehi = null;
        if (staffVehis != null) {
            //不为空的话把车辆编号赋值进人员车辆任务对象中
            staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
            vehi = vehiMapper.findVehiById(staffVehis.getSfvhVehiNo(), staffinfo.getStaffAirportCode());
        }
//		HttpEntity<String> formEntitys = new HttpEntity<String>(JsonHelper.object2str(staffTask).getData(), headersb);
        //根据加油车编号查出对应的车辆信息
//		MyVehi vehi = restTemplate.postForObject(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getVehiInfo", formEntitys, MyVehi.class);

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
        //把要推送的对象放进Map集合中
        webMap.put("staff", staffTask);
        //垮库查询获取调度员ID
        HttpHeaders headers = new HttpHeaders();
        MediaType types = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(types);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MyStaff> formEntityss = new HttpEntity<MyStaff>(staffInfo, headers);
        //跨库查询后使用人员List接受
        ResponseEntity<List<MyStaff>> rateResponse = restTemplate.exchange(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffList", HttpMethod.POST, formEntityss, new ParameterizedTypeReference<List<MyStaff>>() {
        });
        List<MyStaff> staffList = rateResponse.getBody();
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
        taskserviceimpl.sendToKafkaTask(taskInfo, taskAndFlightById);
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                Constant.PAD_TASK_STATE_UPDATE, webMap);
        return map;
    }

    /**
     * 导出油单数据
     */
    @Override
    public List<MyFuelRecpt> findlistfuel() {
        Date date = new Date();
        //查询整个油单数据
        List<MyFuelRecpt> myfuel = fuelRecptMapper.findlistfuel();
        //创建一个新的list
        ArrayList<MyFuelRecpt> arrayList = new ArrayList<MyFuelRecpt>();
        //循环遍历油单数据
        for (MyFuelRecpt myFuelRecpt : myfuel) {
            //创建一个新对象
            MyFuelRecpt FuelRecpt = new MyFuelRecpt();
            //每一次都查询油单号有多少
            Integer count = fuelRecptMapper.selectflrcno(myFuelRecpt.getFlrcNo());
            //如果油单编号不为空，油单必须为13位 油单编号不可重复，油单编号后九位为数字
            if (myFuelRecpt.getFlrcNo() != null && myFuelRecpt.getFlrcNo().length() == 12 && count == 1) {
                FuelRecpt.setFlrcNo(myFuelRecpt.getFlrcNo());
            }
            //加油日期不能大于当前时间
            if (myFuelRecpt.getFlrcDate().before(date)) {
                FuelRecpt.setFlrcDate(myFuelRecpt.getFlrcDate());
            }
            //根据飞机号码去查询购货方
            MyFlightCode flightcode = fuelRecptMapper.selectcustom(myFuelRecpt.getFlrcAircrftNo());
            FuelRecpt.setFlightUnit(flightcode.getArcrCustomNum());
            //根据飞机号码去查询航班表是否有飞机号码
            Integer count1 = fuelRecptMapper.selectflrcregn(myFuelRecpt.getFlrcAircrftNo());
            //如果数量大于 1
            if (count1 >= 1) {
                FuelRecpt.setFlrcAircrftNo(myFuelRecpt.getFlrcAircrftNo());
            }
            //根据航班号 去检索航班表 是否存在这个航班号
            Integer count2 = fuelRecptMapper.selectflrcflno(myFuelRecpt.getFlrcFlightNo());
            if (count2 >= 1) {
                FuelRecpt.setFlrcFlightNo(myFuelRecpt.getFlrcFlightNo());
            }
//			HttpHeaders headersb = new HttpHeaders();
//			MediaType type3 = MediaType.parseMediaType("application/json; charset=UTF-8");
//			headersb.setContentType(type3);
//			headersb.add("Accept", MediaType.APPLICATION_JSON.toString());
//			MyStaff myStaff = new MyStaff();
//			myStaff.setStaffName(myFuelRecpt.getFlrcDeliverName());
//			HttpEntity<String> formEnty = new HttpEntity<String>(JsonHelper.object2str(myStaff).getData(), headersb);
            //根据加油员ID获取人员车辆表的信息
//			Integer count3 = restTemplate.postForObject(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffvehi/selectstaffname", formEnty,Integer.class);
            int count3 = vehitaskMapper.selectstaffname(myFuelRecpt.getFlrcDeliverName());
            //根据加油员姓名去检索人员表是否存在这个加油员
            if (count3 > 1) {
                FuelRecpt.setFlrcDeliverName(myFuelRecpt.getFlrcDeliverName());
            }
            if (myFuelRecpt.getFlrcFnshTime() != null && myFuelRecpt.getFlrcStatTime() != null) {
                //加油结束时间大于加油开始时间，加油总时间不可以超过60分 加油总时间不能超过24小时
                if ((myFuelRecpt.getFlrcFnshTime().getTime() > myFuelRecpt.getFlrcStatTime().getTime()) && !(myFuelRecpt.getFlrcFnshTime().getTime() - myFuelRecpt.getFlrcStatTime().getTime() > 360000L) && !(myFuelRecpt.getFlrcFnshTime().getTime() - myFuelRecpt.getFlrcStatTime().getTime() > 8640000L)) {
                    FuelRecpt.setTimeAll(myFuelRecpt.getFlrcFnshTime().getTime() - myFuelRecpt.getFlrcStatTime().getTime());
                }
            }
            //加油总量乘以密度
            if (myFuelRecpt.getFlrcFuelDnst() != null && myFuelRecpt.getFlrcQuantity() != null) {
                double weight = (myFuelRecpt.getFlrcFuelDnst().multiply(myFuelRecpt.getFlrcQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (weight == myFuelRecpt.getFlrcFuelVol().doubleValue()) {
                    FuelRecpt.setFlrcFuelVol(myFuelRecpt.getFlrcFuelVol());
                }
            }

            //地井编号
            FuelRecpt.setFlrcHydrtPitNo(myFuelRecpt.getFlrcHydrtPitNo());
            //加油车编号
            FuelRecpt.setFlrcVehiNo(myFuelRecpt.getFlrcVehiNo());
            //类型
            FuelRecpt.setFlrcType(myFuelRecpt.getFlrcType());
            //飞机类型
            FuelRecpt.setFlrcAircrftType(myFuelRecpt.getFlrcAircrftType());
            //密度
            FuelRecpt.setFlrcFuelDnst(myFuelRecpt.getFlrcFuelDnst());
            //温度
            FuelRecpt.setFlrcFuelTemp(myFuelRecpt.getFlrcFuelTemp());
            //体积
            FuelRecpt.setFlrcFuelVol(myFuelRecpt.getFlrcFuelVol());
            //质量
            FuelRecpt.setFlrcQuantity(myFuelRecpt.getFlrcQuantity());
            //加油员
            FuelRecpt.setFlrcDeliverName(myFuelRecpt.getFlrcDeliverName());
            //加油结束时间
            FuelRecpt.setFlrcFnshTime(myFuelRecpt.getFlrcFnshTime());
            //加油开始时间
            FuelRecpt.setFlrcStatTime(myFuelRecpt.getFlrcStatTime());
            //化验单号
            FuelRecpt.setFlrcTestBillNo(myFuelRecpt.getFlrcTestBillNo());
            //收油人
            FuelRecpt.setFlrcSign(myFuelRecpt.getFlrcSign());
            arrayList.add(FuelRecpt);
        }
        return arrayList;
    }

    /**
     * 航班订阅
     */
    @Override
    public void readflight(MyFsubscription fsubscription, MyStaff staff) {
        fuelRecptMapper.readflight(staff.getLoginUserIn().getStaffId(), staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), fsubscription.getFlgtRflno(), fsubscription.getFlgtRflop());
    }

    /**
     * 取消订阅
     */
    @Override
    public void DissFlight(MyFsubscription fsubscription, MyStaff staff) {
        fuelRecptMapper.DissFlight(staff.getLoginUserIn().getStaffId(), staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), fsubscription.getFlgtRflno(), fsubscription.getFlgtRflop());
    }

    /**
     * 查询机场代码对应名称
     */
    @Override
    public ArrayList<Object> flightcode(MyStaff staff) {
        List<MyAirportCode> airlist = fuelRecptMapper.flightcode(staff.getLoginUserIn().getStaffAirportCode());
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (MyAirportCode myAirportCode : airlist) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(myAirportCode.getApcdIataCode(), myAirportCode.getApcdAirportName());
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /**
     * 航班预建表增加
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void insertFlightTemp(MyFlightTemp myFlightTemp) {
        if (fuelRecptMapper.insertFlightTemp(myFlightTemp) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
    }

    /**
     * 航班预建表修改
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateFlightTemp(MyFlightTemp myFlightTemp) {
        if (fuelRecptMapper.updateFlightTemp(myFlightTemp) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 航班预建表删除
     */
    @Override
    public void deleteFlightTemp(MyFlightTemp myFlightTemp) {
        if (fuelRecptMapper.deleteFlightTemp(myFlightTemp.getFlgtFlno()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 航班预建表查询
     */
    @Override
    public List<MyFlightTemp> selectFlightTemp() {
        return fuelRecptMapper.selectFlightTemp();
    }

    /**
     * 航班预建表详情
     */
    @Override
    public MyFlightTemp selectFlightTempFind(MyFlightTemp myFlightTemp) {
        return fuelRecptMapper.selectFlightTempFind(myFlightTemp.getFlgtFlno());
    }

    @Override
    public ArrayList<Map<String, Object>> flightcodeNew(MyStaff staff) {
        List<MyAirportCode> airlist = fuelRecptMapper.flightcode(staff.getLoginUserIn().getStaffAirportCode());
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        for (MyAirportCode myAirportCode : airlist) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("code", myAirportCode.getApcdIataCode());
            hashMap.put("name", myAirportCode.getApcdAirportName());
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateFuelPad(MyFuelRecpt fuelRecpt, MyStaff staff) {
        MyFuelRecpt fuelById = null;
        String flrcId = fuelRecpt.getFlrcId();
        if (StrUtil.isNotBlank(flrcId)) {
            fuelById = fuelRecptMapper.findFuelById(flrcId);
        }
        String flrcNo = fuelRecpt.getFlrcNo();
        if (fuelById == null && StrUtil.isNotBlank(flrcNo)) {
            fuelById = fuelRecptMapper.findFuelByNo(flrcNo);
        }
        if (fuelById == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("油单不存在，请先上传油单再修改", null));
        }
        Integer flrcType = compareType(fuelRecpt.getFlrcType());
        String flrcSign = fuelRecpt.getFlrcSign();
//        MyFuelRecpt fuelById = null;
        fuelRecpt.setFlrcType(flrcType);
        String taskId = fuelRecpt.getTaskId();
        System.out.println("修改油单任务id" + taskId);
        if (null != fuelRecpt.getFlrcDate()) {
            fuelRecpt.setFlrcWkopDate(fuelRecpt.getFlrcDate());
        }
        MyTask taskInfo = taskMapper.getTaskInfo(taskId);

        //校验加油量
        validFuelQuantity(fuelRecpt, staff);
        //校验油单号格式
        validFuelNoFormat(fuelRecpt, staff);
        //校验是否存一个任务出现多次油单上传，校验一个航班是否存在多次加油任务
        Boolean noCheckRepetition = fuelRecpt.getNoCheckRepetition();
        if (!noCheckRepetition) {
            checkRepetition(fuelRecpt, taskInfo, staff);
        }
        try {
            // 大离线版本不需要回收
//            tFuelNoService.recyclingFlueNoInsert(fuelRecpt, staff);
//            fuelById = fuelRecptMapper.findFuelById(fuelRecpt.getFlrcId());
            if (StringUtils.isEmpty(fuelRecpt.getFlrcVehiNum())) {
                fuelRecpt.setFlrcVehiNum(fuelRecpt.getFlrcVehiNum());
            }
            if (StringUtils.isEmpty(fuelRecpt.getFlrcVehiNo())) {
                fuelRecpt.setFlrcVehiNo(fuelRecpt.getFlrcVehiNo());
            }

            // 大离线版本不需要回收
//            log.debug("修改油单->findFuelById->单号" + fuelById.getFlrcNo());
//            if (StringUtils.isNotEmpty(fuelRecpt.getFlrcNo())) {
//                //新老油单不一致的时候
//                System.out.println("新老油单不一致的时候" + JSON.toJSONString(fuelRecpt));
//                if (!fuelRecpt.getFlrcNo().equals(fuelById.getFlrcNo())) {
//                    //回收老的
//                    System.out.println("旧油单号" + fuelById.getFlrcNo());
//                    Integer integer1 = tFuelNoService.updateFuleStatus(fuelById.getFlrcNo());
//                    Assert.notNull(integer1, "回收油单编号失败-----" + JSON.toJSONString(fuelRecpt));
//
//                    System.out.println("新油单号" + fuelRecpt.getFlrcNo());
//                    Integer integer = tFuelNoService.updatefuleNoEnd(fuelRecpt.getFlrcNo());
//                    Assert.notNull(integer, "修改油单编号失败------" + JSON.toJSONString(fuelRecpt));
//                }
//            } else {
//                throw new CustomException(ReturnMsg.getInstanceNGz("油单编号不可为空"));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> objectObjectHashMap = Maps.newHashMap();
        fuelRecpt.setFlrcAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        fuelRecpt.setFlrcAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        fuelRecpt.setFlrcDeliverName(staff.getLoginUserIn().getStaffName());
        fuelRecpt.setFlrcFuelVol(fuelRecpt.getFlrcFiguars());
        // 日期格式化
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format2 = format.format(date);
        //航班号裁切
        String flgtFlno = fuelRecpt.getFlrcFlightNo();
        Arrays.stream(flgtFlno.split("/")).filter(no -> StringUtils.contains(no, fuelRecpt.getFlrcAirlCode())).findAny().ifPresent(fuelRecpt::setFlrcFlightNo);
        if (null != taskInfo && StringUtils.isNotEmpty(taskInfo.getFlrcBwtar())) {
            fuelRecpt.setFlrcBwtar(taskInfo.getFlrcBwtar());
        }
        if (StringUtils.isEmpty(fuelRecpt.getFlrcBwtar()) || ObjectUtil.equal("O", fuelRecpt.getFlrcBwtar())) {
            fuelRecpt.setFlrcUpdateErrMsg("保税类型异常未上传");
        }
        validFuelRecpt(fuelRecpt, staff);
        // 油单更新
        fuelRecptMapper.update(fuelRecpt);
        MyFuelRecpt fuelById1 = fuelRecptMapper.findFuelByNo(fuelRecpt.getFlrcNo());
        BeanUtil.copyProperties(fuelById1, fuelRecpt);
        // 油单历史记录
        pcUpdateFuelLog(fuelRecpt, staff);
        objectObjectHashMap.put("fuel", fuelRecpt);
        //更改对应的任务信息中的油单上传时间
        if (fuelRecptMapper.updateTaskNo(taskId, format2, fuelRecpt.getFlrcNo()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }

        if (null != fuelById1 && (StringUtils.isEmpty(fuelRecpt.getFlrcVehiNum()) || StringUtils.isEmpty(fuelRecpt.getFlrcVehiNo()))) {
            //TODO 应该不是空
            fuelRecpt.setFlrcVehiNum(fuelById1.getFlrcVehiNum());
            fuelRecpt.setFlrcVehiNo(fuelById1.getFlrcVehiNo());
        }

        objectObjectHashMap.put("fuel", taskInfo);
        if (StringUtils.isNotEmpty(taskId)) {
            RefuelingTask flightInfo = taskserviceimpl.getFlightInfo(taskId);
            flightInfo.setCompleteTime(date);
            taskserviceimpl.send(flightInfo);
        }
        MyFlightTask taskAndFlightById = taskMapper.getTaskAndFlightById(taskId);
        if (null != taskAndFlightById) {
            fuelRecpt.setFlgtOlvr(taskAndFlightById.getFlgtOlvr());
        }
        //校验油单，油单上传状态不正常，则不上传油单
        //油单校验，异常，则不上传总部
        if (Integer.valueOf(1).equals(fuelRecpt.getFlrcStatus())) {
            return objectObjectHashMap;
        }
        // 注册事务同步回调
        MyFuelRecpt finalFuelById = fuelById;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 这里是在事务提交后执行的操作
                CompletableFuture.runAsync(() -> {
                    try {
                        //TODO 可能有bug 无法推送
                        // kafka给ERP推送
                        if (flrcSign == null) {
                            fuelRecpt.setFlrcSign(finalFuelById.getFlrcSign());
                        } else {
                            fuelRecpt.setFlrcSign(flrcSign);
                        }
                        send2kafkaByTopicZsdOilGeneration(fuelRecpt);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });
        return objectObjectHashMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyFuelRecpt UpFuelRecptPc(MyFuelRecpt fuelRecpt, MyStaff staff) {

        // 收油机场（去向机场）三码，收油机场，自提、配送的目的地机场三码
        if (fuelRecpt.getFlrcSupplyFuelType() == 1 || fuelRecpt.getFlrcSupplyFuelType() == 2) {
            fuelRecpt.setFlrcRapc3(fuelRecpt.getFlrcDest3c());
            fuelRecpt.setFlrcRapcn(fuelRecpt.getFlrcDest());
        }

        // 代结算机场三码（即销售结算的记账机场），每个机场固定配置，默认与供油机场相同；主要用于系统的销售记账
//        if(fuelRecpt.getSettlementRapcn() == null || fuelRecpt.getSettlementRapcn()== ""){
//            fuelRecpt.setSettlementRapcn(fuelRecpt.getFlrcDeparture());
//        }
        fuelRecpt.setStaffSchedulingId(staff.getLoginUserIn().getStaffId());

        String flrcAirlCode = (StrUtil.isNotBlank(fuelRecpt.getFlrcFlightNo()) && fuelRecpt.getFlrcFlightNo().length() >= 2) ? fuelRecpt.getFlrcFlightNo().substring(0, 2) : fuelRecpt.getFlrcFlightNo();
        fuelRecpt.setFlrcAirlCode(flrcAirlCode);
        // 油单ID
        //fuelRecpt.setFlrcFuelVol(fuelRecpt.getFlrcFiguars());
        fuelRecpt.setFlrcId(UUIDUitl.getUUID());
        fuelRecpt.setFlrcAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        fuelRecpt.setFlrcAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());

        fuelRecpt.setFlrcFuelVol(fuelRecpt.getFlrcFiguars());
        if (fuelRecpt.getFlrcFnshTime() == null) {
            fuelRecpt.setFlrcFnshTime(new Date());
        }
        if (fuelRecpt.getFlrcStatTime() == null) {
            Calendar nowTime1 = Calendar.getInstance();
            nowTime1.add(Calendar.MINUTE, -30);
            fuelRecpt.setFlrcStatTime(nowTime1.getTime());
        }
        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcVehiNum())) {
            fuelRecpt.setFlrcVehiNo(fuelRecpt.getFlrcVehiNum());
        }
        try {
            if (StringUtils.isNotEmpty(fuelRecpt.getFlrcSingle())) {
                String s = PDFUtils.imgToPdf(fuelRecpt.getFlrcSingle());
                fuelRecpt.setFlrcSingle(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //校验加油量
        validFuelQuantity(fuelRecpt, staff);
        //校验油单号格式
        validFuelNoFormat(fuelRecpt, staff);
        //校验加油客户信息及报税
        validFuelRecpt(fuelRecpt, staff);
        if (Integer.valueOf(1).equals(fuelRecpt.getFlrcStatus())) {
            return fuelRecpt;
        }
        // 油单插入
        if (fuelRecptMapper.upFuelrecpt(fuelRecpt) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
        // 回收油单号
        automaticRecover(fuelRecpt.getFlrcNo());
        // 油单历史记录
        pcUpdateFuelLog(fuelRecpt, staff);
        // 注册事务同步回调
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 这里是在事务提交后执行的操作
                CompletableFuture.runAsync(() -> {
                    try {
                        //TODO 可能有bug 无法推送
                        // kafka给ERP推送
                        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcSingle())) {
                            fuelRecpt.setFlrcSingle("1");
                        }
                        send2kafkaByTopicZsdOilGeneration(fuelRecpt);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });
        return fuelRecpt;
    }

    @Override
    @Transactional
    public ReturnMsg<TaskAndRecpt> handleTaskAndRecpt(List<TaskAndRecpt> list) {
        for (TaskAndRecpt taskAndRecpt : list) {
            this.judgeTaskAndRecpt(taskAndRecpt);
        }
        return ReturnMsg.getInstanceOKz(null);
    }

    /**
     * 油单离线上传：
     * 1、根据taskId查询任务，如果有，判断任务中油单编号是否和上传油单编号一致。如果没有，保存油单后赋值
     * 2、油单编号一致后根据油单编号查询油单，判断油单任务编号和任务编号是否一致
     * 3、保存油单、保存任务
     *
     * @param taskAndRecpt
     * @return 是否成功
     */
    private void judgeTaskAndRecpt(TaskAndRecpt taskAndRecpt) {
        MyTask task = taskAndRecpt.gettTask();
        MyFuelRecpt recpt = taskAndRecpt.gettFuelRecpt();
        //校验
        if (task == null) throw new CustomException(ReturnMsg.getInstanceNGz("缺少任务信息", taskAndRecpt));//缺少参数
        if (StringUtils.isEmpty(task.getTaskId()))
            throw new CustomException(ReturnMsg.getInstanceNGz("任务实体缺少任务ID", taskAndRecpt));
        if (StringUtils.isEmpty(task.getTaskFuelRecptNo()))
            throw new CustomException(ReturnMsg.getInstanceNGz("任务实体缺少油单编号", taskAndRecpt));
        if (recpt == null) throw new CustomException(ReturnMsg.getInstanceNGz("缺少油单信息", taskAndRecpt));//缺少参数
        if (StringUtils.isEmpty(recpt.getTaskId()))
            throw new CustomException(ReturnMsg.getInstanceNGz("油单实体缺少任务ID", taskAndRecpt));
        if (StringUtils.isEmpty(recpt.getFlrcNo()))
            throw new CustomException(ReturnMsg.getInstanceNGz("油单实体缺少油单编号", taskAndRecpt));
        //任务处理
        //查询任务
        MyTask taskInfo = taskMapper.getTaskInfo(task.getTaskId());
        if (taskInfo == null)
            throw new CustomException(ReturnMsg.getInstanceNGz("查询不到任务编号是：{" + task.getTaskId() + "} 的任务", taskAndRecpt));//查询不到任务
        if (!StringUtils.equals(task.getTaskId(), recpt.getTaskId()))
            throw new CustomException(ReturnMsg.getInstanceNGz("上传油单任务编号不相符", taskAndRecpt));
        //判断油单编号
        if (StringUtils.isEmpty(taskInfo.getTaskFuelRecptNo())) {//任务中油单编号为空
            //查询油单
            MyFuelRecpt fuel = fuelRecptMapper.findFuelByNo(recpt.getFlrcNo());
            if (fuel == null)
                throw new CustomException(ReturnMsg.getInstanceNGz("查询油单失败，油单不存在：{" + recpt.getFlrcNo() + "}", taskAndRecpt));
            //比较查询油单中任务ID和上传的任务中的任务ID
            if (!StringUtils.equals(fuel.getTaskId(), task.getTaskId()))
                throw new CustomException(ReturnMsg.getInstanceNGz("油单任务ID不相符", taskAndRecpt));
            //保存油单
            ModelAssistant.copyProperties(recpt, fuel);
            if (fuelRecptMapper.upFuelrecpt(fuel) <= 0)
                throw new CustomException(ReturnMsg.getInstanceNGz("新增油单失败", taskAndRecpt));
            task.setTaskFuelRecptNo(fuel.getFlrcNo());
        } else {//油单编号不为空
            if (!StringUtils.equals(taskInfo.getTaskFuelRecptNo(), recpt.getFlrcNo()))
                throw new CustomException(ReturnMsg.getInstanceNGz("上传油单编号和任务中油单编号不相符", taskAndRecpt));
            MyFuelRecpt fuel = fuelRecptMapper.findFuelByNo(taskInfo.getTaskFuelRecptNo());
            if (fuel == null)
                throw new CustomException(ReturnMsg.getInstanceNGz("油单不存在：" + taskInfo.getTaskFuelRecptNo(), taskAndRecpt));
//			if(!StringUtils.equals(fuel.getTaskId(),taskInfo.getTaskId()))throw new CustomException(ReturnMsg.getInstanceNGz("油单任务ID和上传任务ID不一致",taskAndRecpt));
            ModelAssistant.copyProperties(recpt, fuel);
            if (fuelRecptMapper.updatefuel(fuel) != 1)
                throw new CustomException(ReturnMsg.getInstanceNGz("更新油单失败", taskAndRecpt));
            task.setTaskFuelRecptNo(fuel.getFlrcNo());
        }
        ModelAssistant.copyProperties(task, taskInfo);
        System.out.println("lixian--" + JSON.toJSONString(taskInfo));
        if (taskMapper.updateTaskInfo(taskInfo) != 1)
            throw new CustomException(ReturnMsg.getInstanceNGz("更新任务失败", taskAndRecpt));
    }

    public List<MyFlightTask> getTodayFligtTasklist(String flgtAirportCode) {
        return fuelRecptMapper.getTodayFligtTasklist(flgtAirportCode);
    }

    public Integer findDoingTask(String staffId) {
        return fuelRecptMapper.findDoingTask(staffId);
    }

    @Override
    public void cancelFuel(String flrcId) {
        fuelRecptMapper.auditfuel(flrcId, 0);
    }

    @Override
    public void recoveryFuel(String flrcId, int flrcTakebackFlg) {
        fuelRecptMapper.recoveryFuel(flrcId, flrcTakebackFlg);
    }

    @Override
    //@Transactional
    public Map<String, Object> findNewFuelParam(TFuelNo tFuelNo, MyStaff staff) {
        System.out.println("--------------init1111-----" + Thread.currentThread().getId());
        String fuelNoLock = "fuelNoLock";
        RLock lock = null;
        if (StringUtils.isEmpty(tFuelNo.getRemark()))
            throw new CustomException(ReturnMsg.getInstanceNGz("申请单号备注不可为空"));
        try {
            //Thread.sleep(10000);
            HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
            objectObjectHashMap.put("fuleParam", fuelMapper.findOneByNow(staff.getLoginUserIn().getStaffAirportCode()));
            //分布式锁
            lock = distributedLocker.lock(fuelNoLock, 20L);
            System.out.println("--------------加锁-----" + Thread.currentThread().getId());
            boolean res = lock.tryLock(25, 20, TimeUnit.SECONDS);
            if (res) {
                try {
                    System.out.println("--------------持有锁-----" + Thread.currentThread().getId());
                    objectObjectHashMap.put("fuleNos", tFuelNoService.getFuelNoByTypeRemark(tFuelNo.getRemark(), staff));
                } finally {
                    System.out.println("--------------解锁-----" + Thread.currentThread().getId());
                    if (lock.isLocked()) { // 是否还是锁定状态
                        if (lock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
                            lock.unlock(); // 释放锁
                        }
                    }
                }
            } else {
                System.out.println("--------------锁没生效-----" + Thread.currentThread().getId());
                System.out.println("--------------锁没生效");
                return null;
            }
            log.debug("开始获取单号结果->" + JSON.toJSONString(objectObjectHashMap));
            return objectObjectHashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getFuelPad(TFuelDistribution tFuelDistribution) {
        TimeInterval timer = cn.hutool.core.date.DateUtil.timer();
        log.info("获取油单：staff{}，padid{}，获取时间：{}，线程ID{}", StrUtil.blankToDefault(tFuelDistribution.getStaffId(), ""), StrUtil.blankToDefault(tFuelDistribution.getPadId(), ""), cn.hutool.core.date.DateUtil.now(), Thread.currentThread().getId());
        System.out.println("--------------init1111-----" + Thread.currentThread().getId());
        String fuelNoLock = "fuelNoLockPad";
        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("fuleParam", fuelMapper.findOneByNow(tFuelDistribution.getFlgtAirportCode()));
        log.info("请求进入耗时：{}，线程ID{}", timer.intervalRestart(), Thread.currentThread().getId());
        boolean res = redissonDistributedLocker.tryLock(fuelNoLock);
        log.info("获取锁耗时：{}，线程ID{}", timer.intervalRestart(), Thread.currentThread().getId());
        if (!res) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.warn("Failed to acquire the lock.，线程ID{}", Thread.currentThread().getId());
            return null;
        }
        try {
            log.info("获取到锁耗时：{}，线程ID{}", timer.intervalRestart(), Thread.currentThread().getId());
            List<TFuelNo> tFuelNos = tFuelNoService.padGetFuelNoByType(tFuelDistribution);
            if (CollectionUtil.isEmpty(tFuelNos)) {
                log.info("获取油单为空，线程ID{}", Thread.currentThread().getId());
                return null;
            }
            log.info("获取到油单号耗时：{}，线程ID{}", timer.intervalRestart(), Thread.currentThread().getId());
            log.info("获取到油单号：{}，线程ID{}", JSONUtil.toJsonStr(JSONUtil.parseArray(tFuelNos)), Thread.currentThread().getId());
            objectObjectHashMap.put("fuleNos", tFuelNos);
            return objectObjectHashMap;
        } catch (Exception e) {
            // Handle any exceptions here
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Error occurred while processing fuel numbers.", e);
            return null;
        } finally {
            log.info("释放锁，线程ID{}", Thread.currentThread().getId());
            redissonDistributedLocker.unlock(fuelNoLock); // Release the lock
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer recycleFuelPad(List<TFuelNo> tFuelNo, String padId) {
        log.info("释放油单：padid{}，获取时间：{}，线程ID{}", StrUtil.blankToDefault(padId, ""), cn.hutool.core.date.DateUtil.now(), Thread.currentThread().getId());
        String fuelNoLock = "fuelNoLockPad";
        boolean res = redissonDistributedLocker.tryLock(fuelNoLock);
        if (!res) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.warn("Failed to acquire the lock.，线程ID{}", Thread.currentThread().getId());
            return null;
        }
        try {
            List<String> fuelNoList = tFuelNo.stream().filter(fil -> fil.getStatus() == 0).map(s -> s.getFlrcNo()).collect(Collectors.toList());
            System.out.println("padId----" + padId + "=-----过滤后-----" + JSON.toJSONString(fuelNoList));
            List<TFuelDistribution> dataList = tFuelDistributionMapper.selectByFuelNoList(fuelNoList);
            List<TFuelDistribution> updateDataList = dataList.stream().filter(dto -> StringUtils.equals(dto.getPadId(), padId))
                    .collect(Collectors.toList());
            if (updateDataList.isEmpty()) {
                return 0;
            }
            tFuelDistributionMapper.updateList(updateDataList);
            List<TFuelNo> updateFuelNoList = updateDataList.stream().map(s -> {
                TFuelNo fuelNo = new TFuelNo();
                fuelNo.setFlrcNo(s.getFlrcNo());
                return fuelNo;
            }).collect(Collectors.toList());
            tFuelNoMapper.updateStatus(updateFuelNoList, 0, 3);
            return updateFuelNoList.size();
        } catch (Exception e) {
            // Handle any exceptions here
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Error occurred while processing fuel numbers.", e);
            return null;
        } finally {
            log.info("释放锁，线程ID{}", Thread.currentThread().getId());
            redissonDistributedLocker.unlock(fuelNoLock); // Release the lock
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Integer recycleFuelPc(List<TFuelNo> tFuelNo) {
        List<TFuelDistribution> collect = tFuelNo.stream().map(dto -> {
            TFuelDistribution tFuelDistribution = new TFuelDistribution();
            tFuelDistribution.setFlrcNo(dto.getFlrcNo());
            return tFuelDistribution;
        }).collect(Collectors.toList());
        int i = tFuelDistributionMapper.updateList(collect);
        AtomicInteger i1 = new AtomicInteger();
        tFuelNo.stream().collect(Collectors.groupingBy(TFuelNo::getStatus, Collectors.toList()))
                .forEach((x, y) -> {
                    //


                    i1.addAndGet(tFuelNoMapper.updateStatus(y, x, 3));
                });

        return i1.get();
    }

    @Override
    public List<Map<String, Object>> selectByDateTaskAndRecpt(MyFuelRecpt myFuelRecpt, MyStaff staff) {
        Date startDate = myFuelRecpt.getStartDate();
        Date endDate = myFuelRecpt.getEndDate();
        if (ObjectUtil.isNull(startDate) && ObjectUtil.isNull(endDate)) {
            String nowStr = DateUtil.getCurrentDateStr();
            String date = StrUtil.blankToDefault(staff.getStaffServStation(), nowStr);
            try {
                myFuelRecpt.setFlrcDate(cn.hutool.core.date.DateUtil.parseDate(date));
            } catch (Exception e) {
                myFuelRecpt.setFlrcDate(cn.hutool.core.date.DateUtil.parseDate(nowStr));
                log.error("加油日期格式错误");
            }
        }
        List<MyFuelRecpt> myFuelRecpts = fuelRecptMapper.findFuelByStaffIdAndDate(staff.getLoginUserIn().getStaffId(), myFuelRecpt);
        // 客户信用逻辑
        List<TCreditInfo> tCreditInfos = tCreditInfoMapper.selectCreditInfo(new TCreditInfo());

        return myFuelRecpts.stream()
                .filter(mytask -> StringUtils.isNotEmpty(mytask.getFlrcNo()))
                .map(mytask -> {
                    mytask.setFlrcSigned(StrUtil.isNotBlank(mytask.getFlrcSign()) ? 1 : 0);
                    mytask.setFlrcSign(null);
                    Map<String, Object> result = Maps.newHashMap();
                    MyFlightTask myFlightTask = taskMapper.selectTaskByRecptNo(mytask.getFlrcNo());
                    if (myFlightTask != null) {
                        // 客户信用逻辑
                        if (StringUtils.isNotEmpty(mytask.getArcrCustomNum())) {
                            tCreditInfos.stream().filter(dto ->
                                            ObjectUtil.contains(mytask.getArcrCustomNum(), dto.getCstno()))
                                    .findFirst()
                                    .ifPresent(dto -> {
                                        myFlightTask.setCilvl(dto.getCilvl());
                                        myFlightTask.setCitst(dto.getCitst());
                                        myFlightTask.setCirmk(dto.getCirmk());
                                    });
                        }
                        if (!StringUtils.isEmpty(myFlightTask.getTaskId())) {
                            mytask.setTaskId(myFlightTask.getTaskId());
                        } else {
                            mytask.setTaskId("");
                        }
                        Integer ftyp = flightService.pingSingleType(myFlightTask);
                        myFlightTask.setFlrcType(ftyp);
                        result.put("MyFuel", mytask);
                        result.put("MyTaskFlight", myFlightTask);
                    }
                    return result;
                }).collect(Collectors.toList());
    }

    @Async
    void pcUpdateFuelLog(MyFuelRecpt fuellist, MyStaff staff) {
        MyFuelRecptOld myFuelRecptOld = new MyFuelRecptOld();
        ModelAssistant.copyProperties(fuellist, myFuelRecptOld);
        myFuelRecptOld.setFlrcOperatingType(1);
        myFuelRecptOld.setFlrcManualStaff(staff.getLoginUserIn().getStaffId());
        myFuelRecptOld.setFlrcManualTime(cn.hutool.core.date.DateUtil.formatDateTime(new Date()));
        myFuelRecptOldMapper.upFuelrecpt(myFuelRecptOld);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> pcUpdateFuel(MyFuelRecpt fuelRecpt, MyStaff staff) {
        System.out.println("修改油单任务id" + fuelRecpt.getTaskId());
        String fuelNoLock = "fuelNoLock";
        RLock lock = null;
        MyFuelRecpt fuelById = fuelRecptMapper.findFuelById(fuelRecpt.getFlrcId());
        if (fuelById == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("油单不存在"));
        }
        String flrcSign = fuelById.getFlrcSign();
        // 收油机场（去向机场）三码，收油机场，自提、配送的目的地机场三码
        if (fuelRecpt.getFlrcSupplyFuelType() == 1 || fuelRecpt.getFlrcSupplyFuelType() == 2) {
            fuelRecpt.setFlrcRapc3(fuelRecpt.getFlrcDest3c());
            fuelRecpt.setFlrcRapcn(fuelRecpt.getFlrcDest());
        }

        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcVehiNum())) {
            fuelRecpt.setFlrcVehiNo(fuelRecpt.getFlrcVehiNum());
        }
        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcSingleNew())) {
            try {
                String s = PDFUtils.imgToPdf(fuelRecpt.getFlrcSingleNew());
                fuelRecpt.setFlrcSingle(s);
            } catch (Exception e) {
                fuelRecpt.setFlrcSingle(fuelRecpt.getFlrcSingleNew());
                e.printStackTrace();
            }
        }

        Map<String, Object> objectObjectHashMap = Maps.newHashMap();
/*        fuelRecpt.setFlrcAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        fuelRecpt.setFlrcAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        fuelRecpt.setFlrcDeliverName(staff.getLoginUserIn().getStaffName());*/
        fuelRecpt.setFlrcFuelVol(fuelRecpt.getFlrcFiguars());
        // 日期格式化
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format2 = format.format(date);
        //航班号裁切
        String flgtFlno = fuelRecpt.getFlrcFlightNo();
        Arrays.stream(flgtFlno.split("/")).filter(no -> StringUtils.contains(no, fuelRecpt.getFlrcAirlCode())).findAny().ifPresent(fuelRecpt::setFlrcFlightNo);
        fuelRecpt.setFlrcAirlCode(fuelRecpt.getFlrcFlightNo().substring(0, 2));
        ModelAssistant.copyProperties(fuelRecpt, fuelById);
        //校验加油量
        validFuelQuantity(fuelById, staff);
        //校验油单号格式
        validFuelNoFormat(fuelById, staff);
        //校验加油客户信息及保税类型
        validFuelRecpt(fuelById, staff);
        //油单校验，异常，则不上传总部
        if (Integer.valueOf(1).equals(fuelById.getFlrcStatus())) {
            objectObjectHashMap.put("newfuel", fuelById);
            log.error(fuelById.getFlrcUpdateErrMsg());
            return objectObjectHashMap;
        }
        // 油单插入
        if (fuelRecptMapper.update(fuelById) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
        // 油单历史记录
        pcUpdateFuelLog(fuelRecpt, staff);
        objectObjectHashMap.put("fuel", fuelRecpt);
        // 注册事务同步回调
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 这里是在事务提交后执行的操作
                CompletableFuture.runAsync(() -> {
                    try {
                        //TODO 可能有bug 无法推送
                        // kafka给ERP推送
                        if (StringUtils.isNotEmpty(fuelById.getFlrcSingle())) {
                            fuelById.setFlrcSingle("1");
                        }
                        String flrcSignNew = fuelRecpt.getFlrcSign();
                        if (flrcSignNew == null) {
                            fuelById.setFlrcSign(flrcSign);
                        } else {
                            fuelById.setFlrcSign(flrcSignNew);
                        }
                        send2kafkaByTopicZsdOilGeneration(fuelById);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });
        return objectObjectHashMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyFuelRecpt resend(MyFuelRecpt fuelRecpt, MyStaff staff) {

        MyFuelRecpt fuelById = fuelRecptMapper.findFuelById(fuelRecpt.getFlrcId());
        if (fuelById == null) {
            return fuelById;
        }
        validFuelRecpt(fuelById, staff);
        if (Integer.valueOf(1).equals(fuelById.getFlrcStatus())) {
            fuelRecptMapper.update(fuelById);
            log.error(fuelRecpt.getFlrcUpdateErrMsg());
            return fuelById;
        }
        try {
            //TODO 可能有bug 无法推送
            // kafka给ERP推送
            if (StringUtils.isNotEmpty(fuelById.getFlrcSingle())) {
                fuelById.setFlrcSingle("1");
            }
            send2kafkaByTopicZsdOilGeneration(fuelById);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return fuelById;
    }

    //（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油 7:  内航国内补加油 8: 内航 离境 补加油 9 : 外航 补油
    // ）
    //1.抽油单类型：外航保税/外航非保税——油单号第5位为“4”；内航离境保税/内航离境非保税——油单号第5位为“5”；内航国内——油单号第5位为“6”；
    private Integer compareType(Integer type) {
        //3国内，2离境，1外航
        switch (type) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 3;
            case 8:
                return 2;
            case 9:
                return 1;
            default:
                return 3;
        }
    }

    @Override
    public String findRecptBase(String flrcId) {
        return fuelRecptMapper.findRecptBase(flrcId);
    }

    @Override
    public List<MyStaff> findStaffByName(MyStaff staff) {
        return staffMapper.findByName(staff.getStaffName());
    }

    @Override
    public MyFuelRecpt getFuelData(MyFuelVo fuelVo) {
        fuelVo.setAdid("D");
        MyFlight myFlight = flightMapper.getFlightInfoDetail(fuelVo.getFlrcAircrftNo(), fuelVo.getFlrcFlightNo(), fuelVo.getFlrcAirportCode(), fuelVo.getFlrcDate(), fuelVo.getAdid());
        MyFuelRecpt myFuelRecpt = new MyFuelRecpt();

        if (myFlight != null) {
            myFuelRecpt.setFlrcAircrftType(myFlight.getFlgtAcname());
            myFuelRecpt.setFlrcDeparture(myFlight.getFlgtOrgnm());
            myFuelRecpt.setFlrcDest(myFlight.getFlgtDesnm());
            myFuelRecpt.setFlrcTransit(myFlight.getFlgtTrsnm1());
        } else {
            myFlight = new MyFlight();
        }

        //加油客户编号
        Pair<String, String> pair = taskserviceimpl.pingCountries(fuelVo.getFlrcAircrftNo(), fuelVo.getFlrcFlightNo(), true);

        if (!"未知".equals(pair.getRight())) {
            if (StrUtil.isBlank(myFlight.getFlgtAcname()) && StringUtils.isNotEmpty((pair.getLeft()))) {
                myFlight.setFlgtAcname(pair.getLeft());//类型
            }
            String right = pair.getRight();
            if (StringUtils.isNotEmpty(right)) {
                String[] $s = right.split("&");
                if ($s.length == 2) {
                    myFlight.setFlgtAlcname($s[0]);//购买航空公司
                    myFlight.setArcrCustomNum($s[1]);
                }
            }
        }


//        if (myFlight!=null){
//            myFuelRecpt.setFlrcAircrftType(myFlight.getFlgtAcname());
//            myFuelRecpt.setFlrcDeparture(myFlight.getFlgtOrgnm());
//            myFuelRecpt.setFlrcDest(myFlight.getFlgtDesnm());
//            myFuelRecpt.setFlrcTransit(myFlight.getFlgtTrsnm1());
//            myFuelRecpt.setArcrCustomNum(myFlight.getArcrCustomNum());
//            myFuelRecpt.setFlrcAirlName(myFlight.getFlgtAlcname());
//            return myFuelRecpt;
//        }else {
//
//        }

        myFuelRecpt.setArcrCustomNum(myFlight.getArcrCustomNum());
        myFuelRecpt.setFlrcAirlName(myFlight.getFlgtAlcname());
        return myFuelRecpt;
    }

    @Override
    public MyFuelRecpt deleteFuel(MyFuelRecpt fuelRecpt, MyStaff staff) {
        Objects.requireNonNull(fuelRecpt.getFlrcId(), "ID 不能为空");
        MyFuelRecpt fuellist = fuelRecptMapper.findFuellist(fuelRecpt);
        if (null != fuellist) {
            MyFuelRecptOld myFuelRecptOld = new MyFuelRecptOld();
            ModelAssistant.copyProperties(fuellist, myFuelRecptOld);
            myFuelRecptOld.setFlrcOperatingType(2);
            myFuelRecptOld.setFlrcManualStaff(staff.getLoginUserIn().getStaffId());
            int i = myFuelRecptOldMapper.upFuelrecpt(myFuelRecptOld);
            if (i == 1) {
                int i1 = fuelRecptMapper.deleteFuelById(fuelRecpt.getFlrcId());
                // System.out.println("---------------执行表面删除逻辑");
                if (1 != i1) {
                    return null;
                } else {
                    return fuelRecpt;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void test() {
        MyFuelRecpt fuelByNo = fuelRecptMapper.findFuelByNo("2210300773005-D");
        send2kafkaByTopicZsdOilGeneration(fuelByNo);
        MyFuelRecpt fuelByNo1 = fuelRecptMapper.findFuelByNo("2210300772816-D");
        send2kafkaByTopicZsdOilGeneration(fuelByNo1);
    }

    @Override
    public void printFuel(MyFuelRecpt myFuelRecpt, String staffId) {
        MyStaff staffById = staffMapper.getStaffById(staffId);
        if (staffById == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz(Constant.CODE_ERR, "用户不存在!", null));
        }
        String printServerUrl = staffById.getPrintServerUrl();
        if (StrUtil.isBlank(printServerUrl)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(Constant.CODE_ERR, "该用户尚未配置油单打印服务地址，请配置后充实!", null));
        }
        myFuelRecpt.setFlrcTypeNameInfo();
        myFuelRecpt.setFlrcId(myFuelRecpt.getFlrcNo());
        myFuelRecpt.setFlrcSign("");
        myFuelRecpt.setFlrcSingle("");
        //myFuelRecpt.setFlrcMeterStat(new BigDecimal("10"));
        // 创建JSONConfig并设置时间格式
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

        //TODO 通过staff
        ////链式构建请求
        //String result2 = null;
        //try {
        //    result2 = HttpRequest.post("http://192.168.1.101:8686/commitOilSheet.action")
        //            .header(Header.CONTENT_TYPE, "application/json;charset=GBK")//头信息，多个头信息多次调用此方法即可
        //            .body(new String(JSON.toJSONString(myFuelRecpt,serializeConfig).getBytes(StandardCharsets.UTF_8), "GBK"))//json
        //            .execute().body();
        //} catch (UnsupportedEncodingException e) {
        //    throw new RuntimeException(e);
        //}
        //Console.log(result2);
        String jsonString = JSON.toJSONString(myFuelRecpt, serializeConfig);
        Console.log(jsonString);
        try {
            // 创建URL对象
            URL url = new URL(printServerUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=GBK"); // 设置内容类型为JSON，并指定字符集为GBK
            con.setDoOutput(true);

            // 将字符串转换为GBK编码的字节数组
            byte[] inputBytes = jsonString.getBytes(Charset.forName("GBK"));

            // 发送POST输出
            try (DataOutputStream os = new DataOutputStream(con.getOutputStream())) {
                os.write(inputBytes, 0, inputBytes.length);
            }

            // 读取响应
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(new String());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：校验油单信息
     *
     * @param fuelRecpt 油单信息
     * @return void
     * @author zhaojiacan
     * @date 2024/4/15
     */
    private void validFuelRecpt(MyFuelRecpt fuelRecpt, MyStaff staff) {
        if (StringUtils.isEmpty(fuelRecpt.getArcrCustomNum())) {
            fuelRecpt.setFlrcStatus(1);
            fuelRecpt.setFlrcUpdateErrMsg(String.format("未找到加油客户编号, 飞机号【%s】，航班号【%s】，请去基础信息管理维护后重新上传", fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo()));
            return;
        }
        String trimArcrCustomNum = StrUtil.blankToDefault(fuelRecpt.getArcrCustomNum(), StrUtil.EMPTY).replaceFirst("^0+", "");
        //沈阳通航业务不做校验
        Integer flrcBusType = fuelRecpt.getFlrcBusType() == null ? Integer.valueOf(2) : fuelRecpt.getFlrcBusType();
        if (!Integer.valueOf(1).equals(flrcBusType)) {
            String apcdCnafAirportCode = staff == null ? StrUtil.EMPTY : Optional.ofNullable(staff.getLoginUserIn()).map(LoginUser::getStaffAirportCode).orElse(StrUtil.EMPTY);
            TOrderInfo flightOrder = null;
            //if (StrUtil.isNotBlank(apcdCnafAirportCode)) {
            //    TAirportCode airportCode = myTAirportCodeMapper.selectByCnafAirportCode(apcdCnafAirportCode);
            //    TOrderInfo tOrderInfo = new TOrderInfo();
            //    tOrderInfo.setApc3(airportCode.getApcdIataCode());
            //    tOrderInfo.setFlno(fuelRecpt.getFlrcFlightNo());
            //    tOrderInfo.setRegn(fuelRecpt.getFlrcAircrftNo());
            //    List<TOrderInfo> tOrderInfos = tOrderInfoMapper.selectOrderInfoByRegnAndNo(tOrderInfo);
            //    if (CollUtil.isNotEmpty(tOrderInfos)) {
            //        flightOrder = tOrderInfos.get(0);
            //        String cstno = StrUtil.blankToDefault(flightOrder.getCstno(),StrUtil.EMPTY);
            //        String trimCstno= cstno.replaceFirst("^0+", "");
            //        if (!trimArcrCustomNum.equals(trimCstno)) {
            //            fuelRecpt.setFlrcStatus(1);
            //            fuelRecpt.setFlrcUpdateErrMsg(String.format("上传油单时的加油客户编号%s与本系统维护的航班客户编号%s不一致，飞机号【%s】，航班号【%s】，请确认后去基础信息管理维护后重新上传",
            //                    fuelRecpt.getArcrCustomNum(), cstno, fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo()));
            //            return;
            //        }else{
            //            fuelRecpt.setFlrcStatus(0);
            //            fuelRecpt.setFlrcUpdateErrMsg(StrUtil.EMPTY);
            //        }
            //    }
            //}
            if (flightOrder == null) {
                //判断系统飞机号对应的加油客户编号是否和上传油单时的加油客户编号一致
                Pair<String, String> pair = taskserviceimpl.pingCountries(fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo(), true);
                String regnInfo[] = StrUtil.blankToDefault(pair.getRight(), StrUtil.EMPTY).split("&");
                if (ArrayUtil.isEmpty(regnInfo)) {
                    fuelRecpt.setFlrcStatus(1);
                    fuelRecpt.setFlrcUpdateErrMsg(String.format("加油客户编号【%s】，飞机号【%s】，航班号【%s】，在本系统中未找到该飞机信息，请确认后去基础信息管理维护后重新上传",
                            fuelRecpt.getArcrCustomNum(), fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo()));
                    return;
                } else if (regnInfo.length <= 1) {
                    fuelRecpt.setFlrcStatus(1);
                    fuelRecpt.setFlrcUpdateErrMsg(String.format("加油客户编号【%s】，飞机号【%s】，航班号【%s】，在本系统中未找到该飞机信息，请确认后去基础信息管理维护后重新上传",
                            fuelRecpt.getArcrCustomNum(), fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo()));
                    return;
                } else if (!StrUtil.blankToDefault(regnInfo[1], StrUtil.EMPTY).replaceFirst("^0+", "").equals(trimArcrCustomNum)) {
                    fuelRecpt.setFlrcStatus(1);
                    fuelRecpt.setFlrcUpdateErrMsg(String.format("加油客户编号【%s】与本系统维护的航班客户编号【%s】不一致，飞机号【%s】，航班号【%s】，请确认后去基础信息管理维护后重新上传",
                            fuelRecpt.getArcrCustomNum(), regnInfo[1], fuelRecpt.getFlrcAircrftNo(), fuelRecpt.getFlrcFlightNo()));
                    return;
                } else {
                    fuelRecpt.setFlrcStatus(0);
                    fuelRecpt.setFlrcUpdateErrMsg(StrUtil.EMPTY);
                }
            }
        }
        if (StringUtils.isEmpty(fuelRecpt.getFlrcBwtar()) || fuelRecpt.getFlrcBwtar().equals("O")) {
            fuelRecpt.setFlrcStatus(1);
            fuelRecpt.setFlrcUpdateErrMsg(String.format("上传油单的保税类型为空，或者类型错误，请确认后去基础信息管理维护后重新上传"));
        } else {
            fuelRecpt.setFlrcStatus(0);
            fuelRecpt.setFlrcUpdateErrMsg(StrUtil.EMPTY);
        }
    }
}
