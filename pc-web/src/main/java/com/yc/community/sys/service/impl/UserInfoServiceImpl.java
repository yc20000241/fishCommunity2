package com.yc.community.sys.service.impl;

import com.yc.community.common.util.CopyUtil;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import com.yc.community.sys.mapper.RoleMenuMapper;
import com.yc.community.sys.mapper.UserInfoMapper;
import com.yc.community.sys.response.AuthorUserInfoResponse;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.response.MenuVo;
import com.yc.community.sys.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public InitUserInfoResponse getInitUserInfo(HttpServletRequest request) {
        String authToken = jwtProvider.getToken(request);
        int length = jwtProperties.getTokenPrefix().length();
        authToken = authToken.substring(length);
        String loginAccount = jwtProvider.getSubjectFromToken(authToken);
        UserDetail userDetails = (UserDetail)redisTemplate.opsForValue().get(loginAccount);
        UserInfo userInfo = userDetails.getUserInfo();

        List<String> roles = userDetails.getRoles();
        List<MenuVo> list = roleMenuMapper.getRolesMenuList(roles);

        InitUserInfoResponse copy = CopyUtil.copy(userInfo, InitUserInfoResponse.class);
        copy.setMenuVoList(list);

        return copy;
    }

    @Override
    public AuthorUserInfoResponse getUserInfoById(String id) {
        UserInfo byId = getById(id);
        AuthorUserInfoResponse copy = CopyUtil.copy(byId, AuthorUserInfoResponse.class);
        return copy;
    }
}
