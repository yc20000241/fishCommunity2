package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCategoryEnum {
    ARTICLE_APPLY(1,"审批消息"),
    ARTICLE_LIKE(2, "文章点赞"),
    COMMENT_LIKE(3,"评论点赞"),
    PUBLISH_COMMENT(4, "发表评论"),
    ADD_FRIEND_APPLY(5,"发送好友申请"),
    USER_FOCUS(6, "关注");

    private Integer category;

    private String content;

    public static String getContentByCategory(Integer category){
        for (MessageCategoryEnum value : MessageCategoryEnum.values()) {
            if(value.getCategory() == category)
                return value.content;
        }
        return null;
    }
}
