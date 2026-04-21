package com.higer.flightinfo.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.flightinfo.entity.*;
import com.higer.flightinfo.repository.MetaRepository;
import com.higer.flightinfo.service.KafkaService;
import com.higer.flightinfo.service.TFlightService;
import com.higer.flightinfo.util.MessageSequenceGenerator;
import com.higer.flightinfo.util.TFlightUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;

@Component
@RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
@Slf4j
public class RabbitMqConsumer {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private TFlightService tFlightService;

    @Autowired
    private TaskScheduleComponent taskScheduleComponent;

    @Autowired
    private KafkaService kafkaService;

    @Value("${tflight_info_topic}")
    private String kafkaTopic;

    @RabbitHandler
    public void process(String content, Channel channel, Message message){
        try {
            log.info("收到消息：{}",content);
            Meta meta = parseXml(content);
            TFlight flight = null;
            if(StringUtils.equals("DELETE", meta.getType())){
                flight = tFlightService.transform2TFlightNew(meta.getBody(), StringUtils.equals("DELETE", meta.getType()));
            }else{
                 flight = tFlightService.transform2TFlight(meta.getBody(), StringUtils.equals("DELETE", meta.getType()));
            }
            if(flight!=null){
                taskScheduleComponent.saveTFlightData(meta.getBody());
                taskScheduleComponent.saveTFlightChangeLog(meta.getBody());
                sendKafka(flight,meta.getSeqn());
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("XML解析失败",e);
            try {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException ex) {
                log.error("消息拒绝失败",e);
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("系统异常",e);
            try {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException ex) {
                log.error("消息拒绝失败",e);
            }
        }
    }

    private Meta parseXml(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Element header = root.element("Header");
        String rcvr = header.elementTextTrim("RCVR");
        String seqn = header.elementTextTrim("SEQN");
        String ddtm = header.elementTextTrim("DDTM");
        String type = header.elementTextTrim("TYPE");
        Element body = root.element("Body");
        Meta meta = new Meta(null, rcvr, seqn, ddtm, type, xml, null, body);
        return metaRepository.save(meta);
    }


    private void sendKafka(TFlight tFlight,String seqn){
        String flgtFfid = tFlight.getFlgtFfid();
        tFlight.setMsgSeqn(seqn);
        String message = JSONObject.toJSONString(tFlight);
        if(StringUtils.isNotBlank(tFlight.getFlgtFtyp()) && (tFlight.getFlgtFtyp().equals("CX") || tFlight.getFlgtFtyp().equals("FX"))){
            for (int i = 0; i < 3; i++) {
                log.info("发送Kafka:{}", message);
                kafkaService.send(kafkaTopic,flgtFfid,message,1);
            }
        }
        log.info("发送Kafka:{}", message);
        kafkaService.send(kafkaTopic,flgtFfid,message,null);
    }

    public static void main(String[] args) {
        //try {
        //    String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        //            "<MSG>\n" +
        //            "    <Header>\n" +
        //            "        <RCVR>ZHHY</RCVR>\n" +
        //            "        <SEQN>2023092500081875</SEQN>\n" +
        //            "        <DDTM>2023-09-25 12:36:57</DDTM>\n" +
        //            "        <TYPE>DELETE</TYPE>\n" +
        //            "    </Header>\n" +
        //            "    <Body>\n" +
        //            "        <FID>434f7417bf498a11ec750b31ed2afc17</FID>\n" +
        //            "        <AirportFID>19D856F7504049E390F306439B152A4D</AirportFID>\n" +
        //            "    </Body>\n" +
        //            "</MSG>";
        //    Document document = DocumentHelper.parseText(s);
        //    Element root = document.getRootElement();
        //    Element header = root.element("Header");
        //    String rcvr = header.elementTextTrim("RCVR");
        //    String seqn = header.elementTextTrim("SEQN");
        //    String ddtm = header.elementTextTrim("DDTM");
        //    String type = header.elementTextTrim("TYPE");
        //    Element body = root.element("Body");
        //    Meta meta = new Meta(null, rcvr, seqn, ddtm, type, s, null, body);
        //    TFlightExtend tFlight=new TFlightExtend();
        //    if(StringUtils.equals("DELETE", meta.getType())){
        //        tFlight.setFlgtFfid(meta.getBody().elementTextTrim("FID"));
        //        if(StringUtils.equals("DELETE", meta.getType())){
        //            tFlight.setFlgtFtyp("CX");
        //        }
        //    }else{
        //        tFlight.setFlgtFfid(meta.getBody().elementTextTrim("FID"));
        //        if(StringUtils.equals("DELETE", meta.getType())){
        //            tFlight.setFlgtFtyp("CX");
        //        }
        //    }
        //    System.out.println(JSON.toJSONString(tFlight));
        //} catch (Exception e) {
        //    throw new RuntimeException(e);
        //}


       /* String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><MSG><Header><RCVR>ZHHY</RCVR><SEQN>2020083100082601</SEQN><DDTM>2020-08-31 12:10:26</DDTM><TYPE>DELETE</TYPE></Header><Body><FID>1bea29db5d752faa1036a387ec748441</FID><AirportFID></AirportFID></Body></MSG>";
       // Meta meta = parseXml(content);
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
        Element body1 = root.element("Body");
        Meta meta = new Meta(null, rcvr, seqn, ddtm, type, xml, null, body1);
        Element body = meta.getBody();
        TFlightExtend tFlight=new TFlightExtend();
        tFlight.setFlgtFfid(body.elementTextTrim("FID"));
        tFlight.setFlgtFlno(body.elementTextTrim("FlightNo"));//航班号
        tFlight.setFlgtFlop(TFlightUtils.dateFormat(body.elementTextTrim("FlightDate")));//航班日期
        tFlight.setFlgtOrg3C(body.elementTextTrim("DepCode"));//起飞机场
//        tFlight.setFlgtDes3C(body.elementTextTrim("ArrCode"));//到达机场
        String routes=body.elementTextTrim("Routes");
        if(StringUtils.isNotEmpty(routes)){
            String[] routeArr=routes.split(",");
            tFlight.setFlgtDes3C(routeArr.length>0?routeArr[routeArr.length-1]:null);//到达机场
            String vialc = RegExUtils.replaceAll(routes, ",", "-");
            if(StringUtils.equals(tFlight.getFlgtAdid(),"D")){
                tFlight.setFlgtVialc(StringUtils.substring(vialc,StringUtils.indexOf(vialc,"KMG")));
            }else{
                tFlight.setFlgtVialc(StringUtils.substring(vialc,0,StringUtils.indexOf(vialc,"KMG"))+"KMG");
            }
        }
        tFlight.setFlgtMissionProp(body.elementTextTrim("FlightCla"));//航班任务属性
        tFlight.setFlgtRegn(body.elementTextTrim("AirNum"));//飞机号
        tFlight.setFlgtAcname(body.elementTextTrim("AirType"));//飞机类型
//        tFlight.setFlgtVialc(RegExUtils.replaceAll(routes,",","-"));

        tFlight.setFlgtAl2C(StringUtils.substring(body.elementTextTrim("FlightNo"),0,2));//航空公司二字码


        System.out.println("胖子让我输出这个--------"+tFlight.getFlgtAdid()+"----------"+tFlight.getFlgtVialc());
        if(StringUtils.isNotEmpty(body.elementTextTrim("ShareMainF"))){//是共享航班，过滤
        }
        tFlight.setFlgtFtyp(TFlightUtils.transformFtyp(body.elementTextTrim("FlightStatus"),tFlight.getFlgtAdid()));//航班状态
//        tFlight.setFlgtVialc(String.format("%s-%s",tFlight.getFlgtOrg3C(),tFlight.getFlgtDes3C()));
        tFlight.setFlgtFlti(TFlightUtils.transformFlti(body.elementTextTrim("Fcategory")));
        tFlight.setLinkFfid(body.elementTextTrim("LFIDForKMG"));
        if(StringUtils.equals("DELETE", meta.getType())){
            tFlight.setFlgtFtyp("CX");
        }
*/
        String flgtFfid = "c64a9e74f68f6cc663bdc373b0d2ddeb";
        int partition = flgtFfid.charAt(flgtFfid.length() - 1)%6;
        System.out.println(partition);
    }

}
