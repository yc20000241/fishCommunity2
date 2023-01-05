package com.yc.community.service.dataHandled.kafka;

import com.alibaba.fastjson.JSON;
import com.yc.community.service.dataHandled.ws.WebSocketServer;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.service.impl.FishArticlesServiceImpl;
import com.yc.community.service.modules.articles.service.impl.FishMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class kafkaConsumer {

    @Autowired
    private FishArticlesServiceImpl fishArticlesService;

    @Autowired
    private FishMessageServiceImpl fishMessageService;

    @Autowired
    private WebSocketServer webSocketServer;


    @KafkaListener(topics = "message")
    public void messageListener(ConsumerRecord<String, String> record) {
        String value = record.value();
        FishMessage fishMessage = JSON.parseObject(value, FishMessage.class);
        fishMessageService.save(fishMessage);
        log.info("消费者进行消费："+ value);

        webSocketServer.sendMessage(value, fishMessage.getReceiveId());
    }

    @KafkaListener(topics = "articleLook")
    public void articleLookListener(ConsumerRecord<String, String> record) {
        String value = record.value();
        FishArticles fishArticles = JSON.parseObject(value, FishArticles.class);
        fishArticlesService.updateById(fishArticles);
        log.info("消费者进行消费："+ value);
    }

}
