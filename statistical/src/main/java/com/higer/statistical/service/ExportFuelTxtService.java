package com.higer.statistical.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.higer.statistical.entity.TFuelRecpt.ExportTFuel;
import com.higer.statistical.entity.flight.TAirportCode;
import com.higer.statistical.entity.flight.TFlightCode;
import com.higer.statistical.entity.flight.TFlightCodeTemporary;
import com.higer.statistical.entity.flight.TFuelRecpt;
import com.higer.statistical.entity.user.TVehi;
import com.higer.statistical.redis.RedisService;
import com.higer.statistical.repository.flight.TAirportCodeRepository;
import com.higer.statistical.repository.flight.TFlightCodeRepository;
import com.higer.statistical.repository.flight.TFlightCodeTemporaryRepository;
import com.higer.statistical.repository.flight.TFuelRecptRepository;
import com.higer.statistical.repository.user.TStaffRepository;
import com.higer.statistical.repository.user.TVehiRepository;
import com.higer.statistical.util.FileUtil;
import com.higer.statistical.util.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportFuelTxtService {

    private static final SimpleDateFormat simpleDateTime =new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat simpleTime=new SimpleDateFormat("HH:mm");

    @Value("${export.tfuel.mandt}")
    private String mandt;

    @Value("${export.tfuel.uname}")
    private String uname;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TFuelRecptRepository tFuelRecptRepository;

    @Autowired
    private TAirportCodeRepository tAirportCodeRepository;

    @Autowired
    private TStaffRepository tStaffRepository;

    @Autowired
    private TFlightCodeRepository tFlightCodeRepository;

    @Autowired
    private TFlightCodeTemporaryRepository tFlightCodeTemporaryRepository;

    @Autowired
    private TVehiRepository tVehiRepository;


    public void exportTxt(String[] ids, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map optional = parseToken(request);
            if(optional != null){
//                List<TFuelRecpt> list = tFuelRecptRepository.findByFlrcRevwStatusAndFlrcAirportCodeAndFlrcDate(1,String.valueOf(optional.get("staffAirportCode")),date);//只查询已审批的油单
                List<TFuelRecpt> list =tFuelRecptRepository.findByFlrcIdIsIn(ids);
//            List<TFuelRecpt> list = tFuelRecptRepository.findByFlrcRevwStatusAndFlrcAirportCodeAndFlrcDate(1,"2210",date);//只查询已审批的油单
                log.info("查询到油单数量："+list.size());
                List<String> lines = list.stream()
                        .filter(fuel->fuel.getFlrcRevwStatus()!=0)
                        .map(this::transformTFuelRecpt2ExportBean)
                        .map(this::exportBean2Line)
                        .collect(Collectors.toList());
                File tmpFile=File.createTempFile(LocalDate.now().toString()+"导出",".txt");
                FileUtils.writeLines(tmpFile,lines);
                FileUtil.downloadFiles(LocalDate.now().toString()+"导出",".txt",tmpFile,request,response);
                list.stream()
                        .filter(fuel->fuel.getFlrcRevwStatus()!=0)
                        .forEach(o->{
                            o.setFlrcRevwStatus(2);
                            tFuelRecptRepository.save(o);
                        });
            }else{
                log.error("未登录");
                throw  new RuntimeException("未登录");
            }
        } catch (IOException e) {
            log.error("导出TXT错误",e);
            e.printStackTrace();
        }
    }

    public Map  parseToken(HttpServletRequest request) {
        try {
            String token=request.getHeader("token");
            String tokenBase64 = token.substring(token.indexOf(".")+1, token.lastIndexOf("."));
            String _token = new String(Base64.getDecoder().decode(tokenBase64), StandardCharsets.UTF_8);
            Map map = JSONObject.parseObject(_token, Map.class);
            String login_user_key = (String) map.get("LOGIN_USER_KEY");
            String tokens = "tokens:"+login_user_key;
            String obj = redisService.getStr(tokens);
            Map data = JSONObject.parseObject(obj, Map.class);
            return data;

        } catch (Exception e){
            log.error("Token解析错误",e);
        }
        return null;
    }

    /**
     * 导出EPR类格式化成行
     * @param exportTFuel
     * @return
     */
    private String exportBean2Line(ExportTFuel exportTFuel) {
        final BeanWrapper wrapper = new BeanWrapperImpl(exportTFuel);
        return Arrays.stream(exportTFuel.getClass().getDeclaredFields()).sequential().map(field -> {
//            System.out.println(field.getName()+"："+wrapper.getPropertyValue(field.getName()));
            Object propertyValue = wrapper.getPropertyValue(field.getName());
            if(null == propertyValue ){
                return "";
            }
            return propertyValue;
        }).map(Object::toString).collect(Collectors.joining("\t"));
    }

    public TFlightCode getFlightCodeInfoByRegnAndFlno(String flgtRegn, String flgtFlno) {
        try {
            List<TFlightCode> mfList= Lists.newArrayList();
            if (StringUtils.isNotBlank(flgtRegn) && StringUtils.isNotBlank(flgtFlno)) {
                mfList= tFlightCodeRepository.selectByArcrRegnsFlnoAndTime(flgtRegn,flgtFlno);
            }
            if(mfList==null || mfList.size()==0){
                if (StringUtils.isNotBlank(flgtRegn)){
                    mfList= tFlightCodeRepository.selectByArcrRegnsAndTime(flgtRegn);
                }
            }
            if(mfList==null || mfList.size()==0){
                if(StringUtils.isNotBlank(flgtRegn)){
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(StringUtils.isNotBlank(newFlgtRegn)){
                        if (StringUtils.isNotBlank(flgtFlno)) {
                            mfList= tFlightCodeRepository.selectByArcrRegnsFlnoAndTime(newFlgtRegn,flgtFlno);
                        }
                        if(mfList==null || mfList.size()==0){
                            mfList= tFlightCodeRepository.selectByArcrRegnsAndTime(newFlgtRegn);
                        }
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                if (StringUtils.isNotBlank(flgtRegn) && StringUtils.isNotBlank(flgtFlno)) {
                    mfList= TFlightCodeTemporary.converToFlgihtCode(tFlightCodeTemporaryRepository.selectByArcrRegnsFlnoAndTime(flgtRegn, flgtFlno));
                }
            }
            if(mfList==null || mfList.size()==0){
                if (StringUtils.isNotBlank(flgtRegn)){
                    mfList= TFlightCodeTemporary.converToFlgihtCode(tFlightCodeTemporaryRepository.selectByArcrRegnsAndTime(flgtRegn));
                }
            }
            if(mfList==null || mfList.size()==0){
                if (StringUtils.isNotBlank(flgtRegn) && StringUtils.isNotBlank(flgtFlno)) {
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(StringUtils.isNotBlank(newFlgtRegn)) {
                        mfList = TFlightCodeTemporary.converToFlgihtCode(tFlightCodeTemporaryRepository.selectByArcrRegnsFlnoAndTime(newFlgtRegn, flgtFlno));
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                if (StringUtils.isNotBlank(flgtRegn)){
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(StringUtils.isNotBlank(newFlgtRegn)){
                        mfList= TFlightCodeTemporary.converToFlgihtCode(tFlightCodeTemporaryRepository.selectByArcrRegnsAndTime(newFlgtRegn));
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                return  null;
            }
            mfList.stream()
                    .sorted(Comparator.comparing((TFlightCode item) -> item.getCnafUpdateTime(),
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            return mfList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TFlightCode getFlightCodeInfo(String flgtRegn, String flgtFlno) {
        if(StringUtils.isBlank(flgtRegn)){
            return null;
        }
        String replaceFlgtRegn=StringUtils.replace(flgtRegn,"-","");
        TFlightCode flightCodeInfoByRegnAndFlno = getFlightCodeInfoByRegnAndFlno(replaceFlgtRegn, flgtFlno);
        if(flightCodeInfoByRegnAndFlno==null){
            flightCodeInfoByRegnAndFlno = getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtFlno);
        }
        if(flightCodeInfoByRegnAndFlno==null){
            String replaceFlgtFlno=StringUtils.replace(flgtFlno,"-","");
            flightCodeInfoByRegnAndFlno=getFlightCodeInfoByRegnAndFlno(replaceFlgtFlno,flgtFlno);
        }
        if(flightCodeInfoByRegnAndFlno==null){
            flightCodeInfoByRegnAndFlno=getFlightCodeInfoByRegnAndFlno(flgtFlno,flgtFlno);
        }
        return flightCodeInfoByRegnAndFlno;
    }

    private String transformTFuelRecpt2ExportBeanTest(TFuelRecpt tFuelRecpt){
        try {
            //校验
            Assert.hasLength(tFuelRecpt.getFlrcAirportCode(),"机场号码不能为空");
            Assert.hasLength(tFuelRecpt.getFlrcAircrftNo(),"飞机号码不能为空");
            Assert.hasLength(tFuelRecpt.getFlrcDeliverId(),"加油员id不能为空");
            Assert.hasLength(tFuelRecpt.getFlrcVehiNo(),"加油车编号不能为空");
            TAirportCode one = tAirportCodeRepository.findOneByApcdCnafAirportCode(tFuelRecpt.getFlrcAirportCode());
            Assert.notNull(one,"查询不到对应生效机场："+tFuelRecpt.getFlrcAirportCode());
            String flgtRegn = tFuelRecpt.getFlrcAircrftNo();//拼接飞机号
            String flrcFlightNo = tFuelRecpt.getFlrcFlightNo();
            if(StringUtils.isBlank(flgtRegn)){
                Assert.notNull(null,"飞机号拼接异常："+flgtRegn);
            }
            log.info("transform获取飞机信息：飞机号-{}，航班号-{}",flgtRegn,flrcFlightNo);
            //获取飞机号归属信息
            TFlightCode tFlightCode=getFlightCodeInfo(flgtRegn,flrcFlightNo);
            if(null == tFlightCode && StringUtils.isNotBlank(flrcFlightNo)){
               Assert.notNull(tFlightCode,"查询不到对应的购货方编号："+flrcFlightNo);
            }else{
               Assert.notNull(tFlightCode,"航班号不可为空："+flrcFlightNo);
            }
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    /**
     * 本地实体格式化导出类
     * @param tFuelRecpt
     * @return
     */
    private ExportTFuel transformTFuelRecpt2ExportBean(TFuelRecpt tFuelRecpt) {

        //校验
        Assert.hasLength(tFuelRecpt.getFlrcAirportCode(),"机场号码不能为空");
        Assert.hasLength(tFuelRecpt.getFlrcAircrftNo(),"飞机号码不能为空");
        Assert.hasLength(tFuelRecpt.getFlrcDeliverId(),"加油员id不能为空");
        Assert.hasLength(tFuelRecpt.getFlrcVehiNo(),"加油车编号不能为空");
        TAirportCode one = tAirportCodeRepository.findOneByApcdCnafAirportCode(tFuelRecpt.getFlrcAirportCode());
        Assert.notNull(one,"查询不到对应生效机场："+tFuelRecpt.getFlrcAirportCode());
        String flgtRegn = tFuelRecpt.getFlrcAircrftNo();//拼接飞机号
        String flrcFlightNo = tFuelRecpt.getFlrcFlightNo();
        if(StringUtils.isEmpty(flgtRegn)){
            Assert.notNull(null,"飞机号拼接异常："+flgtRegn);
        }
        log.info("transform获取飞机信息：飞机号-{}，航班号-{}",flgtRegn,flrcFlightNo);
        //获取飞机号归属信息
        TFlightCode tFlightCode=getFlightCodeInfo(flgtRegn,flrcFlightNo);
        if(null == tFlightCode && StringUtils.isNotBlank(flrcFlightNo)){
            Assert.notNull(tFlightCode,"查询不到对应的购货方编号："+flrcFlightNo);
        }else{
            Assert.notNull(tFlightCode,"航班号不可为空："+flrcFlightNo);
        }
        //校验完成
        ExportTFuel exportTFuel = new ExportTFuel();

        //TODO 写死数据
        exportTFuel.setSpart("11");//部门
        exportTFuel.setKunnr(tFlightCode.getArcrCustomNum());//飞机单位（购货方编号）
        exportTFuel.setVstel(tFuelRecpt.getFlrcAirportCode());//机场（装运点）
        exportTFuel.setLgort("X999");//油罐
        exportTFuel.setPernr2("0");//加油员2
        exportTFuel.setMatnr("1");//油品规格（1=3号航空燃料）
        exportTFuel.setOrgeh("");//组织单位，为空
        exportTFuel.setPersa(one.getApcdPersa());//人事范围
        exportTFuel.setBtrtl(one.getApcdBtrtl());//人事子范围
        //TODO 一堆要赋值的地方
        int flrcType = 3; // 默认为 国内
        if(null != tFuelRecpt.getFlrcType() ){
            flrcType =compareType(tFuelRecpt.getFlrcType());
        }
        exportTFuel.setZlx(flrcType+"");//油单类型
        exportTFuel.setBstkd(tFuelRecpt.getFlrcNo());//加油单编号
        exportTFuel.setJydata(simpleDateTime.format(tFuelRecpt.getFlrcDate()));//加油日期
        exportTFuel.setMfrgr(tFuelRecpt.getFlrcFlightNo());//飞机号
        exportTFuel.setTrmtyp(flgtRegn);//飞机号码
        exportTFuel.setTraty(tFlightCode.getArcrAcname());//飞机类型
        exportTFuel.setSyname(tFuelRecpt.getFlrcAirlCode());//收油人(航空公司二字码)
        if(null != tFuelRecpt.getFlrcFuelDnst()){
            if(tFuelRecpt.getFlrcFuelDnst().toString().length() == 5){
                exportTFuel.setSjmd(tFuelRecpt.getFlrcFuelDnst().toString()+"0");//密度
            }else{
                exportTFuel.setSjmd(tFuelRecpt.getFlrcFuelDnst()+"");//密度
            }
        }

        exportTFuel.setBstzd(tFuelRecpt.getFlrcFuelTemp()+"");//温度
        exportTFuel.setJytj(tFuelRecpt.getFlrcFuelVol()+"");//体积
        exportTFuel.setSjzl(tFuelRecpt.getFlrcQuantity()+"");//质量

        TVehi byVehiPlateNo = tVehiRepository.findByVehiPlateNo(tFuelRecpt.getFlrcVehiNo());
        if(null != byVehiPlateNo){
            exportTFuel.setOicntper(byVehiPlateNo.getVehiNo());//加油车编号
        }else{
            exportTFuel.setOicntper(tFuelRecpt.getFlrcVehiNo());//加油车编号
        }

      //  exportTFuel.setOicntper(tFuelRecpt.getFlrcVehiNo());//加油车编号
        exportTFuel.setOicntpho(tFuelRecpt.getFlrcHydrtPitNo());//地井编号
        exportTFuel.setPernr(tFuelRecpt.getFlrcDeliverId());//加油员ID
        exportTFuel.setJykssj(formtDate(tFuelRecpt.getFlrcStatTime()));//加油开始时间
        exportTFuel.setJyjssj(formtDate(tFuelRecpt.getFlrcFnshTime()));//加油结束时间
        exportTFuel.setOicntnte(tFuelRecpt.getFlrcTestBillNo());//化验单编号
//        exportTFuel.setVsart("");//航班任务
        exportTFuel.setVkorg(one.getApcdVkorg());
        exportTFuel.setVtweg(one.getApcdVtweg());
        exportTFuel.setWerks(one.getApcdWerks());
        exportTFuel.setVkbur(one.getApcdVkbur());
        exportTFuel.setVkgrp(one.getApcdVkgrp());
        //设置评估类型
///3国内，2离境，1外航


        // 设置保税类型 也就是评估类型
        if(StringUtils.isNotEmpty(tFuelRecpt.getFlrcBwtar())){
            exportTFuel.setBwtar(tFuelRecpt.getFlrcBwtar());
        }else{
            if("2901".equals(tFuelRecpt.getFlrcAirportCode())){
                if(flrcType == 3){
                    exportTFuel.setBwtar("FB");
                }else{
                    exportTFuel.setBwtar("B");
                }
            }else{
                exportTFuel.setBwtar("FB");
            }
        }


      /*  if(StringUtils.equals("1",one.getApcdBwtar())){//保税机场
            exportTFuel.setBwtar("FB");
        }else if (tFuelRecpt.getFlrcType()==1||tFuelRecpt.getFlrcType()==2||tFuelRecpt.getFlrcType()==5||tFuelRecpt.getFlrcType()==4){//外航或离境
            exportTFuel.setBwtar("YB");
        }else{
            exportTFuel.setBwtar("B");
        }*/
        return exportTFuel;
    }

    private String translateFlghtNo(String flrcAircrftNo) {

        if(StringUtils.isNotEmpty(flrcAircrftNo)){
            //如果飞机号码没有-则手动加上
            if( flrcAircrftNo.startsWith("B") && flrcAircrftNo.indexOf("-")==-1){
                StringBuffer stringBuilder1=new StringBuffer(flrcAircrftNo);
                stringBuilder1.insert(1,"-");
                flrcAircrftNo=stringBuilder1.toString();
                System.out.println("飞机号:"+flrcAircrftNo);
            }else{
                //TODO
                String a = "^([A-Z]+)(\\d*.*)$";
                Pattern compile = Pattern.compile(a);
                Matcher matcher = compile.matcher(flrcAircrftNo);
                if(matcher.matches()){
                    if(StringUtils.isNotEmpty(matcher.group(2)) && StringUtils.isNotEmpty(matcher.group(1))){
                        flrcAircrftNo = matcher.group(1) + "-" + matcher.group(2);
                    }else if(StringUtils.isNotEmpty(matcher.group(1))){
                        flrcAircrftNo = matcher.group(1);
                    }
                }
            }
          return  flrcAircrftNo;
        }
        return null;
    }

    private String formtDate(Date date) {
        if(date!=null){
            return simpleTime.format(date);
        }else{
            return "";
        }
    }
    //（1：外航加油，2：内航离境加油，3：内航国内加油，4：外航抽油，5：内航离境抽油，6：内航国内抽油
    // 7:  内航国内补加油 8: 内航 离境 补加油 9 : 外航 补油）
    private Integer compareType(Integer type){
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

    public ResponseObject<Object> exportTxtAdvance(String[] ids, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map optional = parseToken(request);
            if(optional != null){
                List<TFuelRecpt> list =tFuelRecptRepository.findByFlrcIdIsIn(ids);
                log.info("查询到油单数量："+list.size());
                List<String> error = list.stream()
                        .filter(fuel->fuel.getFlrcRevwStatus()!=0)
                        .map(o->{
                            String returnMsg = "";
                            String s = transformTFuelRecpt2ExportBeanTest(o);
                            if(s.equals("error")){
                                returnMsg = o.getFlrcId();
                            }
                            return returnMsg;
                        }).filter(o-> StringUtils.isNotEmpty(o))
                        .collect(Collectors.toList());
                List<String> success = list.stream()
                        .filter(fuel->fuel.getFlrcRevwStatus()!=0)
                        .map(o->{
                            String s = transformTFuelRecpt2ExportBeanTest(o);
                            String returnMsg = "";
                            if(s.equals("success")){
                                returnMsg =  o.getFlrcId();
                            }
                            return returnMsg;
                        }).filter(o-> StringUtils.isNotEmpty(o))
                        .collect(Collectors.toList());
                String s = JSON.toJSONString(error);
                return  ResponseObject.success(success, s);
            }else{
                log.error("未登录");
                throw new RuntimeException("未登录");
            }
        } catch (Exception e) {
            log.error("导出TXT错误",e);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Double s = 0.789;
        Double s1 = 0.7891;

        System.out.println(s.toString().length());
        System.out.println(s1.toString().length());
    }
}
