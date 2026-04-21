package com.higer.higerservice.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class BaseUtil {
    private static final int HTTP_TIMEOUT = 1000 * 30;
    private final static String REGX_TIME = "\\d{1,2}:\\d{1,2}";
    private static Log log = LogFactory.getLog(BaseUtil.class);

    //根据名称获取spring实例化的对象
    @SuppressWarnings("unchecked")
    public static <T> T getSpringBean(ServletContext sc, String beanName, Class<? extends Object> T) {
        try {
            WebApplicationContext wx = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(sc);
            if (wx == null) return null;
            Object obj = wx.getBean(beanName);
            return obj == null ? null : (T) obj;
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
            log.debug(BaseUtil.log4stack(e));
        }
        return null;
    }

    /*
     * 日志输出
     */
    public static String log4stack(Exception e) {
        String ret = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        e.printStackTrace(ps);
        ret = new String(os.toByteArray());
        return ret;
    }

    public static String log4stackThrowable(Throwable e) {
        String ret = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        e.printStackTrace(ps);
        ret = new String(os.toByteArray());
        return ret;
    }

    /*
     * SHA加密算法
     */
    static public String encryptSHA(String inputStr) {
        byte[] inputData = inputStr.getBytes();
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
            sha.update(inputData);
            byte[] d = sha.digest();
            BigInteger bigSha = new BigInteger(d);
            return bigSha.toString(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /*
     * 获取地址栏汉字参数
     */
    public static String getvar(String uri, String varname, String encode) {
        if (uri == null)
            return null;
        String uriname = varname + "=";
        int i = uri.indexOf(uriname);
        if (-1 == i)
            return null;
        i += uriname.length();
        int j = uri.indexOf("&", i);
        if (j == -1)
            j = uri.length();
        log.debug(uri.substring(i, j));
        String var = uri.substring(i, j);
        if (var == null)
            return null;
        try {
            var = URLDecoder.decode(var, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            var = null;
        }
        return var;
    }

    /**
     * 格式化Date返回字符串
     *
     * @param date    日期字符串
     * @param pattern 格式化模式
     * @return 格式化过的日期字符串
     * @throws NullPointerException
     */
    public static String formatDate(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String result = "";
        try {
            if (date != null)
                result = sdf.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    /**
     * 格式化到分
     *
     * @param date 被格式化日期
     * @return yyyy-mm-dd HH:mm
     * @throws NullPointerException
     */
    public static String formatMin(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        String result = "";
        try {
            result = sdf.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    public static void saveFile(String fileName, String content) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(fileName, true));
            osw.write(content);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化到秒
     *
     * @param date 被格式化日期
     * @return yyyy-mm-dd HH:mm:ss
     * @throws NullPointerException
     */
    public static String formatSec(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";
        try {
            if (date != null) {
                result = sdf.format(date);
            }

        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    /**
     * 格式化到秒
     *
     * @param date 被格式化日期
     * @return yyyy-mm-dd HH:mm:ss
     * @throws NullPointerException
     */
    public static String formatSec123(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = "";
        try {
            result = sdf.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    public static String formatHour(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String result = "";
        try {
            result = sdf.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    public static String formatMinss(Date date) throws NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String result = "";
        try {
            result = sdf.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }

    /**
     * 格式化字符串返回Date
     *
     * @param date    日期
     * @param pattern 格式化模式
     * @return
     * @throws ParseException       解析异常
     * @throws NullPointerException 空
     */
    public static Date format(String date, String pattern)
            throws ParseException, NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date result = new Date();
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    /**
     * 格式化到日
     *
     * @param date 被格式化日期字符串
     * @return yyyy-mm-dd
     * @throws ParseException
     * @throws NullPointerException
     */
    public static Date formatDay(String date) throws ParseException,
            NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date result = new Date();
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    /**
     * 格式化到分
     *
     * @param date 被格式化日期字符串
     * @return yyyy-mm-dd HH:mm
     * @throws ParseException
     * @throws NullPointerException
     */
    public static Date formatMin(String date) throws ParseException,
            NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        Date result = new Date();
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    public static Date formatMinss(String date) throws ParseException, NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date result = new Date();
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    /**
     * @param date
     * @return yyyy-mm-dd HH:mm:ss
     * @throws ParseException
     * @throws NullPointerException
     */
    public static Date formatSec(String date) throws ParseException,
            NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = new Date();
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    public static String syncData(String httpUrl) {
        String strReturn = "";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setConnectTimeout(30000); // 设置超时时间
            conn.setReadTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            InputStream ins2 = conn.getInputStream();
            strReturn = convertStreamToString(ins2);
            ins2.close();
        } catch (Exception e) {
            return "error";
        }
        return strReturn;
    }

    /**
     * 流转换
     *
     * @param ins
     * @return
     */
    private static String convertStreamToString(InputStream ins) {
        String strRet = "";
        try {
            StringBuffer sbuf = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(ins, "UTF-8");
            Reader buf = new BufferedReader(reader);
            int ch;
            while ((ch = buf.read()) > -1) {
                sbuf.append((char) ch);
            }
            buf.close();
            strRet = sbuf.toString();
            sbuf.delete(0, sbuf.length());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return strRet;
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 生产文件 如果文件所在路径不存在则生成路径
     *
     * @param fileName    文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author 谭松峰
     * @date 2008-8-27
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    public static int boolToInt(boolean b) {
        if (b == true) {
            return 1;
        }
        return 0;
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String MD5(String string) {
        String str1 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str1 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str1;
    }

    /**
     * 获取byte数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        byte[] bytes = null;
        try {
            InputStream is = new FileInputStream(file);
            // 获取文件大小
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // 文件太大，无法读取
                // throw new IOException("文件太大无法读取 "+file.getName());
            }
            // 创建一个数据来保存文件数据
            bytes = new byte[(int) length];
            // 读取数据到byte数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset < bytes.length) {
                // throw new IOException("文件未读取完整 "+file.getName());
            }
            is.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return bytes;
    }

    /**
     * @param key
     * @param value
     * @param fileURL void
     * @Title: writeData
     * @Description: TODO读取配置文件改变值
     */
    public static void main(String[] args) {
        writeData("password", "qqqqqq");

        String fileIO = getFileIO("password");
        System.out.println(fileIO);


    }

    public static void writeData(String key, String value) {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            URL url = BaseUtil.class
                    .getResource("/cn/higer/config/db.properties");
            File file = new File(url.toURI());
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();// 一定要在修改值之前关闭fis
            fos = new FileOutputStream(file);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");


            fos.close();
            System.out.println("" + prop);

        } catch (IOException e) {
            System.err.println("Visit  for updating " + value + " value error");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @param name
     * @param fileURL
     * @return String
     * @Title: getFileIO
     * @Description: TODO获取配置文件的值
     */
    public static String getFileIO(String name) {
        Properties prop = new Properties();
        InputStream in = BaseUtil.class
                .getResourceAsStream("/cn/higer/config/db.properties");
        try {
            prop.load(in);
            return new String(prop.getProperty(name).getBytes("ISO-8859-1"), "utf-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * int 转 BOOL
     *
     * @param param
     * @return
     */
    // public static boolean intToBool(int param) {
    // if (param == 1) {
    // return true;
    // }
    // return false;
    // }
    public static boolean intToBool(int param) {
        if (param == 20) {
            return true;
        }
        return false;
    }

    /**
     * 设置系统时间
     */
    public static void setSystemTime(String timeStr) {
        String osName = System.getProperty("os.name");
        String cmd = "";
        String date = timeStr.substring(0, 10);
        String time = timeStr.substring(11);
        try {
            if (osName.matches("^(?i)Windows.*$")) {// Window 系统
                cmd = "  cmd /c time " + time;
                Runtime.getRuntime().exec(cmd);
                cmd = " cmd /c date " + date;
                Runtime.getRuntime().exec(cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getConn(String URL, String NAME, String PASS) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");// 加载Mysql数据驱动
            DriverManager.getConnection("jdbc:oracle:thin:@" + URL, NAME, PASS);// 创建数据连接
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断数组数据是否为合法图片
     *
     * @param imageContent
     * @return
     */
    public static boolean isImage(byte[] imageContent) {
        if (imageContent == null || imageContent.length == 0) {
            return false;
        }
        Image img = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(imageContent);
            img = ImageIO.read(is);
            if (img == null || img.getWidth(null) <= 0
                    || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
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

    public static String getCatalinaHome() {
        String property = System.getProperty("catalina.home");
        return property.substring(0, 1);

    }

    public static boolean writeData(String path, byte[] b) {
        File f = new File(path);
        try {
            File pf = f.getParentFile();
            if (!pf.exists())
                pf.mkdirs();
            if (!f.exists())
                f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            fout.write(b);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // post提交表单
    public static byte[] postform(String httpurl, byte b[]) {
        byte bb[] = null;
        try {
            URL url = new URL(httpurl);
            HttpURLConnection urlconn = (HttpURLConnection) url
                    .openConnection();
            if (b == null) {
                urlconn.setRequestMethod("GET");
            } else {
                urlconn.setRequestMethod("POST");
                urlconn.setDoOutput(true);
            }
            urlconn.setConnectTimeout(HTTP_TIMEOUT);
            urlconn.setReadTimeout(HTTP_TIMEOUT);
            urlconn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlconn.connect();
            if (b != null) {
                OutputStream os = urlconn.getOutputStream();
                os.write(b);
                os.close();
            }
            InputStream in = null;
            int code = urlconn.getResponseCode();
            if (code == 200) {
                in = urlconn.getInputStream();
            } else {
                in = urlconn.getErrorStream();
            }
            if (in != null) {
                bb = readbodydata(in, 0);
                in.close();
            }
            urlconn.disconnect();

        } catch (IOException e) {
            log.debug("网络异常,等待超时:" + e);
        }
        return bb;
    }

    /**
     * 读入流文件
     *
     * @param in
     * @param size
     * @return
     */
    public static byte[] readbodydata(InputStream in, int size) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final int DEFAULT_SIZE = 8192;
        try {
            if (size == 0)
                size = DEFAULT_SIZE;
            byte[] buf = new byte[size];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                bout.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();
    }

    /**
     * int转String 自动补全
     *
     * @param num
     * @return
     */
    public static String DecimalIntToString(int num) {
        DecimalFormat df = new DecimalFormat("000000");
        return df.format(num);
    }

    public static String DecimalIntToString_Long(int num) {
        DecimalFormat df = new DecimalFormat("00000000");
        return df.format(num);
    }

    /**
     * 比较考生zp
     *
     * @param img
     * @return
     */
    public static boolean checkZp(byte[] img, byte[] stuImg, String zpUrl) {
        try {
            // URL url=new URL("http://127.0.0.1:30000/query");
            URL url = new URL(zpUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("func", "102");
            urlConnection.setDoOutput(true);
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(BaseUtil.DecimalIntToString_Long(img.length).getBytes());// 车载指纹图片长度
            outStream.write(img);// 写入车在指纹图片信息
            outStream.write(BaseUtil.DecimalIntToString_Long(1).getBytes());
            outStream.write(BaseUtil.DecimalIntToString_Long(stuImg.length).getBytes());
            outStream.write(stuImg);
            outStream.flush();
            outStream.close();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();

            String body = IOUtils.toString(inputStream, encoding).trim();

            log.debug("照片比对返回======" + body + "===============");
            if (body.equals("-1")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            log.debug("指纹服务异常:" + e);
            return false;
        }

    }

    public static String getText(String path) throws IOException {
        String text = "";
        try {
            File file = new File(path);
            String lineTxt = "";

            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read;
                read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // System.out.println(lineTxt);
                    text += lineTxt;

                }
                read.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    public static byte[] getFile(String path) throws IOException {
        try {
            File file = new File(path);

            if (file.isFile() && file.exists()) { // 判断文件是否存在
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte[] temp = new byte[1024];
                int size = 0;
                while ((size = in.read(temp)) != -1) {
                    out.write(temp, 0, size);
                }
                in.close();
                byte[] con = out.toByteArray();
                return con;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /*rht add 2018-08-09*/
    // 查询视频转发存储使用情况
    public static String videoStorageUse(String urlPath)
            throws IOException {

        try {
            URL url = new URL(urlPath + "query_resource.action");

            // 转换成JSON格式

            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);

            OutputStream outStream = urlConnection.getOutputStream();
            String jsonString = "diskinfo";
            byte[] bpost = jsonString.getBytes("UTF-8");
            outStream.write(bpost);
            // outStream.setContentLength(bpost.length);
            outStream.flush();
            outStream.close();

            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String result = IOUtils.toString(inputStream, encoding).trim();
            // 接收返回信息
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
	
	
/*	static public void OutputStreamByByte(byte[] bpost) {

		try {
			HttpServletResponse response = getResponse();
			response.setContentType("application/json");
			response.setContentLength(bpost.length);
			System.out.println("bpost.length-----"+bpost.length);
			OutputStream outs = response.getOutputStream();
			outs.write(bpost);
			outs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/

    /**
     * 获得指定日期的后一天
     *
     * @return
     */
    public static Date getSpecifiedDayAfter(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }

    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    /************************************ 图片压缩相关方法 *****************************************/
    /*
     * w:压缩后照片宽度 h:压缩后照片高度
     */
    public static byte[] changeimg(byte[] data, int w, int h) {
        Image img;
        try {
            img = ImageIO.read(new ByteArrayInputStream(data));
            int width = img.getWidth(null); // 得到源图宽
            int height = img.getHeight(null); // 得到源图长
            w = (int) (width * h / height);
            BufferedImage image = new BufferedImage(w, h,
                    BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", bais);
            return bais.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            log.debug(BaseUtil.log4stack(e));
            return null;
        }
    }

    public static byte[] reduceImg(byte[] data, int widthdist, int heightdist) {
        try {

            Image src = ImageIO.read(new ByteArrayInputStream(data));

            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpg", bais);
            return bais.toByteArray();


        } catch (IOException ex) {
            return null;
        }
    }

    public static byte[] reduceImg_jpeg(byte[] data, int widthdist, int heightdist) {
        try {

            Image src = ImageIO.read(new ByteArrayInputStream(data));

            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            ImageIO.write(tag, "JPEG", bais);
            return bais.toByteArray();


        } catch (IOException ex) {
            return null;
        }
    }

    // png转jpg
    public static String pngToJpg(String zp) {
        try {
            Base64 base64 = new Base64();
            byte[] decode = base64.decode(zp);
            ByteArrayInputStream in = new ByteArrayInputStream(decode); // 将b作为输入流；
            BufferedImage bufferedImage;

            bufferedImage = ImageIO.read(in);

            BufferedImage newBufferedImage = new BufferedImage(
                    bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                    Color.WHITE, null);

            ByteArrayOutputStream os = new ByteArrayOutputStream();// 新建流。

            ImageIO.write(newBufferedImage, "jpg", os);// 利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte b[] = os.toByteArray();

            String zpbase = base64.encodeToString(b);
            return zpbase;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.debug(e);
            return null;
        }
    }

    public static String GpsStrToDouble(String str, boolean isGphpd) {
        double res = 0;// 结果
        if (isGphpd) {
            try {
                res = Double.valueOf(str);
            } catch (Exception e) {
                res = 0;
            }
            res = res * Math.PI / 180;
            return res + "";
        }

        String s = "";
        if (str.length() < 12) {
            return 0 + "";
        }

        int i;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i + 2) == '.') {
                res = strToInt(s);
                break;
            } else {
                s = s + str.charAt(i);
            }
        }
        s = "";
        for (int j = i; j < str.length(); j++) {
            s = s + str.charAt(j);
        }

        try {
            res = res + Double.valueOf(s) / 60;

        } catch (Exception e) {
            res = 0;
        }
        //res = res * Math.PI / 180;
        return res + "";
    }

    public static int strToInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getsj(int i) {

        String time = String.valueOf(i);  //获取内存定时时间
        String times = time.toString();
        String times2 = null;
        if (times.length() == 3) {
            times2 = "0" + times.substring(0, 1) + ":" + times.substring(1);
        } else {
            times2 = times.substring(0, 2) + ":" + times.substring(2);
        }
        time = times2;
        //分割字符串

        if (time == null) time = "07:00";

        if (!time.matches(REGX_TIME)) {
            log.debug("预约定时不合格" + time);
            time = "07:00";
        }
        time += ":00";
        return time;
    }

    public static boolean saveBytesToFile(String path, byte[] bfile) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String parent = "", fileName = "";
        int index = path.lastIndexOf("/");
        System.out.println(index);
        if (index != -1) {
            parent = path.substring(0, index);
            System.out.println(parent);
        }
        try {
            File dir = new File(parent);
            System.out.println(dir);
            if (!dir.exists()) {
                boolean d = dir.mkdirs();
                System.out.println("======" + d);
            }
            file = new File(path);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public static String qudouhao(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String regex = "^,*|,*$";
        return str.replaceAll(regex, "");

    }

    public static String getGpsFlagStr(int flag) {// GPS信息
        String str = "";
        switch (flag) {
            case -1:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notlink.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 0:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 1:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 2:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 3:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 4:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_gdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;
            case 5:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;

            case 6:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>";
                break;

            default:
                str = str + "<span style='float:right; font-size:22.8px;margin-left:7px; padding-top:1px; color:red;'><img src='images/obd/gps_notgdj.png' style='float:right;height: 23px; margin-left: 5px;padding-top:1px;'></span>" + Integer.toString(flag);
                break;
        }
        return str;
    }

    public static int carBiLi(double bl, int tuW, int sjW) {
        BigDecimal bd = new BigDecimal(tuW / (sjW * bl)).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());

    }


    public static String getSql(String string) {


        InputStream inputStream = null;
        try {
            inputStream = BaseUtil.class.getResourceAsStream(string);
            byte[] bytes = new byte[0];
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String str = new String(bytes, "utf-8");
            return str;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
