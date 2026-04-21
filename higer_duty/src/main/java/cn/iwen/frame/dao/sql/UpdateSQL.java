package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.OrmException;
import cn.iwen.frame.dao.bean.BaseField;

public class UpdateSQL extends BaseSQL{

	public void toField(List<String> fieldListSrc,Map<String, Object> params) {
		for(BaseField field:fieldList) {
			fieldListSrc.add(field.getColName() + " = " + field.getLabelName(params));
		}
	}
	
	public StringBuilder toSQL(Map<String, Object> params) {
		List<String> fieldList = new ArrayList<String>();
		List<String> whereList = new ArrayList<String>();
		toField(fieldList,params);
		toWhere(whereList,null,params);
		if(fieldList.isEmpty() || whereList.isEmpty() || params.isEmpty()) 
			throw new OrmException("Update:字段或条件不能为空");
		StringBuilder sb = new StringBuilder();
		sb.append("update " + super.toTable(null) + " set ");
		sb.append(String.join(",", fieldList) + " where ");
		sb.append(String.join(" and ", whereList));
		return sb;
	}

}
