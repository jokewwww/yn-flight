package cn.iwen.frame.dao.tools.biz;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.iwen.frame.dao.tools.bean.ConvertInfo;

public interface IAutoToolsBiz  {

	int convert(List<ConvertInfo> infolist);
	
	List<String> getTables();
	
	/*
	 * 提取action中的方法
	 * */
	void extractMethod(Class<?> actionCls);
	
	//查询所有action方法
	JSONObject getWebMethods();
	
}
