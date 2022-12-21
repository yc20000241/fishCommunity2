package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageCategoryEnum {
    ARTICLE_APPLY_PASS(1,"审批已通过"),
    ARTICLE_APPLY_NOT_PASS(2,"审批未通过");

    private Integer category;

    private String content;
}
