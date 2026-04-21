package com.higer.higerservice.component;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.usage.DataBiz;
import com.higer.higerservice.util.ErrorDataSend;
import com.higer.higerservice.util.ErrorDoPostSend;
import com.pro.net.ServerSocketThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/6 20:39
 * @Description:
 */
@Component
public class MyBootListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(MyBootListener.class);

    private static ErrorDataSend errorDataSend = null;

    private static ServerSocketThread serverSocketThread = null;

    private static DataBiz dataBiz = null;

    private static ErrorDoPostSend errorDoPostSend = null;

    private static String webVersion = "1.6";
    @Value("${lockTest}")
    private Boolean lock;

//    @Value("${spring.datasource.url}")
//    private String mysqlUrl;


    public static void startWork(int port) {
        if (null != serverSocketThread) {
            serverSocketThread.stopWork();
            serverSocketThread = null;
        }

        if (null == serverSocketThread) {
            serverSocketThread = new ServerSocketThread(port, dataBiz);
            System.out.println("当前的jar版本是----:" + serverSocketThread.getJarVer());
            serverSocketThread.startWork();
        }
        if (null != errorDataSend) {
            errorDataSend.stopWork();
            errorDataSend = null;
        }
        if (null == errorDataSend) {
            errorDataSend = new ErrorDataSend();
            errorDataSend.startWork();
        }

        if (null != errorDoPostSend) {
            errorDoPostSend.stopWork();
            errorDoPostSend = null;
        }
        if (null == errorDoPostSend) {
            errorDoPostSend = new ErrorDoPostSend();
            errorDoPostSend.startWork();
        }

    }

    public static void stopWork() {
        if (null != serverSocketThread) {
            serverSocketThread.stopWork();
            serverSocketThread = null;
        }
    }

    public static void stopWorkKafka() {
        if (null != errorDataSend) {
            errorDataSend.stopWork();
            errorDataSend = null;
        }
    }

    public static void stopWorkPost() {
        if (null != errorDoPostSend) {
            errorDoPostSend.stopWork();
            errorDoPostSend = null;
        }
    }

    public static void notifyKafka() {
        //上线之后开启
        errorDataSend.notifyDo();
    }

    public static void notifyPost() {
        //上线之后开启
        errorDoPostSend.notifyDo();
    }

    public int setTabcolumnname(String tabname, String columnname, EntityManager em, ApplicationContext applicationContext, String val) {

        String sql = "SELECT COUNT(*) FROM information_schema.columns WHERE  table_name = '" + tabname + "' AND column_name='" + columnname + "';";
        System.out.println(sql);
        javax.persistence.Query query = em.createNativeQuery(sql);
        List<Object> resultList = query.getResultList();
        int i = Integer.parseInt(resultList.get(0).toString());
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String url = conn.getMetaData().getURL();
            System.out.println("监听数据库 链接地址 ------- " + url);
            logger.info("监听数据库 链接地址 : : " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i == 0) {
            sql = "alter table " + tabname + " add COLUMN " + val + "  DEFAULT NULL";
            try {
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println(i);
        return 1;

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //   logger.info("mysqlUrl --------------"+mysqlUrl);
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        ObjectProperties bean = applicationContext.getBean(ObjectProperties.class);
        try {
            EntityManager em = applicationContext.getBean(EntityManager.class);
            //  setTabcolumnname("a_apk_version", "type", em, applicationContext, " type int ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebSocketService webSocketService = applicationContext.getBean(WebSocketService.class);
        String myProt = bean.getMyProt();
        if (!StringUtils.isEmpty(myProt)) {
            Integer prot = Integer.valueOf(myProt);
            dataBiz = applicationContext.getBean(DataBiz.class);
            MyBootListener.startWork(prot);
            logger.info("TCP启动成功 ------ 端口是" + prot);
        } else {
            logger.info("TCP启动失败 ------ 无端口信息");
        }

        logger.info("当前版本: " + webVersion);

        //lock 默认不开启 , 需要调试的时候开启
        if (lock) {
            timer(webSocketService); //测试用
        }
    }


    private void timer(WebSocketService webSocketService) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Long now = new Date().getTime();
                List<Map<String, Object>> list = dataBiz.list;
                list.forEach(map -> {
                    String dateTimeStr = (String) map.get("dateTimeStr");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Long parse = sdf.parse(dateTimeStr).getTime();
                        Long poor = now - parse;
                        if (poor.longValue() > 10000L) { // 超过10秒
                            map.put("state", 0);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
                if (list.size() > 0) {
                    String s = JSON.toJSONString(list);
                    webSocketService.onMessage(s, null);
                }
            }
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }
}
