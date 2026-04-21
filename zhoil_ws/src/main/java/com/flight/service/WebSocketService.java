package com.flight.service;


import com.flight.bean.login.MyStaff;
import com.flight.config.SpringContextHelper;
import com.flight.constant.Common;
import com.flight.model.FromUser;
import com.flight.model.OutMessage;
import com.flight.model.User;
import com.flight.prop.Prop;
import com.flight.util.JsonHelper;
import com.zh.bean.login.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 推送消息
 */
@ServerEndpoint(value = "/websocket/{fromUserId}/{token}", configurator = SpringContextHelper.class)
@Component
public class WebSocketService {

    private final static Logger log = LoggerFactory.getLogger(WebSocketService.class);

    public static RestTemplate restTemplate;

    public static Prop prop;
    public static StringRedisTemplate staRedis;
    @Autowired
    private StringRedisTemplate sredis;
    @Autowired
    private TokenService tokenServie;
    @Autowired
    private TokenService tokenService;

    public static void redisKeyevent(String message) {
        try {
            if (!StringUtils.isEmpty(message)) {
                String[] split = message.split(":");
                if (split.length == 2) {
                    HttpHeaders headersb = new HttpHeaders();
                    MediaType type3 = MediaType.parseMediaType("application/json; charset=UTF-8");
                    headersb.setContentType(type3);
                    headersb.add("Accept", MediaType.APPLICATION_JSON.toString());
                    MyStaff tasks = new MyStaff();
                    tasks.setStaffId(split[1]);
                    HttpEntity<String> formEntityv = new HttpEntity<String>(JsonHelper.object2str(tasks), headersb);
                    //跨库调用方法获取参数
                    MyStaff staffinfo = restTemplate.postForObject("http://" + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/deleteToken", formEntityv, MyStaff.class);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeSession(Session session, String msg) {
        try {
            if (session != null) {
//				session.close();
                log.debug("session id " + session.getId() + " be close" + "----msg" + msg);
                session.close(new CloseReason(CloseReason.CloseCodes.getCloseCode(1000), "后台关闭" + msg));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toCloseSession(Session session) {
        try {
            if (session != null) {
//				session.close();
                log.debug("session id " + session.getId() + " be close");
                session.close(new CloseReason(CloseReason.CloseCodes.getCloseCode(1000), "onClean主动后台关闭"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给同一机场同一区域的所有人广播
     */
    private static boolean broadcast(String message, OutMessage<?> out) {
        if (Common.PAD_BROADCAST.equals(out.getType())) {
            if (out.getTo() == null) {
                return false;
            }

            Set<String> keys = staRedis.keys(Common.pcLogin + out.getTo() + ":*");
            if (keys == null || keys.size() == 0) {
                return false;
            }
            String next = keys.iterator().next();
            String[] split = next.split(":");
            broadcast(message, split, out, Common.login);
            broadcast(message, split, out, Common.pcLogin);
            return false;
        }
        return true;
    }

    /**
     * 广播
     */
    private static void broadcast(String message, String[] split, OutMessage<?> out, String pre) {
        Set<String> loginKeys;
        Session sin;
        String to;
        loginKeys = staRedis.keys(pre + "*:" + split[2] + ":" + split[3] + ":" + Common.PAD_STAFF_ONLINE);
        if (loginKeys != null && loginKeys.size() > 0) {
            for (String loginKey : loginKeys) {
                to = loginKey.split(":")[1];
                sin = Common.getMapLogin().get(to);
                if (sin == null) {
                    sin = Common.getMapPcLogin().get(to);
                    if (sin == null) {
                        continue;
                    }
                }
                out.setTo(to);
                message = JsonHelper.object2str(out);
                log.debug("广播 ------------------- " + message);
                try {
//					sin.getBasicRemote().sendText(message);
                    Common.sendText(sin, message);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 退出
     */
    public static void logout(String message) {
        synchronized (WebSocketService.class) {
            Session session = null;
            OutMessage<?> out = JsonHelper.str2Object(message, OutMessage.class);

            try {
                if (Common.getMapLogin().get(out.getTo()) == null && Common.getMapPcLogin().get(out.getTo()) == null) {
                    log.debug("不在此 +++++++++++++++++++++222 " + message);
                    return;
                }

                // 加油员
                session = Common.getMapLogin().get(out.getTo());
                if (session == null) {
                    // 调度员
                    session = Common.getMapPcLogin().get(out.getTo());
                }
                if (session == null) {
                    return;
                }
                FromUser fromUser = Common.getMapSessionUser().get(session);
                fromUser.setLogout(true);
                out.setFlg(Common.STAFF);
                out.setType(Common.PAD_OR_PC_OFFLINE);
                message = JsonHelper.object2str(out);
                if (!session.isOpen()) {
                    log.debug("发送失败 222 ================ " + message);
//				closeSession(session);
                    return;
                }
//			session.getBasicRemote().sendText(message);
                Common.sendText(session, message);
                log.debug(out.getTo() + " ======================== 退出了。。。。");

                closeSession(session, "logout 527");
            } catch (Exception e) {
                if (session != null) {
                    closeSession(session, "logout 530");
                }
            }
        }

    }

    /**
     * 聊天
     */
    public static void receiveChartMsg(String message) {
        String to = null;

        try {

            OutMessage<?> out = JsonHelper.str2Object(message, OutMessage.class);
            to = out.getTo();

            Session session = Common.getMapLogin().get(to);
            if (session == null) {
                session = Common.getMapPcLogin().get(to);
            }
            if (session == null) {
                log.debug("不存在 ************* 111 " + to);
                return;
            }
//			session.getBasicRemote().sendText(message);
            Common.sendText(session, message);
        } catch (Exception e1) {
        }

    }

    /**
     * 通知调度员
     */
    public static void noticeManager(String message) {
        String to = null;

        try {

            OutMessage<?> out = JsonHelper.str2Object(message, OutMessage.class);
            to = out.getTo();

            Session session = Common.getMapPcLogin().get(to);
            if (session == null) {
                log.debug("不存在 ************* 222 " + to);
                return;
            }
//			session.getBasicRemote().sendText(message);
            Common.sendText(session, message);
        } catch (Exception e1) {
        }

    }

    /**
     * 连接连接时
     */
    @OnOpen
    public void onOpen(@PathParam("fromUserId") String fromUserId, @PathParam("token") String token, Session session) {
        log.info("连接超时专用" + fromUserId + "---" + token);

        try {
            synchronized (WebSocketService.class) {
                log.debug("open fromUserId sessionid 111 ========================== " + fromUserId + " " + session.getId());
                // 判断是否登录。
                if (!isLogin(fromUserId, token, session)) {
                    return;
                }

                log.debug("open fromUserId sessionid 222 ========================== " + fromUserId + " " + session.getId());
                Set<String> keys = null;
                FromUser fUser = new FromUser(fromUserId, false, tokenServie.getUUIDFromJWT(token));

                // clear,断线后再次登录时。
                Session sin2 = Common.getMapLogin().get(fromUserId);
                if (sin2 == null) {
                    sin2 = Common.getMapPcLogin().get(fromUserId);
                }

                if (sin2 != null) {
                    log.debug("session id:" + sin2.getId() + " is repeat login,be remove");
                    Common.getMapPcLogin().remove(fromUserId);
                    Common.getMapLogin().remove(fromUserId);
                    Common.getMapSessionUser().remove(sin2);
                    closeSession(sin2, "onOpen -> 111");
                }

                // 设置名字
                LoginUser loginUser = tokenService.getLoginUser(token);
                fUser.setStaffName(loginUser.getStaffName());
                fUser.setSfvhStaffStatus("0");

                // 调度员上线
                if (sredis.keys(Common.pcLogin + fromUserId + Common.all).size() > 0) {
                    Common.getMapPcLogin().put(fromUserId, session);
                    log.info("---------");
                    log.info("当前登录用户是:" + fromUserId);
                    log.info("MapPcLogin的size:" + Common.getMapPcLogin().entrySet().size());
                    log.info("MapPcLogin里面有:" + String.join(",", Common.getMapPcLogin().keySet()));
                    log.info("----------");
                    keys = sredis.keys(Common.pcLogin + fromUserId + Common.all);
                    setCode(session, fUser, keys);

                } else if (sredis.keys(Common.login + fromUserId + Common.all).size() > 0) {
                    keys = sredis.keys(Common.login + fromUserId + Common.all);
                    setCode(session, fUser, keys);

                    fUser.setFlg(true);
                    Common.getMapLogin().put(fromUserId, session);

                    // 通知调度员
                    Map<String, Object> map = new HashMap<>();
                    map.put("staff", new User(fromUserId, fUser.getStaffName(), "0", 0));
                    OutMessage<Map<String, Object>> out = new OutMessage<Map<String, Object>>(null, Common.STAFF, "100", map);
                    Set<String> keys1 = staRedis.keys(Common.pcLogin + "*:" + fUser.getStaffAirportCode() + ":" + fUser.getStaffAptareaCode() + ":*");
                    String msg;
                    if (keys1 != null && keys1.size() > 0) {
                        for (String key : keys1) {
                            out.setTo(key.split(":")[1]);
                            msg = JsonHelper.object2str(out);
                            log.debug("msg___________________222 " + msg);
                            try {
                                staRedis.convertAndSend("noticeManager", msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    closeSession(sin2, "onopen ->156");
                }
                // 修改状态
                try {
                    String next = keys.iterator().next();
                    int lastIndexOf = next.lastIndexOf(":");
                    sredis.rename(next, next.substring(0, lastIndexOf + 1) + Common.PAD_STAFF_ONLINE);
                } catch (Exception e) {
                    log.error("keys is null -> 暂时位置 keys的作用");
                }

                // 去重（重复登录时）
                synchronized (Common.getMapSessionUser()) {
                    Iterator<Entry<Session, FromUser>> iterator = Common.getMapSessionUser().entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<Session, FromUser> next2 = iterator.next();
                        if (fromUserId.equals(next2.getValue().getFromUserId())) {
                            Common.getMapSessionUser().remove(next2.getKey());
//				iterator.remove();
                            break;
                        }
                    }
                    Common.getMapSessionUser().put(session, fUser);
                }
            }
            log.info(fromUserId + "open 结束");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isLogin(String fromUserId, String token, Session session) {
        try {
            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(fromUserId)) {
                kickOut(fromUserId, session);
                closeSession(session, "isLogin -> 192");
                return false;
            }
            LoginUser loginUser = tokenServie.getLoginUser(token);
            if (loginUser == null || !fromUserId.equals(loginUser.getStaffId())) {
                kickOut(fromUserId, session);
                closeSession(session, "isLogin -> 198");
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            closeSession(session, "isLogin -> 203");
            return false;
        }
        return true;
    }

    private void kickOut(String fromUserId, Session session) {
        OutMessage<String> out = new OutMessage<>();
        out.setTo(fromUserId);
        out.setFlg(Common.STAFF);
        out.setType(Common.PAD_STAFF_OFFLINE);
        if (sredis.hasKey(Common.pcLogin + fromUserId)) {
            out.setType(Common.PAD_OR_PC_OFFLINE);
        }
        try {
            String object2str = JsonHelper.object2str(out);
            if (!session.isOpen()) {
                log.debug("发送失败 444 ================ " + object2str);
//				closeSession(session);
                return;
            }
            log.debug("你需要重新登录。。。。。------------------------ " + object2str);
//			session.getBasicRemote().sendText(object2str);
            Common.sendText(session, object2str);
        } catch (Exception e) {
            log.debug("session id:" + session.getId() + " is on kickOut error,errormsg is  " + e.getMessage());
            closeSession(session, "kickOut -> 229");
        }
    }

    private void setCode(Session session, FromUser fUser, Set<String> keys) {
        if (keys == null || keys.size() == 0) {
            log.debug("session id:" + session.getId() + " is on error,errormsg is setcode keys is null");
            closeSession(session, "setCode -> 236");
            return;
        }
        String string = keys.iterator().next();
        String[] split = string.split(":");
        // 所属机场代码
        fUser.setStaffAirportCode(split[2]);
        // 所属机场区域代码
        fUser.setStaffAptareaCode(split[3]);
        // loginKey
        fUser.setLoginKey(string);
    }

    /**
     * 连接关闭时
     */
    @OnClose
    public void onClose(Session session) {
        log.debug("session id:" + session.getId() + " is on close");
        removeUser(session);
    }

    /**
     * 删除用户
     */
    private void removeUser(Session session) {
        try {
            synchronized (WebSocketService.class) {
                FromUser fromUser = Common.getMapSessionUser().get(session);
                if (fromUser == null) {
                    log.debug("del sessionid == " + session.getId());
                    Common.getMapSessionUser().remove(session);
                    return;
                }
                log.debug("del sessionid 999 == " + session.getId() + fromUser.getFromUserId());
                String status = Common.PAD_STAFF_OFFLINE;
                //		if (Common.getMapLogin().get(fromUser.getFromUserId()) == null && Common.getMapPcLogin().get(fromUser.getFromUserId()) == null) {
                Integer sfvhStassStatus = 0;
                Integer isAnimate = 0;
                if (fromUser.isLogout()) {
                    //			status = Common.PAD_STAFF_OFFLINE;
                    log.debug(fromUser.getFromUserId() + " =================== 下线了");
                } else {
                    sfvhStassStatus = 1;
                    isAnimate = 1;
                    status = Common.PAD_BROKEN_NETWORK;
                    log.debug(fromUser.getFromUserId() + " =================== 断线了");
                }
                Map<String, Object> map = new HashMap<>();
                map.put("staff", new User(fromUser.getFromUserId(), fromUser.getStaffName(), sfvhStassStatus.toString(), isAnimate));
                OutMessage<Map<String, Object>> outMsg = new OutMessage<Map<String, Object>>(null, Common.STAFF, status, map);
                // 修改状态
                // updateStatus(fromUser, status);
                if (fromUser.isFlg()) {
                    // 通知调度员
                    Set<String> keys1 = staRedis.keys(Common.pcLogin + "*:" + fromUser.getStaffAirportCode() + ":" + fromUser.getStaffAptareaCode() + ":*");
                    if (keys1 == null || keys1.size() == 0) {
                        return;
                    }
                    String msg;
                    for (String key : keys1) {
                        log.info("离线推送---->key--->" + key);
                        outMsg.setTo(key.split(":")[1]);
                        msg = JsonHelper.object2str(outMsg);
                        log.debug("msg___________________333 " + msg);
                        try {
                            staRedis.convertAndSend("noticeManager", msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Common.getMapLogin().remove(fromUser.getFromUserId());
                } else {
                    // 调度员：
                    Common.getMapPcLogin().remove(fromUser.getFromUserId());
                }
                Common.getMapSessionUser().remove(session);
                toCloseSession(session);
            }
        } catch (Exception e) {
            synchronized (Common.getMapSessionUser()) {
                FromUser fromUser = Common.getMapSessionUser().get(session);
                if (fromUser != null) {
                    if (fromUser.isFlg()) {
                        Common.getMapLogin().remove(fromUser.getFromUserId());
                    } else {
                        Common.getMapPcLogin().remove(fromUser.getFromUserId());
                    }
                    Common.getMapSessionUser().remove(session);
                    toCloseSession(session);
                }
            }
            e.printStackTrace();
        }
    }

    private void updateStatus(FromUser fromUser, String status) {
        try {
            int lastIndexOf = fromUser.getLoginKey().lastIndexOf(":");
            Set<String> keys = sredis.keys(fromUser.getLoginKey().substring(0, lastIndexOf) + Common.all);
            String loginKey = fromUser.getLoginKey().substring(0, fromUser.getLoginKey().lastIndexOf(":")) + ":" + status;
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    try {
                        sredis.rename(key, loginKey);
                    } catch (Exception e) {
                    }
                }
            }
            fromUser.setLoginKey(loginKey);
        } catch (Exception e) {
            log.error("状态修改失败，用户【" + fromUser + "】，状态【" + status + "】");
        }
    }

    /**
     * 发生错误时
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("session id:" + session.getId() + " is on error,errormsg is " + error.getMessage());
        //removeUser(session);
        /*synchronized (WebSocketService.class) {
            FromUser fromUser = Common.getMapSessionUser().get(session);
            if (fromUser != null && fromUser.isFlg()) {
                Map<String, Session> mapLogin = Common.getMapLogin();
                if (mapLogin != null) {
                    mapLogin.remove(fromUser.getFromUserId());
                }
                //Common.getMapLogin().put(fromUser.getFromUserId(), null);
            }
        }*/
    }

    /**
     * 发消息
     */
    public void sendInfo(String message) {
        OutMessage<?> out = JsonHelper.str2Object(message, OutMessage.class);
        try {
            if (org.apache.commons.lang3.StringUtils.isBlank(out.getTo())) {
                log.debug("to 为空 ================================== " + message);
                return;
            }

            // 广播
            if (!broadcast(message, out)) {
                return;
            }

            Session session = null;
            String[] tos = out.getTo().split(",");
            String object2str = null;
            for (String to : tos) {
                out.setTo(to);
                // 加油员
                session = Common.getMapLogin().get(to);
                if (session == null) {
                    // 调度员
                    session = Common.getMapPcLogin().get(to);
                }
                if (session == null) {
                    continue;
                }
                object2str = JsonHelper.object2str(out);
                if (!session.isOpen()) {
                    log.debug("发送失败 111 ================ " + object2str);
//					closeSession(session);
                    removeUser(session);
                    continue;
                }
                log.debug("推送消息 ++++++++++++++++++++++++++ == " + object2str);
//				session.getBasicRemote().sendText(object2str);
                Common.sendText(session, object2str);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知前端退出
     */
    public void noticeLogout(String message, String userId) {
        synchronized (WebSocketService.class) {
            log.debug(userId + " -------------------- 退出了。。。");
            Session session = Common.getMapLogin().get(userId);
            if (session == null) {
                session = Common.getMapPcLogin().get(userId);
            }
            if (session == null) {
                return;
            }
            FromUser fromUser = Common.getMapSessionUser().get(session);
            fromUser.setLogout(true);
            fromUser.setTimeout(true);
            try {
                if (!session.isOpen()) {
                    log.debug("发送失败 333 ================ " + message);
//				closeSession(session);
                    removeUser(session);
                    return;
                }
//			session.getBasicRemote().sendText(message);
                Common.sendText(session, message);
            } catch (Exception e) {
                removeUser(session);
            }
        }
    }

    /**
     * 聊天
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        synchronized (WebSocketService.class) {

            OutMessage<?> out = null;
            OutMessage<?> outMe = null;
            try {
                log.debug("message  ####____####____#### == " + message);

                FromUser fromUser = Common.getMapSessionUser().get(session);
                if (fromUser == null) {
                    return;
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(message)
                        && org.apache.commons.lang3.StringUtils.containsIgnoreCase(message, "ping")) {
                    log.info("receive ping message from {}, content is : {}", fromUser.getFromUserId(), message);
                    OutMessage<User> outMsg = new OutMessage<User>(fromUser.getFromUserId(), Common.STAFF, Common.PAD_HEART_BEAT, null);
                    Common.sendText(session, JsonHelper.object2str(outMsg));
                    return;
                }
                // 给自己。
                outMe = JsonHelper.str2Object(message, OutMessage.class);
                outMe.setType(Common.CHAT_TYPE);
                outMe.setTo(fromUser.getFromUserId());
//			session.getBasicRemote().sendText(JsonHelper.object2str(outMe));
                Common.sendText(session, JsonHelper.object2str(outMe));

                out = JsonHelper.str2Object(message, OutMessage.class);
                out.setFlg(fromUser.getFromUserId());

                if (org.apache.commons.lang3.StringUtils.isEmpty(out.getTo())) {
                    // 给所有的加油员
                    if (Common.PC_TO_ALL_PAD.equals(out.getType())) {
                        if (!fromUser.isFlg()) {
                            sendMsgByAirCode(out, fromUser, Common.login);
                            return;
                        }
                    } else {
                        // 给调度员
                        if (fromUser.isFlg()) {
                            sendMsgByAirCode(out, fromUser, Common.pcLogin);
                            return;
                        }
                    }

                } else {
                    // 给加油员
                    if (!fromUser.isFlg()) {
                        out.setType(Common.CHAT_TYPE);
                        staRedis.convertAndSend("chartMsg", JsonHelper.object2str(out));
                        return;
                    }
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 根据机场code发送消息
     *
     * @param out
     * @param fromUser
     * @param pre
     */
    private void sendMsgByAirCode(OutMessage<?> out, FromUser fromUser, String pre) {

        out.setType(Common.CHAT_TYPE);

        Set<String> keys = staRedis.keys(pre + "*:" + fromUser.getStaffAirportCode() + ":" + fromUser.getStaffAptareaCode() + ":*");
        if (keys == null || keys.size() == 0) {
            return;
        }

        String msg;
        for (String key : keys) {
            out.setTo(key.split(":")[1]);
            msg = JsonHelper.object2str(out);
            log.debug("msg___________________111 " + msg);
            try {
                staRedis.convertAndSend("chartMsg", msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//	/**
//	 * 关闭出错连接 并删除对应map缓存
//	 * @param session
//	 */
//	private static void closeErrorConnect(Session session){
//		synchronized (WebSocketService.class){
//			FromUser fromUser=Common.getMapSessionUser().get(session);
//			String fromUserId=fromUser.getFromUserId();
//
//			log.debug("session id:"+session.getId()+" mus be close");
//			Common.getMapPcLogin().remove(fromUserId);
//			Common.getMapLogin().remove(fromUserId);
//			Common.getMapSessionUser().remove(session);
//			closeSession(session);
//		}
//
//	}
}
