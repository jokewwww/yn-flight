package com.higer.higerservice.entity.oilpro;

import java.util.List;


public class UpdateResponse {
    private int status;//0表示不需要更新 1表示需要更新
    private String date;//需要更新时候有效 更新的时间 再次请求是否需要更新时候传送此参数供服务端判断是否需要更新
    private List<String> listAllData;//需要更新时候有效 所有数据的数据库ID

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

    public List<String> getListAllData() {
        return listAllData;
    }

    public void setListAllData(List<String> listAllData) {
        this.listAllData = listAllData;
    }

}
