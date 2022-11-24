package com.yc.community.sys.mapper;

import com.yc.community.sys.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import com.yc.community.common.pojo.RolePathsVo;

import java.util.List;

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
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<RolePathsVo> getRolePaths();

}
