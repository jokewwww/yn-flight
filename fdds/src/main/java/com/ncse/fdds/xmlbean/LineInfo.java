package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.XMLValue;

public class LineInfo {

    private String ORG;//始发站
    @XMLValue("flgtDes3c")
    private String DES;//终点站
    @XMLValue("flgtTrs3c1")
    private String VI1;//经停1
    @XMLValue("flgtTrs3c2")
    private String VI2;//经停2
    @XMLValue("flgtTrs3c3")
    private String VI3;//经停3
    @XMLValue("flgtTrs3c4")
    private String VI4;//经停4
    @XMLValue("flgtTrs3c5")
    private String VI5;//经停5
    @XMLValue("flgtTrs3c6")
    private String VI6;//经停6

    private void appendVI(StringBuilder result, String vi) {
        if (!vi.isEmpty())
            result.append(vi).append("-");
    }


    public String getLineInfoValue() {
        StringBuilder result = new StringBuilder();
        if (!ORG.isEmpty() && !DES.isEmpty()) {
            result.append(ORG).append("-");
            appendVI(result, VI1);
            appendVI(result, VI2);
            appendVI(result, VI3);
            appendVI(result, VI4);
            appendVI(result, VI5);
            appendVI(result, VI6);
            result.append(DES);
        }
        return result.toString();
    }

    public String getLineInfoDesValue() {
        return DES;
    }
}
