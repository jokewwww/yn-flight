package cn.iwen.frame.dao.tools.biz.impl;

import org.apache.commons.lang3.StringUtils;

public class ToolsUtils {

	// 名字转换
	public static String changeName(String srcName, boolean flag) {
		if (StringUtils.isEmpty(srcName))
			return srcName;
		StringBuilder strBuild = new StringBuilder();
		for (String name : srcName.toLowerCase().split("_")) {
			if (flag) {// 首单词中的首字母，不需要转换大写 如 类属性
				flag = false;
				strBuild.append(name);
				continue;
			}
			strBuild.append(captureName(name));
		}
		return strBuild.toString();
	}
			
	// 首字母小写
	public static String firstToLowerCase(String name) {
		char[] cs = name.toCharArray();
		if (Character.isUpperCase(cs[0]))
			cs[0] += 32;
		return String.valueOf(cs);
	}
		
	// 首字母大写,且当第二个字母为小写时。Yuanwen 20150719 20:58
	public static String captureName(String name) {
		if(StringUtils.isEmpty(name)) return "";
		char[] cs = name.toCharArray();
		int strlen = cs.length;
		if (strlen == 0)
			return name;
		else if (1 == strlen) {
			if (Character.isLowerCase(cs[0]))
				cs[0] -= 32;
		} else {
			if (Character.isLowerCase(cs[0]) && Character.isLowerCase(cs[1]))
				cs[0] -= 32;
		}
		return String.valueOf(cs);
	}
		
}
