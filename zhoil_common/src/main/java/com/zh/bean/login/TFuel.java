package com.zh.bean.login;

import java.io.Serializable;
import java.util.Date;

/**
 * T_FUEL（提供给地图）
 */
public class TFuel implements Serializable {
    /**
     * 化验单编号
     */
    private String fuelTestBillNo;

    /**
     * 化验单日期
     */
    private Date fuelDate;

    /**
     * 油品名称
     */
    private String fuelName;

    /**
     * 温度（摄氏度）
     */
    private Double fuelTemp;

    /**
     * 密度（克/毫升（g/cm³））
     */
    private Double fuelDnst;

    /**
     * 所属机场代码
     */
    private String fuelAirportCode;

    /**
     * T_FUEL
     */
    private static final long serialVersionUID = 1L;

    /**
     * 化验单编号
     * @return fuel_test_bill_no 化验单编号
     */
    public String getFuelTestBillNo() {
        return fuelTestBillNo;
    }

    /**
     * 化验单编号
     * @param fuelTestBillNo 化验单编号
     */
    public void setFuelTestBillNo(String fuelTestBillNo) {
        this.fuelTestBillNo = fuelTestBillNo == null ? null : fuelTestBillNo.trim();
    }

    /**
     * 化验单日期
     * @return fuel_date 化验单日期
     */
    public Date getFuelDate() {
        return fuelDate;
    }

    /**
     * 化验单日期
     * @param fuelDate 化验单日期
     */
    public void setFuelDate(Date fuelDate) {
        this.fuelDate = fuelDate;
    }

    /**
     * 油品名称
     * @return fuel_name 油品名称
     */
    public String getFuelName() {
        return fuelName;
    }

    /**
     * 油品名称
     * @param fuelName 油品名称
     */
    public void setFuelName(String fuelName) {
        this.fuelName = fuelName == null ? null : fuelName.trim();
    }

    /**
     * 温度（摄氏度）
     * @return fuel_temp 温度（摄氏度）
     */
    public Double getFuelTemp() {
        return fuelTemp;
    }

    /**
     * 温度（摄氏度）
     * @param fuelTemp 温度（摄氏度）
     */
    public void setFuelTemp(Double fuelTemp) {
        this.fuelTemp = fuelTemp;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @return fuel_dnst 密度（克/毫升（g/cm³））
     */
    public Double getFuelDnst() {
        return fuelDnst;
    }

    /**
     * 密度（克/毫升（g/cm³））
     * @param fuelDnst 密度（克/毫升（g/cm³））
     */
    public void setFuelDnst(Double fuelDnst) {
        this.fuelDnst = fuelDnst;
    }

    /**
     * 所属机场代码
     * @return fuel_airport_code 所属机场代码
     */
    public String getFuelAirportCode() {
        return fuelAirportCode;
    }

    /**
     * 所属机场代码
     * @param fuelAirportCode 所属机场代码
     */
    public void setFuelAirportCode(String fuelAirportCode) {
        this.fuelAirportCode = fuelAirportCode == null ? null : fuelAirportCode.trim();
    }
}