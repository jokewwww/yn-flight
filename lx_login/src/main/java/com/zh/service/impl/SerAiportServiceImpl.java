package com.zh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirportCode;
import com.zh.bean.login.MyServAirport;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.SerAiportMapper;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.SerAirportService;
import com.zh.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SerAiportServiceImpl implements SerAirportService {

    @Autowired
    Prop prop;
    @Autowired
    private SerAiportMapper airportMapper;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据机位查询服务机场信息（跨库使用）
     */
    @Override
    public MyServAirport findfwflight(String svapAptplacNo) {
        return airportMapper.findfwflight(svapAptplacNo);
    }

    /**
     * 根据机位查询机场区域代码（跨库使用）
     */
    @Override
    public String getAirportAreaCode(String svapAptplacNo, String getAirportAreaCode) {
        return airportMapper.getAirportAreaCode(svapAptplacNo, getAirportAreaCode);
    }

    /**
     * 根据机场代码，机场区域代码查询服务表
     */
    @Override
    public List<MyServAirport> selectairport(MyStaff staff) {
        return airportMapper.selectairport(staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode());
    }

    /**
     * 对服务表进行增加
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void insertairport(MyServAirport servAirport) {
        if (airportMapper.insertairport(servAirport) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }
    }

    /**
     * 根据机场代码，机场区域代码，机位号，地井编号删除信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deleteairport(MyServAirport servAirport, MyStaff staff) {
        if (airportMapper.deleteairport(servAirport.getSvapAptplacNo(), servAirport.getSvapHydrtPitNo(), staff.getLoginUserIn().getStaffAirportCode(), staff.getLoginUserIn().getStaffAptareaCode()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 同步服务表信息
     */
    @Override
    public void synchronizationAirport(MyServAirport servAirport, MyStaff staff) {
        //跨库方法
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        MyAirportCode airportCodeInfo = new MyAirportCode();
        airportCodeInfo.setApcdIcaoCode(staff.getLoginUserIn().getStaffAirportCode());
        HttpEntity<String> formEntity = new HttpEntity<String>(JsonHelper.object2str(airportCodeInfo).getData(), headers);
        //根据人员所属机场码跨库查询出机场名称
        String info = restTemplate.postForObject(Constant.HTTP + prop.getMapIp() + ":" + prop.getMapPort() + "/map/draw/getManageGeoRegion", formEntity, String.class);
        //根据登录人员对应的机场代码删除信息
        airportMapper.delirport(staff.getLoginUserIn().getStaffAirportCode());
        MyServAirport myServAirport = new MyServAirport();
        //将获取到的字符串转换成JsonArray;
        JSONArray jsonArray = JSONArray.parseArray(info);
        //如果说 jsonArray的size 大于0
        if (jsonArray.size() > 0) {
            //new 一个hashMap泛型的集合
            ArrayList<HashMap> arrayList = new ArrayList<HashMap>();
            //循环
            for (int i = 0; i < jsonArray.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = jsonArray.getJSONObject(i);
                //new 一个 hashmap对象
                HashMap<Object, Object> hashMap = new HashMap<>();
                hashMap.put("SmID", job.get("SmID"));
                hashMap.put("parkList", job.get("parkList"));
                arrayList.add(hashMap);
            }
            //循环遍历获取到的集合
            for (HashMap hashMap : arrayList) {
                String a = (String) hashMap.get("SmID");
                //给机场区域代码赋值
                myServAirport.setSvapAptareaCode(a);
                String b = (String) hashMap.get("parkList");
                //将获取到的 parklist 数据转换成数组类型
                String[] split = b.split("','");
                for (String s : split) {
                    //把单引号去掉
                    s = s.replace("\'", "");
                    //以 , 分割
                    String[] split1 = s.split(",");
                    for (int i = 1; i < split1.length; i++) {
                        myServAirport.setSvapAptplacNo(split1[0]);
                        myServAirport.setSvapHydrtPitNo(split1[i]);
                    }
                }
                //循环添加数据
                airportMapper.inserport(myServAirport.getSvapAptareaCode(), myServAirport.getSvapAptplacNo(), myServAirport.getSvapHydrtPitNo(), staff.getLoginUserIn().getStaffAirportCode());
            }
        }
    }

    /**
     * 查询服务机场
     */
    @Override
    public List<MyServAirport> selectServAirport(MyServAirport servAirport) {
        return airportMapper.selectServAirport(servAirport);
    }

}
