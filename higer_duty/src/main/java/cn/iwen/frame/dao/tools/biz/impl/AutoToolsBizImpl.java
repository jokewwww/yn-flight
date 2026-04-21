package cn.iwen.frame.dao.tools.biz.impl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.bean.OrmDBType;
import cn.iwen.frame.dao.database.DBFactory;
import cn.iwen.frame.dao.tools.bean.ConvertColumn;
import cn.iwen.frame.dao.tools.bean.ConvertInfo;
import cn.iwen.frame.dao.tools.bean.TestCase;
import cn.iwen.frame.dao.tools.bean.UrlMethod;
import cn.iwen.frame.dao.tools.biz.IAutoToolsBiz;

@Service
public class AutoToolsBizImpl  implements IAutoToolsBiz {

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DBFactory df;
	
	@Override
	public List<String> getTables() {
		List<String> tablelist = new ArrayList<String>();
		Connection conn = null;
		OrmDBType dbtype = df.getDbType();
		DataSource ds = dbtype.getDataSource();
		
		try {
			conn = ds.getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			ResultSet tableSet = null;
			if(dbtype.isOracle()){
				String schem = dbtype.getSchem();			
				tableSet = databaseMetaData.getTables(null, schem, "%", 
						new String[]{"TABLE"});
			}else{
				tableSet = databaseMetaData.getTables(null, "%", "%",
						new String[]{"TABLE"});
			}
			while(tableSet.next()){
				String tableName = tableSet.getString("TABLE_NAME");
				if(dbtype.isOracle()){
					if(tableName.indexOf("==$0") != -1) continue;
				}
				tablelist.add(tableName);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tablelist;
	}

	
	//获取表中所有列信息
	private List<ConvertColumn> getColumnInfo(String tableName) {
		List<ConvertColumn> colList = new ArrayList<>();
		OrmDBType dbtype = df.getDbType();
		DataSource ds = dbtype.getDataSource();
		DatabaseMetaData databaseMetaData = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			databaseMetaData = conn.getMetaData();
			ResultSet columnSet = null;
			if(dbtype.isOracle()){
				columnSet = databaseMetaData.getColumns(null, dbtype.getSchem(), 
						tableName, "%");
			}else{
				columnSet = databaseMetaData.getColumns(null, "%", tableName, "%");
			}
			//获取所有列
			while(columnSet.next()){
				//log.debug(columnSet.getString("TABLE_CAT") +","+ columnSet.getString("TABLE_SCHEM"));
				ConvertColumn col = new ConvertColumn();
				String colName = columnSet.getString("COLUMN_NAME");
				col.setColName(colName);
				col.setComment(columnSet.getString("REMARKS"));
				String typeName = columnSet.getString("TYPE_NAME").toLowerCase();
				col.setColType(typeName);
				//log.debug(colName + "=" +columnSet.getInt("DATA_TYPE") +"="+ typeName);
				//是否为空
				col.setNullable("YES".equals(columnSet.getString("IS_NULLABLE")));
				if(dbtype.isOracle()){
					col.setAutoIncrement(false);
				}else if(dbtype.isMssql()){//判断是否主键
					if(typeName.endsWith(" identity"))
						col.setAutoIncrement(true);//自增
				}else{
					col.setAutoIncrement("YES".equals(columnSet.getString("IS_AUTOINCREMENT")));
				}
				col.convert();//表列--》 字段的转换
				colList.add(col);
			}
			//获取主键
			ResultSet pkCols = null;
			if(dbtype.isOracle()){
				pkCols = databaseMetaData.getPrimaryKeys(null, dbtype.getSchem(),
						tableName);
			}else if(dbtype.isMssql()){
				pkCols = databaseMetaData.getPrimaryKeys(null, "dbo", tableName);
			}else{
				pkCols = databaseMetaData.getPrimaryKeys(null, "%", tableName);
			}
			if(pkCols == null) return colList;
			while(pkCols.next()){
				//test(pkCols);
				String colName = pkCols.getString("COLUMN_NAME");
				log.debug("主键:" + colName);
				for(ConvertColumn col : colList){
					if(colName.equals(col.getColName())){
						col.setID(true);//设置为主键
					}
				}
			}
		} catch (SQLException e) {
			log.debug(BaseUtils.log4stack(e));
		}finally{
			if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return colList;
	}
		
	//转换
	public int convert(List<ConvertInfo> infolist){
		//遍历表
		for(ConvertInfo info : infolist){
			info.convert();//表转换
			List<ConvertColumn> collist = getColumnInfo(info.getTableName());
			info.setColList(collist);
			log.debug("包名：" + info.getPackName() + ",表名：" + info.getTableName()
					+ ",类名：" + info.getClassName());
		}
		saveOrmFile(infolist);
		return 0;
	}
				
	//保存
	private void saveOrmFile(List<ConvertInfo> infolist){
		//遍历表
		for(ConvertInfo info : infolist){
			if(info.getColList().isEmpty()) continue;
			AutoCreateClass acc = new AutoCreateClass(info);
			acc.saveEntityClass();
			acc.saveIBizClass();
			acc.saveBizClass();
			//acc.saveIDaoClass();
			//acc.saveDaoClass();
			acc.saveActionClass();

			//acc.saveJspView();
			//acc.saveEditJspView();
			//acc.saveJavaScript();
			//acc.saveLayoutXml();
		}
	}

	/*
	 * ACTION测试相关方法
	 * */
	private List<UrlMethod> methodList = new ArrayList<UrlMethod>();
	
	@Override
	public void extractMethod(Class<?> actionCls) {
		log.debug("action:" + actionCls.getSimpleName() + "," + actionCls.getTypeName());
		String pktName = actionCls.getPackage().getName();
		String clsName = actionCls.getSimpleName();
		if(pktName.endsWith(".web")) pktName = pktName.substring(0, pktName.length() - ".web".length());
		log.debug("package:" + pktName);
		String url1 = "",url2 = null;
		String[] vues = null;
		RequestMapping reqMap = actionCls.getAnnotation(RequestMapping.class);
		if(reqMap != null) {
			vues = reqMap.value();
			if(vues.length > 0) {
				url1 = vues[0];
				if(url1.endsWith("/")) {
					url1 = url1.substring(0, url1.length() - 1);
				}
				if(url1.startsWith("/")) url1 = url1.substring(1,url1.length());
			}
		}
		Method[] methods = actionCls.getMethods();
		for(Method method : methods) {
			UrlMethod urlmethod = new UrlMethod();
			reqMap = method.getAnnotation(RequestMapping.class);
			if(reqMap != null) {
				vues = reqMap.value();
				urlmethod.setMethodName(method.getName());
				if(vues.length > 0) {
					url2 = vues[0];
					if(!url2.startsWith("/")) url2 = "/" + url2;
					url2 = url1 + url2;
					urlmethod.setPktName(pktName);
					urlmethod.setClsName(clsName);
					if(url2.startsWith("/")) url2 = url2.replaceFirst("/", "");
					urlmethod.setUrl(url2);
				}
				RequestMethod[]requMethods = reqMap.method();
				if(requMethods.length > 0) {
					urlmethod.setMethodType(requMethods[0].toString());
				}else {
					urlmethod.setMethodType(RequestMethod.POST.toString());
				}
				if(urlmethod.getUrl() != null) {
					TestCase testCase = method.getAnnotation(TestCase.class);
					String params = "{}";
					if(testCase != null) {
						try {
							Object obj = JSON.parse(testCase.value());
							params = obj.toString();
						}catch(Exception ex) {
						}
					}
					urlmethod.setParams(params);
					methodList.add(urlmethod);
				}
			}
		}
		log.debug("====");
	}

	@Override
	public JSONObject getWebMethods() {
		JSONObject pktJson = new JSONObject();
		for(UrlMethod urlmethod : methodList) {
			//包
			JSONObject clsJson = pktJson.getJSONObject(urlmethod.getPktName());
			if(clsJson == null) {
				clsJson = new JSONObject();
				pktJson.put(urlmethod.getPktName(), clsJson);
			}
			//类
			JSONObject actionJson = clsJson.getJSONObject(urlmethod.getClsName());
			if(actionJson == null) {
				actionJson = new JSONObject();
				clsJson.put(urlmethod.getClsName(), actionJson);
			}
			//方法
			actionJson.put(urlmethod.getUrl(), urlmethod);
		}
		return pktJson;
	}
	
}
