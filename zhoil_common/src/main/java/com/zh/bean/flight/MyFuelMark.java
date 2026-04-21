package com.zh.bean.flight;

import java.io.Serializable;

public class MyFuelMark implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 油单ID
	 */
	private String flrcId;

	private Integer flrcRevwStatus;

    public String getFlrcId() {
        return flrcId;
    }

    public void setFlrcId(String flrcId) {
        this.flrcId = flrcId;
    }

    public Integer getFlrcRevwStatus() {
        return flrcRevwStatus;
    }

    public void setFlrcRevwStatus(Integer flrcRevwStatus) {
        this.flrcRevwStatus = flrcRevwStatus;
    }
}
