package com.yc.community.sys.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import com.yc.community.sys.response.AuthorUserInfoResponse;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.service.IUserInfoService;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@RestController
@RequestMapping("/api/web/sys/user")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/getInitUserInfo")
    public CommonResponse getInitUserInfo(HttpServletRequest request){
        InitUserInfoResponse userInfo = userInfoService.getInitUserInfo(request);
        return CommonResponse.OKBuilder.data(userInfo).build();
    }

    @GetMapping("/getUserInfoById")
    public CommonResponse getUserInfoById(@RequestParam("id") String id){
        AuthorUserInfoResponse userInfo = userInfoService.getUserInfoById(id);
        return CommonResponse.OKBuilder.data(userInfo).build();
    }
}

