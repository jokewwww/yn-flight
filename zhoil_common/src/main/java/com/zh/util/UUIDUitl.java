package com.zh.util;

import java.util.UUID;

/**
 * UUID工具类
 */
public class UUIDUitl {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
