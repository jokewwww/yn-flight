package cn.iwen.frame.dao.builder;

import cn.iwen.frame.dao.expr.FieldExpr;
import cn.iwen.frame.dao.orm.IOrmBiz;
import cn.iwen.frame.dao.sql.JoinSQL;

public class JoinBuilder extends SelectBuilder {

	private IOrmBiz outOrmbiz;
	
	public JoinBuilder(IOrmBiz ormbiz,JoinSQL jtTable,IOrmBiz outOrmbiz) {
		super(ormbiz,jtTable);
		this.outOrmbiz = outOrmbiz;
	}
	
	@Override
	public JoinSQL build() {
		return (JoinSQL)super.build();
	}
	
	/*
	 * 添加连接条件
	 * */
	public JoinBuilder addLinkCond(String localField,String outField) { 
		String localName = super.getOrmbiz().toColName(localField);
		String outName = outOrmbiz.toColName(outField); 
		if(localName != null && outName != null) {
			addLinkCond(new FieldExpr(localName, "=", outName));
		}
		return this;
	}
	
	//添加连接条件
	public JoinBuilder addLinkCondVue(String localField,Object fieldVue) {
		String localName = super.getOrmbiz().toColName(localField);
		if(localName != null) {
			FieldExpr expr = new FieldExpr(localName, "=");
			expr.setVue(fieldVue);
			addLinkCond(expr);
		}
		return this;
	}
		
	//添加连接条件
	public JoinBuilder addLinkCond(FieldExpr expr) { 
		build().addLinkCond(expr);
		return this;
	}
	
	//设置前缀
	public JoinBuilder setPrefix(String prefix) { 
		build().setPrefix(prefix);
		return this;
	}
	
}
