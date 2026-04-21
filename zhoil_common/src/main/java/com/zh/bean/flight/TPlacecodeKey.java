package com.zh.bean.flight;

public class TPlacecodeKey {
    private String placeCode;

    private String airportCode;

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode == null ? null : placeCode.trim();
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }
}