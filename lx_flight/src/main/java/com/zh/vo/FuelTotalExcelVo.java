package com.zh.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class FuelTotalExcelVo {

    private String flrcDate;

    private Long arcrCustomNum;

    private String flrcAirlName;

    private Integer flrcNo;

    private Integer flrcType;

    private BigDecimal dayFuelVol;

    private BigDecimal dayFuelQuantity;

    private String flrcBwtar;
}
