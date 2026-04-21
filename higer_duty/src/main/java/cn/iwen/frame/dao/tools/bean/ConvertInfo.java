package cn.iwen.frame.dao.tools.bean;

import java.util.*;

import org.apache.commons.lang3.StringUtils;


/*
 * 表转换类信息
 */
public class ConvertInfo {

	//包名
	private  String packName;
	
	//类名
	private String className; 
	//表名
	private String tableName;
	//注解
	private String comment;
	
	//保存路径
	private String savePath;
	
	// 首字母小写
	public static String firstToLowerCase(String name) {
		char[] cs = name.toCharArray();
		if (Character.isUpperCase(cs[0]))
			cs[0] += 32;
		return String.valueOf(cs);
	}
		
	/*
	 * 表名转换为类名  Yuanwen  
	 */
	public void convert(){
		//字段名
		className = changeName(tableName,false);
	}
	
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
		
	//或者主键名称，若组合主键，则随机返回一个
	public String getIdName(){
		for(ConvertColumn col : colList){
			if(col.isID()) return col.getFieldName();
		}
		return null;
	}
	
	//字段列表
	private List<ConvertColumn> colList;

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public List<ConvertColumn> getColList() {
		return colList;
	}

	public void setColList(List<ConvertColumn> colList) {
		this.colList = colList;
	}
	
}
