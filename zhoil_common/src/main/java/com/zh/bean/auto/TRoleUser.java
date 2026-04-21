package com.zh.bean.auto;

import java.io.Serializable;

public class TRoleUser implements Serializable {
    /**
     * 人员ID
     */
    private String userid;

    /**
     * 角色ID
     */
    private Integer roleid;

    /**
     * t_role_user
     */
    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     * @return userId 人员ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 人员ID
     * @param userid 人员ID
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 角色ID
     * @return roleId 角色ID
     */
    public Integer getRoleid() {
        return roleid;
    }

    /**
     * 角色ID
     * @param roleid 角色ID
     */
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
}