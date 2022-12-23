package com.yc.community.service.modules.articles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishUserCommentLike;
import com.yc.community.service.modules.articles.mapper.FishUserCommentLikeMapper;
import com.yc.community.service.modules.articles.request.CommentLikeRequest;
import com.yc.community.service.modules.articles.service.IFishUserCommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-23
 */
@Service
public class FishUserCommentLikeServiceImpl extends ServiceImpl<FishUserCommentLikeMapper, FishUserCommentLike> implements IFishUserCommentLikeService {

    @Autowired
    private FishCommentsServiceImpl fishCommentsService;

    @Autowired
    private MessageAdapter messageAdapter;

    @Override
    public void likeComment(CommentLikeRequest commentLikeRequest) {
        List<FishUserCommentLike> list = list(new QueryWrapper<FishUserCommentLike>().eq("user_id", commentLikeRequest.getUserId()).eq("comment_id", commentLikeRequest.getCommentId()));
        if (list.size() > 0)
            throw new BusinessException(BusinessExceptionCode.COMMENT_HAS_LIKEN);

        FishUserCommentLike fishUserCommentLike = new FishUserCommentLike();
        fishUserCommentLike.setCommentId(commentLikeRequest.getCommentId());
        fishUserCommentLike.setUserId(commentLikeRequest.getUserId());
        fishUserCommentLike.setId(UUIDUtil.getUUID());
        save(fishUserCommentLike);

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("userComment", commentLikeRequest);
        FishComments byId = fishCommentsService.getById(fishUserCommentLike.getCommentId());
        stringObjectHashMap.put("comment", byId);

        messageAdapter.adapter(MessageCategoryEnum.COMMENT_LIKE.getCategory(),stringObjectHashMap);
    }
}
