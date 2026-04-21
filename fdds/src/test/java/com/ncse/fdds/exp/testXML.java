package com.ncse.fdds.exp;

import com.ncse.fdds.utils.XmlParseUtil;
import com.ncse.fdds.xmlbean.KMGData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class testXML {


    @Test
    public void testxml1() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope xmlns=\"http://schema.kcia.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Header><MessageSendDateTime>2018-11-12 22:58:10</MessageSendDateTime><MessageSeqence>2129038</MessageSeqence><ServiceType>FDDS</ServiceType><MessageType>KMGData</MessageType><SourceSystemID>KMG-ICP</SourceSystemID><Iata>KMG</Iata><RequestType>UpdateOrCreate</RequestType></Header><Body><FlightIdentity><FID>26ebefdf624d4de89e7efc720cc74cc6</FID><Direction>A</Direction><FlightNo>GS6571</FlightNo><FlightDate>2018-11-12</FlightDate><AirNum>B1659</AirNum><DepCode>HAK</DepCode><ArrCode>KMG</ArrCode></FlightIdentity><FlightInfo><STD></STD><STA>2018-11-12 21:50:00</STA><ETD></ETD><ETA>2018-11-12 22:56:42</ETA><ABT></ABT><TDT>2018-11-12 22:56:00</TDT><ITY>A320</ITY><IFC>GCR</IFC><FLC>GS</FLC><CLA>W/Z</CLA><NAT>PAX</NAT><FST>J</FST><OTC>REG</OTC><HDA>YAG</HDA><TOF>D</TOF><FlightStatus>OB</FlightStatus><DelayCode></DelayCode><MFlightNo></MFlightNo><LFlightNo>GS6572</LFlightNo><LFlightDate>2018-11-12</LFlightDate></FlightInfo><Resource><DepStand></DepStand><ArrStand>162</ArrStand><Gate></Gate><CAR>12</CAR><CR2></CR2><MP1></MP1><MP2></MP2><CID></CID><CD2></CD2></Resource><LineInfo><ORG>HAK</ORG><DES>KMG</DES><VI1></VI1><VI2></VI2><VI3></VI3><VI4></VI4><VI5></VI5><VI6></VI6></LineInfo><ProcessNode><ATA>2018-11-12 23:06:00</ATA><ATD></ATD><FBT></FBT><LBT></LBT></ProcessNode></Body></Envelope>";
        log.info("result: {}", XmlParseUtil.parseXml2JsonWithDefaultPrettyPrinter(new KMGData(), xml));
    }
}
