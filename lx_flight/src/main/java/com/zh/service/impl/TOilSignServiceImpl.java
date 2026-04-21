package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFlight;
import com.zh.bean.flight.MyFlightTask;
import com.zh.bean.flight.MyTask;
import com.zh.bean.flight.TOilSign;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.FlightMapper;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.dao.mapper.my.TOilSignMapper;
import com.zh.dao.mapper.my.TaskMapper;
import com.zh.exception.CustomException;
import com.zh.service.TOilSignService;
import com.zh.util.SendMsg2Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TOilSignServiceImpl implements TOilSignService {

    private final static Logger log = LoggerFactory.getLogger(TOilSignServiceImpl.class);
    @Autowired
    public TOilSignMapper tOilSignMapper;
    @Autowired
    public FlightMapper flightMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TaskMapper taskMapper;

    /**
     * 新建
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertOilSign(TOilSign oilSign) {
        System.out.println("----------------新增----------------------");
        System.out.println("flgt_id:" + oilSign.getFlgtId() + "Json:" + JSON.toJSONString(oilSign));

        // 插入
        int i = tOilSignMapper.insertSelective(oilSign);

        // 修改预计加油量
        MyFlight myFlight = new MyFlight();
        myFlight.setFlgtId(oilSign.getFlgtId());
        myFlight.setFlgtTakeoffFuel(oilSign.getFlgtTakeoffFuel());
        myFlight.setFlgtChockFuel(oilSign.getFlgtChockFuel());
        myFlight.setFlgtOtatFuel(oilSign.getFlgtOtatFuel());
        int j = flightMapper.updateFlightFuel(myFlight);

        //根据航班ID查询任务信息
        MyTask taskInfoById = flightMapper.getTaskInfoById(oilSign.getFlgtId());
        MyFlightTask taskAndFlightById = new MyFlightTask();
        if (taskInfoById != null) {
            // 根据任务ID查出单条任务航班信息
            taskAndFlightById = taskMapper.getTaskAndFlightById(taskInfoById.getTaskId());
        } else {
            // 根据航班ID查出航班单条记录
            taskAndFlightById = flightMapper.getFlightInfoById(oilSign.getFlgtId());
        }
        //把要推送的航班任务对象放进Map集合中
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
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

        if (i != 1 || j != 1) {
            System.out.println("插入数据失败:返回值:" + i + "Json:" + JSON.toJSONString(oilSign));
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
        return i;
    }

    @Override
    @Transactional
    public Integer updateOilSign(TOilSign oilSign) {
        System.out.println("修改flgt_id:" + oilSign.getFlgtId() + "Json:" + JSON.toJSONString(oilSign));
        try {
            return tOilSignMapper.updateByPrimaryKeySelective(oilSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询
     */
    @Override
    public List<TOilSign> select() {
        return tOilSignMapper.select();
    }

    @Override
    public TOilSign selectOilSign(TOilSign oilSign) {
        return tOilSignMapper.selectByPrimaryKey(oilSign.getId());
    }

    @Override
    public TOilSign selectByFlgtId(TOilSign oilSign) {
        return tOilSignMapper.selectByFlgtId(oilSign.getFlgtId());
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOilSign(TOilSign oilSign) {
        if (tOilSignMapper.deleteByPrimaryKey(oilSign.getId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

}
