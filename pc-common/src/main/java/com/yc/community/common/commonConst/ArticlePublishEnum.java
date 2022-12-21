package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArticlePublishEnum {

    ARTICLE_PUBLISH(0, "文章审核中"),
    ARTICLE_PUBLISHED(1,"文章审核通过"),
    ARTICLE_NOT_PASS(2,"文章审批未通过");

    private Integer code;

    private String str;

    public String getStr() {
        return str;
    }

    public Integer getCode() {
        return code;
    }
}
