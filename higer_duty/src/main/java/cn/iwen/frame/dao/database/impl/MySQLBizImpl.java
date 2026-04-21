package cn.iwen.frame.dao.database.impl;

import cn.iwen.frame.PageBean;
import cn.iwen.frame.dao.database.BaseDataBaseBiz;

public class MySQLBizImpl extends BaseDataBaseBiz {

	@Override
	public String autoIdSQL(String seqName) {
		return "select last_insert_id()";
	}
	
	@Override
	public StringBuilder wrapPageSql(PageBean<?> pb, StringBuilder sb,String orderSql) {
		sb.append(orderSql);
		sb.append(" limit " + pb.getOffset() +","+ pb.getPageSize());
		return sb;
	}

	@Override
	public void wrapOneSql(StringBuilder sb) {
		sb.append(" limit 1 ");
	}
}
