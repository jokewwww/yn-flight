package com.zh.util;

import java.text.MessageFormat;

/**
 * 格式化message工具（给页面提示哪个字段错误）
 */
public class MessagesFormatUtil {

	/**
	 * 格式化message
	 * 
	 * @param msg
	 *            被替换的字符串
	 * @param args
	 *            替换的字符串
	 * @return 格式化后的字符串
	 */
	public static String format(String msg, Object... args) {
		if (args == null) {
			return msg;
		}
		return MessageFormat.format(msg, args);
	}
}
