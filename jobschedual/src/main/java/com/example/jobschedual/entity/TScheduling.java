package com.example.jobschedual.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="T_SCHEDULING")
public class TScheduling {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="group_id")
    private String groupId;

    @Column(name="name")
    private String name;

    @Column(name="staff_id")
    private String staffId;

    @Column(name="remark")
    private String remark;
}
