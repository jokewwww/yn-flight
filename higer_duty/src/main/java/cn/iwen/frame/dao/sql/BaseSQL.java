package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.expr.IExprSQL;

public abstract class BaseSQL {

	final protected Log log = LogFactory.getLog(this.getClass());
	private String tableInfo;
	protected final List<BaseField> fieldList = new ArrayList<>();//结果集
	protected List<IExprSQL> condList = new ArrayList<>();//条件
	
	abstract public StringBuilder toSQL(Map<String, Object> params);
	
	public void addField(BaseField bean) {
		if(bean == null) return;
		fieldList.add(bean);
	}
	public void addFields(List<BaseField> beans) {
		fieldList.addAll(beans);
	}
	//空函数
	//public void toField(List<String> strList,String label) {}
	
	//条件表达式
	public void toWhere(List<String> strList,String label,Map<String,Object> params) {
		for(IExprSQL cond : condList) {
			String exprStr = cond.toSQL(label, params);
			if(StringUtils.isNotEmpty(exprStr)) strList.add(exprStr);
		}
	}
	//表
	public void toTable(List<String> strList,String label,Map<String,Object> params) {
		strList.add(toTable(label));
	}
	
	public void setTableInfo(String tableInfo) {
		this.tableInfo = tableInfo;
	}

	public void addCond(IExprSQL cond) {
		condList.add(cond);
	}
	
	protected String toTable(String label) {
		if(label == null) label = "";
		return String.format(tableInfo, label);
	}

}
