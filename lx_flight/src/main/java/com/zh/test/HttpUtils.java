package com.zh.test;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private static HttpUtils instance;

    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (null == instance) {
            instance = new HttpUtils();
        }
        return instance;
    }

    /**
     * @param paramMap
     * @param method
     * @return String
     * @Description: 格式化参数
     * @author: Song
     * @date: 2017年5月19日 上午10:26:21
     */
    private static String formatParam(Map<String, String> paramMap, String method) {
        StringBuffer _param = new StringBuffer();
        String param = "";
//		if("get".equals(method))
//			_param.append("?");
        for (String key : paramMap.keySet()) {
            _param.append(key + "=" + paramMap.get(key) + "&");
        }
        param = _param.toString();
        param = param.substring(0, param.length() - 1);

//		if("get".equals(method))
//			param=URLEncoder.encode(param);

        return param;
    }

    /**
     * @Description: 向指定地址发送GET请求
     * @Param: url:请求地址
     * @Param: param:参数
     * @Author: LRC
     * @Date: 2018-08-22
     */
    public static String sendGet(String url, Map<String, String> param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + formatParam(param, "get");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //1.获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
            //2.中文有乱码的需要将PrintWriter改为如下
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("post推送结果：" + result);
        return result;
    }

    //所有异常外抛
    public static String postBody(String url, String body, String contentType) throws Exception {
        // 实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 实例化post方法
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(10000)
                .setSocketTimeout(50000).build();
        httpPost.setConfig(requestConfig);
        // 结果
        CloseableHttpResponse response = null;
        String content = null;
        httpPost.setHeader("Content-Type", contentType);
        try {
            // 将参数给post方法
            if (body != null) {
                StringEntity stringEntity = new StringEntity(body, "UTF-8");
                httpPost.setEntity(stringEntity);
            }
            // 执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                try {
                    content = EntityUtils.toString(response.getEntity(), "UTF-8");
                } finally {
                    response.close();
                }
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 国辉组 post请求方法
     *
     * @param url
     * @param body
     * @return
     */
    public static String postBody(String url, byte[] body) {
        try {
            URL urls = new URL(url);
            //http://127.0.0.1:30000/query
            URLConnection urlConnection = urls.openConnection();
            urlConnection.setDoOutput(true);
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(body);
            outStream.flush();
            outStream.close();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String bodys = IOUtils.toString(inputStream, encoding);
            String msg = "";// BaseUtil.formatSec(new Date()) +" : "+bodys;
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getReturn(InputStream is) throws IOException {
        System.out.println("------------------------------------------------------");
        System.out.println("InputStream length is: " + is.available());
        System.out.println("------------------------------------------------------");
//		if(is.available()<=0)
//			return "fail";
        byte[] bt = new byte[1024];
        int len = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(bt)) > 0) {
            baos.write(bt, 0, len);
        }
        String reStr = new String(baos.toByteArray(), "UTF-8");
        return null != reStr && reStr.length() > 0 ? reStr : "fail";
    }

}
