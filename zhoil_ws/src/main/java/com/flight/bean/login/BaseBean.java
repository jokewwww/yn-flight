package com.flight.bean.login;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LoginUser loginUserIn;

    //机场统计日期条件,分机场统计
    private String dataTime;

    public LoginUser getLoginUserIn() {
        return loginUserIn;
    }

    public void setLoginUserIn(LoginUser loginUserIn) {
        this.loginUserIn = loginUserIn;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
