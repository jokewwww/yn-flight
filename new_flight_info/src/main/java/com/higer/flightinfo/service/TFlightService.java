package com.higer.flightinfo.service;

import com.higer.flightinfo.entity.TFlight;
import com.higer.flightinfo.entity.TFlightExtend;
import com.higer.flightinfo.util.TFlightUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TFlightService {

    @Value("${flight.airport-3c}")
    private String airport3C;
    @Value("${flight.airport-code}")
    private String airportCode;

    public TFlight transform2TFlight(Element body,boolean delete){
        TFlightExtend tFlight=new TFlightExtend();
        tFlight.setFlgtFfid(body.elementTextTrim("FID"));
        tFlight.setFlgtFlno(body.elementTextTrim("FlightNo"));//航班号
        tFlight.setFlgtFlop(TFlightUtils.dateFormat(body.elementTextTrim("FlightDate")));//航班日期
        tFlight.setFlgtOrg3C(body.elementTextTrim("DepCode"));//起飞机场
//        tFlight.setFlgtDes3C(body.elementTextTrim("ArrCode"));//到达机场
        String routes=body.elementTextTrim("Routes");
        String[] routeArr=routes.split(",");
        tFlight.setFlgtDes3C(routeArr.length>0?routeArr[routeArr.length-1]:null);//到达机场
        tFlight.setFlgtMissionProp(body.elementTextTrim("FlightCla"));//航班任务属性
        tFlight.setFlgtRegn(body.elementTextTrim("AirNum"));//飞机号
        tFlight.setFlgtAcname(body.elementTextTrim("AirType"));//飞机类型
//        tFlight.setFlgtVialc(RegExUtils.replaceAll(routes,",","-"));
        String vialc = RegExUtils.replaceAll(routes, ",", "-");
        tFlight.setFlgtAl2C(StringUtils.substring(body.elementTextTrim("FlightNo"),0,2));//航空公司二字码
        tFlight.setFlgtAirportCode(airportCode);//机场代码
        if(StringUtils.equals(airport3C,body.elementTextTrim("DepCode"))){//进出港
            tFlight.setFlgtAdid("D");
            tFlight.setFlgtDStot(TFlightUtils.dateFormat(body.elementTextTrim("STD")));//计划起飞
            tFlight.setFlgtDEtot(TFlightUtils.dateFormat(body.elementTextTrim("ETD")));//预计起飞
            tFlight.setFlgtDAtot(TFlightUtils.dateFormat(body.elementTextTrim("ATD")));//实际起飞
            tFlight.setFlgtPlacecode(body.elementTextTrim("DepStand"));//出发地机位
        }else if(StringUtils.equals(airport3C,body.elementTextTrim("ArrCode"))){
            tFlight.setFlgtAdid("A");
            tFlight.setFlgtAStot(TFlightUtils.dateFormat(body.elementTextTrim("STA")));//计划到达
            tFlight.setFlgtAEtot(TFlightUtils.dateFormat(body.elementTextTrim("ETA")));//预计到达
            tFlight.setFlgtAAtot(TFlightUtils.dateFormat(body.elementTextTrim("ATA")));//实际到达
            tFlight.setFlgtPlacecode(body.elementTextTrim("ArrStand"));//目的地机位
        }else{
            return null;//进出港都不是昆明，过滤
        }
        if(StringUtils.equals(tFlight.getFlgtAdid(),"D")){
            tFlight.setFlgtVialc(StringUtils.substring(vialc,StringUtils.indexOf(vialc,"KMG")));
        }else{
            tFlight.setFlgtVialc(StringUtils.substring(vialc,0,StringUtils.indexOf(vialc,"KMG"))+"KMG");
        }
        System.out.println("胖子让我输出这个--------"+tFlight.getFlgtAdid()+"----------"+tFlight.getFlgtVialc());
        if(StringUtils.isNotEmpty(body.elementTextTrim("ShareMainF"))){//是共享航班，过滤
            return null;
        }
        tFlight.setFlgtFtyp(TFlightUtils.transformFtyp(body.elementTextTrim("FlightStatus"),tFlight.getFlgtAdid()));//航班状态
//        tFlight.setFlgtVialc(String.format("%s-%s",tFlight.getFlgtOrg3C(),tFlight.getFlgtDes3C()));
        tFlight.setFlgtFlti(TFlightUtils.transformFlti(body.elementTextTrim("Fcategory")));
        tFlight.setLinkFfid(body.elementTextTrim("LFIDForKMG"));
        if(delete){
            tFlight.setFlgtFtyp("CX");
        }
        return tFlight;
    }


    public TFlight transform2TFlightNew(Element body,boolean delete){
        TFlightExtend tFlight=new TFlightExtend();
        tFlight.setFlgtFfid(body.elementTextTrim("FID"));
        if(delete){
            tFlight.setFlgtFtyp("CX");
            return tFlight;
        }

        tFlight.setFlgtFlno(body.elementTextTrim("FlightNo"));//航班号
        tFlight.setFlgtFlop(TFlightUtils.dateFormat(body.elementTextTrim("FlightDate")));//航班日期
        tFlight.setFlgtOrg3C(body.elementTextTrim("DepCode"));//起飞机场
//        tFlight.setFlgtDes3C(body.elementTextTrim("ArrCode"));//到达机场
        String routes=body.elementTextTrim("Routes");
        if(StringUtils.isNotEmpty(routes)){
            String[] routeArr=routes.split(",");
            tFlight.setFlgtDes3C(routeArr.length>0?routeArr[routeArr.length-1]:null);//到达机场
            String vialc = RegExUtils.replaceAll(routes, ",", "-");
            if(StringUtils.equals(tFlight.getFlgtAdid(),"D")){
                tFlight.setFlgtVialc(StringUtils.substring(vialc,StringUtils.indexOf(vialc,"KMG")));
            }else{
                tFlight.setFlgtVialc(StringUtils.substring(vialc,0,StringUtils.indexOf(vialc,"KMG"))+"KMG");
            }
        }
        tFlight.setFlgtMissionProp(body.elementTextTrim("FlightCla"));//航班任务属性
        tFlight.setFlgtRegn(body.elementTextTrim("AirNum"));//飞机号
        tFlight.setFlgtAcname(body.elementTextTrim("AirType"));//飞机类型
//        tFlight.setFlgtVialc(RegExUtils.replaceAll(routes,",","-"));

        tFlight.setFlgtAl2C(StringUtils.substring(body.elementTextTrim("FlightNo"),0,2));//航空公司二字码
        tFlight.setFlgtAirportCode(airportCode);//机场代码
        if(StringUtils.equals(airport3C,body.elementTextTrim("DepCode"))){//进出港
            tFlight.setFlgtAdid("D");
            tFlight.setFlgtDStot(TFlightUtils.dateFormat(body.elementTextTrim("STD")));//计划起飞
            tFlight.setFlgtDEtot(TFlightUtils.dateFormat(body.elementTextTrim("ETD")));//预计起飞
            tFlight.setFlgtDAtot(TFlightUtils.dateFormat(body.elementTextTrim("ATD")));//实际起飞
            tFlight.setFlgtPlacecode(body.elementTextTrim("DepStand"));//出发地机位
        }else if(StringUtils.equals(airport3C,body.elementTextTrim("ArrCode"))){
            tFlight.setFlgtAdid("A");
            tFlight.setFlgtAStot(TFlightUtils.dateFormat(body.elementTextTrim("STA")));//计划到达
            tFlight.setFlgtAEtot(TFlightUtils.dateFormat(body.elementTextTrim("ETA")));//预计到达
            tFlight.setFlgtAAtot(TFlightUtils.dateFormat(body.elementTextTrim("ATA")));//实际到达
            tFlight.setFlgtPlacecode(body.elementTextTrim("ArrStand"));//目的地机位
        }else{
            return null;//进出港都不是昆明，过滤
        }

        System.out.println("胖子让我输出这个--------"+tFlight.getFlgtAdid()+"----------"+tFlight.getFlgtVialc());
        if(StringUtils.isNotEmpty(body.elementTextTrim("ShareMainF"))){//是共享航班，过滤
            return null;
        }
        tFlight.setFlgtFtyp(TFlightUtils.transformFtyp(body.elementTextTrim("FlightStatus"),tFlight.getFlgtAdid()));//航班状态
//        tFlight.setFlgtVialc(String.format("%s-%s",tFlight.getFlgtOrg3C(),tFlight.getFlgtDes3C()));
        tFlight.setFlgtFlti(TFlightUtils.transformFlti(body.elementTextTrim("Fcategory")));
        tFlight.setLinkFfid(body.elementTextTrim("LFIDForKMG"));

        return tFlight;
    }
}
