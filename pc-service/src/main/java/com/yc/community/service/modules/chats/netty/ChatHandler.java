package com.yc.community.service.modules.chats.netty;


import com.yc.community.common.util.JsonUtils;
import com.yc.community.service.modules.chats.request.ChatData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;


/**
 * 用于处理消息的handler
 * 由于它的传输数据的载体是frame，这个frame 在netty中，是用于为websocket专门处理文本对象的，frame是消息的载体，此类叫：TextWebSocketFrame
 */
@DependsOn("springUtil")
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于记录和管理所有客户端的channel
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static HashMap<String, HashMap<String, Channel>> ctxIdWithRoom = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg){
        //获取客户端所传输的消息
        String content = msg.text();
        log.info("客户端所传输的消息"+content);
        //1.获取客户端发来的消息
		ChatData chatDataResponse = null;
		chatDataResponse = JsonUtils.jsonToPojo(content, ChatData.class);
        
        Channel channel =  ctx.channel();
		channel.writeAndFlush(new TextWebSocketFrame(
				JsonUtils.objectToJson(null)
		));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
        log.info("加入channel_id为"+ctx.channel().id());
	}

	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String chanelId = ctx.channel().id().asShortText();
		System.out.println("客户端被移除：channel id 为："+chanelId);
		log.info("客户端被移除：channel id 为："+chanelId);

		users.remove(ctx.channel());

    }


	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
		//发生了异常后关闭连接，同时从channelgroup移除
		log.info("netty异常"+cause);
		ctx.channel().close();
		users.remove(ctx.channel());

    }


}
