package service;

import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.sys.mapper.RolePermissionMapper;
import com.yc.community.sys.service.impl.RolePermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.RolePathsVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CacheService {

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
