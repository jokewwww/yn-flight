package com.higer.read_kafka.service;

import com.alibaba.fastjson.JSON;
import com.higer.read_kafka.component.RedissonDistributedLocker;
import com.higer.read_kafka.constant.Constant;
import com.higer.read_kafka.entity.flight.*;
import com.higer.read_kafka.entity.flight.in.InTFlight;
import com.higer.read_kafka.entity.user.TStaff;
import com.higer.read_kafka.repository.flight.*;
import com.higer.read_kafka.repository.user.TStaffRepository;
import com.higer.read_kafka.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.higer.read_kafka.constant.Constant.FLIGHT_NUM_DISTRIBUTED_LOCK_KEY;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:34
 * @Description:
 */
@Service
public class KafkaService {

    private final static Logger log = LoggerFactory.getLogger(KafkaService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TStaffRepository tStaffRepository;

    @Autowired
    private TFlightRepository tFlightRepository;

    @Autowired
    private TAirportCodeRepository tAirportCodeRepository;

    @Autowired
    private TAirlinesCodeRepository tAirlinesCodeRepository;

    @Autowired
    private TForeignairportCodeRepository tForeignairportCodeRepository;

    @Autowired
    private TTaskRepository tTaskRepository;
    //(unitName = "primaryPersistenceUnit")

    @PersistenceContext
    private EntityManager entityManagerFlight;

    @Value("${flightChangeLogUrl}")
    private String flightChangeLogUrl;

    @Autowired
    private StringRedisTemplate staRedis;

    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    @Autowired
    private TParamRepository tParamRepository;

    @Autowired
    private TCustomRepository tCustomRepository;

    @Autowired
    private  TFlightCodeRepository flightCodeRepository;

    @Autowired
    private TFlightCodeTemporaryRepository flightCodeTemporaryRepository;

    private final static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    /**
     * @Description: 新增 , 修改 , 删除   航班
     * @Param: [inTFlight]
     * @return: void
     * @Author: XiuHongXin
     * @Date: 2019/1/28
     */
    public synchronized int flightUpdateOrAddOrDelete(InTFlight inTFlight) {
        atomicBoolean.set(false);
        inTFlight.setFlgtAirportCode("2901");
        try {

            System.out.println("start----   " + Thread.currentThread().getId() + " ---findByFlgtFfid->" + inTFlight.getFlgtFfid() + "------" + DateUtil.getCurrentDateTimeStr());
            tFlightRepository.flush();
            TFlight tFlight = tFlightRepository.findByFlgtFfid(inTFlight.getFlgtFfid());
            if (StringUtils.isEmpty(inTFlight.getFlgtAdid())) {
                //只有进港才会触发
                if ("A".equals(inTFlight.getFlgtAdid())) {
                    if (!StringUtils.isEmpty(inTFlight.getErrorCode())) {
                        if ("DV".equals(inTFlight.getErrorCode()) || "FH".equals(inTFlight.getErrorCode())) {
                            if (tFlight != null) {

                                //flgt_d_etot 预计起飞时间
                                if (null == tFlight.getFlgtDEtot()) {
                                    if (null != inTFlight.getFlgtDEtot()) {
                                        // 说明可能是备降之后在起飞 取正常状态 然后放入 预计到达时间(前站)
                                        inTFlight.setFlgtFtyp("OT");//放入正常状态
                                    } else {
                                        //给inTFlight 赋异常状态
                                        // 刚刚迫降到长春 , 清空预计起飞  和 机位
                                        inTFlight.setFlgtFtyp(inTFlight.getErrorCode());
                                    }
                                } else {
                                    //说明备降之后在起飞 取正常状态 然后放入 预计到达时间(前站)
                                    if (tFlight.getFlgtDEtot().getTime() != inTFlight.getFlgtDEtot().getTime()) {
                                        inTFlight.setFlgtFtyp("OT");//放入正常状态
                                    } else {
                                        // 保持异常状态
                                        //给inTFlight 赋异常状态
                                        // 刚刚迫降到长春 , 清空预计起飞  和 机位
                                        inTFlight.setFlgtFtyp(inTFlight.getErrorCode());
                                    }
                                }
                            }
                        } else {
                            //制空计飞 预飞 实飞 时间
                            inTFlight.setFlgtDAtot(null);
                            inTFlight.setFlgtDEtot(null);
                            //inTFlight.setFlgtDStot(null);
                        }
                    }
                }
            }
            // 判断当前实体中的 航班类型是不是正常 如果是
            if ("DV".equals(inTFlight.getFlgtFtyp()) || "FH".equals(inTFlight.getFlgtFtyp())) {
                //flgt_a_etot
                inTFlight.setFlgtAEtot(null);
                inTFlight.setFlgtPlacecode(null);
            }
            //写死存入所属机场代码 , 所属机场区域代码
            if(StringUtils.isEmpty(inTFlight.getFlgtAirportCode())){
                inTFlight.setFlgtAirportCode("2901");
            }
            inTFlight.setFlgtAptareaCode("");
            // 垮库查询获取调度员ID
            List<TStaff> tStaffs1 = tStaffRepository.findByStaffAirportCode(inTFlight.getFlgtAirportCode());
            String userId = "";
            if (tStaffs1 != null && tStaffs1.size() > 0) {
                // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
                int i = 0;
                for (TStaff myStaff : tStaffs1) {
                    if (i == 0) {
                        userId = myStaff.getStaffId();
                    }
                    if (i > 0) {
                        userId = userId + "," + myStaff.getStaffId();
                    }
                    i++;
                }
            }

            if (tFlight == null) {
                //放入主键ID
                String uuid = UUID.randomUUID().toString();
                inTFlight.setFlgtId(uuid);
                TFlight newTFlight = new TFlight();
                ModelAssistant.copyProperties(inTFlight, newTFlight);
                if (newTFlight != null) {
                    Date dsDate = null;
                    if (!StringUtils.isEmpty(newTFlight.getFlgtDStot())) {
                        dsDate = newTFlight.getFlgtDStot();
                    }
                    Date cur = new Date();// 取时间
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(cur);
                    calendar.set(Calendar.HOUR_OF_DAY, 4);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date dayStart = calendar.getTime();
                    calendar.add(calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 4);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date dayEnd = calendar.getTime();
                    // 该航班是今天的新添加的航班，插入序号最大值+1
                    if (dsDate != null && dayStart.before(dsDate) && dayEnd.after(dsDate)) {
                        if ("D".equals(newTFlight.getFlgtAdid())) {
                            int flightMaxNum = this.getFlightMaxNum();
                            newTFlight.setFlgtNum(flightMaxNum);
                        } else {
                            newTFlight.setFlgtNum(0);
                        }
                    }
                    //存入信息 (经停或者机场等信息)
                    pingTFlight(newTFlight);
                    getFlightLine(true, userId, newTFlight, inTFlight.getSharedFfidStr(), inTFlight.getLinkFfid(),inTFlight,null);
                }
            } else {
                if (tFlight != null) {
                    TFlight old = new TFlight();
                    ModelAssistant.copyProperties(tFlight, old);
                    //针对同一秒推送两条的情况做特殊处理
                    if (!StringUtils.isEmpty(tFlight.getFlgtFtyp()) && "FX".equals(tFlight.getFlgtFtyp())) {
                        if("SH".equals(inTFlight.getFlgtFtyp())){
                            Thread.sleep(1000);
                        }
                    }
                    List<Map<String, Object>> compare = CompareObjectUtil.compare(tFlight, inTFlight);
                    if (null == compare || compare.size() == 0) {
                        System.out.println("-----发现无变化:->" + JSON.toJSONString(inTFlight));
                        return 1;
                    }
                    if(!StringUtils.isEmpty(inTFlight.getFlgtRegn())){
                        if(!inTFlight.getFlgtRegn().equals(old.getFlgtRegn())){
                            atomicBoolean.set(true);
                        }
                    }
                    inTFlight.setFlgtId(null);
                    TFlight tFlightPublic = new TFlight();
                    ModelAssistant.copyProperties(tFlight, tFlightPublic);
                    System.out.println("old->201L------" + JSON.toJSONString(tFlightPublic));
                   /* if (!StringUtils.isEmpty(tFlightPublic.getFlgtFtyp())) {
                        if ("FX".equals(tFlightPublic.getFlgtFtyp()) || "CX".equals(tFlightPublic.getFlgtFtyp()) || "NO".equals(tFlightPublic.getFlgtFtyp())) {
                            if (!StringUtils.isEmpty(inTFlight.getFlgtFtyp()) && ("FX".equals(inTFlight.getFlgtFtyp()) || "CX".equals(inTFlight.getFlgtFtyp()) || "NO".equals(inTFlight.getFlgtFtyp()))) {
                                tTaskRepository.updateTaskStuts(0, tFlightPublic.getFlgtId());
                            }
                        }
                    }*/
                    //计算实际到达时间 和数据发送时间 相差秒数

                    if (null == tFlight.getFlgtAAtot() && null != inTFlight.getFlgtAAtot()) {
                        Integer integer = DateUtil.compareDate(inTFlight.getFlgtAAtot());
                        tFlight.setFlgtChockFuel(integer);
                    }
                    if (null == tFlight.getFlgtDAtot() && null != inTFlight.getFlgtDAtot()) {
                        Integer integer = DateUtil.compareDate(inTFlight.getFlgtDAtot());
                        tFlight.setFlgtTakeoffFuel(integer);
                    }

                    ModelAssistant.copyProperties(inTFlight, tFlight);

                    if (!org.apache.commons.lang3.StringUtils.isEmpty(tFlight.getFlgtPlacecode())) {
                        Map<String, Object> placeCode = pingsql(tFlight.getFlgtPlacecode(), "2901");
                        if (placeCode != null) {
                            if (!org.apache.commons.lang3.StringUtils.isEmpty(String.valueOf(placeCode.get("placeCodeType")))) {
                                if (String.valueOf(placeCode.get("placeCodeType")).startsWith("Y")) {
                                    tFlight.setFlgtFnflag("F");
                                } else {
                                    tFlight.setFlgtFnflag("N");
                                }
                            }
                            if (!org.apache.commons.lang3.StringUtils.isEmpty(String.valueOf(placeCode.get("fontColor")))) {
                                tFlight.setFlgtGate(String.valueOf(placeCode.get("fontColor")));
                            }
                        }
                    }
                    //存入信息 (经停或者机场等信息)
                    pingTFlight(tFlight);
                    //写死存入所属机场代码 , 所属机场区域代码
                    tFlight.setFlgtAirportCode("2901");
                    tFlight.setFlgtAptareaCode("");
                    TAirportCode urc = tAirportCodeRepository.findOne(tFlight.getFlgtAirportCode());
                    //取航油内部编码
                    if (urc != null && !StringUtils.isEmpty(urc.getApcdCnafAirportCode())) {
                        //格式化后的当前系统日期
                        String format = DateFormat.getDateInstance(DateFormat.MEDIUM).format(tFlight.getFlgtFlop());
                        try {
                            tFlight.setFlgtFlop(DateUtil.getDateByString(format));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Date dsDate = null;
                    if (!StringUtils.isEmpty(tFlight.getFlgtDStot())) {
                        dsDate = tFlight.getFlgtDStot();
                    }
                    Date cur = new Date();// 取时间
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(cur);
                    calendar.set(Calendar.HOUR_OF_DAY, 4);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    //  System.out.println("开始时间：" + calendar.getTime());
                    Date dayStart = calendar.getTime();
                    calendar.add(calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 4);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    // System.out.println("结束时间：" + calendar.getTime());
                    Date dayEnd = calendar.getTime();
                    // 该航班是今天的新添加的航班，插入序号最大值+1
                    if (dsDate != null && dayStart.before(dsDate) && dayEnd.after(dsDate)) {
                        if (tFlight.getFlgtNum() == null) {
                            if ("D".equals(tFlight.getFlgtAdid())) {
                                int flightMaxNum = this.getFlightMaxNum();
                                tFlight.setFlgtNum(flightMaxNum);
                            } else {
                                tFlight.setFlgtNum(0);
                            }
                        }
                    }
                    TFlight newFlight = getFlightLine(false, userId, tFlight, inTFlight.getSharedFfidStr(), inTFlight.getLinkFfid(),inTFlight,old);
                    System.out.println( "getFlightLine 后 --->" + JSON.toJSONString(newFlight) );
                    TFlight linkFlight = null;
                    //TODO 先处理本场航班
                    if (!StringUtils.isEmpty(tFlight.getFlgtAdid())) {
                        if ("D".equals(tFlight.getFlgtAdid())) {
                            if (StringUtils.isEmpty(tFlight.getFlgtLinkFlno())) {
                                tFlight.setFlgtGame("Y");//本场
                            } else {
                                linkFlight = tFlightRepository.findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(
                                        tFlight.getFlgtLinkFlno(), tFlight.getFlgtLinkFlop(), tFlight.getFlgtRepeat(), tFlight.getFlgtLinkRepeat(),
                                        tFlight.getFlgtAirportCode(), "A"
                                );
                                if (null != linkFlight) {
                                    if (null != linkFlight.getFlgtAStot() && null == linkFlight.getFlgtAEtot() && null == linkFlight.getFlgtAAtot()) {
                                        // 大于一小时即为本场
                                        if (DateUtil.compareDateAbs(tFlight.getFlgtDStot(), linkFlight.getFlgtAStot())) {
                                            tFlight.setFlgtGame("Y");//本场
                                        }
                                    }
                                    if (null != linkFlight.getFlgtAStot() && null != linkFlight.getFlgtAEtot() && null == linkFlight.getFlgtAAtot()) {
                                        // 大于一小时即为本场
                                        if (DateUtil.compareDateAbs(tFlight.getFlgtDStot(), linkFlight.getFlgtAEtot())) {
                                            tFlight.setFlgtGame("Y");//本场
                                        }
                                    }
                                    if (null != linkFlight.getFlgtAStot() && null != linkFlight.getFlgtAEtot() && null != linkFlight.getFlgtAAtot()) {
                                        Date d = null;
                                        if (null != tFlight.getFlgtDStot()) {
                                            d = tFlight.getFlgtDStot();
                                            if (null != tFlight.getFlgtDEtot()) {
                                                d = tFlight.getFlgtDEtot();
                                            }
                                        }
                                        // 大于一小时即为本场
                                        if (DateUtil.compareDateAbs(d, linkFlight.getFlgtAAtot())) {
                                            tFlight.setFlgtGame("Y");//本场
                                        }
                                    }
                                }
                            }
                            tFlightRepository.flush();
                            tFlightRepository.save(tFlight);
                            entityManagerFlight.clear();
                        } else {
                            //D
                            linkFlight = tFlightRepository.findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(
                                    tFlight.getFlgtLinkFlno(), tFlight.getFlgtLinkFlop(), tFlight.getFlgtRepeat(), tFlight.getFlgtLinkRepeat(),
                                    tFlight.getFlgtAirportCode(), "D"
                            );
                            if (null != linkFlight) {
                                if (null != tFlight.getFlgtAStot() && null == tFlight.getFlgtAEtot() && null == tFlight.getFlgtAAtot()) {
                                    // 大于一小时即为本场
                                    if (DateUtil.compareDateAbs(linkFlight.getFlgtDStot(), tFlight.getFlgtAStot())) {
                                        linkFlight.setFlgtGame("Y");//本场
                                    }
                                }
                                if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null == tFlight.getFlgtAAtot()) {
                                    // 大于一小时即为本场
                                    if (DateUtil.compareDateAbs(linkFlight.getFlgtDStot(), tFlight.getFlgtAEtot())) {
                                        linkFlight.setFlgtGame("Y");//本场
                                    }
                                }
                                if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null != tFlight.getFlgtAAtot()) {
                                    Date d = null;
                                    if (null != linkFlight.getFlgtDStot()) {
                                        d = linkFlight.getFlgtDStot();
                                        if (null != linkFlight.getFlgtDEtot()) {
                                            d = linkFlight.getFlgtDEtot();
                                        }
                                    }
                                    // 大于一小时即为本场
                                    if (DateUtil.compareDateAbs(d, tFlight.getFlgtAAtot())) {
                                        linkFlight.setFlgtGame("Y");//本场
                                    }
                                }
                                tFlightRepository.flush();
                                tFlightRepository.save(linkFlight);
                                entityManagerFlight.clear();
                            }
                        }
                    }
                    //TODO 添加预计加油逻辑  使用 flgt_atax
                  /*  if (!StringUtils.isEmpty(tFlight.getFlgtAdid())) {
                        if ("A".equals(tFlight.getFlgtAdid())) {
                            //存在关联航班
                            if (!StringUtils.isEmpty(tFlight.getFlgtLinkFlno())) {
                                TFlight dLinkFlight = tFlightRepository.findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(
                                        tFlight.getFlgtLinkFlno(), tFlight.getFlgtLinkFlop(), tFlight.getFlgtRepeat(), tFlight.getFlgtLinkRepeat(),
                                        tFlight.getFlgtAirportCode(), "D"
                                );
                                //说明是关联航班
                                if (null != dLinkFlight) {
                                    //一、非本场航班
                                    if (!"Y".equals(dLinkFlight.getFlgtGame())) {
                                        //2、飞机已经前方起飞（即有 预计到达时间），则 预计加油时间 = 预计到达时间；
                                        if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null == tFlight.getFlgtAAtot()) {
                                            dLinkFlight.setFlgtAtax(tFlight.getFlgtAEtot());
                                        }

                                        //3、飞机已经到达（即有 实际到达时间），则 预计加油时间 = 实际到达时间；
                                        if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null != tFlight.getFlgtAAtot()) {
                                            dLinkFlight.setFlgtAtax(tFlight.getFlgtAAtot());
                                        }
                                    } else {
                                        //本场
                                        //2、飞机已经前方起飞（即有 预计到达时间 和 计划起飞时间），则 预计加油时间 = 计划起飞时间 - 1小时；
                                        if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null == tFlight.getFlgtAAtot()) {
                                            dLinkFlight.setFlgtAtax(DateUtil.poorDate(dLinkFlight.getFlgtDStot()));
                                        }
                                        *//**
                     3、飞机已经到达（即有 实际到达时间 和 计划起飞时间，预计起飞时间可能有），
                     若有预计起飞时间，则 预计加油时间 = 预计起飞时间 - 1小时；
                     若无预计起飞时间，则 预计加油时间 = 计划起飞时间 - 1小时；
                     *//*
                                        if (null != tFlight.getFlgtAStot() && null != tFlight.getFlgtAEtot() && null != tFlight.getFlgtAAtot()) {
                                            Date d = null;
                                            if (null != dLinkFlight.getFlgtDStot()) {
                                                d = dLinkFlight.getFlgtDStot();
                                                if (null != dLinkFlight.getFlgtDEtot()) {
                                                    d = dLinkFlight.getFlgtDEtot();
                                                }
                                            }
                                            dLinkFlight.setFlgtAtax(DateUtil.poorDate(d));
                                        }
                                    }
                                    tFlightRepository.flush();
                                    tFlightRepository.save(dLinkFlight);
                                    entityManagerFlight.clear();
                                }
                            }
                        } else {
                            // 4、单出航班，
                            if (StringUtils.isEmpty(tFlight.getFlgtLinkFlno())) {
                               *//*
                                若有预计起飞时间，则 预计加油时间 = 预计起飞时间 - 1小时；
	                            若无预计起飞时间，则 预计加油时间 = 计划起飞时间 - 1小时；
                                *//*
                                Date d = null;
                                if (null != tFlight.getFlgtDStot()) {
                                    d = tFlight.getFlgtDStot();
                                    if (null != tFlight.getFlgtDEtot()) {
                                        d = tFlight.getFlgtDEtot();
                                    }
                                }
                                tFlight.setFlgtAtax(DateUtil.poorDate(d));
                                tFlightRepository.flush();
                                tFlightRepository.save(tFlight);
                                entityManagerFlight.clear();
                            }
                        }
                    }*/
                    if (!StringUtils.isEmpty(inTFlight.getFlgtAdid()) && "D".equals(inTFlight.getFlgtAdid())) {

                        if (!StringUtils.isEmpty(inTFlight.getFlgtRegn())) {
                            TFlight byFlgtFfid = tFlightRepository.findByFlgtFfid(inTFlight.getFlgtFfid());
                            if (byFlgtFfid != null && StringUtils.isEmpty(byFlgtFfid.getFlgtRegn())) {
                                byFlgtFfid.setFlgtRegn(inTFlight.getFlgtRegn());
                                tFlightRepository.flush();
                                tFlightRepository.saveAndFlush(byFlgtFfid);
                                entityManagerFlight.clear();
                            }
                            if(byFlgtFfid != null
                                    && !StringUtils.isEmpty(inTFlight.getFlgtFtyp())
                                    && !StringUtils.isEmpty(byFlgtFfid.getFlgtFtyp())){
                                if(!inTFlight.getFlgtFtyp().equals(byFlgtFfid.getFlgtFtyp())){
                                    byFlgtFfid.setFlgtFtyp(inTFlight.getFlgtFtyp());
                                    tFlightRepository.flush();
                                    tFlightRepository.saveAndFlush(byFlgtFfid);
                                    entityManagerFlight.clear();
                                }
                            }
                        }
                    }
                    // TODO 特殊处理 解决时间问题 , 但是不会推送
                    if (!StringUtils.isEmpty(inTFlight.getFlgtAdid()) && "A".equals(inTFlight.getFlgtAdid())) {
                        if(!StringUtils.isEmpty(tFlight.getFlgtFlti()) && !"D".equals(tFlight.getFlgtFlti())){
                            if(!StringUtils.isEmpty(tFlight.getFlgtLinkFfid())){
                                TFlight flightD = tFlightRepository.findByFlgtFfid(tFlight.getFlgtLinkFfid());
                                if (flightD != null && flightD.getFlgtFlno().equals(inTFlight.getFlgtFlno())) {
                                    flightD.setFlgtFlti(tFlight.getFlgtFlti());
                                    tFlightRepository.flush();
                                    tFlightRepository.saveAndFlush(flightD);
                                    entityManagerFlight.clear();
                                }
                            }
                        }
                        if (null != inTFlight.getFlgtAAtot()) {
                            TFlight flightA = tFlightRepository.findByFlgtFfid(inTFlight.getFlgtFfid());
                            if (flightA != null && null == flightA.getFlgtAAtot()) {
                                flightA.setFlgtAAtot(inTFlight.getFlgtAAtot());
                                tFlightRepository.flush();
                                tFlightRepository.saveAndFlush(flightA);
                                entityManagerFlight.clear();
                            }
                        }
                    }
                    //准备更新操作之前 查询出原数据 和 新数据newFlight.flgtId
                    //  String jsonData = userId + "&" + JSON.toJSONString(tFlightPublic) + "&" + JSON.toJSONString(inTFlight) + "&" + JSON.toJSONString(inTFlight);
                    //  kafkaServiet.send(flightChangeLogUrl, jsonData);
                }
                TFlight catchtFlight = tFlightRepository.findByFlgtFfid(inTFlight.getFlgtFfid());
                if(catchtFlight != null ){
                    if(!StringUtils.isEmpty(inTFlight.getFlgtFtyp())){
                        catchtFlight.setFlgtFtyp(inTFlight.getFlgtFtyp());
                        tFlightRepository.flush();
                        tFlightRepository.saveAndFlush(catchtFlight);
                        entityManagerFlight.clear();
                    }
                }
            }
            entityManagerFlight.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //       System.out.println("end --- " + Thread.currentThread().getId() + " --- findByFlgtFfid->" + inTFlight.getFlgtFfid() + "------" + DateUtil.getCurrentDateTimeStr());
        return 1;
    }


    /**
     * @Description: 添加经停机场等信息
     * @Param: [tFlight]
     * @return: com.higer.read_kafka.entity.flight.TFlight
     * @Author: XiuHongXin
     * @Date: 2019/2/14
     */
    private TFlight pingTFlight(TFlight tFlight) {
        try {
            if (!StringUtils.isEmpty(tFlight.getFlgtAdid()) && "D".equals(tFlight.getFlgtAdid())) {
                Integer timeCount = 70;
                Date time = null;
                if (null != tFlight.getFlgtDStot()) {
                    time = tFlight.getFlgtDStot();
                }
                if (null != tFlight.getFlgtDEtot()) {
                    time = tFlight.getFlgtDEtot();
                }
                if (time != null) {
                    TParamEntity flight_expect_time = tParamRepository.findByPName("flight_expect_time");
                    if(null != flight_expect_time){
                        timeCount = Integer.valueOf(flight_expect_time.getValue1());
                        Date date = DateUtil.addDateMinut(time, timeCount);
                        tFlight.setFlgtAtax(date);
                    }
                }
            }
            if (!StringUtils.isEmpty(tFlight.getFlgtFlno())) {
                //SHE-PVG-BKK
                if (tFlight.getFlgtFlno().equals("HO1359")) {
                    tFlight.setFlgtVialc("SHE-PVG-BKK");
                    tFlight.setFlgtDes3C("BKK");
                }
                if (tFlight.getFlgtFlno().equals("HO1359") || tFlight.getFlgtFlno().equals("3U8580")) {
                    tFlight.setFlgtFlti("I");
                }
                if (tFlight.getFlgtFlno().equals("DR5035")){
                    tFlight.setFlgtVialc("SHE-FOC");
                    tFlight.setFlgtDes3C("FOC");
                    tFlight.setFlgtFlti("D");
                }

            }
            //存放航空公司信息
            if (!StringUtils.isEmpty(tFlight.getFlgtAl2C())) {
                TAirlinesCode one = tAirlinesCodeRepository.findOne(tFlight.getFlgtAl2C());
                if (one != null) {
                    tFlight.setFlgtAlcname(one.getAlcdArlnName());
                    if (null != one.getAlcdArlnNw() && 1 == one.getAlcdArlnNw()) {
                        tFlight.setFlgtFnflag("F");
                    }
                } else {
                    TForeignairportCode byAlcdIcaoCode = tForeignairportCodeRepository.findByAlcdIcaoCode(tFlight.getFlgtAl2C());
                    if (byAlcdIcaoCode != null) {
                        tFlight.setFlgtFnflag("F");
                    }
                }
            }
            List<String> lists = new ArrayList<String>();
            //出发机场
            if (!StringUtils.isEmpty(tFlight.getFlgtOrg3C())) {
                lists.add(tFlight.getFlgtOrg3C());
            }
            //目的地机场
            if (!StringUtils.isEmpty(tFlight.getFlgtDes3C())) {
                lists.add(tFlight.getFlgtDes3C());
            }
            //经停1
            if (!StringUtils.isEmpty(tFlight.getFlgtTrs3C1())) {
                lists.add(tFlight.getFlgtTrs3C1());
            }
            //经停2
            if (!StringUtils.isEmpty(tFlight.getFlgtTrs3C2())) {
                lists.add(tFlight.getFlgtTrs3C2());
            }
            List<TAirportCode> byIds = tAirportCodeRepository.findByIds(lists);
            byIds.forEach(tAirportCode -> {
                if(!StringUtils.isEmpty(tAirportCode.getApcdAirportProp()) && !"D".equals(tAirportCode.getApcdAirportProp())){
                    tFlight.setFlgtFlti("I");
                }
                //出发机场
                if (!StringUtils.isEmpty(tFlight.getFlgtOrg3C())) {
                    if (tAirportCode.getApcdIataCode().equals(tFlight.getFlgtOrg3C())) {
                        tFlight.setFlgtOrgnm(tAirportCode.getApcdAirportName());
                    }
                }
                //目的地机场
                if (!StringUtils.isEmpty(tFlight.getFlgtDes3C())) {
                    if (tAirportCode.getApcdIataCode().equals(tFlight.getFlgtDes3C())) {
                        tFlight.setFlgtDesnm(tAirportCode.getApcdAirportName());
                    }
                }
                //经停1
                if (!StringUtils.isEmpty(tFlight.getFlgtTrs3C1())) {
                    if (tAirportCode.getApcdIataCode().equals(tFlight.getFlgtTrs3C1())) {
                        tFlight.setFlgtTrsnm1(tAirportCode.getApcdAirportName());
                    }
                }
                //经停2
                if (!StringUtils.isEmpty(tFlight.getFlgtTrs3C2())) {
                    if (tAirportCode.getApcdIataCode().equals(tFlight.getFlgtTrs3C2())) {
                        tFlight.setFlgtTrsnm2(tAirportCode.getApcdAirportName());
                    }
                }
            });

            return tFlight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //关联航班发生改变之后 更改对应的关联航班
    private Boolean updateFlight(TFlight tFlight) {
        try {
            if ("A".equals(tFlight.getFlgtAdid())) {
                if (!StringUtils.isEmpty(tFlight.getFlgtLinkFlno())) {
                    TFlight tFlight1 = tFlightRepository.findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(
                            tFlight.getFlgtLinkFlno(), tFlight.getFlgtLinkFlop(), tFlight.getFlgtRepeat(), tFlight.getFlgtLinkRepeat(),
                            tFlight.getFlgtAirportCode(), "D"
                    );
                    if (tFlight1 != null) {
                        if (!tFlight.getFlgtFlno().equals(tFlight1.getFlgtLinkFlno())) {
                            tFlight1.setFlgtLinkFlno(tFlight.getFlgtFlno());
                            saveFlight(tFlight1);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * @Description: 共享航班 , 关联航班 拼接
     * @Param: [tFlight , SharedFfidStr]
     * @return: com.higer.read_kafka.entity.flight.TFlight
     * @Author: XiuHongXin
     * @Date: 2019/2/18
     */
    private TFlight getFlightLine(Boolean lock, String userId, TFlight tFlight, String sharedFfidStr, String linkFfid, InTFlight inTFlight , TFlight oldTflight) {
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TFlight dFlight = new TFlight();
        //首先处理航线信息(自身航线)
        // 创建一个空字符串用来装航线机场全名称
        String flgtVialcName = "";
        // 获取航线
        String flgtVialc = tFlight.getFlgtVialc();
        if (!StringUtils.isEmpty(flgtVialc)) {
            // 把航线按照-分割成每一个机场三字码
            String[] split1 = flgtVialc.split("-");
            // for循环航线数组
            for (int i = 0; i < split1.length; i++) {
                // 循环航线，根据航线中的机场三字码查询每个机场
                TAirportCode one = tAirportCodeRepository.findOne(split1[i]);
               /* if(!StringUtils.isEmpty(one.getApcdAirportProp()) && !"D".equals(one.getApcdAirportProp())){
                    tFlight.setFlgtFlti("I");
                }*/
                String airportName = "";
                if (one != null) {
                    airportName = one.getApcdAirportName();
                }
                // 如果i等于0说明是第一次进来直接赋值就行
                if (i == 0 && !org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                    flgtVialcName = airportName;
                }
                if (i == 0 && org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                    flgtVialcName = split1[i];
                }
                // 如果i>0小于数组的长度的话说明是不是第一次进来就开始拼接
                if (i > 0 && i < split1.length && !org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                    // 经停机场拼接
                    if (split1.length == 3 && i == 1) {
                        tFlight.setFlgtTrs3C1(split1[i]);
                        tFlight.setFlgtTrsnm1(airportName);
                    }
                    if (split1.length == 4 && i == 2) {
                        tFlight.setFlgtTrs3C2(split1[i]);
                        tFlight.setFlgtTrsnm2(airportName);
                    }
                    flgtVialcName = flgtVialcName + "-" + airportName;
                }
                if (i > 0 && i < split1.length && org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                    flgtVialcName = flgtVialcName + "-" + split1[i];
                }
            }
            // 把拼接好的航线全称放进航班对象中
            tFlight.setFlgtTrsnm3(flgtVialcName);
            // 判断传过来的航班如果是A
            if ("A".equals(tFlight.getFlgtAdid()) || ("D".equals(tFlight.getFlgtAdid())
                    && org.apache.commons.lang3.StringUtils.isBlank(tFlight.getFlgtLinkFlno()))) {
                // 航班拼接
                tFlight = getFlightLine(tFlight, flgtVialc);
            }
        }
        //是否是共享航班
        if (tFlight.getFlgtShareNoFlg() != null && 1 == tFlight.getFlgtShareNoFlg()) {
            //判断是否存在共享航班
            if (!StringUtils.isEmpty(sharedFfidStr)) {
                if (!StringUtils.isEmpty(tFlight.getFlgtFlno())) {
                    //说明是更新操作
                    if (!tFlight.getFlgtFlno().contains(sharedFfidStr)) { // 如果不包含 , 则拼接
                        tFlight.setFlgtFlno(tFlight.getFlgtFlno() + "/" + sharedFfidStr);
                    } else {
                        tFlight.setFlgtFlno(tFlight.getFlgtFlno());
                    }
                } else {
                    //说明是新增操作
                    tFlight.setFlgtFlno(tFlight.getFlgtFlno());
                }
            }
        }
        //处理关联航班
        if (!StringUtils.isEmpty(linkFfid)) {
            tFlight.setFlgtLinkFfid(linkFfid);
            // 判断传过来的航班如果是D并且连接航班号不能为空
            if ("D".equals(tFlight.getFlgtAdid())) {
                //根据关联航班ID 查出关联航班
                TFlight byFlgtFfid = tFlightRepository.findByFlgtFfid(linkFfid);
                // 判断不为空
                if (byFlgtFfid != null && !StringUtils.isEmpty(byFlgtFfid.getFlgtVialc())) {

                    byFlgtFfid.setFlgtLinkFfid(tFlight.getFlgtFfid());

                    //判断之前的航班 和 新推送的航班 机位号都是空的时候 判断关联
                    if (StringUtils.isEmpty(tFlight.getFlgtPlacecode())) {
                        if (!StringUtils.isEmpty(byFlgtFfid.getFlgtPlacecode())) {
                            tFlight.setFlgtPlacecode(byFlgtFfid.getFlgtPlacecode());
                        }
                    }
                    // 获取查出来的航班航线
                    String flgtVialcs = byFlgtFfid.getFlgtVialc();
                    if(!StringUtils.isEmpty(flgtVialcs)){
                        // 把航线按照-分割成数组
                        String[] split2 = tFlight.getFlgtVialc().split("-");
                        // for循环航线数组
                        for (int i = 1; i < split2.length; i++) {
                            // 把查出来的航线和传过来的航线进行拼接
                            flgtVialcs = flgtVialcs + "-" + split2[i];
                        }
                        // 把拼接完成的航线三字码赋值到将要添加的航班中
                        tFlight.setFlgtTrs3C5(flgtVialcs);
                        // 航班拼接
                        tFlight = getFlightLine(tFlight, tFlight.getFlgtTrs3C5());

                    }
                    // 连接航班日期
                    if (!StringUtils.isEmpty(tFlight.getFlgtFlop()) && !StringUtils.isEmpty(byFlgtFfid.getFlgtFlop())) {
                        //对调航班日期
                        byFlgtFfid.setFlgtLinkFlop(tFlight.getFlgtFlop());
                        tFlight.setFlgtLinkFlop(byFlgtFfid.getFlgtFlop());
                    }

                    //对调 航班号存入 关联航班号
                    byFlgtFfid.setFlgtLinkFlno(tFlight.getFlgtFlno());
                    tFlight.setFlgtLinkFlno(byFlgtFfid.getFlgtFlno());

                    Integer maxFlgtNum = findMaxFlgtNum();

                    if (maxFlgtNum == null) {
                        maxFlgtNum = 1;
                    } else {
                        maxFlgtNum = maxFlgtNum + 1;
                    }
                    //放统一的数字
                    // 连接航班连接次数
                    byFlgtFfid.setFlgtLinkRepeat(maxFlgtNum);
                    tFlight.setFlgtRepeat(maxFlgtNum);
                    // 航班连接次数
                    byFlgtFfid.setFlgtRepeat(maxFlgtNum);
                    tFlight.setFlgtLinkRepeat(maxFlgtNum);
                    //更新对应的关联航班 A
                    saveFlight(byFlgtFfid);
                } else {
                    // 把拼接完成的航线三字码赋值到将要添加的航班中
                    tFlight.setFlgtTrs3C5(tFlight.getFlgtVialc());
                    // 航班拼接
                    tFlight = getFlightLine(tFlight, tFlight.getFlgtVialc());
                }
                dFlight = tFlight;
            } else {
                //处理进港航班带关联航班的情况 A
                //根据关联航班ID 查出关联航班  D
                TFlight byFlgtFfid = tFlightRepository.findByFlgtFfid(linkFfid);
                // 判断不为空
                if (byFlgtFfid != null) {
                    byFlgtFfid.setFlgtLinkFfid(tFlight.getFlgtFfid());
                    // 获取查出来的航班航线
                    String flgtVialcs = tFlight.getFlgtVialc();
                    if(!StringUtils.isEmpty(flgtVialcs)){
                        // 把航线按照-分割成数组
                        String[] split2 = byFlgtFfid.getFlgtVialc().split("-");
                        // for循环航线数组
                        for (int i = 1; i < split2.length; i++) {
                            // 把查出来的航线和传过来的航线进行拼接
                            flgtVialcs = flgtVialcs + "-" + split2[i];
                        }
                        // 把拼接完成的航线三字码赋值到将要添加的航班中
                        byFlgtFfid.setFlgtTrs3C5(flgtVialcs);
                        // 航班byFlgtFfid接
                        byFlgtFfid = getFlightLine(byFlgtFfid, byFlgtFfid.getFlgtTrs3C5());

                        // 连接航班日期
                        if (!StringUtils.isEmpty(tFlight.getFlgtFlop()) && !StringUtils.isEmpty(byFlgtFfid.getFlgtFlop())) {
                            //对调航班日期
                            byFlgtFfid.setFlgtLinkFlop(tFlight.getFlgtFlop());
                            tFlight.setFlgtLinkFlop(byFlgtFfid.getFlgtFlop());
                        }
                        //对调 航班号存入 关联航班号
                        byFlgtFfid.setFlgtLinkFlno(tFlight.getFlgtFlno());
                        tFlight.setFlgtLinkFlno(byFlgtFfid.getFlgtFlno());

                        Integer maxFlgtNum = findMaxFlgtNum();

                        if (maxFlgtNum == null) {
                            maxFlgtNum = 1;
                        } else {
                            maxFlgtNum = maxFlgtNum + 1;
                        }
                        //放统一的数字
                        // 连接航班连接次数
                        byFlgtFfid.setFlgtLinkRepeat(maxFlgtNum);
                        tFlight.setFlgtRepeat(maxFlgtNum);
                        // 航班连接次数
                        byFlgtFfid.setFlgtRepeat(maxFlgtNum);
                        tFlight.setFlgtLinkRepeat(maxFlgtNum);
                    }
                    //更新对应的关联航班
                    saveFlight(byFlgtFfid);
                    dFlight = byFlgtFfid;
                } else {
                    // 把拼接完成的航线三字码赋值到将要添加的航班中
                    tFlight.setFlgtTrs3C5(tFlight.getFlgtVialc());
                    // 航班拼接
                    tFlight = getFlightLine(tFlight, tFlight.getFlgtVialc());
                }
            }
        }
        if ("DV".equals(inTFlight.getErrorCode()) || "FH".equals(inTFlight.getErrorCode())) {
            tFlight.setFlgtDStot(null);
        }

        //保存数据
        TFlight newFlight = saveFlight(tFlight);
        System.out.println("保存数据--->" + JSON.toJSONString(newFlight));
        // 创建SimpleDateFormat日期格式化对象
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 在此区分 新增航班还是修改航班
        Map<String, Object> webMap = new HashMap<String, Object>();
        webMap.put("staff",new TStaff());
        List<TTask> save = new ArrayList<TTask>();
        if (dFlight != null) {
            save = tTaskRepository.findByTaskFlightId(dFlight.getFlgtId());
        }
        Boolean wsflg = true;
        // 归档不发送WS
        if ("FX".equals(dFlight.getFlgtFtyp())) {
            wsflg = false;
        }
        if (lock) {
            //出港的时候 进行任务派发
            // 航班进出港等于出港的且不是非营运航班时候才创建任务
            if (Constant.CLEAR_A_PORT.equals(tFlight.getFlgtAdid())) {
                // 创建任务对象为添加任务做准备
                TTask task = new TTask();
                // 生成UUID为任务ID
                String taskId = UUID.randomUUID().toString();
                // 把生成的UUID赋值到任务对象中的任务ID里
                task.setTaskId(taskId);
                // 把航班对象中的航班ID赋值到任务对象中的航班ID里
                task.setTaskFlightId(tFlight.getFlgtId());
                // 把航班对象中的航班号赋值到任务对象中的航班号里
                task.setTaskFlightNo(tFlight.getFlgtFlno());
                // 把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                task.setTaskAirportCode(tFlight.getFlgtAirportCode());
                // 把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                task.setTaskAptareaCode(tFlight.getFlgtAptareaCode());
                task.setTaskStatus(0);
                task.setTaskContent(-1);
                // 获取当前系统时间
                Date date = new Date();
                // 接收格式化以后的时间
                String forMatTime = sim.format(date);
                try {
                    // 因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                    task.setTaskRecCreTime(sim.parse(forMatTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                TTask newTask = saveTask(task);
                if (save.size() == 0) {
                    save.add(newTask);
                }
                // 根据任务ID查出单条任务航班信息
                Map<String, Object> taskAndFlightById = getTaskAndFlightById(save.get(0).getTaskId());
                if (taskAndFlightById != null) {
                    //TODO 默认值
                    Integer flrcType = pingSingleType(taskAndFlightById);
                    setFlightAlcname(taskAndFlightById);
                    taskAndFlightById.put("flrcType", flrcType);
                    // 把要推送的航班任务对象放进Map集合中
                    webMap.put("flight", taskAndFlightById);
                    // 判断如果航班是本场的话再推送一条本场航班消息
                    if (Constant.FLGT_DGAME.equals(taskAndFlightById.get("flgtGame"))) {
                        // 把要推送的航班任务对象放进Map集合中
                        webMap.put("selfFlight", taskAndFlightById);
                    }
                    // 当天航班推送消息
                    Object flgtDStot = taskAndFlightById.get("flgtDStot");
                    if (null != flgtDStot) {
                        try {
                            if (DateUtil.isEffectiveDate(sim1.parse(String.valueOf(flgtDStot)))) {
                                if (taskAndFlightById.get("flgtNum") != null && (Integer) taskAndFlightById.get("flgtNum") > 0 && wsflg) {
                                    System.out.println("------------------" +
                                            Thread.currentThread().getId() + "808L "
                                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                            + JSON.toJSONString(webMap)
                                    );
                                    //   SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "200",
                                    //           webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                            Constant.PC_FLIGHT_ADD, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                sendMsg(webMap, tFlight, userId, wsflg);
            }
        } else {
            //TODO 提前处理 修改航班没有任务的情况
            if (Constant.CLEAR_A_PORT.equals(tFlight.getFlgtAdid())) {
                if(!StringUtils.isEmpty(tFlight.getFlgtId())){
                    List<TTask> byTaskFlightId = tTaskRepository.findByTaskFlightId(tFlight.getFlgtId());
                    if (byTaskFlightId.size() == 0) {
                        // 创建任务对象为添加任务做准备
                        TTask task = new TTask();
                        // 生成UUID为任务ID
                        String taskId = UUID.randomUUID().toString();
                        // 把生成的UUID赋值到任务对象中的任务ID里
                        task.setTaskId(taskId);
                        // 把航班对象中的航班ID赋值到任务对象中的航班ID里
                        task.setTaskFlightId(tFlight.getFlgtId());
                        // 把航班对象中的航班号赋值到任务对象中的航班号里
                        task.setTaskFlightNo(tFlight.getFlgtFlno());
                        // 把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                        task.setTaskAirportCode(tFlight.getFlgtAirportCode());
                        // 把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                        task.setTaskAptareaCode(tFlight.getFlgtAptareaCode());
                        task.setTaskStatus(0);
                        task.setTaskContent(-1);
                        // 获取当前系统时间
                        Date date = new Date();
                        // 接收格式化以后的时间
                        String forMatTime = sim.format(date);
                        try {
                            // 因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                            task.setTaskRecCreTime(sim.parse(forMatTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // 添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                        tTaskRepository.flush();
                        TTask newTask = tTaskRepository.save(task);
                        entityManagerFlight.clear();
                        if (save.size() == 0) {
                            save.add(newTask);
                        }
                    }else{
                        save = byTaskFlightId;
                    }
                }
            }
            if (save.size() > 0) {
                //   System.out.println("Thread.currentThread().getId()----" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "----------------------------进入修改推送---------------------------");
                Boolean finalWsflg = wsflg;
                List<TTask> finalSave = save;
                save.forEach(tTask -> {
                    // 航班更新redis 推送信息
                    // 根据任务ID查出单条任务航班信息
                    Map<String, Object> taskAndFlightById = getTaskAndFlightById(tTask.getTaskId());
                    if (taskAndFlightById != null) {
                        Date flgtAAtot = newFlight.getFlgtAAtot();
                        Object flgtAAtot1 = taskAndFlightById.get("flgtAAtot");
                        if (flgtAAtot1 == null && flgtAAtot != null) {
                            taskAndFlightById.put("flgtAAtot", flgtAAtot);
                            newFlight.setFlgtAAtot(newFlight.getFlgtAAtot());
                            TFlight newFlight1 = saveFlight(newFlight);
                            // System.out.println("保存数据--->未保存成功 实达时间  做完特殊处理的数据 " + JSON.toJSONString(newFlight1) + "----====-000---" + JSON.toJSONString(taskAndFlightById));
                        }
                        Integer flrcType = pingSingleType(taskAndFlightById);
                        taskAndFlightById.put("flrcType", flrcType);
                        //设置飞机号码信息
                        setFlightAlcname(taskAndFlightById);
                        // 把要推送的航班任务对象放进Map集合中
                        webMap.put("flight", taskAndFlightById);
                        // 判断如果航班是本场的话再推送一条本场航班消息
                        if (Constant.FLGT_DGAME.equals(taskAndFlightById.get("flgtGame"))) {
                            // 把要推送的航班任务对象放进Map集合中
                            webMap.put("selfFlight", taskAndFlightById);
                        }
                        Object flgtDStot = taskAndFlightById.get("flgtDStot");
                        if (null != flgtDStot) {
                            try {
                                if (DateUtil.isEffectiveDate(sim1.parse(String.valueOf(flgtDStot)))) {
                                    // 当天航班推送消息
                                    if (taskAndFlightById.get("flgtNum") != null && Integer.valueOf(String.valueOf(taskAndFlightById.get("flgtNum"))) > 0) {
                                        // 推送给调度员
                                        if (finalWsflg) {
                                            if(!StringUtils.isEmpty(inTFlight.getFlgtRegn())){
                                                if(atomicBoolean.get()){
                                                    System.out.println(" oldtFlight -> " + JSON.toJSONString(oldTflight)  + " <- inTFlight ->"
                                                            + JSON.toJSONString(inTFlight) + " <- finalSave ->" + JSON.toJSONString(finalSave));
                                                    if(finalSave.size() == 1){
                                                        Optional<TTask> any = finalSave.stream().filter(o -> o.getTaskStatus() >= 7 && o.getTaskStatus() > 4).findAny();
                                                        if (any.isPresent()) {
                                                            System.out.println("推送type 81 -> 原数据 " + JSON.toJSONString(inTFlight));
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    "81", webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                        }else{
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                        }
                                                    } else if(finalSave.size() > 1){
                                                        List<TTask> collect = finalSave.stream().filter(o -> o.getTaskStatus() == 7).collect(Collectors.toList());
                                                        if(collect.size() > 1){
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    "81", webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                        }else{
                                                            System.out.println("2推送type 81 -> 原数据 " + JSON.toJSONString(inTFlight));
                                                            /*SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    "81", webMap);*/
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                    Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                        }
                                                    }else{
                                                        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                                Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                    }
                                                }else{
                                                    SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                            Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                }
                                            }else{
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT,
                                                        Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                            }

                                        }
                                        // 推送给加油员
                                        // 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
                                        if (1 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                || 3 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                || 4 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                || 5 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))) {
                                            if (finalWsflg) {
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, String.valueOf(taskAndFlightById.get("taskOpeStaffId")), Constant.TASKFLIGHT,
                                                        Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---893L---" + JSON.toJSONString(webMap));
                                            }
                                        }
                                        // 航班进出港等于进港的时候WS推送
                                        if (Constant.CLEAR_A_ENTER.equals(newFlight.getFlgtAdid())) {
                                            if (!StringUtils.isEmpty(userId)) {
                                                if (taskAndFlightById != null && (!StringUtils.isEmpty(taskAndFlightById.get("flgtFlno")))) {
                                                    if (taskAndFlightById.get("flgtNum") != null && Integer.valueOf(String.valueOf(taskAndFlightById.get("flgtNum"))) > 0) {
                                                        if (taskAndFlightById != null) {
                                                            taskAndFlightById.put("flrcType", 3);
                                                            if (!StringUtils.isEmpty(String.valueOf(taskAndFlightById.get("taskOpeStaffId")))) {
                                                                TStaff taskOpeStaffId = tStaffRepository.findByStaffId(String.valueOf(taskAndFlightById.get("taskOpeStaffId")));
                                                                if (taskOpeStaffId != null) {
                                                                    taskAndFlightById.put("taskOpeStaffName", taskOpeStaffId.getStaffName());
                                                                }
                                                            }
                                                        }
                                                        // 把要推送的航班任务对象放进Map集合中
                                                        webMap.put("flight", taskAndFlightById);
                                                        if (finalWsflg) {
                                                            //   SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "200",
                                                            //            webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "18",
                                                                    webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                            System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---963L---" + JSON.toJSONString(webMap));
                                                        }
                                                        // 推送给加油员
                                                        // 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
                                                        if (1 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                || 3 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                || 4 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))
                                                                || 5 == Integer.valueOf(String.valueOf(taskAndFlightById.get("taskStatus")))) {
                                                            if (finalWsflg) {
                                                                SendMsg2Redis.testDingYue(stringRedisTemplate, String.valueOf(taskAndFlightById.get("taskOpeStaffId")), Constant.TASKFLIGHT,
                                                                        Constant.PC_FLIGHT_UPDATE, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                                System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---975L---" + JSON.toJSONString(webMap));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        // 地图WS信息推送
                                        if (newFlight.getFlgtNum() != null && newFlight.getFlgtNum() > 0) {
                                            webMap.put("flight", newFlight);
                                            if (finalWsflg) {
                                                SendMsg2Redis.testDingYue(stringRedisTemplate, null, newFlight.getFlgtAirportCode() + ":" + newFlight.getFlgtAptareaCode(),
                                                        Constant.PC_FLIGHT_ADD, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                                System.out.println("=====" + newFlight.getFlgtFfid() + "-----" + Thread.currentThread().getId() + "---988L---" + JSON.toJSONString(webMap));
                                            }
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        return newFlight;
    }

    /**
     * 航线拼接
     *
     * @param flight
     * @param flightLine
     * @return
     */
    private TFlight getFlightLine(TFlight flight, String flightLine) {
        try {
            if (!StringUtils.isEmpty(flightLine)) {
                // 再把拼接好的航线三字码分割成数组
                String[] split3 = flightLine.split("-");
                // 用来存放全称
                String flgtVialcNames = null;
                // 用来存放简称
                String flgtVialcNamess = null;
                // for循环航线数组
                for (int i = 0; i < split3.length; i++) {
                    // 循环航线，根据航线中的机场三字码查询每个机场全称T_AIRPORT_CODE
                    String airportName = "";
                    // 循环航线，根据航线中的机场三字码查询每个机场简称
                    String airportNames = "";
                    TAirportCode byApcdIataCode = tAirportCodeRepository.findByApcdIataCode(split3[i]);
                    if (byApcdIataCode != null) {
                        airportName = byApcdIataCode.getApcdAirportName();
                        airportNames = byApcdIataCode.getApcdAirportNameS();
                    }
                    // 如果i等于0说明是第一次进来直接赋值就行
                    if (i == 0 && !org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                        // 赋值全称
                        flgtVialcNames = airportName;
                    }
                    if (i == 0 && org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                        // 赋值全称
                        flgtVialcNames = split3[i];
                    }
                    // 如果i等于0说明是第一次进来直接赋值就行
                    if (i == 0 && !org.apache.commons.lang3.StringUtils.isBlank(airportNames)) {
                        // 赋值简称
                        flgtVialcNamess = airportNames;
                    }
                    if (i == 0 && org.apache.commons.lang3.StringUtils.isBlank(airportNames)) {
                        // 赋值简称
                        flgtVialcNamess = split3[i];
                    }
                    // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                    if (i > 0 && i < split3.length && !org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                        // 拼接全称
                        flgtVialcNames = flgtVialcNames + "-" + airportName;
                    }
                    if (i > 0 && i < split3.length && org.apache.commons.lang3.StringUtils.isBlank(airportName)) {
                        // 拼接全称
                        flgtVialcNames = flgtVialcNames + "-" + split3[i];
                    }
                    // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                    if (i > 0 && i < split3.length && !org.apache.commons.lang3.StringUtils.isBlank(airportNames)) {
                        // 拼接简称
                        flgtVialcNamess = flgtVialcNamess + "-" + airportNames;
                    }
                    if (i > 0 && i < split3.length && org.apache.commons.lang3.StringUtils.isBlank(airportNames)) {
                        // 拼接简称
                        flgtVialcNamess = flgtVialcNamess + "-" + split3[i];
                    }
                }
                // 把拼接完成的航线全称赋值到将要添加的航班中
                flight.setFlgtTrsnm5(flgtVialcNames);
                // 把拼接完成的航线简称赋值到将要添加的航班中
                flight.setFlgtTrsnm4(flgtVialcNamess);
            }
            return flight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: 根据任务ID查询任务单条记录和航班
     * @Param: [taskId]
     * @Author: XiuHongXin
     * @Date: 2019/2/20
     */
    private Map<String, Object> getTaskAndFlightById(String taskId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT\n" +
                    "\ttask.task_id AS taskId,\n" +
                    "\ttask.task_ope_staff_id AS taskOpeStaffId,\n" +
                    "\ttask.task_content AS taskContent,\n" +
                    "\ttask.task_status AS taskStatus,\n" +
                    "\ttask.task_asg_time AS taskAsgTime,\n" +
                    "\ttask.task_acc_time AS taskAccTime,\n" +
                    "\ttask.task_chag_sta_time AS taskChagStaTime,\n" +
                    "\ttask.task_chag_end_time AS taskChagEndTime,\n" +
                    "\ttask.task_done_time AS taskDoneTime,\n" +
                    "\ttask.task_fuel_recpt_no AS taskFuelRecptNo,\n" +
                    "\ttask.task_vehi_no AS taskVehiNo,\n" +
                    "\ttask.task_cre_staff_id AS taskCreStaffId,\n" +
                    "\ttask.task_starmark AS taskStarmark,\n" +
                    "\ttask.task_rec_cre_time AS taskRecCreTime,\n" +
                    "\tflight.flgt_id AS flgtId,\n" +
                    "\tflight.flgt_ffid AS flgtFfid,\n" +
                    "\tflight.flgt_airport_code AS flgtAirportCode,\n" +
                    "\tflight.flgt_aptarea_code AS flgtAptareaCode,\n" +
                    "\tflight.flgt_flno AS flgtFlno,\n" +
                    "\tflight.flgt_flop AS flgtFlop,\n" +
                    "\tflight.flgt_acname AS flgtAcname,\n" +
                    "\tflight.flgt_regn AS flgtRegn,\n" +
                    "\tflight.flgt_placecode AS flgtPlacecode,\n" +
                    "\tflight.flgt_al2c AS flgtAl2c,\n" +
                    "\tflight.flgt_alcname AS flgtAlcname,\n" +
                    "\tflight.flgt_d_stot AS flgtDStot,\n" +
                    "\tflight.flgt_d_etot AS flgtDEtot,\n" +
                    "\tflight.flgt_d_atot AS flgtDAtot,\n" +
                    "\tflight.flgt_org3c AS flgtOrg3c,\n" +
                    "\tflight.flgt_orgnm AS flgtOrgnm,\n" +
                    "\tflight.flgt_trs3c1 AS flgtTrs3c1,\n" +
                    "\tflight.flgt_trs3c2 AS flgtTrs3c2,\n" +
                    "\tflight.flgt_trs3c3 AS flgtTrs3c3,\n" +
                    "\tflight.flgt_trs3c4 AS flgtTrs3c4,\n" +
                    "\tflight.flgt_trs3c5 AS flgtTrs3c5,\n" +
                    "\tflight.flgt_trsnm1 AS flgtTrsnm1,\n" +
                    "\tflight.flgt_trsnm2 AS flgtTrsnm2,\n" +
                    "\tflight.flgt_trsnm3 AS flgtTrsnm3,\n" +
                    "\tflight.flgt_trsnm4 AS flightValic,\n" +
                    "\tflight.flgt_trsnm5 AS flgtVialc,\n" +
                    "\tflight.flgt_des3c AS flgtDes3c,\n" +
                    "\tflight.flgt_desnm AS flgtDesnm,\n" +
                    "\tflight.flgt_adid AS flgtAdid,\n" +
                    "\tflight.flgt_flti AS flgtFlti,\n" +
                    "\tfl.flgt_ftyp AS aFlgtFtyp,\n" +
                    "\tflight.flgt_ftyp AS flgtFtyp,\n" +
                    "\tflight.flgt_proxy AS flgtProxy,\n" +
                    "\tfl.flgt_flno AS flgtLinkFlno,\n" +
                    "\tflight.flgt_fnflag AS flgtFnflag,\n" +
                    "\tflight.flgt_game AS flgtGame,\n" +
                    "\tflight.flgt_chocks_in AS flgtChocksIn,\n" +
                    "\tflight.flgt_chocks_out AS flgtChocksOut,\n" +
                    "\tflight.flgt_vip AS flgtVip,\n" +
                    "\tflight.flgt_num AS flgtNum,\n" +
                    "\ttainscode.alcd_arln_name AS alcdArlnName,\n" +
                    "\ttainscode.alcd_arln_name_s AS alcdArlnNames,\n" +
                    "\ttaport.apcd_airport_name AS apcdSAirportName,\n" +
                    "\ttaport.apcd_airport_name_s AS apcdSAirportNames,\n" +
                    "\ttaport1.apcd_airport_name AS apcdEAirportName,\n" +
                    "\ttaport1.apcd_airport_name_s AS apcdEAirportNames,\n" +
                    "\tfl.flgt_org3c AS beorg3c,\n" +
                    "\tfl.flgt_a_stot AS flgtAStot,\n" +
                    "\tfl.flgt_a_etot AS flgtAEtot,\n" +
                    "\tfl.flgt_a_atot AS flgtAAtot,\n" +
                    "\tfl.flgt_placecode AS flgtPlacecodeIn\n" +
                    "FROM\n" +
                    "\tT_TASK AS task,\n" +
                    "\tT_FLIGHT AS flight\n" +
                    "LEFT JOIN T_AIRLINES_CODE tainscode ON tainscode.alcd_icao_code = flight.flgt_al2c\n" +
                    "LEFT JOIN T_AIRPORT_CODE taport ON taport.apcd_iata_code = flight.flgt_org3c\n" +
                    "LEFT JOIN T_AIRPORT_CODE taport1 ON taport1.apcd_iata_code = flight.flgt_des3c\n" +
                    "LEFT JOIN T_FLIGHT fl\n" +
                    "        ON fl.flgt_adid = 'A'\n" +
                    "        AND flight.flgt_link_ffid = fl.flgt_ffid\n" +
                    "WHERE\n" +
                    "\tflight.flgt_adid = \"D\" and\n" +
                    "\ttask.task_flight_id = flight.flgt_id\n" );
            if (!StringUtils.isEmpty(taskId)) {
                sql.append("and task.task_id =\"" + taskId + "\"");
            }
            sql.append(
                    " order by flgtAAtot desc limit 1\n");

            //System.out.println("-------" + sql.toString());

            // EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            if (list.size() > 0) {
                return list.get(0);
            }
            //EntityManagerFactoryUtils.closeEntityManager(entityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Description: 推送各种信息的方法
     * @Param: []
     * @return: java.lang.Boolean
     * @Author: XiuHongXin
     * @Date: 2019/2/20
     */
    private Map<String, Object> sendMsg(Map<String, Object> webMap, TFlight tFlight, String userId, Boolean wsflg) {
        try {
            SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 航班进出港等于进港的时候WS推送
            if (Constant.CLEAR_A_ENTER.equals(tFlight.getFlgtAdid())) {
                if (!StringUtils.isEmpty(userId)) {
                    // 根据任务ID查出单条任务航班信息
                    Map<String, Object> taskAndFlightByFFId = getTaskAndFlightByFFId(tFlight.getFlgtFfid());
                    if (taskAndFlightByFFId != null && (!org.apache.commons.lang3.StringUtils.isBlank((String) taskAndFlightByFFId.get("flgtFlno")))) {
                        Object flgtDStot = taskAndFlightByFFId.get("flgtDStot");
                        if (null != flgtDStot) {
                            if (DateUtil.isEffectiveDate(sim1.parse(String.valueOf(flgtDStot)))) {
                                if (taskAndFlightByFFId.get("flgtNum") != null && (Integer) taskAndFlightByFFId.get("flgtNum") > 0) {
                                    // 把要推送的航班任务对象放进Map集合中
                                    webMap.put("flight", taskAndFlightByFFId);
                                    if (wsflg) {
                                        //     SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "200",
                                        //           webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "17",
                                                webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 地图WS信息推送
            if (tFlight.getFlgtNum() != null && tFlight.getFlgtNum() > 0) {
                webMap.put("flight", tFlight);
                if (wsflg) {
                    SendMsg2Redis.testDingYue(stringRedisTemplate, null, tFlight.getFlgtAirportCode() + ":" + tFlight.getFlgtAptareaCode(),
                            Constant.PC_FLIGHT_ADD, webMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }
            return webMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据进港航班FFID查询任务单条记录和航班
    private Map<String, Object> getTaskAndFlightByFFId(String ffid) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT\n" +
                    "\ttask.task_id AS taskId,\n" +
                    "\ttask.task_ope_staff_id AS taskOpeStaffId,\n" +
                    "\ttask.task_content AS taskContent,\n" +
                    "\ttask.task_status AS taskStatus,\n" +
                    "\ttask.task_asg_time AS taskAsgTime,\n" +
                    "\ttask.task_acc_time AS taskAccTime,\n" +
                    "\ttask.task_chag_sta_time AS taskChagStaTime,\n" +
                    "\ttask.task_chag_end_time AS taskChagEndTime,\n" +
                    "\ttask.task_done_time AS taskDoneTime,\n" +
                    "\ttask.task_fuel_recpt_no AS taskFuelRecptNo,\n" +
                    "\ttask.task_vehi_no AS taskVehiNo,\n" +
                    "\ttask.task_cre_staff_id AS taskCreStaffId,\n" +
                    "\ttask.task_starmark AS taskStarmark,\n" +
                    "\ttask.task_rec_cre_time AS taskRecCreTime,\n" +
                    "\tflight.flgt_id AS flgtId,\n" +
                    "\tflight.flgt_ffid AS flgtFfid,\n" +
                    "\tflight.flgt_airport_code AS flgtAirportCode,\n" +
                    "\tflight.flgt_aptarea_code AS flgtAptareaCode,\n" +
                    "\tflight.flgt_flno AS flgtFlno,\n" +
                    "\tflight.flgt_flop AS flgtFlop,\n" +
                    "\tflight.flgt_acname AS flgtAcname,\n" +
                    "\tflight.flgt_regn AS flgtRegn,\n" +
                    "\tflight.flgt_placecode AS flgtPlacecode,\n" +
                    "\tflight.flgt_al2c AS flgtAl2c,\n" +
                    "\tflight.flgt_alcname AS flgtAlcname,\n" +
                    "\tflight.flgt_d_stot AS flgtDStot,\n" +
                    "\tflight.flgt_d_etot AS flgtDEtot,\n" +
                    "\tflight.flgt_d_atot AS flgtDAtot,\n" +
                    "\tflight.flgt_org3c AS flgtOrg3c,\n" +
                    "\tflight.flgt_orgnm AS flgtOrgnm,\n" +
                    "\tflight.flgt_trs3c1 AS flgtTrs3c1,\n" +
                    "\tflight.flgt_trs3c2 AS flgtTrs3c2,\n" +
                    "\tflight.flgt_trs3c3 AS flgtTrs3c3,\n" +
                    "\tflight.flgt_trs3c4 AS flgtTrs3c4,\n" +
                    "\tflight.flgt_trs3c5 AS flgtTrs3c5,\n" +
                    "\tflight.flgt_trsnm1 AS flgtTrsnm1,\n" +
                    "\tflight.flgt_trsnm2 AS flgtTrsnm2,\n" +
                    "\tflight.flgt_trsnm3 AS flgtTrsnm3,\n" +
                    "\tflight.flgt_trsnm4 AS flightValic,\n" +
                    "\tflight.flgt_trsnm5 AS flgtVialc,\n" +
                    "\tflight.flgt_des3c AS flgtDes3c,\n" +
                    "\tflight.flgt_desnm AS flgtDesnm,\n" +
                    "\tflight.flgt_adid AS flgtAdid,\n" +
                    "\tflight.flgt_flti AS flgtFlti,\n" +
                    "\tflight.flgt_ftyp AS flgtFtyp,\n" +
                    "\tflight.flgt_proxy AS flgtProxy,\n" +
                    "\tflight.flgt_fnflag AS flgtFnflag,\n" +
                    "\tflight.flgt_game AS flgtGame,\n" +
                    "\tflight.flgt_chocks_in AS flgtChocksIn,\n" +
                    "\tflight.flgt_chocks_out AS flgtChocksOut,\n" +
                    "\tflight.flgt_vip AS flgtVip,\n" +
                    "\tflight.flgt_num AS flgtNum,\n" +
                    "\tflight.flgt_repeat AS flgtRepeat,\n" +
                    "\tflight.flgt_link_flop AS flgtLinkFlop,\n" +
                    "\tflight.flgt_link_repeat AS flgtLinkRepeat,\n" +
                    "\tflight.flgt_otc AS flgtOtc,\n" +
                    "\ttf2.flgt_flno AS flgtLinkFlno,\n" +
                    "\ttf2.flgt_org3c AS beorg3c,\n" +
                    "\ttf2.flgt_a_stot AS flgtAStot,\n" +
                    "\ttf2.flgt_a_etot AS flgtAEtot,\n" +
                    "\ttf2.flgt_a_atot AS flgtAAtot,\n" +
                    "\ttf2.flgt_placecode AS flgtPlacecodeIn\n" +
                    "FROM\n" +
                    "\tT_FLIGHT tf2\n" +
                    "LEFT JOIN T_FLIGHT flight ON flight.flgt_adid = 'D'\n" +
                    "AND flight.flgt_link_ffid = tf2.flgt_ffid\n" +
                    "LEFT JOIN T_TASK task ON task.task_flight_id = flight.flgt_id\n" +
                    "WHERE\n" +
                    "\ttf2.flgt_adid = 'A'");
            if (!StringUtils.isEmpty(ffid)) {
                sql.append("AND tf2.flgt_ffid =\"" + ffid + "\"");
            }
            //EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            //EntityManagerFactoryUtils.closeEntityManager(entityManager);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    public void flight4Change() {
        try {
            Set<String> keys1 = staRedis.keys("login:" + "*:" + "*" + ":" + ":*");
            if (keys1 != null && keys1.size() > 0) {
                keys1.forEach(s -> {
                    staRedis.delete(s);
                });
            }
            try {
                tFlightRepository.updateFlightNumNull();
            } catch (Exception e) {
                System.out.println("-------updateFlightNumNull");
                throw new RuntimeException(e);
            }
            // 创建人员对象用来存放机场所属代码
            TStaff staffInfo = new TStaff();
            staffInfo.setStaffAirportCode("2901");
            // 垮库查询获取调度员ID
            List<TStaff> tStaffs = tStaffRepository.findByStaffAirportCode(staffInfo.getStaffAirportCode());
            String userId = tStaffs.stream().map(TStaff::getStaffId).collect(Collectors.joining(","));
            // 序号追加
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String nowstr = fm.format(now);
            List<String> idList = getFlightFlgtIdList(nowstr, "2901", "");
            int j = 1;
            Date date1 = new Date();
            for (String id : idList) {
                System.out.println( "凌晨更新状态--->" + "flgtId -> " + id + " --- 序号 --> " + j);
                tFlightRepository.flush();
                try {
                    TFlight byFlgtFlopAndFlgtNum = tFlightRepository.findByFlgtFlopAndFlgtNum(date1, j);
                    if(null != byFlgtFlopAndFlgtNum){
                        j++;
                        tFlightRepository.updateFlightNum(j, id);
                        entityManagerFlight.clear();
                    }else{
                        tFlightRepository.updateFlightNum(j, id);
                        entityManagerFlight.clear();
                    }
                } catch (Exception e) {
                    tFlightRepository.updateFlightNum(j, id);
                    entityManagerFlight.clear();
                }
                j++;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, -1);//+1今天的时间加一天
            String date = fm.format(calendar.getTime());
            //获取没起飞的航班ID
            List<Map<String, Object>> flightIdList = getNotTakeOff(date, "2901", "");

            for (Map<String, Object> myFlight : flightIdList) {
                System.out.println( "凌晨更新状态2--->" + "flgtId2 -> " + (String) myFlight.get("flgtId") + " --- 序号2 --> " + j);

                tFlightRepository.flush();
                try {
                    TFlight byFlgtFlopAndFlgtNum = tFlightRepository.findByFlgtFlopAndFlgtNum(date1, j);
                    if(null != byFlgtFlopAndFlgtNum){
                        j++;
                        tFlightRepository.updateFlightNum(j, (String) myFlight.get("flgtId"));
                        entityManagerFlight.clear();
                    }else{
                        tFlightRepository.updateFlightNum(j, (String) myFlight.get("flgtId"));
                        entityManagerFlight.clear();
                    }
                } catch (Exception e) {
                    tFlightRepository.updateFlightNum(j, (String) myFlight.get("flgtId"));
                    entityManagerFlight.clear();
                }


                j++;
            }
            // 300
            SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, "300", "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw e;
        }
    }

    //获取4点到4点的航班ID列表
    private List<String> getFlightFlgtIdList(String date, String airportCode, String airportAreaCode) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select flgt_id flgtId \n" +
                    "\t\tfrom T_FLIGHT\n" +
                    "\t\twhere flgt_adid = 'D'\n" +
                    "\t\tAND flgt_d_stot >= DATE_ADD(\"" + date + "\",INTERVAL 4 HOUR)\n" +
                    "        AND flgt_d_stot < DATE_ADD(\"" + date + "\",INTERVAL 28 HOUR)\n" +
                    "      AND flgt_ffid is not null\n" +
                    "      AND flgt_ftyp <> 'NO'\n" +
                    "        AND flgt_ftyp <> 'CX'\n" +
                    "        AND flgt_ftyp <> 'DE'\n" +
                    "        AND flgt_ftyp <> 'FX'\n");
            if (!StringUtils.isEmpty(airportCode)) {
                sql.append("        AND flgt_airport_code = \"" + airportCode + "\"\n");
            }
            if (!StringUtils.isEmpty(airportAreaCode)) {
                sql.append("        AND flgt_aptarea_code = \"" + airportAreaCode + "\"\n");
            }
            sql.append("\t\tORDER BY flgt_d_stot ASC");
            //System.out.println(sql.toString());
            //  EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<String> list = new ArrayList<String>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                String flgtId = (String) row.get("flgtId");
                list.add(flgtId);
            }
            //EntityManagerFactoryUtils.closeEntityManager(entityManager);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取没起飞的航班ID
    private List<Map<String, Object>> getNotTakeOff(String date, String airportCode, String airportAreaCode) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select flgt_id AS flgtId\n" +
                    "\t\tfrom T_FLIGHT\n" +
                    "\t\twhere flgt_adid = 'D'\n" +
                    "\t\tAND flgt_d_stot >= DATE_ADD(\"" + date + "\",INTERVAL 4 HOUR)\n" +
                    "\t\tAND flgt_d_stot < DATE_ADD(\"" + date + "\",INTERVAL 28 HOUR)\n" +
                    "\t\tAND flgt_ftyp <> 'CX' AND flgt_ftyp <> 'NO'\n");
            if (!StringUtils.isEmpty(airportCode)) {
                sql.append("        AND flgt_airport_code = \"" + airportCode + "\"\n");
            }
            if (!StringUtils.isEmpty(airportAreaCode)) {
                sql.append("        AND flgt_aptarea_code = \"" + airportAreaCode + "\"\n");
            }
            sql.append("\t\tAND (flgt_d_etot > DATE_ADD(\"" + date + "\",INTERVAL 28 HOUR)\n" +
                    "\t\tOR flgt_d_etot is null)");
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Integer findMaxFlgtNum() {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select MAX(flgt_repeat) maxFlgtNum from T_FLIGHT where flgt_flop = \"" + DateUtil.getCurrentDateStr() + "\"");
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            if (rows.size() < 0) {
                return 1;
            } else {
                Map row = (Map) rows.get(0);
                Integer maxFlgtNum = (Integer) row.get("maxFlgtNum");
                if (StringUtils.isEmpty(maxFlgtNum)) {
                    return 0;
                }
                return maxFlgtNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public TFlight saveFlight(TFlight tFlight) {
        // tFlightRepository.flush();
        TFlight save = null;
        synchronized (tFlightRepository) {
            save = tFlightRepository.save(tFlight);
            entityManagerFlight.clear();
        }
        return save;
    }

    public TTask saveTask(TTask tTask) {
        // tTaskRepository.flush();
        TTask save = null;
        synchronized (tTaskRepository) {
            save = tTaskRepository.save(tTask);
            //entityManagerFlight.flush();
            entityManagerFlight.clear();
        }
        return save;

    }

    private Map<String, Object> pingsql(String place_code, String airport_code) {

        String sql = "SELECT t.place_code AS placeCode,t.place_code_type AS placeCodeType,tt.name AS placeCodeTypeName,t.flrc_hydrt_pit_no AS flrcHydrtPitNo,t.airport_code AS airportCode,tt.font_color AS fontColor  FROM T_PLACECODE t LEFT JOIN T_PLACECODE_TYPE tt ON t.place_code_type=tt.id "
                + "                    where t.place_code=\"" + place_code + "\" and t.airport_code=\"" + airport_code + "\"";

        Query querys = entityManagerFlight.createNativeQuery(sql.toString());
        querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = querys.getResultList();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : rows) {
            Map row = (Map) obj;
            list.add(row);
        }
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private void setFlightAlcname(Map<String, Object> taskAndFlightById) {
        Object flgtRegn = taskAndFlightById.get("flgtRegn");
        Object flgtFlno = taskAndFlightById.get("flgtFlno");
        TFlightCode flightCodeInfo = getFlightCodeInfo(flgtRegn == null ? "" : (String) flgtRegn, flgtFlno == null ? "" : (String) flgtFlno);
        if(flightCodeInfo!=null){
            String  c1= flightCodeInfo.getArcrCustomNum().replaceFirst("^0+", "");
            TCustom info = tCustomRepository.findByCstmNum(c1);
            if(info==null){
                info = tCustomRepository.findByCstmNum(org.apache.commons.lang3.StringUtils.leftPad(c1,10,"0"));
            }
            if(info!=null){
                taskAndFlightById.put("flgtAlcname",info.getCstmName());
            }
        }
    }

    public TFlightCode getFlightCodeInfoByRegnAndFlno(String flgtRegn, String flgtFlno) {
        try {
            List<TFlightCode> mfList= new ArrayList<>();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn) && org.apache.commons.lang3.StringUtils.isNotBlank(flgtFlno)) {
                mfList= flightCodeRepository.selectByArcrRegnsFlnoAndTime(flgtRegn,flgtFlno);
            }
            if(mfList==null || mfList.size()==0){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn)){
                    mfList= flightCodeRepository.selectByArcrRegnsAndTime(flgtRegn);
                }
            }
            if(mfList==null || mfList.size()==0){
                if(org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn)){
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(newFlgtRegn)){
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtFlno)) {
                            mfList= flightCodeRepository.selectByArcrRegnsFlnoAndTime(newFlgtRegn,flgtFlno);
                        }
                        if(mfList==null || mfList.size()==0){
                            mfList= flightCodeRepository.selectByArcrRegnsAndTime(newFlgtRegn);
                        }
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn) && org.apache.commons.lang3.StringUtils.isNotBlank(flgtFlno)) {
                    mfList= TFlightCodeTemporary.converToFlgihtCode(flightCodeTemporaryRepository.selectByArcrRegnsFlnoAndTime(flgtRegn, flgtFlno));
                }
            }
            if(mfList==null || mfList.size()==0){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn)){
                    mfList= TFlightCodeTemporary.converToFlgihtCode(flightCodeTemporaryRepository.selectByArcrRegnsAndTime(flgtRegn));
                }
            }
            if(mfList==null || mfList.size()==0){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn) && org.apache.commons.lang3.StringUtils.isNotBlank(flgtFlno)) {
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(newFlgtRegn)) {
                        mfList = TFlightCodeTemporary.converToFlgihtCode(flightCodeTemporaryRepository.selectByArcrRegnsFlnoAndTime(newFlgtRegn, flgtFlno));
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(flgtRegn)){
                    String newFlgtRegn = translateFlghtNo(flgtRegn);
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(newFlgtRegn)){
                        mfList= TFlightCodeTemporary.converToFlgihtCode(flightCodeTemporaryRepository.selectByArcrRegnsAndTime(newFlgtRegn));
                    }
                }
            }
            if(mfList==null || mfList.size()==0){
                return  null;
            }
            mfList.stream()
                    .sorted(Comparator.comparing((TFlightCode item) -> item.getCnafUpdateTime(),
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            return mfList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TFlightCode getFlightCodeInfo(String flgtRegn, String flgtFlno) {
        if(org.apache.commons.lang3.StringUtils.isBlank(flgtRegn)){
            return null;
        }
        String replaceFlgtRegn= org.apache.commons.lang3.StringUtils.replace(flgtRegn,"-","");
        TFlightCode flightCodeInfoByRegnAndFlno = getFlightCodeInfoByRegnAndFlno(replaceFlgtRegn, flgtFlno);
        if(flightCodeInfoByRegnAndFlno==null){
            flightCodeInfoByRegnAndFlno = getFlightCodeInfoByRegnAndFlno(flgtRegn, flgtFlno);
        }
        if(flightCodeInfoByRegnAndFlno==null){
            String replaceFlgtFlno= org.apache.commons.lang3.StringUtils.replace(flgtFlno,"-","");
            flightCodeInfoByRegnAndFlno=getFlightCodeInfoByRegnAndFlno(replaceFlgtFlno,flgtFlno);
        }
        if(flightCodeInfoByRegnAndFlno==null){
            flightCodeInfoByRegnAndFlno=getFlightCodeInfoByRegnAndFlno(flgtFlno,flgtFlno);
        }
        return flightCodeInfoByRegnAndFlno;
    }

    private String translateFlghtNo(String flrcAircrftNo) {
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(flrcAircrftNo)){
            //如果飞机号码没有-则手动加上
            if( flrcAircrftNo.startsWith("B") && flrcAircrftNo.indexOf("-")==-1){
                StringBuffer stringBuilder1=new StringBuffer(flrcAircrftNo);
                stringBuilder1.insert(1,"-");
                flrcAircrftNo=stringBuilder1.toString();
                System.out.println("飞机号:"+flrcAircrftNo);
            }else{
                //TODO
                String a = "^([A-Z]+)(\\d*.*)$";
                Pattern compile = Pattern.compile(a);
                Matcher matcher = compile.matcher(flrcAircrftNo);
                if(matcher.matches()){
                    if(org.apache.commons.lang3.StringUtils.isNotEmpty(matcher.group(2)) && org.apache.commons.lang3.StringUtils.isNotEmpty(matcher.group(1))){
                        flrcAircrftNo = matcher.group(1) + "-" + matcher.group(2);
                    }else if(org.apache.commons.lang3.StringUtils.isNotEmpty(matcher.group(1))){
                        flrcAircrftNo = matcher.group(1);
                    }
                }
            }
            return  flrcAircrftNo;
        }
        return null;
    }

    /**
     * @Description: 拼接油单类型
     * @Param:
     * @return:
     * @Author: XiuHongXin
     * @Date: 2019/9/3
     */
    private Integer pingSingleType(Map<String, Object> myFlightTask) {
        try {
            String flgtRegn = "";
            String flgtFlti = "";
            if (!StringUtils.isEmpty(myFlightTask.get("flgtRegn"))) {
                flgtRegn = myFlightTask.get("flgtRegn").toString();
            }
            if (!StringUtils.isEmpty(myFlightTask.get("flgtFlti"))) {
                flgtFlti = myFlightTask.get("flgtFlti").toString();
            }
            Integer taskContent = null;
            if (null != myFlightTask.get("taskContent")) {
                taskContent = Integer.valueOf(myFlightTask.get("taskContent").toString());
            }
            String flgtFltiIn = null;
            if (null != myFlightTask.get("flgtFltiIn")) {
                flgtFltiIn = myFlightTask.get("flgtFltiIn").toString();
            }
            String flgtFlno = null;
            if (null != myFlightTask.get("flgtFlno")) {
                flgtFlno = myFlightTask.get("flgtFlno").toString();
            }
            String flgtLinkFlno = null;
            if (null != myFlightTask.get("flgtLinkFlno")) {
                flgtLinkFlno = myFlightTask.get("flgtLinkFlno").toString();
            }
            if (taskContent == null) {
                //TODO  默认
                return 3;
            }



/*           Query querys = entityManagerFlight.createNativeQuery(pingsql(flgtRegn).toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            if(rows==null || rows.size()<=0){
                //如果飞机号码没有-则手动加上
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(flgtRegn) && flgtRegn.indexOf("-") == -1) {
                    StringBuffer stringBuilder1 = new StringBuffer(flgtRegn);
                    stringBuilder1.insert(1, "-");
                    String newflgtRegn = stringBuilder1.toString();
                    System.out.println("飞机号:" + newflgtRegn);
                    Query newQuerys = entityManagerFlight.createNativeQuery(pingsql(newflgtRegn).toString());
                    newQuerys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    rows = newQuerys.getResultList();
                }
            }
            List<Map<String, Object>> mfList = new ArrayList<Map<String, Object>>();
            if(rows!=null){
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    mfList.add(row);
                }
            }*/
            log.info("航显处理获取飞机信息：飞机号-{}，航班号-{}",flgtRegn,flgtFlno);
            //获取飞机号归属信息
            TFlightCode flightCodeInfo = getFlightCodeInfo(flgtRegn, flgtFlno);
            if (flightCodeInfo!=null && org.apache.commons.lang3.StringUtils.isNotBlank(flightCodeInfo.getArcrCustomNum())) {
                String  c1= flightCodeInfo.getArcrCustomNum().replaceFirst("^0+", "");
                TCustom info = tCustomRepository.findByCstmNum(c1);
                if(info==null){
                    info = tCustomRepository.findByCstmNum(org.apache.commons.lang3.StringUtils.leftPad(c1,10,"0"));
                }
                String cstmRegion = Optional.ofNullable(info).map(TCustom::getCstmRegion).orElse("");  //购买航空公司
                // TODO 优先处理特殊航班的情况
                //判断是否包含CN
                if (cstmRegion.contains("CN")) {
                    //判断是否是经停航班
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(flgtFlno) && org.apache.commons.lang3.StringUtils.isNotEmpty(flgtLinkFlno)) {
                        // 说明经停
                        if (flgtFlno.equals(flgtLinkFlno)) {
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(flgtFltiIn)) {
                                //说明国际航班
                                if (!"D".equals(flgtFltiIn)) {
                                    if (taskContent == 1) {
                                        // 5 内航 离境 抽油
                                        return 5;
                                    } else if (taskContent == 0) {
                                        // 2 内航 离境 加油
                                        return 2;
                                    } else {
                                        // 内航 离境 补加油
                                        return 8;
                                    }
                                }
                            }
                        }
                    }
                    // 判断国内国外 0：加油，1：抽油 2补加油
                    if ("D".equals(flgtFlti)) {
                        if (taskContent == 1) {
                            // 6 内航国内抽油
                            return 6;
                        } else if (taskContent == 0) {
                            // 3 内航国内加油
                            return 3;
                        } else {
                            // 7 内航国内补加油
                            return 7;
                        }
                    } else {
                        if (taskContent == 1) {
                            // 5 内航 离境 抽油
                            return 5;
                        } else if (taskContent == 0) {
                            // 2 内航 离境 加油
                            return 2;
                        } else {
                            // 内航 离境 补加油
                            return 8;
                        }
                    }
                    //判断任务类型
                } else {
                    //判断任务类型
                    if (taskContent == 1) {
                        // 4   外行  抽油
                        return 4;
                    } else if (taskContent == 0) {
                        //  1 外行   加油
                        return 1;
                    } else {
                        // 9 外航 补油
                        return 9;
                    }
                }
            }

            // 默认
            return 3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 3;
    }

    private int getFlightMaxNum() {

        try {

            if (!redissonDistributedLocker.tryLock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY, TimeUnit.SECONDS,2,2)) {
                //分布式缩 加锁失败 返回默认值
                return 0;
            }
            String currentDateStr = DateUtil.getCurrentDateStr();
            String incrKey="sequenceKey:"+currentDateStr;
            AtomicInteger atomicInteger = new AtomicInteger(86400 * 7);//7天过期
            AtomicLong atomicLong = JedisUtil.setIncr(incrKey, atomicInteger);

            redissonDistributedLocker.unlock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY);
            return atomicLong.intValue();

        } catch (Exception e){
            log.error("从redis里获取航班序号，发生异常，原因："+e.getMessage());
        }finally {

            redissonDistributedLocker.unlock(FLIGHT_NUM_DISTRIBUTED_LOCK_KEY);
        }

        return 0;
    }

    public static void main(String[] args) {
        int j = 1;
        for (int i = 0; i < 100; i++) {

            if(i == 10){
                j++;
                System.out.println("----10----"+j);
            }else{
                System.out.println("---" + j);
            }
            j++;
        }
    }

}
