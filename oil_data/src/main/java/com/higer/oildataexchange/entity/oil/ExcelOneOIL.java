package com.higer.oildataexchange.entity.oil;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "excel_oil1")
public class ExcelOneOIL implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "xh")
    private String xh; // '序号'
    @Column(name = "gsmc")
    private String gsmc; // '公司名称'
    @Column(name = "jcmc")
    private String jcmc; // '机场名称'
    @Column(name = "date")
    private String date; // '日期'
    @Column(name = "jylsheng")
    private String jylsheng; // '加油量升'
    @Column(name = "jylgongjin")
    private String jylgongjin; // '加油量斤'
    @Id
    @Column(name = "ydhm")
    private String ydhm; // '油单号码'
    @Column(name = "fjhm")
    private String fjhm; // '飞机号码'
    @Column(name = "fjlx")
    private String fjlx; // '飞机类型'
    @Column(name = "hkgs")
    private String hkgs; // '航空公司'
    @Column(name = "begin_date")
    private String beginDate; // '开始时间'
    @Column(name = "end_date")
    private String endDate; // '结束时间'
    @Column(name = "jyy")
    private String jyy; // '加油员'
    @Column(name = "hbh")
    private String hbh; // '航班号'
    @Column(name = "hx")
    private String hx; // '航线'
    @Column(name = "ypwd")
    private String ypwd; // '油品温度'
    @Column(name = "ypmd")
    private String ypmd; // '油品密度'
    @Column(name = "djbh")
    private String djbh; // '地井编号'
    @Column(name = "ycbh")
    private String ycbh; // '油车编号'
    @Column(name = "xsdd")
    private String xsdd; // '销售订单'
    @Column(name = "upload_status")
    private String uploadStatus; //'0待上传 1上传失败 2上传成功
    @Column(name = "upload_fail_reason")
    private String uploadFailReason;
    @Column(name = "fail_xml")
    private String failXml;
    @Column(name = "jcszm")
    private String jcszm;
    @Column(name = "sfszm")
    private String sfszm;
    @Column(name = "jtszm")
    private String jtszm;
    @Column(name = "mdszm")
    private String mdszm;
    @Column(name = "gongsi")
    private String gongsi;
    @Column(name = "bslx")
    private String bslx;
    @Column(name = "ydlx")
    private String ydlx;
    @Column(name = "hkgsdm")
    private String hkgsdm;
}