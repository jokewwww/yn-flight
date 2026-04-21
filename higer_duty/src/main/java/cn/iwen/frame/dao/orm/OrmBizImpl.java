package cn.iwen.frame.dao.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.OrmColumn;
import cn.iwen.frame.dao.bean.OrmTable;
import cn.iwen.frame.dao.builder.JoinBuilder;
import cn.iwen.frame.dao.expr.Exprs;
import cn.iwen.frame.dao.expr.IExprSQL;

public class OrmBizImpl implements IOrmBiz {

	private OrmTable ormTable = new OrmTable();
	//关联的字段信息
	private List<OrmColumn> colList= new ArrayList<OrmColumn>();
	

	//验证class是否一致
	public boolean checkClass(Class<?> tClass) {
		return ormTable.getEntityClass() == tClass;
	}
		
	/*
	 * 完善连接查询语句
	 * 1 查询结果
	 * 2 连接条件
	 * */
	public void joinSQL(JoinBuilder builder,Class<?> joinCls) {
		for(OrmColumn col : colList) {
			col.joinSQL(builder,joinCls);
		}
	}
	
	//逗号分割
	public String allColumn() {
		List<String> list = new ArrayList<String>();
		for(OrmColumn col : colList) {
			list.add(col.getColName());
		}
		return String.join(",", list);
	}
	/*
	 * 获取实体表达式
	 * */
	@Override
	public IExprSQL toExpr(Object obj) {
		Exprs exprs = new Exprs(this,"and");
		if(obj instanceof Integer){
			exprs.eq(toIdColName(), obj);
		}else {//若主键存在，则放弃其他字段
			Object id = toIdValue(obj);
			if(id != null) exprs.eq(toIdColName(), id);
			else {
				for(OrmColumn col : colList) {
					Object val = col.getValue(obj);
					if(val == null) continue;
					exprs.add(Exprs.EQ(col.getColName(), val));
				}
			}
		}
		return exprs;
	}
	
	/*
	 * 更具实体类实例创建字段列表
	 * */
	public List<BaseField> toFieldList(Object obj,boolean nopk,boolean ignoreNull,String fields,String exFields){
		List<BaseField> fieldList = new ArrayList<BaseField>();
		List<String> strList = BaseUtils.str2StrList(fields);
		List<String> exStrList = BaseUtils.str2StrList(exFields);
		for(OrmColumn col : colList) {
			Object val = col.getValue(obj);
			if(col.isAutoFlag()) continue;//自增字段
			if(nopk && col.isId()) continue;//非主键且不包含,则跳过
			if(ignoreNull && val == null) continue;//过滤到控制
			if(col.isExtCol()) continue;//伪列
			if(!col.match(strList)) continue;
			if(!exStrList.isEmpty() && col.match(exStrList)) continue;
			BaseField field = BaseField.make(col.getColName());
			field.setData(val);
			fieldList.add(field);
		}
		return fieldList;
	}
	
	//主键赋值
	@Override
	public boolean idValue(Object obj,Integer id) {
		boolean bret = false;
		for(OrmColumn ormCol : colList){
			if(ormCol.isId()){
				ormCol.setValue(obj,id);
				bret = true;
				break;
			}
		}
		return bret;
	}

	private OrmColumn  toIdColumn() {
		for(OrmColumn ormCol : colList){
			if(ormCol.isId()){
				return ormCol;
			}
		}
		return null;
	}
	

	//获取主键类型
	@Override
	public String  toIdColName() {
		OrmColumn col  = toIdColumn();
		return col == null?null:col.getColName();
	}
	
	//获取实体主键值
	public Integer  getIdValue(Object obj) {
		OrmColumn ormCol  = toIdColumn();
		return ormCol == null?null:(Integer)ormCol.getValue(obj);
	}
	
	public String getColName(String field) {
		String name = null;
		for(OrmColumn col : colList) {
			if(col.match(field)) {
				name = col.getColName();
				break;
			}
		}
		return name;
	}

	@Override
	public boolean init(Class<?> tClass) {
		//初始化实体类及表信息
		Table table = (Table) tClass.getAnnotation(Table.class);
		if (table == null) return false;
		ormTable.setEntityClass(tClass);
		ormTable.setTableName(table.name());
		ormTable.setSchema(table.schema());
		initFields(tClass);
		return true;
	}

	//初始化类中域与表中列的关系
	private void initFields(Class<?> tClass) {
		Field[] fields = getClassFields(tClass);
		boolean pk = false;
		for (Field field : fields) {
			try {// 异常继续，故异常在循环内
				Column col = (Column) field.getAnnotation(Column.class);
				if (col == null) continue;
				OrmColumn ormCol = new OrmColumn();
				if(ormCol.initColumn(col,field)) {
					if(ormCol.initPK(field)) {
						if(pk) ormTable.setCpkey(true);//设置为复合主键
						else pk = true;
					}
					ormCol.setMany2many((ManyToMany)field.getAnnotation(ManyToMany.class));
					colList.add(ormCol);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	/*获取类字段*/
	private static Field[] getClassFields(Class<?> tClass) {
		if(Object.class.equals(tClass)) return null;
		Field[] fields1 = tClass.getDeclaredFields();
		Field[] fields2 = getClassFields(tClass.getSuperclass());
		if(fields2 != null) {
			fields1 = ArrayUtils.addAll(fields1,fields2);
		}
		return fields1;
	}

	
	@Override
	public List<OrmColumn> toColList() {
		return colList;
	}

	@Override
	public OrmColumn toColumn(String name) {
		for(OrmColumn col : colList) {
			if(col.match(name)) {
				return col;
			}
		}
		return null;
	}

	@Override
	public String toColName(String name) {
		OrmColumn col = toColumn(name);
		return col == null?null:col.getColName();
	}
	
	@Override
	public String toColName(String label,String name) {
		String colName = toColName(name);
		return colName == null?null:label + "." + colName;
	}
	

	@Override
	public Object toIdValue(Object obj) {
		OrmColumn ormCol  = toIdColumn();
		return ormCol == null?null:ormCol.getValue(obj);
	}

	@Override
	public Object createInst() {
		try {
			return ormTable.getEntityClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toTableInfo(String label) {
		if(label == null) label = "";
		StringBuilder sb = new StringBuilder();
		sb.append(ormTable.getTableName() + " " + label);
		if(StringUtils.isNotEmpty(ormTable.getSchema())) sb.insert(0, ormTable.getSchema() + ".");
		return sb.toString();
	}

	@Override
	public String toNexSeq() {
		String seq = null;
		for(OrmColumn ormCol : colList){
			seq = ormCol.getSequence();
			if(seq != null){
				break;
			}
		}
		return seq;
	}
	
}
