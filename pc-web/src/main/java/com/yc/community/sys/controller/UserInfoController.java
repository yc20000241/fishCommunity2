package com.yc.community.sys.controller;


import com.yc.community.common.response.CommonResponse;
import com.yc.community.common.util.CopyUtil;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.request.EditUserInfoRequest;
import com.yc.community.sys.response.AuthorUserInfoResponse;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/web/sys/user")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/getInitUserInfo")
    public CommonResponse getInitUserInfo(HttpServletRequest request){
        InitUserInfoResponse userInfo = userInfoService.getInitUserInfo(request);
        return CommonResponse.OKBuilder.data(userInfo).build();
    }

    @GetMapping("/getUserInfoById")
    public CommonResponse getUserInfoById(@RequestParam("id") String id){
        AuthorUserInfoResponse userInfo = userInfoService.getUserInfoById(id);
        return CommonResponse.OKBuilder.data(userInfo).build();
    }

    @PostMapping("/editUserInfoById")
    public CommonResponse editUserInfoById(EditUserInfoRequest editUserInfoRequest){
        userInfoService.editUserInfoById(editUserInfoRequest);
        return CommonResponse.OKBuilder.msg("资料更新成功").build();
    }

    @GetMapping("/getById")
    public CommonResponse getById(@RequestParam("id") String id){
        UserInfo byId = userInfoService.getById(id);
        InitUserInfoResponse ts = CopyUtil.copy(byId, InitUserInfoResponse.class);
        return CommonResponse.OKBuilder.data(ts).build();
    }

    @GetMapping("/downline")
    public CommonResponse downline(@RequestParam("userId") String userId){
        userInfoService.downline(userId);
        return CommonResponse.OKBuilder.build();
    }

    @GetMapping("/listAll")
    public CommonResponse listAll(@RequestParam("keyWord") String keyWord){
        List<UserInfo> list = userInfoService.listAll(keyWord);
        return CommonResponse.OKBuilder.data(list).build();
    }

    @GetMapping("/deleteUser")
    public CommonResponse deleteUser(@RequestParam("userId") String userId){
        userInfoService.deleteUser(userId);
        return CommonResponse.OKBuilder.build();
    }

    @PostMapping("/add")
    public CommonResponse add(@RequestBody UserInfo userInfo){
        userInfoService.add(userInfo);
        return CommonResponse.OK;
    }
}

