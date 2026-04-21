package cn.iwen.frame.dao.orm;

import java.util.List;

import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.OrmColumn;
import cn.iwen.frame.dao.builder.JoinBuilder;
import cn.iwen.frame.dao.expr.IExprSQL;

public interface IOrmBiz {

	/*
	 * 实体对象映射初始化
	 * */
	boolean init(Class<?> tClass);
	//验证实体类是否相等
	boolean checkClass(Class<?> tClass);
	
	//逗号分割
	String allColumn();
	/*
	 * 完善连接查询语句
	 * 1 查询结果
	 * 2 连接条件
	 * */
	public void joinSQL(JoinBuilder builder,Class<?> joinCls);
	//获取字段列表
	List<OrmColumn> toColList();
	//映射表的字段对象
	OrmColumn toColumn(String name);
	//实体域 --> 表中字段
	String toColName(String name);
	//实体域 --> 表中字段
	String toColName(String label,String name);
	//获取主键的值
	Object toIdValue(Object obj);
	//主键设置值
	boolean idValue(Object obj,Integer vue);
	//创建实例
	Object createInst();
	//获取主键的字段名
	String toIdColName();
	//获取表的信息
	String toTableInfo(String label);
	//实体中不为null字段，转化为and表达式,若主键不为null，则仅保留主键
	IExprSQL toExpr(Object obj);
	//实体转化为域列表
	List<BaseField> toFieldList(Object obj,boolean nopk,boolean ignoreNull,String fields,String exFields);
	//获取自增值的oracle序列
	String toNexSeq();
}
