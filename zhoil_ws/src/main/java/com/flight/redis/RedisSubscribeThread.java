package com.flight.redis;

import com.flight.constant.Common;
import com.flight.model.FromUser;
import com.flight.model.OutMessage;
import com.flight.model.User;
import com.flight.service.WebSocketService;
import com.flight.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 需要通知前端tkn过期
 */
@Component
public class RedisSubscribeThread implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void run(String... strings) throws Exception {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.pSubscribe(new MessageListener() {
                        @Override
                        public void onMessage(Message message, byte[] pattern) {
                            try {
                                String tokenKey = message.toString();
                                if (!tokenKey.startsWith(Common.tokens)) {
                                    return;
                                }
                                FromUser fUser = null;
                                Session session = null;
                                tokenKey = tokenKey.replace(Common.tokens, "");
                                synchronized (Common.getMapSessionUser()) {
                                    for (Entry<Session, FromUser> en : Common.getMapSessionUser().entrySet()) {
                                        if (tokenKey.equals(en.getValue().getUuid())) {
                                            fUser = en.getValue();
                                            session = en.getKey();
                                            break;
                                        }
                                    }
                                }
                                if (fUser == null) {
                                    return;
                                }
                                if (session == null) {
                                    return;
                                }

                                String loginKey = null;
                                if (fUser.isFlg()) {
                                    loginKey = Common.login + fUser.getFromUserId();
                                } else {
                                    loginKey = Common.pcLogin + fUser.getFromUserId();
                                }

                                if (redisTemplate.hasKey(Common.tokens + tokenKey)) {
                                    return;
                                }

                                try {
                                    redisTemplate.delete(loginKey);
                                } catch (Exception e) {
                                }
                                int lastIndexOf = fUser.getLoginKey().lastIndexOf(":");
                                Set<String> keys = redisTemplate.keys(fUser.getLoginKey().substring(0, lastIndexOf) + ":*");
                                if (keys != null && keys.size() > 0) {
                                    try {
//									redisTemplate.delete(keys);
                                        for (String sKey : keys) {
                                            if (sKey.replace(":", "").length() + 4 == sKey.length()
                                                    && (sKey.endsWith(":0") || sKey.endsWith(":1") || sKey.endsWith(":103") || sKey.endsWith(":104"))) {
                                                redisTemplate.delete(sKey);
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                OutMessage<User> outMsg = new OutMessage<User>(null, Common.STAFF, Common.PAD_OR_PC_OFFLINE, null);
                                webSocketService.noticeLogout(JsonHelper.object2str(outMsg), fUser.getFromUserId());

                                session.close(new CloseReason(CloseReason.CloseCodes.getCloseCode(1000), "后台关闭2"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "*@0*".getBytes());
                    return null;
                }
            });
        } catch (Exception e) {
        }
    }
}