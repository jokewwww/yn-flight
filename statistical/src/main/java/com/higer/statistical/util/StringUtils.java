package com.higer.statistical.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {
    /** 记录日志 */
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 是否为空判断
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
    
    /**
     * 是否为空判断
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o.toString().trim());
    }
    
    /**
     * 字段串数组第一组不为空
     * @param ss
     * @return
     */
    public static boolean firstNotEmpty(final String[] ss) {
        return ss != null && ss.length > 0 && ss[0] != null && ss[0].length() > 0;
    }
    
    /**
     * 将Object型转换为字符串
     * 
     * @param str
     * @return
     */
    public static String valueOf(Object o) {
        if (o == null) {
            return null;
        }
        String s = null;
        if (o instanceof Number) {
            s = String.valueOf(o);
        } else {
            s = o.toString();
        }
        return s;
    }

    /**
     * 取数组中的第一组值
     * 
     * @param obj
     * @return
     */
    public static String getFirstString(Object obj) {
        if (obj == null) {
            return "";
        }
        String s = null;
        if (obj instanceof String[]) {
            String[] ss = (String[]) obj;
            s = ss[0];
        } else if (obj instanceof String) {
            s = (String) obj;
        } else {
            s = obj.toString();
        }
        return s;
    }

    /**
     * 获取数组的第一个元素，并且作了字符串null、""的判断，这两种情况都处理为null
     * 
     * @param obj
     * @return
     */
    public static String getFirstStr(Object obj) {
        if (obj == null) {
            return null;
        }
        String tmp = null;
        if (obj instanceof String[]) {
            String[] ss = (String[]) obj;
            tmp = ss[0];
        } else if (obj instanceof String) {
            tmp = (String) obj;
        }
        if ("".equals(tmp)) {
            tmp = null;
        }
        return tmp;
    }

    /**
     * 功能描述: 驼峰字符串转换成下划线连接<br>
     * 例如: nextValueMySql 转换为 next_value_my_sql
     * 
     * @param param
     * @return
     */
    public static String camelVunderline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }


    /**
     * 功能描述: 下划线字符串转换成驼峰连接<br>
     * 例如: next_value_my_sql 转换为 nextValueMySql 
     * 
     * @param param
     * @return
     */
    public static String underlineVcamel(String name) {
    	StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
    
    /**
     * 将Null处理为空字符串
     * 
     * @param str
     * @return
     */
    public static String toEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
    
	/**
	 * 删除最后的逗号
	 */
	public static StringBuilder removeLastChar(String sb){
		if(sb.trim().endsWith(",")){
			sb = sb.substring(0, sb.lastIndexOf(","));
		}
		return new StringBuilder(sb);
	}
	/**
	 * 删除最后符号
	 */
	public static StringBuilder removeLastCharStop(String sb,String last){
		if(sb.trim().endsWith(last)){
			sb = sb.substring(0, sb.lastIndexOf(last));
		}
		return new StringBuilder(sb);
	}
	/**
	 * @author jianglijie
	 * add by 2017-01-04
	 * 阿拉伯数字转汉字
	 * @param str
	 * @return
	 */
	public static String getChinese(String str){
	    StringBuilder sb= new StringBuilder();
	    for(int i = 0; i < str.length(); i++){
	        char c = str.charAt(i);
	        switch(c){
	            case '0':sb.append("零");break;
	            case '1':sb.append("一");break;
	            case '2':sb.append("二");break;
	            case '3':sb.append("三");break;
	            case '4':sb.append("四");break;
	            case '5':sb.append("五");break;
	            case '6':sb.append("六");break;
	            case '7':sb.append("七");break;
	            case '8':sb.append("八");break;
	            case '9':sb.append("九");break;
	            default :sb.append(c);
	        }
	    }
	    return sb.toString();
	}
}
