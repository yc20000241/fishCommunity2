package com.yc.community.service.modules.chats.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.chats.entity.FishFriendApply;
import com.yc.community.service.modules.chats.response.FriendApplyListResponse;
import com.yc.community.service.modules.chats.service.IFishFriendApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/api/service/chats/fishFriendApply")
public class FishFriendApplyController {

    @Autowired
    private IFishFriendApplyService fishFriendApplyService;

    @PostMapping("/friendApply")
    public CommonResponse friendApply(@RequestBody FishFriendApply fishFriendApply){
        fishFriendApplyService.friendApply(fishFriendApply);
        return CommonResponse.OKBuilder.msg("已发送好友申请").build();
    }

    @PostMapping("/getList")
    public CommonResponse getList(@RequestParam("userId") String userId){
        List<FriendApplyListResponse> list = fishFriendApplyService.getList(userId);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/getRemindCount")
    public CommonResponse getRemindCount(@RequestParam("userId") String userId){
        Integer count = fishFriendApplyService.getRemindCount(userId);
        return CommonResponse.OKBuilder.data(count).build();
    }

    @GetMapping("/hasReadApply")
    public CommonResponse hasReadApply(@RequestParam("userId") String userId){
        fishFriendApplyService.hasReadApply(userId);
        return CommonResponse.OKBuilder.build();
    }
}

