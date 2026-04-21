package com.zh.bean;

import java.util.Date;

public class OperationLog {

    private long id;

    //员工id
    private String staffId;
    //员工名称
    private String staffName;
    //模块名称
    private String moduleName;
    //操作名称
    private String actionName;
    //操作是否成功 0：失败，1：成功
    private int succeed;
    //请求内容 requestBody
    private String content;
    //失败原因
    private String failReason;

    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getSucceed() {
        return succeed;
    }

    public void setSucceed(int succeed) {
        this.succeed = succeed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
