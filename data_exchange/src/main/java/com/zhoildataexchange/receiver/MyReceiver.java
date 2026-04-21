package com.zhoildataexchange.receiver;

import com.zhoildataexchange.entity.flight.in.InTFlight;
import com.zhoildataexchange.service.KafkaService;
import com.zhoildataexchange.util.JsonUtils;

public class MyReceiver implements Runnable {
    private String msg;
    private KafkaService kafkaService;

    public MyReceiver(KafkaService kafkaService, String msg) {
        this.kafkaService = kafkaService;
        this.msg = msg;
    }

    @Override
    public void run() {
        InTFlight inTFlight = JsonUtils.json2object(msg, InTFlight.class);
        kafkaService.flightUpdateOrAddOrDelete(inTFlight);
    }
}
