package cn.iwen.frame.dao.expr;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import cn.iwen.frame.BaseUtils;

public class BaseExpr implements IExprSQL {

	private String opt;
	private String colName;
	private Object vue1;
	private Object vue2;
	
	public BaseExpr(String colName,String opt,Object vue) {
		this.colName = colName;
		this.opt = opt;
		this.vue1 = vue;
	}
	
	public BaseExpr(String colName,String opt,Object vue1,Object vue2) {
		this(colName,opt,vue1);
		this.vue2 = vue2;
	}
	
	private String createKey(Map<String,Object> params,String key) {
		if(params == null) return key;
		int i = 0;
		String str = null;
		while(true) {
			str = key + "_" +  i;
			if(!params.containsKey(str)) break;
			i++;
		}
		return str;
	}
	
	@Override
	public String toSQL(String label,Map<String,Object> params) {
		if(label == null) label = "";
		else label += ".";
		String expr = null;
		if(Exprs.OPT_EQ.equals(opt) && vue1 == null) {
			expr =  label + colName + " is null ";
		}else if(Exprs.OPT_NOEQ.equals(opt) && vue1 == null) {
			expr =  label + colName + " is not null ";
		}else if(Exprs.OPT_IN.equals(opt)) {
			if(params == null) {
				try {
					List<String> strs = JSON.parseArray(JSON.toJSONString(vue1), String.class);
					expr =  label + colName + " in("+String.join(",", strs)+") ";
				}catch(JSONException ex) {
					ex.printStackTrace();
				}
			}else {
				String key = createKey(params, colName);
				params.put(key, vue1);
				expr =  label + colName + " in(:"+key+")";
			}
		}else {
			if(params == null) {
				expr =  label + colName + " " 
							+ opt + " " + toValue(vue1);
				if(vue2 != null) {
					expr += " and " + toValue(vue2);
				}
			}else {
				String key = createKey(params, colName);
				expr =  label + colName + " " + opt +" :" + key;
				params.put(key, vue1);
				if(vue2 != null) {
					key = createKey(params, colName);
					expr += " and :" + key;
					params.put(key, vue2);
				}
			}
		}
		return expr;
	}
	
	//值转化为表达式
	private String toValue(Object vue) {
		if(vue instanceof Integer) return vue.toString();
		else if(vue instanceof Date) return "'" + BaseUtils.date2str(vue, "") + "'";
		else return "'" + vue + "'";
	}

	protected String getOpt() {
		return opt;
	}

	protected String getColName() {
		return colName;
	}

	public void setVue(Object vue1) {
		this.vue1 = vue1;
	}


}
