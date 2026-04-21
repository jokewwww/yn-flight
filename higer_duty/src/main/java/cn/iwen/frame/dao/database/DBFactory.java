package cn.iwen.frame.dao.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import cn.iwen.frame.dao.BaseDao;
import cn.iwen.frame.dao.bean.OrmDBType;
import cn.iwen.frame.dao.database.impl.MySQLBizImpl;
import cn.iwen.frame.dao.database.impl.OracleBizImpl;
//import cn.iwen.frame.dao.database.impl.SQLServerBizImpl;
import cn.iwen.frame.dao.tools.biz.IAutoToolsBiz;

@Component
public class DBFactory implements ApplicationListener<ContextRefreshedEvent>{

	private static Log log = LogFactory.getLog(DBFactory.class);
	private Map<String,OrmDBType> dbtypeMap = new HashMap<>();
	private List<BaseDao<?>> daolist = new ArrayList<>();
	private OrmDBType dbType;
	
	@Autowired
	private IAutoToolsBiz toolsbiz;
	
	public BaseDataBaseBiz create(DataSource dataSource,BaseDao<?> basedao){
		daolist.add(basedao);
		OrmDBType ormdb = createOrmDB(dataSource);
		BaseDataBaseBiz orm = null;
		if(ormdb.isMysql()) {
			orm = new MySQLBizImpl();
		}else if(ormdb.isOracle()) {
			orm = new OracleBizImpl();
		}else if(ormdb.isMssql()) {
			//orm = new SQLServerBizImpl();
		}
		dbType = ormdb;
		return orm;
	}
	
	public BaseDao<?> findBaseDao(Class<?> cls) {
		for(BaseDao<?> dao : daolist) {
			if(dao.checkClass(cls)) return dao;
		}
		return null;
	}
	private OrmDBType createOrmDB(DataSource dataSource){
		String code = dataSource.hashCode() + "";
		OrmDBType ormdb = dbtypeMap.get(code);
		if(ormdb == null){
			ormdb = new OrmDBType(dataSource);
			dbtypeMap.put(code,ormdb);
		}
		return ormdb;
	}

	//spring容器初始化完成执行此方法
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() != null) {
			log.debug("spring frame init over2");
			org.springframework.context.ApplicationContext app = event.getApplicationContext();
			String[] strs = app.getBeanNamesForAnnotation(Controller.class);
			//log.debug("----------" + JSON.toJSONString(app.getBeansWithAnnotation(Controller.class)));
			for(String str : strs) {
				toolsbiz.extractMethod(app.getType(str));
			}
		}
	}

	public OrmDBType getDbType() {
		return dbType;
	}
	
}
