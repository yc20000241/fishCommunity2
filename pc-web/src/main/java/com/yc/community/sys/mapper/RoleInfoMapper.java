package com.yc.community.sys.mapper;

import com.yc.community.sys.entity.RoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@Mapper
@Component
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    List<RoleInfo> listRoleByUserId(@Param("id") String id);
}
