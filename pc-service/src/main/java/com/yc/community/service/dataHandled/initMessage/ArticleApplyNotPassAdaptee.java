package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.service.modules.articles.entity.FishMessage;

public class ArticleApplyNotPassAdaptee implements  IMessageAdaptee {

    private final Integer CATEGORY = 2;

    @Override
    public Boolean ifAdaptee(Integer category) {
        if(category == CATEGORY)
            return true;
        return false;
    }

    @Override
    public FishMessage initMessage(Object object) {
        return null;
    }
}
