package cn.iwen.frame.dao.sql;

import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.database.BaseDataBaseBiz;

public class LineSelect extends JoinSQL {

	private final String selfLabel = "tt";
	private String outLabel = "t";
	private BaseDataBaseBiz dbBiz;
	
	public LineSelect() {
		super(null);
	}

	//条件表达式
	public void toWhere(List<String> strList,String label,Map<String,Object> params) {
		strList.add(toOnWhere(selfLabel, outLabel,params));
		super.toWhere(strList, label, null);
	}
	
	@Override
	public StringBuilder toSQL(String label,Map<String, Object> params) { 
		if(null != label) this.outLabel = label;
		StringBuilder sb = super.toSQL(selfLabel, params);
		dbBiz.wrapOneSql(sb);
		return sb;
	}

	public void setDbBiz(BaseDataBaseBiz dbBiz) {
		this.dbBiz = dbBiz;
	}
	
}
