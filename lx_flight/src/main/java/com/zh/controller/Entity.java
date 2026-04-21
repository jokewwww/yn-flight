package com.zh.controller;

import java.util.List;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: EntityJson.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2022年03月09日 10:12
 */
public class Entity {

    public List<EntityJson> test;

    public List<EntityJson> getTest() {
        return test;
    }

    public void setTest(List<EntityJson> test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "test=" + test +
                '}';
    }
}
