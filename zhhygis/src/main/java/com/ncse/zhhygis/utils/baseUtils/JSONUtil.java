package com.ncse.zhhygis.utils.baseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JSONUtil {

    public static HashMap<String, Object> reflect(JSONObject json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Set keys = json.keySet();
        for (Object key : keys) {
            Object o = json.get(key);
            if (o instanceof JSONArray)
                map.put((String) key, reflect((JSONArray) o));
            else if (o instanceof JSONObject)
                map.put((String) key, reflect((JSONObject) o));
            else
                map.put((String) key, o);
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     * @see JSONHelper#reflect(JSONObject)
     */
    public static List<Object> reflect(JSONArray json) {

        List<Object> list = new ArrayList<Object>();
        for (Object o : json) {
            if (o instanceof JSONArray)
                list.add(reflect((JSONArray) o));
            else if (o instanceof JSONObject)
                list.add(reflect((JSONObject) o));
            else
                list.add(o);
        }
        return list;
    }

    public static void main(String[] args) {
        JSONObject a = new JSONObject();
        a.put("a", "1");
        JSONObject b = new JSONObject();
        b.put("a", "3");
        b.putAll(a);
        JSONObject c = new JSONObject();
        b.putAll(c);
        System.out.println(b);
    }

}
