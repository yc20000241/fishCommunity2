package com.yc.community.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.util.AccessToken;
import com.yc.community.sys.util.AuthProvider;
import com.yc.community.sys.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import response.CommonResponse;


@Slf4j
@Service
public class AuthServiceImpl{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public AccessToken login(String loginAccount, String password) {
        // 1 创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernameAuthentication = new UsernamePasswordAuthenticationToken(loginAccount, password);
        // 2 认证
        Authentication authentication = this.authenticationManager.authenticate(usernameAuthentication);
        // 3 保存认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4 生成自定义token
        AccessToken accessToken = jwtProvider.createToken((UserDetails) authentication.getPrincipal());

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        // 放入缓存
        stringRedisTemplate.opsForValue().set(userDetail.getUsername(), JSONObject.toJSONString(userDetail));
        return accessToken;
    }

    public void logout() {
        stringRedisTemplate.opsForValue().set(AuthProvider.getLoginAccount(), "");
        SecurityContextHolder.clearContext();
    }


    public void refreshToken(String token) {
        AccessToken accessToken = jwtProvider.refreshToken(token);
        String userDetail = stringRedisTemplate.opsForValue().get(accessToken.getLoginAccount());
        stringRedisTemplate.opsForValue().set(accessToken.getLoginAccount(), userDetail);
    }
}
