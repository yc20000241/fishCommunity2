package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCategoryEnum {
    ARTICLE_APPLY(1,"审批消息"),
    ARTICLE_OR_COMMENT_LIKE(2, "文章或评论点赞");

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