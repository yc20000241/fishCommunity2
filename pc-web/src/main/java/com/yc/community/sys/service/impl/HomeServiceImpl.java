package com.yc.community.sys.service.impl;

import com.yc.community.security.component.UserDetailAcessUserInfo;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class HomeServiceImpl {

    @Autowired
    private UserDetailAcessUserInfo userDetailAcessUserInfo;

    public void sign(HttpServletRequest request) {
        UserInfo userInfo = userDetailAcessUserInfo.getUserInfo(request);
        System.out.println(userInfo);
    }
}
