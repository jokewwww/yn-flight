package cn.iwen.frame.dao.bean;

import java.util.List;

//函数结果集封装
public class FuncField extends BaseField {

	private String func;
	
	public static BaseField make(String func,String alias) {
		FuncField bf = new FuncField();
		bf.func = func;
		bf.setAlias(alias);
		return bf;
	}
	
	@Override
	public void toSelectSQL(List<String> strList, String label) {
		strList.add( func + " " + super.getAlias());
	}
}
