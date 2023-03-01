package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishFocusRelation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2023-03-01
 */
public interface IFishFocusRelationService extends IService<FishFocusRelation> {

    String addRelation(FishFocusRelation fishFocusRelation);

    Boolean ifFocus(FishFocusRelation fishFocusRelation);

    IPage<FishArticles> focusWriteArticle(String userId, Integer pageNo);
}
