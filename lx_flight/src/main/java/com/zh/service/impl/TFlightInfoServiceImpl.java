package com.zh.service.impl;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirportCode;
import com.zh.bean.flight.MyFlight;
import com.zh.bean.flight.MyTFlightInfo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.AirportCodeMapper;
import com.zh.dao.mapper.my.TFlightInfoMapper;
import com.zh.service.IExcelService;
import com.zh.service.TFlightInfoService;
import com.zh.util.DateUtil;
import com.zh.util.ModelAssistant;
import com.zh.util.UUIDUitl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TFlightInfoServiceImpl implements TFlightInfoService {

    @Autowired
    private TFlightInfoMapper tFlightInfoMapper;
    @Autowired
    private AirportCodeMapper airportCodeMapper;

    @Autowired
    private IExcelService iExcelService;

    @Override
    public ReturnMsg<Object> insert(MyTFlightInfo myTFlightInfo, MyStaff staff) {
        MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(myTFlightInfo.getFlgtDes3c());
        if (null == myAirportCode) return new ReturnMsg<Object>(Constant.CODE_ERR, "目的地机场查不到相关信息", null);
        myTFlightInfo.setFlgtDesnm(myAirportCode.getApcdAirportName());
        myTFlightInfo.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        int insert = tFlightInfoMapper.insertSelective(myTFlightInfo);
        if (1 != insert) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "新增失败", null);
        }
        return new ReturnMsg<Object>(Constant.CODE_OK, null, myTFlightInfo);
    }

    @Override
    public ReturnMsg<Object> update(MyTFlightInfo myTFlightInfo, MyStaff staff) {
        MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(myTFlightInfo.getFlgtDes3c());
        if (null == myAirportCode) return new ReturnMsg<Object>(Constant.CODE_ERR, "目的地机场查不到相关信息", null);
        myTFlightInfo.setFlgtDesnm(myAirportCode.getApcdAirportName());
        myTFlightInfo.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        int i = tFlightInfoMapper.updateByPrimaryKeySelective(myTFlightInfo);
        if (1 != i) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "修改失败", null);
        }
        return new ReturnMsg<Object>(Constant.CODE_OK, null, myTFlightInfo);
    }

    @Override
    public ReturnMsg<Object> sendOne(MyTFlightInfo myTFlightInfo, MyStaff staff) {
        try {
            myTFlightInfo = tFlightInfoMapper.selectByPrimaryKey(myTFlightInfo.getId());
            MyAirportCode myAirportCode = airportCodeMapper.selectAirportCodeFind(myTFlightInfo.getFlgtDes3c());
            if (null == myAirportCode)
                return new ReturnMsg<Object>(Constant.CODE_ERR, "目的地机场查不到相关信息", null);
            myTFlightInfo.setFlgtDesnm(myAirportCode.getApcdAirportName());
            myTFlightInfo.setFlgtDEtot(againDate(myTFlightInfo.getFlgtDEtot()));
            MyAirportCode tAirportCode = airportCodeMapper.selectAirportCodes(staff.getLoginUserIn().getStaffAirportCode());
            addFlight(rowToTFlight(myTFlightInfo, tAirportCode));
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnMsg<Object>(Constant.CODE_ERR, null, null);
        }
        return new ReturnMsg<Object>(Constant.CODE_OK, null, null);
    }

    @Override
    public ReturnMsg<Object> getAll(MyTFlightInfo myTFlightInfo, MyStaff staff) {
        myTFlightInfo.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        List<MyTFlightInfo> all = tFlightInfoMapper.getAll(myTFlightInfo);
        return new ReturnMsg<Object>(Constant.CODE_OK, null, all);
    }

    @Override
    public ReturnMsg<Object> delete(MyTFlightInfo myTFlightInfo, MyStaff staff) {
        int i = tFlightInfoMapper.deleteByPrimaryKey(myTFlightInfo.getId());
        if (1 != i) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "删除失败", null);
        }
        return new ReturnMsg<Object>(Constant.CODE_OK, null, null);
    }

    public void addFlight(Pair<MyFlight, MyFlight> pair) {
        try {
            MyFlight aFlight = null;
            MyFlight dFlight = null;
            String linkIdA = null;
            String uuidD = null;
            if (pair.getLeft() != null) {
                linkIdA = UUIDUitl.getUUID();
                pair.getLeft().setFlgtId(linkIdA);
                aFlight = pair.getLeft();
            }
            if (pair.getRight() != null) {
                uuidD = UUIDUitl.getUUID();
                pair.getRight().setFlgtId(uuidD);
                pair.getRight().setFlgtFfid(uuidD);
                if (StringUtils.isNotEmpty(linkIdA)) {
                    pair.getRight().setFlgtLinkFfid(linkIdA);
                    pair.getLeft().setFlgtLinkFfid(uuidD);
                    aFlight = pair.getLeft();
                }
                dFlight = pair.getRight();
            }
            iExcelService.addFlightAndTask(aFlight, dFlight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pair<MyFlight, MyFlight> rowToTFlight(MyTFlightInfo myTFlightInfo, MyAirportCode airport) {
        MyFlight in = new MyFlight();
        MyFlight out = new MyFlight();
        out.setFlgtFfid(UUID.randomUUID().toString());
        out.setFlgtFlno(myTFlightInfo.getFlgtFlno());
        out.setFlgtRegn(myTFlightInfo.getFlgtRegn());
        out.setFlgtFlti(myTFlightInfo.getFlgtFlti());
        out.setFlgtDes3c(myTFlightInfo.getFlgtDes3c());
        out.setFlgtDesnm(myTFlightInfo.getFlgtDesnm());
        out.setFlgtDStot(myTFlightInfo.getFlgtDEtot());
        out.setFlgtDEtot(myTFlightInfo.getFlgtDEtot());
        out.setFlgtPlacecode(myTFlightInfo.getFlgtPlacecode());
        out.setFlgtAcname(myTFlightInfo.getFlgtAcname());
        out.setFlgtAdid("D");
        out.setFlgtFlop(new Date());
        out.setFlgtFtyp("OT");
        out.setFlgtMissionProp("W/Z");
        out.setFlgtAirportCode(airport.getApcdCnafAirportCode());
        out.setFlgtAl2c(StringUtils.substring(myTFlightInfo.getFlgtFlno(), 0, 2));
        out.setFlgtOrg3c(airport.getApcdIataCode());
        out.setFlgtOrgnm(airport.getApcdAirportNameS());
        ModelAssistant.copyProperties(out, in);
        in.setFlgtFfid(UUID.randomUUID().toString());
        in.setFlgtAdid("A");
        in.setFlgtFtyp("LD");
        in.setFlgtAAtot(myTFlightInfo.getFlgtDEtot());
        in.setFlgtAEtot(myTFlightInfo.getFlgtDEtot());
        in.setFlgtAStot(myTFlightInfo.getFlgtDEtot());
        out.setFlgtVialc(airport.getApcdIataCode() + "-" + myTFlightInfo.getFlgtDes3c());
        return Pair.of(in, out);
    }

    private String getItatLikeAirport(String name) {
        MyAirportCode myAirportCode = airportCodeMapper.selectAirportLikeName(name);
        return myAirportCode != null ? myAirportCode.getApcdIataCode() : null;
    }


    //0 0 2 * * ?     凌晨2点
    @Scheduled(cron = "0 0 2 * * ? ")
    public void timingFlight() {
        System.out.println("-------10秒一次" + DateUtil.getCurrentDateTimeStr());
        MyTFlightInfo myTFlightInfo = new MyTFlightInfo();
        myTFlightInfo.setIsAuto(1);
        myTFlightInfo.setIsEffective(1);
        //is_auto
        List<MyTFlightInfo> allSelective = tFlightInfoMapper.getAllSelective(myTFlightInfo);
        long count = allSelective.stream().map(o -> {
                    MyAirportCode myTFlightInfo1 = airportCodeMapper.selectAirportCodeFind(o.getFlgtDes3c());
                    if (null == myTFlightInfo1) {
                        return null;
                    }
                    o.setFlgtDesnm(myTFlightInfo1.getApcdAirportName());
                    return o;
                }).filter(Objects::nonNull)
                .filter(o -> DateUtil.calculateWeek(o.getTimes()))
                .map(
                        o -> {
                            o.setFlgtDEtot(againDate(o.getFlgtDEtot()));
                            MyAirportCode tAirportCode = airportCodeMapper.selectAirportCodes(o.getFlgtAirportCode());
                            Pair<MyFlight, MyFlight> myFlightMyFlightPair = rowToTFlight(o, tAirportCode);
                            return myFlightMyFlightPair;
                        }
                ).peek(this::addFlight)
                .count();
        System.out.println("-----航班计划执行结果-----数据一共" + allSelective.size() + "条 ,共计成功" + count + "条");
    }


    private Date againDate(Date old) {
        Calendar calOld = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        calOld.setTime(old);
        cal.set(Calendar.HOUR_OF_DAY, calOld.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calOld.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, calOld.get(Calendar.SECOND));
        Date time = cal.getTime();
        return time;
    }

}
