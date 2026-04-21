package com.higer.oildataexchange.common;

import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class PDFUtils {

    /**
     * @param templateFile pdf模板
     * @param tFuelRecpt
     * @throws
     * @Title: fillTemplate
     * @Description:(根据模板生成pdf文件)
     * @param: @throws Exception
     * @return: void
     */
    public static byte[] fillTemplate(TFuelRecpt tFuelRecpt, File templateFile) {
        PdfReader reader;
        PdfStamper stamper;
        ByteArrayOutputStream bos;
        FileOutputStream out;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(templateFile);
            // 读取pdf模板
            reader = new PdfReader(bytes);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFullCompression();
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
            //往pdf合同模版里面设置值  //mock
            form.setField("Date", DateFormatUtils.format(tFuelRecpt.getFlrcDate(), Constant.YYYY_MM_DD));
            form.setField("Delivery_Type", TflightUtils.transformFlrcType(tFuelRecpt.getFlrcType()));
            form.setField("Delivery_No", tFuelRecpt.getFlrcNo());
            form.setField("Airport", tFuelRecpt.getFlrcAirport());
            form.setField("Delivered_to", tFuelRecpt.getFlrcAirlName());
            form.setField("Flight_No", tFuelRecpt.getFlrcFlightNo());
            form.setField("Aircraft_No", tFuelRecpt.getFlrcAircrftNo());
            form.setField("Aircraft_Type", tFuelRecpt.getFlrcAircrftType());
            form.setField("Departure", tFuelRecpt.getFlrcDeparture());
            form.setField("Transit_stop", tFuelRecpt.getFlrcTransit());
            form.setField("Destination", tFuelRecpt.getFlrcDest());
            form.setField("Test_bill_No", tFuelRecpt.getFlrcTestBillNo());
            form.setField("Destination_and_grade", tFuelRecpt.getFlrcFuelName());
            form.setField("Temperature", Objects.toString(tFuelRecpt.getFlrcFuelTemp()));
            form.setField("Actualdensity", Objects.toString(tFuelRecpt.getFlrcFuelDnst()));
            form.setField("Meter_Start", Objects.toString(tFuelRecpt.getFlrcMeterStat()));
            form.setField("Meter_Finish", Objects.toString(tFuelRecpt.getFlrcMeterFnsh()));
            form.setField("Figures", Objects.toString(tFuelRecpt.getFlrcFiguars()));
            form.setField("Figures_in_Words", tFuelRecpt.getFlrcFiguarsWord());
            form.setField("Quantity", Objects.toString(tFuelRecpt.getFlrcQuantity().intValue()));
            form.setField("Hydrant_Pit_No", tFuelRecpt.getFlrcHydrtPitNo());
            form.setField("Vehicle_type_and_No", tFuelRecpt.getFlrcVehiNo());
            form.setField("Time_Start", DateFormatUtils.format(tFuelRecpt.getFlrcStatTime(), Constant.YYYY_MM_DD_HH_MM_SS));
            form.setField("Time_Finish", DateFormatUtils.format(tFuelRecpt.getFlrcFnshTime(), Constant.YYYY_MM_DD_HH_MM_SS));
            form.setField("Deliver_Signature", tFuelRecpt.getFlrcDeliverName());
            String imageBase64 = tFuelRecpt.getFlrcSign();
            if (StringUtils.isNotEmpty(imageBase64)) {
                Rectangle signRect = form.getFieldPositions("Receiver_Signature").get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
//            	ImageBaseCodeUtil ImageBaseCodeUtil=new ImageBaseCodeUtil();
//            	String decodedata=ImageBaseCodeUtil.readFile(imagePath);
//            	Image image=ImageBaseCodeUtil.decoderBase64File(decodedata);
                byte[] decode = Base64Utils.decodeFromString(imageBase64);
                byte[] newImage = ImageUtils.compressPic(decode);
                if (newImage == null || newImage.length == 0) {
                    newImage = decode;
                }
                Image image = Image.getInstance(newImage);
                // 获取操作的页面
                PdfContentByte under = stamper.getOverContent(1);
                // 根据域的大小缩放图片
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();
//            Document doc = new Document();
//            PdfCopy copy = new PdfCopy(doc, bos);
//            doc.open();
//            //pdf模版的页数
//            int pagecount= reader.getNumberOfPages();
//            for(int i=1 ;i<pagecount+1;i++){
//                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
//                copy.addPage(importPage);
//            }
//            doc.close();
            // 注释 暂不开放
            // pdf转jpg
            //FileUtils.writeByteArrayToFile(new File("D:\\pdf\\test\\old.pdf"),bos.toByteArray());
            /*byte[] imageByte = ImageUtils.pdf2image(bos.toByteArray());
            if (imageByte!=null && imageByte.length>0){
                byte[] pdfByte = imgToPdf(imageByte);
                if (pdfByte!=null && pdfByte.length>0){
                    return pdfByte;
                }
                //压缩jpg
                *//*byte[] newImageByte = ImageUtils.compressPic(imageByte);
                if (newImageByte!=null && newImageByte.length>0) {
                    byte[] pdfByte = imgToPdf(newImageByte);
                    if (pdfByte!=null && pdfByte.length>0){
                        return pdfByte;
                    }
                }*//*
            }*/
            System.out.println("生成pdf成功，油单号：" + tFuelRecpt.getFlrcNo());
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            log.error("生成pdf失败,油单号:" + tFuelRecpt.getFlrcNo() + ":" + e.getMessage(), e);
            return null;
        }
    }


    public static byte[] imgToPdf(byte[] imgByte) throws IOException {
        Document document = new Document(new RectangleReadOnly(320.0F, 580F), 0, 0, 0, 0);
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            // 添加PDF文档的某些信息，比如作者，主题等等
                /*document.addAuthor("arui");
                document.addSubject("test pdf.");*/
            // 打开文档
            document.open();
            // 读取一个图片
            Image image = Image.getInstance(imgByte);
            //设置图片的绝对位置
            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();//重新设置宽高
            image.scalePercent(100);
            image.setAbsolutePosition(0, 0);
            image.scaleAbsolute(documentWidth, documentHeight);
            image.setAlignment(Image.ALIGN_CENTER);
            // 插入一个图片
            document.add(image);
            document.close();
            out.flush();
            out.close();
            //FileUtils.writeByteArrayToFile(new File("D:\\pdf3.pdf"),out.toByteArray());
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (document != null) {
                document.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return null;
    }


    public static byte[] fillTemplateTest(String font, String type, String isUse, TFuelRecpt tFuelRecpt, File templateFile) {
        PdfReader reader;
        PdfStamper stamper;
        ByteArrayOutputStream bos;
        FileOutputStream out;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(templateFile);
            // 读取pdf模板
            reader = new PdfReader(bytes);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFullCompression();
            AcroFields form = stamper.getAcroFields();
            if ("true".equals(isUse)) {
                form.addSubstitutionFont(BaseFont.createFont(font, type, BaseFont.NOT_EMBEDDED));
            }
            //往pdf合同模版里面设置值  //mock
            form.setField("Date", DateFormatUtils.format(tFuelRecpt.getFlrcDate(), Constant.YYYY_MM_DD));
            form.setField("Delivery_Type", TflightUtils.transformFlrcType(tFuelRecpt.getFlrcType()));
            form.setField("Delivery_No", tFuelRecpt.getFlrcNo());
            form.setField("Airport", tFuelRecpt.getFlrcAirport());
            form.setField("Delivered_to", tFuelRecpt.getFlrcAirlName());
            form.setField("Flight_No", tFuelRecpt.getFlrcFlightNo());
            form.setField("Aircraft_No", tFuelRecpt.getFlrcAircrftNo());
            form.setField("Aircraft_Type", tFuelRecpt.getFlrcAircrftType());
            form.setField("Departure", tFuelRecpt.getFlrcDeparture());
            form.setField("Transit_stop", tFuelRecpt.getFlrcTransit());
            form.setField("Destination", tFuelRecpt.getFlrcDest());
            form.setField("Test_bill_No", tFuelRecpt.getFlrcTestBillNo());
            form.setField("Destination_and_grade", tFuelRecpt.getFlrcFuelName());
            form.setField("Temperature", Objects.toString(tFuelRecpt.getFlrcFuelTemp()));
            form.setField("Actualdensity", Objects.toString(tFuelRecpt.getFlrcFuelDnst()));
            form.setField("Meter_Start", Objects.toString(tFuelRecpt.getFlrcMeterStat()));
            form.setField("Meter_Finish", Objects.toString(tFuelRecpt.getFlrcMeterFnsh()));
            form.setField("Figures", Objects.toString(tFuelRecpt.getFlrcFiguars()));
            form.setField("Figures_in_Words", tFuelRecpt.getFlrcFiguarsWord());
            form.setField("Quantity", Objects.toString(tFuelRecpt.getFlrcQuantity()));
            form.setField("Hydrant_Pit_No", tFuelRecpt.getFlrcHydrtPitNo());
            form.setField("Vehicle_type_and_No", tFuelRecpt.getFlrcVehiNo());
            form.setField("Time_Start", DateFormatUtils.format(tFuelRecpt.getFlrcStatTime(), Constant.YYYY_MM_DD_HH_MM_SS));
            form.setField("Time_Finish", DateFormatUtils.format(tFuelRecpt.getFlrcFnshTime(), Constant.YYYY_MM_DD_HH_MM_SS));
            form.setField("Deliver_Signature", tFuelRecpt.getFlrcDeliverName());
            String imageBase64 = tFuelRecpt.getFlrcSign();
            if (StringUtils.isNotEmpty(imageBase64)) {
                Rectangle signRect = form.getFieldPositions("Receiver_Signature").get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
//            	ImageBaseCodeUtil ImageBaseCodeUtil=new ImageBaseCodeUtil();
//            	String decodedata=ImageBaseCodeUtil.readFile(imagePath);
//            	Image image=ImageBaseCodeUtil.decoderBase64File(decodedata);
                byte[] decode = Base64Utils.decodeFromString(imageBase64);
                byte[] newImage = ImageUtils.compressPic(decode);
                if (newImage == null || newImage.length == 0) {
                    newImage = decode;
                }
                Image image = Image.getInstance(newImage);
                // 获取操作的页面
                PdfContentByte under = stamper.getOverContent(1);
                // 根据域的大小缩放图片
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();
//            Document doc = new Document();
//            PdfCopy copy = new PdfCopy(doc, bos);
//            doc.open();
//            //pdf模版的页数
//            int pagecount= reader.getNumberOfPages();
//            for(int i=1 ;i<pagecount+1;i++){
//                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
//                copy.addPage(importPage);
//            }
//            doc.close();
            // 注释 暂不开放
            // pdf转jpg
            //FileUtils.writeByteArrayToFile(new File("D:\\pdf\\test\\old.pdf"),bos.toByteArray());
            /*byte[] imageByte = ImageUtils.pdf2image(bos.toByteArray());
            if (imageByte!=null && imageByte.length>0){
                byte[] pdfByte = imgToPdf(imageByte);
                if (pdfByte!=null && pdfByte.length>0){
                    return pdfByte;
                }
                //压缩jpg
                *//*byte[] newImageByte = ImageUtils.compressPic(imageByte);
                if (newImageByte!=null && newImageByte.length>0) {
                    byte[] pdfByte = imgToPdf(newImageByte);
                    if (pdfByte!=null && pdfByte.length>0){
                        return pdfByte;
                    }
                }*//*
            }*/
            System.out.println("生成pdf成功，油单号：" + tFuelRecpt.getFlrcNo());
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            log.error("生成pdf失败,油单号:" + tFuelRecpt.getFlrcNo() + ":" + e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            byte[] bytes = FileUtils.readFileToByteArray(new File("D:\\111.png"));
            imgToPdf(bytes);
            //manipulatePdf("D:\\pdf\\hh.pdf","D:\\pdf\\hhhhhh.pdf"
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PDFUtils.pdfToImage();
    }
}
