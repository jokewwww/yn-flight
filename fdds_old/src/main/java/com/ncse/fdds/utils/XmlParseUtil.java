package com.ncse.fdds.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ncse.fdds.annotation.JSONNeed;
import com.ncse.fdds.annotation.XMLValue;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlParseUtil {
    /**
     * 反射设置实体不同类型字段的值 <暂时只支持 日期 字符串 boolean Integer值设置 待扩建>
     *
     * @param field
     * @param obj
     * @param value
     * @throws Exception
     */
    public static void convertValue(Field field, Object obj, String value)
            throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        if (field.getGenericType().toString().equals("class java.lang.Integer")) {
            field.set(obj, Integer.parseInt(value));
        } else if (field.getGenericType().toString().equals("boolean")) {
            field.set(obj, Boolean.parseBoolean(value));
        } else if (field.getGenericType().toString().equals(
                "class java.util.Date")) {
            field.set(obj, sim.parse(value));
        } else {
            field.set(obj, value);
        }

    }

    private static <T> ObjectNode parseObj(T Obj, ObjectMapper mapper) throws IllegalAccessException {
        ObjectNode result = mapper.createObjectNode();
        Class<?> c = Obj.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            //如果包含xmlvalue注解
            if (field.isAnnotationPresent(XMLValue.class) || field.isAnnotationPresent(JSONNeed.class)) {
                XMLValue xmlValue = field.getAnnotation(XMLValue.class);
                field.setAccessible(true);
                Object param = field.get(Obj);
                //如果需要的json字段已经有值，则不会赋值为空
                if (param == null) {
                    if (result.get(xmlValue.value()) == null)
                        //如果需要的字段为空
                        result.put(xmlValue.value(), "");
                }
                //如果类型是string则将值赋予到result中
                else if (field.getType() == String.class) {
                    //不论是否json字段之前的值是什么，复写
                    result.put(xmlValue.value(), (String) param);
                } else if (field.getType() == List.class) {
                    List list = (List) param;
                    ArrayNode arrayNode = mapper.createArrayNode();
                    for (int i = 0; i < list.size(); i++) {
                        arrayNode.add(parseObj(list.get(i), mapper));
                    }
                    result.put(xmlValue.value(), arrayNode);
                } else if (field.getType() == Date.class) {
                    SimpleDateFormat sim = new SimpleDateFormat(xmlValue.toDateFormat());
                    result.put(xmlValue.value(), sim.format((Date) param));
                } else if (field.getType() == com.ncse.fdds.xmlbean.LineInfo.class) {
                    if (field.get(Obj) != null) {
                        result.put("flgt_vialc", (
                                (com.ncse.fdds.xmlbean.LineInfo) field.get(Obj)
                        ).getLineInfoValue());
                        String des = ((com.ncse.fdds.xmlbean.LineInfo) field.get(Obj)).getLineInfoDesValue();
                        if (StringUtils.isNotEmpty(des)) {
                            result.put("flgt_des3c", des);
                        }
                    } else {

                        result.put("flgt_vialc", "");
                    }

                } else {
                    result.setAll(parseObj(param, mapper));
                }
            }

        }

        return result;
    }

    private static ObjectNode checkDirection(ObjectNode t) {
        if (t.get("flgt_adid") != null)
            //进港
            if ("A".compareToIgnoreCase((t.get("flgt_adid").asText())) == 0) {
                t.remove("td_flgt_placecode");
                t.put("flgt_placecode", t.get("ta_flgt_placecode"));
                t.remove("ta_flgt_placecode");
            } else {//离港
                t.remove("ta_flgt_placecode");
                t.put("flgt_placecode", t.get("td_flgt_placecode"));
                t.remove("td_flgt_placecode");
            }
        else {
            throw new IllegalArgumentException("Direction is null");
        }
        return t;
    }

    public static String parseXml2Json(Object obj, String xml) throws DocumentException, IllegalAccessException, InstantiationException, JsonProcessingException, ParseException {
        Object imffData = parseXml(obj, xml);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(checkDirection(parseObj(imffData, mapper)));
    }

    public static String parseXml2JsonWithDefaultPrettyPrinter(Object obj, String xml) throws DocumentException, IllegalAccessException, InstantiationException, JsonProcessingException, ParseException {
        Object Data = parseXml(obj, xml);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(checkDirection(parseObj(Data, mapper)));
    }

    public static Object parseXml(Object obj, String xml) throws DocumentException, IllegalArgumentException, InstantiationException, IllegalAccessException, ParseException {
        Document doc = DocumentHelper.parseText(xml);
        Element et = doc.getRootElement();
        return parseStructNode(obj, et);
    }


    public static <T> T parseStructNode(T result, Element et) throws IllegalAccessException, InstantiationException, ParseException {
        HashMap<String, Field> fieldsMap = new HashMap<String, Field>();
        Class<?> c = result.getClass();
        Field[] fields = c.getDeclaredFields();
        //缓存类所有字段名称
        for (Field field : fields) {
            fieldsMap.put(field.getName(), field);
        }
        Set<String> fieldsName = fieldsMap.keySet();
        List<Element> rList = et.elements();
        for (int i = 0; i < rList.size(); i++) {
            Element node = rList.get(i);
            String nodeName = node.getName();
            //如果字段列表中包含该xml节点
            if (fieldsName.contains(nodeName)) {
                Field field = fieldsMap.get(nodeName);
                field.setAccessible(true);
                //特殊处理字段

                //如果类型是string则将值赋予到result中
                if (field.getType() == String.class) {
                    field.set(result, node.getText());
                    //如果是list 则读取当前节点下所有子节点的集合，每个子节点的属性是其子节点的所有属性
                } else if (field.getType() == List.class) {
                    //特殊字段处理
//                    if(nodeName.equals("PFLT")){
//                        continue;
//                    }
                    // 如果是List类型，得到其Generic的类型
                    Type genericType = field.getGenericType();
                    if (genericType == null) continue;
                    // 如果是泛型参数的类型
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        //递归并将结果设置到result中
                        List<Object> collection = new ArrayList<Object>();
                        List<Element> es = node.elements();
                        if (es == null)
                            throw new IllegalArgumentException(nodeName + " elements is null");
                        //将当前节点的所有子节点 下的 子节点所组成的结构 的集合 设置到当前节点
                        for (Element e : es) {
                            collection.add(parseStructNode(genericClazz.newInstance(), e));
                        }
                        field.set(result, collection);
                    }
                    //如果是时间类型，则按照注解的格式解析
                } else if (field.getType() == Date.class) {
                    XMLValue xmlValue = field.getAnnotation(XMLValue.class);
                    String dateFormat = "yyyyMMddHHmmss";
                    if (xmlValue != null)
                        dateFormat = xmlValue.fromDateFormat();
                    SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
                    if (!node.getText().isEmpty())
                        field.set(result, sim.parse(node.getText()));
                } else {
                    //将自定义类型new出来并反射所需字段
                    field.set(result, parseStructNode(field.getType().newInstance(), node));
                }

            }
        }

        return result;
    }

}
