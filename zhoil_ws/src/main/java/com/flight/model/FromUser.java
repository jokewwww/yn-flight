package com.flight.model;

import java.io.Serializable;

/**
 * 登录者信息
 */
public class FromUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fromUserId;
    /**
     * false: 调度员；true：加油员。
     */
    private boolean flg;
    private String token;
    private String uuid;
    private boolean isLogout;
    private boolean timeout;
    // 名字
    private String staffName;
    // 状态：0==离线【用户里面的字段，和断线的种别不一样，】
    private String sfvhStaffStatus;
    /**
     * 所属机场代码
     */
    private String staffAirportCode;
    /**
     * 所属机场区域代码
     */
    private String staffAptareaCode;
    /**
     * 登录之后再redis中的key
     */
    private String loginKey;

    public FromUser(String fromUserId, boolean flg, String uuid) {
        super();
        this.fromUserId = fromUserId;
        this.flg = flg;
        this.uuid = uuid;
    }

    public FromUser() {
        super();
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public boolean isFlg() {
        return flg;
    }

    public void setFlg(boolean flg) {
        this.flg = flg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStaffAirportCode() {
        return staffAirportCode;
    }

    public void setStaffAirportCode(String staffAirportCode) {
        this.staffAirportCode = staffAirportCode;
    }

    public String getStaffAptareaCode() {
        return staffAptareaCode;
    }

    public void setStaffAptareaCode(String staffAptareaCode) {
        this.staffAptareaCode = staffAptareaCode;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isLogout() {
        return isLogout;
    }

    public void setLogout(boolean isLogout) {
        this.isLogout = isLogout;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
}
