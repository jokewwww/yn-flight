package cn.iwen.frame.dao.bean;

public class OrmTable{

	//数据库schema
	private String schema;
	//表名
	private String tableName;
	//实体类型
	private Class<?> entityClass;
	//复合主键
	private boolean cpkey;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	public boolean isCpkey() {
		return cpkey;
	}
	public void setCpkey(boolean cpkey) {
		this.cpkey = cpkey;
	}
	
}
