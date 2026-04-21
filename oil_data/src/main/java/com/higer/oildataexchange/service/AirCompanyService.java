package com.higer.oildataexchange.service;

import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ModelAssistant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.airCompany.ALRQ;
import com.higer.oildataexchange.entity.airCompany.ALRQ_R;
import com.higer.oildataexchange.entity.airUnit.TAirlinesCode;
import com.higer.oildataexchange.repository.TAirLinesCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class AirCompanyService implements Job {


    @Autowired
    private RedisTemplate<String, Date> redisTemplate;

    @Autowired
    private SendHttpService sendHttpService;

    @Autowired
    private TAirLinesCodeRepository tAirLinesCodeRepository;

    @Value(value = "${airCompanyUrl}")
    private String airCompanyInfoUrl;

    public void airCompanyInfoSchedule() {
        try {
            Date currentDate = new Date();
            Date lastDate = redisTemplate.opsForValue().getAndSet("airCompanyInfo", currentDate);
            if (lastDate == null) {
                log.info(String.format("第一次执行航空公司查询，执行时间为%s", DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            } else {
                log.info(String.format("上一次执行订航空公司查询时间%s，本次执行时间%s", DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS), DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            }
            ALRQ alrq = new ALRQ();
            alrq.setLUTS(lastDate == null ? "" : DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS_SSS));
            //alrq.setLUTS("2018-04-03 23:59:59.000");
            alrq.setLMTN(100);
            R<ALRQ> rgrqr = R.newInstanceR("CNAF", "CNAF_TSN", alrq);
            String xml = XmlUtils.convertToXml(rgrqr, Constant.CHARSET, true);
            log.info("airCompanyInfoSchedule发送：" + xml);
            String s = sendHttpService.sendHttpPost(airCompanyInfoUrl, xml);
            log.info("airCompanyInfoSchedule收到：" + s);
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            List<Element> sub = bd.elements("SUB");
            sub.stream().map(Element::asXML)
                    .map(item -> XmlUtils.convertToObj(item, ALRQ_R.class))
                    .map(ALRQ_R::toAirLinesCode)
                    .forEach(one -> {
                        TAirlinesCode tAirlinesCode = tAirLinesCodeRepository.findByAlcdIcaoCode(one.getAlcdIcaoCode());
                        if (tAirlinesCode != null) {
                            ModelAssistant.copyProperties(one, tAirlinesCode);
                            tAirLinesCodeRepository.save(tAirlinesCode);
                        } else {
                            tAirLinesCodeRepository.save(one);
                        }
                    });
            currentDate = null;
            System.gc();
        } catch (Exception e) {
            log.error("飞机所属单位查询异常：" + e.getMessage());
            e.printStackTrace();
            System.gc();
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.airCompanyInfoSchedule();
    }
}
