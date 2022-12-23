package com.yc.community.service.modules.articles.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.mapper.FishCommentsMapper;
import com.yc.community.service.modules.articles.service.IFishCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-19
 */
@Service
public class FishCommentsServiceImpl extends ServiceImpl<FishCommentsMapper, FishComments> implements IFishCommentsService {

    @Autowired
    private FishArticlesServiceImpl fishArticlesService;

    @Autowired
    private MessageAdapter messageAdapter;

    @Override
    public void commitComment(FishComments fishComments) {
        FishArticles byId = fishArticlesService.getById(fishComments.getArticleId());
        if(byId.getPublishStatus() == 0)
            throw new BusinessException(BusinessExceptionCode.ARTICLE_NOT_APPLIED);

        fishComments.setId(UUIDUtil.getUUID());
        fishComments.setCreatedTime(new Date());
        fishComments.setLikeCount(0);
        save(fishComments);

        messageAdapter.adapter(MessageCategoryEnum.PUBLISH_COMMENT.getCategory(), fishComments);
    }

    @Override
    public List<FishComments> getCommentList(String articleId) {
        List<FishComments> childrenList = list(new QueryWrapper<FishComments>().eq("article_id", articleId).ne("parent_id", "-1"));
        Map<String, List<FishComments>> parentIdHashMap = childrenList.stream().collect(Collectors.groupingBy(FishComments::getParentId));

        List<FishComments> list = list(new QueryWrapper<FishComments>().eq("article_id", articleId).eq("parent_id", "-1"));
        list.forEach(x -> {
            x.setChildrenList(parentIdHashMap.get(x.getId()));
        });
        return list;
    }
}
