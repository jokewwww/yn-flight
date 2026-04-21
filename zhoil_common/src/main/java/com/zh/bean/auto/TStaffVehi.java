package com.zh.bean.auto;

import java.io.Serializable;

/**
 * t_staff_vehi
 */
public class TStaffVehi implements Serializable {
    /**
     * 加油员ID
     */
    private String sfvhStaffId;

    /**
     * 加油车编号
     */
    private String vehiNo;

    /**
     * 登录状态（0：未登录，1：空闲，2：工作）
     */
    private Integer sfvhStaffStatus;

    /**
     * t_staff_vehi
     */
    private static final long serialVersionUID = 1L;

    /**
     * 加油员ID
     * @return sfvh_staff_id 加油员ID
     */
    public String getSfvhStaffId() {
        return sfvhStaffId;
    }

    /**
     * 加油员ID
     * @param sfvhStaffId 加油员ID
     */
    public void setSfvhStaffId(String sfvhStaffId) {
        this.sfvhStaffId = sfvhStaffId == null ? null : sfvhStaffId.trim();
    }

    /**
     * 加油车编号
     * @return vehi_no 加油车编号
     */
    public String getVehiNo() {
        return vehiNo;
    }

    /**
     * 加油车编号
     * @param vehiNo 加油车编号
     */
    public void setVehiNo(String vehiNo) {
        this.vehiNo = vehiNo == null ? null : vehiNo.trim();
    }

    /**
     * 登录状态（0：未登录，1：空闲，2：工作）
     * @return sfvh_staff_status 登录状态（0：未登录，1：空闲，2：工作）
     */
    public Integer getSfvhStaffStatus() {
        return sfvhStaffStatus;
    }

    /**
     * 登录状态（0：未登录，1：空闲，2：工作）
     * @param sfvhStaffStatus 登录状态（0：未登录，1：空闲，2：工作）
     */
    public void setSfvhStaffStatus(Integer sfvhStaffStatus) {
        this.sfvhStaffStatus = sfvhStaffStatus;
    }
}