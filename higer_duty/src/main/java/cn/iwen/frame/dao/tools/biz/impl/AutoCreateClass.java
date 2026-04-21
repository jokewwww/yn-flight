package cn.iwen.frame.dao.tools.biz.impl;


import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.tools.bean.ConvertColumn;
import cn.iwen.frame.dao.tools.bean.ConvertInfo;


/*
 * 根据表结构，生成代码
 * */
public class AutoCreateClass {

	private static Log log = LogFactory.getLog(AutoCreateClass.class);   
	
	
	//private static String BASE_PACKAGE = "cn.iwen.frame.dao";
	
	private static String SRC_PATH = "java/";
	
	
	private ConvertInfo info;
	
	private String idName;//主键名称
	
	private String packName;
	private String entityName;//类名
	private String lowerEntityName;//类名
	
	private String entityAllName;//类路径
	
	//private String iDaoName;
	//private String iDaoAllName;
	
	String daoName;
	String daoAllName;
	
	private String iBizName;
	private String iBizAllName;
	
	String bizName;
	String bizAllName;
	
	String webName;
	String webAllName;
	
	
		
	/*
	 * 框架重构
	 *  去掉dao层
	 * */
	public AutoCreateClass(ConvertInfo info){
		this.info = info;
		packName = info.getPackName();
		entityName = info.getClassName();
		lowerEntityName = ToolsUtils.firstToLowerCase(entityName);
		
		bizName =  entityName + "BizImpl";
		bizAllName = packName + ".biz.impl." + bizName;
		
		iBizName = "I" + entityName + "Biz";
		iBizAllName = info.getPackName() + ".biz." + iBizName;
		
		daoName = entityName + "DaoImpl";
		daoAllName = info.getPackName() + ".dao.impl." + daoName;
		
		//iDaoName = "I" + info.getClassName() + "Dao";
		//iDaoAllName = info.getPackName() + ".dao." + iDaoName;
		
		entityAllName = info.getPackName() + ".entity." + entityName;
		
		webName = entityName + "Action";
		webAllName = packName + ".web." + webName;
		
		idName = getIdName();
	}
	
	private String getIdName(){
		for(ConvertColumn cc : info.getColList()){
			if(cc.isID()) return cc.getFieldName();
		}
		return null;
	}
	
	//提取包名称
	private  String getPackageName(){
		return packName.substring(packName.lastIndexOf(".") + 1);
	}
	private String readFileString(String name) {
		InputStream in = this.getClass().getResourceAsStream("../../" + name);
		return new String(BaseUtils.readbodydata(in, 0));
	}
	
	//保存js文件
	public void saveJavaScript(){
		String filePath = info.getSavePath();
		if (!filePath.endsWith("/"))	filePath += "/";
		InputStream in = this.getClass().getResourceAsStream("file/ui.js");
		String fstr = new String(BaseUtils.readbodydata(in, 0));
		List<String> cols = new ArrayList<>();
		int rowCount = 0;
		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			String fieldName = col.getComment();//注解
			if(StringUtils.isEmpty(fieldName)) fieldName = fname;
			String editor = null;
			if(col.isID()){//主键隐藏
				editor = "hidden:true";
			}else{
				editor = col.isNullable()?"editor:{}":"editor:{required:true}";
				rowCount++;
			}
			cols.add(String.format("{field:'%s',title:'%s',%s}"
					, fname,fieldName,editor));
		}
		int height = rowCount * 30 + 100;
		fstr = MessageFormat.format(fstr, 
				String.join(",\n\t\t\t", cols)
				,idName
				,getPackageName() + "/" +  entityName.toLowerCase()
				,height);
		filePath += "webapp/js/" + getPackageName() + "/" 
					+ entityName +".js";
		if (BaseUtils.writeFile(filePath, fstr))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
	
	//保存js文件
	public void saveJavaScript1(){
		
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";

		filePath += "webapp/js/" + getPackageName() + "/"+ entityName.toLowerCase() +".js";
		
		StringBuilder strFile = new StringBuilder();
		strFile.append("\n\ndefine(function(require,exports,module){\n\n");//ADM模块化
		
		strFile.append("\tfunction Main(jqObj,loadParams){\n");

		String packName = getPackageName() + "/" +  entityName.toLowerCase();
		strFile.append("\n");
		strFile.append("\tvar columns = [[\n");
		int size = info.getColList().size();
		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			String fieldName = col.getComment();//注解
			if(StringUtils.isEmpty(fieldName)) fieldName = fname;
			StringBuilder  tmpbuf = new StringBuilder();
			tmpbuf.append("\t\t{field:'"+fname+"',title:'"+fieldName+"'");
			if(col.isID()){//主键隐藏
				tmpbuf.append(",hidden:true");
			}else{
				if(col.isNullable()) tmpbuf.append(",editor:{}");//可为空
				else tmpbuf.append(",editor:{required:true}");
			}
			if(--size == 0) tmpbuf.append("}\n");//最后一行
			else tmpbuf.append("},\n");
			strFile.append(tmpbuf);
			
		}
		strFile.append("\t]];\n");  
		int height = info.getColList().size() * 50;
		strFile.append("\tvar formDialog = {\n");
		strFile.append("\t\ttitle:'',\n");
		strFile.append("\t\theight:" + height + ",\n");
		strFile.append("\t\tformEx:{\n");
		strFile.append("\t\t\turl:'"+packName+"/save.do'\n");       
		strFile.append("\t\t}\n"); 
		strFile.append("\t}\n"); 
		strFile.append("\n");
		strFile.append("\tvar MainUI = {\n");
		strFile.append("\t\teName:'datagrid',\n");
		strFile.append("\t\tidField:'"+idName+"',\n");
		strFile.append("\t\turl:'"+packName+"/json.do',\n");
		strFile.append("\t\tdelUrl:'"+packName+"/delete.do',\n");
		strFile.append("\t\tcolumns:columns,\n");
		strFile.append("\t\tformDialog:formDialog\n");       
		strFile.append("\t}\n");  
		strFile.append("\n");
		strFile.append("\tjqObj.createUI(MainUI);\n");
		
		strFile.append("\n");
		strFile.append("}\n\tmodule.exports = Main;\n");
		strFile.append("\n");
		strFile.append("});\n");
		
		if (BaseUtils.writeFile(filePath, strFile.toString()))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
				
		
	//保存DAO层接口文件
	public void saveJspView(){
		
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";

		filePath += "webapp/WEB-INF/" + getPackageName() + "/"+ entityName +"List.jsp";
		
		StringBuilder strFile = new StringBuilder();
		strFile.append("<%@ page language=\"java\" contentType=\"text/html; charset=utf-8\" pageEncoding=\"utf-8\"%>\n");
		
		strFile.append("<%@ include file=\"/WEB-INF/taglib.jsp\"%>\n");
		
		strFile.append("\n");
		strFile.append("\n");
		String packName = getPackageName() + "/" +  entityName.toLowerCase();
		/*
		 * easyui datagrid
		 * */
		strFile.append("<table class=\"easyui-datagrid\"\n");
		strFile.append("\tdata-options=\"formHeight:315,formTitle:'',\n");
		strFile.append("\t\turl:'"+packName+"/json.do',editUrl:'"+packName+"/edit.do',\n");
		strFile.append("\t\tsaveUrl:'"+packName+"/save.do',delUrl:'"+packName+"/del.do',\n");
		strFile.append("\t\tfit:true,singleSelect:true,idField:'"+idName+"',pagination:true,border:false\">\n");
		strFile.append("\t<thead>\n\t\t<tr>\n");
		
		//遍历所有域
		StringBuilder  keyField = new StringBuilder();
		StringBuilder  noKeyField = new StringBuilder();
		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			String fieldName = col.getComment();//注解
			if(StringUtils.isEmpty(fieldName))
				fieldName = fname;
			if(col.isID()){//主键隐藏
				keyField.append("\t\t\t<th data-options=\"field:'"+fname 
						+ "',hidden:true\">"+fieldName+"</th>\n");
				continue;
			}
			StringBuilder  filedStr = new StringBuilder();
			filedStr.append("\t\t\t<th data-options=\"field:'"+fname + "',align:'center',width:130");
			filedStr.append("\">"+fieldName+"</th>\n");
			noKeyField.append(filedStr);
		}
		
		strFile.append(keyField);
		strFile.append(noKeyField);
		
		strFile.append("\t\t</tr>\n\t</thead>\n</table>\n");
		
		strFile.append("<div class='datagrid-toolbar'>\n");
		strFile.append("  <form>\n");
		strFile.append("\t<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add',plain:true,uId:'add'\">新增</a>\n");
		strFile.append("\t<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-edit',plain:true,uId:'modify'\">修改</a>\n");
		strFile.append("\t<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-remove',plain:true,uId:'del'\">删除</a>\n");
		strFile.append("  </form>\n");
		strFile.append("</div>\n");
		
		strFile.append("\n");
		strFile.append("\n");
		
		
		strFile.append("<script type=\"text/javascript\" src=\"js/"+getPackageName() + 
				"/"+ entityName.toLowerCase() + ".js\"></script>\n");
		
		strFile.append("\n");
		strFile.append("\n");
		

		if (BaseUtils.writeFile(filePath, strFile.toString()))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
			
	//保存JSP表单编辑页面
	public void saveEditJspView(){
		
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";

		filePath += "webapp/WEB-INF/" + getPackageName() + "/"+ entityName +"Edit.jsp";
		
		StringBuilder strFile = new StringBuilder();
		strFile.append("<%@ page language=\"java\" contentType=\"text/html; charset=utf-8\" pageEncoding=\"utf-8\"%>\n");
		
		strFile.append("<%@ include file=\"/WEB-INF/taglib.jsp\"%>\n");
		
		strFile.append("\n");
		strFile.append("\n");
		//页面变量
		strFile.append("<c:set var=\"width\" value=\"150\"/>\n");
		/*
		 * 页面布局
		 * */
		strFile.append("<div class=\"dialog_element_layout\">\n");
		strFile.append("\t<form:form class=\"dialog_form\" commandName=\""+ lowerEntityName +"\">\n");
		
		//遍历所有域
		StringBuilder  keyField = new StringBuilder();
		StringBuilder  noKeyField = new StringBuilder();
		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			String fieldName = col.getComment();//注解
			if(StringUtils.isEmpty(fieldName))
				fieldName = fname;
			if(col.isID()){//主键隐藏
				keyField.append("\t\t<form:hidden path=\""+fname+ "\"/>\n");
				continue;
			}
			StringBuilder  filedStr = new StringBuilder();
			filedStr.append("\t\t\t<tr>\n");
			filedStr.append("\t\t\t\t<td  class=\"td_left\">"+fieldName+"</td>\n");
			filedStr.append("\t\t\t\t<td class=\"td_right\">\n");
			String fieldType = col.getFieldType();
			String required = "";
			if(col.isNullable() == false){
				required = ",required:true";
			}
			if("String".equals(fieldType)){//字符类型
				filedStr.append("\t\t\t\t\t<form:input path=\""+fname
						+"\" cssClass=\"easyui-textbox\" data-options=\"width:${width}"+required+"\"/>\n");
			}else if("Integer".equals(fieldType)){
				filedStr.append("\t\t\t\t\t<form:input path=\""+fname
						+"\" cssClass=\"easyui-numberbox\" data-options=\"width:${width}"+required+"\"/>\n");
			}else if("java.util.Date".equals(fieldType)){
				filedStr.append("\t\t\t\t\t<form:input path=\""+fname
						+"\" cssClass=\"easyui-datebox\" data-options=\"width:${width}"+required+"\"/>\n");
			}
			filedStr.append("\t\t\t\t</td>\n");
			filedStr.append("\t\t\t</tr>\n");
			noKeyField.append(filedStr);
		}
		//隐藏域
		strFile.append(keyField);
		strFile.append("\t\t<table class=\"table_form_edit\">\n");
		strFile.append(noKeyField);
		strFile.append("\t\t</table>\n");
		strFile.append("\t</form:form>\n");
		strFile.append("</div>\n");
		
		strFile.append("<div class=\"dialog_button_layout\">\n");
		strFile.append("\t<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"uId:'save'\">确定</a>\n");
		strFile.append("\t<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"uId:'cancel'\">取消</a>\n");
		strFile.append("</div>\n");
		
		strFile.append("\n");
		strFile.append("\n");

		if (BaseUtils.writeFile(filePath, strFile.toString()))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
				
	//action控制层代码
	public  void saveActionClass() {
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";
		filePath += SRC_PATH + webAllName.replaceAll("\\.", "/") + ".java";
		 
		String temp = readFileString("file/web.txt");
		String fileStr = MessageFormat.format(temp
				,packName
				,entityAllName
				,iBizAllName
				,webName
				,iBizName
				,ToolsUtils.firstToLowerCase(iBizName)
				,entityName.toLowerCase()
				,entityName
				,lowerEntityName
				,getPackageName());

		if (BaseUtils.writeFile(filePath, fileStr))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
	//保存BIZ层类文件
	public void saveBizClass() {
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";
		
		filePath += SRC_PATH +  bizAllName.replaceAll("\\.", "/") + ".java";
		String temp = readFileString("file/biz.txt");
		String fileStr = MessageFormat.format(temp,packName,entityAllName,iBizAllName,bizName,entityName,iBizName);

		if (BaseUtils.writeFile(filePath,fileStr))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
	//保存BIZ层接口文件
	public void saveIBizClass(){
		//确定文件路径
		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";
		filePath +=  SRC_PATH + iBizAllName.replaceAll("\\.", "/") + ".java";
		
		String temp = readFileString("file/ibiz.txt");
		String fileStr = MessageFormat.format(temp,packName,entityAllName,iBizName,entityName);

		if (BaseUtils.writeFile(filePath, fileStr))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
	
	// 保存实体类
	public void saveEntityClass() {

		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";
		filePath +=  SRC_PATH + entityAllName.replaceAll("\\.", "/") + ".java";
		
		

		// log.debug("文件路径:" + filePath);
		StringBuilder strFile = new StringBuilder();
		StringBuilder setgetBuf = new StringBuilder();

		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			// 字段注释
			strFile.append("\t/**\n");
			strFile.append("\t*" + col.getComment() + "\n");
			strFile.append("\t*/\n");
			// 注解
			if (col.isID()) // 主键
				strFile.append("\t@Id\n");
			if (col.isAutoIncrement()) // 自增
				strFile.append("\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
			if (col.isNullable())// 可否为NULL
				strFile.append("\t@Column(name = \"" + col.getColName()
						+ "\")\n");
			else
				strFile.append("\t@Column(name = \"" + col.getColName()
						+ "\",nullable=false)\n");
			if("java.util.Date".equals(col.getFieldType())){
				strFile.append("\t@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)\n");
			}
			strFile.append("\tprivate " + col.getFieldType() + " " + fname
					+ ";\n");
			strFile.append("\n");

			// getter and setter
			setgetBuf.append("\tpublic " + col.getFieldType() + " get"
					+ ToolsUtils.captureName(fname) + "() {\n");
			setgetBuf.append("\t\treturn " + fname + ";\n");
			setgetBuf.append("\t}\n");
			setgetBuf.append("\n");

			setgetBuf.append("\tpublic void set" + ToolsUtils.captureName(fname)
					+ "(" + col.getFieldType() + " " + fname + ") {\n");
			setgetBuf.append("\t\tthis." + fname + "=" + fname + ";\n");
			setgetBuf.append("\t}\n");
			setgetBuf.append("\n");

		}

		String temp = readFileString("file/bean.txt");
		String fileStr = MessageFormat.format(temp
				,packName
				,info.getTableName()
				,entityName
				,strFile.toString()
				,setgetBuf.toString());

		if (BaseUtils.writeFile(filePath, fileStr))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
		
	// 保存实体类
	public void saveEntityClass1() {

		String filePath = info.getSavePath();
		if (filePath.endsWith("/") == false)
			filePath += "/";

		filePath +=  SRC_PATH + entityAllName.replaceAll("\\.", "/") + ".java";
		
		// log.debug("文件路径:" + filePath);
		StringBuilder strFile = new StringBuilder();
		StringBuilder setgetBuf = new StringBuilder();

		// 包名
		strFile.append("package " + packName + ".entity;\n");
		strFile.append("\n");

		// import
		// strFile.append("import java.util.*;\n");
		strFile.append("import javax.persistence.*;\n");
		strFile.append("\n");
		strFile.append("\n");
		// 类名
		strFile.append("@Table(name=\"" + info.getTableName() + "\")\n");
		strFile.append("public class " + entityName + " {\n");

		for (ConvertColumn col : info.getColList()) {
			String fname = col.getFieldName();
			// 字段注释
			strFile.append("\t/**\n");
			strFile.append("\t*" + col.getComment() + "\n");
			strFile.append("\t*/\n");
			// 注解
			if (col.isID()) // 主键
				strFile.append("\t@Id\n");
			if (col.isAutoIncrement()) // 自增
				strFile.append("\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
			if (col.isNullable())// 可否为NULL
				strFile.append("\t@Column(name = \"" + col.getColName()
						+ "\")\n");
			else
				strFile.append("\t@Column(name = \"" + col.getColName()
						+ "\",nullable=false)\n");
			if("java.util.Date".equals(col.getFieldType())){
				strFile.append("\t@com.alibaba.fastjson.annotation.JSONField(format=\"yyyy-MM-dd  HH:mm:ss\")\n");
			}
			strFile.append("\tprivate " + col.getFieldType() + " " + fname
					+ ";\n");
			strFile.append("\n");

			// getter and setter
			setgetBuf.append("\tpublic " + col.getFieldType() + " get"
					+ ToolsUtils.captureName(fname) + "() {\n");
			setgetBuf.append("\t\treturn " + fname + ";\n");
			setgetBuf.append("\t}\n");
			setgetBuf.append("\n");

			setgetBuf.append("\tpublic void set" + ToolsUtils.captureName(fname)
					+ "(" + col.getFieldType() + " " + fname + ") {\n");
			setgetBuf.append("\t\tthis." + fname + "=" + fname + ";\n");
			setgetBuf.append("\t}\n");
			setgetBuf.append("\n");

		}

		strFile.append("\n");
		strFile.append("\n");

		strFile.append(setgetBuf);

		// 类结束符
		strFile.append("}\n");

		if (BaseUtils.writeFile(filePath, strFile.toString()))
			log.debug(filePath + "写入磁盘成功");
		else
			log.debug(filePath + "写入磁盘失败");
	}
	
}
