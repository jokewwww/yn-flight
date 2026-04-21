package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 加油量实时监控
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: RefuelingVolumeMonitoring.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月06日 8:46
 */
public class FuelVolumeMonitorSend implements Serializable {

    private static final long serialVersionUID = 7766701522717057657L;
    private FuelVolumeMonitor fuelVolumeMonitor;

    public FuelVolumeMonitorSend(FuelVolumeMonitor fuelVolumeMonitor) {
        this.fuelVolumeMonitor = fuelVolumeMonitor;
    }

    public FuelVolumeMonitor getFuelVolumeMonitor() {
        return fuelVolumeMonitor;
    }

    public void setFuelVolumeMonitor(FuelVolumeMonitor fuelVolumeMonitor) {
        this.fuelVolumeMonitor = fuelVolumeMonitor;
    }
}
