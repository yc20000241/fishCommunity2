package com.yc.community.service.dataHandled.initMessage;

import com.alibaba.fastjson.JSONObject;
import com.yc.community.service.dataHandled.kafka.KafkaProducer;
import com.yc.community.service.modules.articles.entity.FishMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageAdapter {

    private List<IMessageAdaptee> iMessageAdaptees;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostConstruct
    public void init(){
        iMessageAdaptees = new ArrayList<IMessageAdaptee>();
        iMessageAdaptees.add(new ArticleApplyAdaptee());
        iMessageAdaptees.add(new ArticleLikeAdaptee());
    }

    @Async
    public void adapter(Integer category, Object object){
        FishMessage fishMessage = null;
        for (int i = 0; i < iMessageAdaptees.size(); i++)
            if(iMessageAdaptees.get(i).ifAdaptee(category)){
                fishMessage = iMessageAdaptees.get(i).initMessage(object);
                break;
            }

        kafkaProducer.commonSend("message", JSONObject.toJSONString(fishMessage));
    }
}
