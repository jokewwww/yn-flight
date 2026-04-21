package com.flight.service;

import com.flight.constant.Common;
import com.flight.model.OutMessage;
import com.flight.model.User;
import com.flight.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Set;

/**
 * 心跳
 */
@Component
@EnableScheduling
public class HeartBeat {

    private final static Logger log = LoggerFactory.getLogger(HeartBeat.class);

    //@Scheduled(fixedRate = 10000)
    public static void heartBeat() {
        log.debug("pad 和 pc数：========== " + Common.getMapSessionUser().size());
        log.debug("pad数：========== " + Common.getMapLogin().size());
        log.debug("pc数：========== " + Common.getMapPcLogin().size());

        Session session = null;
        OutMessage<User> outMsg = null;
        synchronized (Common.getMapLogin()) {
            Set<String> keySet = Common.getMapLogin().keySet();
            if (keySet != null) {
                for (String key : keySet) {
                    try {
                        session = Common.getMapLogin().get(key);
                        if (session != null) {
                            outMsg = new OutMessage<User>(key, Common.STAFF, Common.PAD_HEART_BEAT, null);
//						session.getBasicRemote().sendText(JsonHelper.object2str(outMsg));
                            Common.sendText(session, JsonHelper.object2str(outMsg));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        synchronized (Common.getMapPcLogin()) {
            Set<String> keySetPC = Common.getMapPcLogin().keySet();
            if (keySetPC != null) {
                for (String key : keySetPC) {
                    try {
                        session = Common.getMapPcLogin().get(key);
                        if (session != null) {
                            outMsg = new OutMessage<User>(key, Common.STAFF, Common.PAD_HEART_BEAT, null);
//						session.getBasicRemote().sendText(JsonHelper.object2str(outMsg));
                            Common.sendText(session, JsonHelper.object2str(outMsg));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
