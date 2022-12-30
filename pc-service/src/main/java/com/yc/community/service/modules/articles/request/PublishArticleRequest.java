package com.yc.community.service.modules.articles.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PublishArticleRequest {

    @NotBlank(message = "文章标题为空")
    private String title;

    @NotBlank(message = "文章内容为空")
    private String content;

    private String filePath;

    @NotBlank(message = "用户id不可为空")
    private String userId;

    @NotBlank(message = "用户名不可为空")
    private String userName;

    private String describe;

    private String articleId;

    private String fileName;

    private String pictureUrl;
}
