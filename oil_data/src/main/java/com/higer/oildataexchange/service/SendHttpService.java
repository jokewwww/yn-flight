package com.higer.oildataexchange.service;

import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.entity.HttpSendLog;
import com.higer.oildataexchange.entity.acdm.FligtHttpRespone;
import com.higer.oildataexchange.repository.HttpSendLogRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SendHttpService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpSendLogRepository httpSendLogRepository;

    public String sendHttpPost(@NonNull String url, @NonNull String xml) {
        log.debug("发送XML，URL：\r\n{},\r\n发送信息：\r\n{}", url, xml);
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.TEXT_XML);
//        将请求头部和参数合成一个请求
        //请求体
        HttpEntity<String> httpEntity = new HttpEntity<>(xml, headers);
        String receive = restTemplate.postForObject(url, httpEntity, String.class);
        HttpSendLog httpSendLog = new HttpSendLog(url, xml);
        //TODO 接收数据太大
        //httpSendLog.setResponseData(receive);
        httpSendLogRepository.save(httpSendLog);
        log.debug("接收数据：\r\n{}", receive);
        return receive;

    }

    public String sendAcdmHttpPost(@NonNull String url, @NonNull String params) {
        log.info("发送ACDM信息，URL：\r\n{},\r\n发送信息：\r\n{}", url, params);
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.add("accessToken", "1086b25b-08f4-4d3d-bcc0-0438c3f65c63");
        //请求体
        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        String receive = restTemplate.postForObject(url, httpEntity, String.class);
        HttpSendLog httpSendLog = new HttpSendLog(url, params);
        httpSendLogRepository.save(httpSendLog);
        log.info("ACDM-接收数据：\r\n{}", receive);
        FligtHttpRespone respone = JSONObject.parseObject(receive, FligtHttpRespone.class);
        //返回异常后，重新发送
        if (respone != null && "10000".equals(respone.getCode())) {
            sendAcdmHttpPost(url, params);
        }
        return receive;

    }

}
