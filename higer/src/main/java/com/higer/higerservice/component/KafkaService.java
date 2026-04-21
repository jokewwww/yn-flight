package com.higer.higerservice.component;

import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.repository.oilpro.ARecordsRepository;
import com.higer.higerservice.util.DateUtil;
import com.higer.higerservice.util.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;


/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 14:09
 * @Description:
 */
@Component
@Slf4j
public class KafkaService {

    public final static Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ARecordsRepository aRecordsRepository;

    //发送消息方法
    public ResponseObject send(String topic, String key, String msg, Integer lock) {
        try {
            ListenableFuture send = kafkaTemplate.send(topic, key, msg);
            kafkaTemplate.metrics();
            kafkaTemplate.execute(new KafkaOperations.ProducerCallback<String, String, Object>() {
                @Override
                public Object doInKafka(Producer<String, String> producer) {
                    return null;
                }
            });
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                @Override
                public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                    ARecords aRecords = new ARecords();
                    aRecords.setFunc("kafka");
                    //gps car 数据不在进行区分 进行统一发送
                    aRecords.setDateType(1);
                    aRecords.setDate(value);
                    aRecords.setErrorMsg(exception.getMessage());
                    aRecords.setInsertDate(DateUtil.getCurrentDateStr());
                    aRecords.setState(1);
                    if (exception instanceof TimeoutException) {
                        aRecords.setErrorMsg("500" + exception.getMessage());
                        System.err.println("kafka 报错 500 了 ");
                        //需要更改记录
                        ResponseObject.error(exception.getMessage(), 500);
                    } else {
                        System.err.println("kafka 报错 1 了 ");
                        //ResponseObject.error(exception.getMessage(), 1);
                    }
                    if (lock != null) {
                        //MyBootListener.notifyKafka();
                    } else {
                        aRecordsRepository.saveAndFlush(aRecords);
                    }
                    //失败的情况下 在此进行确认是否能发送遗留数据
                    MyBootListener.notifyKafka();
                }

                @Override
                public boolean isInterestedInSuccess() {
                    //System.out.println("是否启用监听  true 启用 false 不启用");
                    return true;
                }

                @Override
                public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {

                    System.out.println("发送kafka正确的数据Real=-=---------" + value);

                    System.err.println("kafka success 了 ");

                    //成功之后唤醒线程 发送遗留数据
                    MyBootListener.notifyKafka();

                    if (lock != null) {
                        System.err.println("发送成功 删除数据了");
                        ARecords one = aRecordsRepository.findOne(lock);
                        if (one != null) {
                            aRecordsRepository.delete(one);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error", 1);
    }


    /**
     * @Description: 发送主题 , partition分区  数据  备用 没做优化处理    key 用来区分 是否实时数据
     * @Param: [topic, partition, msg]
     * @return: java.util.concurrent.atomic.AtomicBoolean
     * @Author: XiuHongXin
     * @Date: 2018/10/25
     */
    public ResponseObject send(String topic, String key, Integer partition, Object msg, Integer lock) {
        try {
            kafkaTemplate.send(topic, partition, msg);
            kafkaTemplate.metrics();
            kafkaTemplate.execute(new KafkaOperations.ProducerCallback<String, String, Object>() {
                @Override
                public Object doInKafka(
                        Producer<String, String> producer) {
                    return null;
                }
            });
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                @Override
                public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                    if (exception instanceof TimeoutException) {
                        ResponseObject.error(exception.getMessage(), 500);
                    } else {
                        ResponseObject.error(exception.getMessage(), 1);
                    }
                }

                @Override
                public boolean isInterestedInSuccess() {
                    return true;
                }

                @Override
                public void onSuccess(String topic, Integer partition,
                                      String key, String value,
                                      RecordMetadata recordMetadata) {
                    ResponseObject.success(value.getBytes(), value, "success");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error", 1);
    }


}
