package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.component.DistributedLocker;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.TFuelDistributionMapper;
import com.zh.dao.mapper.my.TFuelNoMapper;
import com.zh.dao.mapper.my.TVehiFuelMapper;
import com.zh.dao.mapper.my.TaskMapper;
import com.zh.exception.CustomException;
import com.zh.service.TFuelNoService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class TFuelNoServiceImpl implements TFuelNoService {

    @Autowired
    private TVehiFuelMapper vehiFuelMapper;

    @Autowired
    private TFuelNoMapper tFuelNoMapper;

    @Autowired
    private TFuelDistributionMapper tFuelDistributionMapper;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    private TVehiFuelMapper tVehiFuelMapper;

    public static void main(String[] args) {

        String s = "2230300589002";
        String substring = s.substring(5, s.length());
        System.out.println("substring->" + substring);
        System.out.println("substring->length" + substring.length());
        Integer integer = Integer.valueOf(substring);
        int length = integer.toString().length();
        System.out.println("length->" + length);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < substring.length() - length; i++) {
            stringBuffer.append("0");
        }
        System.out.println(stringBuffer.toString());
    }

    /**
     * @Description: 生成单号的方法
     * @Param: [num, type]
     * @return: java.lang.String
     * @Author: XiuHongXin   油单类型3国内，2离境，1外航
     * @Date: 2019/9/13
     */
    @Override
    public synchronized String generateNo(MyStaff staff, String start, String end, Integer type) {
        Assert.notNull(start, "start");
        Assert.notNull(end, "end");
        Assert.notNull(type, "start不可为空");
        Long num = 0L;
        Long flrcNoMax = 0L;
        TFuelNo tFuelNo = tFuelNoMapper.selectMaxNO(type, staff.getLoginUserIn().getStaffAptareaCode());
        if (null != tFuelNo && !StringUtils.isEmpty(tFuelNo.getFlrcNo())) {
            flrcNoMax = Long.valueOf(tFuelNo.getFlrcNo());
        }
        if (flrcNoMax > Long.valueOf(start)) {
            return "新申请的油单标号不能小于目前油单的最大值";
        }
        num = Math.abs(Long.valueOf(end) - Long.valueOf(start));
        Long flrcNo = Long.valueOf(start);
        try {
            for (int i = 1; i <= num; i++) {
                flrcNo = flrcNo + i;
                TFuelNo tFuelNo1 = new TFuelNo();
                tFuelNo1.setId(UUID.randomUUID().toString());
                tFuelNo1.setFlrcNo(flrcNo.toString());
                tFuelNo1.setFuelType(type);
                tFuelNo1.setStatus(0);
                tFuelNo1.setRemark(staff.getLoginUserIn().getStaffAptareaCode());
                tFuelNoMapper.insert(tFuelNo1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "共生成" + num + "条";
    }

    @Override
    public synchronized ResponseObject<Object> selectAll(MyStaff staff, InTFuelNo inTFuelNo) {
        //Integer pages =  ((inTFuelNo.getPage()-1)*inTFuelNo.getSize());
        PageHelper.startPage(inTFuelNo.getPage(), inTFuelNo.getSize());
        TFuelNo tFuelNo = new TFuelNo();
        tFuelNo.setFlrcNo(inTFuelNo.getFlrcNo());
        tFuelNo.setFuelType(inTFuelNo.getFuelType());
        tFuelNo.setRemark(staff.getLoginUserIn().getStaffAirportCode());
        List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tFuelNo);
        PageInfo<TFuelNo> pageInfo = new PageInfo<TFuelNo>(tFuelNos);
        return new ResponseObject<Object>("0", "", tFuelNos, pageInfo);
    }


    /**
     * @Description: 获取最新的一条未下发的数据  带备注
     * @Param: [type, aptareaCode]
     * @return: com.zh.bean.flight.TFuelNo
     * @Author: XiuHongXin
     * @Date: 2019/9/14
     */

    /**
     * @Description: 获取最新的一条未下发的数据
     * @Param: [type, aptareaCode]
     * @return: com.zh.bean.flight.TFuelNo
     * @Author: XiuHongXin
     * @Date: 2019/9/14
     */
    @Override
    public synchronized TFuelNo getNewNo(Integer type, String aptareaCode, Integer status) {
        try {
            TFuelNo tFuelNo = null;
            tFuelNo = tFuelNoMapper.selectMaxNOByStatus(type, aptareaCode);
            if (null != tFuelNo) {
                tFuelNo.setFuelType(type);
                tFuelNo.setStatus(status);
                tFuelNoMapper.updateByPrimaryKey(tFuelNo);
            }
            return tFuelNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 获取最新的一条未下发的数据  带备注
     * @Param: [type, aptareaCode]
     * @return: com.zh.bean.flight.TFuelNo
     * @Author: XiuHongXin
     * @Date: 2019/9/14
     */
    @Override
    public TFuelNo getNewNoRemark(String remark, Integer type, String aptareaCode, Integer status) {
        try {
            TFuelNo tFuelNo = null;
            tFuelNo = tFuelNoMapper.selectMaxNOByStatus(type, aptareaCode);
            if (null != tFuelNo) {
                RLock lock = distributedLocker.lock(tFuelNo.getFlrcNo(), 10L);
                boolean res = lock.tryLock(25, 10, TimeUnit.SECONDS);
                if (res) {
                    System.out.println("--------------持有锁-----" + Thread.currentThread().getId());
                    try {
                        // 回收
                        tFuelNo.setFuelType(type);
                        tFuelNo.setStatus(status);
                        tFuelNo.setNote(remark);
                        updateByPrimaryKey(tFuelNo);
                    } finally {
                        System.out.println("--------------解锁-----" + Thread.currentThread().getId());
                        lock.unlock();
                    }
                }
            }
            return tFuelNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<TFuelNo> getNewNoRemarkNew(String remark, Integer type, String aptareaCode, Integer status) {
        try {
            List<TFuelNo> tFuelNo = tFuelNoMapper.selectMaxNOByStatusNew(aptareaCode);
            if (null != tFuelNo && 3 == tFuelNo.size()) {
                updateBatch(tFuelNo);
                return tFuelNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void updateByPrimaryKey(TFuelNo tFuelNo) {
        tFuelNoMapper.updateByPrimaryKey(tFuelNo);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void updateBatch(List<TFuelNo> tFuelNo) {
        tFuelNoMapper.updateBatch(tFuelNo);
    }

    @Override
    public List<TFuelNo> getFuelNoByTypeRemark(String remark, MyStaff staff) {
        System.out.println("开始获取单号->" + remark + JSON.toJSONString(staff));
        if (remark != null) {
            List<TFuelNo> tFuelNos = Lists.newArrayList();
            String[] vehifuels = remark.split(",");
            if (vehifuels.length == 6) {
                List<TVehiFuel> tVehiFuels = tVehiFuelMapper.selectByTaskId(vehifuels[5]);
                if (tVehiFuels.size() == 3) {
                    tVehiFuels.stream().forEach(tVehiFuel -> {
                        TFuelNo tFuelNo = new TFuelNo();
                        tFuelNo.setFuelType(tVehiFuel.getFuelType());
                        tFuelNo.setFlrcNo(tVehiFuel.getFlrcNo());
                        List<TFuelNo> tFuelNoList = tFuelNoMapper.selectByAll(tFuelNo);
                        TFuelNo tempfuelno = tFuelNoList.get(0);
                        if (!staff.getLoginUserIn().getStaffId().equals(tVehiFuel.getStaffId())) {
                            // TODO 任务ID相同 , 但是获取单号的人不同
                            System.out.println("前任务取消,新的任务来获取 , 更新缓存数据 -> " + JSON.toJSONString(remark) + "----" + JSON.toJSONString(staff));
                            String[] tempremarks = remark.split(",");
                            tVehiFuel.setFuelType(tempfuelno.getFuelType());
                            tVehiFuel.setStaffId(tempremarks[1]);
                            tVehiFuel.setVehiNo(tempremarks[3]);
                            tVehiFuel.setFlrcNo(tFuelNo.getFlrcNo());
                            tVehiFuel.setRemark(remark);
                            tVehiFuel.setFuelType(tFuelNo.getFuelType());
                            tVehiFuelMapper.updateByPrimaryKeySelective(tVehiFuel);
                        }
                        tFuelNos.add(tempfuelno);
                    });
                    if (tFuelNos.size() == 3) {
                        return tFuelNos;
                    } else {
                        throw new CustomException(ReturnMsg.getInstanceNGz("单号获取失败"));
                    }
                } else if (tVehiFuels.size() > 0) {
                    throw new CustomException(ReturnMsg.getInstanceNGz("单号归还存在异常,请联系管理员"));
                }
            } else {
                throw new CustomException(ReturnMsg.getInstanceNGz("申请单号备注内容异常"));
            }
            List<TFuelNo> newNoRemarkNew = getNewNoRemarkNew(remark, 1, staff.getLoginUserIn().getStaffAirportCode(), 1);
            if (null != newNoRemarkNew && 3 == newNoRemarkNew.size()) {
                asyncTVehiFuel(newNoRemarkNew, remark, staff);
                return newNoRemarkNew;
            } else {
                throw new CustomException(ReturnMsg.getInstanceNGz("单号不足 请重新申请"));
            }
        }
        return null;
    }

    @Async
    void asyncTVehiFuel(List<TFuelNo> newNoRemarkNew, String remark, MyStaff staff) {
        for (TFuelNo tFuelNo : newNoRemarkNew) {
            TVehiFuel tVehiFuel = new TVehiFuel();
            String[] tempremarks = remark.split(",");
            tVehiFuel.setId(UUID.randomUUID().toString());
            tVehiFuel.setTaskId(tempremarks[5]);
            tVehiFuel.setStaffId(tempremarks[1]);
            tVehiFuel.setVehiNo(tempremarks[3]);
            tVehiFuel.setFlrcNo(tFuelNo.getFlrcNo());
            tVehiFuel.setRemark(remark);
            tVehiFuel.setFuelType(tFuelNo.getFuelType());
            tVehiFuel.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            insertSelective(tVehiFuel);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void insertSelective(TVehiFuel tVehiFuel) {
        tVehiFuelMapper.insertSelective(tVehiFuel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TFuelNo> padGetFuelNoByType(TFuelDistribution request) {
        String staffId = request.getStaffId();
        String staffAirportCode = request.getFlgtAirportCode();
        //TFuelDistribution tFuelDistribution = new TFuelDistribution();
//        tFuelDistribution.setStaffId(staffId);
        //  tFuelDistribution.setPadId(request.getPadId());
//        List<TFuelDistribution> tFuelDistributionList = tFuelDistributionMapper.selectList(tFuelDistribution);
//        if(CollectionUtil.isNotEmpty(tFuelDistributionList)){
//            return tFuelDistributionList.stream().map(dto->{
//                TFuelNo tFuelNo = new TFuelNo();
//                tFuelNo.setFlrcNo(dto.getFlrcNo());
//                tFuelNo.setFuelType(dto.getFuelType());
//                tFuelNo.setStatus(0);
//                tFuelNo.setRemark(dto.getFlgtAirportCode());
//                return tFuelNo;
//            }).collect(Collectors.toList());
//
//        }else{
        // 获取三类油单号
        List<TFuelNo> tFuelNos1 = tFuelNoMapper.selectNOByStatusNew(staffAirportCode, 1);
        List<TFuelNo> tFuelNos2 = tFuelNoMapper.selectNOByStatusNew(staffAirportCode, 2);
        List<TFuelNo> tFuelNos3 = tFuelNoMapper.selectNOByStatusNew(staffAirportCode, 3);
        List<TFuelNo> tFuelNos = Lists.newArrayList();
        tFuelNos.addAll(tFuelNos1);
        tFuelNos.addAll(tFuelNos2);
        tFuelNos.addAll(tFuelNos3);
        // 将油单号标记为使用
        tFuelNoMapper.updateStatus(tFuelNos, 3, 0);
        List<TFuelDistribution> collect = tFuelNos.stream().map(dto -> {
            TFuelDistribution distribution = new TFuelDistribution();
            distribution.setStaffId(staffId);
            distribution.setFlgtAirportCode(staffAirportCode);
            distribution.setFlrcNo(dto.getFlrcNo());
            distribution.setPadId(request.getPadId());
            distribution.setFuelType(dto.getFuelType());
            distribution.setStatus(3);
            distribution.setCreateDate(new Date());
            return distribution;
        }).collect(Collectors.toList());
        tFuelDistributionMapper.insertList(collect);
        return tFuelNos;
//        }
    }

    // PC 修改油单方法
    @Override
    public synchronized String updateFule(String oldNO, Integer type, String aptareaCode) {
        String newNo = null;
        try {
            TFuelNo tFuelNo = new TFuelNo();
            tFuelNo.setFlrcNo(oldNO);
            List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tFuelNo);
            if (tFuelNos.size() > 0) {
                tFuelNos.get(0).setStatus(0);
                tFuelNoMapper.updateByPrimaryKey(tFuelNos.get(0));
            }
            //3国内，2离境，1外航
            TFuelNo newNo1 = getNewNo(compareType(type), aptareaCode, 2);
            if (null != newNo1 && null != newNo1.getFlrcNo()) {
                newNo = newNo1.getFlrcNo();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return newNo;
    }

    // 修改油单编号变成已经使用
    @Override
    public String test() {
        try {
            //final  String source = "22,29,30,33";
           /* final  String source = "22";
            List<TAirportCode> tAirportCodes = myTAirportCodeMapper.selectGroupApcdCnafAirportCode();
            tAirportCodes.stream()
                    .filter(o->null != o && !StringUtils.isEmpty(o.getApcdCnafAirportCode()))
                    .filter(o->source.contains(o.getApcdCnafAirportCode().substring(0,2)))
                    .peek(o->{
                        if(!StringUtils.isEmpty(o.getApcdCnafAirportCode()) && !"2210".equals(o.getApcdCnafAirportCode())){
                            for (int i = 1; i < 4; i++) {
                                ArrayList<TFuelNo> objects = Lists.newArrayList();
                                Integer num = 600000;
                                //下边需要8位
                                for (int j = 1; j < 10001; j++) {
                                    num = num + 1;
                                    String noStr = (o.getApcdCnafAirportCode()+String.valueOf(i) +"00"+ String.valueOf(num));
                                    TFuelNo tFuelNo = new TFuelNo(
                                            UUID.randomUUID().toString(),
                                            noStr ,
                                            i,
                                            0,
                                            o.getApcdCnafAirportCode()
                                    );
                                    System.out.println(noStr);
                                    objects.add(tFuelNo);
                                }
                                tFuelNoMapper.insertList(objects);
                            }
                        }
                    }).forEach(o->{
                System.out.println(o.getApcdCnafAirportCode());
            });*/
            //2210300700000   2210200100000   29013 60001000
            //for (int i = 1; i < 4; i++) {

            /**
             * 2909 300  400000   2w
             * 2909 2000  50000    2w
             * 2909 1000   50000     2w
             * 3028 3 600   00001
             */
            ArrayList<TFuelNo> objects = Lists.newArrayList();
            Integer num = 60000001;
            //下边需要8位
            for (int j = 0; j < 20000; j++) {
                if (j != 0) {
                    num = num + 1;
                }
                String noStr = ("3028" + String.valueOf(3) + String.valueOf(num));
                TFuelNo tFuelNo = new TFuelNo(
                        UUID.randomUUID().toString(),
                        noStr,
                        1,
                        0,
                        "3028"
                );
                System.out.println(noStr);
                objects.add(tFuelNo);
            }
            tFuelNoMapper.insertList(objects);
            // }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    public Integer updatefuleNoEnd(String no) {
        try {
            RLock lock = distributedLocker.lock(no, 10L);
            boolean res = lock.tryLock(25, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    TFuelNo tFuelNo = new TFuelNo();
                    tFuelNo.setFlrcNo(no);
                    List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tFuelNo);
                    if (tFuelNos.size() > 0) {
                        tFuelNos.get(0).setStatus(2);
                        tFuelNoMapper.updateByPrimaryKey(tFuelNos.get(0));
                    }
                } finally {
                    lock.unlock();
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Integer, String> getMaxFuelNo(MyStaff staff) {
        synchronized (TFuelNoService.class) {
            List<TFuelNo> maxFuelNo = tFuelNoMapper.getMaxFuelNo(staff.getLoginUserIn().getStaffAirportCode());
            Map<Integer, String> collect = maxFuelNo.stream().collect(Collectors.toMap(TFuelNo::getFuelType, TFuelNo::getFlrcNo));
            return collect;
        }
    }

    @Override
    @Transactional
    public synchronized ReturnMsg<String> generateFuelNo(MyStaff staff, InTFuelNo tFuelNo) {
        try {
            String prefix = staff.getLoginUserIn().getStaffAirportCode() + tFuelNo.getFuelType();
            String starFlrcNo = tFuelNo.getFlrcNo();
            String endFlrcNoStr = tFuelNo.getEndFlrcNo();
            if (!starFlrcNo.startsWith(prefix)) {
                starFlrcNo = prefix + starFlrcNo;
            }
            if (!endFlrcNoStr.startsWith(prefix)) {
                endFlrcNoStr = prefix + endFlrcNoStr;
            }
            if (starFlrcNo.length() < 13) {
                starFlrcNo = prefix + starFlrcNo;
            }
            if (endFlrcNoStr.length() < 13) {
                endFlrcNoStr = prefix + endFlrcNoStr;
            }
            String top = starFlrcNo.substring(0, 5);
            String starSub = starFlrcNo.substring(5, starFlrcNo.length());
            String endSub = endFlrcNoStr.substring(5, endFlrcNoStr.length());
            Long publicFlrcNo = Long.valueOf(starSub);
            Long endFlrcNo = Long.valueOf(endSub);
            int str = publicFlrcNo.toString().length();
            int end = endFlrcNo.toString().length();

            Boolean lock = false;
            StringBuffer stringBuffer = new StringBuffer();
            if (str == end) {
                lock = true;
                for (int i = 0; i < starSub.length() - str; i++) {
                    stringBuffer.append("0");
                }
            }
            Long poor = endFlrcNo - publicFlrcNo;
            List<TFuelNo> objects = Lists.newArrayList();
            for (int i = 1; i <= poor; i++) {
                Long flrcNo = publicFlrcNo;
                flrcNo = flrcNo + i;
                StringBuffer newNO = new StringBuffer();
                if (lock) {
                    newNO.append(top);
                    newNO.append(stringBuffer.toString());
                    newNO.append(String.valueOf(flrcNo));
                } else {
                    StringBuffer newStrBuf = new StringBuffer();
                    for (int i1 = 0; i1 < starSub.length() - flrcNo.toString().length(); i1++) {
                        newStrBuf.append("0");
                    }
                    newNO.append(top);
                    newNO.append(newStrBuf.toString());
                    newNO.append(String.valueOf(flrcNo));
                }
                TFuelNo tFuelNo1 = new TFuelNo(
                        UUID.randomUUID().toString(),
                        newNO.toString(),
                        tFuelNo.getFuelType(),
                        0,
                        staff.getLoginUserIn().getStaffAirportCode()
                );
                objects.add(tFuelNo1);
            }
            // 过滤重复数据
            objects = objects.stream().filter(object -> tFuelNoMapper.selectByFlrcNo(object.getFlrcNo()) == null).collect(Collectors.toList());
            tFuelNoMapper.insertList(objects);
            return new ReturnMsg<String>(Constant.CODE_OK, null, endFlrcNo.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new CustomException(ReturnMsg.getInstanceNGz("生成油单号失败", null));
        }
    }

    @Override
    @Transactional
    public synchronized ReturnMsg<String> recyclingNo(MyTask task) {
//        String oldFlrcNo = task.getOldFlrcNo();
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(oldFlrcNo)) {
//            Integer msg = updateFuleStatus(oldFlrcNo);
//            if (msg == 0) {
//                throw new CustomException(ReturnMsg.getInstanceNGz("还原油单编号失败", null));
//            }
//        }
        List<TVehiFuel> tVehiFuels = tVehiFuelMapper.selectByTaskId(task.getTaskId());
        System.out.println("归还油单任务id" + task.getTaskId());
        if (tVehiFuels.size() == 3) {
            tVehiFuels.stream().forEach(tVehiFuel -> {
                TFuelNo tempfulNo = new TFuelNo();
                tempfulNo.setFlrcNo(tVehiFuel.getFlrcNo());
                tempfulNo.setFuelType(tVehiFuel.getFuelType());
                //获取油单号表数据
                List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tempfulNo);
                if (tFuelNos.size() > 0) {
                    TFuelNo tFuelNo = tFuelNos.get(0);
                    //油单号变更0
                    tFuelNo.setStatus(0);
                    System.out.println("油单状态重置为0 油单号:" + tFuelNo.getFlrcNo());
                    tFuelNoMapper.updateByFuelNo(tFuelNo);
                }
                //删除对应缓存TVehiFuel数据
                tVehiFuelMapper.deleteByPrimaryKey(tVehiFuel.getId());
            });
        } else {
            System.out.println("recyclingNo 主动归还单号 单号已经被归还 taskId->" + task.getTaskId());
            // throw new CustomException(ReturnMsg.getInstanceNGz("获取油单号缓存表失败"));
        }
        return new ReturnMsg<String>(Constant.CODE_OK, null, "success");
    }

    /*
        修改油单用
     */
    @Override
    @Transactional
    public synchronized Integer recyclingFlueNo(MyFuelRecpt fuelRecpt) {
        List<TVehiFuel> tVehiFuels = vehiFuelMapper.selectByTaskId(fuelRecpt.getTaskId());
        if (tVehiFuels.size() == 3) {
            tVehiFuels.stream().forEach(tVehiFuel -> {
                TFuelNo tempfulNo = new TFuelNo();
                tempfulNo.setFlrcNo(tVehiFuel.getFlrcNo());
                tempfulNo.setFuelType(tVehiFuel.getFuelType());
                //获取油单号表数据
                List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tempfulNo);
                if (tFuelNos.size() > 0) {
                    TFuelNo tFuelNo = tFuelNos.get(0);
                    if (tFuelNo.getFlrcNo().equals(fuelRecpt.getFlrcNo())) {
                        System.out.println("修改油单原任务油单状态重置为2 油单号:" + tFuelNo.getFlrcNo());
                        tFuelNo.setStatus(2);
                    } else {
                        //原任务油单号变更0
                        tFuelNo.setStatus(0);
                        System.out.println("修改油单原任务油单状态重置为0 油单号:" + tFuelNo.getFlrcNo());
                    }
                    tFuelNoMapper.updateByFuelNo(tFuelNo);
                }
                //删除对应缓存TVehiFuel数据
                vehiFuelMapper.deleteByPrimaryKey(tVehiFuel.getId());
            });
        } else {
            TFuelNo tempfulNo = new TFuelNo();
            tempfulNo.setFlrcNo(fuelRecpt.getFlrcNo());
            List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tempfulNo);
            if (tFuelNos.size() > 0) {
                TFuelNo tFuelNo = tFuelNos.get(0);
                System.out.println("获取油单号缓存表失败->修改油单原任务油单状态重置为2 油单号:" + tFuelNo.getFlrcNo());
                tFuelNo.setStatus(2);
                tFuelNoMapper.updateByFuelNo(tFuelNo);
            }
            //throw new CustomException(ReturnMsg.getInstanceNGz("获取油单号缓存表失败"));
        }
        return null;
    }

    /*
        新增油单用
     */
   /*
        新增油单用
     */
    @Override
    public Integer recyclingFlueNoInsert(MyFuelRecpt fuelRecpt, MyStaff staff) {
        //更改油单号状态
        //获取缓存表TVehiFuel数据
        List<TVehiFuel> tVehiFuels = vehiFuelMapper.selectByTaskId(fuelRecpt.getTaskId());
        if (tVehiFuels.size() == 3) {
            String taskId = tVehiFuels.get(0).getTaskId();
            String staffId = tVehiFuels.get(0).getStaffId();
            if (!fuelRecpt.getTaskId().equals(taskId) || !staff.getLoginUserIn().getStaffId().equals(staffId)) {
                //TODO 单号人员不匹配 说明上个任务已经被取消了
                throw new CustomException(ReturnMsg.getInstanceNGz("油单号与人员不匹配,会导致单号重复"));
            }
            tVehiFuels.stream().forEach(tVehiFuel -> {
                try {
                    // 回收
                    TFuelNo tempfulNo = new TFuelNo();
                    tempfulNo.setFlrcNo(tVehiFuel.getFlrcNo());
                    tempfulNo.setFuelType(tVehiFuel.getFuelType());
                    //获取油单号表数据
                    List<TFuelNo> tFuelNos = tFuelNoMapper.selectByAll(tempfulNo);
                    RLock lock = distributedLocker.lock(tVehiFuel.getFlrcNo(), 10L);
                    boolean res = lock.tryLock(25, 10, TimeUnit.SECONDS);
                    if (res) {
                        System.out.println("--------------持有锁-----" + Thread.currentThread().getId());
                        try {
                            if (tFuelNos.size() > 0) {
                                TFuelNo tFuelNo = tFuelNos.get(0);
                                //油单号与缓存相同状态变更2,否则变更0
                                if (tFuelNo.getFlrcNo().equals(fuelRecpt.getFlrcNo())) {
                                    tFuelNo.setStatus(2);
                                    System.out.println("油单状态置为2 油单号:" + tFuelNo.getFlrcNo());
                                } else {
                                    tFuelNo.setStatus(0);
                                    System.out.println("油单状态重置为0 油单号:" + tFuelNo.getFlrcNo());
                                }
                                tFuelNoMapper.updateByFuelNo(tFuelNo);
                            }
                            //删除对应缓存TVehiFuel数据
                            vehiFuelMapper.deleteByPrimaryKey(tVehiFuel.getId());
                        } finally {
                            System.out.println("--------------解锁-----" + Thread.currentThread().getId());
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
            return null;
            //TODO
            //throw new CustomException(ReturnMsg.getInstanceNGz("获取油单号缓存表失败"));
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> padGetNo(TFuelNo tFuelNo, MyStaff staff) {
        Objects.requireNonNull(tFuelNo.getRemark(), "remark不可为空");
        String imei = tFuelNo.getRemark();

        return null;
    }

    @Override
    public Integer updateFuleStatus(String nos) {
        Assert.notNull(nos, "油单编号不可为空");
        try {
            String[] split = nos.split(",");
            Arrays.stream(split).forEach(no -> {
                try {
                    RLock lock = distributedLocker.lock(no, 10L);
                    boolean res = lock.tryLock(25, 10, TimeUnit.SECONDS);
                    if (res) {
                        try {
                            TFuelNo tFuelNo = new TFuelNo();
                            tFuelNo.setFlrcNo(no);
                            tFuelNo.setStatus(0);
                            tFuelNo.setNote(null);
                            if (1 != tFuelNoMapper.updateByFuelNo(tFuelNo)) {
                                throw new CustomException(ReturnMsg.getInstanceNGz("还原油单编号失败", null));
                            }
                        } finally {
                            System.out.println("--------------解锁-----" + Thread.currentThread().getId());
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // @Override
    public List<TFuelNo> getFuelNoByTypeRemark1(String remark, MyStaff staff) {
        try {
            TFuelNo newNo1 = getNewNoRemark(remark, 1, staff.getLoginUserIn().getStaffAirportCode(), 1);
            TFuelNo newNo2 = getNewNoRemark(remark, 2, staff.getLoginUserIn().getStaffAirportCode(), 1);
            TFuelNo newNo3 = getNewNoRemark(remark, 3, staff.getLoginUserIn().getStaffAirportCode(), 1);


            List<TFuelNo> tFuelNos = Lists.newArrayList();
            tFuelNos.add(newNo1);
            tFuelNos.add(newNo2);
            tFuelNos.add(newNo3);
            return tFuelNos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public synchronized List<TFuelNo> getFuelNoByType(MyStaff staff) {
        try {
            TFuelNo newNo1 = getNewNo(1, staff.getLoginUserIn().getStaffAirportCode(), 1);
            TFuelNo newNo2 = getNewNo(2, staff.getLoginUserIn().getStaffAirportCode(), 1);
            TFuelNo newNo3 = getNewNo(3, staff.getLoginUserIn().getStaffAirportCode(), 1);
            List<TFuelNo> tFuelNos = Lists.newArrayList();
            tFuelNos.add(newNo1);
            tFuelNos.add(newNo2);
            tFuelNos.add(newNo3);
            return tFuelNos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油
    // 7:  内航国内补加油 8: 内航 离境 补加油 9 : 外航 补油）
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
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
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

}
