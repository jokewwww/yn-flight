package com.zh.bean.login;

import java.io.Serializable;

/**
 * 车辆表
 * T_VEHI
 */
public class MyVehi implements Serializable {
    /**
     * 车辆ID（uuid）
     */
    private String vehiId;

    /**
     * 车辆编号
     */
    private String vehiNo;

    /**
     * 车辆别名
     */
    private String vehiNickname;

    /**
     * 车型
     */
    private String vehiType;

    /**
     * 品牌
     */
    private String vehiBrand;

    /**
     * 是否可用（0：不可用，1：空闲，2：使用中）
     */
    private Integer vehiAvailability;

    /**
     * 剩余油量
     */
    private Integer vehiFuelFigu;

    /**
     * 子公司
     */
    private String vehiCompany;

    /**
     * 航空加油站
     */
    private String vehiServStation;

    /**
     * 所属机场代码
     */
    private String vehiAirportCode;

    /**
     * 所属机场区域代码
     */
    private String vehiAptareaCode;

    /**
     * 车牌号
     */
    private String vehiPlateNo;
    
    /**
     * 车高
     */
    private Double vehiHeight;
    
    /**
     * 车辆油单代码（00~49 A0~ZZ）
     */
    private String vehiFuelCode;
    
    /**
     * 车辆油单代码（00~49 A0~ZZ）
     */
    public String getVehiFuelCode() {
		return vehiFuelCode;
	}
    /**
     * 车辆油单代码（00~49 A0~ZZ）
     */
	public void setVehiFuelCode(String vehiFuelCode) {
		this.vehiFuelCode = vehiFuelCode;
	}

	/**
     * 车辆油单序号（00000~99999）
     */
    private String vehiFuelSno;

    private String remark;

    /**
     * T_VEHI
     */
    private static final long serialVersionUID = 1L;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 车高
     */
	public Double getVehiHeight() {
		return vehiHeight;
	}

	/**
     * 车高
     */
	public void setVehiHeight(Double vehiHeight) {
		this.vehiHeight = vehiHeight;
	}

	/**
     * 车辆ID（uuid）
     * @return vehi_id 车辆ID（uuid）
     */
    public String getVehiId() {
        return vehiId;
    }

    /**
     * 车辆ID（uuid）
     * @param vehiId 车辆ID（uuid）
     */
    public void setVehiId(String vehiId) {
        this.vehiId = vehiId == null ? null : vehiId.trim();
    }

    /**
     * 车辆编号
     * @return vehi_no 车辆编号
     */
    public String getVehiNo() {
        return vehiNo;
    }

    /**
     * 车辆编号
     * @param vehiNo 车辆编号
     */
    public void setVehiNo(String vehiNo) {
        this.vehiNo = vehiNo == null ? null : vehiNo.trim();
    }

    /**
     * 车辆别名
     * @return vehi_nickname 车辆别名
     */
    public String getVehiNickname() {
        return vehiNickname;
    }

    /**
     * 车辆别名
     * @param vehiNickname 车辆别名
     */
    public void setVehiNickname(String vehiNickname) {
        this.vehiNickname = vehiNickname == null ? null : vehiNickname.trim();
    }

    /**
     * 车型
     * @return vehi_type 车型
     */
    public String getVehiType() {
        return vehiType;
    }

    /**
     * 车型
     * @param vehiType 车型
     */
    public void setVehiType(String vehiType) {
        this.vehiType = vehiType == null ? null : vehiType.trim();
    }

    /**
     * 品牌
     * @return vehi_brand 品牌
     */
    public String getVehiBrand() {
        return vehiBrand;
    }

    /**
     * 品牌
     * @param vehiBrand 品牌
     */
    public void setVehiBrand(String vehiBrand) {
        this.vehiBrand = vehiBrand == null ? null : vehiBrand.trim();
    }

    /**
     * 是否可用（0：不可用，1：空闲，2：使用中）
     * @return vehi_availability 是否可用（0：不可用，1：空闲，2：使用中）
     */
    public Integer getVehiAvailability() {
        return vehiAvailability;
    }

    /**
     * 是否可用（0：不可用，1：空闲，2：使用中）
     * @param vehiAvailability 是否可用（0：不可用，1：空闲，2：使用中）
     */
    public void setVehiAvailability(Integer vehiAvailability) {
        this.vehiAvailability = vehiAvailability;
    }

    /**
     * 剩余油量
     * @return vehi_fuel_figu 剩余油量
     */
    public Integer getVehiFuelFigu() {
        return vehiFuelFigu;
    }

    /**
     * 剩余油量
     * @param vehiFuelFigu 剩余油量
     */
    public void setVehiFuelFigu(Integer vehiFuelFigu) {
        this.vehiFuelFigu = vehiFuelFigu;
    }

    /**
     * 子公司
     * @return vehi_company 子公司
     */
    public String getVehiCompany() {
        return vehiCompany;
    }

    /**
     * 子公司
     * @param vehiCompany 子公司
     */
    public void setVehiCompany(String vehiCompany) {
        this.vehiCompany = vehiCompany == null ? null : vehiCompany.trim();
    }

    /**
     * 航空加油站
     * @return vehi_serv_station 航空加油站
     */
    public String getVehiServStation() {
        return vehiServStation;
    }

    /**
     * 航空加油站
     * @param vehiServStation 航空加油站
     */
    public void setVehiServStation(String vehiServStation) {
        this.vehiServStation = vehiServStation == null ? null : vehiServStation.trim();
    }

    /**
     * 所属机场代码
     * @return vehi_airport_code 所属机场代码
     */
    public String getVehiAirportCode() {
        return vehiAirportCode;
    }

    /**
     * 所属机场代码
     * @param vehiAirportCode 所属机场代码
     */
    public void setVehiAirportCode(String vehiAirportCode) {
        this.vehiAirportCode = vehiAirportCode == null ? null : vehiAirportCode.trim();
    }

    /**
     * 所属机场区域代码
     * @return vehi_aptarea_code 所属机场区域代码
     */
    public String getVehiAptareaCode() {
        return vehiAptareaCode;
    }

    /**
     * 所属机场区域代码
     * @param vehiAptareaCode 所属机场区域代码
     */
    public void setVehiAptareaCode(String vehiAptareaCode) {
        this.vehiAptareaCode = vehiAptareaCode == null ? null : vehiAptareaCode.trim();
    }

    /**
     * 车牌号
     * @return vehi_plate_no 车牌号
     */
    public String getVehiPlateNo() {
        return vehiPlateNo;
    }

    /**
     * 车牌号
     * @param vehiPlateNo 车牌号
     */
    public void setVehiPlateNo(String vehiPlateNo) {
        this.vehiPlateNo = vehiPlateNo == null ? null : vehiPlateNo.trim();
    }
    
    /**
     * 车辆油单序号（00000~99999）
     * @return vehi_fuel_sno 车辆油单序号（00000~99999）
     */
    public String getVehiFuelSno() {
        return vehiFuelSno;
    }

    /**
     * 车辆油单序号（00000~99999）
     * @param vehiFuelSno 车辆油单序号（00000~99999）
     */
    public void setVehiFuelSno(String vehiFuelSno) {
        this.vehiFuelSno = vehiFuelSno == null ? null : vehiFuelSno.trim();
    }
}