package com.example.contrast_entity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;

/**
 * JSON工具类
 */
public class JsonHelper {

    private static final Logger logger = LogManager.getLogger(JsonHelper.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 把对象转成JSON字符串。
     *
     * @param obj 対象
     * @return JSON字符串
     */
    public static ReturnMsg<String> object2str(Object obj) {
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, null);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(sdf);
            msg.setData(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            msg.setCode(Constant.CODE_ERR);
            msg.setErrInfo("转JSON错误！");
            logger.error("转JSON错误！", e);
        }
        return msg;
    }

    /**
     * JSON字符串转JSON对象
     */
    public static Object str2Object(String str, Class<?> cls) {
        Object retObj = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            mapper.setDateFormat(sdf);
            retObj = mapper.readValue(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return retObj;
    }
}
