package com.zh.bean.login;

import java.io.Serializable;

/**
 * 资源权限表
 * T_RESOURCE
 */
public class MyResource implements Serializable {
    /**
     * 资源ID（uuid）
     */
    private String rescId;

    /**
     * 资源名
     */
    private String rescName;

    /**
     * 人员类型
     */
    private String rescStaffType;

    /**
     * 资源URL
     */
    private String rescDoUrl;

    /**
     * T_RESOURCE
     */
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID（uuid）
     * @return resc_id 资源ID（uuid）
     */
    public String getRescId() {
        return rescId;
    }

    /**
     * 资源ID（uuid）
     * @param rescId 资源ID（uuid）
     */
    public void setRescId(String rescId) {
        this.rescId = rescId == null ? null : rescId.trim();
    }

    /**
     * 资源名
     * @return resc_name 资源名
     */
    public String getRescName() {
        return rescName;
    }

    /**
     * 资源名
     * @param rescName 资源名
     */
    public void setRescName(String rescName) {
        this.rescName = rescName == null ? null : rescName.trim();
    }

    /**
     * 人员类型
     * @return resc_staff_type 人员类型
     */
    public String getRescStaffType() {
        return rescStaffType;
    }

    /**
     * 人员类型
     * @param rescStaffType 人员类型
     */
    public void setRescStaffType(String rescStaffType) {
        this.rescStaffType = rescStaffType == null ? null : rescStaffType.trim();
    }

    /**
     * 资源URL
     * @return resc_do_url 资源URL
     */
    public String getRescDoUrl() {
        return rescDoUrl;
    }

    /**
     * 资源URL
     * @param rescDoUrl 资源URL
     */
    public void setRescDoUrl(String rescDoUrl) {
        this.rescDoUrl = rescDoUrl == null ? null : rescDoUrl.trim();
    }
}