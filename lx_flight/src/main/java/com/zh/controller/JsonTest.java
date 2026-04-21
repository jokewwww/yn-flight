package com.zh.controller;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: JsonTest.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2022年03月09日 10:04
 */
public class JsonTest {

    public static void main(String[] args) {
//        String a = "{\n" +
//                "\"test\":[\"{\\\"code\\\":\\\"333\\\",\\\"name\\\":\\\"555\\\"}\",\"{\\\"code\\\":\\\"444\\\",\\\"name\\\":\\\"666\\\"}\"]\n" +
//                "}";

        String a = "{\n" +
                "\"test\":[\n" +
                "{\"code\":\"333\",\"name\":\"555\"},\n" +
                "{\"code\":\"444\",\"name\":\"666\"}]\n" +
                "}";
        Entity entity = JSON.parseObject(a, Entity.class);
        System.out.println(entity.getTest());
        List<EntityJson> test = entity.getTest();

        Map<String, EntityJson> maps =
                test.stream()
                        .collect(Collectors.toMap(EntityJson::getCode, Function.identity(), (key1, key2) -> key2));

        System.out.println(maps);


//        cn.hutool.json.JSONArray objects = JSONUtil.parseArray(a);

//        list=(List<EntityJson>) JSONArray.parseArray(a, EntityJson.class);
//        System.out.println(objects);

    }
}
