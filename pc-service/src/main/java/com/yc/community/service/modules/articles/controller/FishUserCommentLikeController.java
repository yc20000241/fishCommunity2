package com.yc.community.service.modules.articles.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishUserCommentLike;
import com.yc.community.service.modules.articles.request.CommentLikeRequest;
import com.yc.community.service.modules.articles.service.IFishUserCommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-12-23
 */
@RestController
@RequestMapping("/api/service/articles/fishUserCommentLike")
public class FishUserCommentLikeController {

    @Autowired
    private IFishUserCommentLikeService fishUserCommentLikeService;

    @PostMapping("/likeComment")
    public CommonResponse likeComment(@RequestBody CommentLikeRequest commentLikeRequest){
        fishUserCommentLikeService.likeComment(commentLikeRequest);
        return CommonResponse.OKBuilder.msg("点赞成功").build();
    }

}

