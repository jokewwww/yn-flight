package cn.iwen.frame.dao;

import java.util.List;
import java.util.Map;

import cn.iwen.frame.PageBean;
import cn.iwen.frame.dao.expr.IExprSQL;


public interface IBaseDao<T> {

	/*****************************
	 * 保存
	 * 根据主键判断新增 or 更新
	 */
	int save(T obj);
	
	/*
	 * 新建,null字段忽略
	 * 返回改变的行数
	 */
	int add(T obj);
	
	/*
	 * 新建,null字段有效
	 * 返回改变的行数
	 */
	int addWithNull(T obj);
	
	/*
	 * 新建,null字段忽略,自增主键写入实体类中
	 * 返回改变的行数
	 */
	int addAndId(T obj);
	
	/*
	 * 新建,null字段有效,自增主键写入实体类中
	 * 返回改变的行数
	 */
	int addAndIdWithNull(T obj);
	
	/****************************************
	 * 更新，根据主键修改
	 * null字段忽略
	 * 返回改变的行数
	 */
	int update(T obj);
	
	/*
	 * 更新，根据主键修改
	 * null字段同样修改
	 * 返回改变的行数
	 */
	int updateWithNull(T obj);
	/*
	 * 更新自定义字段
	 * 返回改变的行数
	 */
	int updateFields(T obj,String fields);
	//更新过滤后剩余字段
	int updateExFields(T obj,String fields); 
	/*
	 * 更新自定义字段和自定义条件
	 * */
	int update(T obj,IExprSQL cond);
	
	int update(Map<String,Object>fieldMap,IExprSQL cond);
	/********************
	 * 实体删除，不为null的字段做为条件且条件不能为空 !
	 * 返回改变的行数
	 */
	int delete(T obj);
	
	/*
	 * 通过主键删除
	 * 返回改变的行数
	 */
	int delete(Integer id);
	
	//自定义条件
	int delete(IExprSQL cond);
	
	/*
	 * 查询个体,主键作为条件，且不能为空
	 */
	T get(Integer id);
	T get(T obj);
	
	/*=================无分页查询列表==================*/
	/*
	 * 功能继承:List<T> getList(T obj);
	 * 加入排序参数
	 */
	List<T> getList(T obj);
	List<T> getList(T obj,String orderBy);
	//分空条件查询记录条数
	int getCount(T obj);
	
	int getCount(String field,Object vue);
		
	/*=================分页查询列表==================*/
	/*
	 * 分页查询列表,不为Null的字段作为条件且条件可以为空,默认排序
	 * 若无记录，返回长度为0的List
	 */
	List<T> getPageList(PageBean<T> pb,T obj,String orders);
	//fields=null为查询所有字段
	List<T> getPageList(PageBean<T> pb,T obj,String fields,String orders);
	
}
