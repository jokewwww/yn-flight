package com.zh.bean.flight;

import com.zh.bean.login.BaseBean;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/5/5 13:18
 * @Description:
 */

public class MyNewFlight extends BaseBean {

    private Integer paginate;

    private String queryString;

    private Integer pageSize;

    private String staffAirportCode;

    private String staffAptareaCode;

    private String version;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPaginate() {
        return paginate;
    }

    public void setPaginate(Integer paginate) {
        this.paginate = paginate;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
