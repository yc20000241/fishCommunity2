package com.yc.community.security.component;

import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserDetailAcessUserInfo {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public UserDetail getUserDetail(HttpServletRequest request){
        // 拿到Authorization请求头内的信息
        String authToken = jwtProvider.getToken(request);
        // 去掉token前缀(Bearer )，拿到真实token
        int length = jwtProperties.getTokenPrefix().length();
        authToken = authToken.substring(length);

        // 拿到token里面的登录账号
        String loginAccount = jwtProvider.getSubjectFromToken(authToken);
        UserDetail userDetails = (UserDetail)redisTemplate.opsForValue().get(loginAccount);
        return userDetails;
    }

    public UserInfo getUserInfo(HttpServletRequest request){
        UserDetail userDetail = getUserDetail(request);
        return userDetail.getUserInfo();
    }

    public List<RoleInfo> getRoleInfoList(HttpServletRequest request){
        UserDetail userDetail = getUserDetail(request);
        return userDetail.getRoleInfoList();
    }

    public List<String> getRoles(HttpServletRequest request){
        UserDetail userDetail = getUserDetail(request);
        return userDetail.getRoles();
    }
}
