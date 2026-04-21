package com.higer.oildataexchange.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;

public class JaxbDateAdapter extends XmlAdapter<String, Date> {
    @Override
    public Date unmarshal(String v) throws Exception {
        return null;
    }

    @Override
    public String marshal(Date v) throws Exception {
        if (v != null) {
            return DateFormatUtils.format(v, YYYY_MM_DD_HH_MM_SS);
        }
        return null;
    }
}
