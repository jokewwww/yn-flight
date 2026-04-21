package com.zh.bean.flight;

public class TOilSign {
    private Long id;

    private String flgtId;

    private Integer flgtTakeoffFuel;

    private Integer flgtChockFuel;

    private Integer flgtOtatFuel;

    private Integer leftFuel;

    private Integer rightFuel;

    private Integer centerFuel;

    private String fuelSign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlgtId() {
        return flgtId;
    }

    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId == null ? null : flgtId.trim();
    }

    public Integer getFlgtTakeoffFuel() {
        return flgtTakeoffFuel;
    }

    public void setFlgtTakeoffFuel(Integer flgtTakeoffFuel) {
        this.flgtTakeoffFuel = flgtTakeoffFuel;
    }

    public Integer getFlgtChockFuel() {
        return flgtChockFuel;
    }

    public void setFlgtChockFuel(Integer flgtChockFuel) {
        this.flgtChockFuel = flgtChockFuel;
    }

    public Integer getFlgtOtatFuel() {
        return flgtOtatFuel;
    }

    public void setFlgtOtatFuel(Integer flgtOtatFuel) {
        this.flgtOtatFuel = flgtOtatFuel;
    }

    public Integer getLeftFuel() {
        return leftFuel;
    }

    public void setLeftFuel(Integer leftFuel) {
        this.leftFuel = leftFuel;
    }

    public Integer getRightFuel() {
        return rightFuel;
    }

    public void setRightFuel(Integer rightFuel) {
        this.rightFuel = rightFuel;
    }

    public Integer getCenterFuel() {
        return centerFuel;
    }

    public void setCenterFuel(Integer centerFuel) {
        this.centerFuel = centerFuel;
    }

    public String getFuelSign() {
        return fuelSign;
    }

    public void setFuelSign(String fuelSign) {
        this.fuelSign = fuelSign == null ? null : fuelSign.trim();
    }
}