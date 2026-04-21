package cn.iwen.frame.dao.bean;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.iwen.frame.dao.IFuncField;
import cn.iwen.frame.dao.sql.LineSelect;

public class BaseField {

	//表中字段名
	private String colName;
	//字段别名
	private String alias;
	
	//行内子查询
	private LineSelect subSQL;

	private Object data;
	
	private IFuncField funcField;
	
	public static BaseField make(IFuncField funcField) {
		BaseField bf = new BaseField();
		bf.funcField = funcField;
		return bf;
	}
	
	public static BaseField make(String name) {
		BaseField bf = new BaseField();
		bf.colName = name;
		return bf;
	}
	
	public static BaseField make(String name,String alias) {
		BaseField bf = new BaseField();
		bf.colName = name;
		bf.alias = alias;
		return bf;
	}
	
	public static BaseField make(LineSelect lineSQL,String alias) {
		if(lineSQL == null) return null;
		BaseField bf = new BaseField();
		bf.subSQL = lineSQL;
		bf.alias = alias;
		return bf;
	}
	
	public void toSelectSQL(List<String> strList, String label) {
		if(colName != null) {
			String str = label + "." + colName;
			if(StringUtils.isNotEmpty(alias))
				str += " as " + alias;
			strList.add(str);
		}else if(subSQL != null){
			strList.add("(" + subSQL.toSQL(label,null) + ") as " + alias);
		}else if(funcField != null) {
			strList.add(funcField.onFunc(label));
		}
	}

	//带前缀的
	public void toSelectSQL(List<String> strList, String label,String prefix) {
		prefix += ".";
		if(colName != null) {
			String str = label + "." + colName;
			if(StringUtils.isNotEmpty(alias))
				str += " as " + "'" +  prefix + alias + "'";
			else
				str += " as " + "'" +  prefix + colName + "'";
			strList.add(str);
		}else if(subSQL != null){
			strList.add("(" + subSQL.toSQL(label,null) + ") as " + "'" +  prefix + alias + "'");
		}else if(funcField != null) {
			strList.add(funcField.onFunc(label));
		}
	}
	
	public String getColName() {
		return colName;
	}
	
	//获取update sql语句: col_name = :col_name格式
	public String getLabelName(Map<String, Object> params) {
		String label = "null";
		if(data != null) {
			params.put(colName, data);
			label =  ":" + colName;
		}
		return label;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	protected String getAlias() {
		return alias;
	}

	protected void setAlias(String alias) {
		this.alias = alias;
	}

	protected LineSelect getSubSQL() {
		return subSQL;
	}

	protected void setSubSQL(LineSelect subSQL) {
		this.subSQL = subSQL;
	}

	protected void setColName(String colName) {
		this.colName = colName;
	}
	
}
