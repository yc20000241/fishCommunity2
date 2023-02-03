package com.yc.community.service.modules.chats.netty;


import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.util.JsonUtils;
import com.yc.community.service.modules.chats.entity.FishChatInfo;
import com.yc.community.service.modules.chats.response.ChatData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
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

    private HashMap<String, HashMap<String, Channel>> ctxIdWithUserIdAndChannel = new HashMap<>();

    @Resource(name = "chatUserChannelCache")
    private Cache<String, Object> chatUserCache;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg){
        //获取客户端所传输的消息
        String content = msg.text();
        if(content != null && content.equals("chat ping")){
            log.info("收到聊天心跳"+ content);
            ctx.channel().writeAndFlush(new TextWebSocketFrame("chat pong"));
            return;
        }
        log.info("客户端所传输的消息"+content);
        //1.获取客户端发来的消息
//        ChatData chatData = JSONObject.parseObject(content, ChatData.class);
		ChatData chatData = JsonUtils.jsonToPojo(content, ChatData.class);
        Channel channel =  ctx.channel();
        FishChatInfo fishChatInfo = chatData.getFishChatInfo();

        if(chatData.getAction() == 0){ //刚登录
            chatUserCache.put(fishChatInfo.getUserId(), channel);
        }else if(chatData.getAction() == 1){  //创建对话
            HashMap<String, Channel> userIdAndChannel = new HashMap<>();
            userIdAndChannel.put(fishChatInfo.getUserId(), channel);
            String key = ctx.channel().id().asShortText();
            ctxIdWithUserIdAndChannel.put(key, userIdAndChannel);

            ChatData chatData1 = new ChatData();
            chatData1.setCtxId(key);
            channel.writeAndFlush(new TextWebSocketFrame(
                    JsonUtils.objectToJson(chatData1)
            ));
        }else if(chatData.getAction() == 2){  // 加入对话
            HashMap<String, Channel> stringChannelHashMap = ctxIdWithUserIdAndChannel.get(chatData.getCtxId());
            stringChannelHashMap.put(fishChatInfo.getUserId(), channel);
        }else if(chatData.getAction() == 3){  // 发送信息
            HashMap<String, Channel> stringChannelHashMap = ctxIdWithUserIdAndChannel.get(chatData.getCtxId());
            Channel channel1 = stringChannelHashMap.get(fishChatInfo.getFriendId());

            channel1.writeAndFlush(new TextWebSocketFrame(
                    JsonUtils.objectToJson(chatData)
            ));
        }


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
