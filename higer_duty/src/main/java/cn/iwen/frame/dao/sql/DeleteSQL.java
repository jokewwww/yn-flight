package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.OrmException;

public class DeleteSQL extends BaseSQL{

	public StringBuilder toSQL(Map<String, Object> params) {
		String label = null;
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		sb.append(toTable(label));
		//条件
		List<String> whereList = new ArrayList<String>();
		toWhere(whereList, label,params);
		if(whereList.isEmpty()) {
			throw new OrmException("delete条件不能为空!");
		}else{
			sb.append(" where ");
			sb.append(String.join(" and ", whereList));
		}
		return sb;
	}

}
