package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.zuul.SysRequestLog;
import com.zh.dao.mapper.my.SysRequestLogMapper;
import com.zh.service.SysRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRequestLogServiceImpl implements SysRequestLogService {

	@Autowired
	private SysRequestLogMapper mapper;

    @Override
    public int insertLog(SysRequestLog log) {
        String requestBody = log.getRequestBody();
        MyFuelRecpt myFuelRecpt = JSON.parseObject(requestBody, MyFuelRecpt.class);
        if(myFuelRecpt.getFlrcNo() != null && myFuelRecpt.getFlrcNo() != ""
        && myFuelRecpt.getFlrcDate() != null){
            myFuelRecpt.setFlrcSign(null);
            myFuelRecpt.setFlrcSingle(null);
            myFuelRecpt.setFlrcSingleNew(null);
            String s = JSONObject.toJSONString(myFuelRecpt);
            log.setRequestBody(s);
        }
        return mapper.insertLog(log);
    }

}
