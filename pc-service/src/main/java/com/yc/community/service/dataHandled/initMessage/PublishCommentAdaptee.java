package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.commonConst.H5ColorConst;
import com.yc.community.common.commonConst.MenuConst;
import com.yc.community.common.util.HTMLUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;

import java.util.Date;

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
        String userUrl = MenuConst.USER_CENTER_URL + "?userId=" + fishComments.getFromUserId();
        String commentUrl = MenuConst.ARTICLE_DETAIL_URL+"?id="+fishComments.getArticleId() + "&commentId=" + fishComments.getId();
        String content = HTMLUtil.aFont(userUrl,fishComments.getFromUserName() , H5ColorConst.IDEA_WORD_ORANGE)+"对您进行了评论："+ HTMLUtil.aFont(commentUrl,fishComments.getArticleComment() , H5ColorConst.SKY_BLUE);
        fishMessage.setContent(content);

        return fishMessage;
    }
}
