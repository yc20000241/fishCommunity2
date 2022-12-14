package com.yc.community.service.modules.articles.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.common.commonConst.ActiveEnum;
import com.yc.community.common.commonConst.ArticlePublishEnum;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.common.minio.MinioUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.mapper.FishArticlesMapper;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.service.IFishArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        String fileName = minioUtil.stringUpload(publishArticleRequest.getContent(), ConstList.ARTICLE_BUCKET);
        fishArticles.setFilePath(fileName);

        save(fishArticles);
    }
}
