package com.higer.statistical.service;

import com.google.common.collect.Lists;
import com.higer.statistical.util.ExportUtils;
import com.higer.statistical.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    @Autowired
    private StatisticalService statisticalService;

    public void export(String id, Integer type, Integer exportType, HttpServletRequest request, HttpServletResponse response){
        try {
            List<Map<String, Object>> data =exportType==1?statisticalService.exportUserData(id, type):statisticalService.exportCompanyData(id,type);
            List<Pair<String,String>> column = getColumnPair();
            File file = ExportUtils.exportXls("统计", data, column);
            FileUtil.downloadFiles("ExcelExport",".xlsx",file,request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出列配置
     * @return
     */
    private List<Pair<String,String>> getColumnPair(){
        List<Pair<String,String>> column= Lists.newArrayList();
        column.add(Pair.of("flrc_date","日期"));
        column.add(Pair.of("flrc_type","油单类型"));
        column.add(Pair.of("flrc_no","油单号"));
        column.add(Pair.of("flrc_airport","机场"));
        column.add(Pair.of("flrc_airl_name","飞机所属单位"));
        column.add(Pair.of("flrc_flight_no","航班号"));
        column.add(Pair.of("flrc_aircrft_no","飞机号码"));
        column.add(Pair.of("flrc_aircrft_type","飞机类型"));
        column.add(Pair.of("flrc_departure","起始"));
        column.add(Pair.of("flrc_transit","经停（备降）"));
        column.add(Pair.of("flrc_dest","终点"));
        column.add(Pair.of("flrc_test_bill_no","化验单号码"));
        column.add(Pair.of("flrc_fuel_name","油品名称与标准"));
        column.add(Pair.of("flrc_fuel_temp","温度（℃)"));
        column.add(Pair.of("flrc_fuel_dnst","实际密度 （克/毫升（g/cm³））"));
        column.add(Pair.of("flrc_meter_stat","计量表开始度数"));
        column.add(Pair.of("flrc_figuars","加油数量（小写）（升）"));
        column.add(Pair.of("flrc_figuars_word","加油数量（大写）（升）"));
        column.add(Pair.of("flrc_quantity","加油质量（千克）"));
        column.add(Pair.of("flrc_hydrt_pit_no","加油地井编号"));
        column.add(Pair.of("flrc_vehi_no","加油车车号"));
        column.add(Pair.of("flrc_stat_time","加油开始时间"));
        column.add(Pair.of("flrc_fnsh_time","加油结束时间"));
        column.add(Pair.of("flrc_deliver_name","加油员姓名"));
        return column;
    }
}
