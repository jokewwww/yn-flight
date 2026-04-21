package com.zh.format;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zh.constant.Constant;

/**
 * 日期转换
 */
public class YYYYMMDD_HHMMSS extends JsonDeserializer<Date> {

	/**
	 * 转换格式为"yyyy-MM-dd"的日期类型
	 */
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return DateUtils.format(jp, ctxt, Constant.YYYYMMDD_HHMMSS);
	}
}
