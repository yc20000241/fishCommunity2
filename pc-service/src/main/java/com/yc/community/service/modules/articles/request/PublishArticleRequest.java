package com.yc.community.service.modules.articles.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PublishArticleRequest {

    private String title;

    private String content;

    private MultipartFile file;
}
