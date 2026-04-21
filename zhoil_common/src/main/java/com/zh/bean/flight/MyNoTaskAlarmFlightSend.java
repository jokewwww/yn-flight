package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 未分配任务报警的航班
 */
public class MyNoTaskAlarmFlightSend implements Serializable {

	private MyNoTaskAlarmFlight flight;

    public MyNoTaskAlarmFlightSend(MyNoTaskAlarmFlight flight) {
        this.flight = flight;
    }

    public MyNoTaskAlarmFlight getFlight() {
        return flight;
    }

    public void setFlight(MyNoTaskAlarmFlight flight) {
        this.flight = flight;
    }
}