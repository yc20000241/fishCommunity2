package com.yc.community.service.modules.articles.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.common.commonConst.ActiveEnum;
import com.yc.community.common.commonConst.ArticlePublishEnum;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.minio.MinioUtil;
import com.yc.community.common.util.DateUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.dataHandled.kafka.KafkaProducer;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.mapper.FishArticlesMapper;
import com.yc.community.service.modules.articles.request.ApplyArticleRequest;
import com.yc.community.service.modules.articles.request.ArticleLikeRequest;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.response.TodayTop10Reponse;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-07
 */
@Service
public class FishArticlesServiceImpl extends ServiceImpl<FishArticlesMapper, FishArticles> implements IFishArticlesService {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MessageAdapter messageAdapter;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void publish(PublishArticleRequest publishArticleRequest) {
        FishArticles fishArticles = new FishArticles();
        fishArticles.setId(UUIDUtil.getUUID());
        fishArticles.setCreatedId(publishArticleRequest.getUserId());
        fishArticles.setCreatedName(publishArticleRequest.getUserName());
        fishArticles.setCreatedTime(new Date());
        fishArticles.setPicturePath(publishArticleRequest.getFilePath());
        fishArticles.setStatus(ActiveEnum.ACTIVE.getCode());
        fishArticles.setPublishStatus(ArticlePublishEnum.ARTICLE_PUBLISH.getCode());
        fishArticles.setTitle(publishArticleRequest.getTitle());

        String fileName = minioUtil.stringUpload(publishArticleRequest.getContent(), ConstList.ARTICLE_BUCKET);
        fishArticles.setFilePath(fileName);

        String content = publishArticleRequest.getContent().substring(0,200);
        content = content.replaceAll("</?[^>]+>", "");
        content = content.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        fishArticles.setDescribe(content);

        save(fishArticles);
    }

    @Override
    public IPage<FishArticles> search(String keyWord, String userId, Integer kind, Integer pageNo) {
        QueryWrapper<FishArticles> fishArticlesQueryWrapper = new QueryWrapper<>();
        fishArticlesQueryWrapper.eq("publish_status", ActiveEnum.ACTIVE.getCode());
        if(!StringUtils.isEmpty(keyWord))
            fishArticlesQueryWrapper.like("title", keyWord);
        if(!StringUtils.isEmpty(userId))
            fishArticlesQueryWrapper.eq("created_id", userId);

        if(kind == 1)
            fishArticlesQueryWrapper.orderByDesc("created_time");
        else if(kind == 2)
            fishArticlesQueryWrapper.orderByAsc("created_time");
        else if(kind == 3)
            fishArticlesQueryWrapper.orderByDesc("look_count");
        else
            fishArticlesQueryWrapper.orderByDesc("like_count");

        Page<FishArticles> pageNotst = new Page<>(pageNo, 10);
        IPage<FishArticles> page1 = page(pageNotst, fishArticlesQueryWrapper);

        return page1;
    }

    @Override
    public List<TodayTop10Reponse> getTodayTop10() {
        QueryWrapper<FishArticles> last = new QueryWrapper<FishArticles>();
        last.ge("created_time", DateUtil.getDayBegin())
                .eq("publish_status", ActiveEnum.ACTIVE.getCode())
                .orderByAsc("like_count")
                .last("limit 10");
        List<FishArticles> list = list(last);
        List<TodayTop10Reponse> resultList = new ArrayList<>();

        list.forEach(x -> {
            TodayTop10Reponse todayTop10Reponse = new TodayTop10Reponse(x.getId(),x.getTitle());
            resultList.add(todayTop10Reponse);
        });

        return resultList;
    }

    @Override
    public List<FishArticles> searchApplyArticle(Integer applyStatus, String keyWord) {
        QueryWrapper<FishArticles> fishArticlesQueryWrapper = new QueryWrapper<>();
        fishArticlesQueryWrapper.eq("publish_status", applyStatus);
        if(!StringUtils.isEmpty(keyWord))
            fishArticlesQueryWrapper.like("title", keyWord);

        fishArticlesQueryWrapper.orderByDesc("created_time");
        List<FishArticles> list = list(fishArticlesQueryWrapper);
        return list;
    }

    @Override
    public void applyArticleById(ApplyArticleRequest applyArticleRequest) {
        FishArticles byId = getById(applyArticleRequest.getId());
        byId.setPublishStatus(applyArticleRequest.getPublishState());
        updateById(byId);

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("articleObj", byId);
        stringObjectHashMap.put("publishContent",applyArticleRequest.getPublishContent());
        messageAdapter.adapter(MessageCategoryEnum.ARTICLE_APPLY.getCategory(), stringObjectHashMap);
    }

    @Override
    public FishArticles getArticleInfoById(String id) {
        FishArticles byId = getById(id);
        kafkaProducer.commonSend("articleLook",JSON.toJSONString(byId));
        return byId;
    }

    @Override
    public void articleLike(ArticleLikeRequest articleLikeRequest) {
        FishArticles byId = getById(articleLikeRequest.getArticleId());
        byId.setLikeCount(byId.getLikeCount()+1);
        updateById(byId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("article", byId);
        map.put("userId", articleLikeRequest.getUserId());
        map.put("userName", articleLikeRequest.getUserName());

        messageAdapter.adapter(MessageCategoryEnum.ARTICLE_LIKE.getCategory(), map);
    }

}
