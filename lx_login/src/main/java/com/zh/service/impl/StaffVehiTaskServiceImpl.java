package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTask;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.MyVehi;
import com.zh.component.RedisComponent;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.StaffVehiTaskService;
import com.zh.util.ComparatorEntity;
import com.zh.util.EnumTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class StaffVehiTaskServiceImpl implements StaffVehiTaskService {

    @Autowired
    Prop prop;

    @Autowired
    private MyFuelRecptMapper fuelRecptMapper;

    @Autowired
    private MyStaffVehiTaskMapper vehitaskMapper;

    @Autowired
    private StaffMapper StaffMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${fuel.type}")
    private Integer lock;
    @Autowired
    private RedisComponent redis;
    @Autowired
    private MySettingMapper mysettingmapper;
    @Autowired
    private MySettingMapper mySettingMapper;

    @Autowired
    private TaskMapper taskMapper;

    public static void main(String[] args) {
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
        System.out.println(nowStr);
    }

    /**
     * 查询人员车辆信息
     *
     * @param flight
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MyStaffVehiTask> findStaffvehi(MyStaff task) {
        // 根据获取的机场所属代码，机场所属区域代码去查询所属员工
        List<MyStaffVehiTask> staffList = vehitaskMapper.findStafflist(task.getLoginUserIn().getStaffAirportCode(),
                task.getLoginUserIn().getStaffAptareaCode());
        // 获取所有加油员当前正在工作的任务
        List<MyTask> tasksList = gettaskListById(staffList);
        MyStaff staffInfo = new MyStaff();
        staffInfo.setStaffAirportCode(task.getLoginUserIn().getStaffAirportCode());
        staffInfo.setStaffAptareaCode(task.getLoginUserIn().getStaffAptareaCode());
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
        List<MyTask> taskList = taskMapper.getTaskEndInfo(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode(), nowStr);
        for (MyStaffVehiTask staff : staffList) {
            staff.setSfvhStaffStatus(0);
            staff.setStaffingTaskCount(0);
            // 首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
            if (null != redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId())) {
                // 如果取到说明人员登录
                staff.setSfvhStaffStatus(1);
            } else {
                // 如果取不到说明人员未登录
                staff.setSfvhStaffStatus(0);
            }
            String AptareaCode = Constant.judgeAptareaCode(task.getLoginUserIn().getStaffAptareaCode());
            if (tasksList != null && tasksList.size() > 0) {
                for (MyTask myTask : tasksList) {
                    if (staff.getSfvhStaffId().equals(myTask.getTaskOpeStaffId())) {
                        staff.setFlgtFlno(myTask.getTaskFlightNo());
                        staff.setSfvhStaffStatus(2);
                        break;
                    } else {
                        // 首先判断ID不为空，然后再使用ID去redis中取对应的人员信息
                        if (null != redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId())) {
                            // 如果取到说明人员登录
                            staff.setSfvhStaffStatus(1);
                        } else {
                            // 如果取不到说明人员未登录
                            staff.setSfvhStaffStatus(0);
                        }
                    }
                }
            }
            if (null != redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")) {
                staff.setStaffDateIng(String.valueOf(redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "staffDateIng")));
            } else {
                staff.setStaffDateIng(null);
            }
            Object freetime = null;
            if (null != redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime")) {
                freetime = redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "freetime");
            }
            if (freetime == null) {
                staff.setStaffDateFree(null);
            } else {
                staff.setStaffDateFree(String.valueOf(freetime));
            }
            if (null != redis.get("staffAndTask:" + staff.getSfvhStaffId())) {
                String s = String.valueOf(redis.get("staffAndTask:" + staff.getSfvhStaffId()));
                String[] split = s.split(",");
                staff.setTaskId(split[0]);
            } else {
                staff.setTaskId(null);
            }
            Object taskCount = null;
            if (null != redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount")) {
                taskCount = redis.get(Constant.LOGIN_KEY + staff.getSfvhStaffId() + ":" + task.getLoginUserIn().getStaffAirportCode() + ":" + AptareaCode + ":" + "taskCount");
            }
            if (taskCount == null) {
                staff.setStaffingTaskCount(0);
            } else {
                staff.setStaffingTaskCount((int) taskCount);
            }
        }
        for (MyStaffVehiTask staff : staffList) {
            if (lock == 1) {
                //TODO获取排班序号 临时方法
                for (EnumTeam e : EnumTeam.values()) {
                    if (null != e) {
                        Object object = redis.get(e.toString());
                        if (object != null && !StringUtils.isEmpty(String.valueOf(object))) {
                            if (staff.getSfvhStaffId().equals(String.valueOf(object))) {
                                staff.setClassName(e.toString());
                            }
                        }
                    }
                }
            }
            staff.setStaffEndTaskCount(0);
            for (MyTask taskInfo : taskList) {
                if (taskInfo.getTaskOpeStaffId() != null && taskInfo.getTaskOpeStaffId().equals(staff.getSfvhStaffId())) {
                    staff.setStaffEndTaskCount(staff.getStaffEndTaskCount() + 1);
                }
                if (staff.getTaskId() != null && taskInfo.getTaskId().equals(staff.getTaskId())) {
                    staff.setTaskStatus(taskInfo.getTaskStatus());
                }
            }
        }
        ComparatorEntity.listSort(staffList);
        return staffList;
    }

    /**
     * 查询车辆信息(所属机场所有车辆)
     */
    @Override
    public List<MyVehi> findListVehi(MyStaff staff) {
        if ("0".equals(staff.getLoginUserIn().getStaffType())) {
            return vehitaskMapper.findListVehi(null, null);
        } else {
            return vehitaskMapper.findListVehi(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        }
    }

    /**
     * 显示登录人员所属机场的人员列表
     */
    @Override
    public List<MyStaff> findstafflist(MyStaff staff) {
        if ("0".equals(staff.getLoginUserIn().getStaffType()) || ObjectUtil.equal(1, staff.getStaffAge())) {
            return vehitaskMapper.findStafflists(null, null);
        } else {
            return vehitaskMapper.findStafflists(staff.getLoginUserIn().getStaffAptareaCode(), staff.getLoginUserIn().getStaffAirportCode());
        }
    }

    /**
     * 新增人员信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public Integer insertstaff(MyStaff staff) {
        //通过人员ID去查询出该人员的信息
        MyStaff staffInfo = StaffMapper.getStaffInfo(staff.getStaffId());
        //判断查出来的人员信息是否为空，如果为空就执行新增
        if (staffInfo == null) {
            if (null != staff.getLoginUserIn().getStaffType() && "1".equals(staff.getLoginUserIn().getStaffType())) {
                staff.setStaffAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            }
            if (vehitaskMapper.insertstaff(staff) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            //判断如果是调度员就新增默认的配置信息
            if ("2".equals(staff.getStaffType())) {
                String type1UUID = UUID.randomUUID().toString();
                String type1 = mysettingmapper.findsettinglist("", "default", "1");
                if (vehitaskMapper.addSettingType1(type1UUID, staff.getStaffId(), staff.getStaffAirportCode(), type1) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                }
                String type2UUID = UUID.randomUUID().toString();
                String type2 = mysettingmapper.findsettinglist("", "default", "2");
                if (vehitaskMapper.addSettingType2(type2UUID, staff.getStaffId(), staff.getStaffAirportCode(), type2) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                }
            }
            return 0;
            //否者就说明已经存在，就不新增
        } else {
            return 1;
        }
    }

    /**
     * 修改人员信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updatestaff(MyStaff staff) {
        if (vehitaskMapper.updatestaff(staff) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //判断如果是调度员就新增默认的配置信息
        if ("2".equals(staff.getStaffType())) {
            //删除之前的配置，重新新增配置
            mysettingmapper.deleteSetting(staff.getStaffId());

            String type1UUID = UUID.randomUUID().toString();
            String type1 = mysettingmapper.findsettinglist("", "default", "1");
            if (vehitaskMapper.addSettingType1(type1UUID, staff.getStaffId(), staff.getStaffAirportCode(), type1) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            String type2UUID = UUID.randomUUID().toString();
            String type2 = mysettingmapper.findsettinglist("", "default", "2");
            if (vehitaskMapper.addSettingType2(type2UUID, staff.getStaffId(), staff.getStaffAirportCode(), type2) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
        }
    }

    /**
     * 删除人员信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deletestaff(MyStaff staff) {
        if (vehitaskMapper.deletestaff(staff) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        try {
            mySettingMapper.deleteSetting(staff.getStaffId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改车辆信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public Integer updatevehi(MyVehi vehi) {
        //根据车辆ID查询车辆信息
        MyVehi vehionefind = vehitaskMapper.vehionefind(vehi.getVehiId());
        //根据车辆编号查询车辆信息
        //vehiAirportCode":"2907"
        MyVehi vehiInfo = vehitaskMapper.getVehiInfoAndVehiAirportCode(vehi.getVehiNo(), vehi.getVehiAirportCode());
        //根据车牌号查询车辆信息
        MyVehi vehiInfos = vehitaskMapper.getVehiInfos(vehi.getVehiPlateNo());
        if (vehiInfo != null && vehiInfos != null) {
            if (vehionefind.getVehiId().equals(vehiInfo.getVehiId())) {
                if (vehionefind.getVehiId().equals(vehiInfos.getVehiId())) {
                    if (vehitaskMapper.updatevehi(vehi) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                    }
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 0;
            }
            //为空的话说明不存在此车才新增
        } else {
            if (vehitaskMapper.updatevehi(vehi) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
            return 1;
        }
    }

    /**
     * 删除车辆信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deletevehi(MyVehi vehi) {
        if (vehitaskMapper.deletevehi(vehi) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 人员详情
     */
    @Override
    public MyStaff staffonefind(MyStaff staff) {
        return vehitaskMapper.staffonefind(staff.getStaffId());
    }

    /**
     * 车辆详情
     */
    @Override
    public MyVehi vehionefind(MyVehi vehi) {
        return vehitaskMapper.vehionefind(vehi.getVehiId());
    }

    /**
     * 查询人员姓名有多少个
     */
    @Override
    public int selectstaffname(MyStaff staff) {
        return vehitaskMapper.selectstaffname(staff.getStaffName());
    }

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
}
