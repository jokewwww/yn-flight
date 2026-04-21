package com.zh.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class ModelAssistant {
	
	/**
	 * 对象source的非空属性值覆盖替换对象target
	 * @param source
	 * @param target
	 * @return
	 */
	public static Object copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
		return target;
	}

	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper wrapper = new BeanWrapperImpl(source);
		return Stream.of(wrapper.getPropertyDescriptors()).map(FeatureDescriptor::getName)
				.filter(property -> StringUtils.isEmpty(wrapper.getPropertyValue(property))).toArray(String[]::new);
	}
}
