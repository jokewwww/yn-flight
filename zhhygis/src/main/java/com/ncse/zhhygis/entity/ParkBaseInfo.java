package com.ncse.zhhygis.entity;

public class ParkBaseInfo {
    private Integer id;

    private String aircode;

    private String databasename;

    private String effective;

    private String parkpath;
    private String parkcenter;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAircode() {
        return aircode;
    }

    public void setAircode(String aircode) {
        this.aircode = aircode == null ? null : aircode.trim();
    }

    public String getParkcenter() {
        return parkcenter;
    }

    public void setParkcenter(String parkcenter) {
        this.parkcenter = parkcenter == null ? null : parkcenter.trim();
    }

    public String getParkpath() {
        return parkpath;
    }

    public void setParkpath(String parkpath) {
        this.parkpath = parkpath == null ? null : parkpath.trim();
    }

    public String getDatabasename() {
        return databasename;
    }

    public void setDatabasename(String databasename) {
        this.databasename = databasename == null ? null : databasename.trim();
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective == null ? null : effective.trim();
    }
}