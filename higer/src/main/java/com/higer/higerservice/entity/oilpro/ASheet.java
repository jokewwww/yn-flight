package com.higer.higerservice.entity.oilpro;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/2 10:19
 * @Description:
 */
@Entity
@Table(name = "a_sheet")
@NamedQuery(name = "ASheet.findAll", query = "SELECT a FROM ASheet a")
public class ASheet {
    private String id;
    private String date;
    private String deliveryType;
    private String deliveryNo;
    private String airport;
    private String delivered;
    private String filghtNo;
    private String aircraftNo;
    private String aircraftType;
    private String departure;
    private String transitStop;
    private String destination;
    private String testbillNo;
    private String descriptionAndGrade;
    private Double temperature;
    private Double actualDensity;
    private Long meterStart;
    private Long meterFinish;
    private Long figures;//加油数量 小写 升
    private String figuresInWords;
    private Long quantity;
    private String hydrantPitNo;
    private String vehicleTypeAndNo;
    private String timeStart;
    private String timeFinish;
    private String signName;

    @Id
    @Column(name = "id", nullable = true, length = 128)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date", nullable = true, length = 32)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "delivery_type", nullable = true, length = 32)
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Basic
    @Column(name = "delivery_no", nullable = true, length = 64)
    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    @Basic
    @Column(name = "airport", nullable = true, length = 128)
    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Basic
    @Column(name = "delivered", nullable = true, length = 128)
    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    @Basic
    @Column(name = "filght_no", nullable = true, length = 32)
    public String getFilghtNo() {
        return filghtNo;
    }

    public void setFilghtNo(String filghtNo) {
        this.filghtNo = filghtNo;
    }

    @Basic
    @Column(name = "aircraft_no", nullable = true, length = 32)
    public String getAircraftNo() {
        return aircraftNo;
    }

    public void setAircraftNo(String aircraftNo) {
        this.aircraftNo = aircraftNo;
    }

    @Basic
    @Column(name = "aircraft_type", nullable = true, length = 32)
    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    @Basic
    @Column(name = "departure", nullable = true, length = 32)
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @Basic
    @Column(name = "transit_stop", nullable = true, length = 32)
    public String getTransitStop() {
        return transitStop;
    }

    public void setTransitStop(String transitStop) {
        this.transitStop = transitStop;
    }

    @Basic
    @Column(name = "destination", nullable = true, length = 64)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Basic
    @Column(name = "testbill_no", nullable = true, length = 64)
    public String getTestbillNo() {
        return testbillNo;
    }

    public void setTestbillNo(String testbillNo) {
        this.testbillNo = testbillNo;
    }

    @Basic
    @Column(name = "description_and_grade", nullable = true, length = 64)
    public String getDescriptionAndGrade() {
        return descriptionAndGrade;
    }

    public void setDescriptionAndGrade(String descriptionAndGrade) {
        this.descriptionAndGrade = descriptionAndGrade;
    }

    @Basic
    @Column(name = "temperature", nullable = true, precision = 1)
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Basic
    @Column(name = "actual_density", nullable = true, precision = 4)
    public Double getActualDensity() {
        return actualDensity;
    }

    public void setActualDensity(Double actualDensity) {
        this.actualDensity = actualDensity;
    }

    @Basic
    @Column(name = "meter_start", nullable = true)
    public Long getMeterStart() {
        return meterStart;
    }

    public void setMeterStart(Long meterStart) {
        this.meterStart = meterStart;
    }

    @Basic
    @Column(name = "meter_finish", nullable = true)
    public Long getMeterFinish() {
        return meterFinish;
    }

    public void setMeterFinish(Long meterFinish) {
        this.meterFinish = meterFinish;
    }

    @Basic
    @Column(name = "figures_in_words", nullable = true, length = 32)
    public String getFiguresInWords() {
        return figuresInWords;
    }

    public void setFiguresInWords(String figuresInWords) {
        this.figuresInWords = figuresInWords;
    }

    @Basic
    @Column(name = "figures", nullable = true)
    public Long getFigures() {
        return figures;
    }

    public void setFigures(Long figures) {
        this.figures = figures;
    }

    @Basic
    @Column(name = "quantity", nullable = true)
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "hydrant_pit_no", nullable = true, length = 32)
    public String getHydrantPitNo() {
        return hydrantPitNo;
    }

    public void setHydrantPitNo(String hydrantPitNo) {
        this.hydrantPitNo = hydrantPitNo;
    }

    @Basic
    @Column(name = "vehicle_type_and_no", nullable = true, length = 32)
    public String getVehicleTypeAndNo() {
        return vehicleTypeAndNo;
    }

    public void setVehicleTypeAndNo(String vehicleTypeAndNo) {
        this.vehicleTypeAndNo = vehicleTypeAndNo;
    }

    @Basic
    @Column(name = "time_start", nullable = true, length = 64)
    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    @Basic
    @Column(name = "time_finish", nullable = true, length = 64)
    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    @Basic
    @Column(name = "sign_name", nullable = true, length = 32)
    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ASheet aSheet = (ASheet) o;
        return id == aSheet.id &&
                Objects.equals(date, aSheet.date) &&
                Objects.equals(deliveryType, aSheet.deliveryType) &&
                Objects.equals(deliveryNo, aSheet.deliveryNo) &&
                Objects.equals(airport, aSheet.airport) &&
                Objects.equals(delivered, aSheet.delivered) &&
                Objects.equals(filghtNo, aSheet.filghtNo) &&
                Objects.equals(aircraftNo, aSheet.aircraftNo) &&
                Objects.equals(aircraftType, aSheet.aircraftType) &&
                Objects.equals(departure, aSheet.departure) &&
                Objects.equals(transitStop, aSheet.transitStop) &&
                Objects.equals(destination, aSheet.destination) &&
                Objects.equals(testbillNo, aSheet.testbillNo) &&
                Objects.equals(descriptionAndGrade, aSheet.descriptionAndGrade) &&
                Objects.equals(temperature, aSheet.temperature) &&
                Objects.equals(actualDensity, aSheet.actualDensity) &&
                Objects.equals(meterStart, aSheet.meterStart) &&
                Objects.equals(meterFinish, aSheet.meterFinish) &&
                Objects.equals(figuresInWords, aSheet.figuresInWords) &&
                Objects.equals(figures, aSheet.figures) &&
                Objects.equals(quantity, aSheet.quantity) &&
                Objects.equals(hydrantPitNo, aSheet.hydrantPitNo) &&
                Objects.equals(vehicleTypeAndNo, aSheet.vehicleTypeAndNo) &&
                Objects.equals(timeStart, aSheet.timeStart) &&
                Objects.equals(timeFinish, aSheet.timeFinish) &&
                Objects.equals(signName, aSheet.signName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, deliveryType, deliveryNo, airport, delivered, filghtNo, aircraftNo, aircraftType, departure, transitStop, destination, testbillNo, descriptionAndGrade, temperature, actualDensity, meterStart, meterFinish, figures, figuresInWords, quantity, hydrantPitNo, vehicleTypeAndNo, timeStart, timeFinish, signName);
    }
}
