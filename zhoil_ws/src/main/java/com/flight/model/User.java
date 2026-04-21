package com.flight.model;

import java.io.Serializable;

/**
 * 加油员信息（用于通知PC端）
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

//	public User(String flrcId) {
//		super();
//		this.setSfvhStaffId(flrcId);
//	}
    // 加油员ID
    private String sfvhStaffId;
    // 名字
    private String staffName;
    // 状态：0==离线【用户里面的字段，和断线的种别不一样，】  0 灰色不在线  1 蓝色在线 2 红色正在工作
    private String sfvhStaffStatus;
    private int isAnimate; // 1 闪 0 不闪
    public User(String sfvhStaffId, String staffName, String sfvhStaffStatus, Integer isAnimate) {
        super();
        this.sfvhStaffId = sfvhStaffId;
        this.staffName = staffName;
        this.sfvhStaffStatus = sfvhStaffStatus;
        this.isAnimate = isAnimate;
    }

    public User() {
        super();
    }

    public int getIsAnimate() {
        return isAnimate;
    }

    public void setIsAnimate(int isAnimate) {
        this.isAnimate = isAnimate;
    }

    public String getSfvhStaffId() {
        return sfvhStaffId;
    }

    public void setSfvhStaffId(String sfvhStaffId) {
        this.sfvhStaffId = sfvhStaffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSfvhStaffStatus() {
        return sfvhStaffStatus;
    }

    public void setSfvhStaffStatus(String sfvhStaffStatus) {
        this.sfvhStaffStatus = sfvhStaffStatus;
    }
}
