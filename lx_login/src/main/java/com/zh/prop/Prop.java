package com.zh.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Prop {

    @Value(value = "${remote.flight.ip}")
    private String flightIp;

    @Value(value = "${remote.flight.port}")
    private String flightPort;

    @Value(value = "${remote.map.ip}")
    private String mapIp;

    @Value(value = "${remote.map.port}")
    private String mapPort;

    public String getFlightIp() {
        return flightIp;
    }

    public void setFlightIp(String flightIp) {
        this.flightIp = flightIp;
    }

    public String getFlightPort() {
        return flightPort;
    }

    public void setFlightPort(String flightPort) {
        this.flightPort = flightPort;
    }

    public String getMapIp() {
        return mapIp;
    }

    public void setMapIp(String mapIp) {
        this.mapIp = mapIp;
    }

    public String getMapPort() {
        return mapPort;
    }

    public void setMapPort(String mapPort) {
        this.mapPort = mapPort;
    }
}
