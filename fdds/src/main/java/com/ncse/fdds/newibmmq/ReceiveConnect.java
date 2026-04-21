package com.ncse.fdds.newibmmq;

import com.ibm.mq.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.naming.NamingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
public class ReceiveConnect implements Runnable {
    public static volatile boolean receiveIsStart = false;
    public static volatile CompletableFuture<String> completableFuture = new CompletableFuture<>();
    private QueueConnection connection;

    @Value("${ibmmq.hostname}")
    private String HOST_NAME;
    @Value("${ibmmq.channelname}")
    private String CHANNELNAME;
    @Value("${ibmmq.CCSID}")
    private int CCSID;
    @Value("${ibmmq.listener}")
    private int LISTENER;
    @Value("${ibmmq.QMGRname}")
    private String QMGRNAME;
    @Value("${ibmmq.loginuser}")
    private String loginUser;
    @Value("${ibmmq.password}")
    private String password = "";

    private Receive receive;

    public ReceiveConnect(Receive receive) {
        this.receive = receive;
    }

    /***************************************
     * New thread
     * @return
     * @throws JMSException
     ***************************************/

    public void run() {
        final long nSleepMilliSecs = 50000; //检测间隔时间5秒
        while (true) {
            log.info("111111111111111111");
            boolean result = connect();
            if (result) {
                if (!Receive.isStartFlag) {//如果不在运行
                    System.out.println("程序开始启动");
                    startService();
                    try {
                        //不超时
                        String receiveResult = completableFuture.get();
                        if (receiveResult.equals("ok"))
                            receiveIsStart = true;
                    } catch (InterruptedException | ExecutionException e) {
                        log.debug("receiveResult Exception {}", e);
                    }
                }
            } else {
                if (Receive.isStartFlag)
                    receive.disconnect();
            }
            //等待重试
            try {
                Thread.sleep(nSleepMilliSecs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**************************************
     * Connect to QueueManager
     * @throws NamingException
     * @throws JMSException
     * @throws MQException
     *************************************/
    public boolean connect() {
        try {
            connection = connectQueueManager();
            connection.close();
            return true;
        } catch (Exception e) {
            log.debug("Connect to QueueManager {}", e.getMessage());
            log.info("IBM MQ Connect test fail");
            return false;
        }
    }

    /**********************************************
     * Start EBDS Server
     * @throws JMSException
     *********************************************/
    public void startService() {
        try {
            receive.connect();
        } catch (JMSException | NamingException | MQException ex) {
            log.debug("Start EBDS Server {}", ex.getMessage());
            receive.disconnect();
            completableFuture.completeExceptionally(ex);
            receiveIsStart = false;
        }
    }


    /***************************************
     * Connect to Queue Manager
     * @return
     * @throws JMSException
     ***************************************/
    private QueueConnection connectQueueManager() throws JMSException {
        com.ibm.mq.jms.MQQueueConnectionFactory factory = new com.ibm.mq.jms.MQQueueConnectionFactory();
        factory.setQueueManager(QMGRNAME);
        factory.setCCSID(CCSID);
        factory.setChannel(CHANNELNAME);
        factory.setHostName(HOST_NAME);
        factory.setPort(LISTENER);
        factory.setTransportType(1);
        return factory.createQueueConnection(loginUser, password);
    }
}
