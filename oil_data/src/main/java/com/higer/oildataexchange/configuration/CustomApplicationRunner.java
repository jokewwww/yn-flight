package com.higer.oildataexchange.configuration;

import com.higer.oildataexchange.quartz.QuartzManager;
import com.higer.oildataexchange.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CustomApplicationRunner {

    @Value(value = "${zhoil.data.exchange.flight.enable}")
    private boolean enableFlight;

    @Value(value = "${zhoil.data.exchange.oil-pay.enable}")
    private boolean enableOilPay;

    @Value(value = "${zhoil.data.exchange.oil-order-info.enable}")
    private boolean enableOirq;

    @Value(value = "${zhoil.data.exchange.credit-info.enable}")
    private boolean enableCredit;


    @Value(value = "${zhoil.data.exchange.air-unit.enable}")
    private boolean enableAirUnit;


    @Value(value = "${zhoil.data.exchange.air-company.enable}")
    private boolean enableAirCompany;
    @Autowired
    private QuartzManager quartzManager;

    @PostConstruct
    public void run() throws Exception {
        if (enableFlight) {
            quartzManager.addJob("FLIGHT_EXCHANGE", FlightInfoService.class, "0/30 * * * * ? *");
        }

        if (enableOilPay) {
            quartzManager.addJob("OIL_PAY_CONFIRM", OilPayConfirmService.class, "0/30 * * * * ? *");
        }


        if (enableOirq) {
            quartzManager.addJob("OIL_ORDER_INFO", OrderInfoService.class, "0 */1 * * * ? *");
        }

        if (enableCredit) {
            quartzManager.addJob("CREDIT_INFO", CreditInfoService.class, "0/30 * * * * ? *");
        }


        if (enableAirUnit) {
            quartzManager.addJob("AIR_UNIT", AirUnitService.class, "0/30 * * * * ? *");
        }

        if (enableAirCompany) {
            quartzManager.addJob("AIR_COMPANY", AirCompanyService.class, "0/30 * * * * ? *");
        }
    }
}
