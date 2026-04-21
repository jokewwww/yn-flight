package com.zh.bean.flight;

import java.util.Date;

public class TBouncedLog {
    private String id;

    private String content;

    private Date creTime;

    private String flgtFlno;

    private Integer flgtType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime == null ? null : creTime;
    }

    public String getFlgtFlno() {
        return flgtFlno;
    }

    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno == null ? null : flgtFlno.trim();
    }

    public Integer getFlgtType() {
        return flgtType;
    }

    public void setFlgtType(Integer flgtType) {
        this.flgtType = flgtType;
    }
}