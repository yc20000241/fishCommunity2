package com.yc.community.service.modules.articles.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishFocusRelation;
import com.yc.community.service.modules.articles.service.IFishFocusRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/api/service/articles/fishFocusRelation")
public class FishFocusRelationController {

    @Autowired
    private IFishFocusRelationService fishFocusRelationService;

    @PostMapping("/addRelation")
    public CommonResponse addRelation(@RequestBody FishFocusRelation fishFocusRelation){
        String msg = fishFocusRelationService.addRelation(fishFocusRelation);
        return CommonResponse.OKBuilder.msg(msg).build();
    }

    @GetMapping("/ifFocus")
    public CommonResponse ifFocus(FishFocusRelation fishFocusRelation){
        Boolean flag = fishFocusRelationService.ifFocus(fishFocusRelation);
        return CommonResponse.OKBuilder.data(flag).build();
    }

    @GetMapping("/focusWriteArticle")
    public CommonResponse focusWriteArticle(@RequestParam("userId") String userId,
                                            @RequestParam("pageNo") Integer pageNo){
        IPage<FishArticles> list = fishFocusRelationService.focusWriteArticle(userId, pageNo);
        return CommonResponse.OKBuilder.data(list).build();
    }
}

