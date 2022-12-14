package com.yc.community.service.modules.articles.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-12-07
 */
@RestController
@RequestMapping("/api/service/articles/fishArticles")
public class FishArticlesController {

    @Autowired
    private IFishArticlesService fishArticlesService;

    @PostMapping("/publish")
    public CommonResponse publish(@Validated @RequestBody PublishArticleRequest publishArticleRequest){
        fishArticlesService.publish(publishArticleRequest);
        return CommonResponse.OKBuilder.msg("文章申请审批成功").build();
    }
}

