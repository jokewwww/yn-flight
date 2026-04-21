package com.zh.bean.flight;

import java.util.Date;

public class TFlightTrouble {
    private String id;

    private String flgtRegn;

    private String msg;

    private Date flgtTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFlgtRegn() {
        return flgtRegn;
    }

    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn == null ? null : flgtRegn.trim();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Date getFlgtTime() {
        return flgtTime;
    }

    public void setFlgtTime(Date flgtTime) {
        this.flgtTime = flgtTime;
    }
}