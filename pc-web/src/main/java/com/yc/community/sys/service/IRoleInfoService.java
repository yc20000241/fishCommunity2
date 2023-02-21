package com.yc.community.sys.service;

import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.entity.RoleInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
public interface IRoleInfoService extends IService<RoleInfo> {

    HashMap<String, List<UserInfo>> getRoleAndNotRoleList(String roleId );
}
