package com.zh;

import com.zh.service.AirportCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZhoilFlightApplication.class)
public class ZhoilFlightApplicationTests {

	@Autowired
	AirportCodeService airportCodeService;

	@Test
	public void contextLoads() {

		airportCodeService.selectAirportCodeCascadeAndroid();
	}

}
