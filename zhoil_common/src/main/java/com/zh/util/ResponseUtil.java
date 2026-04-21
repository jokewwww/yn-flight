package com.zh.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);

			response.getWriter().write(JsonHelper.object2str(data).getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
