package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.OrmException;
import cn.iwen.frame.dao.bean.BaseField;

public class InsertSQL extends BaseSQL{

	public void toField(List<String> fieldListSrc, List<String> vueList,Map<String, Object> params) {
		for(BaseField field:fieldList) {
			fieldListSrc.add(field.getColName());
			vueList.add(field.getLabelName(params));
		}
	}
	
	public StringBuilder toSQL(Map<String, Object> params) {
		List<String> fieldList = new ArrayList<String>();
		List<String> vueList = new ArrayList<String>();
		toField(fieldList,vueList,params);
		if(fieldList.isEmpty() || vueList.isEmpty()) 
			throw new OrmException("insert into字段不能为空");
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(super.toTable(null));
		sb.append("(");
		sb.append(String.join(",", fieldList));
		sb.append(") values(");
		sb.append(String.join(",", vueList));
		sb.append(")");
		return sb;
	}

}
