package com.higer.higerservice.service;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.component.KafkaService;
import com.higer.higerservice.component.MyBootListener;
import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.entity.oilpro.ReturnMsg;
import com.higer.higerservice.redis.RedisService;
import com.higer.higerservice.util.DateUtil;
import com.higer.higerservice.util.HttpUtils;
import com.higer.higerservice.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 15:56
 * @Description:
 */
@Service
public class CommInterfaceService {
    //redis 超时时间 单位秒
    private static final long TIME_OUT = 60 * 15;
    @Autowired
    private ARecordsService aRecordsService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private RedisService redisService;

    public ResponseObject doPost(String url, byte[] sendData, Integer id) {
        String data = "";
        ARecords aRecords = new ARecords();
        int errorState = 1;
        try {
            data = new String(sendData, "utf-8");
            if (StringUtils.isEmpty(data)) {
                return ResponseObject.error("byte转化错误", 1);
            }
            String msg = HttpUtils.postBody(url, data, "application/json");
            ReturnMsg returnMsg = JSON.parseObject(msg, ReturnMsg.class);
            if ("0".equals(returnMsg.getCode())) {
                if (null != id) {
                    aRecordsService.delete(id);
                }
                return ResponseObject.success(msg.getBytes(Charset.forName("utf8")), msg, "success");
            }
            aRecords.setErrorMsg(returnMsg.getErrInfo().toString());
            aRecords.setFunc("post");
            aRecords.setDate(data);
            aRecords.setInsertDate(DateUtil.getCurrentDateStr());
            aRecords.setState(1);
        } catch (Exception e) {
            aRecords.setErrorMsg(e.getMessage());
            aRecords.setFunc("post");
            aRecords.setDate(data);
            aRecords.setInsertDate(DateUtil.getCurrentDateStr());
            aRecords.setState(1);
            if (e instanceof TimeoutException) {
                errorState = 500;
                aRecords.setErrorMsg("500" + e.getMessage());
            }
            e.printStackTrace();
        }
        if (null == id) {
            aRecordsService.save(aRecords);
        }
        MyBootListener.notifyPost();
        return ResponseObject.error("请求超时", errorState);
    }

    //返回0表示成功 其他为失败 500为访问超时
    public ResponseObject doWriteKafKa(String topic, String key, String msg, Integer lock) {
        ResponseObject send = null;
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(msg)) {
            return ResponseObject.error("参数异常", 1);
        }
        try {
            //更改业务 优先发送 实时数据
            send = kafkaService.send(topic, key, msg, lock);
            return send;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("系统异常", 1);
    }

    // 返回0表示成功 其他为失败 500为访问超时
    //timeOut表示超时时间单位秒 -1表示永部超时   strRes:该参数是终端传输过来的原数据,如果发送失败储存库中
    public ResponseObject doWriteRedis(String key, String value, Long timeOut, String strRes) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return ResponseObject.error("参数异常", 1);
        }
        try {
            // 更改业务 优先发送正常数据 同时唤醒线程 发送错误数据
            redisService.setStr(key, value);
           /* if (timeOut.longValue() == -1) {
            } else {
                redisService.setStrToTime(key, value, timeOut);
            }*/
            Page<ARecords> all = aRecordsService.findByFuncAndState("post", 1, new PageRequest(0, 10));
            if (all != null && all.getContent().size() > 0) {
                /*ARecords aRecords = new ARecords();
                aRecords.setFunc("redis");
                aRecords.setDate(value);
                aRecords.setErrorMsg("优先发送错误数据");
                aRecords.setInsertDate(DateUtil.getCurrentDateStr());
                aRecords.setState(1);
                aRecordsService.save(aRecords);*/
                //如果存在数据就唤醒
                MyBootListener.notifyKafka();
            }
            return ResponseObject.success(null, "", "success");
        } catch (Exception e) {
            ARecords aRecords = new ARecords();
            aRecords.setFunc("redis");
            aRecords.setDate(strRes);
            aRecords.setErrorMsg(e.getMessage());
            aRecords.setInsertDate(DateUtil.getCurrentDateStr());
            aRecords.setState(1);
            if (e instanceof TimeoutException) {
                aRecords.setErrorMsg("500" + e.getMessage());
                return ResponseObject.error("访问超时", 500);
            }
            aRecordsService.save(aRecords);
            e.printStackTrace();
        }
        MyBootListener.notifyKafka();
        return ResponseObject.error("系统异常", 1);
    }

    // 返回0表示成功 其他为失败 500为访问超时 404表示数据不存在
    public ResponseObject doReadRedis(String key) {
        if (StringUtils.isEmpty(key)) {
            return ResponseObject.error("key不可为空", 1);
        }
        String str = "";
        try {
            str = redisService.getStr(key);
            if (StringUtils.isEmpty(str)) {
                return ResponseObject.error("数据不存在", 404);
            }
            return ResponseObject.success(str.getBytes(Charset.forName("utf8")), str, "success");
        } catch (Exception e) {
            if (e instanceof TimeoutException) {
                return ResponseObject.error("访问超时", 500);
            }
            e.printStackTrace();
        }
        return ResponseObject.error("代码错误", 1);
    }

}
