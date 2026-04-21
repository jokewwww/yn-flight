package com.zh.bean.login;

import java.io.Serializable;

public class Row implements Serializable {

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
     * 优先级
     */
    private Integer settPriority;
    
    /**
     * 优先级
     */
    public Integer getSettPriority() {
		return settPriority;
	}

    /**
     * 优先级
     */
	public void setSettPriority(Integer settPriority) {
		this.settPriority = settPriority;
	}
	
	/**
     * 配置信息row
     */
    private String settRow;

    /**
     * 配置信息row
     */
	public String getSettRow() {
		return settRow;
	}

	/**
     * 配置信息row
     */
	public void setSettRow(String settRow) {
		this.settRow = settRow;
	}
	
	/**
     * 是否执行此配置（0：否，1：是）
     */
    private Integer settStatus;
    
    /**
     * 是否执行此配置（0：否，1：是）
     */
    public Integer getSettStatus() {
		return settStatus;
	}
    
    /**
     * 是否执行此配置（0：否，1：是）
     */
	public void setSettStatus(Integer settStatus) {
		this.settStatus = settStatus;
	}
}
