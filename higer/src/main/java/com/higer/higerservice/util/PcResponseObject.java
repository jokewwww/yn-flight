package com.higer.higerservice.util;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/25 09:52
 * @Description:
 */
@SuppressWarnings("rawtypes")
public class PcResponseObject<T> {

    private String serviceId;// 服务唯一id
    private int code;// 服务状态代码0:请求成功1:业务操作不成功或服务器异常2:访问无权限 ,登录过期或没登录3:频率限制4:服务器报错，或无法连接服务器 ，或json数据解析错误等
    private String msg;// 普通服务返回消息
    private String errMsg;// 服务异常时返回消息
    private T data;// 单条实体返回
    private List<T> datas;// 多条实体返回
    private PageInfo pageInfo;// 分页信息
    private Integer number;//条数
    public PcResponseObject() {
    }
    public PcResponseObject(Page<T> page) {
        PageInfo<T> pi = new PageInfo<T>(page);
        this.datas = page.getContent();
        this.pageInfo = pi;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ResponseObject [serviceId=" + serviceId + ", code=" + code + ", msg=" + msg + ", errMsg=" + errMsg
                + ", data=" + data + ", datas=" + datas + ", pageInfo=" + pageInfo + "，number= " + number + " ]";
    }


}
