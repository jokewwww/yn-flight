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
@Table(name = "tflight_sendxml_data")
@EntityListeners({AuditingEntityListener.class})
public class TflightSendxmlData {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "flgt_ffid", nullable = true, length = 64)
    private String flgtFfid;
    @Basic
    @Column(name = "xml_msg", nullable = true, length = -1)
    private String xmlMsg;

    @Column(name = "create_date", nullable = true)
    @CreatedDate
    private Timestamp createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TflightSendxmlData that = (TflightSendxmlData) o;
        return id == that.id &&
                Objects.equals(flgtFfid, that.flgtFfid) &&
                Objects.equals(xmlMsg, that.xmlMsg) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flgtFfid, xmlMsg, createDate);
    }
}
