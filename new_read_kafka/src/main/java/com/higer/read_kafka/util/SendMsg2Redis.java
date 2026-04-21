package com.higer.read_kafka.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发布消息到REDIS
 * 
 */
public class SendMsg2Redis {

	/**
	 * 发布消息
	 */
	public static <T> void testDingYue(StringRedisTemplate temp, String to, String flg, String type, T t , String date) {
		OutMessage<T> outMessage = new OutMessage<T>();
		outMessage.setTo(to);     // 要发送给谁（id），这个是必须的。
		outMessage.setFlg(flg);
		outMessage.setType(type);
		outMessage.setCurDate(date);
		outMessage.setContent(t); // 其它字段根据业务需要自定义。
		temp.convertAndSend("chat", JsonHelper.object2str(outMessage).getData());
	}
}
