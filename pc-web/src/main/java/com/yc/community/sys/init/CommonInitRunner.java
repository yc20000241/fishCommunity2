package com.yc.community.sys.init;

import com.yc.community.service.modules.chats.netty.NettyServer;
import com.yc.community.sys.service.impl.CacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CommonInitRunner implements ApplicationRunner {

    @Autowired
    private CacheServiceImpl cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载数据到缓存中");
        log.info("开始加载角色权限缓存");
        cacheService.initRolePermissionList();
        log.info("角色权限缓存加载完成");
        cacheService.initUserInfo();
        log.info("加载数据加载完成....");
        log.info("加载参数到缓存");
        NettyServer.getInstance().start();
    }
}
