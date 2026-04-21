package com.zh.bean.auto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BUS_TASK
 * @author lulinlin
 */

public class BusTask implements Serializable {
    /**
     * 任务表id
     */
    private Integer id;

    /**
     * 执行人
     */
    private String tenm;

    /**
     * 航班id
     */
    private String flightId;

    /**
     * 航班唯一号
     */
    private String flseq;

    /**
     * 创建人
     */
    private String cenm;

    /**
     * 任务内容
     */
    private Integer taskContent;

    /**
     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加（抽）油完成6：油单待审核7：任务完成8：拒绝
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 油单编号
     */
    private String oilId;
    
    
    
    public String getOilId() {
		return oilId;
	}

	public void setOilId(String oilId) {
		this.oilId = oilId;
	}

	/**
     * TASK
     */
    private static final long serialVersionUID = 1L;

    /**
     * 任务表id
     *
     * @return id 任务表id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 任务表id
     *
     * @param id 任务表id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 执行人
     *
     * @return Tenm 执行人
     */
    public String getTenm() {
        return tenm;
    }

    /**
     * 执行人
     *
     * @param tenm 执行人
     */
    public void setTenm(String tenm) {
        this.tenm = tenm == null ? null : tenm.trim();
    }

    /**
     * 航班id
     *
     * @return FLIGHT_ID 航班id
     */
    public String getFlightId() {
        return flightId;
    }

    /**
     * 航班id
     *
     * @param flightId 航班id
     */
    public void setFlightId(String flightId) {
        this.flightId = flightId == null ? null : flightId.trim();
    }

    /**
     * 航班唯一号
     *
     * @return flseq 航班唯一号
     */
    public String getFlseq() {
        return flseq;
    }

    /**
     * 航班唯一号
     *
     * @param flseq 航班唯一号
     */
    public void setFlseq(String flseq) {
        this.flseq = flseq == null ? null : flseq.trim();
    }

    /**
     * 创建人
     *
     * @return cenm 创建人
     */
    public String getCenm() {
        return cenm;
    }

    /**
     * 创建人
     *
     * @param cenm 创建人
     */
    public void setCenm(String cenm) {
        this.cenm = cenm == null ? null : cenm.trim();
    }

    /**
     * 任务内容
     *
     * @return TASK_CONTENT 任务内容
     */
    public Integer getTaskContent() {
        return taskContent;
    }

    /**
     * 任务内容
     *
     * @param taskContent 任务内容
     */
    public void setTaskContent(Integer taskContent) {
        this.taskContent = taskContent;
    }

    /**
     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加（抽）油完成6：油单待审核7：任务完成8：拒绝
     *
     * @return STATUS 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加（抽）油完成6：油单待审核7：任务完成8：拒绝
     */
    public String getStatus() {
        return status;
    }

    /**
     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加（抽）油完成6：油单待审核7：任务完成8：拒绝
     *
     * @param status 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加（抽）油完成6：油单待审核7：任务完成8：拒绝
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 创建时间
     *
     * @return CREATE_TIME 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}