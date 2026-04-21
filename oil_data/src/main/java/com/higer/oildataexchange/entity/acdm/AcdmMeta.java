package com.higer.oildataexchange.entity.acdm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "META")

public class AcdmMeta {

    /**
     * 必须 发送者
     */
    private String SNDR;

    /**
     * 必须 发送消息的主类型
     */
    @NonNull
    private String TYPE;

    /**
     * 必须 发送消息的子类型
     */
    @NonNull
    private String STYP;

    /**
     * 非必须 报文发送时间
     */
    private String DTTM;

    /**
     * 非必须 报文序号，DTTM时间相同的按照序号排序
     */
    private String SEQN;

    /**
     * 航班所属航空公司二字码
     */
    private String AIRL;

    /**
     * 航班进港出港属性
     */
    @NonNull
    private String AORD;

    /**
     * 航班出发航站
     */
    private String DEPS;

    /**
     * 航班到达航站
     */
    private String ARVS;

    /**
     * 航班唯一ID
     */
    private String FLID;


}
