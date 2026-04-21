package com.higer.oildataexchange.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ModelAssistant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.airUnit.RGRQ;
import com.higer.oildataexchange.entity.airUnit.RGRQ_R;
import com.higer.oildataexchange.entity.flight.TFlightCode;
import com.higer.oildataexchange.entity.flight.TFlightCodeHistory;
import com.higer.oildataexchange.repository.TCustomRepository;
import com.higer.oildataexchange.repository.TFlightCodeHistoryRepository;
import com.higer.oildataexchange.repository.TFlightCodeRepository;
import com.higer.oildataexchange.repository.TFlightCodeTemporaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class AirUnitService implements Job {


    @Autowired
    private RedisTemplate<String, Date> redisTemplate;

    @Autowired
    private SendHttpService sendHttpService;

    @Autowired
    private TFlightCodeRepository flightCodeRepository;

    @Autowired
    private TFlightCodeHistoryRepository tFlightCodeHistoryRepository;

    @Autowired
    private TFlightCodeTemporaryRepository flightCodeTemporaryRepository;

    @Autowired
    private TCustomRepository tCustomRepository;

    @Value(value = "${fliter_airport_code}")
    private String filterAirportCode;

    @Value(value = "${aircraftUnitUrl}")
    private String airUnitInfoUrl;

    public void airUnitInfoSchedule() {
        try {
            Date currentDate = new Date();
            Date lastDate = redisTemplate.opsForValue().getAndSet("airUnitInfo", currentDate);
            if (lastDate == null) {
                log.info(String.format("第一次执行飞机所属单位查询，执行时间为%s", DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            } else {
                log.info(String.format("上一次执行订飞机所属单位询时间%s，本次执行时间%s", DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS), DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            }
            RGRQ rgrq = new RGRQ();
            rgrq.setLUTS(lastDate == null ? "" : DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS_SSS));
            //rgrq.setLUTS("2020-05-04 00:00:01.001");
            rgrq.setLMTN(100);
            R<RGRQ> rgrqr = R.newInstanceR("CNAF", "CNAF_TSN", rgrq);
            String xml = XmlUtils.convertToXml(rgrqr, Constant.CHARSET, true);
            log.info("airUnitInfoSchedule发送：" + xml + "--------airUnitInfoSchedule发送");
            String s = sendHttpService.sendHttpPost(airUnitInfoUrl, xml);
            //URI uri = getClass().getClassLoader().getResource("respose.xml").toURI();
            //String s=new String(Files.readAllBytes(Paths.get(uri)), StandardCharsets.UTF_8);
            log.info("airUnitInfoSchedule收到：" + s + "airUnitInfoSchedule收到");
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            List<Element> sub = bd.elements("SUB");

            List<RGRQ_R> collect1 = sub.stream().map(Element::asXML)
                    .map(item -> XmlUtils.convertToObj(item, RGRQ_R.class))
                    .collect(Collectors.toList());

            List<TFlightCode> collect = sub.stream().map(Element::asXML)
                    .map(item -> XmlUtils.convertToObj(item, RGRQ_R.class))
                    .map(RGRQ_R::toFlightCode)
                    .collect(Collectors.toList());

            System.out.println("collect_______" + collect.size());
            AtomicInteger count = new AtomicInteger();
            collect.forEach(one -> {
                String iud = one.getIud();
                one.setDelFlag("D".equals(iud) ? 1 : 0);
                String arcrRegn = one.getArcrRegn();
                String newArcrRegn = null;
                //如果飞机号码没有-则手动加上
                if (arcrRegn.startsWith("B") && arcrRegn.indexOf("-") == -1) {
                    StringBuffer stringBuilder1 = new StringBuffer(arcrRegn);
                    stringBuilder1.insert(1, "-");
                    newArcrRegn = stringBuilder1.toString();
                } else {
                    //TODO
                    String a = "^([A-Z]+)(\\d*.*)$";
                    Pattern compile = Pattern.compile(a);
                    Matcher matcher = compile.matcher(arcrRegn);
                    if (matcher.matches()) {
                        if (StringUtils.isNotEmpty(matcher.group(2)) && StringUtils.isNotEmpty(matcher.group(1))) {
                            newArcrRegn = matcher.group(1) + "-" + matcher.group(2);
                        } else if (StringUtils.isNotEmpty(matcher.group(1))) {
                            newArcrRegn = matcher.group(1);
                        }
                    }
                }
                try {
                    if (null == one.getArcrStartDate() || null == one.getArcrEndDate()) {
                        log.error("未处理飞机信息=======时间不正确" + JSON.toJSONString(one));
                        return;
                    }
                    if (StringUtils.isBlank(one.getArcrCustomNum())) {
                        log.error("未处理飞机信息=======加油客户编号为空" + JSON.toJSONString(one));
                        return;
                    }
                    String flno = one.getFlno();
                    String arcrAcname = one.getArcrAcname();
                    String arcrCustomNum = one.getArcrCustomNum();
                    if (StringUtils.isBlank(flno)) {
                        one.setFlno(null);
                    }
                    //保存更新历史
                    TFlightCodeHistory history = new TFlightCodeHistory();
                    BeanUtil.copyProperties(one, history);
                    tFlightCodeHistoryRepository.save(history);

                    one.setCid(one.getId());
                    one.setId(null);
                    //置空ID
                    TFlightCode byArcrRegn = null;
                    Integer cid = one.getCid();
                    Integer delFlag = one.getDelFlag();
                    if (cid != null) {
                        byArcrRegn = flightCodeRepository.findByCid(one.getCid());
                    }
                    TFlightCode oldByArcrRegn;
                    if (StringUtils.isNotBlank(flno)) {
                        oldByArcrRegn = flightCodeRepository.findFlightCodeByRegnAndFlnoAndCusnumAndDelFlag(arcrRegn, flno, arcrCustomNum, delFlag);
                    } else {
                        oldByArcrRegn = flightCodeRepository.findFlightCodeByRegnAndCusnumAndDelFlag(arcrRegn, arcrCustomNum, delFlag);
                    }
                    //老数据不为空且新数据有效，则删除老数据
                    if (oldByArcrRegn != null && one.valid()) {
                        flightCodeRepository.delete(oldByArcrRegn);
                    }
                    arcrAcname = StrUtil.blankToDefault(arcrAcname, Optional.ofNullable(oldByArcrRegn).map(TFlightCode::getArcrAcname).orElse(null));
                    TFlightCode newByArcrRegn;
                    if (StringUtils.isNotBlank(flno)) {
                        newByArcrRegn = flightCodeRepository.findFlightCodeByRegnAndFlnoAndCusnumAndDelFlag(newArcrRegn, flno, arcrCustomNum, delFlag);
                    } else {
                        newByArcrRegn = flightCodeRepository.findFlightCodeByRegnAndCusnumAndDelFlag(newArcrRegn, arcrCustomNum, delFlag);
                    }
                    //老数据不为空且新数据有效，则删除老数据
                    if (newByArcrRegn != null && one.valid()) {
                        flightCodeRepository.delete(newByArcrRegn);
                    }
                    arcrAcname = StrUtil.blankToDefault(arcrAcname, Optional.ofNullable(newByArcrRegn).map(TFlightCode::getArcrAcname).orElse(null));
                    one.setArcrAcname(arcrAcname);
                    if (byArcrRegn != null) {
                        ModelAssistant.copyProperties(one, byArcrRegn);
                        byArcrRegn.setUpdateTime(new Date());
                        flightCodeRepository.saveAndFlush(byArcrRegn);
                        log.info("更新的飞机单位信息：" + JSON.toJSONString(byArcrRegn));
                    } else {
                        one.setId(null);
                        one.setCreateTime(new Date());
                        flightCodeRepository.saveAndFlush(one);
                        log.info("新增的飞机单位信息：" + JSON.toJSONString(one));
                    }
                } catch (Exception e) {
                    log.error("报错了 暂时丢弃-----------" + e.getMessage());
                    log.error("未处理飞机信息=======报错异常" + JSON.toJSONString(one));
                }
                count.getAndIncrement();
                log.info("处理数据总数：{}", count.get());
            });


            //LUTS
            if (CollUtil.isNotEmpty(collect) && collect.size() > 0) {
                RGRQ_R rgrq_r = collect1.get(collect.size() - 1);
                redisTemplate.opsForValue().set("airUnitInfo", DateUtil.parse(rgrq_r.getLUTS(), DatePattern.NORM_DATETIME_PATTERN));
            }

        } catch (Exception e) {
            log.error("飞机所属单位查询异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.airUnitInfoSchedule();
    }
}
