package com.yc.community.service.modules.chats.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用于检测channel 的心跳handler
 * 继承ChannelInboundHandlerAdapter，目的是不需要实现ChannelRead0 这个方法
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;//强制类型转化
            if(event.state()== IdleState.READER_IDLE){
//                log.info("进入读空闲......");
//                System.out.println("进入读空闲......");
            }else if(event.state() == IdleState.WRITER_IDLE) {
//                log.info("进入写空闲......");
//                System.out.println("进入写空闲......");
            }else if(event.state()== IdleState.ALL_IDLE){
//                log.info("channel 关闭之前：users 的数量为："+ChatHandler.users.size());
//                System.out.println("channel 关闭之前：users 的数量为："+ChatHandler.users.size());
                Channel channel = ctx.channel();
                //资源释放
                channel.close();
//                System.out.println("channel 关闭之后：users 的数量为："+ChatHandler.users.size());
//                log.info("channel 关闭之前：users 的数量为："+ChatHandler.users.size());
            }
        }
    }


}
