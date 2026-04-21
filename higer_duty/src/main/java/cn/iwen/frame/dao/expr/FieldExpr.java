package cn.iwen.frame.dao.expr;

import java.util.Map;

public class FieldExpr extends BaseExpr  {

	private String colName2;
	
	public FieldExpr(String colName,String opt) {
		super(colName,opt,null);
	}
	
	public FieldExpr(String colName,String opt,String colName2) {
		super(colName,opt,null);
		this.colName2 = colName2;
	}

	public String toSQL(String label1, String label2, Map<String, Object> params) {
		if(colName2 == null) return super.toSQL(label1,params);
		if(label1 == null) label1 = "";
		else label1 += ".";
		if(label2 == null) label2 = "";
		else label2 += ".";
		return label1 + super.getColName() + " " 
					+ super.getOpt() + " " + label2 + colName2;
	}

	@Override
	public String toSQL(String label, Map<String, Object> params) {
		return toSQL(label,label,params);
	}

}
