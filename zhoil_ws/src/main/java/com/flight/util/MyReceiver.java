package com.flight.util;

import com.flight.service.WebSocketService;

public class MyReceiver implements Runnable {
    private String msg;
    private WebSocketService webSocketService;

    public MyReceiver(WebSocketService webSocketService, String msg) {
        this.webSocketService = webSocketService;
        this.msg = msg;
    }

    @Override
    public void run() {
        webSocketService.sendInfo(msg);
    }
}
