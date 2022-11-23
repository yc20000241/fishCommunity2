package com.yc.community.sys.mapper;

import com.yc.community.sys.entity.PermissionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yc001
 * @since 2022-11-23
 */
@Mapper
@Component
public interface PermissionInfoMapper extends BaseMapper<PermissionInfo> {

}
