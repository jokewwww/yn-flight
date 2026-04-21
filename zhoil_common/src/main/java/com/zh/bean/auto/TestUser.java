package com.zh.bean.auto;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表 test_user
 */
public class TestUser implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 日期
     */
    private Date createTime;

    /**
     * test_user
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     * @return id ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * ID
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 名字
     * @return name 名字
     */
    public String getName() {
        return name;
    }

    /**
     * 名字
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 日期
     * @return create_time 日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 日期
     * @param createTime 日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}