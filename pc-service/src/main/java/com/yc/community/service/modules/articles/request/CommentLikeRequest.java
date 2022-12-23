package com.yc.community.service.modules.articles.request;

import lombok.Data;

@Data
public class CommentLikeRequest {

    private String userId;

    private String commentId;

    private String fromUserName;

    private String fromUserId;

}
