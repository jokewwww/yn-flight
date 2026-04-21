package com.flight.redis;

import com.flight.service.WebSocketService;
import com.flight.util.MyReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订阅者
 */
@Component
public class MessageReceiver {

    private final static Logger log = LoggerFactory.getLogger(MessageReceiver.class);
    private final static ExecutorService executor = Executors.newFixedThreadPool(300);
    private final static ExecutorService executorSingle = Executors.newSingleThreadExecutor();
    @Autowired
    private WebSocketService webSocketService;

    public void receiveMessageSingle(String s) {
        executor.submit(new MyReceiver(webSocketService, s));
    }

    public void receiveMessage(String s) {
        log.debug("from ------------------------------------------- " + s);
        executor.submit(new MyReceiver(webSocketService, s));
    }

    public void receiveMessageLogout(String s) {
        log.debug("from =========================================== " + s);
        webSocketService.logout(s);
    }

    public void receiveChartMsg(String s) {
        log.debug("from ########################################### " + s);
        webSocketService.receiveChartMsg(s);
    }

    public void noticeManager(String s) {
        log.debug("from ___________________________________________ " + s);
        webSocketService.noticeManager(s);
    }

    public void redisKeyevent(String s) {
        log.debug("from redisKeyevent -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-" + s);
        webSocketService.redisKeyevent(s);
    }


}
