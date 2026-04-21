package com.zh.bean.flight;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/26 14:34
 * @Description:
 */
public class VoTask {

    /**
     * taskFlightNo : 任务号 / 没有值放双引号
     * taskAccTime : 任务开始时间 / 没有值放入双引号
     * taskArriveTime : 任务到位时间 / 没有值放入双引号
     * taskChagStaTime : 加油开始时间 / 没有值放入双引号
     * taskChagEndTime : 加油结束时间 / 没有值放入双引号
     * ffidAlias : 航班别名 格式中字段顺序，flgtAirportCode机场二字码, 航空公司代码,航班号, 进离港标志(A:到港，D:离港), 航班计划时间(或航班日期),国际国内标志5个字段组成 。 / 如果没有值放 双引号 / 如果某个字段没有值放入 0
     * taskState : 任务状态 0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）
     */
    private String taskAirportCode;
    private String taskCarNO;
    private String taskFlightNo;
    private String taskAccTime;
    private String taskArriveTime;
    private String taskChagStaTime;
    private String taskChagEndTime;
    private String ffidAlias;
    private String taskState;

    public String getTaskAirportCode() {
        return taskAirportCode;
    }

    public void setTaskAirportCode(String taskAirportCode) {
        this.taskAirportCode = taskAirportCode;
    }

    public String getTaskCarNO() {
        return taskCarNO;
    }

    public void setTaskCarNO(String taskCarNO) {
        this.taskCarNO = taskCarNO;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskFlightNo() {
        return taskFlightNo;
    }

    public void setTaskFlightNo(String taskFlightNo) {
        this.taskFlightNo = taskFlightNo;
    }

    public String getTaskAccTime() {
        return taskAccTime;
    }

    public void setTaskAccTime(String taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    public String getTaskArriveTime() {
        return taskArriveTime;
    }

    public void setTaskArriveTime(String taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }

    public String getTaskChagStaTime() {
        return taskChagStaTime;
    }

    public void setTaskChagStaTime(String taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    public String getTaskChagEndTime() {
        return taskChagEndTime;
    }

    public void setTaskChagEndTime(String taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    public String getFfidAlias() {
        return ffidAlias;
    }

    public void setFfidAlias(String ffidAlias) {
        this.ffidAlias = ffidAlias;
    }
}
