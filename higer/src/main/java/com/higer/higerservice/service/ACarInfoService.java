package com.higer.higerservice.service;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.repository.oilpro.ACarInfoRepository;
import com.higer.higerservice.util.HttpUtils;
import com.pro.entity.OilCarQueryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/12/3 08:25
 * @Description:
 */
@Service
public class ACarInfoService {

    @Value("${getCarNumUrl}")
    private String carNumUrl;

    @Autowired
    private ACarInfoRepository aCarInfoRepository;

    public Integer findByCarNum(String hp, String airport) {

        //目前写死  方便测试
        //return 1;
        try {
            //post 请求数据 获取车辆编号  为空时 数据不存在
            Map<String, String> map = new HashMap<String, String>();
            map.put("vehiAirportCode", airport);
            map.put("vehiPlateNo", hp);
            // System.out.println("hp--------:"+hp);
            //  System.out.println("airport--------:"+airport);

            String url = carNumUrl + "/base/vehiController/getFuelRecptNo";
            String dataStr = HttpUtils.postBody(url, JSON.toJSONString(map), "application/json");
            System.out.println("dataStr----------------:" + dataStr);
            if (!StringUtils.isEmpty(dataStr)) {
                Map data = JSON.parseObject(dataStr);
                Map o = (Map) data.get("data");
                String vehiFuelCode = (String) o.get("vehiFuelCode");
                if (StringUtils.isEmpty(vehiFuelCode)) {
                    return -404;
                }
                return Integer.valueOf(vehiFuelCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public OilCarQueryInfo findBaseVehMsgByCarNo(String hp, String airport) {
        try {
            OilCarQueryInfo oilCarQueryInfo = new OilCarQueryInfo();
            //post 请求数据 获取车辆编号  为空时 数据不存在
            Map<String, String> map = new HashMap<String, String>();
            map.put("vehiAirportCode", airport);
            map.put("vehiPlateNo", hp);
            // System.out.println("hp--------:"+hp);
            //  System.out.println("airport--------:"+airport);

            String url = carNumUrl + "/base/vehiController/getFuelRecptNoAndFuelSno";
            String dataStr = HttpUtils.postBody(url, JSON.toJSONString(map), "application/json");
            System.out.println("dataStr----------------:" + dataStr);
            if (!StringUtils.isEmpty(dataStr)) {
                Map data = JSON.parseObject(dataStr);
                Map o = (Map) data.get("data");
                String vehiFuelSno = (String) o.get("vehiFuelSno");
                String vehiFuelCode = (String) o.get("vehiFuelCode");
                String vehiNo = (String) o.get("vehiNo");
                System.out.println("vehiFuelSno:" + vehiFuelSno);
                System.out.println("vehiFuelCode:" + vehiFuelCode);
                System.out.println("vehiNo:" + vehiNo);
                if (StringUtils.isEmpty(vehiFuelSno)) {

                    vehiFuelSno = "-404";
                }
                if (StringUtils.isEmpty(vehiFuelCode)) {
                    vehiFuelCode = "-404";
                }
                if (StringUtils.isEmpty(vehiNo)) {
                    vehiNo = "-404";
                }
                oilCarQueryInfo.setCarId(Integer.valueOf(vehiFuelCode));
                oilCarQueryInfo.setCarNo(vehiNo);
                oilCarQueryInfo.setCarSequence(Integer.valueOf(vehiFuelSno));

                return oilCarQueryInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
