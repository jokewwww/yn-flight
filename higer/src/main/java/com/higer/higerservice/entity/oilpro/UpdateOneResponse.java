package com.higer.higerservice.entity.oilpro;


public class UpdateOneResponse {
    private int status;//0表示不需要更新  1表示需要更新
    private String date;//需要更新时有效 该条数据更新的最后时间
    private String picData;//需要更新时有效 图片数据的base64码
    private String userInfo;//需要更新时有效  该图片数据的拥护信息(存库的json数据base64后)

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicData() {
        return picData;
    }

    public void setPicData(String picData) {
        this.picData = picData;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }


}
