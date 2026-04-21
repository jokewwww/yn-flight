package com.ncse.fdds.xmlbean;


import com.ncse.fdds.annotation.JSONNeed;
import lombok.ToString;

@ToString
public class Body {
    @JSONNeed
    private FlightIdentity FlightIdentity;
    @JSONNeed
    private FlightInfo FlightInfo;
    @JSONNeed
    private ProcessNode ProcessNode;
    @JSONNeed
    private Resource Resource;
    @JSONNeed
    private LineInfo LineInfo;
}
