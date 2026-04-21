package com.zh.bean.flight;

public class TaskAndRecpt {

    private MyTask tTask;

    private MyFuelRecpt tFuelRecpt;

    public MyTask gettTask() {
        return tTask;
    }

    public void settTask(MyTask tTask) {
        this.tTask = tTask;
    }

    public MyFuelRecpt gettFuelRecpt() {
        return tFuelRecpt;
    }

    public void settFuelRecpt(MyFuelRecpt tFuelRecpt) {
        this.tFuelRecpt = tFuelRecpt;
    }
}
