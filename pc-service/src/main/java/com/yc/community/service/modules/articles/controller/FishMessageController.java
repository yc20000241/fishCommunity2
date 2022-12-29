package com.yc.community.service.modules.articles.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.service.IFishMessageService;
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
 * @since 2022-12-21
 */
@RestController
@RequestMapping("/api/service/articles/fishMessage")
public class FishMessageController {

    @Autowired
    private IFishMessageService fishMessageService;

    @GetMapping("/getMessageList")
    public CommonResponse getMessageList(@RequestParam("userId") String userId){
        List<FishMessage> list = fishMessageService.getMessageList(userId);
        return CommonResponse.OKBuilder.data(list).build();
    }


}

