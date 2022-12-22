package com.yc.community.service.modules.articles.request;

import lombok.Data;

@Data
public class ArticleLikeRequest {

    private String articleId;

    private String userId;

    private String userName;
}
