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
public class FuelVolumeMonitor implements Serializable {
    private static final long serialVersionUID = 7838005343107605261L;

    /**
     * 实时加油量
     */
    private double fuel;

    /**
     * 流速1 // pad按秒传给我
     */
    private double currentSpeed;

    /**
     * 流速2 // pad按秒传给我
     */
    private double currentSpeedT;

    /**
     * 预计加油量 kg
     */
    private double estimatedRefuel;

    /**
     * 预计加油量
     */
    private double estimatedRefuelVol;

    /**
     * 加油进度
     */
    private double refuelProgress;

    /**
     * 预计剩余加油时间
     */
    private double estimatedRefuelTime;

    private String staffId;

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getCurrentSpeedT() {
        return currentSpeedT;
    }

    public void setCurrentSpeedT(double currentSpeedT) {
        this.currentSpeedT = currentSpeedT;
    }

    public double getEstimatedRefuel() {
        return estimatedRefuel;
    }

    public void setEstimatedRefuel(double estimatedRefuel) {
        this.estimatedRefuel = estimatedRefuel;
    }

    public double getRefuelProgress() {
        return refuelProgress;
    }

    public void setRefuelProgress(double refuelProgress) {
        this.refuelProgress = refuelProgress;
    }

    public double getEstimatedRefuelTime() {
        return estimatedRefuelTime;
    }

    public void setEstimatedRefuelTime(double estimatedRefuelTime) {
        this.estimatedRefuelTime = estimatedRefuelTime;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public double getEstimatedRefuelVol() {
        return this.estimatedRefuelVol;
    }

    public void setEstimatedRefuelVol(final double estimatedRefuelVol) {
        this.estimatedRefuelVol = estimatedRefuelVol;
    }
}
