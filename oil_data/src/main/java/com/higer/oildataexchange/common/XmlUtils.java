package com.higer.oildataexchange.common;

import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.acdm.AcdmXml;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {


    /**
     * JavaBean转换成xml
     *
     * @return
     */
    public static String convertToXml(R<?> r, String encoding, boolean format) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(r.getClass(), r.getBD().getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.marshal(r, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
//            throw new RuntimeException("XML解析错误",e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return null;
    }

    /**
     * JavaBean转换成xml
     *
     * @return
     */
    public static String convertToXml(AcdmXml<?> r, String encoding, boolean format) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(r.getClass(), r.getINFO().getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.marshal(r, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
//            throw new RuntimeException("XML解析错误",e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertToObj(String data, Class<T> tClass) {
        try {
            JAXBContext context = JAXBContext.newInstance(tClass);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(data);
            return (T) unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
//            throw new RuntimeException("XML解析错误",e);
        }
        return null;
    }
}
