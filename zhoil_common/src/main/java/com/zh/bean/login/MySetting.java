package com.zh.bean.login;

import java.io.Serializable;
import java.util.Map;

/**
 * 页面配置表
 * T_SETTING
 */
public class MySetting implements Serializable {
    /**
     * 配置ID（uuid）
     */
    private String settOptionId;

    /**
     * 人员ID
     */
    private String settStaffId;

    /**
     * 机场代码
     */
    private String settAirportCode;

    /**
     * 前端配置信息（航班任务列表、重要消息定制）
     */
    private String settInfo;
    private Map<String, Object> settInfomap;

    /**
     * 配置信息状态（1：表头，2：颜色）
     */
    private String settType;
    
    /**
     * 区别row和col
     */
    private Integer settRowOrCol;
    
	public Integer getSettRowOrCol() {
		return settRowOrCol;
	}

	public void setSettRowOrCol(Integer settRowOrCol) {
		this.settRowOrCol = settRowOrCol;
	}

	/**
     * 是否执行此配置（0：否，1：是）
     */
    private Integer settStatus;
    
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
	
	/**
     * T_SETTING
     */
    private static final long serialVersionUID = 1L;
   
    /**
     * 配置信息状态（1：表头，2：颜色）
     */ 
    public String getSettType() {
		return settType;
	}
    /**
     * 配置信息状态（1：表头，2：颜色）
     */
	public void setSettType(String settType) {
		this.settType = settType;
	}

	/**
     * 配置ID（uuid）
     * @return sett_option_id 配置ID（uuid）
     */
    public String getSettOptionId() {
        return settOptionId;
    }

    /**
     * 配置ID（uuid）
     * @param settOptionId 配置ID（uuid）
     */
    public void setSettOptionId(String settOptionId) {
        this.settOptionId = settOptionId == null ? null : settOptionId.trim();
    }

    /**
     * 人员ID
     * @return sett_staff_id 人员ID
     */
    public String getSettStaffId() {
        return settStaffId;
    }

    /**
     * 人员ID
     * @param settStaffId 人员ID
     */
    public void setSettStaffId(String settStaffId) {
        this.settStaffId = settStaffId == null ? null : settStaffId.trim();
    }

    /**
     * 机场代码
     * @return sett_airport_code 机场代码
     */
    public String getSettAirportCode() {
        return settAirportCode;
    }

    /**
     * 机场代码
     * @param settAirportCode 机场代码
     */
    public void setSettAirportCode(String settAirportCode) {
        this.settAirportCode = settAirportCode == null ? null : settAirportCode.trim();
    }

    /**
     * 前端配置信息（航班任务列表、重要消息定制）
     * @return sett_info 前端配置信息（航班任务列表、重要消息定制）
     */
    public String getSettInfo() {
        return settInfo;
    }

    /**
     * 前端配置信息（航班任务列表、重要消息定制）
     * @param settInfo 前端配置信息（航班任务列表、重要消息定制）
     */
    public void setSettInfo(String settInfo) {
        this.settInfo = settInfo == null ? null : settInfo.trim();
    }
	public Map<String, Object> getSettInfomap() {
		return settInfomap;
	}
	public void setSettInfomap(Map<String, Object> settInfomap) {
		this.settInfomap = settInfomap;
	}
}