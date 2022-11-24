package com.yc.community.sys.controller;

import com.yc.community.sys.request.LoginInfo;
import com.yc.community.sys.service.impl.AuthServiceImpl;
import com.yc.community.sys.util.AccessToken;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yc.community.common.response.CommonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * <p>
 * 认证
 * </p>
 *
 * @author yc001
 */
@RestController
@RequestMapping("/api/sys")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody LoginInfo loginInfo) {
        AccessToken accessToken = authService.login(loginInfo.getLoginAccount(), loginInfo.getPassword());
        return CommonResponse.OKBuilder.data(accessToken).build();
    }

    @PostMapping("/logout")
    public CommonResponse logout() {
        authService.logout();
        return CommonResponse.OK;
    }

    @PostMapping("/refresh")
    public CommonResponse refreshToken(HttpServletRequest request) {
        authService.refreshToken(jwtProvider.getToken(request));
        return CommonResponse.OK;
    }

    @GetMapping("/test")
    public CommonResponse test() {
        return CommonResponse.OKBuilder.msg("登录认证测试").build();
    }

    @GetMapping("/test1")
    public CommonResponse test1() {
        return CommonResponse.OKBuilder.msg("登录认证测试").build();
    }
}
