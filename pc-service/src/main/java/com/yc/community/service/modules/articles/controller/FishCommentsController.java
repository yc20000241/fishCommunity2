package com.yc.community.service.modules.articles.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.request.PublishArticleRequest;
import com.yc.community.service.modules.articles.service.IFishCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-12-19
 */
@RestController
@RequestMapping("/api/service/articles/fishComments")
public class FishCommentsController {

    @Autowired
    private IFishCommentsService fishCommentsService;

    @PostMapping("/commitComment")
    public CommonResponse commitComment( @RequestBody FishComments fishComments){
        fishCommentsService.commitComment(fishComments);
        return CommonResponse.OKBuilder.msg("评论成功").build();
    }

    @GetMapping("/getCommentList")
    public CommonResponse getCommentList( @RequestParam("articleId") String articleId){
        List<FishComments> list = fishCommentsService.getCommentList(articleId);
        return CommonResponse.OKBuilder.data(list).build();
    }
}

