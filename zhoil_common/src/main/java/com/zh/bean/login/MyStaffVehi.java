package com.zh.bean.login;

import java.io.Serializable;

/**
 * 人员车辆表
 * T_STAFF_VEHI
 */
public class MyStaffVehi implements Serializable {
    /**
     * 加油员ID
     */
    private String sfvhStaffId;

    /**
     * 加油车编号
     */
    private String sfvhVehiNo;

    /**
     * 登录状态（0：未登录，1：空闲，2：工作）
     */
    private Integer sfvhStaffStatus;

    /**
     * 车牌号码
     */
    private String vehiPlateNo;

    /**
     * T_STAFF_VEHI
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
     * @return sfvh_vehi_no 加油车编号
     */
    public String getSfvhVehiNo() {
        return sfvhVehiNo;
    }

    /**
     * 加油车编号
     * @param sfvhVehiNo 加油车编号
     */
    public void setSfvhVehiNo(String sfvhVehiNo) {
        this.sfvhVehiNo = sfvhVehiNo == null ? null : sfvhVehiNo.trim();
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

    public String getVehiPlateNo() {
        return vehiPlateNo;
    }

    public void setVehiPlateNo(String vehiPlateNo) {
        this.vehiPlateNo = vehiPlateNo;
    }
}