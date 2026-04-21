package com.higer.read_kafka.entity.flight;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:31
 * @Description:
 */
@Entity
@Table(name = "T_FUEL_RECPT")
@NamedQuery(name = "TFuelRecpt.findAll", query = "SELECT a FROM TFuelRecpt a")
public class TFuelRecpt implements Serializable {
    private String flrcId;
    private int flrcType;
    private String flrcNo;
    private Date flrcDate;
    private String flrcAirportCode;
    private String flrcAptareaCode;
    private String flrcAirport;
    private String flrcAirlCode;
    private String flrcAirlName;
    private String flrcFlightNo;
    private String flrcAircrftNo;
    private String flrcAircrftType;
    private String flrcDeparture;
    private String flrcTransit;
    private String flrcDest;
    private String flrcTestBillNo;
    private String flrcFuelName;
    private Double flrcFuelTemp;
    private Double flrcFuelDnst;
    private Double flrcFuelVol;
    private Integer flrcMeterStat;
    private Integer flrcMeterFnsh;
    private int flrcFiguars;
    private String flrcFiguarsWord;
    private Double flrcQuantity;
    private String flrcVehiNo;
    private String flrcHydrtPitNo;
    private Timestamp flrcStatTime;
    private Timestamp flrcFnshTime;
    private String flrcDeliverName;
    private String flrcSign;
    private Integer flrcManualTag;
    private String flrcManualStaff;
    private String flrcManualTime;
    private Integer flrcRevwStatus;
    private Integer flrcTakebackFlg;
    private Timestamp flrcRecCreTime;

    @Id
    @Column(name = "flrc_id")
    public String getFlrcId() {
        return flrcId;
    }

    public void setFlrcId(String flrcId) {
        this.flrcId = flrcId;
    }

    @Basic
    @Column(name = "flrc_type")
    public int getFlrcType() {
        return flrcType;
    }

    public void setFlrcType(int flrcType) {
        this.flrcType = flrcType;
    }

    @Basic
    @Column(name = "flrc_no")
    public String getFlrcNo() {
        return flrcNo;
    }

    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo;
    }

    @Basic
    @Column(name = "flrc_date")
    public Date getFlrcDate() {
        return flrcDate;
    }

    public void setFlrcDate(Date flrcDate) {
        this.flrcDate = flrcDate;
    }

    @Basic
    @Column(name = "flrc_airport_code")
    public String getFlrcAirportCode() {
        return flrcAirportCode;
    }

    public void setFlrcAirportCode(String flrcAirportCode) {
        this.flrcAirportCode = flrcAirportCode;
    }

    @Basic
    @Column(name = "flrc_aptarea_code")
    public String getFlrcAptareaCode() {
        return flrcAptareaCode;
    }

    public void setFlrcAptareaCode(String flrcAptareaCode) {
        this.flrcAptareaCode = flrcAptareaCode;
    }

    @Basic
    @Column(name = "flrc_airport")
    public String getFlrcAirport() {
        return flrcAirport;
    }

    public void setFlrcAirport(String flrcAirport) {
        this.flrcAirport = flrcAirport;
    }

    @Basic
    @Column(name = "flrc_airl_code")
    public String getFlrcAirlCode() {
        return flrcAirlCode;
    }

    public void setFlrcAirlCode(String flrcAirlCode) {
        this.flrcAirlCode = flrcAirlCode;
    }

    @Basic
    @Column(name = "flrc_airl_name")
    public String getFlrcAirlName() {
        return flrcAirlName;
    }

    public void setFlrcAirlName(String flrcAirlName) {
        this.flrcAirlName = flrcAirlName;
    }

    @Basic
    @Column(name = "flrc_flight_no")
    public String getFlrcFlightNo() {
        return flrcFlightNo;
    }

    public void setFlrcFlightNo(String flrcFlightNo) {
        this.flrcFlightNo = flrcFlightNo;
    }

    @Basic
    @Column(name = "flrc_aircrft_no")
    public String getFlrcAircrftNo() {
        return flrcAircrftNo;
    }

    public void setFlrcAircrftNo(String flrcAircrftNo) {
        this.flrcAircrftNo = flrcAircrftNo;
    }

    @Basic
    @Column(name = "flrc_aircrft_type")
    public String getFlrcAircrftType() {
        return flrcAircrftType;
    }

    public void setFlrcAircrftType(String flrcAircrftType) {
        this.flrcAircrftType = flrcAircrftType;
    }

    @Basic
    @Column(name = "flrc_departure")
    public String getFlrcDeparture() {
        return flrcDeparture;
    }

    public void setFlrcDeparture(String flrcDeparture) {
        this.flrcDeparture = flrcDeparture;
    }

    @Basic
    @Column(name = "flrc_transit")
    public String getFlrcTransit() {
        return flrcTransit;
    }

    public void setFlrcTransit(String flrcTransit) {
        this.flrcTransit = flrcTransit;
    }

    @Basic
    @Column(name = "flrc_dest")
    public String getFlrcDest() {
        return flrcDest;
    }

    public void setFlrcDest(String flrcDest) {
        this.flrcDest = flrcDest;
    }

    @Basic
    @Column(name = "flrc_test_bill_no")
    public String getFlrcTestBillNo() {
        return flrcTestBillNo;
    }

    public void setFlrcTestBillNo(String flrcTestBillNo) {
        this.flrcTestBillNo = flrcTestBillNo;
    }

    @Basic
    @Column(name = "flrc_fuel_name")
    public String getFlrcFuelName() {
        return flrcFuelName;
    }

    public void setFlrcFuelName(String flrcFuelName) {
        this.flrcFuelName = flrcFuelName;
    }

    @Basic
    @Column(name = "flrc_fuel_temp")
    public Double getFlrcFuelTemp() {
        return flrcFuelTemp;
    }

    public void setFlrcFuelTemp(Double flrcFuelTemp) {
        this.flrcFuelTemp = flrcFuelTemp;
    }

    @Basic
    @Column(name = "flrc_fuel_dnst")
    public Double getFlrcFuelDnst() {
        return flrcFuelDnst;
    }

    public void setFlrcFuelDnst(Double flrcFuelDnst) {
        this.flrcFuelDnst = flrcFuelDnst;
    }

    @Basic
    @Column(name = "flrc_fuel_vol")
    public Double getFlrcFuelVol() {
        return flrcFuelVol;
    }

    public void setFlrcFuelVol(Double flrcFuelVol) {
        this.flrcFuelVol = flrcFuelVol;
    }

    @Basic
    @Column(name = "flrc_meter_stat")
    public Integer getFlrcMeterStat() {
        return flrcMeterStat;
    }

    public void setFlrcMeterStat(Integer flrcMeterStat) {
        this.flrcMeterStat = flrcMeterStat;
    }

    @Basic
    @Column(name = "flrc_meter_fnsh")
    public Integer getFlrcMeterFnsh() {
        return flrcMeterFnsh;
    }

    public void setFlrcMeterFnsh(Integer flrcMeterFnsh) {
        this.flrcMeterFnsh = flrcMeterFnsh;
    }

    @Basic
    @Column(name = "flrc_figuars")
    public int getFlrcFiguars() {
        return flrcFiguars;
    }

    public void setFlrcFiguars(int flrcFiguars) {
        this.flrcFiguars = flrcFiguars;
    }

    @Basic
    @Column(name = "flrc_figuars_word")
    public String getFlrcFiguarsWord() {
        return flrcFiguarsWord;
    }

    public void setFlrcFiguarsWord(String flrcFiguarsWord) {
        this.flrcFiguarsWord = flrcFiguarsWord;
    }

    @Basic
    @Column(name = "flrc_quantity")
    public Double getFlrcQuantity() {
        return flrcQuantity;
    }

    public void setFlrcQuantity(Double flrcQuantity) {
        this.flrcQuantity = flrcQuantity;
    }

    @Basic
    @Column(name = "flrc_vehi_no")
    public String getFlrcVehiNo() {
        return flrcVehiNo;
    }

    public void setFlrcVehiNo(String flrcVehiNo) {
        this.flrcVehiNo = flrcVehiNo;
    }

    @Basic
    @Column(name = "flrc_hydrt_pit_no")
    public String getFlrcHydrtPitNo() {
        return flrcHydrtPitNo;
    }

    public void setFlrcHydrtPitNo(String flrcHydrtPitNo) {
        this.flrcHydrtPitNo = flrcHydrtPitNo;
    }

    @Basic
    @Column(name = "flrc_stat_time")
    public Timestamp getFlrcStatTime() {
        return flrcStatTime;
    }

    public void setFlrcStatTime(Timestamp flrcStatTime) {
        this.flrcStatTime = flrcStatTime;
    }

    @Basic
    @Column(name = "flrc_fnsh_time")
    public Timestamp getFlrcFnshTime() {
        return flrcFnshTime;
    }

    public void setFlrcFnshTime(Timestamp flrcFnshTime) {
        this.flrcFnshTime = flrcFnshTime;
    }

    @Basic
    @Column(name = "flrc_deliver_name")
    public String getFlrcDeliverName() {
        return flrcDeliverName;
    }

    public void setFlrcDeliverName(String flrcDeliverName) {
        this.flrcDeliverName = flrcDeliverName;
    }

    @Basic
    @Column(name = "flrc_sign")
    public String getFlrcSign() {
        return flrcSign;
    }

    public void setFlrcSign(String flrcSign) {
        this.flrcSign = flrcSign;
    }

    @Basic
    @Column(name = "flrc_manual_tag")
    public Integer getFlrcManualTag() {
        return flrcManualTag;
    }

    public void setFlrcManualTag(Integer flrcManualTag) {
        this.flrcManualTag = flrcManualTag;
    }

    @Basic
    @Column(name = "flrc_manual_staff")
    public String getFlrcManualStaff() {
        return flrcManualStaff;
    }

    public void setFlrcManualStaff(String flrcManualStaff) {
        this.flrcManualStaff = flrcManualStaff;
    }

    @Basic
    @Column(name = "flrc_manual_time")
    public String getFlrcManualTime() {
        return flrcManualTime;
    }

    public void setFlrcManualTime(String flrcManualTime) {
        this.flrcManualTime = flrcManualTime;
    }

    @Basic
    @Column(name = "flrc_revw_status")
    public Integer getFlrcRevwStatus() {
        return flrcRevwStatus;
    }

    public void setFlrcRevwStatus(Integer flrcRevwStatus) {
        this.flrcRevwStatus = flrcRevwStatus;
    }

    @Basic
    @Column(name = "flrc_takeback_flg")
    public Integer getFlrcTakebackFlg() {
        return flrcTakebackFlg;
    }

    public void setFlrcTakebackFlg(Integer flrcTakebackFlg) {
        this.flrcTakebackFlg = flrcTakebackFlg;
    }

    @Basic
    @Column(name = "flrc_rec_cre_time")
    public Timestamp getFlrcRecCreTime() {
        return flrcRecCreTime;
    }

    public void setFlrcRecCreTime(Timestamp flrcRecCreTime) {
        this.flrcRecCreTime = flrcRecCreTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFuelRecpt that = (TFuelRecpt) o;
        return flrcType == that.flrcType &&
                flrcFiguars == that.flrcFiguars &&
                Objects.equals(flrcId, that.flrcId) &&
                Objects.equals(flrcNo, that.flrcNo) &&
                Objects.equals(flrcDate, that.flrcDate) &&
                Objects.equals(flrcAirportCode, that.flrcAirportCode) &&
                Objects.equals(flrcAptareaCode, that.flrcAptareaCode) &&
                Objects.equals(flrcAirport, that.flrcAirport) &&
                Objects.equals(flrcAirlCode, that.flrcAirlCode) &&
                Objects.equals(flrcAirlName, that.flrcAirlName) &&
                Objects.equals(flrcFlightNo, that.flrcFlightNo) &&
                Objects.equals(flrcAircrftNo, that.flrcAircrftNo) &&
                Objects.equals(flrcAircrftType, that.flrcAircrftType) &&
                Objects.equals(flrcDeparture, that.flrcDeparture) &&
                Objects.equals(flrcTransit, that.flrcTransit) &&
                Objects.equals(flrcDest, that.flrcDest) &&
                Objects.equals(flrcTestBillNo, that.flrcTestBillNo) &&
                Objects.equals(flrcFuelName, that.flrcFuelName) &&
                Objects.equals(flrcFuelTemp, that.flrcFuelTemp) &&
                Objects.equals(flrcFuelDnst, that.flrcFuelDnst) &&
                Objects.equals(flrcFuelVol, that.flrcFuelVol) &&
                Objects.equals(flrcMeterStat, that.flrcMeterStat) &&
                Objects.equals(flrcMeterFnsh, that.flrcMeterFnsh) &&
                Objects.equals(flrcFiguarsWord, that.flrcFiguarsWord) &&
                Objects.equals(flrcQuantity, that.flrcQuantity) &&
                Objects.equals(flrcVehiNo, that.flrcVehiNo) &&
                Objects.equals(flrcHydrtPitNo, that.flrcHydrtPitNo) &&
                Objects.equals(flrcStatTime, that.flrcStatTime) &&
                Objects.equals(flrcFnshTime, that.flrcFnshTime) &&
                Objects.equals(flrcDeliverName, that.flrcDeliverName) &&
                Objects.equals(flrcSign, that.flrcSign) &&
                Objects.equals(flrcManualTag, that.flrcManualTag) &&
                Objects.equals(flrcManualStaff, that.flrcManualStaff) &&
                Objects.equals(flrcManualTime, that.flrcManualTime) &&
                Objects.equals(flrcRevwStatus, that.flrcRevwStatus) &&
                Objects.equals(flrcTakebackFlg, that.flrcTakebackFlg) &&
                Objects.equals(flrcRecCreTime, that.flrcRecCreTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(flrcId, flrcType, flrcNo, flrcDate, flrcAirportCode, flrcAptareaCode, flrcAirport, flrcAirlCode, flrcAirlName, flrcFlightNo, flrcAircrftNo, flrcAircrftType, flrcDeparture, flrcTransit, flrcDest, flrcTestBillNo, flrcFuelName, flrcFuelTemp, flrcFuelDnst, flrcFuelVol, flrcMeterStat, flrcMeterFnsh, flrcFiguars, flrcFiguarsWord, flrcQuantity, flrcVehiNo, flrcHydrtPitNo, flrcStatTime, flrcFnshTime, flrcDeliverName, flrcSign, flrcManualTag, flrcManualStaff, flrcManualTime, flrcRevwStatus, flrcTakebackFlg, flrcRecCreTime);
    }
}
