package com.zh.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 给ERP推送kafka的实体类
 */
public class KafkaErp implements Serializable {
    /**
     * 销售组织
     */
    private String vkorg;

    /**
     * 分销渠道
     */
    private String vtweg;

    /**
     * 部门F
     */
    private String spart;

    /**
     * 类型
     */
    private Integer zlx;

    /**
     * 加油单编号
     */
    private String bstkd;

    /**
     * 加油日期
     */
    private Date jydata;

    /**
     * 飞机号码（国内）
     */
    private String trmtyp;

    /**
     * 飞机类型（国内）
     */
    private String traty;

    /**
     * 飞机单位（客户编号）
     */
    private String kunnr;

    /**
     * 航班号
     */
    private String mfrgr;

    /**
     * 评估类型
     */
    private String bwtar;

    /**
     * 密度 
     */
    private Double sjmd;

    /**
     * 温度
     */
    private Double bstzd;

    /**
     * 体积
     */
    private Double jytj;

    /**
     * 质量
     */
    private Double sjzl;

    /**
     * 加油车编号
     */
    private String oicntper;

    /**
     * 地井编号
     */
    private String oicntpho;

    /**
     * 加油员
     */
    private String pernr;

    /**
     * 加油员
     */
    private String pernr2;

    /**
     * 加油开始时间
     */
    private Date jykssj;

    /**
     * 加油结束时间
     */
    private Date jyjssj;

    /**
     * 收油人
     */
    private String syname;

    /**
     * 化验单编号VBAK-OICNTNTE
     */
    private String oicntnte;

    /**
     * 油库（工厂）
     */
    private String werks;

    /**
     * 机场（装运点）
     */
    private String vstel;

    /**
     * 油罐（库存地点）
     */
    private String lgort;

    /**
     * 物料（油品规格）
     */
    private String matnr;

    /**
     * 航班任务
     */
    private String vsart;

    /**
     * 销售办事处
     */
    private String vkbur;

    /**
     * 销售组
     */
    private String vkgrp;

    /**
     * 组织单位
     */
    private Integer orgeh;

    /**
     * 人事范围
     */
    private String persa;

    /**
     * 人事子范围
     */
    private String btrtl;

    /**
     * 是否交货
     */
    private String zflag;

    /**
     * kafka_erp
     */
    private static final long serialVersionUID = 1L;

    /**
     * 销售组织
     * @return VKORG 销售组织
     */
    public String getVkorg() {
        return vkorg;
    }

    /**
     * 销售组织
     * @param vkorg 销售组织
     */
    public void setVkorg(String vkorg) {
        this.vkorg = vkorg == null ? null : vkorg.trim();
    }

    /**
     * 分销渠道
     * @return VTWEG 分销渠道
     */
    public String getVtweg() {
        return vtweg;
    }

    /**
     * 分销渠道
     * @param vtweg 分销渠道
     */
    public void setVtweg(String vtweg) {
        this.vtweg = vtweg == null ? null : vtweg.trim();
    }

    /**
     * 部门F
     * @return SPART 部门F
     */
    public String getSpart() {
        return spart;
    }

    /**
     * 部门F
     * @param spart 部门F
     */
    public void setSpart(String spart) {
        this.spart = spart == null ? null : spart.trim();
    }

    /**
     * 类型
     * @return ZLX 类型
     */
    public Integer getZlx() {
        return zlx;
    }

    /**
     * 类型
     * @param zlx 类型
     */
    public void setZlx(Integer zlx) {
        this.zlx = zlx;
    }

    /**
     * 加油单编号
     * @return BSTKD 加油单编号
     */
    public String getBstkd() {
        return bstkd;
    }

    /**
     * 加油单编号
     * @param bstkd 加油单编号
     */
    public void setBstkd(String bstkd) {
        this.bstkd = bstkd == null ? null : bstkd.trim();
    }

    /**
     * 加油日期
     * @return JYDATA 加油日期
     */
    public Date getJydata() {
        return jydata;
    }

    /**
     * 加油日期
     * @param jydata 加油日期
     */
    public void setJydata(Date jydata) {
        this.jydata = jydata;
    }

    /**
     * 飞机号码（国内）
     * @return TRMTYP 飞机号码（国内）
     */
    public String getTrmtyp() {
        return trmtyp;
    }

    /**
     * 飞机号码（国内）
     * @param trmtyp 飞机号码（国内）
     */
    public void setTrmtyp(String trmtyp) {
        this.trmtyp = trmtyp == null ? null : trmtyp.trim();
    }

    /**
     * 飞机类型（国内）
     * @return TRATY 飞机类型（国内）
     */
    public String getTraty() {
        return traty;
    }

    /**
     * 飞机类型（国内）
     * @param traty 飞机类型（国内）
     */
    public void setTraty(String traty) {
        this.traty = traty == null ? null : traty.trim();
    }

    /**
     * 飞机单位（客户编号）
     * @return KUNNR 飞机单位（客户编号）
     */
    public String getKunnr() {
        return kunnr;
    }

    /**
     * 飞机单位（客户编号）
     * @param kunnr 飞机单位（客户编号）
     */
    public void setKunnr(String kunnr) {
        this.kunnr = kunnr == null ? null : kunnr.trim();
    }

    /**
     * 航班号
     * @return MFRGR 航班号
     */
    public String getMfrgr() {
        return mfrgr;
    }

    /**
     * 航班号
     * @param mfrgr 航班号
     */
    public void setMfrgr(String mfrgr) {
        this.mfrgr = mfrgr == null ? null : mfrgr.trim();
    }

    /**
     * 评估类型
     * @return BWTAR 评估类型
     */
    public String getBwtar() {
        return bwtar;
    }

    /**
     * 评估类型
     * @param bwtar 评估类型
     */
    public void setBwtar(String bwtar) {
        this.bwtar = bwtar == null ? null : bwtar.trim();
    }

    /**
     * 密度
     * @return SJMD 密度
     */
    public Double getSjmd() {
        return sjmd;
    }

    /**
     * 密度
     * @param sjmd 密度
     */
    public void setSjmd(Double sjmd) {
        this.sjmd = sjmd;
    }

    /**
     * 温度
     * @return BSTZD 温度
     */
    public Double getBstzd() {
        return bstzd;
    }

    /**
     * 温度
     * @param bstzd 温度
     */
    public void setBstzd(Double bstzd) {
        this.bstzd = bstzd;
    }

    /**
     * 体积
     * @return JYTJ 体积
     */
    public Double getJytj() {
        return jytj;
    }

    /**
     * 体积
     * @param jytj 体积
     */
    public void setJytj(Double jytj) {
        this.jytj = jytj;
    }

    /**
     * 质量
     * @return SJZL 质量
     */
    public Double getSjzl() {
        return sjzl;
    }

    /**
     * 质量
     * @param sjzl 质量
     */
    public void setSjzl(Double sjzl) {
        this.sjzl = sjzl;
    }

    /**
     * 加油车编号
     * @return OICNTPER 加油车编号
     */
    public String getOicntper() {
        return oicntper;
    }

    /**
     * 加油车编号
     * @param oicntper 加油车编号
     */
    public void setOicntper(String oicntper) {
        this.oicntper = oicntper == null ? null : oicntper.trim();
    }

    /**
     * 地井编号
     * @return OICNTPHO 地井编号
     */
    public String getOicntpho() {
        return oicntpho;
    }

    /**
     * 地井编号
     * @param oicntpho 地井编号
     */
    public void setOicntpho(String oicntpho) {
        this.oicntpho = oicntpho == null ? null : oicntpho.trim();
    }

    /**
     * 加油员
     * @return PERNR 加油员
     */
    public String getPernr() {
        return pernr;
    }

    /**
     * 加油员
     * @param pernr 加油员
     */
    public void setPernr(String pernr) {
        this.pernr = pernr == null ? null : pernr.trim();
    }

    /**
     * 加油员
     * @return PERNR2 加油员
     */
    public String getPernr2() {
        return pernr2;
    }

    /**
     * 加油员
     * @param pernr2 加油员
     */
    public void setPernr2(String pernr2) {
        this.pernr2 = pernr2 == null ? null : pernr2.trim();
    }

    /**
     * 加油开始时间
     * @return JYKSSJ 加油开始时间
     */
    public Date getJykssj() {
        return jykssj;
    }

    /**
     * 加油开始时间
     * @param jykssj 加油开始时间
     */
    public void setJykssj(Date jykssj) {
        this.jykssj = jykssj;
    }

    /**
     * 加油结束时间
     * @return JYJSSJ 加油结束时间
     */
    public Date getJyjssj() {
        return jyjssj;
    }

    /**
     * 加油结束时间
     * @param jyjssj 加油结束时间
     */
    public void setJyjssj(Date jyjssj) {
        this.jyjssj = jyjssj;
    }

    /**
     * 收油人
     * @return SYNAME 收油人
     */
    public String getSyname() {
        return syname;
    }

    /**
     * 收油人
     * @param syname 收油人
     */
    public void setSyname(String syname) {
        this.syname = syname == null ? null : syname.trim();
    }

    /**
     * 化验单编号VBAK-OICNTNTE
     * @return OICNTNTE 化验单编号VBAK-OICNTNTE
     */
    public String getOicntnte() {
        return oicntnte;
    }

    /**
     * 化验单编号VBAK-OICNTNTE
     * @param oicntnte 化验单编号VBAK-OICNTNTE
     */
    public void setOicntnte(String oicntnte) {
        this.oicntnte = oicntnte == null ? null : oicntnte.trim();
    }

    /**
     * 油库（工厂）
     * @return WERKS 油库（工厂）
     */
    public String getWerks() {
        return werks;
    }

    /**
     * 油库（工厂）
     * @param werks 油库（工厂）
     */
    public void setWerks(String werks) {
        this.werks = werks == null ? null : werks.trim();
    }

    /**
     * 机场（装运点）
     * @return VSTEL 机场（装运点）
     */
    public String getVstel() {
        return vstel;
    }

    /**
     * 机场（装运点）
     * @param vstel 机场（装运点）
     */
    public void setVstel(String vstel) {
        this.vstel = vstel == null ? null : vstel.trim();
    }

    /**
     * 油罐（库存地点）
     * @return LGORT 油罐（库存地点）
     */
    public String getLgort() {
        return lgort;
    }

    /**
     * 油罐（库存地点）
     * @param lgort 油罐（库存地点）
     */
    public void setLgort(String lgort) {
        this.lgort = lgort == null ? null : lgort.trim();
    }

    /**
     * 物料（油品规格）
     * @return MATNR 物料（油品规格）
     */
    public String getMatnr() {
        return matnr;
    }

    /**
     * 物料（油品规格）
     * @param matnr 物料（油品规格）
     */
    public void setMatnr(String matnr) {
        this.matnr = matnr == null ? null : matnr.trim();
    }

    /**
     * 航班任务
     * @return VSART 航班任务
     */
    public String getVsart() {
        return vsart;
    }

    /**
     * 航班任务
     * @param vsart 航班任务
     */
    public void setVsart(String vsart) {
        this.vsart = vsart == null ? null : vsart.trim();
    }

    /**
     * 销售办事处
     * @return VKBUR 销售办事处
     */
    public String getVkbur() {
        return vkbur;
    }

    /**
     * 销售办事处
     * @param vkbur 销售办事处
     */
    public void setVkbur(String vkbur) {
        this.vkbur = vkbur == null ? null : vkbur.trim();
    }

    /**
     * 销售组
     * @return VKGRP 销售组
     */
    public String getVkgrp() {
        return vkgrp;
    }

    /**
     * 销售组
     * @param vkgrp 销售组
     */
    public void setVkgrp(String vkgrp) {
        this.vkgrp = vkgrp == null ? null : vkgrp.trim();
    }

    /**
     * 组织单位
     * @return ORGEH 组织单位
     */
    public Integer getOrgeh() {
        return orgeh;
    }

    /**
     * 组织单位
     * @param orgeh 组织单位
     */
    public void setOrgeh(Integer orgeh) {
        this.orgeh = orgeh;
    }

    /**
     * 人事范围
     * @return PERSA 人事范围
     */
    public String getPersa() {
        return persa;
    }

    /**
     * 人事范围
     * @param persa 人事范围
     */
    public void setPersa(String persa) {
        this.persa = persa == null ? null : persa.trim();
    }

    /**
     * 人事子范围
     * @return BTRTL 人事子范围
     */
    public String getBtrtl() {
        return btrtl;
    }

    /**
     * 人事子范围
     * @param btrtl 人事子范围
     */
    public void setBtrtl(String btrtl) {
        this.btrtl = btrtl == null ? null : btrtl.trim();
    }

    /**
     * 是否交货
     * @return ZFLAG 是否交货
     */
    public String getZflag() {
        return zflag;
    }

    /**
     * 是否交货
     * @param zflag 是否交货
     */
    public void setZflag(String zflag) {
        this.zflag = zflag == null ? null : zflag.trim();
    }
}