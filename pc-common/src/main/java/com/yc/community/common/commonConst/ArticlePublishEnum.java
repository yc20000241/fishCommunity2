package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticlePublishEnum {

    ARTICLE_PUBLISH(0, "文章审核中"),
    ARTICLE_PUBLISHED(1,"文章审核通过");

    private Integer code;

    private String str;

    public Integer getCode() {
        return code;
    }
}
