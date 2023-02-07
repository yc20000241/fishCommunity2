package com.yc.community.service.modules.chats.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class NettyServer {

    private static class SingletionWSServer {
        static final NettyServer instance = new NettyServer();
    }

    public static NettyServer getInstance() {
        return SingletionWSServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;
    private Channel serverChannel;

    public NettyServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitialzer());
    }

    public void start() throws InterruptedException {
//        this.future = server.bind("172.27.75.4",8888);
        this.future = server.bind("127.0.0.1",17777);
        serverChannel = future.sync().channel().closeFuture().sync().channel();
        if (future.isSuccess()) {
            log.info("启动 Netty 成功");
        }else{
            log.info("启动 Netty 失败");
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannel.close();
        serverChannel.parent().close();
        log.info("netty关闭");
    }
}
