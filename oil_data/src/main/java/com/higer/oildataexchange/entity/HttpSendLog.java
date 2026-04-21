package com.higer.oildataexchange.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Table(name = "T_HTTP_SEND_LOG")
public class HttpSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Column
    @NonNull
    private String url;

    @Column(name = "request_data")
    @NonNull
    private String requestData;

    @Column(name = "response_data")
//    @NonNull
    private String responseData;

    @Column(name = "create_data")
    @CreatedDate
    private Date createDate;

}
