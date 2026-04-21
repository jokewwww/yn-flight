package com.zh.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 日期转换
 */
public class Decimal4bit extends JsonSerializer<BigDecimal> {

	/**
	 * 转换格式为"yyyy-MM-dd"的日期类型
	 */

	@Override
	public void serialize(BigDecimal d, JsonGenerator jp, SerializerProvider provider) throws IOException, JsonProcessingException {
		BigDecimal bigDecimal = d.setScale(4, BigDecimal.ROUND_HALF_UP);
		System.out.println("bigDecimal = " + bigDecimal);
		jp.writeObject(bigDecimal);
	}
}
