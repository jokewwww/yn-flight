package com.ncse.fdds.newibmmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.mq.*;
import com.ncse.fdds.entity.TflightCache;
import com.ncse.fdds.protect.ProtectProcess;
import com.ncse.fdds.repository.TFlightCacheRepository;
import com.ncse.fdds.utils.XmlParseUtil;
import com.ncse.fdds.xmlbean.HYData;
import com.ncse.fdds.xmlbean.KMGData;
import com.ncse.fdds.xmlbean.XmlInterfaceUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.jms.*;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Slf4j
public class Receive implements MessageListener {

    public static volatile boolean isStartFlag = false;
    private static QueueSession session;
    private static QueueSession newSession;
    private static MessageProducer producer;
    private static MQQueueManager mqQueueManager;
    private static String QMSend = "QMDMZ";
    private static String QSend = "HY_IN";
    private QueueConnection connection;
    private QueueConnection newConnection;
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
    @Value("${kafka.msgtopic}")
    private String topic;
    @Value("${ibmmq.queue}")
    private String queue;
    @Value("${newibmmq.hostname}")
    private String NEW_HOST_NAME;
    @Value("${newibmmq.channelname}")
    private String NEWCHANNELNAME;
    @Value("${newibmmq.CCSID}")
    private int NEWCCSID;
    @Value("${newibmmq.listener}")
    private int NEWLISTENER;
    @Value("${newibmmq.QMGRname}")
    private String NEWQMGRNAME;
    @Value("${newibmmq.loginuser}")
    private String newLoginUser;
    @Value("${newibmmq.password}")
    private String newPassword = "";
    @Value("${newibmmq.queue}")
    private String newQueue;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private QueueReceiver queueReceiver;
    @Autowired
    private TFlightCacheRepository tFlightCacheRepository;

    public static void sendMsg(HYData msg) {
        try {
            System.out.println("发送前的信息是---------- > " + msg);
            //发送--文本消息
            String dataMsg = XmlInterfaceUtils.convertToXml(msg, "utf-8", true);
            System.out.println("发送的XML是 ---------- > " + dataMsg);
            TextMessage msgText = newSession.createTextMessage();
            msgText.setText(dataMsg);
            producer.send(msgText);
        } catch (JMSException e) {
            log.error("发送失败", e);
            e.printStackTrace();
        }
    }

    public static void RequestFlight(String msg) {
        try {
            int openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
            /* 关闭了就重新打开 */
            if (mqQueueManager == null || !mqQueueManager.isConnected()) {
                mqQueueManager = new MQQueueManager(QMSend);
            }
            MQQueue queue = mqQueueManager.accessQueue(QSend, openOptions); //打开接收消息的目标队列
            MQMessage putMessage = new MQMessage(); // 定义一个简单的消息
            putMessage.writeString(msg);  // 将数据放入消息缓冲区
            /*设置写入消息的属性（默认属性）*/
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            // 将消息写入队列
            queue.put(putMessage, pmo);
            queue.close();
            mqQueueManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mqQueueManager.disconnect();
            } catch (MQException e) {
            }
        }
    }

    public void connect() throws NamingException, JMSException, MQException {
        connection = getConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        try {
            newConnection = getNewConnection();
            newSession = newConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = newSession.createQueue(newQueue);
            mqQueueManager = new MQQueueManager(newQueue);
            if (null == queue) {
                log.error("生产队列创建失败");
            }
            producer = newSession.createProducer(queue);
            newConnection.start();
        } catch (JMSException e) {
            log.error("" + e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("" + e);
            e.printStackTrace();
        }
        queueReceiver = session.createReceiver(getQueue(queue));
        queueReceiver.setMessageListener(this);
        connection.start();
        isStartFlag = true;
        log.info("AListener started.");
        ReceiveConnect.completableFuture.complete("ok");
    }

    public void onMessage(Message message) {
        String result = null;
        try {
            TflightCache tflightCache = new TflightCache();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                result = textMessage.getText();
                tflightCache.setMessage(((TextMessage) message).getText());
                //System.out.println("\n"+context);
            } else if (message instanceof BytesMessage) {
                BytesMessage msg = (BytesMessage) message;
                int dataLen = (int) msg.getBodyLength();
                byte[] byteData = new byte[dataLen];
                msg.readBytes(byteData);
//                log.info(new String(byteData,"UTF-8"));
                result = new String(byteData, "UTF-8");
                tflightCache.setMessage(result);
            } else {
                log.warn("FDDS Service msg type error");
                return;
            }
            log.info("---------------航显正常-------------------------");
            //log.info("收到信息：" + tflightCache.getMessage());
            // tFlightCacheRepository.save(tflightCache);

//            log.info("rec msg: {}",result);
//            log.debug("xml2json :{}",XmlParseUtil.parseXml2JsonWithDefaultPrettyPrinter(new KMGData(),result));
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, XmlParseUtil.parseXml2Json(new KMGData(), result));
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info("---------------航显正常onFailure-------------------------");
                    //TODO 失败存储重试
                    ProtectProcess.relet();
                    log.warn("Produce: The message failed to be sent:" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                    log.info("---------------航显正常onSuccess-------------------------");
                    ProtectProcess.relet();//续租守护进程
                    log.info("Produce: The message was sent successfully result: {}", stringObjectSendResult.getProducerRecord().toString());
                }
            });
        } catch (UnsupportedEncodingException ex) {
            log.warn(ex.getMessage() + " msg：" + result);
        }
        /*catch (InstantiationException|JsonProcessingException
                |IllegalAccessException|ParseException|DocumentException em){
            log.warn(em.getMessage()+" msg："+result);
        }*/ catch (JMSException e) {
            disconnect();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private QueueConnection getConnection() throws JMSException {
        com.ibm.mq.jms.MQQueueConnectionFactory factory = new com.ibm.mq.jms.MQQueueConnectionFactory();
        factory.setQueueManager(QMGRNAME);
        factory.setCCSID(CCSID);
        factory.setChannel(CHANNELNAME);
        factory.setHostName(HOST_NAME);
        factory.setPort(LISTENER);
        factory.setTransportType(1);
        return factory.createQueueConnection(loginUser, password);
    }

    private QueueConnection getNewConnection() throws JMSException {
        com.ibm.mq.jms.MQQueueConnectionFactory factory = new com.ibm.mq.jms.MQQueueConnectionFactory();
        factory.setQueueManager(NEWQMGRNAME);
        factory.setCCSID(NEWCCSID);
        factory.setChannel(NEWCHANNELNAME);
        factory.setHostName(NEW_HOST_NAME);
        factory.setPort(NEWLISTENER);
        factory.setTransportType(1);
        return factory.createQueueConnection(newLoginUser, newPassword);
    }

    private Queue getQueue(String queuename) throws JMSException, MQException {
        return new com.ibm.mq.jms.MQQueue(queuename);
    }

    public void disconnect() {
        log.info("recevie start disconnect");
        try {
            queueReceiver.close();
        } catch (Exception e) {
            log.warn("queueReceiver close {}", e.getMessage());
        }
        try {
            newSession.close();
            session.close();
        } catch (Exception e) {
            log.warn("session close {}", e.getMessage());
        }
        try {
            newConnection.stop();
            connection.stop();
        } catch (Exception e) {
            log.warn("connection stop {}", e.getMessage());
        }
        isStartFlag = false;
        log.info("Listener stopped.");
    }

}

