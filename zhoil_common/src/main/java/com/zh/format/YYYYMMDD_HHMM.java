package com.zh.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zh.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换
 */
public class YYYYMMDD_HHMM extends JsonDeserializer<Date> {

	/**
	 * 转换格式为"yyyy-MM-dd"的日期类型
	 */
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			if (jp == null || jp.getText() == null||"".equals(jp.getText())) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String date = jp.getText();
			if(StringUtils.isNotEmpty(date)){
				if(date.length() > 16 ){
					return  format.parse(date);
				}else{
					return  format1.parse(date);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
