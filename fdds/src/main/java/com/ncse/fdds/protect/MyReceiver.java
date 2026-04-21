package com.ncse.fdds.protect;


import com.alibaba.fastjson.JSON;
import com.ncse.fdds.newibmmq.Receive;
import com.ncse.fdds.utils.DateUtil;
import com.ncse.fdds.xmlbean.*;

public class MyReceiver implements Runnable {
    private String msg;

    public MyReceiver(String msg) {
        this.msg = msg;
    }

    public static void main(String[] args) {
        String msg = "{\"aFlgtFtyp\":\"LD\",\"flgtAAtot\":1594743360000,\"flgtAEtot\":1594743382000,\"flgtAStot\":1594744800000,\"flgtAcname\":\"B738\",\"flgtAdid\":\"D\",\"flgtAirportCode\":\"2901\",\"flgtAl2c\":\"KY\",\"flgtAlcname\":\"昆明航空有限公司\",\"flgtAptareaCode\":\"\",\"flgtDEtot\":1594802580000,\"flgtDStot\":1594801200000,\"flgtDes3c\":\"TNA\",\"flgtDesnm\":\"济南遥墙机场\",\"flgtFfid\":\"3f75bb4bb51fb2e16d96b68d45d4620a\",\"flgtFlno\":\"KY8205\",\"flgtFlop\":1594742400000,\"flgtFlti\":\"D\",\"flgtFltiIn\":\"D\",\"flgtFnflag\":\"F\",\"flgtFtyp\":\"SH\",\"flgtGame\":\"Y\",\"flgtId\":\"6dc55bf0-2bc6-47f1-a6ee-4e3c670d7d3a\",\"flgtLinkFlno\":\"KY8280\",\"flgtLinkFlop\":1594742400000,\"flgtLinkRepeat\":3832368,\"flgtMissionProp\":\"W/Z\",\"flgtMissionPropIn\":\"W/Z\",\"flgtNum\":264,\"flgtOrg3c\":\"KMG\",\"flgtOrgnm\":\"昆明长水国际机场\",\"flgtPlacecode\":\"540\",\"flgtPlacecodeIn\":\"540\",\"flgtPlacecodeStatus\":0,\"flgtRegn\":\"B1315\",\"flgtRegnStatus\":0,\"flgtRepeat\":3832368,\"flgtTaskAsign\":1,\"flgtTrs3c5\":\"CAN-KMG-TNA\",\"flgtTrsnm3\":\"昆明长水国际机场-济南遥墙机场\",\"flgtVialc\":\"广州白云机场-昆明长水国际机场-济南遥墙机场\",\"flightValic\":\"广州-昆明-济南\",\"taskAccTime\":1594798297000,\"taskArriveTime\":1594798300000,\"taskAsgTime\":1594797720000,\"taskChagEndTime\":1594799129000,\"taskChagStaTime\":1594798297000,\"taskContent\":0,\"taskCreStaffId\":\"HKJYB2\",\"taskFuelRecptNo\":\"2901310039744\",\"taskId\":\"9088c472-bfe9-4488-b49e-eff128f18ab9\",\"taskOpeStaffId\":\"5991\",\"taskRcPrintTime\":1594799153000,\"taskRecCreTime\":1594491624000,\"taskStatus\":6,\"taskVehiNo\":\"111\"}";
        MyFlightTask flightTask = JSON.parseObject(msg, MyFlightTask.class);
        HYData hyData = new HYData();
        HeaderNew headerNew = new HeaderNew();
        BodyNew bodyNew = new BodyNew();
        ProcessNodeNew processNodeNew = new ProcessNodeNew();
        FlightIdentityNew flightIdentityNew = new FlightIdentityNew();
        headerNew.setMessageSendDateTime(DateUtil.getCurrentDateTimeStr());
        headerNew.setMessageSeqence("1");
        headerNew.setServiceType("PDUS");
        headerNew.setMessageType("HYData");
        headerNew.setSourceSystemID(flightTask.getFlgtId());//出港航班的ffid
        headerNew.setIata("KMG");
        headerNew.setRequestType("CreateOrUpdate");  // CreateOrUpdate（新增或更新） Delete（删除）
        if (null != flightTask.getTaskStatus() && 9 == flightTask.getTaskStatus()) {
            headerNew.setRequestType("Delete");
        }
        //-------------------------------------------
        flightIdentityNew.setFID(flightTask.getFlgtFfid());
        flightIdentityNew.setDirection(flightTask.getFlgtAdid());
        flightIdentityNew.setFlightNo(flightTask.getFlgtFlno());
        flightIdentityNew.setFlightDate(DateUtil.getCurrentToDate(flightTask.getFlgtFlop()));
        flightIdentityNew.setAirNum(flightTask.getFlgtRegn());
        flightIdentityNew.setDepCode(flightTask.getFlgtOrg3c());
        flightIdentityNew.setArrCode(flightTask.getFlgtDes3c());
        bodyNew.setFlightIdentity(flightIdentityNew);
        if (null != flightTask.getTaskArriveTime()) {
            processNodeNew.setStartFuelTime(DateUtil.getCurrentDateTimeStr(flightTask.getTaskArriveTime()));
        }
        if (null != flightTask.getTaskChagEndTime()) {
            processNodeNew.setEndFuelTime(DateUtil.getCurrentDateTimeStr(flightTask.getTaskChagEndTime()));
        }
        bodyNew.setProcessNode(processNodeNew);
        hyData.setHeader(headerNew);
        hyData.setBody(bodyNew);
        try {
            String dataMsg = XmlInterfaceUtils.convertToXml(hyData, "utf-8", true);
            System.out.println(dataMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        overMsg();
    }

    private synchronized void overMsg() {
        //System.out.println("接收的原数据----->" + msg);
        MyFlightTask flightTask = JSON.parseObject(msg, MyFlightTask.class);
        if (null != flightTask.getTaskStatus()) {
            if (flightTask.getTaskStatus() == 6 || flightTask.getTaskStatus() == 4) {
                HYData hyData = new HYData();
                HeaderNew headerNew = new HeaderNew();
                BodyNew bodyNew = new BodyNew();
                ProcessNodeNew processNodeNew = new ProcessNodeNew();
                FlightIdentityNew flightIdentityNew = new FlightIdentityNew();
                headerNew.setMessageSendDateTime(DateUtil.getCurrentDateTimeStr());
                headerNew.setMessageSeqence("1");
                headerNew.setServiceType("PDUS");
                headerNew.setMessageType("HYData");
                headerNew.setSourceSystemID(flightTask.getFlgtId());//出港航班的ffid
                headerNew.setIata("KMG");
                headerNew.setRequestType("CreateOrUpdate");  // CreateOrUpdate（新增或更新） Delete（删除）
                if (null != flightTask.getTaskStatus() && 9 == flightTask.getTaskStatus()) {
                    headerNew.setRequestType("Delete");
                }
                //-------------------------------------------
                flightIdentityNew.setFID(flightTask.getFlgtId());
                flightIdentityNew.setDirection(flightTask.getFlgtAdid());
                flightIdentityNew.setFlightNo(flightTask.getFlgtFlno());
                flightIdentityNew.setFlightDate(DateUtil.getCurrentToDate(flightTask.getFlgtFlop()));
                flightIdentityNew.setAirNum(flightTask.getFlgtRegn());
                flightIdentityNew.setDepCode(flightTask.getFlgtOrg3c());
                flightIdentityNew.setArrCode(flightTask.getFlgtDes3c());
                bodyNew.setFlightIdentity(flightIdentityNew);
                if (null != flightTask.getTaskArriveTime()) {
                    processNodeNew.setStartFuelTime(DateUtil.getCurrentDateTimeStr(flightTask.getTaskArriveTime()));
                }
                if (null != flightTask.getTaskChagEndTime()) {
                    processNodeNew.setEndFuelTime(DateUtil.getCurrentDateTimeStr(flightTask.getTaskChagEndTime()));
                }
                bodyNew.setProcessNode(processNodeNew);
                hyData.setHeader(headerNew);
                hyData.setBody(bodyNew);
                String dataMsg = XmlInterfaceUtils.convertToXml(hyData, "utf-8", true);
                ESBTest_send.RequestFlight(dataMsg);
                //Receive.RequestFlight(dataMsg);
                //Receive.sendMsg(hyData);
            }
        }
    }


}
