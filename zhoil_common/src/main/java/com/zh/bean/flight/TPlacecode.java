package com.zh.bean.flight;

public class TPlacecode extends TPlacecodeKey {
    private String placeCodeType;

    private String flrcHydrtPitNo;
    private String flrcHydrtPitNo1;
    private String flrcHydrtPitNo2;
    private String flrcHydrtPitNo3;
    private String flrcHydrtPitNo4;

    private String fontColor;

    private String placeCodeTypeName;

    public String getPlaceCodeType() {
        return placeCodeType;
    }

    public void setPlaceCodeType(String placeCodeType) {
        this.placeCodeType = placeCodeType == null ? null : placeCodeType.trim();
    }

    public String getFlrcHydrtPitNo() {
        return flrcHydrtPitNo;
    }

    public void setFlrcHydrtPitNo(String flrcHydrtPitNo) {
        this.flrcHydrtPitNo = flrcHydrtPitNo == null ? null : flrcHydrtPitNo.trim();
    }

    public String getFlrcHydrtPitNo1() {
        return flrcHydrtPitNo1;
    }

    public void setFlrcHydrtPitNo1(String flrcHydrtPitNo1) {
        this.flrcHydrtPitNo1 = flrcHydrtPitNo1;
    }

    public String getFlrcHydrtPitNo2() {
        return flrcHydrtPitNo2;
    }

    public void setFlrcHydrtPitNo2(String flrcHydrtPitNo2) {
        this.flrcHydrtPitNo2 = flrcHydrtPitNo2;
    }

    public String getFlrcHydrtPitNo3() {
        return flrcHydrtPitNo3;
    }

    public void setFlrcHydrtPitNo3(String flrcHydrtPitNo3) {
        this.flrcHydrtPitNo3 = flrcHydrtPitNo3;
    }

    public String getFlrcHydrtPitNo4() {
        return flrcHydrtPitNo4;
    }

    public void setFlrcHydrtPitNo4(String flrcHydrtPitNo4) {
        this.flrcHydrtPitNo4 = flrcHydrtPitNo4;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getPlaceCodeTypeName() {
        return placeCodeTypeName;
    }

    public void setPlaceCodeTypeName(String placeCodeTypeName) {
        this.placeCodeTypeName = placeCodeTypeName;
    }
}