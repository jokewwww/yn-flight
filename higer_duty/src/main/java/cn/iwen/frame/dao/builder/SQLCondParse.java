package cn.iwen.frame.dao.builder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.iwen.frame.dao.expr.BaseExpr;
import cn.iwen.frame.dao.expr.Exprs;
import cn.iwen.frame.dao.expr.IExprSQL;
import cn.iwen.frame.dao.orm.IOrmBiz;

/*
  * sql where语句解释器
  * */
public class SQLCondParse {

	final protected Log log = LogFactory.getLog(SQLCondParse.class);
	
	private IOrmBiz ormbiz;
	
	public SQLCondParse(IOrmBiz ormbiz){
		this.ormbiz = ormbiz;
	}
	//条件运算符
	private final static String[] conditionOpts = new String[] {IExprSQL.OPT_EQ,IExprSQL.OPT_NOTEQ,
			IExprSQL.OPT_GT,IExprSQL.OPT_GTEQ,IExprSQL.OPT_LT,IExprSQL.OPT_LTEQ,
			IExprSQL.OPT_BETWEEN,IExprSQL.OPT_LIKE,IExprSQL.OPT_IN};
	
	private Object getArgs(Object[] args,int index) {
		if(index < 0 || index > args.length - 1) return null;
		return args[index];
	}
	
	public IExprSQL parseSQL(String sql,Object...args) {
		int baseIndex = 0;
		//操作符运算符队列
		Deque<String> operateQue = new ArrayDeque<>();
		//操作数运算符队列
		Deque<String> operandQue = new ArrayDeque<>();
		//表达式队列
		Deque<Exprs> exprsQue = new ArrayDeque<>();
		IExprSQL currExpr = null;//当前表达式
		List<String> wordList = splitSql(sql);
		for(String word : wordList) {
			if(eq(word,IExprSQL.OPT_LB)) {//左括号
				operateQue.add(word);
			}else if(eq(word,IExprSQL.OPT_RB)) {//右括号
				do {
					Exprs exprs = exprsQue.peekLast();
					if(exprs != null) {
						if(currExpr != null) {
							exprs.add(currExpr);
							currExpr = null;
						}
					}
					String opt = operateQue.pollLast();
					if(eq(opt,IExprSQL.OPT_LB)) break;
					if(opt == null) {
						log.debug("error,括号不配对");
						break;
					}
				}while(true);
			}else if(isLogicOperate(word)) {//and/or运算符
				String opt = operateQue.pollLast();
				if(eq(opt,IExprSQL.OPT_BETWEEN)) {//skip
					operateQue.add(opt);
					operateQue.add(word);
					continue;
				}
				Exprs newExpr = new Exprs(ormbiz,word);
				if(eq(opt,IExprSQL.OPT_LB)) {
					if(currExpr != null) {
						newExpr.add(currExpr);
						currExpr = null;
					}
					operateQue.add(IExprSQL.OPT_LB);//括号在放回去
				}else {
					IExprSQL expr = currExpr;
					Exprs exprs = exprsQue.pollLast();
					if(expr != null && exprs != null) {
						exprs.add(expr);
						currExpr = null;
					}
					if(exprs != null) expr = exprs;
					if(expr != null) newExpr.add(expr);
				}
				operateQue.add(word);
				exprsQue.add(newExpr);
			}else if(isConditionOperate(word)) {//条件运算符
				operateQue.add(word);
			}else {//操作数
				String preOpt = operateQue.pollLast();//上一个操作符
				if(eq(IExprSQL.OPT_BETWEEN,preOpt)) {
					operandQue.add(word);
					operateQue.add(preOpt);
				} else if(isConditionOperate(preOpt)) {
					String operand = operandQue.pollLast();
					Object value = word;
					if("?".equals(word)) value = getArgs(args, baseIndex++); 
					if(value != null)
						currExpr = new BaseExpr(operand, preOpt, value);
				}else if(eq(IExprSQL.OPT_AND,preOpt)) {
					String preOpt1 = operateQue.pollLast();//上一个操作符
					if(eq(IExprSQL.OPT_BETWEEN,preOpt1)) {
						Object value1 = operandQue.pollLast();//取操作数
						String field = operandQue.pollLast();//取字段
						Object value2 = word;
						if("?".equals(value1)) value1 = getArgs(args, baseIndex++);
						if("?".equals(value2)) value2 = getArgs(args, baseIndex++);
						if(value1 != null && value2 != null)
							currExpr = new BaseExpr(field, preOpt1,value1, value2);
					}else {
						operandQue.add(word);
						if(preOpt1 != null) operateQue.add(preOpt1);
						operateQue.add(preOpt);
					}
				}else {
					operandQue.add(word);
					if(preOpt != null) operateQue.add(preOpt);
				}
			}
		}
		//所有表达式合成一个
		IExprSQL expr = currExpr;
		do {
			Exprs exprs = exprsQue.pollLast();
			if(exprs == null) break;
			if(expr != null) exprs.add(expr);
			expr = exprs;
		}while(true);
		return expr;
	}
	
	//操作数
	private boolean isAllNum(String word) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
       return pattern.matcher(word).matches();  
	}
		
	//操作数
	private boolean isOperand(String word) {
		return eq(IExprSQL.OPT_AND,word) || eq(IExprSQL.OPT_OR,word);
	}
	
	//逻辑操作数
	private boolean isLogicOperate(String word) {
		return eq(IExprSQL.OPT_AND,word) || eq(IExprSQL.OPT_OR,word); 
	}
	
	//条件操作数
	private boolean isConditionOperate(String word) {
		for(String opt:conditionOpts) {
			if(eq(opt,word)) return true;
		}
		return false; 
	}
		
	//字符串是否相等忽略大小写
	private boolean eq(String str1,String str2) {
		if(str1 == null || str2 == null) return false;
		return str1.equalsIgnoreCase(str2);
	}
	
	private static void addStr(List<String> strs,StringBuilder sstr) {
		String str = sstr.toString().trim();
		if(str.length() > 0) {
			strs.add(str);
		}
		sstr.delete(0, sstr.length());
	}
	
	//sql语句词法分割
	public static List<String> splitSql(String sql){
		char[] sqls = sql.toCharArray();
		StringBuilder sstr = new StringBuilder();
		List<String> strs = new ArrayList<>();
		char pch = 0;
		boolean flag1 = false,flag2 = false;
		for(char ch: sqls) {
			if(flag1 || flag2) sstr.append(ch);
			else if(ch == '\'') {//字符串
				if(flag1) {
					if(pch != '\\') flag1 = false;
				}else {
					flag1 = true;
				}
				sstr.append(ch);
			}else if(ch == '`') {//字符串
				flag2 = !flag2;
				sstr.append(ch);
			}else if(ch == '(' || ch == ')' || ch == ',') {
				addStr(strs,sstr);
				strs.add(ch + "");
			}else if(Character.isWhitespace(ch)) {//StringUtils.isBlank(ch+"")
				addStr(strs,sstr);
			}else sstr.append(ch);
			pch = ch;
		}
		addStr(strs,sstr);
		return strs;
	}
	
}
