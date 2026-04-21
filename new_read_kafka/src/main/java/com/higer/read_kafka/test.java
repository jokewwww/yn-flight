package com.higer.read_kafka;

import com.alibaba.fastjson.JSON;
import com.higer.read_kafka.component.KafkaServiet;
import com.higer.read_kafka.entity.flight.TFlight;
import com.higer.read_kafka.entity.flight.in.InTFlight;
import com.higer.read_kafka.repository.flight.TFlightRepository;
import com.higer.read_kafka.service.KafkaService;
import com.higer.read_kafka.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/7/8 13:12
 * @Description:
 */
@RestController
public class test {


    @Autowired
    private KafkaService kafkaService;


    @Autowired
    private TFlightRepository tFlightRepository;

    @GetMapping("/test")
    public void test(
    ){

        List<TFlight> tFlights = tFlightRepository.selectNumNull();


        System.out.println(tFlights.size());


    /*    String s = "{\"flgtAStot\":1637166900000,\"flgtAcname\":\"A321\",\"flgtAdid\":\"D\",\"flgtAl2C\":\"CZ\",\"flgtDes3C\":\"SHE\",\"flgtFfid\":\"1208245749\",\"flgtFlno\":\"CZ6508\",\"flgtFlop\":1637078400000,\"flgtFlti\":\"D\",\"flgtFtyp\":\"UK\",\"flgtIfsr\":\"N\",\"flgtMissionProp\":\"W/Z\",\"flgtOrg3C\":\"PVG\",\"flgtPlacecode\":\"Y09\",\"flgtRegn\":\"B6318\",\"flgtVialc\":\"PVG-SHE\"}";
        //kafkaServiet.send("flight",s7);
        String s1 = "{\"flgtFfid\":\"f1b7901d02e29d9ad601a31f73f56cac\",\"flgtFtyp\":\"CX\"}";
        System.out.println(1);
        InTFlight inTFlight = JsonUtils.json2object(s1, InTFlight.class);
        kafkaService.flightUpdateOrAddOrDelete(inTFlight);*/
    }


}
