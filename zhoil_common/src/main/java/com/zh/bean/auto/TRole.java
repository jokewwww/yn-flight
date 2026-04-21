package com.zh.bean.auto;

import java.io.Serializable;

public class TRole implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * t_role
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
}