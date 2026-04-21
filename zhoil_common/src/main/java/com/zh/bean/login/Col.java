package com.zh.bean.login;

import java.io.Serializable;

public class Col implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * 配置ID（uuid）
     */
    private String settOptionId;
	
    /**
     * 配置ID（uuid）
     */
	public String getSettOptionId() {
		return settOptionId;
	}

	/**
     * 配置ID（uuid）
     */
	public void setSettOptionId(String settOptionId) {
		this.settOptionId = settOptionId;
	}

	/**
     * 配置信息col
     */
    private String settCol;

    /**
     * 配置信息col
     */
	public String getSettCol() {
		return settCol;
	}

	/**
     * 配置信息col
     */
	public void setSettCol(String settCol) {
		this.settCol = settCol;
	}
}
