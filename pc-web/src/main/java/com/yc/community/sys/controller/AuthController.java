package com.yc.community.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 认证
 * </p>
 *
 * @author yc001
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


//    @PostMapping("/login")
//    public ApiResult login(@Valid @RequestBody LoginInfo loginInfo) {
//        return authService.login(loginInfo.getLoginAccount(), loginInfo.getPassword());
//    }
//
//    @PostMapping("/logout")
//    public ApiResult logout() {
//        return authService.logout();
//    }
//
//    @PostMapping("/refresh")
//    public ApiResult refreshToken(HttpServletRequest request) {
//        return authService.refreshToken(jwtProvider.getToken(request));
//    }


}
