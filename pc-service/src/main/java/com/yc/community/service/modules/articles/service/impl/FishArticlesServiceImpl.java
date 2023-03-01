package com.yc.community.service.modules.articles.service.impl;

import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.ActiveEnum;
import com.yc.community.common.commonConst.ArticlePublishEnum;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.minio.MinioUtil;
import com.yc.community.common.util.CopyUtil;
import com.yc.community.common.util.DateUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.dataHandled.kafka.KafkaProducer;
import com.yc.community.service.modules.articles.entity.*;
import com.yc.community.service.modules.articles.esMapper.ArticleMapper;
import com.yc.community.service.modules.articles.mapper.FishArticlesMapper;
import com.yc.community.service.modules.articles.request.ApplyArticleRequest;
import com.yc.community.service.modules.articles.request.ArticleLikeRequest;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.response.ArticleHistoryResponse;
import com.yc.community.service.modules.articles.response.TodayTop10Reponse;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FishUserArticleLikeServiceImpl fishUserArticleLikeService;

    @Resource(name = "userInfoCache")
    private Cache<String, Object> userInfoCache;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public String publish(PublishArticleRequest publishArticleRequest) {
        String msg = null;
        FishArticles fishArticles = new FishArticles();

        if(StringUtils.isEmpty(publishArticleRequest.getArticleId())){
            fishArticles.setId(UUIDUtil.getUUID());
            fishArticles.setPublishStatus(ArticlePublishEnum.ARTICLE_PUBLISH.getCode());
            fishArticles.setCreatedTime(new Date());
            fishArticles.setCreatedId(publishArticleRequest.getUserId());
            fishArticles.setCreatedName(publishArticleRequest.getUserName());
            fishArticles.setStatus(ActiveEnum.ACTIVE.getCode());
            fishArticles.setPicturePath(publishArticleRequest.getFilePath());
            msg = "文章申请审批成功";
        }
        else{
            fishArticles = getById(publishArticleRequest.getArticleId());
            fishArticles.setUpdatedTime(new Date());
            if(!StringUtils.isEmpty(publishArticleRequest.getPictureUrl()))
                fishArticles.setPicturePath(publishArticleRequest.getPictureUrl());
            else{
                fishArticles.setPicturePath(publishArticleRequest.getFilePath());
            }
            msg = "文章编辑成功";
        }

        fishArticles.setTitle(publishArticleRequest.getTitle());
        fishArticles.setDescribe(publishArticleRequest.getDescribe());
        String fileName = minioUtil.stringUpload(publishArticleRequest.getContent(), ConstList.ARTICLE_BUCKET, publishArticleRequest.getFileName());
        fishArticles.setFilePath(fileName);
        saveOrUpdate(fishArticles);

        return msg;
    }

    @Override
    public IPage<FishArticles> search(String keyWord, String userId, Integer kind, Integer pageNo){
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

        List<FishArticles> records = page1.getRecords();
        records.forEach(x -> {
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getCreatedId());
            x.setCreatedName(userInfo.getNick());
        });

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

        list.forEach(x -> {
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getCreatedId());
            x.setCreatedName(userInfo.getNick());
        });

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

        String s = minioUtil.stringDownload(byId.getFilePath(), ConstList.ARTICLE_BUCKET);
        byId.setArticleContent(s);
        kafkaProducer.commonSend("esArticle", JSON.toJSONString(byId));
    }

    @Override
    public FishArticles getArticleInfoById(String id) {
        FishArticles byId = getById(id);
        UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(byId.getCreatedId());
        byId.setCreatedName(userInfo.getNick());
        kafkaProducer.commonSend("articleLook",JSON.toJSONString(byId));
        return byId;
    }

    @Override
    @Transactional
    public void articleLike(ArticleLikeRequest articleLikeRequest) {
        List<FishUserArticleLike> list = fishUserArticleLikeService.list(new QueryWrapper<FishUserArticleLike>().eq("user_id", articleLikeRequest.getUserId()).eq("article_id", articleLikeRequest.getArticleId()));
        if(list.size() > 0)
            throw new BusinessException(BusinessExceptionCode.ARTICLE_HAS_LIKED);

        FishArticles byId = getById(articleLikeRequest.getArticleId());
        byId.setLikeCount(byId.getLikeCount()+1);
        updateById(byId);

        FishUserArticleLike fishUserArticleLike = new FishUserArticleLike();
        fishUserArticleLike.setId(UUIDUtil.getUUID());
        fishUserArticleLike.setArticleId(articleLikeRequest.getArticleId());
        fishUserArticleLike.setUserId(articleLikeRequest.getUserId());
        fishUserArticleLikeService.save(fishUserArticleLike);

        HashMap<String, Object> map = new HashMap<>();
        UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(byId.getCreatedId());
        byId.setCreatedName(userInfo.getNick());
        map.put("article", byId);
        map.put("userId", articleLikeRequest.getUserId());
        map.put("userName", articleLikeRequest.getUserName());

        messageAdapter.adapter(MessageCategoryEnum.ARTICLE_LIKE.getCategory(), map);
    }

    @Override
    public IPage<FishArticles> esSearch(String keyWord, String userId, Integer kind, Integer pageNo) {
        LambdaEsQueryWrapper<EsArticle> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.and(!StringUtils.isEmpty(keyWord), wrap -> {
            wrap.match(EsArticle::getTitle, keyWord).or().match(EsArticle::getArticleContent, keyWord);
        });

        wrapper.eq(!StringUtils.isEmpty(userId), "created_id", userId);

        if(kind == 1)
            wrapper.orderByDesc("created_time");
        else if(kind == 2)
            wrapper.orderByAsc("created_time");
        else if(kind == 3)
            wrapper.orderByDesc("look_count");
        else
            wrapper.orderByDesc("like_count");

        EsPageInfo<EsArticle> esArticleEsPageInfo = articleMapper.pageQuery(wrapper, pageNo, 10);
        Page<FishArticles> fishArticlesIPage = new Page<>();

        List<EsArticle> list = esArticleEsPageInfo.getList();
        if(list.size() != 0){
            List<String> ids = list.stream().map(EsArticle::getId).collect(Collectors.toList());
            Map<String, EsArticle> idMap = list.stream().collect(Collectors.toMap(EsArticle::getId, item -> item));
            List<FishArticles> fishArticles = listByIds(ids);

            fishArticles.forEach(x -> {
                UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getCreatedId());
                x.setCreatedName(userInfo.getNick());
                EsArticle esArticle = idMap.get(x.getId());
                x.setHighlightContent(esArticle.getHighlightContent());
                x.setHighlightTitle(esArticle.getHighlightTitle());
            });
            fishArticlesIPage.setTotal(esArticleEsPageInfo.getTotal());

            ArrayList<FishArticles> result = new ArrayList<>();
            Map<String, FishArticles> fishIdMap = fishArticles.stream().collect(Collectors.toMap(FishArticles::getId, item -> item));
            ids.forEach(x -> {
                result.add(fishIdMap.get(x));
            });
            fishArticlesIPage.setRecords(result);
        }

        return fishArticlesIPage;
    }

    @Override
    public List<EsArticle> estest() {
        articleMapper.delete(new LambdaEsQueryWrapper<>());

        List<FishArticles> list = list();
        ArrayList<EsArticle> esArticles = new ArrayList<>();
        list.forEach(x -> {
            String s = minioUtil.stringDownload(x.getFilePath(), ConstList.ARTICLE_BUCKET);
            EsArticle esArticle = CopyUtil.copy(x, EsArticle.class);
            esArticle.setArticleContent(s);
            esArticles.add(esArticle);
        });

        articleMapper.insertBatch(esArticles);
        List<EsArticle> esArticles1 = articleMapper.selectList(new LambdaEsQueryWrapper<>());
        return esArticles1;
    }

    @Override
    @Async
    public void lookThrough(String articleId, String userId) {
        FishArticles fishArticles = getById(articleId);
        fishArticles.setLookCount(fishArticles.getLookCount()+1);
        updateById(fishArticles);

        String key = userId + "_history";
        Long size = redisTemplate.opsForList().size(key);

        if(size > 200)
            redisTemplate.opsForList().rightPop(key);
        redisTemplate.opsForList().leftPush(key, JSON.toJSONString(fishArticles));
    }

    @Override
    public ArticleHistoryResponse lookThroughHistory(String userId, Integer pageNo) {
        String key = userId + "_history";
        Long size = redisTemplate.opsForList().size(key);

        List<String> range = redisTemplate.opsForList().range(key, (pageNo-1)*10, pageNo*10-1);
        ArrayList<FishArticles> result = new ArrayList<>();
        if(range.size() > 0)
            range.forEach(x -> {
                FishArticles fishArticles = JSONObject.parseObject(x, FishArticles.class);
                result.add(fishArticles);
            });

        ArticleHistoryResponse articleHistoryResponse = new ArticleHistoryResponse();
        articleHistoryResponse.setList(Optional.ofNullable(result).orElse(new ArrayList<>()));
        articleHistoryResponse.setTotal(size.intValue());

        return articleHistoryResponse;
    }



}
