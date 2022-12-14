package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.commonConst.H5ColorConst;
import com.yc.community.common.commonConst.MenuConst;
import com.yc.community.common.util.HTMLUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.entity.FishUserCommentLike;
import com.yc.community.service.modules.articles.request.CommentLikeRequest;

import java.util.Date;
import java.util.HashMap;

public class CommentLikeAdaptee implements IMessageAdaptee {

    private final Integer CATEGORY = 3;

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
        fishMessage.setStatus(0);

        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) object;
        CommentLikeRequest commentLikeRequest = (CommentLikeRequest) stringObjectHashMap.get("userComment");
        FishComments fishComments = (FishComments) stringObjectHashMap.get("comment");

        fishMessage.setCreatedId(commentLikeRequest.getFromUserId());
        fishMessage.setCreatedName(commentLikeRequest.getFromUserName());
        fishMessage.setReceiveId(commentLikeRequest.getUserId());
        fishMessage.setCategoryContent("有人对你的评论进行了点赞");
        String userUrl = MenuConst.USER_CENTER_URL + "?userId=" + commentLikeRequest.getFromUserId();
        String content = HTMLUtil.aFont(userUrl,commentLikeRequest.getFromUserName(), H5ColorConst.IDEA_WORD_ORANGE) + "对你的评论：" + HTMLUtil.font(fishComments.getArticleComment() , H5ColorConst.SKY_BLUE) + " 进行了点赞";
        fishMessage.setContent(content);

        return fishMessage;
    }
}
