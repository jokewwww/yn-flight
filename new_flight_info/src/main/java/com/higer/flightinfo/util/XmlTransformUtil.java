package com.higer.flightinfo.util;

import com.higer.flightinfo.annotation.XmlAlias;
import com.higer.flightinfo.entity.TFlightChange;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class XmlTransformUtil<T> {


    public static <T> T  transformXml(Element body,Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T t = tClass.newInstance();
        ReflectionUtils.doWithFields(tClass,field -> {
            String name = field.getAnnotation(XmlAlias.class).name();
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field,t,body.elementTextTrim(name));
        },field -> field.getAnnotation(XmlAlias.class)!=null);
        return t;
    }

}
