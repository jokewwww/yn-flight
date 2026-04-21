package com.higer.read_kafka.util;

import com.higer.read_kafka.configuration.ChangeStatus;
import com.higer.read_kafka.configuration.OperationLog;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/12/7 16:54
 * @Description: 实体类对比方法
 */
@Component
public class CompareObjectUtil {

    /**
    * @Description:
    * @Param: [obj1 老数据, Obj2 新数据]
    * @return: java.lang.String
    * @Author: XiuHongXin
    * @Date: 2019/3/8
    */
    public static List<Map<String,Object>> compare(Object obj1, Object Obj2)
            throws Exception {
        try {
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            //获取所有属性
            Field[] fs = obj1.getClass().getDeclaredFields();
            for (Field f : fs) {
                Map<String, Object> result = new HashMap<String, Object>();
                //设置访问性，反射类的方法，设置为true就可以访问private修饰的东西，否则无法访问
                f.setAccessible(true);
                String name = f.getName();
                OperationLog annotation = f.getAnnotation(OperationLog.class);
                if (annotation != null) {
                    String entityType = f.getType().getName();
                    if (annotation != null) {
                        Object v1 = f.get(obj1);//数据库
                        Object v2 = f.get(Obj2);//新
                        if(v1 != null && v2 != null){
                            if ("java.util.Date".equals(entityType)) {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                v1 = df.format(v1);
                                v2 = df.format(v2);
                            }
                            if (!equals(v1, v2)) {
                                result.put("fieldName",name);
                                result.put("oldValue",String.valueOf(v1));
                                result.put("newValue",String.valueOf(v2));
                                result.put("timeStamp",DateUtil.getCurrentDateTimeStr());
                            }

                        } else if(null != v1 || null != v2){
                            result.put("fieldName",name);
                            result.put("oldValue",null== v1 ? null : String.valueOf(v1));
                            result.put("newValue",null== v2 ? null : String.valueOf(v2));
                            result.put("timeStamp",DateUtil.getCurrentDateTimeStr());
                        }
                        if(result.size() > 0){
                            lists.add(result);
                        }
                    }
                }

            }
            return lists;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static List<Map<String,Object>> compareStatus(Object obj1, Object Obj2)
            throws Exception {
        try {
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            //获取所有属性
            Field[] fs = obj1.getClass().getDeclaredFields();
            for (Field f : fs) {
                Map<String, Object> result = new HashMap<String, Object>();
                //设置访问性，反射类的方法，设置为true就可以访问private修饰的东西，否则无法访问
                f.setAccessible(true);
                String name = f.getName();
                ChangeStatus annotation = f.getAnnotation(ChangeStatus.class);
                if (annotation != null) {
                    String entityType = f.getType().getName();
                    if (annotation != null) {
                        Object v1 = f.get(obj1);
                        Object v2 = f.get(Obj2);
                        if(v1 != null && v2 != null){
                            if (!equals(v1, v2)) {
                                result.put("fieldName",name);
                            }
                            if(result.size() > 0){
                                lists.add(result);
                            }
                        }
                    }
                }

            }
            return lists;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static boolean equals(Object obj1, Object obj2) {

        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }


}
