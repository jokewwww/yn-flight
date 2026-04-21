package com.zh.service.impl;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTask;
import com.zh.bean.login.*;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.MyFuelMapper;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.exception.CustomException;
import com.zh.service.FuelService;
import com.zh.util.SendMsg2Redis;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FuelServiceImpl implements FuelService {

    @Autowired
    private MyFuelMapper myFuelMapper;
    @Autowired
    private StaffMapper StaffMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 油料最新数据
     */
    @Override
    public TFuel findfuel(TStaff tStaff) {
        String airportCode = "";
        String aptareaCode = "";
        if (StringUtils.isBlank(tStaff.getStaffAirportCode())) {
            airportCode = tStaff.getLoginUserIn().getStaffAirportCode();
            aptareaCode = tStaff.getLoginUserIn().getStaffAptareaCode();
        } else {
            airportCode = tStaff.getStaffAirportCode();
            aptareaCode = tStaff.getStaffAptareaCode();
        }
        return myFuelMapper.findfuel(airportCode, aptareaCode);
    }

    /**
     * 获取七天油料数据
     */
    @Override
    public List<MyFuel> findfuelist(MyStaff staff) {
        if ("0".equals(staff.getLoginUserIn().getStaffType())) {
            return myFuelMapper.findFuel(null, null);
        } else {
            return myFuelMapper.findFuel(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
        }
    }

    /**
     * 油料录入
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public String setFuel(MyFuel fuel, MyStaff staff) {
        //根据化验单编号查询是否存在
        MyFuel fuelInfo = myFuelMapper.getFuel(fuel.getFuelTestBillNo());
        if (fuelInfo == null) {
            if (myFuelMapper.setFuel(fuel, staff.getLoginUserIn().getStaffName()) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
            SendMsg2Redis.testDingYue(stringRedisTemplate, staff.getLoginUserIn().getStaffId(), Constant.STAFF, Constant.OIL_ADD, fuel);
            SendMsg2Redis.testDingYue(stringRedisTemplate, null,
                    staff.getLoginUserIn().getStaffAirportCode() + ":" + staff.getLoginUserIn().getStaffAptareaCode(),
                    Constant.OIL_ADD, fuel);
            return "录入成功";
        } else {
            return "已存在";
        }
    }

    /**
     * 新版油料录入
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public String setNewFuel(List<MyFuel> fuelList, MyStaff staff) {
        Optional<Pair<String, String>> any =
                fuelList.stream()
                        .map(myFuel -> Pair.of(myFuel.getFuelTestBillNo(), this.setFuel(myFuel, staff)))
                        .filter(res -> StringUtils.equals("已存在", res.getValue()))
                        .findAny();
        return any.map(pair -> "化验单编号{" + pair.getKey() + "}已存在").orElse("录入成功");
    }

    /**
     * 根据员工id查询油料表最新一条数据(垮库）
     *
     * @param
     * @return
     */
    @Override
    public TFuel findfuels(MyTask task) {
        TStaff tastaffinfo = myFuelMapper.getStaffById(task.getTaskOpeStaffId());
        return myFuelMapper.findfuels(tastaffinfo.getStaffAirportCode());
    }

    /**
     * 修改人员表（垮库）
     *
     * @param staff
     * @return
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updatestaff(MyStaff staff) {
        if (myFuelMapper.updatestaff(staff) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }

    }

    /***
     * 修改区域表 区域代码
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateAiprortSer(MyStaff staff, MyTask task) {
        //修改区域表 区域代码
        if (myFuelMapper.updateAirprtcodeServ(staff.getStaffAptareaCode(), task.getTaskAptareaCode()) == 0) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //修改人员表区域代码
        if (myFuelMapper.updateAirprtcodeStaff(staff.getStaffAptareaCode(), task.getTaskAptareaCode()) == 0) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        //修改车辆表区域代码
        if (myFuelMapper.updateAirprtcodeVehi(staff.getStaffAptareaCode(), task.getTaskAptareaCode()) == 0) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 根据加油员ID 查询加油车编号
     */
    public MyStaffVehi selectstaffvehi(MyStaffVehi staffvehi) {
        return myFuelMapper.selectstaffvehi(staffvehi.getSfvhStaffId());
    }

    /**
     * 逻辑删除
     */
    @Override
    public void updatefuelLogicDelFlg(MyFuel fuel) {
        myFuelMapper.updatefuelLogicDelFlg(fuel.getFuelTestBillNo());
    }


}
