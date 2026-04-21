package cn.iwen.frame.dao.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class OrderBean implements Comparable<OrderBean> {

	public final static String ORDER_ASC = "asc";
	public final static String ORDER_DESC = "desc";
	
	private String colName;
	
	private String order;
	
	private int num;
	
	private String label = "t";
	
	public OrderBean(int num,String colName,String order) {
		this.colName = colName;
		this.num = num;
		if(ORDER_ASC.equalsIgnoreCase(order))
			this.order = ORDER_ASC;
		else
			this.order = ORDER_DESC;
	}
	
	public void toComSQL(List<String> strList) {
		if(colName != null && StringUtils.isNotEmpty(order))
			strList.add(label + "." + colName + " " + order);
	}

	@Override
	public int compareTo(OrderBean o) {
		return this.num - o.num;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
