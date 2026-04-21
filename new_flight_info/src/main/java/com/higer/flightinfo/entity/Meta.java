package com.higer.flightinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dom4j.Element;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 收到消息消息头
 */
@Entity
@Data
@Table(name="tflight_cache")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Meta{

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 消息接收者
     */
    @Column
    private String rcvr;

    /**
     * 消息序号
     */
    @Column
    private String seqn;

    /**
     * 发送时间
     */
    @Column
    private String ddtm;


    /**
     * 消息类别
     */
    @Column
    private String type;

    /**
     * 原始数据
     */
    @Column
    private String content;

    /**
     * 入库时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;

    @Transient
    @JSONField(serialize=false,deserialize = false)
    private Element body;
}
