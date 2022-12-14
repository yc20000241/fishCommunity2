package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishUserCommentLike;
import com.yc.community.service.modules.articles.request.CommentLikeRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-23
 */
public interface IFishUserCommentLikeService extends IService<FishUserCommentLike> {

    void likeComment(CommentLikeRequest commentLikeRequest);
}
