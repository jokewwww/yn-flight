package com.zhoildataexchange;

import com.zhoildataexchange.entity.flight.in.InTFlight;
import com.zhoildataexchange.service.KafkaService;
import com.zhoildataexchange.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/6/5 12:17
 * @Description:
 */
@RestController
public class test {

    @Autowired
    private KafkaService kafkaService;


    @GetMapping("/test")
    public void test(
    ) {
        String msg = "{\n" +
                " \"customName\": \"东方航空云南有限公司\",\n" +
                " \"customNum\": \"0000120002\",\n" +
                " \"flgtAcname\": \"B738\",\n" +
                " \"flgtAdid\": \"D\",\n" +
                " \"flgtAl2c\": \"\",\n" +
                " \"flgtAlcname\": \"东方航空云南有限公司\",\n" +
                " \"flgtChockFuel\": 0,\n" +
                " \"flgtDes3c\": \"SZX\",\n" +
                " \"flgtDesnm\": \"深圳宝安机场\",\n" +
                " \"flgtFlno\": \"MU5757\",\n" +
                " \"flgtFlop\": 1722873600000,\n" +
                " \"flgtFlti\": \"D\",\n" +
                " \"flgtFtyp\": \"\",\n" +
                " \"flgtIfsr\": \"\",\n" +
                " \"flgtMissionProp\": \"W/Z\",\n" +
                " \"flgtOlvr\": \"\",\n" +
                " \"flgtOrg3c\": \"KMG\",\n" +
                " \"flgtOrgnm\": \"昆明长水国际机场\",\n" +
                " \"flgtOtatFuel\": 0,\n" +
                " \"flgtPlacecode\": \"112\",\n" +
                " \"flgtRegn\": \"B7589\",\n" +
                " \"flgtTakeoffFuel\": 0,\n" +
                " \"flgtTrs3c1\": \"\",\n" +
                " \"flgtTrsnm1\": \"\",\n" +
                " \"flgtVialc\": \"\"\n" +
                "}";
        InTFlight inTFlight = JsonUtils.json2object(msg, InTFlight.class);
        System.out.println("推送原信息 : -------" + msg);
        kafkaService.flightUpdateOrAddOrDelete(inTFlight);
    }

}
