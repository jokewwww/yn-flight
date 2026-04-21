package com.zh.filter;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zh.annotation.SystemLog;
import com.zh.bean.login.BaseBean;
import com.zh.util.JsonHelper;

public class MyInterceptors implements HandlerInterceptor {

	private final static Logger log = LoggerFactory.getLogger(MyInterceptors.class);

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		if (!(arg2 instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) arg2;
		SystemLog annotation = handlerMethod.getMethod().getAnnotation(SystemLog.class);
		if (annotation == null) {
			return true;
		}
		String staffId = null;
		String staffName = null;
		if ("GET".equals(request.getMethod())) {
			staffId = request.getParameter("staffId");
			staffName = request.getParameter("staffName");
		} else if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())
				|| "DELETE".equals(request.getMethod())) {
			try {
				Field declaredField = request.getClass().getDeclaredField("body");
				declaredField.setAccessible(true);
				BaseBean bean = (BaseBean) JsonHelper.str2Object(new String((byte[])declaredField.get(request)), BaseBean.class);
				staffId = bean.getLoginUserIn().getStaffId();
				staffName = bean.getLoginUserIn().getStaffName();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			return true;
		}
		log.info("访问机能日志:" + staffId + "\t" + staffName + "\t" + annotation.description());

		return true;
	}
}