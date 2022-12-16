package com.yc.community.sys.mapper;

import com.yc.community.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.community.sys.response.MenuVo;
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
 * @since 2022-12-16
 */
@Mapper
@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<MenuVo> getRolesMenuList(@Param("roles") List<String> roles);
}
