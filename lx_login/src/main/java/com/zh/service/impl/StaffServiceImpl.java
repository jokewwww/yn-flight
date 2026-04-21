package com.zh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zh.bean.OutMessage;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTask;
import com.zh.bean.login.*;
import com.zh.component.RedisComponent;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.StaffService;
import com.zh.service.TParamService;
import com.zh.service.TokenService;
import com.zh.util.DateUtil;
import com.zh.util.JsonHelper;
import com.zh.util.SendMsg2Redis;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class StaffServiceImpl implements StaffService {

    private final static Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);
    @Autowired
    Prop prop;
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MyStaffVehiTaskMapper vehitaskMapper;
    @Autowired
    private MySettingMapper mysettingmapper;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private StaffMapper StaffMapper;
    @Autowired
    private VehiTypeMapper vehiTypeMapper;
    @Autowired
    private RedisComponent redis;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TParamService tParamService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private MyFuelRecptMapper fuelRecptMapper;
    @Autowired
    private VehiMapper vehiMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TParamMapper tParamMapper;
    @Autowired
    private StaffMapper staffMapper;

    public void sends(String taskId, String staffId, String staffName, String vehiNo, String vehiPlateNo, String status, String airport, Long timeStamp) {
        kafkaTemplate.send("carstatus", "{\"vehiPlateNo\":\"" + vehiPlateNo + "\",\"staffName\":\"" + staffName + "\",\"staffId\":\"" + staffId + "\",\"taskId\":\"" + taskId + "\",\"vehiNo\":\"" + vehiNo + "\",\"status\":\"" + status + "\",\"airport\":\"" + airport + "\",\"timeStamp\":\"" + timeStamp + "\"}");
        log.debug("这是往kafka中推送消息，消息主题是carstatus，消息内容是：" + "{\"vehiPlateNo\":\"" + vehiPlateNo + "\",\"staffName\":\"" + staffName + "\",\"staffId\":\"" + staffId + "\",\"taskId\":\"" + taskId + "\",\"vehiNo\":\"" + vehiNo + "\",\"status\":\"" + status + "\",\"airport\":\"" + airport + "\",\"timeStamp\":\"" + timeStamp + "\"}");
    }

    /**
     * 通过人员id查询人员信息（供地图使用）
     */
    @Override
    public TStaff getStaffById(String staffId) {
        return StaffMapper.getStaffById(staffId);
    }

    /**
     * 平板端人员登录
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Map<String, Object> staffLogin(String staffId, String staffPwd) {
        //先查询此人是否已经绑定车辆
        MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staffId);
        //如果不等于空说明已经绑定车辆
        if (staffVehiInfo != null) {
            //删除人车表中信息
            if (StaffMapper.deleteStaffVehi(staffVehiInfo.getSfvhStaffId()) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        //通过人员ID去查询出该人员的信息
        MyStaff staff = StaffMapper.getStaffInfo(staffId);
        //创建一个Map集合来存放返回的信息
        Map<String, Object> infoMap = new HashMap<String, Object>();
        //同时判断账号和密码，如果对象等于NULL说明账号不正确，如果不等于NULL说明账号正确
        //如果查出来的密码和用户输入的一样的话说明密码正确，一样的话说明密码错误
        if (staff != null && staff.getStaffPwd().equals(staffPwd)) {
            infoMap.put("date", new Date().getTime());
            //开始判断岗位,判断岗位是否是加油员
            if (Constant.CHEER_STAFF.equals(staff.getStaffType()) || "4".equals(staff.getStaffType()) || "5".equals(staff.getStaffType()) || Constant.MANAGER_STAFF.equals(staff.getStaffType())) {
                String companyName = flightMapper.getAirportNames(staff.getStaffAirportCode());
                //判断查出的人员信息是否为空
                if (StringUtils.isNotEmpty(companyName)) {
                    //不为空的话就把查出来的机场名赋值到人员信息中
                    staff.setStaffAirportName(companyName);
                }
                //删除之前登录的。
                Object object = redis.get(Constant.LOGIN_KEY + staff.getStaffId());
                if (null != object) {
                    logoutWS(staff);
                    redis.remove(Constant.TOKENS + object.toString());
                }
                logout(staff, Constant.LOGIN_KEY);
                infoMap.put("staffInfo", staff);
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(staff, loginUser);
                infoMap.put("token", tokenService.saveToken(loginUser).getToken());

                //查询登录人param表，类型是2的，默认查机场代码是1的，循环根据name,查询登录人所属机场的对应信息进行替换
                List<TParam> tparams = tParamMapper.selectDefaultData();
                List<TParam> mapTParams = new ArrayList<TParam>();
                for (TParam tparam : tparams) {
                    TParam tempTParam = tParamMapper.selectByAirportCode(tparam.getpName(), staff.getStaffAirportCode(), 2);
                    if (tempTParam != null) {
                        mapTParams.add(tempTParam);
                    } else {
                        mapTParams.add(tparam);
                    }
                }
                infoMap.put("paramList", mapTParams);

                //登录信息无误，说明登录成功，把员工id放进redis中，key为 login:员工id
                redis.set(Constant.LOGIN_KEY + staff.getStaffId(), loginUser.getToken());
                stringRedisTemplate.expire(Constant.LOGIN_KEY + staff.getStaffId(), expireSeconds, TimeUnit.SECONDS);
                redis.set(Constant.LOGIN_KEY + staff.getStaffId() + ":" + staff.getStaffAirportCode() + ":" + staff.getStaffAptareaCode() + ":" + Constant.PAD_BROKEN_NETWORK, "");
                //创建Map集合用来推送消息
                Map<String, Object> map = new HashMap<String, Object>();
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffId);
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staff.getStaffPhone());
                staffTask.setStaffName(staff.getStaffName());
                MyTask tasks = fuelRecptMapper.gettaskById(staffId);
                String AptareaCode = Constant.judgeAptareaCode(staff.getStaffAptareaCode());
                if (null != redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                    staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
                } else {
                    staffTask.setStaffDateIng(null);
                }

                if (null != redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                    staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                } else {
                    staffTask.setStaffDateFree(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + staffId + ":" + staff.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                } else {
                    staffTask.setStaffingTaskCount(0);
                }
                //放入人员 对应
                if (null != redis.get("staffAndTask:" + staffId)) {
                    String data = String.valueOf(redis.get("staffAndTask:" + staffId));
                    String[] split = data.split(",");
                    if (split.length > 0) {
                        staffTask.setTaskId(split[0]);
                    } else {
                        staffTask.setTaskId(null);
                    }
                } else {
                    staffTask.setTaskId(null);
                }
                //判断任务信息如果不为空
                if (tasks != null) {
                    //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                    staffTask.setSfvhStaffStatus(2);
                    //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                    staffTask.setFlgtFlno(tasks.getTaskFlightNo());
                } else {
                    //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                    if (null != redis.get(Constant.LOGIN_KEY + staffId)) {
                        //如果取到说明人员登录
                        staffTask.setSfvhStaffStatus(1);
                    } else {
                        //如果取不到说明人员未登录
                        staffTask.setSfvhStaffStatus(0);
                    }
                }
                //根据加油员ID获取人员车辆表的信息
                MyStaffVehi staffVehi = StaffMapper.getStaffVehiInfo(staffId);
                //判断如果人员车辆信息是否为空
                if (staffVehi != null) {
                    //不为空的话把车辆编号赋值进人员车辆任务对象中
                    staffTask.setVehiNo(staffVehi.getSfvhVehiNo());
                }
                //根据加油车编号查出对应的车辆信息
                MyVehi vehi = vehitaskMapper.getVehiInfo(staffTask.getVehiNo(), staff.getStaffAirportCode());
                //判断查询出来的车辆信息是否为空
                if (vehi != null) {
                    //如果不为空把车辆ID赋值到人员车辆任务对象中
                    staffTask.setVehiId(vehi.getVehiId());
                    //如果不为空把车辆别名赋值到人员车辆任务对象中
                    staffTask.setVehiNickname(vehi.getVehiNickname());
                    //如果不为空把车辆号赋值到人员车辆任务对象中
                    staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                }
                List<MyTask> taskList = getTaskEndInfo(staff);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfo : taskList) {
                    if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffId)) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }
                //把要推送的对象放进Map集合中
                map.put("staff", staffTask);
                //获取调度员ID
                List<MyStaff> staffList = staffMapper.getStaffLists(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
                String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_LOGIN, map);
                // 人员登录  推送给 PC   ( 胡 )
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
            } else {
                //判断如果不是加油员则往map集合中存放 value为您不是加油员  key为error
                infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NO_CHEER_STAFF_VALUE);
            }
        } else {
            //判断如果账号或密码不对则往map集合中存放  value为账号或密码错误  key为error
            if (staff != null) {
                infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NAME_PWD_ERROR_VALUE);
            } else {
                infoMap.put(Constant.LOGIN_ERROR_KEY, "账号不存在！");
            }

        }
        //把map集合返回到controller
        return infoMap;
    }

    /**
     * 平板端人员登出(注销)
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void removeStaff(MyStaff staff) {
        ////通过人员ID去查询出该人员的信息
        //MyStaff staffInfo = StaffMapper.getStaffInfo(staff.getLoginUserIn().getStaffId());
        //先查询此人是否已经绑定车辆
        MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staff.getLoginUserIn().getStaffId());
        //如果不等于空说明已经绑定车辆
        if (staffVehiInfo != null) {
            //删除人车表中信息
            if (StaffMapper.deleteStaffVehi(staffVehiInfo.getSfvhStaffId()) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        //在redis删除前台传过来的人员id
        logout(staff, Constant.LOGIN_KEY);
        if (staff.getLoginUserIn() != null && staff.getLoginUserIn().getToken() != null) {
            stringRedisTemplate.delete(Constant.TOKENS + staff.getLoginUserIn().getToken());
        }
        //创建Map集合用来推送消息
        Map<String, Object> map = new HashMap<String, Object>();
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        //把人员ID赋值到人员车辆任务对象中
        staffTask.setSfvhStaffId(staff.getLoginUserIn().getStaffId());
        //把查出来的人员电话赋值到人员车辆任务对象中
        staffTask.setStaffPhone(staff.getLoginUserIn().getStaffPhone());
        staffTask.setStaffName(staff.getLoginUserIn().getStaffName());
        //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
        staffTask.setStaffDateIng(null);
        staffTask.setStaffDateFree(null);
        staffTask.setStaffingTaskCount(null);
        staffTask.setTaskId(null);
        List<MyTask> taskList = getTaskEndInfo(staff);
        staffTask.setStaffEndTaskCount(null);
        /*for (MyTask taskInfo : taskList) {
            if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
            }
        }*/
        //如果取不到说明人员未登录
        staffTask.setSfvhStaffStatus(0);
        //把要推送的对象放进Map集合中
        map.put("staff", staffTask);
        //获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));

        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_REMOVE, map);
        //人员登出 发送给PC (胡)
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
    }

    /**
     * 清空加油员的连续工作时间和连续任务数,开始休息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void cleanStaff(MyStaff staff) {
        if (Objects.isNull(staff.getStaffId())) {
            staff.setStaffId(staff.getLoginUserIn().getStaffId());
        }
        //通过人员ID去查询出该人员的信息
        MyStaff staffInfo = StaffMapper.getStaffInfo(staff.getStaffId());
        String AptareaCode = Constant.judgeAptareaCode(staffInfo.getStaffAptareaCode());
        Date date = new Date();
        //连续工作时间
        redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng", "");
        //连续休息时间
        redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime", DateUtil.getCurrentDateTimeStr());
        //连续工作数量
        redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", 0);
        //创建Map集合用来推送消息
        Map<String, Object> map = new HashMap<String, Object>();
        //创建一个人员车辆任务对象用来装要推送的人员信息
        MyStaffVehiTask staffTask = new MyStaffVehiTask();
        //把人员ID赋值到人员车辆任务对象中
        staffTask.setSfvhStaffId(staffInfo.getStaffId());
        //把查出来的人员电话赋值到人员车辆任务对象中
        staffTask.setStaffPhone(staffInfo.getStaffPhone());
        staffTask.setStaffName(staffInfo.getStaffName());
        MyTask tasks = fuelRecptMapper.gettaskById(staffInfo.getStaffId());
        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
            redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng", "");
            staffTask.setStaffDateIng(null);
        }
        //放入人员 对应
        if (null != redis.get("staffAndTask:" + staffInfo.getStaffId())) {
            redis.set("staffAndTask:" + staffInfo.getStaffId(), "");
            staffTask.setTaskId(null);
        }
        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
            staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
        } else {
            staffTask.setStaffDateFree(DateUtil.getCurrentDateTimeStr());
        }
        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
            redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount", 0);
            staffTask.setStaffingTaskCount(0);
        }
        //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId())) {
            //如果取到说明人员登录
            staffTask.setSfvhStaffStatus(1);
            //判断任务信息如果不为空
            if (tasks != null) {
                //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                staffTask.setSfvhStaffStatus(2);
                //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                staffTask.setFlgtFlno(tasks.getTaskFlightNo());
            }
        } else {
            //如果取不到说明人员未登录
            staffTask.setSfvhStaffStatus(0);
        }
        //根据加油员ID获取人员车辆表的信息
        MyStaffVehi staffVehi = StaffMapper.getStaffVehiInfo(staffInfo.getStaffId());
        //判断如果人员车辆信息是否为空
        if (staffVehi != null) {
            //不为空的话把车辆编号赋值进人员车辆任务对象中
            staffTask.setVehiNo(staffVehi.getSfvhVehiNo());
        }
        //根据加油车编号查出对应的车辆信息
        MyVehi vehi = vehitaskMapper.getVehiInfo(staffTask.getVehiNo(), staff.getStaffAirportCode());
        //判断查询出来的车辆信息是否为空
        if (vehi != null) {
            //如果不为空把车辆ID赋值到人员车辆任务对象中
            staffTask.setVehiId(vehi.getVehiId());
            //如果不为空把车辆别名赋值到人员车辆任务对象中
            staffTask.setVehiNickname(vehi.getVehiNickname());
            //如果不为空把车辆号赋值到人员车辆任务对象中
            staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
        }
        List<MyTask> taskList = getTaskEndInfo(staffInfo);
        staffTask.setStaffEndTaskCount(0);
        for (MyTask taskInfo : taskList) {
            if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
            }
        }
        //把要推送的对象放进Map集合中
        map.put("staff", staffTask);
        //获取调度员ID
        List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_LOGIN, map);
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
    }

    /**
     * 通知前端下线
     *
     * @param staff
     */
    private void logoutWS(MyStaff staff) {
        OutMessage<String> outMessage = new OutMessage<String>(null, Constant.STAFF, Constant.PAD_STAFF_OFFLINE, null);
        if (staff.getLoginUserIn() != null) {
            outMessage.setTo(staff.getLoginUserIn().getStaffId());
        } else {
            outMessage.setTo(staff.getStaffId());
        }
        stringRedisTemplate.convertAndSend("chatLogout", JsonHelper.object2str(outMessage).getData());
    }

    /**
     * 在redis删除前台传过来的人员id
     *
     * @param staff
     */
    private void logout(MyStaff staff, String key) {
        String userId;
        if (staff.getLoginUserIn() != null) {
            userId = staff.getLoginUserIn().getStaffId();
        } else {
            userId = staff.getStaffId();
        }
        userId = key + userId;

        stringRedisTemplate.delete(userId);

        Set<String> keys = stringRedisTemplate.keys(userId + ":*");
        if (keys != null && keys.size() > 0) {
            try {
                for (String sKey : keys) {
                    if (sKey.replace(":", "").length() + 4 == sKey.length()
                            && (sKey.endsWith(":0") || sKey.endsWith(":1") || sKey.endsWith(":103") || sKey.endsWith(":104"))) {
                        stringRedisTemplate.delete(sKey);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取所有调度员ID（供远程调用使用）
     */
    @Override
    public List<MyStaff> getStaffList(String staffAirportCode, String staffAptareaCode) {
        return StaffMapper.getStaffList(staffAirportCode, staffAptareaCode);
    }

    /**
     * 获取所有调度员ID（供远程调用使用）
     */
    @Override
    public List<MyStaff> getAllStaffList(String staffAirportCode, String staffAptareaCode) {
        return StaffMapper.getAllStaffList(staffAirportCode, staffAptareaCode, null);
    }


    /**
     * 人员车辆绑定
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Integer staffAndVehi(MyStaffVehi staffVehi) {

        //通过人员ID去查询出该人员的信息
        MyStaff staffInfo = StaffMapper.getStaffInfo(staffVehi.getSfvhStaffId());
        if (ObjectUtil.isNull(staffInfo)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("绑定车辆的人员不存在", null));
        }

        // 拒绝绑定不准假车型
        VehiTypeStaff noVehiNoByStaffIdSfvhVehiNo = vehiTypeMapper.getNoVehiNoByStaffIdSfvhVehiNo(staffVehi.getSfvhStaffId(), staffVehi.getSfvhVehiNo(), staffInfo.getStaffAirportCode());

        if (ObjectUtil.isNotNull(noVehiNoByStaffIdSfvhVehiNo)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("拒绝绑定不准驾车型", null));
        }

        // 拒绝绑定不可用车辆
        MyVehi vehiInfos = vehitaskMapper.getVehiInfosByNo(staffVehi.getSfvhVehiNo(), staffInfo.getStaffAirportCode());
        if (ObjectUtil.isNotNull(vehiInfos) && vehiInfos.getVehiAvailability() == 0) {
            throw new CustomException(ReturnMsg.getInstanceNGz("拒绝绑定不可用车辆", null));
        }
        MyStaff staff = StaffMapper.getStaffInfo(staffVehi.getSfvhStaffId());
        //  新添加逻辑
        //先查询此人是否已经绑定车辆
        MyStaffVehi staffVehiInfo1 = StaffMapper.getStaffVehiInfo(staffVehi.getSfvhStaffId());
        //如果不等于空说明已经绑定车辆
        if (staffVehiInfo1 != null) {
            //删除人车表中信息
            if (StaffMapper.deleteStaffVehi(staffVehiInfo1.getSfvhStaffId()) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        }
        //2019年5月31日17:34:09 胡老大让我加，查询人员表和车辆表，两个机场代码不一样抛异常
        compareStaffAndVehi(staffVehi.getSfvhStaffId(), staffVehi.getSfvhVehiNo());
        //根据人员ID更改对应任务的车辆编号
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
        //根据人员ID查询是否已经绑定车辆
        MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staffVehi.getSfvhStaffId());
        //根据车辆编号查询此车是否已经被绑定
        MyStaffVehi vehiInfo = StaffMapper.getVehiInfo(staffVehi.getSfvhVehiNo(), staffInfo.getStaffAirportCode());
        MyVehi myVehi = new MyVehi();
        myVehi.setVehiNo(staffVehi.getSfvhVehiNo());
        if (vehiInfo == null) {
            //如果为空说明没有绑定就进行绑定
            if (staffVehiInfo == null) {
                //人员车辆绑定
                if (StaffMapper.staffAndVehi(staffVehi.getSfvhStaffId(), staffVehi.getSfvhVehiNo()) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                } else {
                    //修改车辆状态
                    myVehi.setVehiAvailability(2);
                    vehiMapper.updateVehiInfo(myVehi);

                    //创建Map集合用来推送消息
                    Map<String, Object> map = new HashMap<String, Object>();
                    //创建一个人员车辆任务对象用来装要推送的人员信息
                    MyStaffVehiTask staffTask = new MyStaffVehiTask();
                    //把查出来的人员ID赋值到人员车辆任务对象中
                    staffTask.setSfvhStaffId(staffInfo.getStaffId());
                    //把查出来的人员电话赋值到人员车辆任务对象中
                    staffTask.setStaffPhone(staffInfo.getStaffPhone());
                    staffTask.setStaffName(staffInfo.getStaffName());
                    //根据人员ID跨库查询出单条任务信息
                    MyTask tasks = fuelRecptMapper.gettaskById(staffVehi.getSfvhStaffId());
                    //根据加油员ID获取人员车辆表的信息
                    MyStaffVehi staffVehis = StaffMapper.getStaffVehiInfo(staffVehi.getSfvhStaffId());
                    //判断如果人员车辆信息是否为空
                    if (staffVehis != null) {
                        //不为空的话把车辆编号赋值进人员车辆任务对象中
                        staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                    }
                    //根据加油车编号查出对应的车辆信息
                    MyVehi vehi = vehitaskMapper.getVehiInfo(staffTask.getVehiNo(), staff.getStaffAirportCode());
                    //判断查询出来的车辆信息是否为空
                    if (vehi != null) {
                        //如果不为空把车辆ID赋值到人员车辆任务对象中
                        staffTask.setVehiId(vehi.getVehiId());
                        //如果不为空把车辆别名赋值到人员车辆任务对象中
                        staffTask.setVehiNickname(vehi.getVehiNickname());
                        //如果不为空把车辆号赋值到人员车辆任务对象中
                        staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                    }
                    //判断任务信息如果不为空
                    if (tasks != null) {
                        //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                        staffTask.setSfvhStaffStatus(2);
                        //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                        staffTask.setFlgtFlno(tasks.getTaskFlightNo());
                    } else {
                        //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId())) {
                            //如果取到说明人员登录
                            staffTask.setSfvhStaffStatus(1);
                        } else {
                            //如果取不到说明人员未登录
                            staffTask.setSfvhStaffStatus(0);
                        }
                    }
                    String AptareaCode = Constant.judgeAptareaCode(staffInfo.getStaffAptareaCode());
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                        staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
                    } else {
                        staffTask.setStaffDateIng(null);
                    }
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                        staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                    } else {
                        staffTask.setStaffDateFree(null);
                    }
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                        staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                    } else {
                        staffTask.setStaffingTaskCount(0);
                    }
                    //放入人员 对应
                    if (null != redis.get("staffAndTask:" + staffInfo.getStaffId())) {
                        String data = String.valueOf(redis.get("staffAndTask:" + staffInfo.getStaffId()));
                        String[] split = data.split(",");
                        if (split.length > 0) {
                            staffTask.setTaskId(split[0]);
                        } else {
                            staffTask.setTaskId(null);
                        }
                    } else {
                        staffTask.setTaskId(null);
                    }
                    List<MyTask> taskList = getTaskEndInfo(staff);
                    staffTask.setStaffEndTaskCount(0);
                    for (MyTask taskInfo : taskList) {
                        if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                            staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                        }
                    }
                    //把要推送的对象放进Map集合中
                    map.put("staff", staffTask);
                    //获取调度员ID
                    List<MyStaff> staffList = staffMapper.getStaffLists(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
                    String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                    SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
                    //判断任务信息如果不为空
                    if (tasks != null) {
                        // TODO KM正式发布取消注释
                        sends(tasks.getTaskId(), staffInfo.getStaffId(), staffInfo.getStaffName(), staffVehi.getSfvhVehiNo(), staffTask.getVehiPlateNo(), "2", staffInfo.getStaffAirportCode(), new Date().getTime());
                    } else {
                        // TODO KM正式发布取消注释
                        sends("", staffInfo.getStaffId(), staffInfo.getStaffName(), staffVehi.getSfvhVehiNo(), staffTask.getVehiPlateNo(), "1", staffInfo.getStaffAirportCode(), new Date().getTime());
                    }
                }
                //如果不为空说明有绑定就进行改绑
            } else {
                //更新人员车辆绑定
                if (StaffMapper.updateStaffAndVehi(staffVehi.getSfvhStaffId(), staffVehi.getSfvhVehiNo()) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                } else {

                    //绑定新车辆状态
                    myVehi.setVehiAvailability(2);
                    vehiMapper.updateVehiInfo(myVehi);

                    //通过人员ID去查询出该人员的信息
//                    MyStaff staffInfo = StaffMapper.getStaffInfo(staffVehi.getSfvhStaffId());
                    //创建Map集合用来推送消息
                    Map<String, Object> map = new HashMap<String, Object>();
                    //创建一个人员车辆任务对象用来装要推送的人员信息
                    MyStaffVehiTask staffTask = new MyStaffVehiTask();
                    //把查出来的人员ID赋值到人员车辆任务对象中
                    staffTask.setSfvhStaffId(staffInfo.getStaffId());
                    //把查出来的人员电话赋值到人员车辆任务对象中
                    staffTask.setStaffPhone(staffInfo.getStaffPhone());
                    staffTask.setStaffName(staffInfo.getStaffName());
                    MyTask tasks = fuelRecptMapper.gettaskById(staffVehi.getSfvhStaffId());
                    //根据加油员ID获取人员车辆表的信息
                    MyStaffVehi staffVehis = StaffMapper.getStaffVehiInfo(staffVehi.getSfvhStaffId());
                    //判断如果人员车辆信息是否为空
                    if (staffVehis != null) {
                        //不为空的话把车辆编号赋值进人员车辆任务对象中
                        staffTask.setVehiNo(staffVehis.getSfvhVehiNo());
                    }
                    //根据加油车编号查出对应的车辆信息
                    MyVehi vehi = vehitaskMapper.getVehiInfo(staffTask.getVehiNo(), staff.getStaffAirportCode());
                    //判断查询出来的车辆信息是否为空
                    if (vehi != null) {
                        //如果不为空把车辆ID赋值到人员车辆任务对象中
                        staffTask.setVehiId(vehi.getVehiId());
                        //如果不为空把车辆别名赋值到人员车辆任务对象中
                        staffTask.setVehiNickname(vehi.getVehiNickname());
                        //如果不为空把车辆号赋值到人员车辆任务对象中
                        staffTask.setVehiPlateNo(vehi.getVehiPlateNo());
                    }
                    //判断任务信息如果不为空
                    if (tasks != null) {
                        //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                        staffTask.setSfvhStaffStatus(2);
                        //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                        staffTask.setFlgtFlno(tasks.getTaskFlightNo());
                    } else {
                        //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                        if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId())) {
                            //如果取到说明人员登录
                            staffTask.setSfvhStaffStatus(1);
                        } else {
                            //如果取不到说明人员未登录
                            staffTask.setSfvhStaffStatus(0);
                        }
                    }
                    String AptareaCode = Constant.judgeAptareaCode(staffInfo.getStaffAptareaCode());
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                        staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
                    } else {
                        staffTask.setStaffDateIng(null);
                    }
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                        staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                    } else {
                        staffTask.setStaffDateFree(null);
                    }
                    if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                        staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                    } else {
                        staffTask.setStaffingTaskCount(0);
                    }
                    //放入人员 对应
                    if (null != redis.get("staffAndTask:" + staffInfo.getStaffId())) {
                        String data = String.valueOf(redis.get("staffAndTask:" + staffInfo.getStaffId()));
                        String[] split = data.split(",");
                        if (split.length > 0) {
                            staffTask.setTaskId(split[0]);
                        } else {
                            staffTask.setTaskId(null);
                        }
                    } else {
                        staffTask.setTaskId(null);
                    }
                    List<MyTask> taskList = getTaskEndInfo(staff);
                    staffTask.setStaffEndTaskCount(0);
                    for (MyTask taskInfo : taskList) {
                        if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                            staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                        }
                    }
                    //把要推送的对象放进Map集合中
                    map.put("staff", staffTask);
                    //获取调度员ID
                    List<MyStaff> staffList = staffMapper.getStaffLists(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
                    String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                    SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
                    //判断任务信息如果不为空
                    if (tasks != null) {
                        // TODO KM正式发布取消注释
                        sends(tasks.getTaskId(), staffInfo.getStaffId(), staffInfo.getStaffName(), staffVehi.getSfvhVehiNo(), staffTask.getVehiPlateNo(), "2", staffInfo.getStaffAirportCode(), System.currentTimeMillis());
                    } else {
                        // TODO KM正式发布取消注释
                        sends("", staffInfo.getStaffId(), staffInfo.getStaffName(), staffVehi.getSfvhVehiNo(), staffTask.getVehiPlateNo(), "1", staffInfo.getStaffAirportCode(), System.currentTimeMillis());
                    }
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 根据加油员Id获取人员详细信息如果已经绑定车辆显示车辆车牌号
     */
    @Override
    public MyStaff getStaffInfoAndVehiNo(String staffId) {
        //通过人员ID去查询出该人员的信息
        MyStaff staffInfo = StaffMapper.getStaffInfo(staffId);
        //根据人员id去查询人员车辆表，是否绑定车辆
        MyStaffVehi staffVehi = StaffMapper.getStaffVehiInfo(staffId);
        //如果不等于空说明已经绑定车辆，就去获取车辆的车牌号
        if (staffVehi != null) {
            //根据车辆编号去获取车牌号
            staffInfo.setVehiNo(StaffMapper.getVehiNo(staffVehi.getSfvhVehiNo()));
        }
        return staffInfo;
    }

    /**
     * PC端人员登出(注销)
     */
    @Override
    public void PCremoveStaff(MyStaff staff) {
        //在redis删除前台传过来的人员id
        logout(staff, "PClogin:");
        if (staff.getLoginUserIn() != null && staff.getLoginUserIn().getToken() != null) {
            stringRedisTemplate.delete(Constant.TOKENS + staff.getLoginUserIn().getToken());
        }
    }

    /**
     * PC端人员登录
     */
    @Override
    public Map<String, Object> PCstaffLogin(String staffId, String staffPwd) {
        //通过人员ID去查询出该人员的信息
        MyStaff staff = StaffMapper.getStaffInfo(staffId);
        //创建一个Map集合来存放返回的信息
        Map<String, Object> infoMap = new HashMap<String, Object>();
        //同时判断账号和密码，如果对象等于NULL说明账号不正确，如果不等于NULL说明账号正确
        //如果查出来的密码和用户输入的一样的话说明密码正确，一样的话说明密码错误
        if (staff != null && staff.getStaffPwd().equals(staffPwd)) {
            //开始判断岗位,判断岗位是否是调度员
            if (Constant.DISPATCH_STAFF.equals(staff.getStaffType()) || Constant.MANAGER_STAFF.equals(staff.getStaffType())) {

                //删除之前登录的。
                Object object = redis.get("PClogin:" + staff.getStaffId());
                logout(staff, "PClogin:");
                if (null != object) {
                    logoutWS(staff);
                    redis.remove(Constant.TOKENS + object.toString());
                }
                infoMap.put("staffInfo", staff);
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(staff, loginUser);
                infoMap.put("token", tokenService.saveToken(loginUser).getToken());

                //登录信息无误，说明登录成功，把调度员id放进redis中，key为 PClogin:员工id
                redis.set("PClogin:" + staff.getStaffId(), loginUser.getToken());
                stringRedisTemplate.expire("PClogin:" + staff.getStaffId(), expireSeconds, TimeUnit.SECONDS);
                redis.set("PClogin:" + staff.getStaffId() + ":" + staff.getStaffAirportCode() + ":" + staff.getStaffAptareaCode() + ":" + Constant.PAD_BROKEN_NETWORK, staff.getStaffAirportCode());
            } else {
                //判断如果不是调度员则往map集合中存放 value为您不是调度员  key为error
                infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NO_DISPATCH_STAFF_VALUE);
                throw new CustomException(ReturnMsg.getInstanceNGz(Constant.NO_DISPATCH_STAFF_VALUE, null, 401));
            }
        } else {
            //判断如果账号或密码不对则往map集合中存放  value为账号或密码错误  key为error
            infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NAME_PWD_ERROR_VALUE);
            throw new CustomException(ReturnMsg.getInstanceNGz(Constant.NAME_PWD_ERROR_VALUE, null, 401));
        }
        //把map集合返回到controller

        return infoMap;
    }

    /**
     * 根据加油员ID获取人员车辆表的信息（供远程调用）
     */
    @Override
    public MyStaffVehi getStaffVehiInfo(String taskOpeStaffId) {
        return StaffMapper.getStaffVehiInfo(taskOpeStaffId);
    }

    /**
     * 根据加油车编号查出对应的车辆信息（供远程调用）
     */
    @Override
    public MyVehi getVehiInfo(MyStaffVehiTask staffTask) {

        MyStaff staffInfo = StaffMapper.getStaffInfo(staffTask.getSfvhStaffId());
        if (staffInfo == null || StringUtils.isBlank(staffInfo.getStaffAirportCode())) {
            return null;
        }
        return vehitaskMapper.getVehiInfo(staffTask.getVehiNo(), staffInfo.getStaffAirportCode());
    }

    /**
     * 获取所有人员ID（供远程调用使用）
     */
    @Override
    public List<MyStaff> getStaffLists(String staffAirportCode, String staffAptareaCode) {
        return StaffMapper.getStaffLists(staffAirportCode, staffAptareaCode);
    }

    /**
     * 人车解绑
     * 人车解绑
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteStaffVehi(MyStaff staff) {
        //先查询此人是否已经绑定车辆
        MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staff.getLoginUserIn().getStaffId());
        //如果不等于空说明已经绑定车辆
        if (staffVehiInfo != null) {
            //删除人车表中信息
            if (StaffMapper.deleteStaffVehi(staffVehiInfo.getSfvhStaffId()) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            } else {
                // 车辆状态修改
                MyVehi myVehi = new MyVehi();
                myVehi.setVehiNo(staffVehiInfo.getSfvhVehiNo());
                myVehi.setVehiAvailability(1);
                vehiMapper.updateVehiInfo(myVehi);

                //通过人员ID去查询出该人员的信息
                MyStaff staffInfo = StaffMapper.getStaffInfo(staff.getLoginUserIn().getStaffId());
                //创建Map集合用来推送消息
                Map<String, Object> map = new HashMap<String, Object>();
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把查出来的人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffInfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffInfo.getStaffPhone());
                staffTask.setStaffName(staffInfo.getStaffName());
                //根据人员ID跨库查询出单条任务信息
                MyTask tasks = fuelRecptMapper.gettaskById(staffInfo.getStaffId());
                //判断任务信息如果不为空
                if (tasks != null) {
                    //查出来的任务信息不为空的话说明正在工作中，往人员车辆任务对象的加油员状态赋值2正在工作中
                    staffTask.setSfvhStaffStatus(2);
                    //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                    staffTask.setFlgtFlno(tasks.getTaskFlightNo());
                } else {
                    //首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                    if (null != redis.get(Constant.LOGIN_KEY + staff.getLoginUserIn().getStaffId())) {
                        //如果取到说明人员登录
                        staffTask.setSfvhStaffStatus(1);
                    } else {
                        //如果取不到说明人员未登录
                        staffTask.setSfvhStaffStatus(0);
                    }
                }
                String AptareaCode = Constant.judgeAptareaCode(staffInfo.getStaffAptareaCode());
                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                    staffTask.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
                } else {
                    staffTask.setStaffDateIng(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                    staffTask.setStaffDateFree(String.valueOf(redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")));
                } else {
                    staffTask.setStaffDateFree(null);
                }
                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                    staffTask.setStaffingTaskCount((int) redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount"));
                } else {
                    staffTask.setStaffingTaskCount(0);
                }
                List<MyTask> taskList = getTaskEndInfo(staffInfo);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfo : taskList) {
                    if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }
                //把要推送的对象放进Map集合中
                map.put("staff", staffTask);
                //获取调度员ID
                List<MyStaff> staffList = staffMapper.getStaffLists(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
                String userId = staffList.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);

            }
        }
    }

    /**
     * 根据机场代码获取人员的分组名
     */
    @Override
    public ArrayList<Object> getStaffGroupName(MyStaff staff) {
        List<MyStaff> lists = StaffMapper.getStaffGroupName(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (MyStaff str : lists) {
            if (str != null && str.getStaffGroupId() != null) {
                arrayList.add(str.getStaffGroupId());
            }
        }

        return arrayList;
    }

    /**
     * 修改分组
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateGruop(ArrayList<MyStaffVehiTask> staffvehi, MyStaff staff) {
        Integer maxLevel = null;
        Integer publicLevel = -1;
        if (staffvehi.size() > 0) {
            maxLevel = StaffMapper.findMaxLevel(staffvehi.get(0).getStaffGroupId());
        }
        for (MyStaffVehiTask myStaffVehiTask : staffvehi) {
            if (maxLevel != null) {
                maxLevel = maxLevel + 1;
                if (StaffMapper.updateGruopAndLevel(myStaffVehiTask.getSfvhStaffId(), staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), myStaffVehiTask.getStaffGroupId(), maxLevel) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                }
            } else {
                publicLevel = publicLevel + 1;
                if (StaffMapper.updateGruopAndLevel(myStaffVehiTask.getSfvhStaffId(), staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode(), myStaffVehiTask.getStaffGroupId(), publicLevel) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                }
            }
        }
    }

    /**
     * 根据角色类型查出该角色的常用消息
     */
    @Override
    public List<MyMessage> getStaffMessageByType(MyStaff staff) {
        return StaffMapper.getStaffMessageByType(staff.getLoginUserIn().getStaffType(), staff.getLoginUserIn().getStaffAirportCode());
    }

    /**
     * 根据机场代码查出该角色的常用消息
     */
    @Override
    public List<MyMessage> getStaffMessageByCode(MyStaff staff) {
        return StaffMapper.getStaffMessageByCode(staff.getLoginUserIn().getStaffAirportCode());
    }

    /**
     * 根据消息ID删除消息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteStaffMessage(MyMessage message) {
        if (StaffMapper.deleteStaffMessage(message.getMsgId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 新增消息内容
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void addStaffMessage(MyStaff staff, MyMessage message) {
        //生成UUID为航班ID
        String uuid = UUID.randomUUID().toString();
        if (StaffMapper.addStaffMessage(uuid, message.getMsgRole(), staff.getLoginUserIn().getStaffAirportCode(), message.getMsgContent()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
    }

    /**
     * 查询销售信息
     */
    @Override
    public List<MySalesInfo> getSalesInfo(MyStaff staff) {
        return StaffMapper.getSalesInfo(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
    }

    /**
     * 删除销售信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void deleteSalesInfo(MySalesInfo salesInfo) {
        if (StaffMapper.deleteSalesInfo(salesInfo.getSalesId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 新增销售信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void addSalesInfo(MyStaff staff, MySalesInfo salesInfo) {
        //生成UUID为航班ID
        String uuid = UUID.randomUUID().toString();
        salesInfo.setSalesId(uuid);
        salesInfo.setSalesAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        salesInfo.setSalesAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        if (StaffMapper.addSalesInfo(salesInfo) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
    }

    /**
     * 销售信息详情
     */
    @Override
    public MySalesInfo getSalesInfoById(MySalesInfo salesInfo) {
        return StaffMapper.getSalesInfoById(salesInfo.getSalesId());
    }

    /**
     * 修改销售信息
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public void updateSalesInfo(MySalesInfo salesInfo) {
        if (StaffMapper.updateSalesInfo(salesInfo) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 根据机场代码获取所有的加油员信息（供远程调用）
     */
    @Override
    public List<MyStaff> getStaffInfoList(MyStaff staff) {
        return StaffMapper.getStaffInfoList(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
    }

    @Override
    public void deleteToken(MyStaff staff) {
        log.info("请求参数：" + staff.toString());
        if (staff != null) {
            if (!StringUtils.isEmpty(staff.getStaffId())) {
                MyStaff staffInfo = StaffMapper.getStaffInfo(staff.getStaffId());
                System.out.println("查询不到人员" + staff.getStaffId());
                if (staffInfo == null) {
                    return;
                }
                //先查询此人是否已经绑定车辆
                MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staff.getStaffId());
                //如果不等于空说明已经绑定车辆
                if (staffVehiInfo != null) {
                    //删除人车表中信息
                    if (StaffMapper.deleteStaffVehi(staff.getStaffId()) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                    }
                }
                //创建Map集合用来推送消息
                Map<String, Object> map = new HashMap<String, Object>();
                //创建一个人员车辆任务对象用来装要推送的人员信息
                MyStaffVehiTask staffTask = new MyStaffVehiTask();
                //把人员ID赋值到人员车辆任务对象中
                staffTask.setSfvhStaffId(staffInfo.getStaffId());
                //把查出来的人员电话赋值到人员车辆任务对象中
                staffTask.setStaffPhone(staffInfo.getStaffPhone());
                staffTask.setStaffName(staffInfo.getStaffName());
                //把查出来的任务对象中的航班号赋值到人员车辆任务对象中
                staffTask.setStaffDateIng(null);
                staffTask.setStaffDateFree(null);

                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + staffInfo.getStaffAptareaCode() + ":" + "staffDateIng")) {
                    redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + staffInfo.getStaffAptareaCode() + ":" + "staffDateIng", "");
                }
                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + staffInfo.getStaffAptareaCode() + ":" + "freetime")) {
                    redis.set(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + staffInfo.getStaffAptareaCode() + ":" + "freetime", "");
                }
                if (null != redis.get(Constant.LOGIN_KEY + staffInfo.getStaffId() + ":" + staffInfo.getStaffAirportCode() + ":" + staffInfo.getStaffAptareaCode() + ":" + "taskCount")) {
                    staffTask.setStaffingTaskCount(0);
                }
                //放入人员 对应
                if (null != redis.get("staffAndTask:" + staffInfo.getStaffId())) {
                    redis.set("staffAndTask:" + staffInfo.getStaffId(), "");
                    staffTask.setTaskId("");
                }
                staffTask.setStaffEndTaskCount(0);
                //如果取不到说明人员未登录
                staffTask.setSfvhStaffStatus(0);
                List<MyTask> taskList = getTaskEndInfo(staffInfo);
                staffTask.setStaffEndTaskCount(0);
                for (MyTask taskInfo : taskList) {
                    if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staffInfo.getStaffId())) {
                        staffTask.setStaffEndTaskCount(staffTask.getStaffEndTaskCount() + 1);
                    }
                }

                //把要推送的对象放进Map集合中
                map.put("staff", staffTask);
                //获取调度员ID
                List<MyStaff> staffList = StaffMapper.getStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
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
                //人员登出 发送给PC (胡)
                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.STAFF, Constant.PAD_STAFF_VEHI, map);
            }
        }
    }

    @Override
    public MyStaffVehi judgeStaffAndVehi(MyVehi tVehiInfo, MyStaff staff) {
        //先查询此人是否已经绑定车辆
        MyStaffVehi staffVehiInfo = StaffMapper.getStaffVehiInfo(staff.getStaffId());
        //如果不等于空说明已经绑定车辆
        if (staffVehiInfo != null) {
            return staffVehiInfo;
        }
        return null;
    }

    /**
     * 功能描述：根据车牌编号是否被绑定
     *
     * @param tVehiInfo 车辆信息
     * @param staff     操作员
     * @return com.zh.bean.login.MyStaffVehi
     * @author zhaojiacan
     * @date 2024/4/24
     */
    @Override
    public MyStaffVehi judgeVehiIsBind(MyVehi tVehiInfo, MyStaff staff) {
        String airportCode = Optional.ofNullable(staff).map(MyStaff::getLoginUserIn).map(LoginUser::getStaffAirportCode).orElse("");
        String staffId = Optional.ofNullable(staff).map(MyStaff::getLoginUserIn).map(LoginUser::getStaffId).orElse("");
        if (StrUtil.isBlank(airportCode)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("机场编码不能为空", null));
        }
        //先查询此车是否已经被绑定
        List<MyStaffVehi> staffVehiInfos = StaffMapper.getVehiBindInfo(tVehiInfo.getVehiNo(), airportCode);
        if (CollUtil.isEmpty(staffVehiInfos)) {
            return null;
        }
        Optional<MyStaffVehi> selfStaffVehi = staffVehiInfos.stream().filter(staffVehiInfo -> staffVehiInfo.getSfvhStaffId().equals(staffId)).findFirst();
        if (selfStaffVehi.isPresent()) {
            return selfStaffVehi.get();
        } else {
            return staffVehiInfos.get(0);
        }
    }

    /**
     * PC端管理系统人员登录
     */
    @Override
    public Map<String, Object> PCManagestaffLogin(String staffId, String staffPwd) {
        //通过人员ID去查询出该人员的信息
        MyStaff staff = StaffMapper.getStaffInfo(staffId);
        //创建一个Map集合来存放返回的信息
        Map<String, Object> infoMap = new HashMap<String, Object>();
        //同时判断账号和密码，如果对象等于NULL说明账号不正确，如果不等于NULL说明账号正确
        //如果查出来的密码和用户输入的一样的话说明密码正确，一样的话说明密码错误
        if (staff != null && staff.getStaffPwd().equals(staffPwd)) {
            List<TParam> allByPcTparam = tParamService.getAllByPcTparam(staff.getStaffAirportCode());
            infoMap.put("tParams", allByPcTparam.size() == 0 ? allByPcTparam : Lists.newArrayList());
            //开始判断岗位,判断岗位是否是管理员和超级管理员
            if (Constant.CMANAGER_STAFF.equals(staff.getStaffType()) || Constant.MANAGER_STAFF.equals(staff.getStaffType()) || Constant.DISPATCH_STAFF.equals(staff.getStaffType())) {
                //删除之前登录的。
                Object object = redis.get("PClogin:" + staff.getStaffId());
                logout(staff, "PClogin:");
                if (null != object) {
                    logoutWS(staff);
                    redis.remove(Constant.TOKENS + object.toString());
                }
                infoMap.put("staffInfo", staff);
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(staff, loginUser);
                infoMap.put("token", tokenService.saveToken(loginUser).getToken());
                //登录信息无误，说明登录成功，把调度员id放进redis中，key为 PClogin:员工id
                redis.set("PClogin:" + staff.getStaffId(), loginUser.getToken());
                redis.set("PClogin:" + staff.getStaffId() + ":" + staff.getStaffAirportCode() + ":" + staff.getStaffAptareaCode() + ":" + Constant.PAD_BROKEN_NETWORK, staff.getStaffAirportCode());
            } else {
                //判断如果不是调度员则往map集合中存放 value为您不是调度员  key为error
                infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NO_VALUE);
            }
        } else {
            //判断如果账号或密码不对则往map集合中存放  value为账号或密码错误  key为error
            infoMap.put(Constant.LOGIN_ERROR_KEY, Constant.NAME_PWD_ERROR_VALUE);
        }
        //把map集合返回到controller
        return infoMap;
    }

    /**
     * 2019年5月31日17:53:48 比较staff和vehi机场代码是否相同
     *
     * @param staffId
     * @param vehiNo
     * @return
     */
    private void compareStaffAndVehi(String staffId, String vehiNo) {
        Assert.hasText(staffId, "人员ID不能为空");
        Assert.hasText(vehiNo, "车辆号牌不能为空");
        MyStaff staffInfo = StaffMapper.getStaffInfo(staffId);
        Assert.notNull(staffInfo, String.format("ID:%s查询人员失败", staffId));
        MyVehi vehi = vehiMapper.findVehiById(vehiNo, staffInfo.getStaffAirportCode());
        Assert.notNull(vehi, String.format("人员所属机场{%s}和车辆所属机场不一致，请检查", staffInfo.getStaffAirportCode()));
    }


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
}
