package com.higer.higerservice.controller;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.entity.flight.TFuelRecpt;
import com.higer.higerservice.entity.oilpro.ASheet;
import com.higer.higerservice.entity.oilpro.Apk;
import com.higer.higerservice.entity.oilpro.Test;
import com.higer.higerservice.repository.flight.TAirlinesCodeRepository;
import com.higer.higerservice.repository.oilpro.AAiImgRepository;
import com.higer.higerservice.service.ASheetService;
import com.higer.higerservice.service.FileService;
import com.higer.higerservice.util.DateUtil;
import com.higer.higerservice.util.ModelAssistant;
import com.higer.higerservice.util.PcResponseObject;
import com.pro.entity.OilSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/25 09:43
 * @Description:
 */
@Controller
public class RecordController {


    @Autowired
    private FileService fileService;

    @Autowired
    private TAirlinesCodeRepository tAirlinesCodeRepository;

    @Autowired
    private AAiImgRepository aAiImgRepository;
    @Autowired
    private ASheetService aSheetService;

    @RequestMapping("/test3")
    @ResponseBody
    public String test2(
    ) {

        String oilSheet = "{\"flrcAircrftNo\":\"B1703\",\"flrcAircrftType\":\"B738\",\"flrcAirlName\":\"东方航空云南有限公司\",\"flrcAirport\":\"昆明长水国际机场\",\"flrcDate\":\"2019-08-25 16:42:22\",\"flrcDeliverName\":\"刘文强\",\"flrcDeparture\":\"昆明长水国际机场\",\"flrcDest\":\"泸州\",\"flrcFiguars\":0,\"flrcFiguarsWord\":\"\",\"flrcFlightNo\":\"MU5857\",\"flrcFnshTime\":\"2019-08-25 16:42\",\"flrcFuelDnst\":0.7841,\"flrcFuelName\":\"3号喷气燃料\",\"flrcFuelTemp\":22.4,\"flrcHydrtPitNo\":\"112-2\",\"flrcId\":\"2901312000104\",\"flrcMeterFnsh\":84780488,\"flrcMeterStat\":84780488,\"flrcQuantity\":0,\"flrcSign\":\"\",\"flrcStatTime\":\"2019-08-25 16:42\",\"flrcTestBillNo\":\"1\",\"flrcTransit\":\"-\",\"flrcType\":\"3\",\"flrcTypeName\":\"内航国内加油\",\"flrcVehiNo\":\"民航D3163\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJMT0dJTl9VU0VSX0tFWSI6IjNmMDNkZjI0LWYyNGItNDlhMS1hNDk4LWY0NjVhZjdkMjVlYiJ9.ALKsY5Gexp1dLqBAccNRwPBwSml7UbYELpCq5pT7D78\"}";

        System.out.println("notifyOilSheet" + oilSheet);

        OilSheet oilSheetObj = JSON.parseObject(oilSheet, OilSheet.class);
        TFuelRecpt tFuelRecpt = new TFuelRecpt();
        ModelAssistant.copyProperties(oilSheetObj, tFuelRecpt);
        tFuelRecpt.setFlrcType(Integer.valueOf(oilSheetObj.getFlrcType()));
        tFuelRecpt.setFlrcNo(oilSheetObj.getFlrcId());
        tFuelRecpt.setFlrcDate(DateUtil.parse(oilSheetObj.getFlrcDate()));
        tFuelRecpt.setFlrcMeterStat(oilSheetObj.getFlrcMeterStat().intValue());
        tFuelRecpt.setFlrcMeterFnsh(oilSheetObj.getFlrcMeterFnsh().intValue());
        tFuelRecpt.setFlrcStatTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
        tFuelRecpt.setFlrcFnshTime(DateUtil.parse(oilSheetObj.getFlrcStatTime()));
        tFuelRecpt.setFlrcQuantity(oilSheetObj.getFlrcQuantity().doubleValue());

        System.out.println(JSON.toJSON(tFuelRecpt));

        return null;
    }

    @RequestMapping("/testaa/{ver:.+}")
    @ResponseBody
    public String test1(
            @PathVariable String ver
    ) {
        String s = "";
        Apk apk = fileService.getBytes(ver, 0);
        if (apk.getApk() != null && !StringUtils.isEmpty(apk.getVer())) {
            byte[] bytes = apk.getApk();
            s = bytes.toString();
            return s;
        } else {
            s = "已经是最新版";
            return s;
        }
    }

    //油单
    @RequestMapping("/single_record")
    public String test() {
        return "singleRecord";
    }

    @GetMapping("system/testaa/getAll")
    @ResponseBody
    public PcResponseObject<ASheet> findAll(
            @RequestParam(value = "pages", required = false) int page
    ) {
        return aSheetService.findAll(page);
    }

    @PostMapping("/ntt")
    @ResponseBody
    public String ntt(
            HttpServletRequest request,
            @RequestBody Test test
    ) throws IOException {
        System.out.println("testaa");
        BufferedReader br = null;
        try {
            br = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str, wholeStr = "";
        while ((str = br.readLine()) != null) {
            wholeStr += str;
        }
        return "";
    }


}
