package com.higer.flightinfo.entity;

import com.higer.flightinfo.annotation.XmlAlias;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tflight_change")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TFlightChange implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fid")
    @XmlAlias(name = "FID")
    private String fid;

    @Column(name = "airport_fid")
    @XmlAlias(name = "AirportFid")
    private String airportFid;

    @Column(name = "flight_no")
    @XmlAlias(name = "FlightNo")
    private String flightNo;

    @Column(name = "flight_date")
    @XmlAlias(name = "FlightDate")
    private String flightDate;

    @Column(name = "dep_code")
    @XmlAlias(name = "DepCode")
    private String depCode;

    @Column(name = "arr_code")
    @XmlAlias(name = "ArrCode")
    private String arrCode;

    @Column(name = "flight_cla")
    @XmlAlias(name = "FlightCla")
    private String flightCla;

    @Column(name = "air_num")
    @XmlAlias(name = "AirNum")
    private String airNum;

    @Column(name = "air_type")
    @XmlAlias(name = "AirType")
    private String airType;

    @Column
    @XmlAlias(name = "STD")
    private String std;

    @Column
    @XmlAlias(name = "STA")
    private String sta;

    @Column
    @XmlAlias(name = "ETD")
    private String etd;

    @Column
    @XmlAlias(name = "ETA")
    private String eta;

    @Column
    @XmlAlias(name = "ATD")
    private String atd;

    @Column
    @XmlAlias(name = "ATA")
    private String ata;

    @Column(name = "flight_status")
    @XmlAlias(name = "FlightStatus")
    private String flightStatus;

    @Column(name = "dep_stand")
    @XmlAlias(name = "DepStand")
    private String depStand;

    @Column(name = "arr_stand")
    @XmlAlias(name = "ArrStand")
    private String arrStand;

    @Column(name = "gate")
    @XmlAlias(name = "Gate")
    private String gate;

    @Column
    @XmlAlias(name = "Baggage1")
    private String baggage1;

    @Column
    @XmlAlias(name = "Baggage2")
    private String baggage2;

    @Column(name = "check_in_counter1")
    @XmlAlias(name = "CheckInCounter1")
    private String checkInCounter1;

    @Column(name = "check_in_counter2")
    @XmlAlias(name = "CheckInCounter2")
    private String checkInCounter2;

    @Column
    @XmlAlias(name = "Fcategory")
    private String fcategory;

    @Column
    @XmlAlias(name = "VIP")
    private String vip;

    @Column(name = "share_main_f")
    @XmlAlias(name = "ShareMainF")
    private String shareMainF;

    @Column(name = "virtual_main_f")
    @XmlAlias(name = "VirtualMainF")
    private String virtualMainF;

    @Column
    @XmlAlias(name = "Crewman")
    private String crewman;

    @Column
    @XmlAlias(name = "Cabin")
    private String cabin;

    @Column
    @XmlAlias(name = "AXIT")
    private String axit;

    @Column
    @XmlAlias(name = "AIBT")
    private String aibt;

    @Column
    @XmlAlias(name = "AGOT")
    private String agot;

    @Column
    @XmlAlias(name = "ASBT")
    private String asbt;

    @Column
    @XmlAlias(name = "ARDT")
    private String ardt;

    @Column
    @XmlAlias(name = "AGCT")
    private String agct;

    @Column
    @XmlAlias(name = "ACCT")
    private String acct;

    @Column
    @XmlAlias(name = "ASAT")
    private String asat;

    @Column
    @XmlAlias(name = "COBT")
    private String cobt;

    @Column
    @XmlAlias(name = "TOBT")
    private String tobt;

    @Column
    @XmlAlias(name = "AOBT")
    private String aobt;

    @Column
    @XmlAlias(name = "AXOT")
    private String axot;

    @Column
    @XmlAlias(name = "CTOT")
    private String ctot;

    @Column
    @XmlAlias(name = "TTOT")
    private String ttot;

    @Column
    @XmlAlias(name = "TDR")
    private String tdr;

    @Column
    @XmlAlias(name = "TOR")
    private String tor;

    @Column(name = "special_case")
    @XmlAlias(name = "SpecialCase")
    private String specialCase;

    @Column(name = "baggage_number")
    @XmlAlias(name = "BaggageNumber")
    private String baggageNumber;

    @Column(name = "baggage_weight")
    @XmlAlias(name = "BaggageWeight")
    private String baggageWeight;

    @Column(name = "goods_number")
    @XmlAlias(name = "GoodsNumber")
    private String goodsNumber;

    @Column(name = "goods_weight")
    @XmlAlias(name = "GoodsWeight")
    private String goodsWeight;

    @Column(name = "mail_number")
    @XmlAlias(name = "MailNumber")
    private String mailNumber;

    @Column(name = "mail_weight")
    @XmlAlias(name = "MailWeight")
    private String mailWeight;

    @Column(name = "check_in_number")
    @XmlAlias(name = "CheckInNumber")
    private String checkInNumber;

    @Column(name = "security_check_number")
    @XmlAlias(name = "SecurityCheckNumber")
    private String securityCheckNumber;

    @Column(name = "boarding_number")
    @XmlAlias(name = "BoardingNumber")
    private String boardingNumber;

    @Column(name = "link_for_kunming")
    @XmlAlias(name = "LFIDForKMG")
    private String linkForKunming;

    @Column(name = "routes")
    @XmlAlias(name = "Routes")
    private String routes;

    @CreatedDate
    @Column(name = "create_at")
    private Date createAt;

    @Transient
    private String processNode;
}
