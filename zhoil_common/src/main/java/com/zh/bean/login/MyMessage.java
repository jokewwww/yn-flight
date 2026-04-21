package com.zh.bean.login;

import java.io.Serializable;

/**
 *  T_MESSAGE（常用消息表）
 */
public class MyMessage implements Serializable {
	
	/**
     * 消息ID（UUID）
     */
	private String msgId;
	
	/**
     * 角色（2:调度员，3:加油员）
     */
    private String msgRole;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 所属机场代码
     */
    private String msgAirportCode;

    /**
     * T_MESSAGE
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色（2:调度员，3:加油员）
     * @return msg_role 角色（2:调度员，3:加油员）
     */
    public String getMsgRole() {
        return msgRole;
    }

    /**
     * 角色（2:调度员，3:加油员）
     * @param msgRole 角色（2:调度员，3:加油员）
     */
    public void setMsgRole(String msgRole) {
        this.msgRole = msgRole == null ? null : msgRole.trim();
    }

    /**
     * 消息内容
     * @return msg_content 消息内容
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * 消息内容
     * @param msgContent 消息内容
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    /**
     * 所属机场代码
     * @return msg_airport_code 所属机场代码
     */
    public String getMsgAirportCode() {
        return msgAirportCode;
    }

    /**
     * 所属机场代码
     * @param msgAirportCode 所属机场代码
     */
    public void setMsgAirportCode(String msgAirportCode) {
        this.msgAirportCode = msgAirportCode == null ? null : msgAirportCode.trim();
    }
    
    /**
     * 消息ID（UUID）
     */
    public String getMsgId() {
		return msgId;
	}

    /**
     * 消息ID（UUID）
     */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}