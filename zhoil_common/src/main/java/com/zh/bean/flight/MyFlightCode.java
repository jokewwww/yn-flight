package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 *  T_FLIGHT_CODE（飞机号码表）
 */
public class MyFlightCode implements Serializable {
    /**
     * 飞机号码·
     */
    private String arcrRegn;

    /**
     * 飞机类型
     */
    private String arcrAcname;

    /**
     * 指定航班号
     */
    private String flno;

    /**
     * 购货方编号
     */
    private String arcrCustomNum;

    /**
     * 开始日期
     */
    private Date arcrStartDate;

    /**
     * 结束日期
     */
    private Date arcrEndDate;

    /**
     * 购货商名称（业务字段）
     */
    private String arcrName;
    
    /**
     * 购货商国家（业务字段）
     */
    private String cstmRegion;

    private String newCstmRegion;

    /**
     * 删除标识 0-未删除 1-删除
     */
    private Integer delFlag;

    /**
     * 中航油飞机号码ID
     */
    private Integer cid;

    /**
     * 记录创建时间-中航油创建时间
     */
    private Date cnafCreateTime;

    /**
     * 记录更新时间-中航油更新时间
     */
    private Date cnafUpdateTime;

    /**
     * 记录创建时间-系统
     */
    private Date createTime;

    /**
     * 记录更新时间-系统
     */
    private Date updateTime;


    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    public String getNewCstmRegion() {
        return newCstmRegion;
    }

    public void setNewCstmRegion(String newCstmRegion) {
        this.newCstmRegion = newCstmRegion;
    }

    /**
	 * @return the arcrName
	 */
	public String getArcrName() {
		return arcrName;
	}

	/**
	 * @param arcrName the arcrName to set
	 */
	public void setArcrName(String arcrName) {
		this.arcrName = arcrName;
	}

	/**
     * T_FLIGHT_CODE
     */
    private static final long serialVersionUID = 1L;

    /**
     * 飞机号码
     * @return arcr_regn 飞机号码
     */
    public String getArcrRegn() {
        return arcrRegn;
    }

    /**
     * 飞机号码
     * @param arcrRegn 飞机号码
     */
    public void setArcrRegn(String arcrRegn) {
        this.arcrRegn = arcrRegn == null ? null : arcrRegn.trim();
    }

    /**
     * 飞机类型
     * @return arcr_acname 飞机类型
     */
    public String getArcrAcname() {
        return arcrAcname;
    }

    /**
     * 飞机类型
     * @param arcrAcname 飞机类型
     */
    public void setArcrAcname(String arcrAcname) {
        this.arcrAcname = arcrAcname == null ? null : arcrAcname.trim();
    }

    /**
     * 购货方编号
     * @return arcr_custom_num 购货方编号
     */
    public String getArcrCustomNum() {
        return arcrCustomNum;
    }

    /**
     * 购货方编号
     * @param arcrCustomNum 购货方编号
     */
    public void setArcrCustomNum(String arcrCustomNum) {
        this.arcrCustomNum = arcrCustomNum == null ? null : arcrCustomNum.trim();
    }

    /**
     * 开始日期
     * @return arcr_start_date 开始日期
     */
    public Date getArcrStartDate() {
        return arcrStartDate;
    }

    /**
     * 开始日期
     * @param arcrStartDate 开始日期
     */
    public void setArcrStartDate(Date arcrStartDate) {
        this.arcrStartDate = arcrStartDate;
    }

    /**
     * 结束日期
     * @return arcr_end_date 结束日期
     */
    public Date getArcrEndDate() {
        return arcrEndDate;
    }

    /**
     * 结束日期
     * @param arcrEndDate 结束日期
     */
    public void setArcrEndDate(Date arcrEndDate) {
        this.arcrEndDate = arcrEndDate;
    }
    
    /**
     * 购货商国家
     */
    public String getCstmRegion() {
		return cstmRegion;
	}

    /**
     * 购货商国家
     */
	public void setCstmRegion(String cstmRegion) {
		this.cstmRegion = cstmRegion;
	}

    public Integer getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(final Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getCid() {
        return this.cid;
    }

    public void setCid(final Integer cid) {
        this.cid = cid;
    }

    public Date getCnafCreateTime() {
        return this.cnafCreateTime;
    }

    public void setCnafCreateTime(final Date cnafCreateTime) {
        this.cnafCreateTime = cnafCreateTime;
    }

    public Date getCnafUpdateTime() {
        return this.cnafUpdateTime;
    }

    public void setCnafUpdateTime(final Date cnafUpdateTime) {
        this.cnafUpdateTime = cnafUpdateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }
}