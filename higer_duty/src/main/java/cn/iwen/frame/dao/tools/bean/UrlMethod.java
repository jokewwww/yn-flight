package cn.iwen.frame.dao.tools.bean;

import java.util.List;

/*
 * 测试地址对应action方法
 * url ----> method
 * */
public class UrlMethod {

	private final static String METHOD_POSTFIX = ".do";
	
	private String pktName;
	
	private String clsName;
	
	private String methodName;
	
	/*
	 * 方法类型：
	 * POST，GET，PUT，DELETE
	 * */
	private String methodType;
	
	private String url;
	
	private String params;

	public String getPktName() {
		return pktName;
	}

	public void setPktName(String pktName) {
		this.pktName = pktName;
	}

	public String getClsName() {
		return clsName;
	}

	public void setClsName(String clsName) {
		this.clsName = clsName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if(null == url) return;
		this.url = url + METHOD_POSTFIX;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	
	
}
