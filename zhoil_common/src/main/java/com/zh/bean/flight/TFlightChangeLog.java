package com.zh.bean.flight;

import com.zh.bean.login.BaseBean;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 卷皮管理对象 t_flight_change_log
 * 
 * @author ruoyi
 * @date 2020-06-09
 */
public class TFlightChangeLog extends BaseBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String ffid;

    private String flgtFlno;

    private String jsonData;

    private Date createTime;

    private String bz;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFfid(String ffid) 
    {
        this.ffid = ffid;
    }

    public String getFfid() 
    {
        return ffid;
    }
    public void setFlgtFlno(String flgtFlno) 
    {
        this.flgtFlno = flgtFlno;
    }

    public String getFlgtFlno() 
    {
        return flgtFlno;
    }
    public void setJsonData(String jsonData) 
    {
        this.jsonData = jsonData;
    }

    public String getJsonData() 
    {
        return jsonData;
    }
    public void setBz(String bz) 
    {
        this.bz = bz;
    }

    public String getBz() 
    {
        return bz;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ffid", getFfid())
            .append("flgtFlno", getFlgtFlno())
            .append("jsonData", getJsonData())
            .append("createTime", getCreateTime())
            .append("bz", getBz())
            .toString();
    }
}
