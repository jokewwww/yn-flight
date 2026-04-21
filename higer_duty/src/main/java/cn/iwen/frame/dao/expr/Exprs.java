package cn.iwen.frame.dao.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.OrmException;
import cn.iwen.frame.dao.orm.IOrmBiz;

public class Exprs implements IExprSQL{

	public static final String OPT_EQ = "=";
	public static final String OPT_IN = "in";
	public static final String OPT_NOEQ = "<>";
	
	private IOrmBiz ormbiz;
	//逻辑运算符
	private String opt;
	//表达式列表
	private List<IExprSQL> exprList = new ArrayList<IExprSQL>();
		
	public static IExprSQL EQ(String colName,Object vue) {
		return new BaseExpr(colName,OPT_EQ,vue);
	}
	
	public Exprs(IOrmBiz ormbiz,String opt) {
		this.ormbiz = ormbiz;
		this.opt = opt;
	}
	
	public Exprs add(IExprSQL exprSql) {
		exprList.add(exprSql);
		return this;
	}
	
	/*添加表达式*/
	public Exprs add(String field,String opt,Object vue) {
		if(vue == null) return this;
		String colName = ormbiz.toColName(field);
		if(colName != null) {
			add(new BaseExpr(colName, opt, vue));
		}
		return this;
	}
	
	//in
	public Exprs in(String field,Collection<?> sets) {
		if(sets.isEmpty()) return this;
		return add(field,"in",sets);
	}
	
	public Exprs inStr(String field,String strs) {
		if(StringUtils.isEmpty(strs)) return this;
		return add(field,"in",BaseUtils.str2StrList(strs));
	}
	
	public Exprs inInt(String field,String strs) {
		if(StringUtils.isEmpty(strs)) return this;
		return add(field,"in",BaseUtils.str2IntList(strs));
	}
	
	//public Expr in(String field,String strs) {
	//	return add(field,"in",CommonUtils.str2StrList(strs));
	//}
	
	//相似
	public Exprs like(String field,String vue) {
		return add(field,"like",vue);
	}
	
	public Exprs likeFull(String field,String vue) {
		if(StringUtils.isEmpty(vue)) return this;
		vue = "%" + vue + "%";
		return add(field,"like",vue);
	}
	
	//等于
	public Exprs eq(String field,Object vue) {
		if(vue == null || StringUtils.isEmpty(vue.toString())) return this;
		return add(field,OPT_EQ,vue);
	}
	//不等于
	public Exprs noteq(String field,Object vue) {
		if(vue == null || StringUtils.isEmpty(vue.toString())) return this;
		return add(field,OPT_NOEQ,vue);
	}
	//小于
	public Exprs lt(String field,Object vue) {
		return add(field,"<",vue);
	}
	//小于等于
	public Exprs lteq(String field,Object vue) {
		return add(field,"<=",vue);
	}
	//大于等于
	public Exprs gt(String field,Object vue) {
		return add(field,">",vue);
	}
	//大于
	public Exprs gteq(String field,Object vue) {
		return add(field,">=",vue);
	}
	//大于
	public Exprs between(String field,Object vue1,Object vue2) {
		if(vue1 == null || vue2 == null) return this;
		String colName = ormbiz.toColName(field);
		if(colName != null) {
			exprList.add(new BaseExpr(colName, "BETWEEN", vue1,vue2));
		}else {
			throw new OrmException("字段:[" + field + "]不存在");
		}
		return this;
	}
	

	//@Override
	public boolean isEmpty() {
		return exprList.isEmpty();
	}
	
	@Override
	public String toSQL(String label, Map<String, Object> params) {
		if(exprList.isEmpty()) return null;
		StringBuilder sqlstr = new StringBuilder();
		boolean group = exprList.size() > 1?true:false;
		if(group) sqlstr.append("(");
		List<String> slist = new ArrayList<String>();
		for(IExprSQL expr : exprList) {
			String sql = expr.toSQL(label, params);
			if(sql != null) slist.add(sql);
		}
		sqlstr.append(String.join(" " + opt + " " , slist));
		if(group) sqlstr.append(")");
		return sqlstr.toString();
	}

}
