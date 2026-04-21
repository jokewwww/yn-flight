package com.zh.bean.flight;

import java.io.Serializable;

public class MyFuelId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 油单ID
	 */
	private String[] flrcId;

	/**
	 * 油单ID
	 */
	public String[] getFlrcId() {
		return flrcId;
	}

	/**
	 * 油单ID
	 */
	public void setFlrcId(String[] flrcId) {
		this.flrcId = flrcId;
	}
	
}
