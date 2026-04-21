package com.zh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zh.bean.ReturnMsg;
import com.zh.service.IExcelService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Base64;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private IExcelService excelServiceImpl;

    @PostMapping("/import")
    public ReturnMsg importExcel(@RequestBody String json) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            String file = jsonObject.getString("file");
            String airportCode = jsonObject.getString("airportCode");
            Assert.hasLength(file, "文件不能为空");
            Assert.hasLength(airportCode, "机场代码不能为空");
            byte[] decode = Base64.getDecoder().decode(file);
            //测试                                            // C:\Users\Administrator\Desktop
            //     byte[] decode = FileUtils.readFileToByteArray(new File("C:\\Users\\Administrator\\Desktop\\航班计划表-腾冲.xlsx"));
            //       FileUtils.writeByteArrayToFile(new File("C:\\Users\\LRC\\Desktop\\test.txt"),_decode);
            return excelServiceImpl.importTFlight(decode, airportCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnMsg.getInstanceNGz("excel导入失败", e.getMessage());
        }
    }


    @GetMapping("/test")
    public ReturnMsg importExcel() {
        try {
            //测试                                            // C:\Users\Administrator\Desktop
            byte[] decode = FileUtils.readFileToByteArray(new File("D:\\data\\新疆乌鲁木齐机场\\航班计划表 -1.xls"));
            return excelServiceImpl.importTFlight(decode, "2901");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnMsg.getInstanceNGz("excel导入失败", e.getMessage());
        }
    }
}
