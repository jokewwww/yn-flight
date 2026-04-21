package com.zh.controller;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: EntityJson.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2022年03月09日 10:12
 */
public class EntityJson {

    public String name;
    public String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "EntityJson{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
