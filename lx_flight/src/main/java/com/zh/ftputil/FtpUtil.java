package com.zh.ftputil;

import com.zh.bean.flight.MyCustom;
import com.zh.bean.flight.MyFlightCode;
import com.zh.service.FlightService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/3/20 10:16
 * @Description:
 */
@RestController
@RequestMapping(value = "/FtpUtil")
public class FtpUtil implements ITask<ResultBean<String>, Object> {
    private static FtpUtil filter;
    private final AtomicLong flgtupdatecount = new AtomicLong(0);
    private final AtomicLong flgtinsertcount = new AtomicLong(0);
    private final AtomicLong cusmupdatecount = new AtomicLong(0);
    private final AtomicLong cusminsertcount = new AtomicLong(0);
    FtpClient ftpClient;
    @Autowired
    private FlightService flightService;

    /**
     * 连接FTP服务
     *
     * @param url           //IP地址
     * @param port//端口号
     * @param username//用户名
     * @param password//密码
     * @return
     */
    public static FtpClient connectFTP(String url, int port, String username, String password) {
        //创建ftp
        FtpClient ftp = null;
        try {
            //创建地址
            SocketAddress addr = new InetSocketAddress(url, port);
            //连接
            ftp = FtpClient.create();
            ftp.connect(addr);
            //登陆
            ftp.login(username, password.toCharArray());
            ftp.setBinaryType();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }

    /**
     * 取ftp上的文件内容
     *
     * @param ftpFile
     * @param ftp
     * @return
     */
    public static List<String> download(String ftpFile, FtpClient ftp) {
        List<String> list = new ArrayList<>();
        String str = "";
        InputStream is = null;
        BufferedReader br = null;
        try {
            // 获取ftp上的文件
            is = ftp.getFileStream(ftpFile);
            //转为字节流
            br = new BufferedReader(new InputStreamReader(is));
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setFlightService(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostConstruct
    public void init1() {
        filter = this;
        filter.flightService = this.flightService;   // 初使化时将已静态化的UtilService实例化
    }

    @PostMapping(value = "/readAndWriteCus")
    public String readAndWriteCus() {
        FtpClient ftp = connectFTP("111.203.202.56uuuuuuuuuuuuuuyyy", 21, "znjyuser", "ZNJYadmin123!");
        List<String> list = download("afams/diaodu/Customerno.txt", ftp);
        int readnum = 0, editnum = 0, insertnum = 0, errornum = 0;
        List<Object> alllist = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            readnum = list.size();
            String strOut = null;
            if (list.get(i) != null) {
                strOut = list.get(i);
                String[] split = strOut.split("\t");
                if (split.length >= 3) {
                    MyCustom myCustom = new MyCustom();
                    if (StringUtils.isNotEmpty(split[0])) {
                        myCustom.setCstmNum(split[0]);
                        if (StringUtils.isNotEmpty(split[1])) {
                            myCustom.setCstmRegion(split[1]);
                        }
                        if (StringUtils.isNotEmpty(split[2])) {
                            myCustom.setCstmName(split[2]);
                        }
                        System.out.println("新增航空加油客户" + strOut);
                        alllist.add(myCustom);
                        insertnum++;
                        System.out.println("custom" + split[2]);
                    }
                } else {
                    System.out.println("航空加油客户字段不齐" + strOut);
                    errornum++;
                }
            }
            System.out.println(strOut);
        }
        // 创建多线程处理任务
        MultiThreadUtils<Object> threadUtils = MultiThreadUtils.newInstance(5);
        ITask<ResultBean<String>, Object> task = new FtpUtil();
        // 辅助参数  加数
        Map<String, Object> params = new HashMap<>();
        params.put("table", "myCustom");
        // 执行多线程处理，并返回处理结果
        ResultBean<List<ResultBean<String>>> resultBean = threadUtils.execute(alllist, params, task);
        System.out.println("custom读取个数:" + readnum + ",处理个数:" + insertnum + ",字段错误个数" + errornum);
        return "custom读取个数:" + readnum + ",处理个数:" + insertnum + ",字段错误个数" + errornum;
    }

    //    @PostMapping(value = "/readAndWrite")
    public String readAndWrite() {
        FtpClient ftp = connectFTP("111.203.202.56", 21, "znjyuser", "ZNJYadmin123!");
        List<String> list = download("afams/diaodu/Plane-cusno.txt", ftp);
//        List<String> typelist = download("afams/diaodu/Planetype.txt", ftp);
        Map<String, String> typemap = new HashMap<String, String>();
        List<Object> alllist = new ArrayList<Object>();
//        List<Object> updatelist=new ArrayList<Object>();
        int readnum = 0, editnum = 0, insertnum = 0, errornum = 0;
        for (int i = 0; i < list.size(); i++) {
            readnum = list.size();
            String strOut = null;
            if (list.get(i) != null) {
                strOut = list.get(i);
                String[] split = strOut.split("\t");
                if (split.length >= 5) {
                    MyFlightCode myFlightCode = new MyFlightCode();
                    if (StringUtils.isNotEmpty(split[0]) && StringUtils.isNotEmpty(split[2])) {
                        myFlightCode.setArcrRegn(split[0]);
                        Date date = null;
                        try {
                            String statrDate = split[2].replace(".", "-");
                            date = DateUtils.parseDate(statrDate, "yyyy-MM-dd");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (date != null) {
                            myFlightCode.setArcrStartDate(date);
                            System.out.println(myFlightCode.getArcrRegn() + myFlightCode.getArcrStartDate().toString());
                            if (StringUtils.isNotEmpty(split[1])) {
                                myFlightCode.setArcrCustomNum(split[1]);
                            }
                            if (StringUtils.isNotEmpty(split[3])) {
                                myFlightCode.setArcrName(split[3]);
                            } else {
                                String acname = typemap.get(myFlightCode.getArcrRegn());
                                if (StringUtils.isNotEmpty(acname)) {
                                    myFlightCode.setArcrName(acname);
                                }
                            }
                            Date endDate = null;
                            try {
                                String endDatestr = split[4].replace(".", "-");
                                endDate = DateUtils.parseDate(endDatestr, "yyyy-MM-dd");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (endDate != null) {
                                myFlightCode.setArcrEndDate(endDate);
                                alllist.add(myFlightCode);
                                insertnum++;
                                System.out.println("飞机所属客户对照:" + strOut);
                            } else {
                                System.out.println("飞机所属客户对照结束日期转化失败———" + strOut);
                            }
                        } else {
                            System.out.println("飞机所属客户对照开始日期转化失败———" + strOut);
                        }
//                        System.out.println("custom" + split[2]);
                    } else {
                        System.out.println("飞机所属客户对照数据不齐——" + strOut);
                    }
                } else {
                    System.out.println("飞机所属客户对照数据不齐——" + strOut);
                    errornum++;
                    System.out.println(list.get(i).toString());
                }

            }
            System.out.println(strOut);
        }
        // 创建多线程处理任务
        MultiThreadUtils<Object> threadUtils = MultiThreadUtils.newInstance(5);
        ITask<ResultBean<String>, Object> task = new FtpUtil();
        // 辅助参数  加数
        Map<String, Object> params = new HashMap<>();
        params.put("table", "myFlightCode");
        // 执行多线程处理，并返回处理结果
        ResultBean<List<ResultBean<String>>> resultBean = threadUtils.execute(alllist, params, task);
        System.out.println("flightcode读取个数:" + readnum + ",处理个数:" + insertnum + ",字段错误个数" + errornum);
        return "flightcode读取个数:" + readnum + ",处理个数:" + insertnum + ",字段错误个数" + errornum;
    }

    //    @Scheduled(cron = "0 25 13 ? * *")
    @Scheduled(cron = "0 0 0 */1 * ?")
    @PostMapping(value = "/ftpRead")
    public void ftpRead() {
        String cus = readAndWriteCus();
        String filghtCode = readAndWrite();
        System.out.println(cus + filghtCode);
//        System.out.println("新增"+cusminsertcount.get()+"修改"+cusmupdatecount.get()+"新增"+flgtinsertcount.get()+"修改"+flgtupdatecount.get());
    }

    @Override
    public ResultBean execute(Object e, Map<String, Object> params) {
        /**
         * 具体业务逻辑：将list中的元素加上辅助参数中的数据返回
         */
        String table = (String.valueOf(params.get("table")));
//        String result = (String.valueOf(params.get("result")));
//        e = e + addNum;
        if (table.equals("myFlightCode")) {
            MyFlightCode myFlightCode = (MyFlightCode) e;
            MyFlightCode temp = new MyFlightCode();
            temp.setArcrRegn(myFlightCode.getArcrRegn());
            temp.setArcrStartDate(myFlightCode.getArcrStartDate());
//            SpringContextUtils
            MyFlightCode oldtemp = filter.flightService.getFLIGHTCODEFind(temp);
            if (oldtemp != null) {
                filter.flightService.updateFLIGHTCODE(myFlightCode);
//                flgtupdatecount.incrementAndGet();
            } else {
                filter.flightService.addFLIGHTCODE(myFlightCode);
//                flgtinsertcount.incrementAndGet();
            }
        } else if (table.equals("myCustom")) {
            MyCustom myCustom = (MyCustom) e;
            MyCustom temp = new MyCustom();
            temp.setCstmNum(myCustom.getCstmNum());
            MyCustom oldtemp = filter.flightService.getCUSTOMFind(temp);
            if (oldtemp != null) {
                filter.flightService.updateCUSTOM(myCustom);
//                cusmupdatecount.incrementAndGet();
            } else {
                filter.flightService.addCUSTOM(myCustom);
//                cusminsertcount.incrementAndGet();
            }
        }
        ResultBean<String> resultBean = ResultBean.newInstance();
        resultBean.setData(e.toString());
        return resultBean;
    }
}
