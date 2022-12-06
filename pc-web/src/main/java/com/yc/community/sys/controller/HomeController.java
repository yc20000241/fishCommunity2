package com.yc.community.sys.controller;

import com.yc.community.common.response.CommonResponse;
import com.yc.community.sys.service.impl.HomeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author yc001
 */
@RestController
@RequestMapping("/api/web/home")
public class HomeController {

    @Autowired
    private HomeServiceImpl homeService;

    @PostMapping("/sign")
    public CommonResponse sign(HttpServletRequest request){
        homeService.sign(request);
        return CommonResponse.OK;
    }
}
