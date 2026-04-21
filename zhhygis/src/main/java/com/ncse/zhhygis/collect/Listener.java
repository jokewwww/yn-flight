package com.ncse.zhhygis.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ncse.zhhygis.service.CarAirInterfaceService;
import com.ncse.zhhygis.service.CarGisDataService;
import com.ncse.zhhygis.utils.baseUtils.RedisUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CarGisDataService carGisDataService;

    @Autowired
    private CarAirInterfaceService carAirInterfaceService;

    @Autowired
    private RedisUtils redisUtils;

    @KafkaListener(topics = {"${topicName}"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        /*String carStatus = redisUtils.get("carStatus") == null ? "0" : redisUtils.get("carStatus");*/
        logger.info("kafka的value: " + record.value().toString());
        try {
            carGisDataService.carGisDataHandle(record.value().toString(), (String) record.key());
            //推送完了之后把redis里面的数据还原
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = {"${carTopicName}"})
    public void carListen(ConsumerRecord<?, ?> record) {
        try {
            logger.info("kafka实时车辆状态的key: " + record.key());
            logger.info("kafka实时车辆状态的value: " + record.value().toString());
            if (record.value() != null) { //如果最新数据，推送到前端
                //新的状态数据  把状态给前端

                JSONObject gisJson = JSON.parseObject(record.value().toString());
                String staffid = gisJson.getString("staffId");
                String aircode = gisJson.getString("airport");
                if (!StringUtils.isEmpty(staffid)) {
                    //人员详情获取人员名称
                    JSONObject ryxq = JSON.parseObject(carAirInterfaceService.ryxq(staffid));
                    JSONObject ryxqdata = ryxq.getJSONObject("data");
                    String ryname = ryxqdata.getString("staffName");
                    //任务列表里获取任务展示在前端
                    JSONObject rwlb = JSON.parseObject(carAirInterfaceService.rwlb(staffid));
                    JSONArray rwlblist = rwlb.getJSONArray("data");
                    List<Map> list = new ArrayList();
                    Map m;
                    JSONObject json;
                    String status;

                    for (int i = 0; i < rwlblist.size(); i++) {
                        json = rwlblist.getJSONObject(i);
                        status = json.getString("taskStatus");
                        m = new HashMap();
                        m.put("flightNum", json.getString("flgtFlno"));
                        m.put("status", status);
                        m.put("placeCode", json.getString("flgtPlacecode"));
                        m.put("dstot", json.getString("flgtDStot"));
                        list.add(m);
                        if (Integer.valueOf(status).intValue() >= 2) {
                            gisJson.put("flightNum", json.getString("flgtFlno"));
                        }
                    }
                    gisJson.put("name", ryname);
                    gisJson.put("missionList", list);
                }
                String carStatus = StringUtils.isEmpty(gisJson.getString("status")) ? "1" : gisJson.getString("status");
                String carNum = gisJson.getString("vehiPlateNo");
                //获取存储的状态信息
                String redisJson = redisUtils.get(aircode + "carStatus" + carNum);
                if (!StringUtils.isEmpty(redisJson)) {
                    redisUtils.remove(aircode + "carStatus" + carNum);//先删除redis数据
                    //将获取的数据重新更新到redis
                    JSONObject redisjso = JSON.parseObject(redisJson);
                    redisjso.put("carStatus", carStatus);

                    //状态数据全部加入
                    redisjso.putAll(gisJson);

                    redisUtils.set(aircode + "carStatus" + carNum, JSON.toJSONString(redisjso), 600L);//汽车数据
                    String keys = redisUtils.get(aircode + "carStatusKey");//获取汽车数据所有key
					/*if(redisUtils.exists(aircode+"carStatusKey")) {
						redisUtils.remove(aircode+"carStatusKey");//先删除redis汽车key数据
					}
					if(keys.indexOf(carNum)<0){
						keys += carNum+",";
					}*/
                    if (redisUtils.exists(aircode + "carStatusKey")) {
                        redisUtils.remove(aircode + "carStatusKey");//先删除redis汽车key数据
                        if (keys.indexOf(carNum) < 0) {
                            keys += carNum + ",";
                        }
                    } else {
                        keys = carNum;
                    }
                    redisUtils.set(aircode + "carStatusKey", keys, 600L);
                    logger.info("修改车辆状态：当前在redis还存在的车辆有：" + keys);
                    gisJson.put("carStatus", carStatus);
                }


                logger.info(aircode + "  websocket推送给前端车辆状态：" + gisJson.toJSONString());
                //推送到前端
                WebSocketServer.sendInfo(JSON.toJSONString(gisJson), aircode + "carStatusGis");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("车辆状态推送出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
}