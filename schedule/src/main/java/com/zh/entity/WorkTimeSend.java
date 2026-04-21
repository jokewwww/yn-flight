package com.zh.entity;

import java.io.Serializable;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: WorkTime.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月06日 16:04
 */
public class WorkTimeSend implements Serializable {

    /**
     *
     */
    private WorkTime workTime;

    public WorkTimeSend(WorkTime workTime) {
        this.workTime = workTime;
    }

    public WorkTime getWorkTime() {
        return workTime;
    }

    public void setWorkTime(WorkTime workTime) {
        this.workTime = workTime;
    }
}
