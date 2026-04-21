package cn.iwen.frame.dao.builder;

import org.apache.commons.lang3.StringUtils;

import cn.iwen.frame.dao.IFuncField;
import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.FuncField;
import cn.iwen.frame.dao.bean.OrderBean;
import cn.iwen.frame.dao.orm.IOrmBiz;
import cn.iwen.frame.dao.sql.BaseSQL;
import cn.iwen.frame.dao.sql.JoinSQL;
import cn.iwen.frame.dao.sql.SelectSQL;

//基本的sql查询语句
public class SelectBuilder extends SQLBuilder{

	/*构造函数*/
	public SelectBuilder(IOrmBiz ormbiz,SelectSQL selectSql) {
		super(ormbiz,selectSql);
	}
	
	@Override
	public SelectSQL build() {
		return (SelectSQL)super.build();
	}
	
	
	public SelectBuilder distinct() {
		build().setDistinct(true);
		return this;
	}
	
	public boolean isFieldEmpty() {
		return build().isFieldEmpty();
	}
	public SQLBuilder addFuncField(String func,String alias) {
		super.addField(FuncField.make(func,alias));
		return this;
	}
	
	public JoinBuilder addTable(Class<?> cls) {
		JoinBuilder joinBuilder = super.getDao().join(cls);
		BaseSQL baseSql = joinBuilder.build();
		if(baseSql instanceof JoinSQL)
			build().addTable((JoinSQL)baseSql);
		return joinBuilder;
	}
	
	public SelectBuilder addTable(JoinBuilder builder) {
		BaseSQL baseSql = builder.build();
		if(baseSql instanceof JoinSQL)
			build().addTable((JoinSQL)baseSql);
		return this;
	}
	
	//添加回调函数,动态修改结果集
	@Override
	public SelectBuilder addField(IFuncField funcField) {
		super.addField(BaseField.make(funcField));
		return this;
	}
	
	/*排序相关函数*/
	@Override
	public SelectBuilder asc(int num,String fieldName) {
		String colName = super.getOrmbiz().toColName(fieldName);
		if(colName != null) {
			build().addOrder(new OrderBean(num,colName,OrderBean.ORDER_ASC));
		}
		return this;
	}
	
	@Override
	public SelectBuilder desc(int num,String fieldName) {
		String colName = super.getOrmbiz().toColName(fieldName);
		if(colName != null) {
			build().addOrder(new OrderBean(num,colName,OrderBean.ORDER_DESC));
		}
		return this;
	}

	@Override
	public SelectBuilder order(OrderBean orderbean) {
		build().addOrder(orderbean);
		return this;
	}
	
	@Override
	public SelectBuilder order(int num,String orders) {
		if(StringUtils.isEmpty(orders)) return this;
		String[] strs = orders.split(",");
		for(String str : strs) {
			String[] ss = str.split("\\s+");
			String field = ss[0].trim();
			String order = ss[1].trim();
			String colName = super.getOrmbiz().toColName(field);
			if(colName != null) {
				build().addOrder(new OrderBean(num,colName,order));
			}
		}
		return this;
	}
	
}
