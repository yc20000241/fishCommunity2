package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.service.modules.articles.entity.FishMessage;

public interface IMessageAdaptee {

    Boolean ifAdaptee(Integer category);

    FishMessage initMessage(Object object);
}
