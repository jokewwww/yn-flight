package com.higer.higerservice.entity.oilpro;

import javax.persistence.*;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/25 13:43
 * @Description:
 */
@Entity
@Table(name = "a_records")
@NamedQuery(name = "ARecords.findAll", query = "SELECT a FROM ARecords a")
public class ARecords {
    private Integer id;
    private String func;
    private Integer dateType;
    private String date;
    private Integer state;
    private String insertDate;
    private String errorMsg;

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
    @Column(name = "func", nullable = true, length = 32)
    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    @Basic
    @Column(name = "date_type", nullable = true, length = 10)
    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    @Basic
    @Column(name = "date", nullable = true, length = -1)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "state", nullable = true)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "insert_date", nullable = true, length = 32)
    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }


    @Basic
    @Column(name = "error_msg", nullable = true, length = 255)
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
