package com.zh.bean.login;

/**
 * @classname: PadInfo
 * @author: zhaojiacan
 * @description: Pad信息
 * @date: 2024/4/23 17:43
 * @version:1.0
 */

import lombok.Data;

@Data
public class PadInfo extends BaseBean {

    /**
     * 安卓系统版本
     */
    private String androidSysVersion;
    /**
     * app版本号
     */
    private Integer appVersionCode;
    /**
     * app版本名称
     */
    private String appVersionName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * padId
     */
    private String padId;
    /**
     * mac地址
     */
    private String mac;

}
