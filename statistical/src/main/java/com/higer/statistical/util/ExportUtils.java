package com.higer.statistical.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 导出工具类
 */
public class ExportUtils {

    public static  File exportXls(String fileTitle, List<Map<String,Object>> data, List<Pair<String, String>> column) throws IOException {
        // 生成xlsx文件
        // 第一步，创建一个webbook，对应一个Excel文件
        SXSSFWorkbook wb = new SXSSFWorkbook(5000);
//        HSSFWorkbook wb=new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        SXSSFSheet sheet = wb.createSheet();
        /*
         * 设定合并单元格区域范围 firstRow 0-based lastRow 0-based firstCol 0-based lastCol
         * 0-based
         */
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0,column.size()-1);
        // 在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
        sheet.trackAllColumnsForAutoSizing();

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(fileTitle);// 表头
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        CellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setFont(font);
        cell.setCellStyle(style1);


        // 第四步，创建单元格，并设置值表头 设置表头居中
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        for (int i = 0; i <=data.size() ; i++) {
            row=sheet.createRow(i+1);
            for (int j = 0; j < column.size(); j++) {
                if(i==0){
                    cell=row.createCell(j);
                    cell.setCellValue(column.get(j).getSecond());
                    cell.setCellStyle(style);
//                    sheet.setColumnWidth(0, 256*column.get(j).getSecond().length()*5+184);
                }else{
                    cell=row.createCell(j);
                    if(StringUtils.equals("flrc_type",column.get(j).getFirst())){//设置油单类型
                        cell.setCellValue(getFlrcType(setCellValue(data.get(i-1).get(column.get(j).getFirst()))));
                    }else{
                        cell.setCellValue(setCellValue(data.get(i-1).get(column.get(j).getFirst())));
                    }
                    cell.setCellStyle(style);
                }
            }
        }
        setSizeColumn(sheet);//调整列宽
        File tempFile = File.createTempFile(fileTitle+ UUID.randomUUID(), ".xlsx");
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        wb.write(outputStream);
        return tempFile;
    }

    /**
     * null => ——
     * @param value
     * @return
     */
    private static String setCellValue(Object value){
        return java.util.Optional.ofNullable(value).orElse("——").toString();
    }

    /**
     * 油单类型格式化方法
     * @param value
     * @return
     */
    private static String  getFlrcType(String value){
        switch (Integer.parseInt(value)){
            case 1:
                return "外航加油";
            case 2:
                return "内航离境加油";
            case 3:
                return "内航国内加油";
            case 4:
                return "外航抽油";
            case 5:
                return "内航离境抽油";
            case 6:
                return "内航国内抽油";
            default:
                return "-";
        }
    }

    /**
     * 自适应宽度(中文支持) 。。。网上下的，不是特别好用，凑合着用吧
     * @param sheet
     */
    private static void setSizeColumn(SXSSFSheet sheet) {
        for (int columnNum = 0; columnNum <= 8; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
}
