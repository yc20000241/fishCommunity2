package com.yc.community.service.modules.articles.controller;


import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.EsArticle;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.esMapper.ArticleMapper;
import com.yc.community.service.modules.articles.request.ApplyArticleRequest;
import com.yc.community.service.modules.articles.request.ArticleLikeRequest;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.response.TodayTop10Reponse;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import org.elasticsearch.action.search.SearchResponse;
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

    @Autowired
    private ArticleMapper articleMapper;

    @PostMapping("/publish")
    public CommonResponse publish(@Validated @RequestBody PublishArticleRequest publishArticleRequest){
        String msg = fishArticlesService.publish(publishArticleRequest);
        return CommonResponse.OKBuilder.msg(msg).build();
    }

    @GetMapping("/search")
    public CommonResponse search(@RequestParam(value = "keyWord", required = false) String keyWord,
                                 @RequestParam(value = "userId", required = false) String userId,
                                 @RequestParam(value = "kind", defaultValue = "1") Integer kind,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        IPage<FishArticles> list = fishArticlesService.search(keyWord, userId, kind, pageNo);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/getTodayTop10")
    public CommonResponse getTodayTop10(){
        List<TodayTop10Reponse> list = fishArticlesService.getTodayTop10();
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/searchApplyArticle")
    public CommonResponse searchApplyArticle(@RequestParam("applyStatus") Integer applyStatus,
                                       @RequestParam("keyWord") String keyWord){
        List<FishArticles> list = fishArticlesService.searchApplyArticle(applyStatus, keyWord);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @PostMapping("/applyArticleById")
    public CommonResponse applyArticleById(@RequestBody ApplyArticleRequest applyArticleRequest){
        fishArticlesService.applyArticleById(applyArticleRequest);
        return CommonResponse.OKBuilder.msg("审批成功").build();
    }

    @GetMapping("/getById")
    public CommonResponse getById(@RequestParam("id") String id){
        FishArticles byId = fishArticlesService.getArticleInfoById(id);
        return CommonResponse.OKBuilder.data(byId).build();
    }

    @PostMapping("/articleLike")
    public CommonResponse articleLike(@RequestBody ArticleLikeRequest articleLikeRequest){
        fishArticlesService.articleLike(articleLikeRequest);
        return CommonResponse.OKBuilder.msg("文章点赞成功").build();
    }

    @PostMapping("/estest")
    public CommonResponse estest(@RequestBody EsArticle esArticle){
        articleMapper.insert(esArticle);
        LambdaEsQueryWrapper<EsArticle> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.match(EsArticle::getArticleContent, "测试");
        SearchResponse search = articleMapper.search(wrapper);
//        EsPageInfo<EsArticle> esArticleEsPageInfo = articleMapper.pageQuery(wrapper, 1, 10);
        return CommonResponse.OKBuilder.data(search).build();
    }

}

