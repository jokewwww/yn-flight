package com.zh.util;


import com.zh.bean.flight.MyFlightTask;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.record.CFRuleRecord.ComparisonOperator;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * poi操作excel
 *
 * @author Administrator
 */
public class ExcelUtils {
//	public static void main(String[] args) throws Exception {
////		List<User> list = new ArrayList<User>();
////		for (int i=0;i<10;i++) {
////			User us = new User();
////			us.setName("张三"+i);
////			us.setPhone(i+"11111111111");
////			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////			us.setCreate_time(sdf.format(new Date()));
////			us.setUser_name(i+"wawerer@123123.com");
////			list.add(us);
////		}
////		String columnNames[] = {"姓名","电话","时间","邮箱"};
////		String columns[] = {"name","phone","create_time","user_name"};
////		exportExcelByList("D:\\wang.xls", list, columnNames, columns, "用户信息");
//		String[][] mm = readexcell("D:\\wang.xls",1);
//		System.out.println(123);
//	}

    /**
     * 读取excel
     *
     * @param filepath 文件路径
     * @param startrow 读取的开始行
     * @throws Exception
     * @Result 返回一个二维数组（第一维放的是行，第二维放的是列表）
     */
    public static String[][] readexcell(String filepath, int startrow) throws Exception {
        // 判断文件是否存在
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IOException("文件" + filepath + "W不存在！");
        }
        //获取sheet
        Sheet sheet = getSheet(filepath);
        String[][] content = getData(startrow, sheet);
        return content;
    }

    /**
     * 读取excel
     *
     * @param filepath 文件路径
     * @param startrow 读取的开始行
     * @throws Exception
     * @Result 返回一个二维数组（第一维放的是行，第二维放的是列表）
     */
    public static String[][] readexcellByInput(InputStream is, String fileName, int startrow) throws Exception {
        //文件后缀
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf("."));
        //获取sheet
        Sheet sheet = null;
        if (".xls".equals(extension)) {//2003
            //获取工作薄
            POIFSFileSystem fs = new POIFSFileSystem(is);
            sheet = new HSSFWorkbook(fs).getSheetAt(0);
        } else if (".xlsx".equals(extension) || ".xlsm".equals(extension)) {
            sheet = new XSSFWorkbook(is).getSheetAt(0);
        } else {
            throw new IOException("文件（" + fileName + "）,无法识别！");
        }
        //获取表单数据
        String[][] content = getData(startrow, sheet);
        return content;
    }

    /**
     * 获取表单数据
     * wangyue
     *
     * @param startrow
     * @param sheet
     * @return 2018年4月26日下午2:25:43
     */
    private static String[][] getData(int startrow, Sheet sheet) {
        // 得到总行数
        int rowNum = sheet.getLastRowNum() + 1;
        // 根据第一行获取列数
        Row row = sheet.getRow(0);
        //获取总列数
        int colNum = row.getPhysicalNumberOfCells();
        //根据行列创建二维数组
        String[][] content = new String[rowNum - startrow][colNum];
        String[] cols = null;
        //通过循环，给二维数组赋值
        for (int i = startrow; i < rowNum; i++) {
            row = sheet.getRow(i);
            cols = new String[colNum];
            for (int j = 0; j < colNum; j++) {
                //获取每个单元格的值
                cols[j] = getCellValue(row.getCell(j));
                //把单元格的值存入二维数组
                content[i - startrow][j] = cols[j];
            }
        }
        return content;
    }


    /**
     * 根据表名获取第一个sheet
     *
     * @param path d:\\1213.xml
     * @return 2003-HSSFWorkbook  2007-XSSFWorkbook
     * @throws Exception
     */
    public static Sheet getSheet(String file) throws Exception {
        //文件后缀
        String extension = file.lastIndexOf(".") == -1 ? "" : file.substring(file.lastIndexOf("."));
        //创建输入流
        InputStream is = new FileInputStream(file);
        if (".xls".equals(extension)) {//2003
            //获取工作薄
            POIFSFileSystem fs = new POIFSFileSystem(is);
            return new HSSFWorkbook(fs).getSheetAt(0);
        } else if (".xlsx".equals(extension) || ".xlsm".equals(extension)) {
            return new XSSFWorkbook(is).getSheetAt(0);
        } else {
            throw new IOException("文件（" + file + "）,无法识别！");
        }
    }

    /**
     * 功能:获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = cell.getDateCellValue();
                        result = sdf.format(date);
                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        result = df.format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 导出  ---到固定文件目录
     * 根据传入List数据集合导出Excel表格 生成本地excel
     *
     * @param file                        （输出流路径）d:\\123.xml
     * @param list                        任何对象类型的list（数据库直接查询出的）User（id，name，age，sex)
     * @param columnNames（表头名称）(姓名、性别、年龄)
     * @param columns                     （表头对应的列名）（name,sex,age）注意顺序
     * @param sheetName（sheet名称）
     */
    @SuppressWarnings("rawtypes")
    public static void exportExcelByList(String file, List list, String[] columnNames, String[] columns, String sheetName) {
        OutputStream fos = null;
        try {
            //获取输出流
            fos = new FileOutputStream(file);
            //创建工作薄HSSFWorkbook
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建表单sheet
            HSSFSheet sheet = wb.createSheet(sheetName);
            //创建样式对象
            HSSFCellStyle style = wb.createCellStyle(); // 样式对象
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

            //创建行--表头
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < columnNames.length; i++) {
                //创建列、单元格
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(style);
            }
            //创建数据列
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                //创建行--数据
                HSSFRow listRow = sheet.createRow(i + 1);
                //循环列字段数组
                for (int j = 0; j < columns.length; j++) {
                    //创建列
                    HSSFCell listCell = listRow.createCell(j);
                    //根据反射调用方法
                    Method m = o.getClass().getMethod("get" + upperStr(columns[j]));
                    String value = (String) m.invoke(o);
                    if (value != null) {
                        listCell.setCellValue(value);
                        listCell.setCellStyle(style);
                    } else {
                        listCell.setCellValue("");
                        listCell.setCellStyle(style);
                    }
                    sheet.autoSizeColumn(j + 1, true);//自适应，从1开始
                }
            }
            //把工作薄写入到输出流
            wb.write(fos);
            System.out.println("生成excel成功：" + file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据传入List数据集合导出Excel表格 返回页面选择保存路径的excel
     *
     * @param response    （响应页面）
     * @param list        数据列表
     * @param columnNames 表头
     * @param columns     对应列名
     * @param sheetName
     * @param filename
     */
    @SuppressWarnings("rawtypes")
    public static void exportExcel(HttpServletResponse response, List list, String[] columnNames, String[] columns, String sheetName, String filename) {
        OutputStream fos = null;
        try {
            // 响应输出流，让用户自己选择保存路径
            response.setCharacterEncoding("UTF-8");
            response.reset();// 清除缓存
            // response.setContentType("octets/stream");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String((filename).getBytes("UTF-8"), "iso8859-1") + ".xls");
            fos = response.getOutputStream();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFPalette palette = wb.getCustomPalette();
            HSSFRow row1 = sheet.createRow(0);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue("飞机加油计划");
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = wb.createFont();
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font.setFontHeightInPoints((short) 24);// 设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style1.setFont(font);
            style1.setFillPattern(CellStyle.BORDER_NONE);
            style1.setFillForegroundColor((short) 9);
            cell1.setCellStyle(style1);
            Region region = new Region((short) 0, (short) 0, (short) 1, (short) 14);
            sheet.addMergedRegion(region);

            HSSFRow row2 = sheet.createRow(2);
            HSSFCell cell2 = row2.createCell(0);
            cell2.setCellValue(
                    "今天加油架次00:00-05:30  (       )        05:30-24:00  (       )       合计：  (       )       抽油架次  (       )");
            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font1 = wb.createFont();
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font1.setFontHeightInPoints((short) 20);// 设置字体大小
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style2.setFont(font1);
            style2.setFillPattern(CellStyle.BORDER_DASHED);
            style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cell2.setCellStyle(style2);
            Region region1 = new Region((short) 2, (short) 0, (short) 2, (short) 14);
            sheet.addMergedRegion(region1);
            HSSFCellStyle style7 = wb.createCellStyle();
            HSSFFont font7 = wb.createFont();
            style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font7.setFontHeightInPoints((short) 20);// 设置字体大小
            font7.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style7.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            style7.setFont(font7);
            style7.setFillPattern(CellStyle.BORDER_DASHED);
            style7.setFillForegroundColor((short) 9);
            HSSFRow row3 = sheet.createRow(3);
            HSSFCell cell3 = row3.createCell(0);
            cell3.setCellValue("请注意核对！");
            cell3.setCellStyle(style7);
            Region region3 = new Region((short) 3, (short) 0, (short) 3, (short) 7);
            sheet.addMergedRegion(region3);
            HSSFCell cell4 = row3.createCell(8);
            Date now = new Date();
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            String nowStr = sim.format(now);
            long time = 60 * 1000 * 60 * 4;// 60秒
            Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
            String beforeDatetr = sim.format(beforeDate);
            if (nowStr.equals(beforeDatetr)) {
                nowStr = nowStr;
            } else {
                nowStr = beforeDatetr;
            }
            cell4.setCellValue("日期:  " + nowStr);

            Region region2 = new Region((short) 3, (short) 8, (short) 3, (short) 14);
            sheet.addMergedRegion(region2);
            HSSFCellStyle style3 = wb.createCellStyle();
            HSSFFont font2 = wb.createFont();
            style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font2.setFontHeightInPoints((short) 20);// 设置字体大小
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style3.setFont(font2);
            style3.setFillPattern(CellStyle.BORDER_DASHED);
            style3.setFillForegroundColor((short) 9);
            cell4.setCellStyle(style3);

            HSSFCellStyle style5 = wb.createCellStyle();
            HSSFFont font5 = wb.createFont();
            font5.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style5.setFont(font5);
            style5.setFillPattern(CellStyle.BORDER_DASHED);
            style5.setFillForegroundColor((short) 9);
            Region region4 = new Region((short) 4, (short) 0, (short) list.size() + 4, (short) 0);
            sheet.addMergedRegion(region4);

            HSSFCellStyle style6 = wb.createCellStyle();
            HSSFFont font6 = wb.createFont();
            font6.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style6.setFont(font6);
            style6.setFillPattern(CellStyle.BORDER_DASHED);
            style6.setFillForegroundColor((short) 9);
            Region region6 = new Region((short) 4, (short) 14, (short) list.size() + 4, (short) 14);
            sheet.addMergedRegion(region6);

            HSSFCellStyle style11 = wb.createCellStyle();
            style11.setFillPattern(CellStyle.BORDER_DASHED);
            style11.setFillForegroundColor((short) 23);

            HSSFCellStyle style = wb.createCellStyle(); // 样式对象
            // 离境样式
            HSSFCellStyle styleL = wb.createCellStyle(); // 样式对象
            // 外航样式
            HSSFCellStyle styleY = wb.createCellStyle(); // 样式对象
            HSSFFont font0 = wb.createFont();
            font0.setFontHeightInPoints((short) 16);// 设置字体大小

            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
            style.setFont(font0);
            style.setBorderBottom((short) 1);
            style.setBorderLeft((short) 1);
            style.setBorderTop((short) 1);
            style.setBorderRight((short) 1);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setFillPattern(CellStyle.BORDER_NONE);
            style.setFillForegroundColor((short) 9);

            styleL.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            styleL.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
            styleL.setFont(font0);
            styleL.setBorderBottom((short) 1);
            styleL.setBorderLeft((short) 1);
            styleL.setBorderTop((short) 1);
            styleL.setBorderRight((short) 1);
            styleL.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleL.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleL.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleL.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleL.setFillPattern(CellStyle.BORDER_NONE);
            styleL.setFillForegroundColor((short) 9);
            // 离境背景色
            styleL.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleL.setFillPattern(CellStyle.SOLID_FOREGROUND);

            styleY.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            styleY.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
            styleY.setFont(font0);
            styleY.setBorderBottom((short) 1);
            styleY.setBorderLeft((short) 1);
            styleY.setBorderTop((short) 1);
            styleY.setBorderRight((short) 1);
            styleY.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleY.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleY.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleY.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleY.setFillPattern(CellStyle.BORDER_NONE);
            styleY.setFillForegroundColor((short) 9);
            // 离境背景色
            styleY.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
            styleY.setFillPattern(CellStyle.SOLID_FOREGROUND);

            HSSFCellStyle style0 = wb.createCellStyle();
            // 离境样式
            HSSFCellStyle styleL0 = wb.createCellStyle();
            // 外航样式
            HSSFCellStyle styleY0 = wb.createCellStyle();
            HSSFFont font01 = wb.createFont();
            font01.setFontHeightInPoints((short) 24);// 设置字体大小

            style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style0.setFont(font01);
            style0.setFillPattern(CellStyle.BORDER_NONE);
            style0.setFillForegroundColor((short) 9);
            style0.setBorderBottom((short) 1);
            style0.setBorderLeft((short) 1);
            style0.setBorderTop((short) 1);
            style0.setBorderRight((short) 1);
            style0.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style0.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style0.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style0.setTopBorderColor(IndexedColors.BLACK.getIndex());

            styleL0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleL0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleL0.setFont(font01);
            styleL0.setFillPattern(CellStyle.BORDER_NONE);
            styleL0.setFillForegroundColor((short) 9);
            styleL0.setBorderBottom((short) 1);
            styleL0.setBorderLeft((short) 1);
            styleL0.setBorderTop((short) 1);
            styleL0.setBorderRight((short) 1);
            styleL0.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleL0.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleL0.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleL0.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleL0.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleL0.setFillPattern(CellStyle.SOLID_FOREGROUND);

            styleY0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleY0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleY0.setFont(font01);
            styleY0.setFillPattern(CellStyle.BORDER_NONE);
            styleY0.setFillForegroundColor((short) 9);
            styleY0.setBorderBottom((short) 1);
            styleY0.setBorderLeft((short) 1);
            styleY0.setBorderTop((short) 1);
            styleY0.setBorderRight((short) 1);
            styleY0.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleY0.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleY0.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleY0.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleY0.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
            styleY0.setFillPattern(CellStyle.SOLID_FOREGROUND);

            HSSFCellStyle style4 = wb.createCellStyle();
            // 离境样式
            HSSFCellStyle styleL4 = wb.createCellStyle();
            // 外航样式
            HSSFCellStyle styleY4 = wb.createCellStyle();
            HSSFFont font3 = wb.createFont();
            font3.setFontHeightInPoints((short) 20);// 设置字体大小
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style4.setFont(font3);
            style4.setFillPattern(CellStyle.BORDER_NONE);
            style4.setFillForegroundColor((short) 9);
            style4.setBorderBottom((short) 1);
            style4.setBorderLeft((short) 1);
            style4.setBorderTop((short) 1);
            style4.setBorderRight((short) 1);
            style4.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style4.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style4.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style4.setTopBorderColor(IndexedColors.BLACK.getIndex());

            styleL4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleL4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleL4.setFont(font3);
            styleL4.setFillPattern(CellStyle.BORDER_NONE);
            styleL4.setFillForegroundColor((short) 9);
            styleL4.setBorderBottom((short) 1);
            styleL4.setBorderLeft((short) 1);
            styleL4.setBorderTop((short) 1);
            styleL4.setBorderRight((short) 1);
            styleL4.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleL4.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleL4.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleL4.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleL4.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleL4.setFillPattern(CellStyle.SOLID_FOREGROUND);

            styleY4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleY4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleY4.setFont(font3);
            styleY4.setFillPattern(CellStyle.BORDER_NONE);
            styleY4.setFillForegroundColor((short) 9);
            styleY4.setBorderBottom((short) 1);
            styleY4.setBorderLeft((short) 1);
            styleY4.setBorderTop((short) 1);
            styleY4.setBorderRight((short) 1);
            styleY4.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleY4.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleY4.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleY4.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleY4.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
            styleY4.setFillPattern(CellStyle.SOLID_FOREGROUND);

            // 离境外航颜色设定
            HSSFSheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();
            HSSFConditionalFormattingRule cf_R_rule = scf.createConditionalFormattingRule(ComparisonOperator.EQUAL,
                    "\"离境\"", null);
            HSSFPatternFormatting cf_R = cf_R_rule.createPatternFormatting();
            cf_R.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            HSSFConditionalFormattingRule cf_G_rule = scf.createConditionalFormattingRule(ComparisonOperator.EQUAL,
                    "\"外航\"", null);
            HSSFPatternFormatting cf_G = cf_G_rule.createPatternFormatting();
            cf_G.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
            HSSFConditionalFormattingRule[] cfRules = {cf_R_rule, cf_G_rule};
            // 条件格式应用的单元格范围
            CellRangeAddress[] regions = {new CellRangeAddress(6, list.size() + 6, 0, 14)};
            scf.addConditionalFormatting(regions, cfRules);
            // 表头
            HSSFRow row = sheet.createRow(4);
            for (int i = 0; i < columnNames.length; i++) {
                HSSFCell cell = row.createCell(i + 1);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(style4);
            }
            // sheet.autoSizeColumn(j+1, true);//自适应，从1开始
            sheet.setColumnWidth((short) 0, (short) 1200);
            sheet.setColumnWidth((short) 2, (short) 4100);
            sheet.setColumnWidth((short) 3, (short) 4100);
            sheet.setColumnWidth((short) 5, (short) 4100);
            sheet.setColumnWidth((short) 4, (short) 15000);
            sheet.setColumnWidth((short) 14, (short) 1200);

            HSSFFooter footer = sheet.getFooter();
            Date date2 = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time1 = format.format(date2);
            footer.setCenter("第 " + HSSFFooter.page() + "页（" + " 共 " + HSSFFooter.numPages() + "页）" + "\n" + time1);
            footer.setLeft(HSSFFooter.fontSize((short) 16) + "上午密度：_____温度：_____" + "\n");
            footer.setRight(HSSFFooter.fontSize((short) 16) + "下午密度：_____温度：_____" + "\n");
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(false); // 打印方向，true：横向，false：纵向(默认)
            printSetup.setScale((short) 50);
            printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            sheet.setRepeatingRows(new CellRangeAddress(3, 4, 0, 14));// 前两位数是设置需要重复打印的行的范围，后两位设置重复打印的列的范围。-1代表不重复打印。用于表头表头换页重复打印
            sheet.setPrintGridlines(true);
            sheet.setMargin(HSSFSheet.BottomMargin, (double) 0.45);// 页边距（下）
            sheet.setMargin(HSSFSheet.LeftMargin, (double) 0.1);// 页边距（左）
            sheet.setMargin(HSSFSheet.RightMargin, (double) 0.1);// 页边距（右）
            sheet.setMargin(HSSFSheet.TopMargin, (double) 0.3);// 页边距（上）
            sheet.setMargin(HSSFSheet.HeaderMargin, (double) 0.2);// 页眉边距
            sheet.setMargin(HSSFSheet.FooterMargin, (double) 0.1);// 页脚边距
            sheet.setHorizontallyCenter(true);//设置打印页面为水平居中
//			sheet.setVerticallyCenter(true);//设置打印页面为垂直居中

            Method m = null;
            for (int i = 0; i < list.size(); i++) {
                HSSFRow listRow = sheet.createRow(i + 5);
                MyFlightTask o = (MyFlightTask) list.get(i);
                listRow.setHeight((short) 920);
                // 离境
                if ("离境".equals(o.getFlgtbezu())) {
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell listCell = listRow.createCell(j + 1);
                        String value = "";
                        if (columns[j] != null) {
                            m = o.getClass().getMethod("get" + upperStr(columns[j]));
                            Object invoke = m.invoke(o);
                            if (invoke instanceof Date) {
                                value = new SimpleDateFormat("HH:mm").format((Date) invoke);
                            } else {
                                if (invoke != null) {
                                    value = String.valueOf(invoke);
                                }
                            }
                        }
                        if (value != null) {
                            listCell.setCellValue(value + "");
                        } else {
                            listCell.setCellValue("");
                        }
                        if (j == 2 || j == 4) {
                            listCell.setCellStyle(styleL4);
                        } else if (j == 3) {
                            listCell.setCellStyle(styleL0);
                        } else {
                            listCell.setCellStyle(styleL);
                        }
                    }
                } else if ("外航".equals(o.getFlgtbezu())) {
                    // 外航
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell listCell = listRow.createCell(j + 1);
                        String value = "";
                        if (columns[j] != null) {
                            m = o.getClass().getMethod("get" + upperStr(columns[j]));
                            Object invoke = m.invoke(o);
                            if (invoke instanceof Date) {
                                value = new SimpleDateFormat("HH:mm").format((Date) invoke);
                            } else {
                                if (invoke != null) {
                                    value = String.valueOf(invoke);
                                }

                            }
                        }
                        if (value != null) {
                            listCell.setCellValue(value + "");
                        } else {
                            listCell.setCellValue("");
                        }
                        if (j == 2 || j == 4) {
                            listCell.setCellStyle(styleY4);
                        } else if (j == 3) {
                            listCell.setCellStyle(styleY0);
                        } else if (j == 0 || j == 1 || j == 5) {
                            listCell.setCellStyle(styleY);
                        } else {
                            listCell.setCellStyle(style);
                        }
                    }
                } else {
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell listCell = listRow.createCell(j + 1);
                        String value = "";
                        if (columns[j] != null) {
                            m = o.getClass().getMethod("get" + upperStr(columns[j]));
                            Object invoke = m.invoke(o);
                            if (invoke instanceof Date) {
                                value = new SimpleDateFormat("HH:mm").format((Date) invoke);
                            } else {
                                if (invoke != null) {
                                    value = String.valueOf(invoke);
                                }
                            }
                        }
                        if (value != null) {
                            listCell.setCellValue(value + "");
                        } else {
                            listCell.setCellValue("");
                        }
                        if (j == 2 || j == 4) {
                            listCell.setCellStyle(style4);
                        } else if (j == 3) {
                            listCell.setCellStyle(style0);
                        } else {
                            listCell.setCellStyle(style);
                        }
                    }
                }
            }
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 把输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private static String upperStr(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 海量数据导出 100万以上
     * wangyue
     *
     * @param response    直接响应到浏览器
     * @param list        数据列表
     * @param columnNames 表头数组
     * @param columns     和表头数组对应的字段数组
     * @param sheetName   sheet表单名称
     * @param filename    工作薄名称
     *                    2018年4月26日下午1:53:29
     */
    public static void exportBigData(HttpServletResponse response, List list, String[] columnNames, String[] columns, String sheetName, String filename) {

        OutputStream os = null;
        try {
            response.setContentType("application/force-download"); // 设置下载类型
            response.setHeader("Content-Disposition", "attachment;filename=" + filename); // 设置文件的名称
            os = response.getOutputStream(); // 输出流
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);//内存中保留 1000 条数据，以免内存溢出，其余写入 硬盘
            //获得该工作区的第一个sheet
            Sheet sheet1 = wb.createSheet(sheetName);
            int excelRow = 0;
            //标题行
            Row titleRow = (Row) sheet1.createRow(excelRow++);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //明细行
                    Row contentRow = (Row) sheet1.createRow(excelRow++);
                    List<String> reParam = (List<String>) list.get(i);
                    for (int j = 0; j < reParam.size(); j++) {
                        Cell cell = contentRow.createCell(j);
                        cell.setCellValue(reParam.get(j));
                    }
                }
            }
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } // 关闭输出流
        }
    }

    /**
     * 根据传入List<HashMap>数据集合导出Excel表格 返回页面选择保存路径的excel
     *
     * @param response    （响应页面）
     * @param list        数据列表
     * @param columnNames 表头
     * @param columns     对应列名
     * @param sheetName
     * @param filename
     */
    @SuppressWarnings("rawtypes")
    public static void exportStaffFuel(HttpServletResponse response, List<Map<String, String>> list, String[] columnNames, String[] columns, String sheetName, String filename) {
        OutputStream fos = null;
        try {
            // 响应输出流，让用户自己选择保存路径
            response.setCharacterEncoding("UTF-8");
            response.reset();// 清除缓存
            // response.setContentType("octets/stream");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String((filename).getBytes("UTF-8"), "iso8859-1") + ".xls");
//			String file="E:\\wang.xls";
//			fos = new FileOutputStream(file);
            fos = response.getOutputStream();

            //创建工作薄HSSFWorkbook
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建表单sheet
            HSSFSheet sheet = wb.createSheet(sheetName);
            //创建样式对象
            HSSFCellStyle style = wb.createCellStyle(); // 数据样式对象
            HSSFFont font0 = wb.createFont();
            font0.setFontHeightInPoints((short) 12);// 设置字体大小

            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
            style.setFont(font0);
            style.setBorderBottom((short) 1);
            style.setBorderLeft((short) 1);
            style.setBorderTop((short) 1);
            style.setBorderRight((short) 1);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setFillPattern(CellStyle.BORDER_NONE);
            style.setFillForegroundColor((short) 9);

            //创建行--表头
            HSSFRow rowtotal = sheet.createRow(0);
            HSSFCell cell1 = rowtotal.createCell(0);
            cell1.setCellValue(filename);
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = wb.createFont();
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font.setFontHeightInPoints((short) 14);// 设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style1.setFont(font);
            style1.setFillPattern(CellStyle.BORDER_NONE);
            style1.setFillForegroundColor((short) 9);
            cell1.setCellStyle(style1);
            Region region = new Region((short) 0, (short) 0, (short) 0, (short) (columnNames.length + 1));
            sheet.addMergedRegion(region);

            HSSFCellStyle style4 = wb.createCellStyle();//数据类型样式对象
            HSSFFont font3 = wb.createFont();
            font3.setFontHeightInPoints((short) 12);// 设置字体大小
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style4.setFont(font3);
            style4.setFillPattern(CellStyle.BORDER_NONE);
            style4.setFillForegroundColor((short) 9);
            style4.setBorderBottom((short) 1);
            style4.setBorderLeft((short) 1);
            style4.setBorderTop((short) 1);
            style4.setBorderRight((short) 1);
            style4.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style4.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style4.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style4.setTopBorderColor(IndexedColors.BLACK.getIndex());

            HSSFCellStyle style5 = wb.createCellStyle();//表格前合并空白列
            HSSFFont font5 = wb.createFont();
            font5.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style5.setFont(font5);
            style5.setFillPattern(CellStyle.BORDER_DASHED);
            style5.setFillForegroundColor((short) 9);
            Region region4 = new Region((short) 1, (short) 0, (short) list.size() + 2, (short) 0);
            sheet.addMergedRegion(region4);

            HSSFCellStyle style6 = wb.createCellStyle();//表格后合并空白列
            HSSFFont font6 = wb.createFont();
            font6.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style6.setFont(font6);
            style6.setFillPattern(CellStyle.BORDER_DASHED);
            style6.setFillForegroundColor((short) 9);
            Region region6 = new Region((short) 1, (short) (columnNames.length + 1), (short) list.size() + 2, (short) (columnNames.length + 1));
            sheet.addMergedRegion(region6);

            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < columnNames.length; i++) {
                //创建列、单元格
                HSSFCell cell = row.createCell(i + 1);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(style4);
            }
            int number = 0;
            //创建数据列
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> a = list.get(i);
                //创建行--数据
                HSSFRow listRow = sheet.createRow(i + 2);
                //循环列字段数组
                for (int j = 0; j < columns.length; j++) {
                    //创建列
                    HSSFCell listCell = listRow.createCell(j + 1);
                    //根据反射调用方法
                    String value = a.get(columns[j]);
                    if (value != null) {
                        if ("num".equals(columns[j])) {
                            HSSFDataFormat df = wb.createDataFormat();
                            style.setDataFormat(df.getBuiltinFormat("#,#0"));
                            listCell.setCellValue(Integer.parseInt(value));
                            listCell.setCellStyle(style);
                            number = number + Integer.parseInt(value);
                        } else {
                            listCell.setCellValue(value);
                            listCell.setCellStyle(style);
                        }
                    } else {
                        listCell.setCellValue("");
                        listCell.setCellStyle(style);
                    }
//					sheet.autoSizeColumn(j+1, true);//自适应，从1开始
                    sheet.setColumnWidth((short) 0, (short) 1200);
                    sheet.setColumnWidth((short) 1, (short) 2600);
                    sheet.setColumnWidth((short) 2, (short) 3000);
                    for (int l = 3; l < columnNames.length; l++) {
                        sheet.setColumnWidth((short) l, (short) 7800);
                    }
                    sheet.setColumnWidth((short) (columnNames.length), (short) 3500);
                    sheet.setColumnWidth((short) (columnNames.length + 1), (short) 1200);
                }
            }
            HSSFRow rowbottom = sheet.createRow(list.size() + 2);//合计行
            HSSFCell cellb = rowbottom.createCell(1);
            cellb.setCellValue("合计：（" + list.size() + "）人，（" + number + "）架次");
            HSSFCellStyle styleb = wb.createCellStyle();
            styleb.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            HSSFFont fontb = wb.createFont();
            styleb.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            fontb.setFontHeightInPoints((short) 12);// 设置字体大小
            fontb.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleb.setFont(fontb);
            styleb.setFillPattern(CellStyle.BORDER_NONE);
            styleb.setFillForegroundColor((short) 9);
            cellb.setCellStyle(styleb);
            Region regionb = new Region((short) list.size() + 2, (short) 1, (short) list.size() + 2, (short) (columnNames.length));
            sheet.addMergedRegion(regionb);
            //把工作薄写入到输出流
            wb.write(fos);
            System.out.println("生成excel成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 导入excel
     *
     * @param response
     * @param list
     * @param columnNames
     * @param columns
     * @param sheetName
     * @param filename
     * @param type        1 处理时间  2普通合计 3带保税类型合计
     */
    @SuppressWarnings("rawtypes")
    public static void exportCountByVehi(HttpServletResponse response, List<Map<String, Object>> list, String[] columnNames, String[] columns, String sheetName, String filename, Integer type) {

        OutputStream fos = null;
        try {

            // 响应输出流，让用户自己选择保存路径
            response.setCharacterEncoding("UTF-8");
            response.reset();// 清除缓存
            // response.setContentType("octets/stream");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String((filename).getBytes("UTF-8"), "iso8859-1") + ".xls");
            fos = response.getOutputStream();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFPalette palette = wb.getCustomPalette();
            HSSFRow row1 = sheet.createRow(0);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(filename);
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = wb.createFont();
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font.setFontHeightInPoints((short) 18);// 设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style1.setFont(font);
            style1.setFillPattern(CellStyle.BORDER_NONE);
            style1.setFillForegroundColor((short) 9);
            cell1.setCellStyle(style1);
            Region region = new Region((short) 0, (short) 0, (short) 0, (short) (columnNames.length - 1));
            sheet.addMergedRegion(region);
            //存储最大列宽
            Map<Integer, Integer> maxWidth = new HashMap<>();
            int excelRow = 0;
            //标题行
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                maxWidth.put(i, cell.getStringCellValue().getBytes().length * 256 + 200);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //明细行
                    Row contentRow = (Row) sheet.createRow(i + 2);
                    Map<String, Object> reParam = (Map<String, Object>) list.get(i);
                    for (int j = 0; j < reParam.size(); j++) {
                        Cell cell = contentRow.createCell(j);
                        String s = columns[j];
                        Object a = reParam.get(columns[j]);
                        if (type == 1 && j == 0) {
                            Date time = new Date(reParam.get(columns[j]).toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String timeFormat = sdf.format(time);
                            cell.setCellValue(timeFormat);
                        } else {
                            cell.setCellValue(reParam.get(columns[j]) == null ? "" : reParam.get(columns[j]).toString());
                        }
                        int length = cell.getStringCellValue().getBytes().length * 256 + 200;
                        //这里把宽度最大限制到15000
                        if (length > 15000) {
                            length = 15000;
                        }
                        maxWidth.put(j, Math.max(length, maxWidth.get(j)));
                    }
                }
            }
            for (int i = 0; i < columnNames.length; i++) {
                sheet.setColumnWidth(i, maxWidth.get(i));
            }
            if (type == 2 || type == 3) {
                Row contentRow = (Row) sheet.createRow(list.size() + 2);
                // 最后一行合计x
                int num = list.stream().mapToInt(one -> Integer.parseInt(one.get("num") == null ? "0" : one.get("num").toString())).sum();
                double fuelVol = list.stream().mapToDouble(one -> Double.parseDouble(one.get("fuelVol") == null ? "0" : one.get("fuelVol").toString())).sum();
                double quantity = list.stream().mapToDouble(one -> Double.parseDouble(one.get("quantity") == null ? "0" : one.get("quantity").toString())).sum();
                Cell cell = contentRow.createCell(type == 2 ? 2 : 4);
                cell.setCellValue(num);
                Cell cell2 = contentRow.createCell(type == 2 ? 3 : 5);
                cell2.setCellValue(fuelVol);
                Cell cell3 = contentRow.createCell(type == 2 ? 4 : 6);
                cell3.setCellValue(quantity);
                Cell cel0 = contentRow.createCell(0);
                cel0.setCellValue("合计");
                HSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
                cel0.setCellStyle(cellStyle);
                CellRangeAddress cra = new CellRangeAddress(list.size() + 2, list.size() + 2, 0, type == 2 ? 2 : 3);
                //在sheet里增加合并单元格
                sheet.addMergedRegion(cra);
            }
            wb.write(fos);
            System.out.println("生成excel成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } // 关闭输出流
        }
    }

    public static void exportFuelData(HttpServletResponse response, List<Map<String, Object>> list, String[] columnNames, String[] columns, String sheetName, String filename, int type) {

        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        OutputStream fos = null;
        try {

            // 响应输出流，让用户自己选择保存路径
            response.setCharacterEncoding("UTF-8");
            response.reset();// 清除缓存
            // response.setContentType("octets/stream");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String((filename).getBytes("UTF-8"), "iso8859-1") + ".xls");
            fos = response.getOutputStream();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFPalette palette = wb.getCustomPalette();
            HSSFRow row1 = sheet.createRow(0);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(filename);
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = wb.createFont();
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            font.setFontHeightInPoints((short) 18);// 设置字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style1.setFont(font);
            style1.setFillPattern(CellStyle.BORDER_NONE);
            style1.setFillForegroundColor((short) 9);
            cell1.setCellStyle(style1);
            Region region = new Region((short) 0, (short) 0, (short) 0, (short) (columnNames.length - 1));
            sheet.addMergedRegion(region);
            //存储最大列宽
            Map<Integer, Integer> maxWidth = new HashMap<>();
            int excelRow = 0;
            //标题行
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                maxWidth.put(i, cell.getStringCellValue().getBytes().length * 256 + 200);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //明细行
                    Row contentRow = (Row) sheet.createRow(i + 2);
                    Map<String, Object> reParam = (Map<String, Object>) list.get(i);
                    for (int j = 0; j < reParam.size(); j++) {
                        Cell cell = contentRow.createCell(j);
                        String s = columns[j];
                        Object a = reParam.get(columns[j]);
                        if (type == 1 && j == 0) {
                            Date time = new Date(reParam.get(columns[j]).toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String timeFormat = sdf.format(time);
                            cell.setCellValue(timeFormat);
                        } else {
                            cell.setCellValue(reParam.get(columns[j]) == null ? "" : reParam.get(columns[j]).toString());
                        }
                        int length = cell.getStringCellValue().getBytes().length * 256 + 200;
                        //这里把宽度最大限制到15000
                        if (length > 15000) {
                            length = 15000;
                        }
                        maxWidth.put(j, Math.max(length, maxWidth.get(j)));
                    }
                }
            }
            for (int i = 0; i < columnNames.length; i++) {
                sheet.setColumnWidth(i, maxWidth.get(i));
            }
            if (type == 2 || type == 3) {
                Row contentRow = (Row) sheet.createRow(list.size() + 2);
                // 最后一行合计x
                int addNum = list.stream().mapToInt(one -> Integer.parseInt(one.get("addCount") == null ? "0" : one.get("addCount").toString())).sum();
                double addFuelVol = list.stream().mapToDouble(one -> Double.parseDouble(one.get("addTotalVol") == null ? "0" : one.get("addTotalVol").toString())).sum();
                double addQuantity = list.stream().mapToDouble(one -> Double.parseDouble(one.get("addTotalQuantity") == null ? "0" : one.get("addTotalQuantity").toString())).sum();

                int pumpNum = list.stream().mapToInt(one -> Integer.parseInt(one.get("pumpCount") == null ? "0" : one.get("pumpCount").toString())).sum();
                double pumpFuelVol = list.stream().mapToDouble(one -> Double.parseDouble(one.get("pumpTotalVol") == null ? "0" : one.get("pumpTotalVol").toString())).sum();
                double pumpQuantity = list.stream().mapToDouble(one -> Double.parseDouble(one.get("pumpTotalQuantity") == null ? "0" : one.get("pumpTotalQuantity").toString())).sum();

                Cell cell = contentRow.createCell(type == 2 ? 3 : 4);
                cell.setCellValue(addNum);
                Cell cell2 = contentRow.createCell(type == 2 ? 4 : 5);
                cell2.setCellValue(addFuelVol);
                Cell cell3 = contentRow.createCell(type == 2 ? 5 : 6);
                cell3.setCellValue(addQuantity);

                Cell cell4 = contentRow.createCell(type == 2 ? 6 : 7);
                cell4.setCellValue(pumpNum);
                Cell cell5 = contentRow.createCell(type == 2 ? 7 : 8);
                cell5.setCellValue(pumpFuelVol);
                Cell cell6 = contentRow.createCell(type == 2 ? 8 : 9);
                cell6.setCellValue(pumpQuantity);

                Cell cel0 = contentRow.createCell(0);
                cel0.setCellValue("合计");
                HSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
                cel0.setCellStyle(cellStyle);
                CellRangeAddress cra = new CellRangeAddress(list.size() + 2, list.size() + 2, 0, type == 2 ? 2 : 3);
                //在sheet里增加合并单元格
                sheet.addMergedRegion(cra);
            }
            wb.write(fos);
            System.out.println("生成excel成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } // 关闭输出流
        }
    }
}
