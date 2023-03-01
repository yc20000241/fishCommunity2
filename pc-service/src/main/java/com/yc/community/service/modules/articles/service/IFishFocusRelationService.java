package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishFocusRelation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2023-03-01
 */
public interface IFishFocusRelationService extends IService<FishFocusRelation> {

    void addRelation(FishFocusRelation fishFocusRelation);

    Boolean ifFocus(FishFocusRelation fishFocusRelation);
}
