package com.higer.oildataexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@AllArgsConstructor
@XmlRootElement(name = "R")
@NoArgsConstructor
@XmlType(propOrder = {"HD", "BD"})
@XmlAccessorType(XmlAccessType.FIELD)
public class R<T> {

    private Hd HD;

    @XmlAnyElement(lax = true)
    private T BD;

    public static <T> R<T> newInstanceR(String rsn, String ssn, T bd) {
        R<T> r = new R<>();
        Hd hd = new Hd();
        hd.setMT(bd.getClass().getSimpleName());
        //hd.setMS(UUIDUtils.generateUuid8());
        hd.setRSN(rsn);
        hd.setSSN(ssn);
        hd.setSTM(new Date());
        r.setBD(bd);
        r.setHD(hd);
        return r;
    }
}
