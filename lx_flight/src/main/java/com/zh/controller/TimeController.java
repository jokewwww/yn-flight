package com.zh.controller;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.zh.bean.ReturnMsg;
import com.zh.constant.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @classname: TimeController
 * @author: zhaojiacan
 * @description: 时间相关接口
 * @date: 2024/7/2 13:55
 * @version:1.0
 */
@RestController
@RequestMapping("/time")
public class TimeController {

    /**
     * 功能描述：获取当前时间
     *
     * @param
     * @return com.zh.bean.ReturnMsg<java.util.Map<java.lang.String,java.lang.Object>>
     * @author zhaojiacan
     * @date 2024/7/2
     */

    @RequestMapping("/current")
    public ReturnMsg<Map<String, Object>> getCurrentTime() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("datatime", DateUtil.now());
        data.put("timestamp", DateUtil.date().getTime());
        return new ReturnMsg<>(Constant.CODE_OK, null, data);
    }
}
