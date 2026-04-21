package com.example.contrast_entity.receiver;

import com.example.contrast_entity.service.FlightChangeLogsService;

public class MyReceiver extends Thread {
    private String msg;
    private FlightChangeLogsService flightChangeLogsService;

    public MyReceiver(FlightChangeLogsService flightChangeLogsService, String msg) {
        this.flightChangeLogsService = flightChangeLogsService;
        this.msg = msg;
    }

    @Override
    public void run() {
        String[] split = msg.split("&");
        if (split.length > 2) {
            flightChangeLogsService.saveFlightChangeLogs(split[0], split[1],split[2],split[3]);
        }

    }
}
