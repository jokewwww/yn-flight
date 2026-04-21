package cn.iwen.frame.dao.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.OrderBean;

//基本的sql查询语句
public class SelectSQL extends BaseSQL{
	
	
	private String tableSplitChar = " ";//表格分割符
	private boolean distinct;//是否去重
	protected final List<JoinSQL> joinList = new ArrayList<>();//关联表
	protected final List<OrderBean> orderList = new ArrayList<>();//排序
	
	
	public void addTable(JoinSQL joinSQL) {
		joinList.add(joinSQL);
	}
	
	public boolean isFieldEmpty() {
		return fieldList.isEmpty();
	}
	
	/*
	 * 结果集
	 * 先横向，再纵向
	 * */
	public void toField(List<String> strList,String label) {
		for(BaseField fieldbean : fieldList) {
			fieldbean.toSelectSQL(strList, label);
		}
		toJoinField(strList,label);//关联表
	}
	
	//关联表字段
	protected void toJoinField(List<String> strList,String label) {
		int i = 0;
		for(JoinSQL joinsql : joinList) {
			joinsql.toField(strList,createLabel(label,i++));
		}
	}
	
	/*排序相关函数*/
	public void addOrder(OrderBean orderbean) {
		orderList.add(orderbean);
	}
	
	//整合连接排序
	public List<OrderBean> toJoinOrder(String label) {
		if(orderList.isEmpty()) return null;
		int i = 0;
		for(OrderBean obean:orderList) {
			obean.setLabel(label);
		}
		for(JoinSQL joinsql : joinList) {
			List<OrderBean> list = joinsql.toJoinOrder(createLabel(label,i++));
			if(list != null) orderList.addAll(list);
		}
		return orderList;
	}
		
	//排序
	public void toOrder(List<String> strList,String label) {
		toJoinOrder(label);
		Collections.sort(orderList);//排序
		for(OrderBean bean : orderList) {
			bean.toComSQL(strList);
		}
	}
	
	public String toOrderString(String label) {
		List<String> orderList = new ArrayList<String>();
		toOrder(orderList, label);
		String str = "";
		//排序
		if(!orderList.isEmpty()) {
			str = " order by ";
			str += String.join(",", orderList);
		}
		return str;
	}
	
	//表
	public void toTable(List<String> strList,String label,Map<String,Object> params) {
		super.toTable(strList, label,params);
		int i = 0;
		for(JoinSQL joinsql : joinList) {
			joinsql.toJoinTable(strList,createLabel(label, i++),label,params);
		}
	}
	
	//组合为sql语句,无排序
	public StringBuilder toSQLCount(String label,Map<String,Object> params) {
		if(null == label) label = "t";
		List<String> fieldList = new ArrayList<String>();
		List<String> tableList = new ArrayList<String>();
		List<String> whereList = new ArrayList<String>();
		//List<String> orderList = new ArrayList<String>();
		toField(fieldList, label);
		toTable(tableList, label,params);
		toWhere(whereList, label,params);
		//toOrder(orderList, label);
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) ");
		//表
		sb.append(" from ");
		sb.append(String.join(tableSplitChar, tableList));
		//条件
		if(!whereList.isEmpty()) {
			sb.append(" where ");
			sb.append(String.join(" and ", whereList));
		}
		return sb;
	}
		
	//组合为sql语句,无排序
	public StringBuilder toSQLNoOrder(String label,Map<String,Object> params) {
		if(null == label) label = "t";
		List<String> fieldList = new ArrayList<String>();
		List<String> tableList = new ArrayList<String>();
		List<String> whereList = new ArrayList<String>();
		//List<String> orderList = new ArrayList<String>();
		toField(fieldList, label);
		toTable(tableList, label,params);
		toWhere(whereList, label,params);
		//toOrder(orderList, label);
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		if(distinct) sb.append(" DISTINCT ");//是否去重
		//字段
		if(fieldList.isEmpty()) sb.append(" * ");
		else sb.append(String.join(",", fieldList));
		//表
		sb.append(" from ");
		sb.append(String.join(tableSplitChar, tableList));
		//条件
		if(!whereList.isEmpty()) {
			sb.append(" where ");
			sb.append(String.join(" and ", whereList));
		}
		return sb;
	}
	
	//组合为sql语句,无排序
	public StringBuilder toSQL(String label,Map<String,Object> params) {
		if(null == label) label = "t";
		StringBuilder sb = toSQLNoOrder(label,params);
		sb.append(toOrderString(label));
		return sb;
	}
	
	public StringBuilder toSQL(Map<String,Object> params) {
		return toSQL(null,params);
	}

	public boolean isDistinct() {
		return distinct;
	}
	
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	protected String createLabel(String label,int i) {
		return label + i;
	}

}
