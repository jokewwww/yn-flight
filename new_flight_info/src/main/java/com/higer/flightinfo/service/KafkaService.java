package com.higer.flightinfo.service;

import com.alibaba.fastjson.JSONObject;
import com.higer.flightinfo.component.MyBootListener;
import com.higer.flightinfo.entity.TFlightError;
import com.higer.flightinfo.repository.TFlightErrorRepository;
import com.higer.flightinfo.util.MessageSequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.Optional;


/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 14:09
 * @Description:
 */
@Component
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private TFlightErrorRepository tFlightErrorRepository;

    //发送消息方法
    public void send(String topic, String msg, Integer lock) {
        try {
            ListenableFuture send =kafkaTemplate.send(topic,msg);
            kafkaTemplate.metrics();
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                @Override
                public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                    System.out.println("kafka error 了 " + value);
                    TFlightError tFlightError=new TFlightError();
                    tFlightError.setErrorData(value);
                    tFlightError.setErrorReason(exception.getMessage());
                    tFlightError.setErrorTime(new Date());
                    tFlightError.setStatus(1);
                    if (exception instanceof TimeoutException) {
                        tFlightError.setErrorReason("500，" + exception.getMessage());
                        System.err.println("kafka 报错 500 了 ："+exception.getMessage());
                    } else {
                        tFlightError.setErrorReason("1，" + exception.getMessage());
                        System.err.println("kafka 报错 1 了 ");
                    }
                    if (lock == null) {
                        tFlightErrorRepository.saveAndFlush(tFlightError);
                    }  // MyBootListener.notifyKafka();

                    //失败的情况下 在此进行确认是否能发送遗留数据
                    MyBootListener.notifyKafka();
                }

                @Override
                public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
                    System.out.println("kafka success 了 ");

                    //成功之后唤醒线程 发送遗留数据
                    MyBootListener.notifyKafka();

                    if (lock != null) {
                        System.err.println("发送成功 删除数据了");
                        TFlightError tFlightError=new TFlightError();
                        tFlightError.setId(lock);
                        Optional<TFlightError> one =  tFlightErrorRepository.findOne(Example.of(tFlightError));
                        one.ifPresent(flightError -> tFlightErrorRepository.delete(flightError));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description: 发送主题 , partition分区  数据  备用 没做优化处理    key 用来区分 是否实时数据
     * @Param: [topic, partition, msg]
     * @return: java.util.concurrent.atomic.AtomicBoolean
     * @Author: XiuHongXin
     * @Date: 2018/10/25
     */
    public void send(String topic, String key, Object msg, Integer lock) {
        try {
            kafkaTemplate.send(topic, key, msg);
            kafkaTemplate.metrics();
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                @Override
                public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                    System.out.println("kafka error 了 " + value);
                    TFlightError tFlightError=new TFlightError();
                    tFlightError.setErrorData(value);
                    tFlightError.setErrorReason(exception.getMessage());
                    tFlightError.setErrorTime(new Date());
                    tFlightError.setStatus(1);
                    if (exception instanceof TimeoutException) {
                        tFlightError.setErrorReason("500，" + exception.getMessage());
                        System.err.println("kafka 报错 500 了 ："+exception.getMessage());
                    } else {
                        tFlightError.setErrorReason("1，" + exception.getMessage());
                        System.err.println("kafka 报错 1 了 ");
                    }
                    if (lock == null) {
                        tFlightErrorRepository.saveAndFlush(tFlightError);
                    }  // MyBootListener.notifyKafka();

                    //失败的情况下 在此进行确认是否能发送遗留数据
                    MyBootListener.notifyKafka();
                }

                @Override
                public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
                    System.out.println("kafka success 了 ");
                    log.info("发送Kafka消息分区:{}，消息序号：{}", recordMetadata.partition(), JSONObject.parseObject(msg.toString()).getString("msgSeqn"));
                    //成功之后唤醒线程 发送遗留数据
                    MyBootListener.notifyKafka();

                    if (lock != null) {
                        System.err.println("发送成功 删除数据了");
                        TFlightError tFlightError=new TFlightError();
                        tFlightError.setId(lock);
                        Optional<TFlightError> one =  tFlightErrorRepository.findOne(Example.of(tFlightError));
                        one.ifPresent(flightError -> tFlightErrorRepository.delete(flightError));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
