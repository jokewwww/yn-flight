package com.zh.bean.login;

import java.io.Serializable;

public class MySettPriority implements Serializable {
	
	/**
	 * 用来交换配置信息行的优先级
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 小的数字ID
	 */
	private String minSettId;
	
	/**
	 * 小的数字ID
	 */
	public String getMinSettId() {
		return minSettId;
	}

	/**
	 * 小的数字ID
	 */
	public void setMinSettId(String minSettId) {
		this.minSettId = minSettId;
	}

	/**
	 * 大的数字ID
	 */
	public String getMaxSettId() {
		return maxSettId;
	}

	/**
	 * 大的数字ID
	 */
	public void setMaxSettId(String maxSettId) {
		this.maxSettId = maxSettId;
	}

	/**
	 * 小的数字
	 */
	private Integer minSettPriority;
	
	/**
	 * 大的数字ID
	 */
	private String maxSettId;
	
	/**
	 * 大的数字
	 */
	private Integer maxSettPriority;

	/**
	 * 小的数字
	 */
	public Integer getMinSettPriority() {
		return minSettPriority;
	}

	/**
	 * 小的数字
	 */
	public void setMinSettPriority(Integer minSettPriority) {
		this.minSettPriority = minSettPriority;
	}

	/**
	 * 大的数字
	 */
	public Integer getMaxSettPriority() {
		return maxSettPriority;
	}

	/**
	 * 大的数字
	 */
	public void setMaxSettPriority(Integer maxSettPriority) {
		this.maxSettPriority = maxSettPriority;
	}
}