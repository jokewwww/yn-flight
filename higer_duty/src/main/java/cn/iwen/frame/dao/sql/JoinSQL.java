package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.OrmException;
import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.expr.FieldExpr;

//连接查询，不能单独使用
public class JoinSQL extends SelectSQL {
	public final static String JOIN_TYPE_INNER = "inner";
	public final static String JOIN_TYPE_LEFT = "left";
	public final static String JOIN_TYPE_RIGHT = "right";
	//public final static String JOIN_TYPE_FULL = "full";
	private List<FieldExpr> exprList = new ArrayList<FieldExpr>();//字段表达式
	private String joinType = JOIN_TYPE_INNER;//连接类型
	private String prefix;//别名前缀
	
	public JoinSQL(String jointype) {
		this.joinType = jointype;
		
	}
	
	@Override
	public void toField(List<String> strList, String label) {
		for(BaseField fieldbean : fieldList) {
			if(prefix == null)
				fieldbean.toSelectSQL(strList, label);
			else
				fieldbean.toSelectSQL(strList, label,prefix);
		}
		super.toJoinField(strList,label);//关联表
	}
	
	//添加连接条件
	public void addLinkCond(FieldExpr expr) { 
		exprList.add(expr);
	}

	//获取连接条件
	public String toOnWhere(String selfLabel,String outLabel,Map<String,Object> params) {
		if(exprList.isEmpty()) throw new OrmException("表连接查询，连接条件不能为空");
		List<String> strList = new ArrayList<String>();
		for(FieldExpr expr : exprList) {
			strList.add(expr.toSQL(selfLabel, outLabel,params));
		}
		return String.join(" and ", strList);
	}
	
	//表
	public void toJoinTable(List<String> strList,String selfLabel,String outLabel,Map<String,Object> params) {
		List<String> whereSql = new ArrayList<String>();
		whereSql.add(toOnWhere(selfLabel,outLabel,params));
		toWhere(whereSql, selfLabel, params);
		strList.add("\n" + joinType + " join " + toTable(selfLabel) 
				+ " on " + String.join(" and ", whereSql));
		int i = 0;
		for(JoinSQL joinsql : joinList) {
			joinsql.toJoinTable(strList,createLabel(selfLabel, i++),selfLabel,params);
		}
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
		
}
