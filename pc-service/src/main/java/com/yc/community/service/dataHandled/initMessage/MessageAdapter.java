package com.yc.community.service.dataHandled.initMessage;

import com.yc.community.service.modules.articles.entity.FishMessage;

import javax.annotation.PostConstruct;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter {

    private List<IMessageAdaptee> iMessageAdaptees;

    @PostConstruct
    public void init(){
        iMessageAdaptees = new ArrayList<IMessageAdaptee>();
        iMessageAdaptees.add(new ArticleApplyPassAdaptee());
        iMessageAdaptees.add(new ArticleApplyNotPassAdaptee());
    }

    public FishMessage adapter(Integer category, Object object){
        FishMessage fishMessage = null;
        for (int i = 0; i < iMessageAdaptees.size(); i++)
            if(iMessageAdaptees.get(i).ifAdaptee(category)){
                fishMessage = iMessageAdaptees.get(i).initMessage(object);
                break;
            }
        return fishMessage;
    }
}
