package com.higer.higerservice.entity.oilpro;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/29 10:48
 * @Description:
 */
@Entity
@Table(name = "a_apk_version")
@NamedQuery(name = "AApkVersion.findAll", query = "SELECT a FROM AApkVersion a")
public class AApkVersion {
    private Integer id;
    private String ver;
    private String apkUrl;
    private String remark;

    private Integer type;

    @Basic
    @Column(name = "type", nullable = true, length = 10)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ver", nullable = true, length = 64)
    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    @Basic
    @Column(name = "apk_url", nullable = true, length = 255)
    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AApkVersion that = (AApkVersion) o;
        return id == that.id &&
                Objects.equals(ver, that.ver) &&
                Objects.equals(apkUrl, that.apkUrl) &&
                Objects.equals(remark, that.remark);
    }

}
