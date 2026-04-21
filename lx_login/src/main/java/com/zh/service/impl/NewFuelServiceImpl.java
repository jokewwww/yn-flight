package com.zh.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.login.MyFuel;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.TParam;
import com.zh.dao.mapper.my.NewFuelMapper;
import com.zh.dao.mapper.my.TParamMapper;
import com.zh.quartz.QuartzManager;
import com.zh.service.NewFuelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NewFuelServiceImpl implements NewFuelService, CommandLineRunner {

    private static final String JOB_NAME = "OIL_PARAMS";

    private final static Logger log = LoggerFactory.getLogger(NewFuelServiceImpl.class);

    @Autowired
    private NewFuelMapper fuelMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TParamMapper tParamMapper;

    @Autowired
    private QuartzManager quartzManager;

    @Value("${fuel.type}")
    private Integer fuelType;

    /**
     * 查看油料信息
     *
     * @param staff
     * @return
     */
    @Override
    public List<MyFuel> findNewfuels(MyStaff staff) {
        return fuelMapper.findNewFuelsToday(staff.getLoginUserIn().getStaffAirportCode(), fuelType == 1 ? 4 : 1);
    }

    /**
     * 更新油料信息
     *
     * @param fuelList
     * @return
     */
    @Override
    @Transactional
    public int saveNewFuels(List<MyFuel> fuelList, MyStaff staff) {
        //清除所有任务
        String staffName = staff.getLoginUserIn() != null ? staff.getLoginUserIn().getStaffName() : null;
        String airportCode = staff.getLoginUserIn() != null ? staff.getLoginUserIn().getStaffAirportCode() : null;
        return fuelList.stream()
//                .skip(1)
                .filter(fuel -> StringUtils.isNotEmpty(fuel.getFuelTestBillNo()))
                .peek(fuel -> {
                    if (null == fuel.getFuelType()) {
                        fuel.setFuelType(0);
                    }
                    fuel.setOld(true);
                    if (StringUtils.isEmpty(fuel.getFuelCheckName())) {
                        fuel.setFuelCheckName(staffName);
                    }
                    fuel.setFuelAirportCode(airportCode);
                    fuel.setFuelStaffName(staffName);
                    fuel.setFuelCreateTime(new Date());
                })
                .map(this::createFuelSchedule)
                .allMatch(o -> o == 1) ? 1 : 0;
    }

    private int createFuelSchedule(MyFuel fuel) {
        int res = fuelMapper.addNewFuel(fuel);
        System.out.println(System.currentTimeMillis() + "保存成功");
        quartzManager.addSimpleTriggerJob(
                "fuel@" + fuel.getId(),
                "fuel@" + fuel.getFuelAirportCode(),
                "trigger@" + fuel.getId(),
                "trigger@" + fuel.getFuelAirportCode(),
                MyFuelJobService.class,
                fuel.getFuelDate(),
                5,
                10,
                new JobDataMap(BeanMap.create(fuel)));

        //计划油料
        int compare = DateUtil.compare(fuel.getFuelDate(), new Date());
        if (compare == 0 || compare == 1) {
            MyFuel myFuel = ObjectUtil.clone(fuel);
            myFuel.setFuture(true);
            quartzManager.addSimpleTriggerJob(
                    "fuel_now@" + fuel.getId(),
                    "fuel_now@" + fuel.getFuelAirportCode(),
                    "trigger_now@" + fuel.getId(),
                    "trigger_now@" + fuel.getFuelAirportCode(),
                    MyFuelJobService.class,
                    new Date(),
                    5,
                    10,
                    new JobDataMap(BeanMap.create(myFuel)));
        }

        log.debug("设置推送油料：{}，时间：{}", fuel.getId(), DateFormatUtils.format(fuel.getFuelDate(), "yyyy-MM-dd HH:mm:ss"));
        return res;
    }

    @Override
    public List<MyFuel> findNewFuelParam(MyStaff staff) {
        List<MyFuel> oneByNow = fuelMapper.findOneByNow(staff.getLoginUserIn().getStaffAirportCode());
        oneByNow.stream().forEach(o -> {
            o.setOld(true);
        });
        TParam regionFuelType = tParamMapper.selectByAirportCode("regionFuelType", staff.getLoginUserIn().getStaffAirportCode(), 1);
        if (null != regionFuelType && StringUtils.isNotEmpty(regionFuelType.getValue1())) {
            oneByNow = oneByNow.stream().map(o ->
                    {
                        o.setOld(true);
                        if ("1".equals(regionFuelType.getValue1())) {
                            return o;
                        } else {
                            if (2 == o.getFuelPubType()) {
                                return null;
                            }
                            return o;
                        }
                    }
            ).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return oneByNow;
    }

    @Override
    public List<MyFuel> findNewFuelFuture(MyStaff staff) {
        List<MyFuel> fuelList = fuelMapper.findOneByFuture(staff.getLoginUserIn().getStaffAirportCode());
        fuelList.stream().forEach(o -> {
            o.setOld(true);
        });
        TParam regionFuelType = tParamMapper.selectByAirportCode("regionFuelType", staff.getLoginUserIn().getStaffAirportCode(), 1);
        if (null != regionFuelType && StringUtils.isNotEmpty(regionFuelType.getValue1())) {
            fuelList = fuelList.stream().map(o ->
                    {
                        o.setOld(true);
                        if ("1".equals(regionFuelType.getValue1())) {
                            return o;
                        } else {
                            if (2 == o.getFuelPubType()) {
                                return null;
                            }
                            return o;
                        }
                    }
            ).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return fuelList;
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO 初始化时候设置计划任务 还没想好怎么查
//        log.info("初始化时候设置计划任务 还没想好怎么查");
        log.info("初始化设置计划任务");
        List<MyFuel> fuels = fuelMapper.findUnusedFuel(DateUtils.addDays(new Date(), -1));
        fuels.forEach(fuel -> {
            System.out.println("-------" + fuel.getId());
            if (null != fuel.getId()) {
                quartzManager.addSimpleTriggerJob(
                        "fuel@" + fuel.getId(),
                        "fuel@" + fuel.getFuelAirportCode(),
                        "trigger@" + fuel.getId(),
                        "trigger@" + fuel.getFuelAirportCode(),
                        MyFuelJobService.class,
                        fuel.getFuelDate(),
                        5,
                        50,
                        new JobDataMap(BeanMap.create(fuel)));
            }
            log.debug("设置推送油料：{}，时间：{}", fuel.getId(), DateFormatUtils.format(fuel.getFuelDate(), "yyyy-MM-dd HH:mm:ss"));
        });
    }
}
