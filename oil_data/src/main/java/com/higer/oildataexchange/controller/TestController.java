package com.higer.oildataexchange.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ImageUtils;
import com.higer.oildataexchange.common.PDFUtils;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.acdm.AcdmFlow;
import com.higer.oildataexchange.entity.acdm.AcdmXml;
import com.higer.oildataexchange.entity.acdm.FligtVo;
import com.higer.oildataexchange.entity.flight.FIRQ;
import com.higer.oildataexchange.entity.oil.ExcelOneOIL;
import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import com.higer.oildataexchange.repository.ExcelOil1repository;
import com.higer.oildataexchange.repository.TFuelRecptRepository;
import com.higer.oildataexchange.repository.TOrderInfoRepository;
import com.higer.oildataexchange.repository.TStaffRepository;
import com.higer.oildataexchange.service.AirUnitService;
import com.higer.oildataexchange.service.AirportPushConsumer;
import com.higer.oildataexchange.service.FlightInfoService;
import com.higer.oildataexchange.service.SendHttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class TestController {
    @Autowired
    private ExcelOil1repository excelOil1repository;
    @Autowired
    private SendHttpService service;
    @Autowired
    private TOrderInfoRepository tOrderInfoRepository;
    @Autowired
    private FlightInfoService flightInfoService;

    @Autowired
    private TFuelRecptRepository tFuelRecptRepository;

    @Autowired
    private SendHttpService sendHttpService;
    @Autowired
    private AirportPushConsumer airportPushConsumer;


    @Autowired
    private TStaffRepository tStaffRepository;

    @Value(value = "${send.acdm.url}")
    private String sendAcdmUrl;
    @Autowired
    private AirUnitService airUnitService;

    /**
     * 将一组数据平均分成n组
     *
     * @param source 要分组的数据源
     * @param n      平均分成n组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    @GetMapping("flight")
    public String flightInfoTest() {

        try {
            String url = "http://219.143.3.170:12280/voice_prompt/flightQuery";
            FIRQ firq = new FIRQ();
            firq.setADID("D");
            firq.setAPC3("BSD,CWJ,DIG,DLU,JHG,JMJ,KMG,LJG,LNJ,LUM,NLH,SYM,TCZ,WNH,ZAT");
            try {
                firq.setDTFR(DateUtils.parseDate("2020-11-25 00:00:00", Constant.YYYY_MM_DD_HH_MM_SS));
                firq.setDTTO(DateUtils.parseDate("2020-11-25 23:59:59", Constant.YYYY_MM_DD_HH_MM_SS));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            firq.setLUTS("2020-11-25 00:00:00.000");
            //firq.setALC2("CA,CZ");
            firq.setLMTN(100);
            R<FIRQ> r = R.newInstanceR("CZ", "CNAF", firq);
            String xml = XmlUtils.convertToXml(r, Constant.CHARSET, true);
            log.info("发送信息：" + xml);
            String response = service.sendHttpPost(url, xml);
            flightInfoService.parseResponse(response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("oil")
    public void oilTest(String font, String type, String isUse, @RequestBody String json, HttpServletResponse response) {
        try {
            TFuelRecpt tFuelRecpt = JSONObject.parseObject(json, TFuelRecpt.class);

            ClassPathResource resource = new ClassPathResource("static/template.pdf");
            InputStream inputStream = resource.getInputStream();
            File targetFile = File.createTempFile("template_export_copy", ".pdf");
            try {
                FileUtils.copyInputStreamToFile(inputStream, targetFile);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            byte[] bytes = PDFUtils.fillTemplateTest(font, type, isUse, tFuelRecpt, targetFile);
            byte[] bytes1 = ImageUtils.pdf2image(bytes);
            response.setHeader("Content-Type", "image/jpg");
            OutputStream out = response.getOutputStream();
            out.write(bytes1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("pic")
    public void picTest(String id, HttpServletResponse response) {
        try {
            TFuelRecpt byFlrcId = tFuelRecptRepository.findByFlrcId(id);
            byte[] data = Base64Utils.decodeFromString(byFlrcId.getFlrcSingle());
            byte[] bytes1 = ImageUtils.pdf2image(data);
            String base = Base64Utils.encodeToString(bytes1);
            log.error("base:" + base);
            response.setHeader("Content-Type", "image/jpg");
            OutputStream out = response.getOutputStream();
            out.write(bytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("acdm")
    public void acdm(HttpServletResponse response) {
        try {
            String data = "{\n" +
                    "\t\"dFlgtAAtot\": 1606811620000,\n" +
                    "\t\"flgtAdid\": \"D\",\n" +
                    "\t\"flgtAirportCode\": \"2901\",\n" +
                    "\t\"flgtAl2c\": \"F1\",\n" +
                    "\t\"flgtDEtot\": 1606811620000,\n" +
                    "\t\"flgtDStot\": 1606811620000,\n" +
                    "\t\"flgtDes3c\": \"\",\n" +
                    "\t\"flgtFfid\": \"2b29bf5e-323b-41f9-a3ea-05258fe04e05\",\n" +
                    "\t\"flgtFlno\": \"F111\",\n" +
                    "\t\"flgtFlop\": 1606752000000,\n" +
                    "\t\"flgtFtyp\": \"OT\",\n" +
                    "\t\"flgtGame\": \"Y\",\n" +
                    "\t\"flgtId\": \"2b29bf5e-323b-41f9-a3ea-05258fe04e05\",\n" +
                    "\t\"flgtLinkFlop\": 1606752000000,\n" +
                    "\t\"flgtLinkRepeat\": 1,\n" +
                    "\t\"flgtNum\": 1,\n" +
                    "\t\"flgtOrg3c\": \"\",\n" +
                    "\t\"flgtPlacecode\": \"-1\",\n" +
                    "\t\"flgtPlacecodeStatus\": 0,\n" +
                    "\t\"flgtRegn\": \"B111\",\n" +
                    "\t\"flgtRegnStatus\": 0,\n" +
                    "\t\"flgtRepeat\": 1,\n" +
                    "\t\"flgtTaskAsign\": 0,\n" +
                    "\t\"flgtTrs3c1\": \"\",\n" +
                    "\t\"flgtTrs3c5\": \"-\",\n" +
                    "\t\"flgtTrsnm1\": \"\",\n" +
                    "\t\"flrcType\": 2,\n" +
                    "\t\"orderNo\": \"9\",\n" +
                    "\t\"taskAsgTime\": 1606811721000,\n" +
                    "\t\"taskContent\": 0,\n" +
                    "\t\"taskCreStaffId\": \"wangbin\",\n" +
                    "\t\"taskId\": \"c78c4b38-9146-4ae7-97aa-233126f0a3a4\",\n" +
                    "\t\"taskOpeStaffId\": \"16714\",\n" +
                    "\t\"taskOpeStaffName\": \"艾业东\",\n" +
                    "\t\"taskRecCreTime\": 1606811621000,\n" +
                    "\t\"taskStarmark\": 0,\n" +
                    "\t\"taskStatus\": 1\n" +
                    "}";
            FligtVo fligtVo = JSONObject.parseObject(data, FligtVo.class);

            HashMap<String, String> params = new HashMap<>();
            // 主类型
            params.put("mainType", "FLOP");
            // 子类型
            params.put("subType", "CLDT");
            // 发送者
            params.put("sender", "GSM");
           /* // 报文内容
            params.put("message",
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?><MSG><META><SNDR>AODB</SNDR><SEQS>1000</SEQS><DTTM>20021010090311</DTTM>" +
                            "<TYPE>FLOP</TYPE><STYP>CLDT</STYP></META><FLOP><FLID>121112312</FLID><FFID>CA-CA101-A-12DEC031345-D</FFID>" +
                            "<AOCID>CA101-201808271420-PEK-SHA</AOCID><CLDT CLNO=\"”1”\"><BELT>B01</BELT><BCLS>X</BCLS><PCOT>12DEC031330</PCOT>" +
                            "<PCCT>12DEC031430</PCCT><BTYP>D</BTYP></CLDT></FLOP></MSG>");*/
            String styp = "";
            if (fligtVo.getTaskStatus() == 3) {
                styp = "FUEL-BGN";
            } else if (fligtVo.getTaskStatus() == 7) {
                styp = "FUEL-END";
            } else {
                log.info("未推送acdm，当前状态：" + fligtVo.getTaskStatus());
                return;
            }

            AcdmFlow acdmFlow = airportPushConsumer.formatInfoData(fligtVo);

            String arvs = "D".equals(fligtVo.getFlgtAdid()) ? fligtVo.getFlgtDes3c() : fligtVo.getFlgtOrg3c();
            AcdmXml<AcdmFlow> acdmFlowAcdmXml = AcdmXml.newInstanceAcdm(fligtVo.getFlgtFfid(), styp, fligtVo.getFlgtAl2c(),
                    fligtVo.getFlgtAdid(), "SHE", arvs, "1", acdmFlow);
            String xml = XmlUtils.convertToXml(acdmFlowAcdmXml, Constant.CHARSET, true);
            params.put("message", xml);
            String result = sendHttpService.sendAcdmHttpPost(sendAcdmUrl, JSON.toJSONString(params));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("excelOil")
    public void excelOil() {
        List<ExcelOneOIL> excelOilList = excelOil1repository.findByUploadStatus("0");
        List<List<ExcelOneOIL>> lists = averageAssign(excelOilList, 50);
        lists.forEach(one -> {
            try {
                Thread.sleep(1000);
                MyThread thread = new MyThread(one);
                thread.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });

    }

    @GetMapping("/getImportFlightCode")
    public void getImportFlightCode() {
        airUnitService.airUnitInfoSchedule();
    }

}
