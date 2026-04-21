package com.zh.bean;

import com.github.pagehelper.PageInfo;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/5/5 16:28
 * @Description:
 */
public class ResponseObject<T> {

    public ResponseObject(String code, String errInfo, T data, PageInfo pageInfo) {
        super();
        this.code = code;
        this.errInfo = errInfo;
        this.data = data;
        this.pageInfo = pageInfo;
    }

    /**
     * code 0:OK; 2:NG (1:warn，这个暂时不用)
     */
    private String code;

    /**
     * 错误信息
     */
    private String errInfo;

    /**
     * 数据
     */
    private T data;

    private PageInfo pageInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
