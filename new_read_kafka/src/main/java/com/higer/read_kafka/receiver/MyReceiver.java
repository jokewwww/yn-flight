package com.higer.read_kafka.receiver;

import com.alibaba.fastjson.JSON;
import com.higer.read_kafka.entity.flight.in.InTFlight;
import com.higer.read_kafka.service.KafkaService;
import com.higer.read_kafka.util.JsonUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

public class MyReceiver implements Runnable {
	private String msg;
	private KafkaService kafkaService;

	public MyReceiver(KafkaService kafkaService, String msg) {
		this.kafkaService = kafkaService;
		this.msg = msg;
	}

	@Override
	public void run() {
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		InTFlight inTFlight = JsonUtils.json2object(msg, InTFlight.class);
		System.out.println("航显原数据-->" + JSON.toJSONString(inTFlight));
        String flgtFtyp = inTFlight.getFlgtFtyp();
        if(!StringUtils.isEmpty(flgtFtyp)){
            //FH 返航   DE:删除  NO:非运营, FX:航班归档  CX:取消,
       	if(flgtFtyp.equals("FX")
                || flgtFtyp.equals("NO")
                || flgtFtyp.equals("FX")
                || flgtFtyp.equals("CX"))
       	{
            try {
                Thread.sleep(50);
                kafkaService.flightUpdateOrAddOrDelete(inTFlight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            kafkaService.flightUpdateOrAddOrDelete(inTFlight);
        }
	   }else{
            kafkaService.flightUpdateOrAddOrDelete(inTFlight);
        }
		//kafkaService.flightUpdateOrAddOrDelete(inTFlight);
	}


	public static void main(String[] args) {
		String msg = "{\"flgtAcname\":\"B752\",\"flgtAdid\":\"D\",\"flgtAl2C\":\"CO\",\"flgtDStot\":1573317300000,\"flgtDes3C\":\"NKG\",\"flgtFfid\":\"201294624\",\"flgtFlno\":\"CO1087\",\"flgtFlop\":1573315200000,\"flgtFlti\":\"D\",\"flgtFtyp\":\"UK\",\"flgtIfsr\":\"Y\",\"flgtMissionProp\":\"H/Z\",\"flgtOrg3C\":\"SHE\",\"flgtPlacecode\":\"Y04\",\"flgtRegn\":\"B2856\",\"flgtVialc\":\"SHE-NKG\",\"linkFfid\":\"201294757\"}";
		InTFlight inTFlight = JsonUtils.json2object(msg, InTFlight.class);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String timeFormat = sdf.format(inTFlight.getFlgtDStot());
		System.out.println(timeFormat);
		System.out.println(JSON.toJSONString(inTFlight));
		System.out.println(111);
	}
}
