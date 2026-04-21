package cn.iwen.frame.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.iwen.frame.PageBean;
import cn.iwen.frame.dao.bean.BaseField;
import cn.iwen.frame.dao.bean.OrderBean;
import cn.iwen.frame.dao.bean.OrmColumn;
import cn.iwen.frame.dao.builder.JoinBuilder;
import cn.iwen.frame.dao.builder.SQLBuilder;
import cn.iwen.frame.dao.builder.SQLCondParse;
import cn.iwen.frame.dao.builder.SelectBuilder;
import cn.iwen.frame.dao.database.BaseDataBaseBiz;
import cn.iwen.frame.dao.database.DBFactory;
import cn.iwen.frame.dao.expr.Exprs;
import cn.iwen.frame.dao.expr.IExprSQL;
import cn.iwen.frame.dao.orm.IOrmBiz;
import cn.iwen.frame.dao.orm.OrmBizImpl;
import cn.iwen.frame.dao.sql.BaseSQL;
import cn.iwen.frame.dao.sql.DeleteSQL;
import cn.iwen.frame.dao.sql.InsertSQL;
import cn.iwen.frame.dao.sql.JoinSQL;
import cn.iwen.frame.dao.sql.LineSelect;
import cn.iwen.frame.dao.sql.SelectSQL;
import cn.iwen.frame.dao.sql.UpdateSQL;

abstract public class BaseDao<T> implements IBaseDao<T>,RowMapper<T> {

	protected final Log log = LogFactory.getLog(this.getClass());
	private boolean logFlag = true;
	protected void setLogFlag(boolean logFlag) {
		this.logFlag = logFlag;
	}

	private NamedParameterJdbcTemplate jdbcTemp;
	private BaseDataBaseBiz dbbiz;
	private IOrmBiz ormbiz;
	private Class<T> entityCls;
	/*
	 * 数据库等组建构建工厂
	 * */
	@Autowired
	private DBFactory dbFactory;
	
	/*
	 * 初始化实体--表映射
	 * */
	@PostConstruct
	public void init() {
		Class<T> ormClass = getOrmBean(this.getClass());
		if(ormClass == null) return;
		entityCls = ormClass;
		ormbiz = new OrmBizImpl();
		boolean bret = ormbiz.init(ormClass);
		if(!bret) log.error("init orm error!");
		log.debug("init:" + this);
	}
	
	/*
	 * 注入数据源
	 * */
	@Autowired  
	public void setDS(@Qualifier("dataSource") DataSource ds){
		this.jdbcTemp = new NamedParameterJdbcTemplate(ds);
		dbbiz = dbFactory.create(ds,this);
	}
	/*
	 * 格式构建器
	 * */
	private SQLBuilder delete() {
		DeleteSQL deleteSql = new DeleteSQL();
		SQLBuilder builder = new SQLBuilder(ormbiz, deleteSql);
		return builder;
	}
	
	private SQLBuilder insert() {
		SQLBuilder builder =  new SQLBuilder(ormbiz,new InsertSQL());
		return builder;
	}
	
	private SQLBuilder update() {
		SQLBuilder builder =  new SQLBuilder(ormbiz,new UpdateSQL());
		return builder;
	}
	
	public SelectBuilder select() {
		SelectBuilder builder =  new SelectBuilder(ormbiz,new SelectSQL());
		builder.setDao(this);
		return builder;
	}
	
	public LineSelect lineSelect(OrmColumn ormCol) {
		Class<?> cls = ormCol.getLinkEntiry();
		if(cls == null) return null;
		BaseDao<?> dao = dbFactory.findBaseDao(cls);
		if(dao == null) return null;
		LineSelect lineSelect =  new LineSelect(); 
		lineSelect.setDbBiz(dbbiz);
		JoinBuilder builder =  new JoinBuilder(dao.ormbiz,lineSelect, ormbiz);
		builder.setDao(dao);
		ormCol.lineSQL(builder, null);
		return lineSelect;
	}
	
	//构建连接查询
	private JoinBuilder joinSelect(Class<?> cls,String joinType) {
		BaseDao<?> dao = dbFactory.findBaseDao(cls);
		JoinBuilder builder =  new JoinBuilder(dao.ormbiz,new JoinSQL(joinType),ormbiz);
		builder.setDao(dao);
		dao.ormbiz.joinSQL(builder, entityCls);
		return builder;
	}
	
	public JoinBuilder leftJoin(Class<?> cls) {
		return joinSelect(cls,JoinSQL.JOIN_TYPE_LEFT);
	}
	
	public JoinBuilder join(Class<?> cls) {
		return joinSelect(cls,JoinSQL.JOIN_TYPE_INNER);
	}
	
	public JoinBuilder rightJoin(Class<?> cls) {
		return joinSelect(cls,JoinSQL.JOIN_TYPE_RIGHT);
	}

	/*
	 * 条件表达式构建
	 * */
	public Exprs and() {
		return new Exprs(ormbiz,"and");
	}
	
	public Exprs or() {
		return new Exprs(ormbiz,"or");
	}
	
	public Exprs And(Class<?> cls) {
		BaseDao<?> dao = dbFactory.findBaseDao(cls);
		if(dao == null) return null;
		return new Exprs(dao.ormbiz,"and");
	}
	
	public Exprs Or(Class<?> cls) {
		BaseDao<?> dao = dbFactory.findBaseDao(cls);
		if(dao == null) return null;
		return new Exprs(dao.ormbiz,"or");
	}
	
	@Override
	public int save(T obj) {
		int iret = 0;
		if(null == ormbiz.toIdValue(obj)) iret = addAndId(obj);
		else iret = update(obj);
		return iret;
	}
	/*
	 * 新增记录,忽略null字段
	 * 返回新增的实体
	 * */
	@Override
	public int add(T obj) {
		List<BaseField> fieldList = ormbiz.toFieldList(obj,false,true,null,null);
		SQLBuilder builder = insert();
		builder.addFields(fieldList);
		return exec(builder);
	}

	@Override
	public int addWithNull(T obj) {
		List<BaseField> fieldList = ormbiz.toFieldList(obj,true,false,null,null);
		SQLBuilder builder = insert();
		builder.addFields(fieldList);
		return exec(builder);
	}

	@Override
	public int addAndId(T obj) {
		int iret = add(obj);
		if (iret == 1) {
			Integer id = queryObject(dbbiz.autoIdSQL(ormbiz.toNexSeq()), null, Integer.class);
			ormbiz.idValue(obj, id);
		}
		return iret;
	}

	@Override
	public int addAndIdWithNull(T obj) {
		int iret = addWithNull(obj);
		if (iret == 1) {
			Integer id = queryObject(dbbiz.autoIdSQL(ormbiz.toNexSeq()), null, Integer.class);
			ormbiz.idValue(obj, id);
		}
		return iret;
	}

	private int updateObj(T obj,boolean ignoreNull,String fields,String exFields) {
		List<BaseField> fieldList = ormbiz.toFieldList(obj,true,ignoreNull,fields,exFields);
		SQLBuilder builder = update();
		builder.addFields(fieldList);
		Object id = ormbiz.toIdValue(obj);
		String colName = ormbiz.toIdColName();
		if(id == null) throw new OrmException("更新实体时,主键["+ colName +"]不能为空");
		builder.addCond(Exprs.EQ(colName, id));
		return exec(builder);
	}
	
	@Override
	public int update(T obj) {
		return updateObj(obj,true,null,null);
	}

	@Override
	public int updateWithNull(T obj) {
		return updateObj(obj,false,null,null);
	}

	@Override
	public int updateFields(T obj, String fields) {
		return updateObj(obj,false,fields,null);
	}
	
	public int updateFields(T obj, String fields,IExprSQL exprSQL) {
		List<BaseField> fieldList = ormbiz.toFieldList(obj,true,false,fields,null);
		SQLBuilder builder = update();
		builder.addFields(fieldList);
		builder.addCond(exprSQL);
		return exec(builder);
	}
	

	//更新过滤后剩余字段
	@Override
	public int updateExFields(T obj,String fields) {
		return updateObj(obj,true,null,fields);
	}
		
	public int update(Map<String,Object>fieldMap,IExprSQL cond) {
		SQLBuilder builder = update();
		for(String str:fieldMap.keySet()) {
			OrmColumn col = ormbiz.toColumn(str);
			BaseField field = BaseField.make(col.getColName());
			field.setData(fieldMap.get(str));
			builder.addField(field);
		}
		builder.addCond(cond);
		return exec(builder);
	}
	
	/*
	 * 更新自定义字段和自定义条件
	 * */
	@Override
	public int update(T obj,IExprSQL cond) {
		List<BaseField> fieldList = ormbiz.toFieldList(obj,true,true,null,null);
		SQLBuilder builder = update();
		builder.addFields(fieldList);
		builder.addCond(cond);
		return exec(builder);
	}
	
	@Override
	public int delete(T obj) {
		if(obj == null) return 0;
		return delete(ormbiz.toExpr(obj));
	}

	@Override
	public int delete(Integer id) {
		return delete(ormbiz.toExpr(id));
	}

	//自定义条件
	public int delete(IExprSQL cond) {
		SQLBuilder builder = delete();
		builder.addCond(cond);
		return exec(builder);
	}
		
	/*************************************************
	 * 查询个体,主键作为条件，且不能为空
	 */
	@Override
	public T get(Integer id) {
		IExprSQL expr = ormbiz.toExpr(id);
		List<T> list = getList(null,expr,null);
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public T get(T obj) {
		List<T> list = getList(obj);
		return list.isEmpty()?null:list.get(0);
	}

	
	@Override
	public List<T> getList(T obj) {
		return getList(obj,null);
	}

	/*
	 * 查询所有字段
	 * 实体非空字段作为条件
	 * */
	@Override
	public List<T> getList(T obj, String orderBy) {
		List<T> list = getList(null,ormbiz.toExpr(obj),orderBy);
		return list;
	}

	public T get(String fields,IExprSQL expr){
		List<T> list = getList(fields,expr,null);
		return list.isEmpty()?null:list.get(0);
	}
	
	public List<T> getList(String fields,IExprSQL expr,String orderBy) {
		SelectBuilder builder = select();
		if(fields == null) fields = ormbiz.allColumn();
		builder.addField(fields);
		builder.addCond(expr);
		if(StringUtils.isEmpty(orderBy)) {//默认主键升序
			builder.order(new OrderBean(1,ormbiz.toIdColName(), "asc"));
		}else {
			builder.order(1,orderBy);
		}
		return query(builder);
	}
	
	//分空条件查询记录条数
	@Override
	public	int getCount(T obj) {
		SelectBuilder builder = select();	
		builder.addFuncField("count(1)", "cc");
		builder.addCond(ormbiz.toExpr(obj));
		return queryObject(builder, Integer.class);
	}
	
	//根据字段查询记录条数
	@Override
	public	int getCount(String field,Object vue) {
		return getCount(and().eq(field, vue));
	}
	
	public	int getCount(IExprSQL expr) {
		SelectBuilder builder = select();	
		builder.addFuncField("count(1)", "cc");
		builder.addCond(expr);
		return queryObject(builder, Integer.class);
	}
	
	/*
	 * 分页查询语句
	 * */
	public List<T> getPageList(PageBean<T> pb,SelectBuilder builder){
		String label = "t";
		if(builder.isFieldEmpty()) builder.addField(ormbiz.allColumn());
		SelectSQL selectSql = builder.build();
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuilder sb = selectSql.toSQLNoOrder(label, params);
		if(pb.isBcount()) {//查询记录总数
			StringBuilder countSb = null;
			if(selectSql.isDistinct()) {//去重
				countSb = new StringBuilder(sb);
				dbbiz.wrapCountSql(countSb);
			}else {
				params.clear();//清空
				countSb = selectSql.toSQLCount(label, params);
			}
			pb.setTotal(queryObject(countSb.toString(), params, Integer.class));
		}
		String orderStr = selectSql.toOrderString(label);
		dbbiz.wrapPageSql(pb, sb, orderStr);
		List<T> rows = query(sb.toString(),params);
		pb.setData(rows);
		return rows;
	}
	
	public List<T> getPageList(PageBean<T> pb,T obj,String orders){
		return getPageList(pb,obj,null,orders);
	}
	
	public List<T> getPageList(PageBean<T> pb,String cond,Object ...args){
		SelectBuilder builder = select();
		builder.addField(ormbiz.allColumn());
		SQLCondParse parse = new SQLCondParse(ormbiz);
		builder.addCond(parse.parseSQL(cond,args));
		return getPageList(pb,builder);
	}
	
	protected String allColumn() {
		return ormbiz.allColumn();
	}
	
	public List<T> getPageList(PageBean<T> pb,T obj,String fields,String orders){
		SelectBuilder builder = select();
		if(fields == null) fields = ormbiz.allColumn();//所有字段
		builder.addField(fields);
		builder.addCond(ormbiz.toExpr(obj));
		if(StringUtils.isEmpty(orders)) {//默认主键升序
			builder.order(new OrderBean(1,ormbiz.toIdColName(), "asc"));
		}else {
			builder.order(1,orders);
		}
		return getPageList(pb,builder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T rowObj = null;
		String colName = null;
		try {
			Set<String> colset = getResultSetColumns(rs);
			rowObj = (T) ormbiz.createInst();
			for(OrmColumn ormcol : ormbiz.toColList()){
				String colLabel = ormcol.matchEx(colset);
				if(colLabel != null){
					ormcol.setValue(rowObj, rs.getObject(colLabel));
				}
			}
		} catch (SQLException e) {
			log.error(colName + ",error:" + e.getMessage());
		} 
		return rowObj;
	}
	
	public List<T> query(String sqlstr,Map<String,Object>params) {
		log.debug(sqlstr);
		if(params != null && !params.isEmpty()) log.debug(params);
		return jdbcTemp.query(sqlstr, params, new RowMapper<T>() {
			private Set<String> colset = null;
			
			private JSONObject findJSON(JSONObject src_json,String[] xpaths) {
				JSONObject t_json = src_json;
				for(String xpath : xpaths) {
					JSONObject json = t_json.getJSONObject(xpath);
					if(json == null) {
						json = new JSONObject();
						t_json.put(xpath,json);
					}
					t_json = json;
				}
				return t_json;
			}
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				JSONObject json = new JSONObject();
				if(colset == null) {
					colset =  getResultSetColumns(rs);
				}
				for(String key : colset) {
					String xpath = null,label = key;
					int pos = key.lastIndexOf(".");
					JSONObject cJson = json;
					if(pos >= 0) {
						xpath = key.substring(0,pos);
						label = key.substring(pos + 1);
						cJson = findJSON(json, xpath.split("\\."));
					}
					String fieldName = label;
					OrmColumn ormcol = ormbiz.toColumn(label);
					if(ormcol != null) fieldName = ormcol.getFieldName();
					cJson.put(fieldName,rs.getObject(key));
				}
				return json.toJavaObject(entityCls);
			}
		});
	}
	
	public List<T> query1(String sqlstr,Map<String,Object>params) {
		log.debug(sqlstr + "==" + params);
		//if(params != null && !params.isEmpty()) log.debug(params);
		return jdbcTemp.query(sqlstr, params, this);
	}
	
	public List<T> query(SQLBuilder builder) {
		Map<String,Object>params = new HashMap<String, Object>();
		BaseSQL selectSql = builder.build();
		StringBuilder sb = selectSql.toSQL(params);
		return query(sb.toString(), params);
	}
		
	//查询实体
	public <TT> TT queryObject(SQLBuilder builder, Class<TT> requiredType) {
		try {
			Map<String,Object>params = new HashMap<String, Object>();
			BaseSQL selectSql = builder.build();
			StringBuilder sb = selectSql.toSQL(params);
			dbbiz.wrapOneSql(sb);
			if(logFlag) {
				log.debug(sb.toString());
				if(MapUtils.isNotEmpty(params)) log.debug(params);
			}
			return jdbcTemp.queryForObject(sb.toString(), params, requiredType);
		}catch(EmptyResultDataAccessException ex) {}
		return null;
	}
		
	//执行insert into , update ,delete语句
	public int exec(SQLBuilder builder) {
		BaseSQL baseSql = builder.build();
		Map<String,Object>params = new HashMap<String, Object>();
		StringBuilder sb = baseSql.toSQL(params);
		if(logFlag) {
			log.debug(sb.toString());
			log.debug(params);
		}
		return jdbcTemp.update(sb.toString(), params);
	}
		
	public int exec(String sqlstr,Map<String,Object>params) {
		if(logFlag) {
			log.debug(sqlstr);
			log.debug(params);
		}
		return jdbcTemp.update(sqlstr, params);
	}
	
	//查询实体
	public <TT> TT queryObject(String sql,Map<String,Object> params, Class<TT> requiredType) {
		try {
			if(logFlag) {
				log.debug(sql);
				if(MapUtils.isNotEmpty(params)) log.debug(params);
			}
			return jdbcTemp.queryForObject(sql, params, requiredType);
		}catch(EmptyResultDataAccessException ex) {}
		return null;
	}

	/*
	 * 解析业务类中的实体类
	 * */
	@SuppressWarnings("unchecked")
	private Class<T> getOrmBean(Class<?> tClass) {
		Type type = tClass.getGenericSuperclass();
		if(type instanceof ParameterizedType == false) return null;
		ParameterizedType pt = (ParameterizedType)type;
		Type[] tt = pt.getActualTypeArguments();
		if(tt.length > 0 && tt[0] instanceof Class) {
			return (Class<T>)tt[0];
		}
		return null;
	}

	//获取数据集合中的列
	private static  Set<String> getResultSetColumns(ResultSet rs ) {
		Set<String> colset = new HashSet<>();
    	ResultSetMetaData meta;
		try {
			meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
	    	//log.debug("开始");
	    	for(int i = 1;i <= colCount;i++){//获取所有列
	    		colset.add(meta.getColumnLabel(i));
	    		//log.debug(i + "," + meta.getColumnLabel(i));
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	//log.debug("结束");
    	return colset;
	}

	/*
	 * 检测实体类是否一致
	 * */
	public boolean checkClass(Class<?> tClass) {
		return ormbiz.checkClass(tClass);
	}

	protected IOrmBiz getOrmbiz() {
		return ormbiz;
	}
	
}
