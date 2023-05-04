package com.yc.community.service.modules.chats.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.entity.FishUserMongo;
import com.yc.community.service.modules.chats.response.FriendListResponse;
import com.yc.community.service.modules.chats.response.FriendMapPointResponse;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

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

    @GetMapping("/friendList")
    public CommonResponse friendList(@RequestParam("userId") String userId){
        List<FriendListResponse> list = fishUserFriendRelationService.friendList(userId);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/searchFriendPoint")
    public CommonResponse searchFriendPoint(@RequestParam("userId") String userId,
                                            @RequestParam(value = "distance", required = false) Integer distance){
        FriendMapPointResponse friendMapPointResponse = fishUserFriendRelationService.searchFriendPoint(userId, distance);
        return CommonResponse.OKBuilder.data(friendMapPointResponse).build();
    }

    @GetMapping("/saveTestMongoData")
    public CommonResponse saveTestMongoData(){
        fishUserFriendRelationService.saveTestMongoData();
        return CommonResponse.OK;
    }
}

