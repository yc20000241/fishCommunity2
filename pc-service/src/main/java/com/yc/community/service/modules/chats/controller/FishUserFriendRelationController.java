package com.yc.community.service.modules.chats.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
@RestController
@RequestMapping("/api/service/chats/fishUserFriendRelation")
public class FishUserFriendRelationController {

    @Autowired
    private IFishUserFriendRelationService fishUserFriendRelationService;

    @PostMapping("/addFriend")
    public CommonResponse addFriend(@RequestBody FishUserFriendRelation fishUserFriendRelation){
        fishUserFriendRelationService.addFriend(fishUserFriendRelation);
        return CommonResponse.OKBuilder.msg("发送请求成功").build();
    }
}

