package com.ncse.fdds.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/7/7 09:38
 * @Description:
 */
@Data
@Entity
@Table(name = "tflight_received_data")
@EntityListeners({AuditingEntityListener.class})
public class TflightReceivedData {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "flgt_ffid", nullable = true, length = 64)
    private String flgtFfid;
    @Column(name = "flgt_airport_code", nullable = true, length = 4)
    private String flgtAirportCode;
    @Column(name = "received_msg", nullable = true, length = -1)
    private String receivedMsg;
    @Column(name = "create_date", nullable = true)
    @CreatedDate
    private Timestamp createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TflightReceivedData that = (TflightReceivedData) o;
        return id == that.id &&
                Objects.equals(flgtFfid, that.flgtFfid) &&
                Objects.equals(flgtAirportCode, that.flgtAirportCode) &&
                Objects.equals(receivedMsg, that.receivedMsg) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flgtFfid, flgtAirportCode, receivedMsg, createDate);
    }
}
