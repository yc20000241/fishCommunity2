package com.yc.community.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.sys.entity.RoleUser;
import com.yc.community.sys.mapper.RoleInfoMapper;
import com.yc.community.sys.service.IRoleInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements IRoleInfoService {

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private RoleUserServiceImpl roleUserService;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    public List<RoleInfo> listRoleByUserId(String id) {
        List<RoleInfo> list = roleInfoMapper.listRoleByUserId(id);
        return list;
    }

    @Override
    public HashMap<String, List<UserInfo>> getRoleAndNotRoleList(String roleId) {
        HashMap<String, List<UserInfo>> result = new HashMap<>();

        List<RoleUser> roleUserList = roleUserService.list(new QueryWrapper<RoleUser>().eq("role_id", roleId));
        List<String> userIdList = roleUserList.stream().map(RoleUser::getUserId).collect(Collectors.toList());
        List<UserInfo> hasRoleList = userInfoService.listByIds(userIdList);
        List<String> allUserId = roleUserService.list().stream().map(RoleUser::getUserId).collect(Collectors.toList());
        List<UserInfo> notRoleList = userInfoService.list(new QueryWrapper<UserInfo>().notIn("id", allUserId));

        result.put("hasRoleList", hasRoleList);
        result.put("notRoleList", notRoleList);
        return result;
    }
}
