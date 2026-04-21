package cn.iwen.frame.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/*
 * 继承servlet filter
 */
//@WebFilter(filterName="filter1", urlPatterns="/*")  
final public class SysFilter extends BaseFilter {

	
	public static final String MENUS = "MENUS";//用户菜单key
	
	public void init1(FilterConfig fc) throws ServletException {
		super.init(fc);
	}
	
	public SysFilter(){
		log.debug("init filter");
		//上传附件过滤器
		//regFilter(new UpLoadFilterImpl());
		//访问权限过滤器
		super.regFilter(new AuthFilterImpl());
		
		//super.regFilter(new ResourceFilterImpl());
	}
}
