package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirportCode;
import com.zh.bean.flight.MyCustom;
import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.flight.MyFuelRecptVo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.AirportCodeMapper;
import com.zh.dao.mapper.my.FlightMapper;
import com.zh.dao.mapper.my.StatisticalMapper;
import com.zh.exception.CustomException;
import com.zh.service.FuelRecptService;
import com.zh.service.StatisticalService;
import com.zh.util.DateUtil;
import com.zh.vo.FuelTotalExcelVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    private StatisticalMapper statisticalMapper;

    @Autowired
    private FuelRecptService fuelRecptService;

    @Autowired
    private AirportCodeMapper airportCodeMapper;

    @Autowired
    private FlightMapper flightMapper;

    public static void main(String[] args) {
    }

    /**
     * 根据日期查询各航空公司合计
     */
    @Override
    public List<Map<String, Object>> getOilCustomByAir(String taskDate, MyStaff staff) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecptVo> list = statisticalMapper.getOilCustomByAir(startTime, endTime, staff.getStaffAirportCode());
        list.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
            taskAmountMap.put("flrcDate", DateUtil.date2str(fuelRecpt.getFlrcDate(), ff));
            //航空公司名称
            taskAmountMap.put("flrcAirlName", fuelRecpt.getFlrcAirlName());
            //机场三字码
            taskAmountMap.put("apcdIataCode", fuelRecpt.getApcdIataCode());
            //航班号
            taskAmountMap.put("flrcFlightNo", fuelRecpt.getFlrcFlightNo());
            //飞机号码
            taskAmountMap.put("flrcAircrftNo", fuelRecpt.getFlrcAircrftNo());
            //飞机类型
            taskAmountMap.put("flrcAircrftType", fuelRecpt.getFlrcAircrftType());
            //体积
            taskAmountMap.put("fuelVol", fuelRecpt.getFlrcFuelVol());
            //质量
            taskAmountMap.put("quantity", fuelRecpt.getFlrcQuantity());
            //质量
            taskAmountMap.put("flrcBwtar", StatusConstant.FlrcBwtarEnum.getValueByType(fuelRecpt.getFlrcBwtar()));
            //油单号
            taskAmountMap.put("flrcNo", fuelRecpt.getFlrcNo());
            //航线
            //taskAmountMap.put("flightValic", fuelRecpt.getFlightValic());

            taskAmountMap.put("flgtVialc", fuelRecpt.getFlightValic());
            //加油时间
            SimpleDateFormat ffhh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //客户编号
            taskAmountMap.put("arcrCustomNum", fuelRecpt.getArcrCustomNum());
            taskAmountMap.put("flrcStatTime", DateUtil.date2str(fuelRecpt.getFlrcStatTime(), ffhh));
            res.add(taskAmountMap);
        });
        return res;
    }

    /**
     * 根据日期查询各航空公统计
     */
    @Override
    public List<Map<String, Object>> getCountByAir(String taskDate, MyStaff staff) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecpt> fuelRecpts = statisticalMapper.getCountByAir(startTime, endTime, staff.getStaffAirportCode());
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
            taskAmountMap.put("flrcDate", DateUtil.date2str(fuelRecpt.getFlrcDate(), ff));
            //航空公司名称
            taskAmountMap.put("airlName", fuelRecpt.getFlrcAirlName());
            //体积
            taskAmountMap.put("fuelVol", fuelRecpt.getFlrcFuelVol());
            //质量
            taskAmountMap.put("quantity", fuelRecpt.getFlrcQuantity());
            //数量
            taskAmountMap.put("num", fuelRecpt.getFlrcNo());
            //客户编号
            taskAmountMap.put("arcrCustomNum", StringUtils.isBlank(fuelRecpt.getArcrCustomNum()) ? "" : (Integer.parseInt(fuelRecpt.getArcrCustomNum()) + ""));

            res.add(taskAmountMap);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> distinguishStatisticalTax(String taskDate, MyStaff staff) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecpt> fuelRecpts = statisticalMapper.distinguishStatisticalTax(startTime, endTime, staff.getStaffAirportCode());
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
            taskAmountMap.put("flrcDate", DateUtil.date2str(fuelRecpt.getFlrcDate(), ff));
            //航空公司名称
            taskAmountMap.put("airlName", fuelRecpt.getFlrcAirlName());
            //体积
            taskAmountMap.put("fuelVol", fuelRecpt.getFlrcFuelVol());
            //质量
            taskAmountMap.put("quantity", fuelRecpt.getFlrcQuantity());
            //数量
            taskAmountMap.put("num", fuelRecpt.getFlrcNo());
            //保税类型
            taskAmountMap.put("flrcBwtar", fuelRecpt.getFlrcBwtar());
            //类型
            taskAmountMap.put("flrcType", fuelRecpt.getFlrcType());
            //客户编号
            taskAmountMap.put("arcrCustomNum", StringUtils.isBlank(fuelRecpt.getArcrCustomNum()) ? "" : (Integer.parseInt(fuelRecpt.getArcrCustomNum()) + ""));
            res.add(taskAmountMap);
        });
        return res;
    }

    /**
     * 根据日期查询各航空公司-带保税类型 统计
     */
    @Override
    public List<Map<String, Object>> getCountByAirAndBaoShui(String taskDate, MyStaff staff) {
        String[] times = checkDateStr(taskDate);
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecpt> fuelRecpts = statisticalMapper.getCountByAirAndBaoShui(startTime, endTime, staff.getStaffAirportCode());
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
            taskAmountMap.put("flrcDate", DateUtil.date2str(fuelRecpt.getFlrcDate(), ff));
            //航空公司名称
            taskAmountMap.put("airlName", fuelRecpt.getFlrcAirlName());
            //体积
            taskAmountMap.put("fuelVol", fuelRecpt.getFlrcFuelVol());
            //质量
            taskAmountMap.put("quantity", fuelRecpt.getFlrcQuantity());
            //数量
            taskAmountMap.put("num", fuelRecpt.getFlrcNo());
            //保税类型
//            taskAmountMap.put("flrcBwtar", fuelRecpt.getFlrcBwtar());
            taskAmountMap.put("flrcBwtar", StringUtils.equals("B", fuelRecpt.getFlrcBwtar()) ? "1" : "");
            //客户编号
            taskAmountMap.put("arcrCustomNum", StringUtils.isBlank(fuelRecpt.getArcrCustomNum()) ? "" : (Integer.parseInt(fuelRecpt.getArcrCustomNum()) + ""));
            res.add(taskAmountMap);
        });
        return res;
    }

    /**
     * 日期查询油单
     */
    @Override
    public List<Map<String, Object>> getFuelrecpt(MyStaff staff, MyFuelRecpt myFuelRecpt) {
        List<MyFuelRecpt> fuelRecpts = fuelRecptService.findfuelrecpt(staff, myFuelRecpt);
        List<MyAirportCode> myAirportCodes = airportCodeMapper.selectAirportCode();
        List<MyCustom> customs = flightMapper.getCUSTOM();
        List<Map<String, Object>> res = Lists.newArrayList();
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //油单号
            taskAmountMap.put("flrcNo", fuelRecpt.getFlrcNo());
            //加油机场三码
//            taskAmountMap.put("flrcAirportCode", fuelRecpt.getFlrcAirportCode());
            myAirportCodes.stream().filter(airCode ->
                            ObjectUtil.equal(airCode.getApcdAirportName(), fuelRecpt.getFlrcAirport()))
                    .findFirst()
                    .ifPresent(airCode -> taskAmountMap.put("flrcAirport3c", airCode.getApcdIataCode()));
            //加油机场名称
            taskAmountMap.put("flrcAirport", fuelRecpt.getFlrcAirport());
            //油单日期
            taskAmountMap.put("flrcDate", fuelRecpt.getFlrcDate());
            //版本号
            taskAmountMap.put("flrcVersion", fuelRecpt.getFlrcVersion());
            //油单类型 1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油
            Integer flrcType = fuelRecpt.getFlrcType();
            String flrcTypeName = "";
            switch (flrcType) {
                case 1:
                    flrcTypeName = "外航加油";
                    break; //可选
                case 2:
                    flrcTypeName = "内航离境加油";
                    break; //可选
                case 3:
                    flrcTypeName = "内航国内加油";
                    break; //可选
                case 4:
                    flrcTypeName = "外航抽油";
                    break; //可选
                case 5:
                    flrcTypeName = "内航离境抽油";
                    break; //可选
                case 6:
                    flrcTypeName = "内航国内抽油";
                    break; //可选
            }
            taskAmountMap.put("flrcType", flrcTypeName);
            //保税类型 保税 B  非保税 FB
            taskAmountMap.put("flrcBwtar", fuelRecpt.getFlrcBwtar());
            //服务模式 供油类型 0 加注 1 自提 2 配送
            Integer flrcSupplyFuelType = fuelRecpt.getFlrcSupplyFuelType();
            String flrcSupplyFuelTypeName = "";
            if (ObjectUtil.isNotNull(flrcSupplyFuelType)) {
                switch (flrcSupplyFuelType) {
                    case 0:
                        flrcSupplyFuelTypeName = "加注";
                        break; //可选
                    case 1:
                        flrcSupplyFuelTypeName = "自提";
                        break; //可选
                    case 2:
                        flrcSupplyFuelTypeName = "配送";
                        break; //可选
                }
            }
            taskAmountMap.put("flrcSupplyFuelType", flrcSupplyFuelTypeName);
            //客户代码
            taskAmountMap.put("arcrCustomNum", fuelRecpt.getArcrCustomNum());
            //客户名称
            customs.stream().filter(custom -> ObjectUtil.equal(custom.getCstmNum(), fuelRecpt.getArcrCustomNum()))
                    .findFirst()
                    .ifPresent(custom -> taskAmountMap.put("arcrCustomName", custom.getCstmName()));
            //航班号
            taskAmountMap.put("flrcFlightNo", fuelRecpt.getFlrcFlightNo());
            //机号
            taskAmountMap.put("flrcAircrftNo", fuelRecpt.getFlrcAircrftNo());
            //机型
            taskAmountMap.put("flrcAircrftType", fuelRecpt.getFlrcAircrftType());
            // 始发站三码
            myAirportCodes.stream().filter(airCode ->
                            ObjectUtil.equal(airCode.getApcdAirportName(), fuelRecpt.getFlrcDeparture()))
                    .findFirst()
                    .ifPresent(airCode -> taskAmountMap.put("flrcDeparture3c", airCode.getApcdIataCode()));
            //始发站
            taskAmountMap.put("flrcDeparture", fuelRecpt.getFlrcDeparture());
            //经停站三码
            myAirportCodes.stream().filter(airCode ->
                            ObjectUtil.equal(airCode.getApcdAirportName(), fuelRecpt.getFlrcTransit()))
                    .findFirst()
                    .ifPresent(airCode -> taskAmountMap.put("flrcTransit3c", airCode.getApcdIataCode()));
            //经停站
            taskAmountMap.put("flrcTransit", fuelRecpt.getFlrcTransit());
            //目的站三码
            myAirportCodes.stream().filter(airCode ->
                            ObjectUtil.equal(airCode.getApcdAirportName(), fuelRecpt.getFlrcDest()))
                    .findFirst()
                    .ifPresent(airCode -> taskAmountMap.put("flrcDest3c", airCode.getApcdIataCode()));
            //目的站名称
            taskAmountMap.put("flrcDest", fuelRecpt.getFlrcDest());
            //油品类型
            taskAmountMap.put("flrcFuelName", fuelRecpt.getFlrcFuelName());
            //化验单号
            taskAmountMap.put("flrcTestBillNo", fuelRecpt.getFlrcTestBillNo());
            //温度
            taskAmountMap.put("flrcFuelTemp", fuelRecpt.getFlrcFuelTemp());
            //密度
            taskAmountMap.put("flrcFuelDnst", fuelRecpt.getFlrcFuelDnst());
            //加油升数
            taskAmountMap.put("flrcFiguars", fuelRecpt.getFlrcFiguars());
            //加油重量
            taskAmountMap.put("flrcQuantity", fuelRecpt.getFlrcQuantity());
            //加油开始时间
            taskAmountMap.put("flrcStatTime", ObjectUtil.isNotNull(fuelRecpt.getFlrcStatTime()) ? cn.hutool.core.date.DateUtil.formatDateTime(fuelRecpt.getFlrcStatTime()) : null);
            //加油结束时间
            taskAmountMap.put("flrcFnshTime", ObjectUtil.isNotNull(fuelRecpt.getFlrcFnshTime()) ? cn.hutool.core.date.DateUtil.formatDateTime(fuelRecpt.getFlrcFnshTime()) : null);
            //加油车号
            taskAmountMap.put("flrcVehiNo", fuelRecpt.getFlrcVehiNo());
            //地井
            taskAmountMap.put("flrcHydrtPitNo", fuelRecpt.getFlrcHydrtPitNo());
            //加油员
            taskAmountMap.put("flrcDeliverName", fuelRecpt.getFlrcDeliverName());
            //运油车号
            taskAmountMap.put("flrcTankerNo", fuelRecpt.getFlrcTankerNo());
            //运输里程
            taskAmountMap.put("flrcTransportMileage", fuelRecpt.getFlrcTransportMileage());
            //收油机场三码
            taskAmountMap.put("flrcRapc3", fuelRecpt.getFlrcRapc3());
            //收油机场
            taskAmountMap.put("flrcRapcn", fuelRecpt.getFlrcRapcn());
            //代结算机场三码
            taskAmountMap.put("settlementRapc3", fuelRecpt.getSettlementRapc3());
            //代结算机场名称
            taskAmountMap.put("settlementRapcn", fuelRecpt.getSettlementRapcn());
            //工厂代码
            //销售办公室
            //销售组
            //抽油原因
            taskAmountMap.put("flrcDefuelReason", fuelRecpt.getFlrcDefuelReason());
            //备注信息
            taskAmountMap.put("flrcRemark", fuelRecpt.getFlrcRemark());
            //发送状态 0未上传 1上传失败 2 上传成功 3新增 4修改
            Integer flrcStatus = fuelRecpt.getFlrcStatus();
            String flrcStatusName = "";
            if (ObjectUtil.isNotNull(flrcStatus)) {
                switch (flrcStatus) {
                    case 0:
                        flrcStatusName = "未上传";
                        break; //可选
                    case 1:
                        flrcStatusName = "上传失败";
                        break; //可选
                    case 2:
                        flrcStatusName = "上传成功";
                        break; //可选
                    case 3:
                        flrcStatusName = "新增";
                        break; //可选
                    case 4:
                        flrcStatusName = "修改";
                        break; //可选
                }
            }
            taskAmountMap.put("flrcStatus", flrcStatusName);
            //标准化状态
            //确认状态 '0 未成功 1 成功 2 失败',
            Integer flrcConfirmStatus = fuelRecpt.getFlrcConfirmStatus();
            String flrcConfirmStatusName = "";
            if (ObjectUtil.isNotNull(flrcConfirmStatus)) {
                switch (flrcConfirmStatus) {
                    case 0:
                        flrcConfirmStatusName = "未成功";
                        break; //可选
                    case 1:
                        flrcConfirmStatusName = "成功";
                        break; //可选
                    case 2:
                        flrcConfirmStatusName = "失败";
                        break; //可选
                }
            }
            taskAmountMap.put("flrcConfirmStatus", flrcConfirmStatusName);
            //结算状态 0未支付 1 成功 2 失败
            Integer flrcPayStatus = fuelRecpt.getFlrcPayStatus();
            String flrcPayStatusName = "";
            if (ObjectUtil.isNotNull(flrcPayStatus)) {
                switch (flrcPayStatus) {
                    case 0:
                        flrcPayStatusName = "未成功";
                        break; //可选
                    case 1:
                        flrcPayStatusName = "成功";
                        break; //可选
                    case 2:
                        flrcPayStatusName = "失败";
                        break; //可选
                }
            }
            taskAmountMap.put("flrcPayStatus", flrcPayStatusName);
            //发送ERP状态
            //平台状态描述

            res.add(taskAmountMap);
        });
        return res;
    }

    /**
     * 日期查询加油员合计
     */
    @Override
    public List<Map<String, Object>> getCountByStaff(String taskDate, MyStaff staff) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecpt> fuelRecpts = statisticalMapper.getCountByStaff(startTime, endTime, staff.getStaffAirportCode());
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            taskAmountMap.put("flrcDate", fuelRecpt.getFlrcDate());
            //员工编号
            taskAmountMap.put("flrcDeliverId", fuelRecpt.getFlrcDeliverId());
            //加油员
            taskAmountMap.put("flrcDeliverName", fuelRecpt.getFlrcDeliverName());
            //重量
            taskAmountMap.put("flrcQuantity", fuelRecpt.getFlrcQuantity());
            //体积
            taskAmountMap.put("flrcFuelVol", fuelRecpt.getFlrcFuelVol());
            //数量
            taskAmountMap.put("num", fuelRecpt.getFlrcNo());
            //平均数量
            taskAmountMap.put("flrcFiguars", fuelRecpt.getFlrcFiguars());
            //加油时间
            taskAmountMap.put("flrcMeterStat", fuelRecpt.getFlrcMeterStat());
            //每小时加油数
            taskAmountMap.put("flrcMeterFnsh", fuelRecpt.getFlrcMeterFnsh());

            res.add(taskAmountMap);
        });
        return res;
    }

    /**
     * 日期查询加油车合计
     */
    @Override
    public List<Map<String, Object>> getCountByVehi(String taskDate, MyStaff staff) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        List<Map<String, Object>> res = Lists.newArrayList();

        String startTime = times[0];
        String endTime = times[1];
        List<MyFuelRecpt> fuelRecpts = statisticalMapper.getCountByVehi(startTime, endTime, staff.getStaffAirportCode());
        fuelRecpts.forEach(fuelRecpt -> {
            Map<String, Object> taskAmountMap = new HashMap<String, Object>();
            //日期
            taskAmountMap.put("flrcDate", fuelRecpt.getFlrcDate());
            //加油车编号
            taskAmountMap.put("flrcVehiNum", fuelRecpt.getFlrcVehiNum());
            //加油车牌号
            taskAmountMap.put("flrcVehiNo", fuelRecpt.getFlrcVehiNo());
            //重量
            taskAmountMap.put("flrcQuantity", fuelRecpt.getFlrcQuantity());
            //体积
            taskAmountMap.put("flrcFuelVol", fuelRecpt.getFlrcFuelVol());
            //数量
            taskAmountMap.put("num", fuelRecpt.getFlrcNo());
            //平均数量
            taskAmountMap.put("flrcFiguars", fuelRecpt.getFlrcFiguars());
            //加油时间
            taskAmountMap.put("flrcMeterStat", fuelRecpt.getFlrcMeterStat());
            //每小时加油数
            taskAmountMap.put("flrcMeterFnsh", fuelRecpt.getFlrcMeterFnsh());
            res.add(taskAmountMap);
        });
        return res;
    }

    @Override
    public ReturnMsg<List<Map<String, Object>>> getHours(String taskDate, MyStaff staff) {
        try {
            Objects.requireNonNull(taskDate, "taskDate不可为空");
            Objects.requireNonNull(staff.getLoginUserIn().getStaffAirportCode(), "Token不可为空");
            List<Map<String, Object>> lists = statisticalMapper.getHours(taskDate, staff.getLoginUserIn().getStaffAirportCode());
            return new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, lists);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_ERR, e.getMessage(), null);
        }
    }

    @Override
    public List<Map<String, Object>> exportGetHours(String dataTime, MyStaff staff) {
        return statisticalMapper.getHours(dataTime, staff.getLoginUserIn().getStaffAirportCode());
    }

    @Override
    public List<Map<String, Object>> exportCountByAir(String taskDate, MyStaff staff) {
        String[] times = checkDateStr(taskDate);

        List<Map<String, Object>> res = Lists.newArrayList();
        String startTime = times[0];
        String endTime = times[1];
        List<FuelTotalExcelVo> fuelRecpts = statisticalMapper.getExcelData(startTime, endTime, staff.getStaffAirportCode());
        if (CollectionUtils.isEmpty(fuelRecpts)) {
            return res;
        }

        /**
         * 加油数据 抽油数据到一行合并
         */
        boolean isShow = StringUtils.equals("1", staff.getStaffType());

        Map<String, List<FuelTotalExcelVo>> collect = fuelRecpts.stream().collect(Collectors.groupingBy(fuelRecpt -> {
            StringBuffer stringBuffer = new StringBuffer("");
            stringBuffer.append(fuelRecpt.getFlrcDate())
                    .append("&&")
                    .append((fuelRecpt.getArcrCustomNum() + ""));
            if (isShow) {
                stringBuffer.append("&&").append(fuelRecpt.getFlrcBwtar());
            }
            return stringBuffer.toString();
        }));

        collect.forEach((k, v) -> {
            FuelTotalExcelVo fuelTotalExcelVo = v.get(0);
            Map<String, Object> taskAmountMap = Maps.newHashMap();
            //日期
            taskAmountMap.put("flrcDate", fuelTotalExcelVo.getFlrcDate());
            //航空公司名称
            taskAmountMap.put("airlName", fuelTotalExcelVo.getFlrcAirlName());
            //客户编号
            taskAmountMap.put("arcrCustomNum", fuelTotalExcelVo.getArcrCustomNum());
            if (isShow) {
                //保税 非保税
                taskAmountMap.put("flrcBwtar", StatusConstant.FlrcBwtarEnum.getValueByType(fuelTotalExcelVo.getFlrcBwtar()));
            }

            //加油数据
            final AtomicReference<BigDecimal> addTotalVol = new AtomicReference<>(BigDecimal.ZERO);
            final AtomicReference<BigDecimal> addTotalQuantity = new AtomicReference<>(BigDecimal.ZERO);
            AtomicInteger addCount = new AtomicInteger(0);
            //抽油数据
            AtomicReference<BigDecimal> pumpTotalVol = new AtomicReference<>(BigDecimal.ZERO);
            AtomicReference<BigDecimal> pumpTotalQuantity = new AtomicReference<>(BigDecimal.ZERO);
            AtomicInteger pumpCount = new AtomicInteger(0);
            v.stream().forEach(fuelTotalExcelVo1 -> {

                if (Lists.newArrayList(1, 2, 3).contains(fuelTotalExcelVo1.getFlrcType())) {
                    //加油
                    addTotalVol.set(addTotalVol.get().add(fuelTotalExcelVo1.getDayFuelVol()));
                    addTotalQuantity.set(addTotalQuantity.get().add(fuelTotalExcelVo1.getDayFuelQuantity()));
                    addCount.addAndGet(fuelTotalExcelVo1.getFlrcNo());

                }
                if (Lists.newArrayList(4, 5, 6).contains(fuelTotalExcelVo1.getFlrcType())) {
                    //抽油
                    pumpTotalVol.set(pumpTotalVol.get().add(fuelTotalExcelVo1.getDayFuelVol()));
                    pumpTotalQuantity.set(pumpTotalQuantity.get().add(fuelTotalExcelVo1.getDayFuelQuantity()));
                    pumpCount.addAndGet(fuelTotalExcelVo1.getFlrcNo());
                }
            });
            taskAmountMap.put("addTotalVol", addTotalVol.get().setScale(2, BigDecimal.ROUND_DOWN).toString());
            taskAmountMap.put("addTotalQuantity", addTotalQuantity.get().setScale(2, BigDecimal.ROUND_DOWN).toString());
            taskAmountMap.put("addCount", addCount.get());

            taskAmountMap.put("pumpTotalVol", pumpTotalVol.get().setScale(2, BigDecimal.ROUND_DOWN).toString());
            taskAmountMap.put("pumpTotalQuantity", pumpTotalQuantity.get().setScale(2, BigDecimal.ROUND_DOWN).toString());
            taskAmountMap.put("pumpCount", pumpCount.get());

            res.add(taskAmountMap);
        });

        return res;
    }

    private String[] checkDateStr(String taskDate) {
        if (StringUtils.isEmpty(taskDate)) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段为空", null));
        }
        String[] times = taskDate.split(",");
        if (times.length != 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段格式错误", null));
        }
        if (StringUtils.isEmpty(times[0]) || StringUtils.isEmpty(times[1])) {
            throw new CustomException(ReturnMsg.getInstanceNGz("所查询时间段开始或结束时间为空", null));
        }
        return times;
    }

}
