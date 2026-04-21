package com.zh.bean;

import javax.validation.constraints.Digits;

import com.zh.annotation.MsgList;

/**
 * 接收页面的分页信息
 * 
 * @author 徐陆
 */
public class MyPage {

	/**
	 * 页码（下标值从1开始）
	 */
	@MsgList(msgs= {"页码"})
//	@NotBlank(message = "页数不能为空！")
	@Digits(integer = 10000, fraction = 0, message = "页数必须是数字！")
	private Integer page;

	/**
	 * 每页显示多少件
	 */
	@MsgList(msgs= {"页数"})
//	@NotBlank(message = "页数不能为空！")
	@Digits(integer = 10000, fraction = 0, message = "页数必须是数字！")
	private Integer pageSize;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
