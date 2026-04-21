package com.ncse.fdds.ibmmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.mq.MQException;
import com.ncse.fdds.utils.XmlParseUtil;
import com.ncse.fdds.xmlbean.KMGData;
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
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

@Slf4j
public class Receive implements MessageListener {

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

    public void connect() throws NamingException, JMSException, MQException {
        connection = getConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueReceiver = session.createReceiver(getQueue(queue));
        queueReceiver.setMessageListener(this);
        connection.start();
        log.info("AListener started.");
    }

    public void onMessage(Message message) {
        try {
            String result = null;
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                result = textMessage.getText();

                //System.out.println("\n"+context);
            } else if (message instanceof BytesMessage) {
                BytesMessage msg = (BytesMessage) message;
                int dataLen = (int) msg.getBodyLength();
                byte[] byteData = new byte[dataLen];
                msg.readBytes(byteData);
//                log.info(new String(byteData,"UTF-8"));
                result = new String(byteData, StandardCharsets.UTF_8);

            } else {
                log.warn("FDDS Service msg type error");
                return;
            }
//            log.info("rec msg: {}",result);
//            log.debug("xml2json :{}",XmlParseUtil.parseXml2JsonWithDefaultPrettyPrinter(new KMGData(),result));
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, XmlParseUtil.parseXml2Json(new KMGData(), result));
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //TODO 失败存储重试
                    log.warn("Produce: The message failed to be sent:" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                    log.debug("Produce: The message was sent successfully result: {}", stringObjectSendResult.getProducerRecord().toString());
                }
            });
        } catch (InstantiationException | JsonProcessingException
                 | IllegalAccessException | ParseException | DocumentException em) {
            log.warn(em.getMessage());
        } catch (JMSException e) {
            try {
                disconnect();
            } catch (JMSException e1) {
                log.warn(e1.getMessage());
            }
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

    public void disconnect() throws JMSException {
        queueReceiver.close();
        session.close();
        connection.stop();
        ReceiveConnect.connFlag = false;
        log.info("Listener stopped.");
    }
}

