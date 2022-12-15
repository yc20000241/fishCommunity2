package com.yc.community.service.modules.articles.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("/search")
    public CommonResponse search(@RequestParam(value = "keyWord", required = false) String keyWord,
                                 @RequestParam("userId") String userId,
                                 @RequestParam(value = "kind", defaultValue = "1") Integer kind,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        IPage<FishArticles> list = fishArticlesService.search(keyWord, userId, kind, pageNo);
        return CommonResponse.OKBuilder.data(list).build();
    }
}

