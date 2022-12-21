package com.yc.community.service.dataHandled.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class kafkaConsumer {

    @KafkaListener(topics = "message")
    public void listener1(ConsumerRecord<String, String> record) {
        String value = record.value();
        System.out.println("message"+value);
    }

}
