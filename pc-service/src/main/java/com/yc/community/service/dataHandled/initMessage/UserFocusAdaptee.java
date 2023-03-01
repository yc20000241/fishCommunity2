package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.common.commonConst.H5ColorConst;
import com.yc.community.common.commonConst.MenuConst;
import com.yc.community.common.util.HTMLUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.entity.UserInfo;

import java.util.Date;
import java.util.HashMap;

public class UserFocusAdaptee implements  IMessageAdaptee {

    private final Integer CATEGORY = 6;

    @Override
    public Boolean ifAdaptee(Integer category) {
        if(category == CATEGORY)
            return true;
        return false;
    }

    @Override
    public FishMessage initMessage(Object object) {
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) object;
        UserInfo userInfo = (UserInfo)stringObjectHashMap.get("userInfo");

        FishMessage fishMessage = new FishMessage();
        fishMessage.setId(UUIDUtil.getUUID());
        fishMessage.setCategory(CATEGORY);
        fishMessage.setCreatedName(userInfo.getUserName());
        String userUrl = MenuConst.USER_CENTER_URL + "?userId=" +userInfo.getId();
        String content = HTMLUtil.aFont(userUrl,userInfo.getNick(), H5ColorConst.IDEA_WORD_ORANGE) + "关注了你";
        fishMessage.setContent(content);
        fishMessage.setCreatedTime(new Date());
        fishMessage.setReceiveId((String) stringObjectHashMap.get("receive"));
        fishMessage.setStatus(0);
        String contentByCategory = "有人关注了你";
        fishMessage.setCategoryContent(contentByCategory);
        return fishMessage;
    }
}
