package com.zh.util;

import com.zh.bean.OutMessage;
import com.zh.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发布消息到REDIS
 * 
 * @author 徐陆
 */
public class SendMsg2Redis {

	private final static Logger log = LoggerFactory.getLogger(SendMsg2Redis.class);
	
	/**
	 * 发布消息
	 */
	public static <T> void testDingYue(StringRedisTemplate temp, String to, String flg, String type, T t) {
		OutMessage<T> outMessage = new OutMessage<T>();
		outMessage.setTo(to);     // 要发送给谁（id），这个是必须的。
		outMessage.setFlg(flg);
		outMessage.setType(type);
		outMessage.setContent(t); // 其它字段根据业务需要自定义。
		outMessage.setCurDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		temp.convertAndSend("chat", JsonHelper.object2str(outMessage).getData());
		log.debug("这是往WebSocket中发布消息，消息内容是：" + JsonHelper.object2str(outMessage).getData());
	}

    /**
     * 发布所有消息
     */
    public static <T> void testDingYueToAllType(StringRedisTemplate temp, String flg, String type, T t) {
        Set<String> keys = temp.keys(Constant.PC_LOGIN + "*"+ ":*");
        if (keys == null || keys.size() == 0) {
            return;
        }
        OutMessage<T> outMessage = new OutMessage<T>();
        String collect = keys.stream().map(dto -> {
            String[] split = dto.split(":");
            return split[1];
        }).collect(Collectors.joining(","));
        // 要发送给谁（id），这个是必须的。
        outMessage.setTo(collect);
        outMessage.setFlg(flg);
        outMessage.setType(type);
        outMessage.setContent(t); // 其它字段根据业务需要自定义。
        outMessage.setCurDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        temp.convertAndSend("chatSingle", JsonHelper.object2str(outMessage).getData());
        log.debug("这是往WebSocket中发布所有消息，消息内容是：" + JsonHelper.object2str(outMessage).getData());


    }
}
