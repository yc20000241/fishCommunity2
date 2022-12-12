package com.yc.community.sys.service.impl;

import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import com.yc.community.sys.mapper.UserInfoMapper;
import com.yc.community.sys.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserInfo getUserInfo(HttpServletRequest request) {
        String authToken = jwtProvider.getToken(request);
        int length = jwtProperties.getTokenPrefix().length();
        authToken = authToken.substring(length);
        String loginAccount = jwtProvider.getSubjectFromToken(authToken);
        UserDetail userDetails = (UserDetail)redisTemplate.opsForValue().get(loginAccount);
        UserInfo userInfo = userDetails.getUserInfo();
        return userInfo;
    }
}
