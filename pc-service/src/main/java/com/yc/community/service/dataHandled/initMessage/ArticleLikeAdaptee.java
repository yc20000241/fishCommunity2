package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;

import java.util.Date;
import java.util.HashMap;

public class ArticleLikeAdaptee implements IMessageAdaptee {

    private final Integer CATEGORY = 2;

    @Override
    public Boolean ifAdaptee(Integer category) {
        if(category == CATEGORY)
            return true;
        return false;
    }

    @Override
    public FishMessage initMessage(Object object) {
        FishMessage fishMessage = new FishMessage();
        fishMessage.setId(UUIDUtil.getUUID());
        fishMessage.setCategory(CATEGORY);
        fishMessage.setCreatedTime(new Date());

        HashMap<String, Object> map = (HashMap<String, Object>)object;
        Object obj = map.get("article");
        FishArticles fishArticles = (FishArticles)obj;
        fishMessage.setCreatedId(map.get("userId").toString());
        fishMessage.setCreatedName(map.get("userName").toString());
        fishMessage.setReceiveId(fishArticles.getCreatedId());
        String content = fishMessage.getCreatedName()+"对您对文章《"+fishArticles.getTitle()+"》进行了点赞";
        fishMessage.setContent(content);
        fishMessage.setCategoryContent("有人对你对文章进行了点赞");

        return fishMessage;
    }
}
