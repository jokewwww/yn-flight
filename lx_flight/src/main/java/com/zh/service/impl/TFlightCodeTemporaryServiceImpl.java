package com.zh.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFlightTask;
import com.zh.bean.flight.TFlightCodeTemporary;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.TStaff;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.dao.mapper.my.TFlightCodeTemporaryMapper;
import com.zh.exception.CustomException;
import com.zh.service.TFlightCodeTemporaryService;
import com.zh.service.TaskService;
import com.zh.util.SendMsg2Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class TFlightCodeTemporaryServiceImpl implements TFlightCodeTemporaryService {

    @Autowired
    private TFlightCodeTemporaryMapper tFlightCodeTemporaryMapper;
    @Autowired
    private TaskService tasksService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private StaffMapper staffMapper;


    @Override
    public List<TFlightCodeTemporary> getTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary) {
        return tFlightCodeTemporaryMapper.getTFlightCodeTemporary(tFlightCodeTemporary);
    }

    @Override
    public TFlightCodeTemporary addTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary) {
        tFlightCodeTemporary.setFlno(StrUtil.isBlank(tFlightCodeTemporary.getFlno()) ? null : tFlightCodeTemporary.getFlno());
        TFlightCodeTemporary one = tFlightCodeTemporaryMapper.getOne(tFlightCodeTemporary);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        tFlightCodeTemporaryMapper.addTFlightCodeTemporary(tFlightCodeTemporary);
        return tFlightCodeTemporary;
    }

    @Override
    public int updateTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary) {
        tFlightCodeTemporary.setFlno(StrUtil.isBlank(tFlightCodeTemporary.getFlno()) ? null : tFlightCodeTemporary.getFlno());
        TFlightCodeTemporary one = tFlightCodeTemporaryMapper.getOne(tFlightCodeTemporary);
        if (ObjectUtil.isNotNull(one) && !one.getId().equals(tFlightCodeTemporary.getId())) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        int i = tFlightCodeTemporaryMapper.updateTFlightCodeTemporary(tFlightCodeTemporary);

        Date now = new Date();
        String nowStr = cn.hutool.core.date.DateUtil.formatDate(now);
        String beforeDatetr = cn.hutool.core.date.DateUtil.formatDate(cn.hutool.core.date.DateUtil.offset(now, DateField.HOUR, -4));
        if (!nowStr.equals(beforeDatetr)) {
            nowStr = beforeDatetr;
        }
        // 异步操作, 查询今天是否有该飞机号和航班号的航班，如果飞机号发生变更，则socket通知调度端和pad
        MyFlightTask flight = tasksService.getFlightByRegnAndFlno(null, null, nowStr, tFlightCodeTemporary.getArcrRegn(), tFlightCodeTemporary.getFlno());
        if (ObjectUtil.isNotNull(flight)) {
            flight.setFlgtUpdateTime(new Date());
            Map<String, Object> webMap = new HashMap<String, Object>();
            webMap.put("staff", new TStaff());
            // 把要推送的航班任务对象放进Map集合中
            webMap.put("flight", flight);
            // 判断如果航班是本场的话再推送一条本场航班消息
            if (Constant.FLGT_DGAME.equals(flight.getFlgtGame())) {
                // 把要推送的航班任务对象放进Map集合中
                webMap.put("selfFlight", flight);
            }
            List<MyStaff> allStaffList = staffMapper.getAllStaffList(flight.getFlgtAirportCode(), null, null);
            String staffIdsStr = allStaffList.stream().map(staff -> String.valueOf(staff.getStaffId())).collect(Collectors.joining(","));
            SendMsg2Redis.testDingYue(stringRedisTemplate, staffIdsStr, Constant.TASKFLIGHT,
                    Constant.PC_FLIGHT_UPDATE, webMap);
            String taskOpeStaffId = flight.getTaskOpeStaffId();
            if (taskOpeStaffId != null) {
                SendMsg2Redis.testDingYue(stringRedisTemplate, taskOpeStaffId, Constant.TASKFLIGHT,
                        Constant.PC_FLIGHT_UPDATE, webMap);
            }
        }
        return i;
    }

    @Override
    public int deleteTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary) {
        return tFlightCodeTemporaryMapper.deleteTFlightCodeTemporary(tFlightCodeTemporary.getId());
    }
}
