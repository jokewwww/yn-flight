package com.flight.constant;

import com.flight.model.FromUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.websocket.Session;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 共通类
 */
public class Common {

    /**
     * 1 加油员下线（原来是100，改成1了）
     */
//	public final static String PAD_STAFF_OFFLINE = "100";
    public final static String PAD_STAFF_OFFLINE = "1";
    /**
     * 0 加油员上线（原来是102，改成0了）
     */
//	public final static String PAD_STAFF_ONLINE = "101";
    public final static String PAD_STAFF_ONLINE = "0";
    /**
     * 0:人员
     */
    public final static String STAFF = "0";
    /**
     * 1：航班任务
     */
    public final static String TASKFLIGHT = "1";
    /**
     * 300 广播
     */
    public final static String PAD_BROADCAST = "300";
    /**
     * 102 心跳检测
     */
    public final static String PAD_HEART_BEAT = "102";
    /**
     * 103 断网
     */
    public final static String PAD_BROKEN_NETWORK = "103";
    /**
     * 104 加油员或调度员自己下线（告知过期，WS需要断掉）
     */
    public final static String PAD_OR_PC_OFFLINE = "104";
    /**
     * 200 聊天（type）
     */
    public final static String CHAT_TYPE = "200";
    /**
     * 201 调度员群发给加油员（type）
     */
    public final static String PC_TO_ALL_PAD = "201";
    public final static String tokens = "tokens:";
    // 加油员
    public final static String login = "login:";
    // 调度员。
    public final static String pcLogin = "PClogin:";
    public final static String all = ":*";
    public final static String tokensPre = "tokens:";
    private final static Logger log = LoggerFactory.getLogger(Common.class);
    private static Map<String, Session> mapLogin = new ConcurrentHashMap<>();
    // 登录者信息
    private static Map<Session, FromUser> mapSessionUser = new ConcurrentHashMap<>();
    private static Map<String, Session> mapPcLogin = new ConcurrentHashMap<>();

    /**
     * 给调度员发消息
     */
    public static void toManager(StringRedisTemplate sredis, String message, String str) {
        Set<String> keys = sredis.keys(str);
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                try {
                    Session session = Common.getMapPcLogin().get(key.split(":")[1]);
                    if (session != null) {
//						session.getBasicRemote().sendText(message);
                        Common.sendText(session, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Session> getMapLogin() {
        return mapLogin;
    }

    public static Map<String, Session> getMapPcLogin() {
        return mapPcLogin;
    }

    public static Map<Session, FromUser> getMapSessionUser() {
        return mapSessionUser;
    }

    public static void sendText(Session session, String message) {
        synchronized (session) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                } else {
                    log.debug("发送失败 888 === " + message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("发送失败 777 === " + message);
            }
        }
    }
}
