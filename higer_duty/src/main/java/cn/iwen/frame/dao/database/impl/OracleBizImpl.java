package cn.iwen.frame.dao.database.impl;

import cn.iwen.frame.PageBean;
import cn.iwen.frame.dao.database.BaseDataBaseBiz;


public class OracleBizImpl extends BaseDataBaseBiz {

	@Override
	public String autoIdSQL(String seqName) {
		return "select "  + seqName + ".nextval from dual ";
	}

	@Override
	public StringBuilder wrapPageSql(PageBean<?> pb, StringBuilder sb, String order) {
		sb.insert(0, "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		sb.append(" ) A WHERE ROWNUM <=  " + (pb.getPageSize() 
				+ pb.getOffset())  +" ) WHERE RN >= " + pb.getOffset());
		return sb;
	}
	
	@Override
	public	void wrapOneSql(StringBuilder sb) {
		String w = "where";
		int index = sb.indexOf(w);
		if(index > -1) {
			sb.insert(index + w.length(), " rownum = 1 and ");
		}
	}
}
