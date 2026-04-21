package com.higer.higerservice.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FaceComparisonUtil {


    private static String urlPath = "http://127.0.0.1:30000/";

    //默认地址为http://127.0.0.1:30000/
    public static void setUrlPath(String url) {
        urlPath = url;
    }
    //人脸比对返回成功或者失败

    /**
     * @param base64     原图片
     * @param base64List 待比对的图片List
     * @return
     * @throws IOException
     */
    public static boolean comparison(String base64,
                                     List<String> base64List) throws IOException {

        try {
            List<String> list = new ArrayList<String>();
            list.add(base64);
            List<String> baseZpTzm = facetrans(list);
            if (baseZpTzm != null && baseZpTzm.size() > 0 && !"".equals(baseZpTzm.get(0))) {

                List<String> zpTzms = facetrans(base64List);

                if (zpTzms != null && zpTzms.size() > 0) {

                    for (int i = 0; i < zpTzms.size(); i++) {
                        if ("".equals(zpTzms.get(i))) {
                            int k = i + 1;
                            System.out.println("待比对的第--" + k + "--张照片转换特征码失败  ");
                        }
                    }
                    boolean facecompare = facecompare(baseZpTzm.get(0), zpTzms);
                    return facecompare;
                } else {

                    System.out.println("待比对的图片转换特征码全部失败!");
                    return false;
                }
            } else {
                System.out.println("原始图片转换特征码失败!");

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //	log.debug(BaseUtil.log4stack(e));
            System.out.println("比对失败" + e);

            return false;
        }
    }

    /**
     * base转特征码
     *
     * @param base64List
     * @return
     * @throws IOException
     */
    public static List<String> facetrans(List<String> base64List)
            throws IOException {
        try {
            // "http://127.0.0.1:30000/facetrans.action"
            URL url = new URL(urlPath + "facetrans.action");
            List<Zp> zps = new ArrayList<Zp>();
            for (String base64 : base64List) {
                Zp p = new Zp();
                p.setZp(base64);
                zps.add(p);
            }
            // 添加2个base64的图片

            // 转换成JSON格式
            String jsonString = JSON.toJSONString(zps);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(jsonString.getBytes());
            outStream.flush();
            outStream.close();

            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String result = IOUtils.toString(inputStream, encoding).trim();
            // 接收返回信息
            List<String> strList = JSON.parseArray(result, String.class);
            return strList;
			/*
			Picture person = JSON.parseObject(result, Picture.class);
			System.out.println("code------------" + person.getCode());
			// 返回的照片特征码
			if (person.getCode() == 0) {
				List<Zp> pictures = person.getPictures();
				return pictures;
			} else {
				return null;
			}*/
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    //

    /**
     * 特征码比较
     *
     * @param tzm    现场招聘
     * @param zpList
     * @return
     */
    public static boolean facecompare(String tzm,
                                      List<String> zpList) {
        try {
            URL url = new URL(urlPath + "facecompare.action");
            // 先把要原始照片放到list;
            Zp p = new Zp();
            p.setZp(tzm);

            List<Zp> zps = new ArrayList<Zp>();
            zps.add(p);
            // 再吧要比对的特征码放到list里
            for (int i = 0; i < zpList.size(); i++) {
                Zp zp = new Zp();
                zp.setZp(zpList.get(i));
                zps.add(zp);
            }
            String jsonString = JSON.toJSONString(zps);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(jsonString.getBytes());
            outStream.flush();
            outStream.close();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String body = IOUtils.toString(inputStream, encoding).trim();
            Picture person = JSON.parseObject(body, Picture.class);
            // 当code大于0表示人脸识别比对成功了
            System.out.println("code+++++++======:" + person.getCode());
            if (person.getCode() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            System.out.println("服务异常:" + e);
            return false;
        }

    }

    public static void main(String[] args) {
        try {
            //1.初始化URL地址端口默认为30000
            FaceComparisonUtil.setUrlPath("http://127.0.0.1:30000/");
            //2.进行人脸比对第一个参数为原始图片的base64码第2个参数为待比对的图片base64码list
            String base64 = "";
            List<String> base64List = new ArrayList<String>();
            boolean comparison = FaceComparisonUtil.comparison(base64, base64List);
            System.out.println("比对结果" + comparison);
        } catch (Exception e) {

            System.out.println("服务异常:" + e);
        }

    }

    /**
     * 流文件转byte
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static List<String> facecomparesome(String tzm, List<String> zpList) {
        try {

            URL url = new URL(urlPath + "facecomparesome.action");
            // 先把要原始照片放到list;
            Zp p = new Zp();
            p.setZp(tzm);

            List<Zp> zps = new ArrayList<Zp>();
            zps.add(p);
            // 再吧要比对的特征码放到list里
            for (int i = 0; i < zpList.size(); i++) {
                p = new Zp();
                p.setZp(zpList.get(i));
                zps.add(p);
            }
            String jsonString = JSON.toJSONString(zps);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(jsonString.getBytes());
            outStream.flush();
            outStream.close();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String body = IOUtils.toString(inputStream, encoding).trim();

            List<String> strList = JSON.parseArray(body, String.class);
            // 当code大于0表示人脸识别比对成功了
            for (int i = 0; i < strList.size(); i++) {

                System.out.println("code+++++++======:" + strList.get(i));
            }

            return strList;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("服务异常:" + e);
            return null;
        }

    }

}

