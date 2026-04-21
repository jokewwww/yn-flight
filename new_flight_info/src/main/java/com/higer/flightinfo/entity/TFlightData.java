package com.higer.flightinfo.entity;

import com.higer.flightinfo.annotation.XmlAlias;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tflight_data")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TFlightData implements Serializable {


    @Id
    @Column(length = 60)
    @XmlAlias(name = "FID")
    private String fid;

    @Column(name = "airport_fid",length = 60)
    @XmlAlias(name = "AirportFid")
    private String airportFid;

    @Column(name = "flight_no",length = 60)
    @XmlAlias(name = "FlightNo")
    private String flightNo;

    @Column(name = "flight_date",length = 60)
    @XmlAlias(name = "FlightDate")
    private String flightDate;

    @Column(name = "dep_code",length = 60)
    @XmlAlias(name = "DepCode")
    private String depCode;

    @Column(name = "arr_code",length = 60)
    @XmlAlias(name = "ArrCode")
    private String arrCode;

    @Column(name = "flight_cla",length = 60)
    @XmlAlias(name = "FlightCla")
    private String flightCla;

    @Column(name = "air_num",length = 60)
    @XmlAlias(name = "AirNum")
    private String airNum;

    @Column(name = "air_type",length = 60)
    @XmlAlias(name = "AirType")
    private String airType;

    @Column(length = 60)
    @XmlAlias(name = "STD")
    private String std;

    @Column(length = 60)
    @XmlAlias(name = "STA")
    private String sta;

    @Column(length = 60)
    @XmlAlias(name = "ETD")
    private String etd;

    @Column(length = 60)
    @XmlAlias(name = "ETA")
    private String eta;

    @Column(length = 60)
    @XmlAlias(name = "ATD")
    private String atd;

    @Column(length = 60)
    @XmlAlias(name = "ATA")
    private String ata;

    @Column(name = "flight_status",length = 60)
    @XmlAlias(name = "FlightStatus")
    private String flightStatus;

    @Column(name = "dep_stand",length = 60)
    @XmlAlias(name = "DepStand")
    private String depStand;

    @Column(name = "arr_stand",length = 60)
    @XmlAlias(name = "ArrStand")
    private String arrStand;

    @Column(name = "gate",length = 60)
    @XmlAlias(name = "Gate")
    private String gate;

    @Column(length = 60)
    @XmlAlias(name = "Baggage1")
    private String baggage1;

    @Column(length = 60)
    @XmlAlias(name = "Baggage2")
    private String baggage2;

    @Column(name = "check_in_counter1",length = 60)
    @XmlAlias(name = "CheckInCounter1")
    private String checkInCounter1;

    @Column(name = "check_in_counter2",length = 60)
    @XmlAlias(name = "CheckInCounter2")
    private String checkInCounter2;

    @Column(length = 60)
    @XmlAlias(name = "Fcategory")
    private String fcategory;

    @Column(length = 60)
    @XmlAlias(name = "VIP")
    private String vip;

    @Column(name = "share_main_f",length = 60)
    @XmlAlias(name = "ShareMainF")
    private String shareMainF;

    @Column(name = "virtual_main_f",length = 60)
    @XmlAlias(name = "VirtualMainF")
    private String virtualMainF;

    @Column(length = 60)
    @XmlAlias(name = "Crewman")
    private String crewman;

    @Column(length = 60)
    @XmlAlias(name = "Cabin")
    private String cabin;

    @Column(length = 60)
    @XmlAlias(name = "AXIT")
    private String axit;

    @Column(length = 60)
    @XmlAlias(name = "AIBT")
    private String aibt;

    @Column(length = 60)
    @XmlAlias(name = "AGOT")
    private String agot;

    @Column(length = 60)
    @XmlAlias(name = "ASBT")
    private String asbt;

    @Column(length = 60)
    @XmlAlias(name = "ARDT")
    private String ardt;

    @Column(length = 60)
    @XmlAlias(name = "AGCT")
    private String agct;

    @Column(length = 60)
    @XmlAlias(name = "ACCT")
    private String acct;

    @Column(length = 60)
    @XmlAlias(name = "ASAT")
    private String asat;

    @Column(length = 60)
    @XmlAlias(name = "COBT")
    private String cobt;

    @Column(length = 60)
    @XmlAlias(name = "TOBT")
    private String tobt;

    @Column(length = 60)
    @XmlAlias(name = "AOBT")
    private String aobt;

    @Column(length = 60)
    @XmlAlias(name = "AXOT")
    private String axot;

    @Column(length = 60)
    @XmlAlias(name = "CTOT")
    private String ctot;

    @Column(length = 60)
    @XmlAlias(name = "TTOT")
    private String ttot;

    @Column(length = 60)
    @XmlAlias(name = "TDR")
    private String tdr;

    @Column(length = 60)
    @XmlAlias(name = "TOR")
    private String tor;

    @Column(name = "special_case",length = 60)
    @XmlAlias(name = "SpecialCase")
    private String specialCase;

    @Column(name = "baggage_number",length = 60)
    @XmlAlias(name = "BaggageNumber")
    private String baggageNumber;

    @Column(name = "baggage_weight",length = 60)
    @XmlAlias(name = "BaggageWeight")
    private String baggageWeight;

    @Column(name = "goods_number",length = 60)
    @XmlAlias(name = "GoodsNumber")
    private String goodsNumber;

    @Column(name = "goods_weight",length = 60)
    @XmlAlias(name = "GoodsWeight")
    private String goodsWeight;

    @Column(name = "mail_number",length = 60)
    @XmlAlias(name = "MailNumber")
    private String mailNumber;

    @Column(name = "mail_weight",length = 60)
    @XmlAlias(name = "MailWeight")
    private String mailWeight;

    @Column(name = "check_in_number",length = 60)
    @XmlAlias(name = "CheckInNumber")
    private String checkInNumber;

    @Column(name = "security_check_number",length = 60)
    @XmlAlias(name = "SecurityCheckNumber")
    private String securityCheckNumber;

    @Column(name = "boarding_number",length = 60)
    @XmlAlias(name = "BoardingNumber")
    private String boardingNumber;

    @Column(name = "link_for_kunming",length = 60)
    @XmlAlias(name = "LFIDForKMG")
    private String linkForKunming;

    @Column(name = "routes",length = 60)
    @XmlAlias(name = "Routes")
    private String routes;

    @CreatedDate
    @Column(name = "create_at",length = 60)
    private Date createAt;

    @LastModifiedDate
    @Column(name = "update_at",length = 60)
    private Date updateAt;

    @Transient
    private List<TFlightProcessNode> processNode;


}
