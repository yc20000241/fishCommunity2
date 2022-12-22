package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.request.ApplyArticleRequest;
import com.yc.community.service.modules.articles.request.ArticleLikeRequest;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.response.TodayTop10Reponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-07
 */
public interface IFishArticlesService extends IService<FishArticles> {

    void publish(PublishArticleRequest publishArticleRequest);

    IPage<FishArticles> search(String keyWord, String userId, Integer kind, Integer pageNo);

    List<TodayTop10Reponse> getTodayTop10();

    List<FishArticles> searchApplyArticle(Integer applyStatus, String keyWord);

    void applyArticleById(ApplyArticleRequest applyArticleRequest);

    FishArticles getArticleInfoById(String id);

    void articleLike(ArticleLikeRequest articleLikeRequest);
}
