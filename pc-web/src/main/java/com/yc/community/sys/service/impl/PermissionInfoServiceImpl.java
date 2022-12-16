package com.yc.community.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.community.common.commonConst.ActiveEnum;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.sys.entity.PermissionInfo;
import com.yc.community.sys.mapper.PermissionInfoMapper;
import com.yc.community.sys.service.IPermissionInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-11-23
 */
@Service
public class PermissionInfoServiceImpl extends ServiceImpl<PermissionInfoMapper, PermissionInfo> implements IPermissionInfoService {

    @PostConstruct
    public void init(){
        List<PermissionInfo> list = list(new QueryWrapper<PermissionInfo>().eq("active", ActiveEnum.ACTIVE.getCode()).eq("if_public", ActiveEnum.ACTIVE.getCode()));
        List<String> collect = list.stream().map(PermissionInfo::getPath).collect(Collectors.toList());
        ConstList.PUBLIC_PERMISSION_URL = collect;
    }
}
