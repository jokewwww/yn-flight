package com.higer.higerservice.entity.flight;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "T_FLIGHT_CODE")
@NamedQuery(name = "TFlightCode.findAll", query = "SELECT a FROM TFlightCode a")
public class TFlightCode {

    @Id
    @Column(name = "arcr_regn")
    private String arcrRegn;

    @Column(name = "arcr_acname")
    private String arcrAcname;

    @Column(name = "arcr_custom_num")
    private String arcrCustomNum;

    @Column(name = "arcr_start_date")
    private Date arcrStartDate;

    @Column(name = "arcr_end_date")
    private Date arcrEndDate;
}
