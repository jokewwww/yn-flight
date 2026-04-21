package com.higer.flightinfo.entity;

import com.higer.flightinfo.annotation.XmlAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tflight_process_node")
public class TFlightProcessNode {

    @Id
    @Column(length = 60)
    private String fid;

    @Column
    private String item;

    @Column(name = "airport")
    private String airport;

    @Column(name = "process_node_name")
    @XmlAlias(name = "ProcessNodeName")
    private String processNodeName;

    @Column(name = "process_node_time")
    @XmlAlias(name = "ProcessNodeTime")
    private String processNodeTime;

    @Column(name = "from_client")
    @XmlAlias(name = "FromClient")
    private String fromClient;

    @Column(name = "from_user_name")
    @XmlAlias(name = "FromUserName")
    private String fromUserName;

    @Column(name = "from_user_mobile")
    @XmlAlias(name = "FromUserMobile")
    private String fromUserMobile;

    @Column(name = "from_user_department")
    @XmlAlias(name = "FromUserDepartment")
    private String fromUserDepartment;

    @Column(name = "process_node_info")
    @XmlAlias(name = "ProcessNodeInfo")
    private String processNodeInfo;
}
