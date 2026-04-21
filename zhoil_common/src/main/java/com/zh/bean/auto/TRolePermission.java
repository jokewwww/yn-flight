package com.zh.bean.auto;

import java.io.Serializable;

public class TRolePermission implements Serializable {
    /**
     * 角色ID
     */
    private Integer roleid;

    /**
     * 资源ID
     */
    private Integer permissionid;

    /**
     * t_role_permission
     */
    private static final long serialVersionUID = 1L;

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

    /**
     * 资源ID
     * @return permissionId 资源ID
     */
    public Integer getPermissionid() {
        return permissionid;
    }

    /**
     * 资源ID
     * @param permissionid 资源ID
     */
    public void setPermissionid(Integer permissionid) {
        this.permissionid = permissionid;
    }
}