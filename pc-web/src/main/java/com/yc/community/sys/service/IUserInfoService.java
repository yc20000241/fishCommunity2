package com.yc.community.sys.service;

import com.yc.community.sys.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.sys.response.AuthorUserInfoResponse;
import com.yc.community.sys.response.InitUserInfoResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
public interface IUserInfoService extends IService<UserInfo> {

    InitUserInfoResponse getInitUserInfo(HttpServletRequest request);

    AuthorUserInfoResponse getUserInfoById(String id);
}
