package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 * 机位信息接口（提供给地图）
 */
public class TFlightPlace implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 飞机类型
	 */
	private String flgtAcname;
	/**
	 * 航班号
	 */
	private String flgtFlno;
	
	/**
	 * 航空公司二字码
	 */
	private String flgtAl2c;

	/**
	 * 航空公司
	 */
	private String flgtAlcname;
	
    /**
     * 计划到达时间
     */
    private Date flgtAStot;

    /**
     * 预计到达时间
     */
    private Date flgtAEtot;

    /**
     * 实际到达时间（或称落地时间）
     */
    private Date flgtAAtot;

    /**
     * 计划起飞时间
     */
    private Date flgtDStot;

    /**
     * 预计起飞时间
     */
    private Date flgtDEtot;

    /**
     * 实际起飞时间（或称离地时间）
     */
    private Date flgtDAtot;
	
	/**
	 * 进离港（A：进港，D：出港）
	 */
	private String flgtAdid;
	/**
	 * 航班国内/国际（D：国内，I：国际，R：地区，M：混合，U：未知）
	 */
	private String flgtFlti;
	/**
	 * 所属机场代码
	 */
	private String flgtAirportCode;
	/**
	 * 机位号
	 */
	private String flgtPlacecode;
	/**
	 * @return the flgtAcname
	 */
	public String getFlgtAcname() {
		return flgtAcname;
	}
	/**
	 * @param flgtAcname the flgtAcname to set
	 */
	public void setFlgtAcname(String flgtAcname) {
		this.flgtAcname = flgtAcname;
	}
	/**
	 * @return the flgtFlno
	 */
	public String getFlgtFlno() {
		return flgtFlno;
	}
	/**
	 * @param flgtFlno the flgtFlno to set
	 */
	public void setFlgtFlno(String flgtFlno) {
		this.flgtFlno = flgtFlno;
	}
	/**
	 * @return the flgtAl2c
	 */
	public String getFlgtAl2c() {
		return flgtAl2c;
	}
	/**
	 * @param flgtAl2c the flgtAl2c to set
	 */
	public void setFlgtAl2c(String flgtAl2c) {
		this.flgtAl2c = flgtAl2c;
	}
	/**
	 * @return the flgtAlcname
	 */
	public String getFlgtAlcname() {
		return flgtAlcname;
	}
	/**
	 * @param flgtAlcname the flgtAlcname to set
	 */
	public void setFlgtAlcname(String flgtAlcname) {
		this.flgtAlcname = flgtAlcname;
	}
	/**
	 * @return the flgtAStot
	 */
	public Date getFlgtAStot() {
		return flgtAStot;
	}
	/**
	 * @param flgtAStot the flgtAStot to set
	 */
	public void setFlgtAStot(Date flgtAStot) {
		this.flgtAStot = flgtAStot;
	}
	/**
	 * @return the flgtDStot
	 */
	public Date getFlgtDStot() {
		return flgtDStot;
	}
	/**
	 * @param flgtDStot the flgtDStot to set
	 */
	public void setFlgtDStot(Date flgtDStot) {
		this.flgtDStot = flgtDStot;
	}
	/**
	 * @return the flgtAdid
	 */
	public String getFlgtAdid() {
		return flgtAdid;
	}
	/**
	 * @param flgtAdid the flgtAdid to set
	 */
	public void setFlgtAdid(String flgtAdid) {
		this.flgtAdid = flgtAdid;
	}
	/**
	 * @return the flgtFlti
	 */
	public String getFlgtFlti() {
		return flgtFlti;
	}
	/**
	 * @param flgtFlti the flgtFlti to set
	 */
	public void setFlgtFlti(String flgtFlti) {
		this.flgtFlti = flgtFlti;
	}
	/**
	 * @return the flgtAirportCode
	 */
	public String getFlgtAirportCode() {
		return flgtAirportCode;
	}
	/**
	 * @param flgtAirportCode the flgtAirportCode to set
	 */
	public void setFlgtAirportCode(String flgtAirportCode) {
		this.flgtAirportCode = flgtAirportCode;
	}
	/**
	 * @return the flgtPlacecode
	 */
	public String getFlgtPlacecode() {
		return flgtPlacecode;
	}
	/**
	 * @param flgtPlacecode the flgtPlacecode to set
	 */
	public void setFlgtPlacecode(String flgtPlacecode) {
		this.flgtPlacecode = flgtPlacecode;
	}
	/**
	 * @return the flgtAEtot
	 */
	public Date getFlgtAEtot() {
		return flgtAEtot;
	}
	/**
	 * @param flgtAEtot the flgtAEtot to set
	 */
	public void setFlgtAEtot(Date flgtAEtot) {
		this.flgtAEtot = flgtAEtot;
	}
	/**
	 * @return the flgtAAtot
	 */
	public Date getFlgtAAtot() {
		return flgtAAtot;
	}
	/**
	 * @param flgtAAtot the flgtAAtot to set
	 */
	public void setFlgtAAtot(Date flgtAAtot) {
		this.flgtAAtot = flgtAAtot;
	}
	/**
	 * @return the flgtDEtot
	 */
	public Date getFlgtDEtot() {
		return flgtDEtot;
	}
	/**
	 * @param flgtDEtot the flgtDEtot to set
	 */
	public void setFlgtDEtot(Date flgtDEtot) {
		this.flgtDEtot = flgtDEtot;
	}
	/**
	 * @return the flgtDAtot
	 */
	public Date getFlgtDAtot() {
		return flgtDAtot;
	}
	/**
	 * @param flgtDAtot the flgtDAtot to set
	 */
	public void setFlgtDAtot(Date flgtDAtot) {
		this.flgtDAtot = flgtDAtot;
	}
	
}
