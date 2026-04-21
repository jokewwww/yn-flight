package com.ncse.fdds.newibmmq;

import com.ibm.mq.MQException;
import com.ncse.fdds.entity.TflightCache;
import com.ncse.fdds.repository.TFlightCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import javax.jms.*;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Receive implements MessageListener {
    public static volatile boolean isStartFlag = false;
    private QueueConnection connection;
    private QueueSession session;
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
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private QueueReceiver queueReceiver;

    @Autowired
    private TFlightCacheRepository tFlightCacheRepository;

    public void connect() throws NamingException, JMSException, MQException {
        connection = getConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
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
                result = new String(byteData, StandardCharsets.UTF_8);
                tflightCache.setMessage(result);
            } else {
                log.warn("FDDS Service msg type error");
                return;
            }
            log.info("收到信息：" + tflightCache.getMessage());
            // tFlightCacheRepository.save(tflightCache);

//            log.info("rec msg: {}",result);
//            log.debug("xml2json :{}",XmlParseUtil.parseXml2JsonWithDefaultPrettyPrinter(new KMGData(),result));
           /* ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, XmlParseUtil.parseXml2Json(new KMGData(),result) );
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //TODO 失败存储重试
                    ProtectProcess.relet();
                    log.warn("Produce: The message failed to be sent:" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                    ProtectProcess.relet();//续租守护进程
                    log.info("Produce: The message was sent successfully result: {}",stringObjectSendResult.getProducerRecord().toString());
                }
            });*/
        }
        /*catch (InstantiationException|JsonProcessingException
                |IllegalAccessException|ParseException|DocumentException em){
            log.warn(em.getMessage()+" msg："+result);
        }*/ catch (JMSException e) {
            disconnect();
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
            session.close();
        } catch (Exception e) {
            log.warn("session close {}", e.getMessage());
        }
        try {
            connection.stop();
        } catch (Exception e) {
            log.warn("connection stop {}", e.getMessage());
        }
        isStartFlag = false;
        log.info("Listener stopped.");
    }
}

