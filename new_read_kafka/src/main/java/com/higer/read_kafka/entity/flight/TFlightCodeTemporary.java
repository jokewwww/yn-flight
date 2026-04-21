package com.higer.read_kafka.entity.flight;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name="T_FLIGHT_CODE_TEMPORARY")
@NamedQuery(name = "TFlightCodeTemporary.findAll", query = "SELECT a FROM TFlightCodeTemporary a")
public class TFlightCodeTemporary {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id", nullable = false)
    private Integer id;

    /**
     * 飞机号码
     */
    @Column( name = "arcr_regn", nullable = false)
    private String arcrRegn;

    /**
     * 飞机类型
     */
    @Column(name = "arcr_acname")
    private String arcrAcname;

    /**
     * 购货方编号
     */
    @Column(name = "arcr_custom_num")
    private String arcrCustomNum;

    /**
     * 开始日期
     */
    @Column(name = "arcr_start_date", nullable = false)
    private Date arcrStartDate;

    /**
     * 结束日期
     */
    @Column(name = "arcr_end_date")
    private Date arcrEndDate;


    /**
     * 指定的航班号
     */
    @Column(name = "flno")
    private String flno;

    /**
     * 记录创建时间-系统
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 记录更新时间-系统
     */
    @Column(name = "update_time")
    private Date updateTime;

    public static TFlightCode converToFlgihtCode(TFlightCodeTemporary tFlightCodeTemporary){
        if(tFlightCodeTemporary==null){
            return null;
        }
        TFlightCode tFlightCode = new TFlightCode();
        BeanUtils.copyProperties(tFlightCodeTemporary,tFlightCode);
        return tFlightCode;
    }

    public static List<TFlightCode> converToFlgihtCode(List<TFlightCodeTemporary> tFlightCodeTemporaries){
        if(tFlightCodeTemporaries==null ||tFlightCodeTemporaries.size()==0){
            return new ArrayList<>();
        }
        return tFlightCodeTemporaries.stream().map(TFlightCodeTemporary::converToFlgihtCode).collect(Collectors.toList());
    }
}
