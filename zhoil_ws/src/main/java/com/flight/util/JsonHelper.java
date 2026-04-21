package com.flight.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    /**
     * 把对象转成JSON字符串。
     */
    public static String object2str(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON字符串转JSON对象
     */
    public static <T> T str2Object(String str, Class<T> cls) {
        T t = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            t = mapper.readValue(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
