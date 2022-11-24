package com.yc.community.sys.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.sys.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yc.community.common.pojo.RolePathsVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CacheServiceImpl {

    @Resource(name = "rolePermissionListCache")
    private Cache<String, List<String>> rolePermissionListCache;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    public void initRolePermissionList(){
        List<RolePathsVo> list = rolePermissionMapper.getRolePaths();

        Map<String, List<RolePathsVo>> rolePaths = list.stream().collect(Collectors.groupingBy(RolePathsVo::getRoleId));
        rolePaths.forEach((roleId, voList) -> {
            List<String> pathList = voList.stream().map(RolePathsVo::getPath).collect(Collectors.toList());
            rolePermissionListCache.put(roleId, pathList);
        });
    }

}
