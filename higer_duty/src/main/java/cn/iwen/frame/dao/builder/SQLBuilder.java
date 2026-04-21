package cn.iwen.frame.dao.builder;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.BaseDao;
import cn.iwen.frame.dao.IFuncField;
import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.OrderBean;
import cn.iwen.frame.dao.bean.OrmColumn;
import cn.iwen.frame.dao.expr.Exprs;
import cn.iwen.frame.dao.expr.IExprSQL;
import cn.iwen.frame.dao.orm.IOrmBiz;
import cn.iwen.frame.dao.sql.BaseSQL;
import cn.iwen.frame.dao.sql.LineSelect;

//基本的sql查询语句
public class SQLBuilder {
	
	final protected Log log = LogFactory.getLog(this.getClass());
	private BaseSQL baseSql;
	private IOrmBiz ormbiz;
	private BaseDao<?> dao;
	
	/*构造函数*/
	public SQLBuilder(IOrmBiz ormbiz,BaseSQL baseSql) {
		this.ormbiz = ormbiz;
		this.baseSql = baseSql;
		baseSql.setTableInfo(ormbiz.toTableInfo("%s"));
	}
	
	//构建
	public BaseSQL build() {
		return baseSql;
	}
	
	public Exprs and() {
		Exprs exprs = new Exprs(ormbiz, "and");
		addCond(exprs);
		return exprs;
	}
	
	public Exprs or() {
		Exprs exprs =  new Exprs(ormbiz, "or");
		addCond(exprs);
		return exprs;
	}
	
	/*
	 * 结果集
	 * */
	public SQLBuilder addFields(List<BaseField> beans) {
		baseSql.addFields(beans);
		return this;
	}
	
	public SQLBuilder addFuncField(String func,String alias) {
		return this;
	}
	
	public SQLBuilder addField(IFuncField funcField) {
		return this;
	}
	
	public SQLBuilder addField(BaseField bean) {
		baseSql.addField(bean);
		return this;
	}
	
	//添加值
	public SQLBuilder addFieldVue(String name,Object vue) {
		String colName = ormbiz.toColName(name);
		if(colName != null) {
			BaseField fb = BaseField.make(colName);
			fb.setData(vue);
			baseSql.addField(fb);
		}
		return this;
	}
	
	//添加结果集
	public SQLBuilder addField(String fieldNames) {
		List<String> list  = BaseUtils.str2StrList(fieldNames);
		for(String field : list) {
			OrmColumn col = ormbiz.toColumn(field);
			if(col == null) continue;
			BaseField  baseField = null;
			if(col.isExtCol() ) {
				if(col.getMany2many() != null)
				 baseField = BaseField.make(dao.lineSelect(col), col.getColName());
			}else {
				baseField = BaseField.make(col.getColName());
			}
			baseSql.addField(baseField);
		}
		return this;
	}
	//添加别名
	public SQLBuilder addAliasField(String name,String alias) {
		OrmColumn col = ormbiz.toColumn(name);
		if(col != null) {
			baseSql.addField(BaseField.make(col.getColName(),alias));
		}
		return this;
	}
	
	//添加子查询为结果集
	public SQLBuilder addField(String alias,LineSelect subSQL) {
		baseSql.addField(BaseField.make(subSQL, alias));
		return this;
	}

	/*
	 * 结果集
	 * */
	public SQLBuilder asc(int num,String fieldName) {
		return this;
	}
	
	public SQLBuilder desc(int num,String fieldName) {
		return this;
	}
	
	public SQLBuilder order(OrderBean orderbean) {
		return this;
	}
	
	
	public SQLBuilder order(int num,String orders) {
		return this;
	}
	
	//where条件
	public SQLBuilder addCond(IExprSQL cond) {
		baseSql.addCond(cond);
		return this;
	}

	protected IOrmBiz getOrmbiz() {
		return ormbiz;
	}

	public void setDao(BaseDao<?> dao) {
		this.dao = dao;
	}
	
	public BaseDao<?> getDao() {
		return dao;
	}

}
