package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.commonConst.H5ColorConst;
import com.yc.community.common.commonConst.MenuConst;
import com.yc.community.common.util.HTMLUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.articles.request.CommentLikeRequest;

import java.util.Date;
import java.util.HashMap;

public class SendFriendApplyAdaptee implements IMessageAdaptee {

    private final Integer CATEGORY = 5;

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
        UserInfo userInfo = (UserInfo)stringObjectHashMap.get("fromUserInfo");
        String toUserId = (String) stringObjectHashMap.get("toUserId");
        String msg = (String) stringObjectHashMap.get("content");

        fishMessage.setCreatedId(userInfo.getId());
        fishMessage.setCreatedName(userInfo.getUserName());
        fishMessage.setReceiveId(toUserId);
        fishMessage.setCategoryContent("有人对你发送了好友申请");
        String userUrl = MenuConst.USER_CENTER_URL + "?userId=" +fishMessage.getCreatedId();
        String content = HTMLUtil.aFont(userUrl,fishMessage.getCreatedName(), H5ColorConst.IDEA_WORD_ORANGE) + "向你发送了好友申请: " +HTMLUtil.font(msg,H5ColorConst.SKY_BLUE);
        fishMessage.setContent(content);

        return fishMessage;
    }
}
