package com.higer.oildataexchange.entity.acdm;

import com.higer.oildataexchange.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@AllArgsConstructor
@XmlRootElement(name = "MSG")
@NoArgsConstructor
@XmlType(propOrder = {"META", "INFO"})
@XmlAccessorType(XmlAccessType.FIELD)
public class AcdmXml<T> {

    private AcdmMeta META;

    @XmlAnyElement(lax = true)
    private T INFO;

    //FUEL-BGN FUEL-END
    public static <T> AcdmXml<T> newInstanceAcdm(String flid, String styp, String airl, String aord, String deps, String arvs, String seqn, T info) {
        AcdmXml<T> r = new AcdmXml<>();
        AcdmMeta meta = new AcdmMeta();
        meta.setSNDR("GSM");
        meta.setDTTM(DateFormatUtils.format(new Date(), Constant.YYYYMMDDHHMMSS));
        meta.setSTYP(styp);
        meta.setTYPE("GSSP");
        meta.setAIRL(airl);
        meta.setAORD(aord);
        meta.setDEPS(deps);
        meta.setARVS(arvs);
        meta.setSEQN(seqn);
        meta.setFLID(flid);
        r.setINFO(info);
        r.setMETA(meta);
        return r;
    }
}
