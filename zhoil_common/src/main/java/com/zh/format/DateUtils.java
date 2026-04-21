package com.zh.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.zh.annotation.MsgList;
import com.zh.bean.ReturnMsg;
import com.zh.constant.Constant;
import com.zh.exception.CustomException;
import com.zh.util.MessagesFormatUtil;

/**
 * 日期转换
 */
public class DateUtils {

	private static final Logger logger = LogManager.getLogger(DateUtils.class);

	public static Date format(JsonParser jp, DeserializationContext ctxt, String str) throws IOException {
		if (jp == null || jp.getText() == null||"".equals(jp.getText())) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(str);
		String date = jp.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {

			Map<String, Object> errMap = new HashMap<String, Object>();
			ReturnMsg returnMsg = ReturnMsg.getInstance(Constant.CODE_ERR, "输入的日期格式应该是【" + str + "】！", errMap);

			try {
				Field parser = DeserializationContext.class.getDeclaredField("_parser");
				parser.setAccessible(true);
				UTF8StreamJsonParser object2 = (UTF8StreamJsonParser) parser.get(ctxt);
				Field field = object2.getCurrentValue().getClass().getDeclaredField(object2.getCurrentName());
				MsgList msgList = field.getAnnotation(MsgList.class);

				String[] msgs = null;
				if (msgList != null) {
					msgs = msgList.msgs();
				}
				if (msgs != null && msgs.length > 0) {
					errMap.put(jp.getCurrentName(), MessagesFormatUtil.format("{0}:输入的日期格式应该是【" + str + "】！", msgs));
				} else {
					errMap.put(jp.getCurrentName(), "输入的日期格式应该是【" + str + "】！");
				}
			} catch (Exception e1) {
				logger.error("请联系管理员！", e);
				returnMsg.setErrInfo("请联系管理员！");
				throw new CustomException(returnMsg);
			}
			throw new CustomException(returnMsg);
		}
	}
}
