package com.yc.community.sys.service.impl;

import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.sys.mapper.RoleInfoMapper;
import com.yc.community.sys.service.IRoleInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements IRoleInfoService {

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    public List<RoleInfo> listRoleByUserId(String id) {
        List<RoleInfo> list = roleInfoMapper.listRoleByUserId(id);
        return list;
    }
}
