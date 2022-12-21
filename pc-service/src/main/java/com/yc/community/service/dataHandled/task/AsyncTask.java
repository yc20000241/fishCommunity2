package com.yc.community.service.dataHandled.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Async
    public void sendMessageToProduce(Object object){

    }
}
