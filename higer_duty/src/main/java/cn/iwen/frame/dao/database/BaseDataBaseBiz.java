package cn.iwen.frame.dao.database;

import cn.iwen.frame.PageBean;

public abstract class BaseDataBaseBiz {

	//获取最新自增值的语句
	abstract public String autoIdSQL(String seqName);
		//分页查询
	abstract 	public StringBuilder wrapPageSql(PageBean<?>pb,StringBuilder sb,String orderSql);
		//查询一条记录
	abstract public 	void wrapOneSql(StringBuilder sb);
		
	public 	void wrapCountSql(StringBuilder sb) {
		sb.insert(0, "select count(1) from (");
		sb.append(") as a");
	}
	
}
