package com.ncse.fdds.xmlbean;

import com.alibaba.fastjson.JSON;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/1 09:33
 * @Description:
 */
public class XmlInterfaceUtils {


    private XmlInterfaceUtils() {

    }

    /**
     * 对象 转 xml
     *
     * @param object
     * @return 返回一个生成xml的位置
     */
    public static String convertToXml(Object object) {
        if (object == null) {
            return null;
        }
        Element root = null;
        try {
            Class<?> aClass = object.getClass();
            // 根节点
            root = new Element(aClass.getSimpleName());
            for (Field declaredField : aClass.getDeclaredFields()) {
                declaredField.setAccessible(true);
                // 这个属性为 List 时
                if (declaredField.getGenericType() instanceof ParameterizedType) {
                    Element child1 = new Element(declaredField.getName());
                    List<?> objList = (List<?>) declaredField.get(object);
                    if (objList != null) {
                        ListObjecToXml(objList, child1);
                        root.addContent(child1);
                    }
                } else {
                    String fieldName = declaredField.getName();
                    Element child = new Element(fieldName);
                    child.setText(String.valueOf(declaredField.get(object)));
                    root.addContent(child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("对象转换成XML出现异常：" + e.getMessage());
        }
        try {
            // 将 根节点 加入 文档中
            Document doc = new Document(root);
            // 创建xml输出流操作类
            XMLOutputter xmlOutput = new XMLOutputter();
            // 格式化xml内容
            xmlOutput.setFormat(Format.getPrettyFormat());
            File directory = new File("src/main/resources");
            String courseFile = directory.getCanonicalPath();

            // 把xml输出到指定位置
            File f = new File(courseFile + File.separator + "temp");
            if (!f.exists()) {
                f.mkdirs();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHssmm");
            System.out.println(f.getAbsolutePath());
            File file = new File(f.getAbsolutePath() + File.separator + object.getClass().getSimpleName() + format.format(new Date()) + ".xml");
            xmlOutput.output(doc, new FileOutputStream(file));
            System.out.println("生成完毕！ " + file.getName() + "文件生成位置为：" + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成xml文件出现异常：" + e.getMessage());
        }
        return null;
    }

    // 处理 List<实体> objData
    private static void ListObjecToXml(List<?> objData, Element element) throws Exception {
        if (objData != null) {
            for (int i = 0; i < objData.size(); i++) {
                Field[] declaredFields = objData.get(i).getClass().getDeclaredFields();
                Object obj = objData.get(i);
                Element root = new Element(obj.getClass().getSimpleName());
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    Element child = new Element(declaredField.getName());
                    if (declaredField.getGenericType() instanceof ParameterizedType) {
                        List<?> objList = (List<?>) declaredField.get(obj);
                        if (objList != null) {
                            ListObjecToXml(objList, child);
                            root.addContent(child);
                        }
                    } else {
                        child.setText(String.valueOf(declaredField.get(obj)));
                        root.addContent(child);
                    }
                }
                element.addContent(root);
            }
        }
    }

    /**
     * xml 转 对象
     *
     * @param clazz
     * @param xmlStr
     * @return
     * @throws Exception
     */
    public static Object dataXmltoEntity(Class<?> clazz, String xmlStr) {
        if (clazz == null) {
            System.out.println("未设置对象的类型");
            return null;
        }
        File file = new File(xmlStr);
        if (!file.exists()) {
            System.out.println("解析失败，找不到文件");
            return null;
        }
        //创建Jdom2的解析器对象
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        Element root = null;
        Object obj = null;
        try {
            document = builder.build(file.getAbsoluteFile());
            root = document.getRootElement();
            if (!root.getName().equals(clazz.getSimpleName())) {
                System.out.println("xml内容无法转成 " + clazz + "对象，请检查！");
                return null;
            }
            // new出 当前最大的对象
            obj = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("无法进行xml解析，请检查！");
        }
        try {
            List<Element> children = root.getChildren();
            for (Element child : children) {
                // 第二层的xml数据
                List<Element> children1 = child.getChildren();
                if (children1.isEmpty()) { // 处理第一层的xml数据
                    Field field = clazz.getDeclaredField(child.getName());
                    field.setAccessible(true);
                    if (field.getGenericType().getTypeName().equals("int")) {
                        field.set(obj, Integer.parseInt(child.getValue()));
                    } else {
                        field.set(obj, child.getValue());
                    }
                } else { // 处理第二层的 xml 数据
                    mm(clazz, obj, child, children1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解析xml数据转换成实体出现异常：" + e.getMessage());
        }
        return obj;
    }

    private static void mm(Class<?> clazz, Object obj, Element child, List<Element> children1) throws Exception {
        // 取到当前的 list 属性
        Field field = clazz.getDeclaredField(child.getName());
        field.setAccessible(true);
        Class<?> genericClazz = null;
        if (field.getType() == List.class) {
            // 如果是List类型，得到其Generic的类型
            Type genericType = field.getGenericType();
            if (genericType != null) {
                // 如果是泛型参数的类型
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里的class类型对象
                    genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                }
            }
        }
        if (genericClazz != null) {
            List list = new ArrayList();
            // list 中 包含的对象
            for (Element element : children1) {
                // 取出当前类的属性与值
                List<Element> children2 = element.getChildren();
                // new 出List中包含的对象
                Object o = genericClazz.getConstructor().newInstance();
                // 当前对象进行赋值
                for (Element element1 : children2) {
                    if (element1.getChildren().isEmpty()) {
                        Field field1 = genericClazz.getDeclaredField(element1.getName());
                        field1.setAccessible(true);
                        field1.set(o, element1.getValue());
                    } else {
                        // 递归处理
                        mm(genericClazz, o, element1, element1.getChildren());
                    }
                }
                list.add(o);
            }
            field.set(obj, list);
        }
    }


    public static void main(String[] args) {
        HYData hyData = new HYData();
        BodyNew bodyNew = new BodyNew();
        FlightIdentityNew flightIdentityNew = new FlightIdentityNew();
        flightIdentityNew.setAirNum("airNum");
        flightIdentityNew.setArrCode("arrCode");
        flightIdentityNew.setDepCode("depCode");
        flightIdentityNew.setDirection("direction");
        flightIdentityNew.setFID("fid");
        ProcessNodeNew processNodeNew = new ProcessNodeNew();
        processNodeNew.setEndFuelTime("endFuelTime");

        bodyNew.setFlightIdentity(flightIdentityNew);
        bodyNew.setProcessNode(processNodeNew);
        HeaderNew header = new HeaderNew();
        header.setIata("iata");
        header.setMessageSendDateTime("sendTIme");
        header.setMessageSeqence("seqence");
        header.setMessageType("messageType");
        header.setRequestType("reqType");
        hyData.setBody(bodyNew);
        hyData.setHeader(header);
        String xxx = convertToXml(hyData, "utf-8", true);
        System.out.println(xxx);
//        String s = XmlInterfaceUtils.convertToXml(hyData);
//        System.out.println(s);
    }


    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding, boolean format) {
        String result = null;
        StringWriter writer = null;
        try {
            //System.out.println("obj--->"+ JSON.toJSONString(obj));
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
