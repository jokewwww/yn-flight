package com.higer.higerservice.entity.oilpro;


public class UpdateOneRequest {
    private String id;//UpdateResponse 返回的listAllData中的一个数据
    private String date;//该条数据上次更新的时间 从未更新过为空

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
