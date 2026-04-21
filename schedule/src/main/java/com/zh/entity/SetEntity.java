package com.zh.entity;

import java.io.Serializable;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: SetEntity.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月08日 10:39
 */
public class SetEntity implements Serializable {

    /**
     * 时间
     */
    private String time;

    /**
     * 间隔
     */
    private String intervalTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }
}
