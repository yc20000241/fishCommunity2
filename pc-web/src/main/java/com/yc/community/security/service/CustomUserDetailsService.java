package com.yc.community.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.service.impl.RoleInfoServiceImpl;
import com.yc.community.sys.service.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private RoleInfoServiceImpl roleInfoService;

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("开始登陆验证，用户名为: {}", s);

        // 根据用户名验证用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getUserName, s);
        UserInfo userInfo = userInfoService.getOne(queryWrapper);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户名不存在，登陆失败。");
        }

        // 构建UserDetail对象
        UserDetail userDetail = new UserDetail();
        userDetail.setUserInfo(userInfo);
        List<RoleInfo> roleInfoList = roleInfoService.listRoleByUserId(userInfo.getId());
        if(roleInfoList.size() == 0)
            throw new BusinessException(BusinessExceptionCode.PLease_ADMIN_GIVE_A_ROLE);
        userDetail.setRoleInfoList(roleInfoList);
        return userDetail;
    }
}
