package cn.iwen.frame.dao.expr;

import java.util.Map;

/*
 * sql语句条件部分接口
 * */
public interface IExprSQL {
	
	String OPT_EQ = "=";
	String OPT_NOTEQ = "<>";
	String OPT_GT = ">";
	String OPT_GTEQ = ">=";
	String OPT_LT = "<";
	String OPT_LTEQ = "<=";
	String OPT_BETWEEN = "BETWEEN";
	String OPT_LIKE = "like";
	String OPT_IN = "in";
	
	String OPT_AND = "and";
	String OPT_OR = "or";
	
	String OPT_LB = "(";//LEFT_BRACKET
	String OPT_RB = ")";//RIGHT_BRACKET
	
	
	String toSQL(String label, Map<String, Object> params);

}
