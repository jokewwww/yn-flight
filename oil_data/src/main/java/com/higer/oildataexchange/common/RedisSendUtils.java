package com.higer.oildataexchange.common;

import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.entity.OutMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

@Slf4j
public class RedisSendUtils {

    /**
     * 发布消息
     */
    public static <T> void testDingYue(StringRedisTemplate temp, String to, String flg, String type, T t) {
        OutMessage<T> outMessage = new OutMessage<T>();
        outMessage.setTo(to);     // 要发送给谁（id），这个是必须的。
        outMessage.setFlg(flg);
        outMessage.setType(type);
        outMessage.setContent(t); // 其它字段根据业务需要自定义。
        outMessage.setCurDate(DateFormatUtils.format(new Date(), Constant.YYYY_MM_DD_HH_MM_SS));
        log.debug("推送信息:{} to {}", JSONObject.toJSONString(outMessage.getContent()), outMessage.getTo());
        temp.convertAndSend("chat", JSONObject.toJSONString(outMessage));
    }
}
