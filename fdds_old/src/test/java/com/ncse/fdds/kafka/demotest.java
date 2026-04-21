package com.ncse.fdds.kafka;

import com.ncse.fdds.FDDSApplication;
import com.ncse.fdds.utils.XmlParseUtil;
import com.ncse.fdds.xmlbean.KMGData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FDDSApplication.class)
@Slf4j
public class demotest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.msgtopic}")
    private String topic;

    @Test
    public void testxml1() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope xmlns=\"http://schema.kcia.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Header><MessageSendDateTime>2018-12-10 10:40:44</MessageSendDateTime><MessageSeqence>2627306</MessageSeqence><ServiceType>FDDS</ServiceType><MessageType>KMGData</MessageType><SourceSystemID>KMG-ICP</SourceSystemID><Iata>KMG</Iata><RequestType>UpdateOrCreate</RequestType></Header><Body><FlightIdentity><FID>5c589b55e9804778985e8bddeccecc40</FID><Direction>A</Direction><FlightNo>CZ3409</FlightNo><FlightDate>2018-12-10</FlightDate><AirNum>B8993</AirNum><DepCode>CAN</DepCode><ArrCode>KMG</ArrCode></FlightIdentity><FlightInfo><STD></STD><STA>2018-12-10 18:53:00</STA><ETD></ETD><ETA>2018-12-10 18:40:05</ETA><ABT></ABT><TDT>2018-12-10 18:40:44</TDT><ITY>A321</ITY><IFC>CSN</IFC><FLC>CZ</FLC><CLA>W/Z</CLA><NAT>PAX</NAT><FST>J</FST><OTC>REG</OTC><HDA>YAG</HDA><TOF>D</TOF><FlightStatus>LD</FlightStatus><DelayCode></DelayCode><MFlightNo></MFlightNo><LFlightNo>CZ3678</LFlightNo><LFlightDate>2018-12-10</LFlightDate></FlightInfo><Resource><DepStand></DepStand><ArrStand>138</ArrStand><Gate></Gate><CAR>15</CAR><CR2></CR2><MP1></MP1><MP2></MP2><CID></CID><CD2></CD2></Resource><LineInfo><ORG>CAN</ORG><DES>KMG</DES><VI1></VI1><VI2></VI2><VI3></VI3><VI4></VI4><VI5></VI5><VI6></VI6></LineInfo><ProcessNode><ATA></ATA><ATD></ATD><FBT></FBT><LBT></LBT></ProcessNode></Body></Envelope>";
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, XmlParseUtil.parseXml2JsonWithDefaultPrettyPrinter(new KMGData(), xml));
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //TODO 失败存储重试
                log.warn("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                log.debug("Produce: The message was sent successfully result: {}", stringObjectSendResult.getProducerRecord().toString());
            }
        });
    }

    @Test
    public void testxml2() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope xmlns=\"http://schema.kcia.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Header><MessageSendDateTime>2018-12-10 22:57:52</MessageSendDateTime><MessageSeqence>2129034</MessageSeqence><ServiceType>FDDS</ServiceType><MessageType>KMGData</MessageType><SourceSystemID>KMG-ICP</SourceSystemID><Iata>KMG</Iata><RequestType>UpdateOrCreate</RequestType></Header><Body><FlightIdentity><FID>26ebefdf624d4de89e7efc720cc74cc6</FID><Direction>A</Direction><FlightNo>GS6571</FlightNo><FlightDate>2018-12-10</FlightDate><AirNum>B1659</AirNum><DepCode>HAK</DepCode><ArrCode>KMG</ArrCode></FlightIdentity><FlightInfo><STD></STD><STA>2018-12-10 21:50:00</STA><ETD></ETD><ETA>2018-12-10 22:56:42</ETA><ABT></ABT><TDT>2018-12-10 22:56:00</TDT><ITY>A320</ITY><IFC>GCR</IFC><FLC>GS</FLC><CLA>W/Z</CLA><NAT>PAX</NAT><FST>J</FST><OTC>REG</OTC><HDA>YAG</HDA><TOF>D</TOF><FlightStatus>LD</FlightStatus><DelayCode></DelayCode><MFlightNo></MFlightNo><LFlightNo>GS6572</LFlightNo><LFlightDate>2018-12-10</LFlightDate></FlightInfo><Resource><DepStand></DepStand><ArrStand>162</ArrStand><Gate></Gate><CAR>12</CAR><CR2></CR2><MP1></MP1><MP2></MP2><CID></CID><CD2></CD2></Resource><LineInfo><ORG>HAK</ORG><DES>KMG</DES><VI1></VI1><VI2></VI2><VI3></VI3><VI4></VI4><VI5></VI5><VI6></VI6></LineInfo><ProcessNode><ATA></ATA><ATD></ATD><FBT></FBT><LBT></LBT></ProcessNode></Body></Envelope>";
        kafkaTemplate.send(topic, XmlParseUtil.parseXml2Json(new KMGData(), xml));

    }

}
