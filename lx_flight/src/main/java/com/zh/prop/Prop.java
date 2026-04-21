package com.zh.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Prop {

    @Value(value = "${remote.login.ip}")
    private String loginIp;

    @Value(value = "${remote.login.port}")
    private String loginPort;

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginPort() {
        return loginPort;
    }

    public void setLoginPort(String loginPort) {
        this.loginPort = loginPort;
    }

}
