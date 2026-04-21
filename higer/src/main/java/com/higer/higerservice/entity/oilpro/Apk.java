package com.higer.higerservice.entity.oilpro;

public class Apk {
    private Integer id;
    private String ver;
    private byte[] apk;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public byte[] getApk() {
        return apk;
    }

    public void setApk(byte[] apk) {
        this.apk = apk;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
