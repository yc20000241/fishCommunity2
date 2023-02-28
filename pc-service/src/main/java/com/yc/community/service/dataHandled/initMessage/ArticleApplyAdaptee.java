package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.commonConst.ArticlePublishEnum;
import com.yc.community.common.commonConst.H5ColorConst;
import com.yc.community.common.commonConst.MenuConst;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.util.HTMLUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishMessage;

import java.util.Date;
import java.util.HashMap;

public class ArticleApplyAdaptee implements  IMessageAdaptee {

    private final Integer CATEGORY = 1;

    @Override
    public Boolean ifAdaptee(Integer category) {
        if(category == CATEGORY)
            return true;
        return false;
    }

    @Override
    public FishMessage initMessage(Object object) {
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) object;
        FishArticles fishArticles = (FishArticles)stringObjectHashMap.get("articleObj");
        String publishContent = stringObjectHashMap.get("publishContent").toString();

        FishMessage fishMessage = new FishMessage();
        fishMessage.setId(UUIDUtil.getUUID());
        fishMessage.setCategory(CATEGORY);
        fishMessage.setCreatedName("admin");
        fishMessage.setContent(publishContent);
        fishMessage.setCreatedTime(new Date());
        fishMessage.setReceiveId(fishArticles.getCreatedId());
        fishMessage.setStatus(0);
//        String url = MenuConst.ARTICLE_DETAIL_URL+"?id="+fishArticles.getId();
        String contentByCategory = "您的文章《" + fishArticles.getTitle() + "》"+ (fishArticles.getPublishStatus()==1 ? "审批已通过": "审批未通过");
        fishMessage.setCategoryContent(contentByCategory);
        return fishMessage;
    }
}
