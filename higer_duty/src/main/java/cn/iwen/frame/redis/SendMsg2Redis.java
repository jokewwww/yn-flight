package cn.iwen.frame.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发布消息到REDIS
 * 
 * @author 徐陆
 */
public class SendMsg2Redis {

	private final static Log log = LogFactory.getLog(SendMsg2Redis.class);
	
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
		String msg = JSON.toJSONString(outMessage);
		temp.convertAndSend("chat",msg);
		log.debug("---" + msg);
	}
}
