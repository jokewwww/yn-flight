package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.StatisticalService;
import com.zh.util.ExcelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/recptStatistical")
public class StatisticalController extends BaseController {

    @Autowired
    private StatisticalService statisticalService;

    /**
     * 根据日期查询各航空公司合计
     */
    @PostMapping(value = "/getCountByAir")
    public ReturnMsg<List<Map<String, Object>>> getCountByAir(

            @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = new ArrayList<>();
        // staffType 1导出保税类型 0不导出
        if ("1".equals(staff.getStaffType())) {
            fuelRecpts = statisticalService.getCountByAirAndBaoShui(staff.getDataTime(), staff);
        } else {
            fuelRecpts = statisticalService.getCountByAir(staff.getDataTime(), staff);
        }
        ReturnMsg<List<Map<String, Object>>> msg = new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, fuelRecpts);
        return msg;
    }

    /**
     * 区分统计报税油单
     */
    @PostMapping(value = "/distinguishStatisticalTax")
    public ReturnMsg<List<Map<String, Object>>> distinguishStatisticalTax(
            @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = new ArrayList<>();
        // staffType 1导出保税类型 0不导出
        fuelRecpts = statisticalService.distinguishStatisticalTax(staff.getDataTime(), staff);
        ReturnMsg<List<Map<String, Object>>> msg = new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, fuelRecpts);
        return msg;
    }

    /**
     * 日期查询加油员合计
     */
    @PostMapping(value = "/getCountByStaff")
    public ReturnMsg<List<Map<String, Object>>> getCountByStaff(
            @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getCountByStaff(staff.getDataTime(), staff);
        ReturnMsg<List<Map<String, Object>>> msg = new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, fuelRecpts);
        return msg;
    }

    /**
     * 日期查询加油车合计
     */
    @PostMapping(value = "/getCountByVehi")
    public ReturnMsg<List<Map<String, Object>>> getCountByVehi(
            @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getCountByVehi(staff.getDataTime(), staff);
        ReturnMsg<List<Map<String, Object>>> msg = new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, fuelRecpts);
        return msg;
    }

    /**
     * 根据日期获取7加油车合计列表
     */
    @PostMapping(value = "/exportCountByVehi")
    public void exportCountByVehi(HttpServletResponse response, @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getCountByVehi(staff.getDataTime(), staff);
        String[] columnNames = {"日期", "车辆编号", "车牌号码", "作业时间(小时)", "加油量(千克)", "销售量(升)", "加油架次", "架平均加油量(千克/架)", "每小时加油量(千克/小时)"};
        String[] columns = {"flrcDate", "flrcVehiNum", "flrcVehiNo", "flrcMeterStat", "flrcQuantity", "flrcFuelVol", "num", "flrcFiguars", "flrcMeterFnsh"
        };
        String sheetName = "加油车作业统计表";
        String fileName = "加油车作业统计表";
        ExcelUtils.exportCountByVehi(response, fuelRecpts, columnNames, columns, sheetName, fileName, 1);
    }

    /**
     * 根据日期获取油单列表
     */
    @PostMapping(value = "/getFuelrecpt")
    public void getFuelrecpt(HttpServletResponse response, @RequestBody MyStaff staff, @RequestBody MyFuelRecpt myFuelRecpt) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getFuelrecpt(staff, myFuelRecpt);
        String[] columnNames = {"油单日期", "油单号", "加油机场三码", "加油机场名称", "版本号", "油单类型", "保税类型", "服务模式", "客户代码", "客户名称"
                , "航班号", "机号", "机型", "始发站三码", "始发站", "经停站三码", "经停站", "目的站三码", "目的站名称", "油品类型", "化验单号"
                , "温度", "密度", "加油升数", "加油重量", "加油开始时间", "加油结束时间", "加油车号", "地井", "加油员", "运油车号"
                , "运输里程", "收油机场三码", "收油机场", "代结算机场三码", "代结算机场名称", "抽油原因", "备注信息", "发送状态", "确认状态", "结算状态"
        };
        String[] columns = {"flrcDate", "flrcNo", "flrcAirport3c", "flrcAirport", "flrcVersion", "flrcType", "flrcBwtar", "flrcSupplyFuelType", "arcrCustomNum", "arcrCustomName"
                , "flrcFlightNo", "flrcAircrftNo", "flrcAircrftType", "flrcDeparture3c", "flrcDeparture", "flrcTransit3c", "flrcTransit", "flrcDest3c", "flrcDest", "flrcFuelName", "flrcTestBillNo"
                , "flrcFuelTemp", "flrcFuelDnst", "flrcFiguars", "flrcQuantity", "flrcStatTime", "flrcFnshTime", "flrcVehiNo", "flrcHydrtPitNo", "flrcDeliverName", "flrcTankerNo"
                , "flrcTransportMileage", "flrcRapc3", "flrcRapcn", "settlementRapc3", "settlementRapcn", "flrcDefuelReason", "flrcRemark", "flrcStatus", "flrcConfirmStatus", "flrcPayStatus"
        };
        String sheetName = "油单列表";
        String fileName = "油单列表";
        ExcelUtils.exportCountByVehi(response, fuelRecpts, columnNames, columns, sheetName, fileName, 1);
    }

    /**
     * 根据日期获取7加油员合计列表
     */
    @PostMapping(value = "/exportCountByStaff")
    public void exportCountByStaff(HttpServletResponse response, @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getCountByStaff(staff.getDataTime(), staff);
        String[] columnNames = {"日期", "员工编号", "加油员", "作业时间(小时)", "加油量(千克)", "销售量(升)", "加油架次", "架平均加油量(千克/架)", "每小时加油量(千克/小时)"};
        String[] columns = {"flrcDate", "flrcDeliverId", "flrcDeliverName", "flrcMeterStat", "flrcQuantity", "flrcFuelVol", "num", "flrcFiguars", "flrcMeterFnsh"
        };
        String sheetName = "加油员作业统计表";
        String fileName = "加油员作业统计表";
        ExcelUtils.exportCountByVehi(response, fuelRecpts, columnNames, columns, sheetName, fileName, 1);
    }

    /**
     * 根据日期获取航空公司加油信息统计
     */
    @PostMapping(value = "/exportCountByAir")
    public void exportCountByAir(HttpServletResponse response, @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.exportCountByAir(staff.getDataTime(), staff);
        if (CollectionUtils.isEmpty(fuelRecpts)) {
            return;
        }
        String[] columnNames;
        String[] columns;
        // staffType 1导出保税类型 0不导出
        if ("1".equals(staff.getStaffType())) {
            columnNames = new String[]{"日期", "客户编号", "航空公司", "保税类型", "加油单数量", "加油量(升)", "加油量(千克)", "抽油单数量", "抽油量(升)", "抽油量(千克)"};
            columns = new String[]{"flrcDate", "arcrCustomNum", "airlName", "flrcBwtar", "addCount", "addTotalVol", "addTotalQuantity", "pumpCount", "pumpTotalVol", "pumpTotalQuantity"};
        } else {
            columnNames = new String[]{"日期", "客户编号", "航空公司", "加油单数量", "加油量(升)", "加油量(千克)", "抽油单数量", "抽油量(升)", "抽油量(千克)"};
            columns = new String[]{"flrcDate", "arcrCustomNum", "airlName", "addCount", "addTotalVol", "addTotalQuantity", "pumpCount", "pumpTotalVol", "pumpTotalQuantity"};
        }

        String sheetName = "各航空公司统计";
        String fileName = "前端系统按航空公司统计加油量";
        ExcelUtils.exportFuelData(response, fuelRecpts, columnNames, columns, sheetName, fileName, "1".equals(staff.getStaffType()) ? 3 : 2);
    }

    /**
     * 根据日期获取航空公司加油信息详情
     */
    @PostMapping(value = "/exportDetailCountByAir")
    public void exportDetailCountByAir(HttpServletResponse response, @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.getOilCustomByAir(staff.getDataTime(), staff);
        String[] columnNames = {"油单日期", "客户编号", "加油机场", "保税类型", "航班号", "航线", "飞机号", "机型", "客户名称", "加油时间", "加油量(升)", "加油量(千克)", "油单号"};
        String[] columns = {"flrcDate", "arcrCustomNum", "apcdIataCode", "flrcBwtar", "flrcFlightNo", "flgtVialc", "flrcAircrftNo", "flrcAircrftType", "flrcAirlName", "flrcStatTime", "fuelVol", "quantity", "flrcNo"};
        String sheetName = "航空公司加油详情";
        String fileName = "航空公司加油详情";
        ExcelUtils.exportCountByVehi(response, fuelRecpts, columnNames, columns, sheetName, fileName, 0);
    }

    /**
     * 日期查询高峰架次统计表(小时,数量)
     */
    @PostMapping(value = "/getHours")
    public ReturnMsg<List<Map<String, Object>>> getHours(
            @RequestBody MyStaff staff) {
        return statisticalService.getHours(staff.getDataTime(), staff);
    }

    /**
     * 导出日期查询高峰架次统计表(小时,数量)
     */
    @PostMapping(value = "/exportGetHours")
    public void exportGetHours(HttpServletResponse response, @RequestBody MyStaff staff) {
        List<Map<String, Object>> fuelRecpts = statisticalService.exportGetHours(staff.getDataTime(), staff);
        String[] columnNames = {"(小时)日期", "航班数"};
        String[] columns = {"time", "num"};
        String sheetName = "高峰架次统计表";
        String fileName = "高峰架次统计表";
        ExcelUtils.exportCountByVehi(response, fuelRecpts, columnNames, columns, sheetName, fileName, 0);
    }
}