package cn.iwen.weblog.web;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.iwen.frame.BaseUtils;

@Table(name="web_log")
public class LogsBean {

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id",nullable=false)
	private Integer logId;

	/**
	*
	*/
	@Column(name = "create_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIMEMS_FMT)
	private java.util.Date createTime;

	/**
	*
	*/
	@Column(name = "code_name",nullable=false)
	private String codeName;

	/**
	*
	*/
	@Column(name = "thread_name",nullable=false)
	private String threadName;

	/**
	*
	*/
	@Column(name = "msg",nullable=false)
	private String msg;

	/**
	*
	*/
	@Column(name = "code1")
	private Integer code1;

	/**
	*
	*/
	@Column(name = "code2")
	private Integer code2;

	/**
	*
	*/
	@Column(name = "code3")
	private Integer code3;

	/**
	*
	*/
	@Column(name = "code4")
	private Integer code4;

	/**
	*
	*/
	@Column(name = "text1")
	private String text1;

	/**
	*
	*/
	@Column(name = "text2")
	private String text2;

	/**
	*
	*/
	@Column(name = "text3")
	private String text3;

	/**
	*
	*/
	@Column(name = "text4")
	private String text4;


	
	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId=logId;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime=createTime;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName=codeName;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName=threadName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg=msg;
	}

	public Integer getCode1() {
		return code1;
	}

	public void setCode1(Integer code1) {
		this.code1=code1;
	}

	public Integer getCode2() {
		return code2;
	}

	public void setCode2(Integer code2) {
		this.code2=code2;
	}

	public Integer getCode3() {
		return code3;
	}

	public void setCode3(Integer code3) {
		this.code3=code3;
	}

	public Integer getCode4() {
		return code4;
	}

	public void setCode4(Integer code4) {
		this.code4=code4;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1=text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2=text2;
	}

	public String getText3() {
		return text3;
	}

	public void setText3(String text3) {
		this.text3=text3;
	}

	public String getText4() {
		return text4;
	}

	public void setText4(String text4) {
		this.text4=text4;
	}

	public void addMsg(String line) {
		if(msg == null) msg = line;
		else msg += line;
	}

}
