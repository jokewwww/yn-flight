package cn.iwen.frame.dao.bean;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.iwen.frame.dao.builder.JoinBuilder;
import cn.iwen.frame.dao.sql.LineSelect;


/*
 * 实体映射的字段信息
 */
public class OrmColumn {

	protected static Log log = LogFactory.getLog(OrmColumn.class);
	
	public final static String EXT_COLUMN1 = "EXT_COLUMN";//伪列标志
	//表中列名
	private String colName;
	//类中域名
	private String fieldName;
	
	//是否主键
	private boolean  isId;
	
	//自增字段
	private boolean autoFlag;
	
	//是否伪列，即本类映射的表中没有此字段,true是,false否
	private boolean extCol;
		
	//序列名称
	private String sequence;
	
	private Field field;
	
	//是否可为空 true 可为空，false不能为空
	private boolean nullable ;

	private LineSelect subSql;
	
	private ManyToMany many2many;
	
	public Class<?> getLinkEntiry(){
		return many2many == null?null:many2many.targetEntity();
	}
	
	/*
	 * mapped格式:
	 * 	1 roleId=roleId
	 *  2 codeName;codeValue=menuType;codeType=1&codeDesc1=abcd
	 * */
	public void joinSQL(JoinBuilder builder,Class<?> joinCls){
		if(many2many == null || joinCls != many2many.targetEntity()) return;
		builder.addLinkCond(colName,many2many.mappedBy());
	}
	
	/*
	 * mapped格式:
	 * 	1 roleId=roleId
	 *  2 codeName;codeType=1&codeDesc1=abcd;codeValue=menuType
	 * */
	public void lineSQL(JoinBuilder builder,Class<?> joinCls){
		//if(many2many == null || joinCls != many2many.targetEntity()) return;
		String mapStr = many2many.mappedBy();
		String []mapStrs = mapStr.split(";");
		if(mapStrs.length != 3) return;
		//查询结果
		builder.addAliasField(mapStrs[0],colName);
		String[]strs = null;
		//条件1
		strs = mapStrs[1].split("&");
		for(String str:strs) {
			String[] ss = str.split("=");
			if(ss.length == 2) {
				builder.addLinkCondVue(ss[0],ss[1]);
			}
		}
		//条件2
		strs = mapStrs[2].split("&");
		for(String str:strs) {
			String[] ss = str.split("=");
			if(ss.length == 2) {
				builder.addLinkCond(ss[0],ss[1]);
			}
		}
	}
	
	//是否包含字段
	public boolean match(String name) {
		return (colName.equalsIgnoreCase(name) 
				|| fieldName.equals(name))?true:false;
	}
	
	//字段匹配
	public boolean match(Collection<String> set) {
		if(set.isEmpty()) return true;
		for(String str : set) {
			if(match(str))  return true;
		}
		return false;
	}
	
	//字段匹配
	public String matchEx(Collection<String> set) {
		if(set.isEmpty()) return null;
		for(String str : set) {
			if(match(str))  return str;
		}
		return null;
	}
		
	//获取实体的值
	public Object getValue(Object obj){
		try {
			if(obj == null) return null;
			//log.debug(fieldName);
			if(PropertyUtils.isReadable(obj, fieldName))
				return PropertyUtils.getProperty(obj, fieldName);
		} catch (NoClassDefFoundError | Exception e) {
			log.debug(obj +","+ fieldName +","+ e.getMessage());
		} 
		return null;
	}
	
	public boolean initColumn(Column col,Field colField) {
		String name = col.name();
		if(StringUtils.isEmpty(name)) name = colField.getName();
		colName = name;
		fieldName = colField.getName();
		field = colField;
		nullable = col.nullable();
		extCol = !col.insertable();
		return true;
	}
	
	//初始化主键
	public boolean initPK(Field colField) {
		Id id = (Id) colField.getAnnotation(Id.class);
		if (id == null) return false;
		GeneratedValue gv = (GeneratedValue) colField.getAnnotation(GeneratedValue.class);
		if (gv != null) {
			if (gv.strategy() == GenerationType.IDENTITY)
				this.setAutoFlag(true);// 设置为自增
			else if (gv.strategy() == GenerationType.SEQUENCE) {// oracle序列模式
				this.setSequence(gv.generator());// 设置模式名称
				autoFlag = true;
			}
		}
		this.setId(true);// 主键
		return true;
	}
	//设置实体的值
	public void setValue(Object obj,Object value){
		try {
			if(value == null || field == null) return;
			Class<?> c = field.getType();
			//log.debug(fieldName + "," + value.getClass() 
				//+ "," + field.getType() + "," + value);
			/**/
			if(value instanceof Long){//Long 转换为 Int or short
				if(c.equals(Integer.class)){
					value = ((Long)value).intValue();
				}else if(c.equals(Short.class))
					value = ((Long)value).shortValue();
			}else if(value instanceof Short){
				if(c.equals(Integer.class))
					value = ((Short)value).intValue();
			}else if(value instanceof Double){
				if(c.equals(Float.class))
					value = ((Double)value).floatValue();
				else if(c.equals(String.class)){
					value = value.toString();
				}
			}else if(value instanceof Integer){
				if(c.equals(Long.class))
					value = ((Integer)value).longValue();
				else if(c.equals(Boolean.class)) {
					value = ((Integer)value) == 0?false:true;
				} if(c.equals(Byte.class)) {
					value = ((Integer)value).byteValue();
				}
			}else if(value instanceof BigDecimal){
				if(c.equals(Double.class)){
					value = ((BigDecimal)value).doubleValue();
				}else if(c.equals(Integer.class)){
					value = ((BigDecimal)value).intValue();
				}else if(c.equals(String.class)){
					value = value.toString();
				}
			}else if(value instanceof String){
				if(c.equals(Double.class)){
					value = Double.parseDouble(value.toString());
				}
			}else if(value instanceof Boolean) {
				if(c.equals(Byte.class)){
					value = (byte)((Boolean)value?1:0);
				}else if(c.equals(Integer.class)) {
					value =  (int)((Boolean)value?1:0);
				}
			}else if(value instanceof Byte) {
				if(c.equals(Integer.class)){
					value = new Integer(value.toString());
				}
			}
			PropertyUtils.setProperty(obj, fieldName, value);
		} catch (NoClassDefFoundError | Exception e) {
			log.error(fieldName +","+ e.getMessage());
		}
	}
		
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}


	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public boolean isExtCol() {
		return extCol;
	}

	public void setExtCol(boolean extCol) {
		this.extCol = extCol;
	}

	public boolean isAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(boolean autoFlag) {
		this.autoFlag = autoFlag;
	}

	public LineSelect getSubSql() {
		return subSql;
	}

	public void setSubSql(LineSelect subSql) {
		this.subSql = subSql;
	}

	public void setMany2many(ManyToMany many2many) {
		this.many2many = many2many;
		//if(many2many != null) extCol = true;
	}

	public ManyToMany getMany2many() {
		return many2many;
	}

}
