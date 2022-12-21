package com.yc.community.service.modules.articles.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ApplyArticleRequest {

    @NotBlank(message = "文章id为空")
    private String id;

    @NotNull
    private Integer publishState;

    @NotBlank(message = "审批内容为空")
    private String publishContent;

}
