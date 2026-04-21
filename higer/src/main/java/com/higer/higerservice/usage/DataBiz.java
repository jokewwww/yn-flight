package com.higer.higerservice.usage;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.component.ObjectProperties;
import com.higer.higerservice.component.WebSocketService;
import com.higer.higerservice.entity.oilpro.AApkVersion;
import com.higer.higerservice.entity.oilpro.AGpsRecords;
import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.entity.oilpro.Apk;
import com.higer.higerservice.repository.flight.TFuelRecptRepository;
import com.higer.higerservice.repository.flight.TTaskRepository;
import com.higer.higerservice.repository.oilpro.AGpsRecordsRepository;
import com.higer.higerservice.repository.oilpro.ARecordsRepository;
import com.higer.higerservice.repository.oilpro.ASheetRepository;
import com.higer.higerservice.service.ACarInfoService;
import com.higer.higerservice.service.CommInterfaceService;
import com.higer.higerservice.service.FileService;
import com.higer.higerservice.util.DateUtil;
import com.pro.entity.*;
import com.pro.interf.IDataRequestFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DataBiz implements IDataRequestFace {

    private static final long TIME_OUT = 60 * 15;

    public static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    @Autowired
    protected CommInterfaceService commInterfaceService;
    @Autowired
    protected WebSocketService webSocketService;
    @Autowired
    protected ASheetRepository aSheetRepository;
    @Autowired
    protected ObjectProperties objectProperties;
    @Autowired
    private ARecordsRepository aRecordsRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private ACarInfoService aCarInfoService;
    //测试使用
    @Autowired
    private AGpsRecordsRepository aGpsRecordsRepository;

    @Autowired
    private TFuelRecptRepository tFuelRecptRepository;

    @Autowired
    private TTaskRepository tTaskRepository;

    @Value("${lockTest}")
    private Boolean lock;
    @Value("${getCarNumUrl}")
    private String carNumUrl;

    @Value("${getPrintWait}")
    private String printWait;

    @Override
    public int notifyOilSheet(String oilSheet) {
        try {
            /*System.out.println("notifyOilSheet" + oilSheet);
            String oilSheetNew = new String(oilSheet.getBytes(), "UTF-8");
            OilSheetNew oilSheetObj = null;
            try {
                oilSheetObj = JSON.parseObject(oilSheetNew, OilSheetNew.class);
                System.out.println("parseObject:---oilSheetObj  " + JSON.toJSONString(oilSheetObj));
            } catch (Exception e) {
                System.out.println("JSON 转化失败 notifyOilSheet");
                e.printStackTrace();
            }
            /*TFuelRecpt tFuelRecpt = new TFuelRecpt();
            if (oilSheetObj != null) {
                //油单编号
                //oilSheetObj.getFlrcId();
                List<TFuelRecpt> byFlrcNo = tFuelRecptRepository.findByFlrcNo(oilSheetObj.getFlrcId());
                if (byFlrcNo.size() == 1) {
                    if (byFlrcNo.get(0).getFlrcRevwStatus() == 1) {
                        return 1;
                    }
                } else if (byFlrcNo.size() > 1) {
                    System.out.println(DateUtil.getCurrentDateTimeStr() + "上传油单编号重复两次" + oilSheetObj.getFlrcId());
                    return 1;
                }
                TTask one = null;
                if (byFlrcNo.size() == 0) {
                    String taskId = oilSheetObj.getTaskId();
                    if (!StringUtils.isEmpty(taskId)) {
                        one = tTaskRepository.findOne(taskId);
                        if (one != null) {
                            //one.setTaskStatus(6);  于总让干掉
                            one.setTaskRcPrintTime(new Date());
                            if(null==one.getTaskDoneTime()){
                                one.setTaskDoneTime(new Date());
                            }
                            if(null==one.getTaskChagEndTime()){
                                Calendar nowTime1 = Calendar.getInstance();
                                nowTime1.add(Calendar.MINUTE, -15);
                                one.setTaskChagEndTime(nowTime1.getTime());
                            }
                            if(null==one.getTaskChagStaTime()){
                                Calendar nowTime2 = Calendar.getInstance();
                                nowTime2.add(Calendar.MINUTE, -30);
                                one.setTaskChagStaTime(nowTime2.getTime());
                            }
                            if(null==one.getTaskArriveTime()){
                                Calendar nowTime3 = Calendar.getInstance();
                                nowTime3.add(Calendar.MINUTE, -45);
                                one.setTaskArriveTime(nowTime3.getTime());
                            }
                        }
                    } else {
                        System.out.println("taskId为空");
                        return 1;
                    }
                    ModelAssistant.copyProperties(oilSheetObj, tFuelRecpt);
                    if(null!=tFuelRecpt.getFlrcFlightNo()){
                        String flightNo=tFuelRecpt.getFlrcFlightNo();
                        String[] flightNos=flightNo.split("/");
                        if (flightNos.length>0) {
                            tFuelRecpt.setFlrcFlightNo(flightNos[0]);
                        }
                    }
                    System.out.println("copyProperties数据:" + JSON.toJSONString(tFuelRecpt));
                    tFuelRecpt.setFlrcId(UUID.randomUUID().toString());
                    tFuelRecpt.setFlrcType(Integer.valueOf(oilSheetObj.getFlrcType()));
                    tFuelRecpt.setFlrcNo(oilSheetObj.getFlrcId());
                    tFuelRecpt.setFlrcDate(DateUtil.parse(oilSheetObj.getFlrcDate()));
                    tFuelRecpt.setFlrcMeterStat(oilSheetObj.getFlrcMeterStat().intValue());
                    tFuelRecpt.setFlrcMeterFnsh(oilSheetObj.getFlrcMeterFnsh().intValue());
                    tFuelRecpt.setFlrcStatTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
                    tFuelRecpt.setFlrcFnshTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
                    tFuelRecpt.setFlrcQuantity(oilSheetObj.getFlrcQuantity().doubleValue());
                    tFuelRecpt.setFlrcDeliverId(oilSheetObj.getFlrcDeliverId());
                    tFuelRecpt.setFlrcAirportCode(oilSheetObj.getFlrcAirportCode());
                    tFuelRecpt.setFlrcAirlCode(oilSheetObj.getFlrcAirlCode());
                    tFuelRecpt.setFlrcFuelVol(oilSheetObj.getFlrcFuelVol());
                    tFuelRecpt.setFlrcRecCreTime(new Date());
                    System.out.println("插入的数据:" + JSON.toJSONString(tFuelRecpt));
                    TFuelRecpt save = tFuelRecptRepository.save(tFuelRecpt);
                    if (one != null && save != null) {
                        one.setTaskFuelRecptNo(save.getFlrcNo());
                        tTaskRepository.save(one);
                        System.out.println("油单上传成功---" + JSON.toJSONString(one));
                    }
                    //
                    String flrcNo = oilSheetObj.getFlrcId();
                    //判断去空
                    if (flrcNo.length() > 6) {
                        String vehiFuelSno = flrcNo.substring(flrcNo.length() - 6, flrcNo.length());
                        System.out.println("vehiFuelSno:" + vehiFuelSno);
                        Map<String, String> map = new HashMap<>();
                        map.put("vehiAirportCode", oilSheetObj.getFlrcAirportCode());
                        map.put("vehiPlateNo", oilSheetObj.getFlrcVehiNo());
                        map.put("vehiFuelSno", vehiFuelSno);
                        try {
                            String vehiUrl = carNumUrl + "/base/vehiController/updateVehiFuelSno";
                            String dataStr = HttpUtils.postBody(vehiUrl, JSON.toJSONString(map), "application/json");
                            System.out.println("车牌修改打印编号：" + oilSheetObj.getFlrcId() + "," + dataStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("油单已经存在" + JSON.toJSONString(oilSheetNew));
                    tFuelRecpt = byFlrcNo.get(0);
                    ModelAssistant.copyProperties(oilSheetObj, tFuelRecpt);
                    System.out.println("更新copyProperties数据:" + JSON.toJSONString(tFuelRecpt));
                    tFuelRecpt.setFlrcType(Integer.valueOf(oilSheetObj.getFlrcType()));
                    tFuelRecpt.setFlrcDate(DateUtil.parse(oilSheetObj.getFlrcDate()));
                    tFuelRecpt.setFlrcMeterStat(oilSheetObj.getFlrcMeterStat().intValue());
                    tFuelRecpt.setFlrcMeterFnsh(oilSheetObj.getFlrcMeterFnsh().intValue());
                    tFuelRecpt.setFlrcStatTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
                    tFuelRecpt.setFlrcFnshTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
                    tFuelRecpt.setFlrcQuantity(oilSheetObj.getFlrcQuantity().doubleValue());
                    tFuelRecpt.setFlrcDeliverId(oilSheetObj.getFlrcDeliverId());
                    tFuelRecpt.setFlrcAirportCode(oilSheetObj.getFlrcAirportCode());
                    tFuelRecpt.setFlrcAirlCode(oilSheetObj.getFlrcAirlCode());
                    tFuelRecpt.setFlrcFuelVol(oilSheetObj.getFlrcFuelVol());
                    tFuelRecpt.setFlrcRecCreTime(new Date());
                    System.out.println("更新的数据:" + JSON.toJSONString(tFuelRecpt));
                    TFuelRecpt save = tFuelRecptRepository.save(tFuelRecpt);
                    if (one != null && save != null) {
                        one.setTaskFuelRecptNo(save.getFlrcNo());
                        tTaskRepository.save(one);
                        System.out.println("油单上传成功---" + JSON.toJSONString(one));
                    }
                    return 0;
                }
            } else {
                System.out.println("油单上传失败---" + JSON.toJSONString(oilSheet));
                return 0;
            }*/
//*/
        } catch (Exception e) {
            System.out.println("油单上传失败---" + JSON.toJSONString(oilSheet));
            e.printStackTrace();

        }
        return 0;
    }

    @Override
    public int notifyCarData(String carData) {
        /*
        try {
            if (!StringUtils.isEmpty(carData)) {
                //通过socket达到实时推送
                //dataBiz.webSocketService.onMessage(carData, null);
                //System.err.println("提交的kafka的数据---notifyCarData");
                commInterfaceService.doWriteKafKa("carsignal", carData, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return 0;
    }

    @Override
    public int notifyGpsData(String gpsData) {
        /*
        try {
            if (!StringUtils.isEmpty(gpsData)) {
                OilGpsSignal oilGpsSignal = JSON.parseObject(gpsData, OilGpsSignal.class);
                if (oilGpsSignal != null) {
                    if (!StringUtils.isEmpty(oilGpsSignal.getHp())) {
                        String s = "cargps:" + oilGpsSignal.getHp();
                        commInterfaceService.doWriteRedis(s, gpsData, TIME_OUT);
                    }
                }
                //System.err.println("提交的kafka的数据---notifyGpsData");
                commInterfaceService.doWriteKafKa("cargps", gpsData, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return 0;
    }

    @Override
    public int notifyComboData(String strRes) {
        System.err.println("终端推送到notifyComboData的参数:" + strRes);
        AtomicReference<Boolean> bool = new AtomicReference<Boolean>(true);
        ComboStatusToCec comboStatus = JSON.parseObject(strRes, ComboStatusToCec.class);
        Boolean lookNotifyComboData = false;
        try {
            Long addTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comboStatus.getAddTime()).getTime();
            Long timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comboStatus.getTimeStamp()).getTime();

            System.out.println("进行时间判断------addtime=" + comboStatus.getAddTime() + "   timestamp = " + comboStatus.getTimeStamp());

            if (60 > Math.abs((addTime - timeStamp) / 1000)) {
                System.out.println("数据相差大于一分钟 进行时间判断------addtime=" + comboStatus.getAddTime() + "   timestamp = " + comboStatus.getTimeStamp());
                lookNotifyComboData = true;
                ARecords aRecords = new ARecords();
                aRecords.setFunc("kafka");
                aRecords.setDateType(1);
                aRecords.setDate(strRes);
                aRecords.setErrorMsg("工控机的历史数据");
                aRecords.setInsertDate(DateUtil.getCurrentDateStr());
                aRecords.setState(1);
                aRecordsRepository.saveAndFlush(aRecords);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // lock是锁 , 默认 dev环境下 开启 其他环境关闭  此处用来统计 1分钟 或者 1小时内 终端发送的数据   正式环境下 不需要启用
        if (lock) {
            AGpsRecords aGpsRecords = new AGpsRecords();
            aGpsRecords.setHp(comboStatus.getHp());
            aGpsRecords.setDatas(strRes);
            aGpsRecords.setCreateTime(new Date());
            aGpsRecordsRepository.saveAndFlush(aGpsRecords);
            // 155行-  173行 是 缓存 经纬度等信息
            for (Map<String, Object> map : list) {
                if (map.get("hp").equals(comboStatus.getHp())) {
                    bool.set(false);
                    map.put("dateTimeStr", DateUtil.getCurrentDateTimeStr());
                    map.put("data", strRes);
                    map.put("state", 1);
                    break;
                } else {
                    bool.set(true);
                }
            }
            if (bool.get()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("data", strRes);
                map.put("hp", comboStatus.getHp());
                map.put("dateTimeStr", DateUtil.getCurrentDateTimeStr());
                map.put("state", 1);
                list.add(map);
            }
        }
        //生产环境 打开下边代码
        ComboStatusToCec comboStatus1 = null;
        try {
            //发送kafka 数据
            //测试不发送数据
            if (lookNotifyComboData) {
                commInterfaceService.doWriteKafKa("carsignal", "real", strRes, null);
            }
            if (!StringUtils.isEmpty(strRes)) {
                System.out.println("tcpData res:" + strRes);
                comboStatus1 = JSON.parseObject(strRes, ComboStatusToCec.class);
            }
            if (null != comboStatus1) {
                //此处调用修的接口
                OilGpsSignal oilGpsSignal = new OilGpsSignal();
                oilGpsSignal.loadFromCombo(comboStatus1);
                if (1 == oilGpsSignal.getFlag() || 2 == oilGpsSignal.getFlag()) {
                    //有效的gps数据
                }

                String strGpsSignal = JSON.toJSONString(oilGpsSignal);
                String key = "cargps:" + comboStatus1.getAirport() + ":" + oilGpsSignal.getHp();
                //测试不发送数据
                commInterfaceService.doWriteRedis(key, strGpsSignal, null, strRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //  ver 版本号 版本号一样  返回 null  不一样 返回数组(先放一条)  备注
    @Override
    public byte[] requestUpdateApk(String ver) {
        System.out.println("verver-----verver" + ver);
        if (!StringUtils.isEmpty(ver)) {
            Apk apk = fileService.getBytes(ver, 0);
            if (apk.getApk() != null && !StringUtils.isEmpty(apk.getVer())) {
                return apk.getApk();
            }
        }
        return null;
    }

    //pad 初始化的时候请求到此 得到车辆编码
    @Override
    public int requestCaId(String airport, String hp) {
        //return 1;
        if (StringUtils.isEmpty(airport) || StringUtils.isEmpty(hp)) {
            return 0;
        }
        return aCarInfoService.findByCarNum(hp, airport);
    }

    @Override
    public OilCarQueryInfo requestCaSequence(String airport, String hp) {
        //return 1;
        if (StringUtils.isEmpty(airport) || StringUtils.isEmpty(hp)) {
            return null;
        }
        return aCarInfoService.findBaseVehMsgByCarNo(hp, airport);
    }

    @Override
    public OilCarQueryInfoNew requestCaSequenceNew(String airport, String hp) {
        if (StringUtils.isEmpty(airport) || StringUtils.isEmpty(hp)) {
            return null;
        }
        OilCarQueryInfo baseVehMsgByCarNo = aCarInfoService.findBaseVehMsgByCarNo(hp, airport);
        if (baseVehMsgByCarNo != null) {
            try {
                OilCarQueryInfoNew oilCarQueryInfoNew = new OilCarQueryInfoNew();
                oilCarQueryInfoNew.setCarId(baseVehMsgByCarNo.getCarId());
                oilCarQueryInfoNew.setCarNo(baseVehMsgByCarNo.getCarNo());
                oilCarQueryInfoNew.setCarSequence(baseVehMsgByCarNo.getCarSequence());
                // 新增参数
                List<OilCarParam> oilCarParamList = new ArrayList<OilCarParam>();
                OilCarParam oilCarParam = new OilCarParam();
                oilCarParam.setParam("printWait");
                AApkVersion printNum = fileService.getPrintNum(6);
                oilCarParam.setValue1(printNum.getRemark());
                oilCarParamList.add(oilCarParam);

                OilCarParam oilCarParam1 = new OilCarParam();
                oilCarParam1.setParam("flowThreshold");
                AApkVersion flowThreshold = fileService.getPrintNum(7);
                oilCarParam1.setValue1(flowThreshold.getRemark());
                oilCarParamList.add(oilCarParam1);
                oilCarQueryInfoNew.setCarParams(oilCarParamList);
                return oilCarQueryInfoNew;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

