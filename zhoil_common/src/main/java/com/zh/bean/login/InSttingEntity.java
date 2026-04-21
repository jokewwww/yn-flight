package com.zh.bean.login;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/25 17:24
 * @Description:
 */
public class InSttingEntity implements Serializable {

    private String id;
    private String name;
    private Boolean isShow;
    private String sortBy;
    private Integer priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
