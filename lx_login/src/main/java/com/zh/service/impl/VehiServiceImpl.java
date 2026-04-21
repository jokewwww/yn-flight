package com.zh.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TFlight;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyVehi;
import com.zh.bean.login.TStaff;
import com.zh.bean.login.TVehiInfo;
import com.zh.component.RedisComponent;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.MyStaffVehiTaskMapper;
import com.zh.dao.mapper.my.VehiMapper;
import com.zh.exception.CustomException;
import com.zh.service.VehiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VehiServiceImpl implements VehiService {

    @Autowired
    private VehiMapper vehiMapper;

    @Autowired
    private MyStaffVehiTaskMapper vehitaskMapper;


    @Autowired
    private RedisComponent redis;

    /**
     * 获取车辆数量（总数，在线，保障中）
     */
    @Override
    public Map<String, Object> getVehiSum(TStaff staff) {
        //创建一个map集合用来装查出来的数量
        Map<String, Object> flightAmountMap = new HashMap<String, Object>();
        //以（所属机场，所属区域）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_总数
        Integer vehiSum = vehiMapper.getVehiSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以（所属机场，所属区域，是否可用＝'1：空闲'）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_在线
        Integer noLineSum = vehiMapper.getNoLineSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //以（所属机场，所属区域，是否可用＝'2：使用中'）的条件，统计车辆表（DB）中的所有记录数，作为画面中的车辆_保障中
        Integer AafeguardSum = vehiMapper.getAafeguardSum(staff.getStaffAirportCode(), staff.getStaffAptareaCode());
        //车辆_总数
        flightAmountMap.put("vehiSum", vehiSum);
        //车辆_在线
        flightAmountMap.put("noLineSum", noLineSum);
        //车辆_保障中
        Object car_security = redis.get("car_security");
        if (null != car_security) {
            AafeguardSum = Integer.valueOf(String.valueOf(car_security)) + 1;
        } else {
            AafeguardSum = 0;
        }
        flightAmountMap.put("AafeguardSum", AafeguardSum);
        return flightAmountMap;
    }

    /**
     * 车辆信息接口
     * 以（所属机场代码＝输入_机场ID）的条件，取得车辆表（DB）中对象记录的如下字段，写入输出接口
     */
    @Override
    public List<TVehiInfo> getVehiList(TFlight flight) {
        //以（所属机场代码＝输入_机场ID）的条件，取得车辆表（DB）中对象记录的如下字段，写入输出接口
        List<TVehiInfo> vehiList = vehiMapper.getVehiList(flight.getFlgtAirportCode());
        for (TVehiInfo tVehi : vehiList) {
            //遍历vehiList集合中取得的所有记录，以（加油车编号＝vehiList集合中车辆编号）的条件，取得人员车辆表（DB）中加油员ID字段
            List<TStaff> staffList = vehiMapper.getStaffVehiList(tVehi.getCarID());
            for (TStaff staff : staffList) {
                //遍历staffList集合中取得的所有记录，以（员工ID＝staffList集合中加油员ID）的条件，取得员工表（DB）中如下字段，写入输出接口
                TStaff staffInfo = vehiMapper.getStaffName(staff.getStaffId());
                tVehi.setfMan(staffInfo.getStaffName());
                tVehi.setfPhone(staffInfo.getStaffPhone());
                tVehi.setDriver(staffInfo.getStaffName());
                tVehi.setDriverPhone(staffInfo.getStaffPhone());
            }
        }
        return vehiList;
    }

    /**
     * 手动添加车辆信息接口
     */
    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public Integer addVehiInfo(MyVehi vehi, MyStaff staff) {
        if (vehi.getVehiAirportCode() == null || "".equals(vehi.getVehiAirportCode())) {
            vehi.setVehiAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        }
        if (vehi.getVehiAptareaCode() == null || "".equals(vehi.getVehiAptareaCode())) {
            vehi.setVehiAptareaCode(staff.getLoginUserIn().getStaffAptareaCode());
        }
        //生成UUID为车辆ID
        vehi.setVehiId(UUID.randomUUID().toString());
        //判断如果车高等于空的话就赋默认值
        if (vehi.getVehiHeight() == null) {
            vehi.setVehiHeight(2.0);
        }
        //查询最大车辆油单自增代码
        MyVehi vehie = vehiMapper.selectoilcode();

        if (vehie != null) {
            //如果最大值小于99
            if (!StringUtils.isEmpty(vehie.getVehiFuelCode()) && Integer.valueOf(vehie.getVehiFuelCode()) < 99) {
                if ("0".equals(vehie.getVehiFuelCode().substring(0, 1)) && !"9".equals(vehie.getVehiFuelCode().substring(1))) {
                    //最大值+1
                    Integer codes = Integer.valueOf(vehie.getVehiFuelCode()) + 1;
                    vehi.setVehiFuelCode("0" + codes.toString());
                } else {
                    Integer codes = Integer.valueOf(vehie.getVehiFuelCode()) + 1;
                    vehi.setVehiFuelCode(codes.toString());
                }
            }
            //如果等于99
            if (!StringUtils.isEmpty(vehie.getVehiFuelCode()) && Integer.valueOf(vehie.getVehiFuelCode()) == 99) {
                vehi.setVehiFuelCode("01");
            }
        } else {
            vehi.setVehiFuelCode("01");
        }
        //根据车辆编号查询车辆信息
        List<MyVehi> vehiList = vehitaskMapper.getVehiInfoList(vehi.getVehiNo(), vehi.getVehiAirportCode());
        //根据车牌号查询车辆信息
        MyVehi vehiInfos = vehitaskMapper.getVehiInfos(vehi.getVehiPlateNo());
        //为空的话说明不存在此车才新增
        if (CollUtil.isEmpty(vehiList)) {
            if (vehiInfos == null) {
                //添加车辆信息,判断如果返回等于1说明添加成功，否则添加失败
                if (vehiMapper.addVehiInfo(vehi) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                }
                //成功
                return 1;
            } else {
                //车牌号已存在
                return 2;
            }
            //否者的话说明存在此车就不新增
        } else {
            //车辆编号已存在
            return 0;
        }
    }

    /**
     * 根据机场代码和车辆号获取车辆油单编号
     */
    @Override
    public Map<String, Object> getFuelRecptNo(MyVehi vehi) {
        Map<String, Object> map = new HashMap<String, Object>();
        String fuelRecptNo = vehiMapper.getFuelRecptNo(vehi.getVehiAirportCode(), vehi.getVehiPlateNo());
        if (fuelRecptNo == null) {
            map.put("vehiFuelCode", "");
        } else {
            map.put("vehiFuelCode", fuelRecptNo);
        }
        return map;
    }

    /**
     * 根据机场代码和车辆号获取车辆油单编号
     */
    @Override
    public Map<String, Object> getFuelRecptNoAndFuelSno(MyVehi vehi) {
        Map<String, Object> map = new HashMap<String, Object>();
        MyVehi fuelRecptNo = vehiMapper.getFuelRecptNoAndFuelSno(vehi.getVehiAirportCode(), vehi.getVehiPlateNo());
        if (fuelRecptNo == null) {
            map.put("vehiFuelCode", "");
            map.put("vehiFuelSno", "");
            map.put("vehiNo", "");
        } else {
            map.put("vehiFuelCode", fuelRecptNo.getVehiFuelCode());
            map.put("vehiFuelSno", fuelRecptNo.getVehiFuelSno());
            map.put("vehiNo", fuelRecptNo.getVehiNo());

        }

        return map;
    }

    /**
     * 更新车辆油单序列号
     *
     * @param vehi
     * @return 1;//成功 -1;//查无此车 2;//更新的序号小于数据库已有序号无需更新 -2;//更新数据库失败
     */
    @Override
    public Integer updateVehiFuelSno(MyVehi vehi) {
        MyVehi fuelRecptNo = vehiMapper.getFuelRecptNoAndFuelSno(vehi.getVehiAirportCode(), vehi.getVehiPlateNo());
        if (fuelRecptNo == null || fuelRecptNo.getVehiFuelSno().isEmpty()) {
            return -1;//查无此车
        }
        if (Integer.valueOf(fuelRecptNo.getVehiFuelSno()) > Integer.valueOf(vehi.getVehiFuelSno())) {
            return 2;//更新的序号小于数据库已有序号无需更新
        }
        if (vehiMapper.updateVehiSno(vehi.getVehiFuelSno(), vehi.getVehiAirportCode(), vehi.getVehiPlateNo()) != 1)
            return -2;//更新数据库失败
        return 1;//成功
    }
}
