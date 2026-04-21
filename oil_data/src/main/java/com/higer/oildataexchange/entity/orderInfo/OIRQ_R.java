package com.higer.oildataexchange.entity.orderInfo;

import com.higer.oildataexchange.common.JaxbDateAdapter;
import com.higer.oildataexchange.common.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.ParseException;
import java.util.Date;

import static com.higer.oildataexchange.common.Constant.YYYYMMDD;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SUB")
public class OIRQ_R {

    /**
     * 必须，数据更新类型，I-完整数据，将列出所有数据项，未列出的项认为是null；U-更新数据，仅列出所有发生变更的数据项，未列出的项表示未变更；D-删除数据。默认为“U”
     */
    private String IUD;

    /**
     * 必须，最后更新时间戳，格式：YYYY-MM-DD HH:MI:SS.nnn
     */
    private String LUTS;

    /**
     * 必须，订单号
     */
    private String ORDER_NO;

    /**
     * 必须，供油服务模式（五种模式之一）： 0-机坪加注；1-自提；2-油品配送；3-野外保障加油；4-自助加油
     */
    private String ADDOIL_TYPE;

    /**
     * 必须，订单用油/供油机场
     */
    private String APC3;

    /**
     * 详细地址（场外保障供油或油品配送时可能有）
     */
    private String ADDRESS;

    /**
     * 必须，订单日期（用油日期）YYYYMMDD
     */
    private String ORDER_DATE;

    /**
     * 必须，客户代码
     */
    private String CSTNO;

    /**
     * 必须，客户名称
     */
    private String CSTNM;

    /**
     * 必须，计划加油量（单位是：千克）
     */
    private Integer ESTIMATED_VOLUME;

    /**
     * 必须，计划用油/供油时间YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date ESTIMATED_TIME;

    /**
     * 必须，加油开始日期：YYYY-MM-dd HH:mi:ss
     */
    private String PLAN_DATE_BEGIN;

    /**
     * 必须，加油结束日期：YYYY-MM-dd HH:mi:ss
     */
    private String PLAN_DATE_END;

    /**
     * 关闭说明
     */
    private String CLOSE_CONTENT;

    /**
     * 必须，订单性质，3-国内；2-离境；1-国际
     */
    private String OIL_TYPE;


    /**
     * 必须，订单状态：0-尚未执行；1-等待执行；2-已经执行；3-未能执行
     */
    private String ORDER_STATUS;

    /**
     *
     */
    private String OTYP;
    /**
     * 提油人/用油人姓名
     */
    private String OIL_USER_NM;

    /**
     * 提油人/用油人联系电话
     */
    private String OIL_USER_TEL;

    /**
     * 运油车号/车牌号（自提油运油车编号/牌号）
     */
    private String TANKER_NO;

    /**
     * 航班号：组成格式是航空公司二码+航班号，例如：CZ8887
     */
    private String FLNO;

    /**
     * 航班性质，例如：W/Z，H/Z等
     */
    private String TTYP;

    /**
     * 机号
     */
    private String REGN;
    /**
     * 机型3码
     */
    private String ACT3;
    /**
     * 机型代码（完整代码）
     */
    private String ACTN;
    /**
     * 航空公司二码
     */
    private String ALC2;
    /**
     * 航空公司名称
     */
    private String ALCNM;
    /**
     * 始发地机场3码
     */
    private String ORG3;
    /**
     * 始发地机场名称
     */
    private String ORGNM;
    /**
     * 目的地机场3码
     */
    private String DES3;
    /**
     * 目的地机场名称
     */
    private String DESNM;
    /**
     * 经停机场3码
     */
    private String VIA3;
    /**
     * 经停机场名称
     */
    private String VIANM;
    /**
     * 必须，记录创建时间:YYYY-MM-DD HH:MI:SS
     */
    private String CDAT;
    /**
     * 记录最后更新时间，一般是指航班业务动态的变更时间，不包含程序进行其他处理而导致更新的时间，
     * 与LUTS含义不同，取值可能会不一样。YYYY-MM-DD HH:MI:SS
     */
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date LSTU;

    public TOrderInfo tOrderInfo() {
        TOrderInfo orderInfo = new TOrderInfo();
        orderInfo.setIud(IUD);
        orderInfo.setLuts(LUTS);
        orderInfo.setOrderNo(ORDER_NO);
        orderInfo.setAddoilType(ADDOIL_TYPE);
        orderInfo.setApc3(APC3);
        orderInfo.setAddress(ADDRESS);
        try {
            orderInfo.setOrderDate(StringUtils.isNotEmpty(ORDER_DATE) ? DateUtils.parseDate(ORDER_DATE, YYYYMMDD) : null);
        } catch (ParseException e) {
            e.printStackTrace();
            orderInfo.setOrderDate(null);
        }
        orderInfo.setCstno(CSTNO);
        orderInfo.setCstnm(CSTNM);
        orderInfo.setEstimatedVolume(ESTIMATED_VOLUME);
        orderInfo.setEstimatedTime(ESTIMATED_TIME);
        orderInfo.setOilType(Integer.parseInt(OIL_TYPE));
        orderInfo.setOrderStatus(Integer.parseInt(ORDER_STATUS));
        orderInfo.setOilUserNm(OIL_USER_NM);
        orderInfo.setOilUserTel(OIL_USER_TEL);
        orderInfo.setTankerNo(TANKER_NO);
        orderInfo.setFlno(FLNO);
        orderInfo.setTtyp(TTYP);
        orderInfo.setRegn(REGN);
        orderInfo.setAct3(ACT3);
        orderInfo.setActn(ACTN);
        orderInfo.setAlc2(ALC2);
        orderInfo.setAlcnm(ALCNM);
        orderInfo.setOrg3(ORG3);
        orderInfo.setOrgnm(ORGNM);
        orderInfo.setDes3(DES3);
        orderInfo.setDesnm(DESNM);
        orderInfo.setVia3(VIA3);
        orderInfo.setVianm(VIANM);
        try {
            orderInfo.setCdat(StringUtils.isNotEmpty(CDAT) ? DateUtils.parseDate(CDAT, YYYY_MM_DD_HH_MM_SS) : null);
        } catch (ParseException e) {
            e.printStackTrace();
            orderInfo.setCdat(null);
        }
        orderInfo.setLstu(LSTU);
        orderInfo.setOtyp(OTYP);
        try {
            orderInfo.setPlanDateBegin(StringUtils.isNotEmpty(PLAN_DATE_BEGIN) ? DateUtils.parseDate(PLAN_DATE_BEGIN, YYYY_MM_DD_HH_MM_SS) : null);
        } catch (ParseException e) {
            e.printStackTrace();
            orderInfo.setPlanDateBegin(null);
        }
        try {
            orderInfo.setPlanDateEnd(StringUtils.isNotEmpty(PLAN_DATE_END) ? DateUtils.parseDate(PLAN_DATE_END, YYYY_MM_DD_HH_MM_SS) : null);
        } catch (ParseException e) {
            e.printStackTrace();
            orderInfo.setPlanDateEnd(null);
        }
        orderInfo.setCloseContent(CLOSE_CONTENT);
        return orderInfo;
    }
}
