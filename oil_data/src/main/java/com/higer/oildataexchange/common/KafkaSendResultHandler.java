package com.higer.oildataexchange.common;

import com.higer.oildataexchange.repository.TFlightErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;

@Slf4j
public class KafkaSendResultHandler implements ProducerListener<String, String> {

    @Autowired
    private TFlightErrorRepository tFlightErrorRepository;

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.debug("kafka success");
//        System.out.println("kafka success 了 ");
//
//        //成功之后唤醒线程 发送遗留数据
//        MyBootListener.notifyKafka();
//        TFlightError error = JSONObject.parseObject(producerRecord.value(), TFlightError.class);
//
//        System.err.println("发送成功 删除数据了");
//        Optional<TFlightError> one = tFlightErrorRepository.findById(error.getId());
//        one.ifPresent(tFlightError -> tFlightErrorRepository.delete(tFlightError));
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, Exception exception) {
//        TFlightError tFlightError=new TFlightError();
//        tFlightError.setErrorData(producerRecord.value());
//        tFlightError.setErrorReason(exception.getMessage());
//        tFlightError.setErrorTime(new Date());
//        tFlightError.setStatus(1);
//        if (exception instanceof TimeoutException) {
//            tFlightError.setErrorReason("500，" + exception.getMessage());
//            System.err.println("kafka 报错 500 了 ："+exception.getMessage());
//        } else {
//            System.err.println("kafka 报错 1 了 ");
//        }
//        tFlightErrorRepository.saveAndFlush(tFlightError);
//
//        //失败的情况下 在此进行确认是否能发送遗留数据
//        MyBootListener.notifyKafka();
    }


}
