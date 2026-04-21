package com.higer.statistical.entity.TFuelRecpt;
import lombok.Data;
/**
 * 导出油单实体类
 */
@Data
public class ExportTFuel {
    private String vkorg;//销售组织
    private String vtweg;//分销渠道
    private String spart;//部门
    private String zlx;//类型
    private String bstkd;//加油单编号
    private String jydata;//加油日期
    private String trmtyp;//飞机号码（国内）
    private String traty;//飞机类型（国内）
    private String kunnr;//飞机单位（客户编号）
    private String mfrgr;//航班号
    private String bwtar;//评估类型
    private String sjmd;//密度
    private String bstzd;//温度
    private String jytj;//体积
    private String sjzl;//质量
    private String oicntper;//加油车编号
    private String oicntpho;//地井编号
    private String pernr;//加油员
    private String pernr2;//加油员
    private String jykssj;//加油开始时间
    private String jyjssj;//加油结束时间
    private String syname;//收油人
    private String oicntnte;//化验单编号
    private String werks;//油库（工厂）
    private String vstel;//机场（装运点）
    private String lgort;//油罐（库存地点）
    private String matnr;//物料（油品规格）
//    private String vsart;//航班任务
    private String vkbur;//销售办事处
    private String vkgrp;//销售组
    private String orgeh;//组织单位
    private String persa;//人事范围
    private String btrtl;//人事子范围

    /*
    private String zflag;//是否操作;
    private String mandt;//客户端
    private String uname;//ERP用户名
    private String creator;
    private String auditor;
    private String flag;*/
}
