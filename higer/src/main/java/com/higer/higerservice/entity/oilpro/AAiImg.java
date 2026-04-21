package com.higer.higerservice.entity.oilpro;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/5 15:09
 * @Description:
 */
@Data
@Entity
@Table(name = "a_ai_img")
@NamedQuery(name = "AAiImg.findAll", query = "SELECT a FROM AAiImg a")
public class AAiImg implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "logo_code")
    private String logoCode;
    @Basic
    @Column(name = "img_data")
    private String imgData;
    @Basic
    @Column(name = "img_data_base")
    private String imgDataBase;
    @Transient
    private String username;
    @Transient
    private String psd;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AAiImg aAiImg = (AAiImg) o;
        return id == aAiImg.id &&
                Objects.equals(logoCode, aAiImg.logoCode) &&
                Objects.equals(imgData, aAiImg.imgData) &&
                Objects.equals(imgDataBase, aAiImg.imgDataBase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, logoCode, imgData, imgDataBase);
    }
}
