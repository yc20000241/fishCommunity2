package com.yc.community.service.modules.chats.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.chats.entity.FishChatInfo;
import com.yc.community.service.modules.chats.service.IFishChatInfoService;
import com.yc.community.service.modules.chats.service.impl.FishChatInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/service/chats/fishChatInfo")
public class FishChatInfoController {

    @Autowired
    private IFishChatInfoService fishChatInfoService;

    @GetMapping("/getNotReadChatCount")
    public CommonResponse getRemindCount(@RequestParam("userId") String userId){
        Integer count = fishChatInfoService.getNotReadChatCount(userId);
        return CommonResponse.OKBuilder.data(count).build();
    }

    @GetMapping("/getNotReadChatList")
    public CommonResponse getNotReadChatList(@RequestParam("userId") String userId){
        List<FishChatInfo> list = fishChatInfoService.getNotReadChatList(userId);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/getFriendChatInfoList")
    public CommonResponse getFriendChatInfoList(@RequestParam("userId") String userId,
                                                @RequestParam("friendId") String friendId){
        List<FishChatInfo> list = fishChatInfoService.getFriendChatInfoList(userId, friendId);
        return CommonResponse.OKBuilder.data(list).build();
    }
}

