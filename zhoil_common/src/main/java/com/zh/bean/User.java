package com.zh.bean;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zh.annotation.MsgList;
import com.zh.format.YYYYMMDD_HHMMSS;

public class User {
	@MsgList(msgs= {"字段的汉字名，测试"})
	@NotBlank(message="不能为空！")
	private String abc;

	@JsonDeserialize(using = YYYYMMDD_HHMMSS.class)
	private Date date2;
	
	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}
	
	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

}
