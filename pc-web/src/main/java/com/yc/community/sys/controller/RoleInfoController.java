package com.yc.community.sys.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.service.IRoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@RestController
@RequestMapping("/api/web/sys/role")
public class RoleInfoController {

    @Autowired
    private IRoleInfoService roleInfoService;

    @GetMapping("/list")
    public CommonResponse list(){
        List<RoleInfo> list = roleInfoService.list();
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/getRoleAndNotRoleList")
    public CommonResponse getRoleAndNotRoleList(@RequestParam("roleId") String roleId){
        HashMap<String, List<UserInfo>> map = roleInfoService.getRoleAndNotRoleList(roleId);
        return CommonResponse.OKBuilder.data(map).build();
    }

}

