package com.zh;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zh.filter.MyInterceptors;

/**
 * 配置拦截器
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptors()).addPathPatterns("/**");
	}
}
