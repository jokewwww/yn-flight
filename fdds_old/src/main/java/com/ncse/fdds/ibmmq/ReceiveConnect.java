package com.ncse.fdds.ibmmq;

import com.ibm.mq.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.naming.NamingException;


@Slf4j
public class ReceiveConnect implements Runnable {
    public static volatile boolean connFlag = false;
    public static volatile boolean statusFlag = false;
    private int firstCon = 1;
    private int reconnectTimes = 0;
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

    private final Receive receive;

    public ReceiveConnect(Receive receive) {
        this.receive = receive;
    }

    /***************************************
     * New thread
     * @return
     * @throws JMSException
     ***************************************/

    public void run() {
        final long nSleepMilliSecs = 10000; //检测间隔时间10秒
        while (true) {
            try {
                connect();
//                log.debug("statusFlag:"+ statusFlag + ",connFlag:"+connFlag);
                if ((statusFlag) && (!connFlag)) {
                    startService();
                    firstCon = ++firstCon;
                    reconnectTimes = ++reconnectTimes; //重连次数
                }
                Thread.sleep(nSleepMilliSecs);
            } catch (NamingException ex) {
                log.warn("NamingException {}", ex.getMessage());
            } catch (JMSException ex) {
                log.warn(ex.getMessage());
                try {
                    disconnect();
                } catch (NamingException e) {
                    log.warn(e.getMessage());
                } catch (JMSException e) {
                    log.warn(e.getMessage());
                } catch (MQException e) {
                    log.warn(e.getMessage());
                }
            } catch (MQException ex) {
                log.warn("MQException {}", ex.getMessage());
            } catch (InterruptedException ex) {
                log.warn(ex.getMessage());
            }
        }

    }

    /**************************************
     * Connect to QueueManager
     * @throws NamingException
     * @throws JMSException
     * @throws MQException
     *************************************/
    public void connect() throws NamingException, JMSException, MQException {
        try {
            connection = connectQueueManager();
            statusFlag = true;
            connection.close();
        } catch (JMSException e) {
            statusFlag = false;
            connFlag = false;
            log.debug("Connect to QueueManager {}", e.getMessage());
            log.info("IBM MQ Connect test fail");
        }
    }

    /**********************************************
     * Start EBDS Server
     * @throws JMSException
     *********************************************/
    public void startService() throws JMSException {
        try {
            receive.connect();


            connFlag = true;

        } catch (NamingException | MQException ex) {
            log.debug("Start EBDS Server {}", ex.getMessage());
            connFlag = false;
        }
    }

    /**************************************
     * Cancel Connect
     * @throws NamingException
     * @throws JMSException
     * @throws MQException
     **************************************/
    public void disconnect() throws NamingException, JMSException, MQException {
        receive.disconnect();
        connFlag = false;
        statusFlag = false;

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
