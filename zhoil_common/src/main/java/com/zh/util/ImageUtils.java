package com.zh.util;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;

public class ImageUtils {

    /**
     * 压缩质量0-1
     */
    private static float quality = 0.64F;
    /**
     * pdf转图片dpI
     */
    private static float dpi = 140F;
    /**
     * 根据业务需求：把/home/before下的文件夹中的pdf转成图片，按照A4纸张尺寸进行切图
     */
    public static byte[] pdf2image(byte[] pdfBype) {
        PDDocument pdDocument;
        try {
            pdDocument = PDDocument.load(pdfBype);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            PdfReader pdfReader = new PdfReader(pdfBype);
            int pages = pdfReader.getNumberOfPages();
            //只取第一页
            //for (int i = 0; i < pages; i++) {
                //这里可以设置成png、jpeg格式的图片
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                //这里的DPI可以先按照大一点的来设置，一般是92-98之间
                BufferedImage image = renderer.renderImageWithDPI(0, dpi);
                ImageIO.write(image, "jpg", out);// jpg
                image.flush();
            //}
            pdDocument.close();
//            FileUtils.writeByteArrayToFile(new File("D:\\pdf\\test\\im1.jpg"),out.toByteArray());
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建目录，如果存在目录，就不创建
     */
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    /**
     * @param   原图片流
     * @return
     * @throws IOException
     */
    public static byte[] compressPic(byte[] imageByte) throws IOException {
        //File file = new File("D:\\pdf\\test4.jpg");
        InputStream inputStream = new ByteArrayInputStream(imageByte);
        BufferedImage src = null;
        ByteArrayOutputStream out = null;
        // 指定写图片的方式为 jpg
        ImageWriter imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
                null);

        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality(quality);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        //FileUtils.writeByteArrayToFile(file,oldImage);
        ColorModel colorModel = ImageIO.read(inputStream).getColorModel();// ColorModel.getRGBdefault();

        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(16, 16)));
        try {
         /*   if (oldImage==null || oldImage.length==0) {
                return null;
            } else*/ {
                inputStream = new ByteArrayInputStream(imageByte);
                src = ImageIO.read(inputStream);
                out = new ByteArrayOutputStream();
                imgWrier.reset();
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
                // OutputStream构造
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                // 调用write方法，就可以向输入流写图片
                imgWrier.write(null, new IIOImage(src, null, null),
                        imgWriteParams);
                FileUtils.writeByteArrayToFile(new File("D:\\pdf\\test\\im2.jpg"),out.toByteArray());
                return out.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            out.flush();
            out.close();
        }
    }



    public static void main(String[] args) {
        try {
            //compressPic();

            //insertImagesInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}