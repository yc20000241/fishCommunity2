package com.yc.community.sys.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.service.IUserInfoService;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public CommonResponse getUserInfo(HttpServletRequest request){
        InitUserInfoResponse userInfo = userInfoService.getUserInfo(request);
        return CommonResponse.OKBuilder.data(userInfo).build();
    }
}

