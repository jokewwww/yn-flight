package com.zh.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFUtils {

    public static String imgToPdf(String imageBase64) throws IOException {
        byte[] imgByte = Base64Utils.decodeFromString(imageBase64);
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
            String base64 = Base64Utils.encodeToString(out.toByteArray());
            return base64;
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


    public static void main(String[] args) throws IOException {
        try {
            //imgToPdf("D:\\pdf\\test4.jpg","D:\\pdf\\test3.pdf");
            //manipulatePdf("D:\\pdf\\hh.pdf","D:\\pdf\\hhhhhh.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PDFUtils.pdfToImage();
    }
}
