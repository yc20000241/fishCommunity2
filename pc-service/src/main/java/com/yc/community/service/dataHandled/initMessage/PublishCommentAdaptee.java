package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;

import java.util.Date;
import java.util.HashMap;

public class PublishCommentAdaptee implements IMessageAdaptee {

    private final Integer CATEGORY = 4;

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

        FishComments fishComments = (FishComments) object;
        fishMessage.setCreatedId(fishComments.getFromUserId());
        fishMessage.setCreatedName(fishComments.getFromUserName());
        fishMessage.setReceiveId(fishComments.getToUserId());
        fishMessage.setCategoryContent("您收到了一条评论");
        String content = fishComments.getFromUserName()+"对您进行了评论:"+fishComments.getArticleComment();
        fishMessage.setContent(content);

        return fishMessage;
    }
}
