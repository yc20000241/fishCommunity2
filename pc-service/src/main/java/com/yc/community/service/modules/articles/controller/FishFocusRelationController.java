package com.yc.community.service.modules.articles.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishFocusRelation;
import com.yc.community.service.modules.articles.service.IFishFocusRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        fishFocusRelationService.addRelation(fishFocusRelation);
        return CommonResponse.OKBuilder.msg("关注成功").build();
    }

    @GetMapping("/ifFocus")
    public CommonResponse ifFocus(FishFocusRelation fishFocusRelation){
        Boolean flag = fishFocusRelationService.ifFocus(fishFocusRelation);
        return CommonResponse.OKBuilder.data(flag).build();
    }

}

