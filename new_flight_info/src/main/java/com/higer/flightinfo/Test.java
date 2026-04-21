package com.higer.flightinfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.flightinfo.entity.Meta;
import com.higer.flightinfo.entity.TFlight;
import com.higer.flightinfo.repository.MetaRepository;
import com.higer.flightinfo.service.KafkaService;
import com.higer.flightinfo.service.TFlightService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Test {


    @Autowired
    private MetaRepository metaRepository;
    @Autowired
    private TFlightService tFlightService;
    @Autowired
    private KafkaService kafkaService;

    @Value("${tflight_info_topic}")
    private String kafkaTopic;

    @GetMapping("/test")
    public void findNewFuelParam(){
        List<Meta> metas = metaRepository.findbyTypea();
        System.out.println(JSON.toJSON(metas));
        metas.stream().forEach(o->{
            String xml = o.getContent();
            Document document = null;
            try {
                document = DocumentHelper.parseText(xml);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = document.getRootElement();
            Element header = root.element("Header");
            String rcvr = header.elementTextTrim("RCVR");
            String seqn = header.elementTextTrim("SEQN");
            String ddtm = header.elementTextTrim("DDTM");
            String type = header.elementTextTrim("TYPE");
            Element body = root.element("Body");
            Meta meta = new Meta(null, rcvr, seqn, ddtm, type, xml, null, body);
            TFlight flight = tFlightService.transform2TFlightNew(meta.getBody(), StringUtils.equals("DELETE", meta.getType()));
            String message = JSONObject.toJSONString(flight);
            System.out.println(message);
            kafkaService.send(kafkaTopic,message,null);
        });

    }

}
