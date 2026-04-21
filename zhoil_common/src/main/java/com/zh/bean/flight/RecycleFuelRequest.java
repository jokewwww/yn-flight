package com.zh.bean.flight;

import java.util.List;

public class RecycleFuelRequest {
    private List<TFuelNo> tFuelNo;

    private String password;

    private String padId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TFuelNo> gettFuelNo() {
        return tFuelNo;
    }

    public void settFuelNo(List<TFuelNo> tFuelNo) {
        this.tFuelNo = tFuelNo;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }
}