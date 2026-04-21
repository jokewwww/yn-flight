package com.higer.higerservice.entity.oilpro;

import lombok.Data;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/7 23:40
 * @Description:
 */
@Data
public class Test {
    private String id;
    private String date;
    private String deliveryType;
    private String deliveryNo;
    private String airport;
    private String delivered;
    private String filghtNo;
    private String aircraftNo;
    private String aircraftType;
    private String departure;
    private String transitStop;
    private String destination;
    private String testbillNo;
    private String descriptionAndGrade;
    private Double temperature;
    private Double actualDensity;
    private Long meterStart;
    private Long meterFinish;
    private Long figures;//加油数量 小写 升
    private String figuresInWords;
    private Long quantity;
    private String hydrantPitNo;
    private String vehicleTypeAndNo;
    private String timeStart;
    private String timeFinish;
    private String signPhoto;
    private String signName;
}
