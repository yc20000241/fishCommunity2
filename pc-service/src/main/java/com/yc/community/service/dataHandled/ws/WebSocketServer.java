package com.yc.community.service.dataHandled.ws;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {
    //    在线人数
    private static int onlineCount;
    //    当前会话
    private Session session;
    //    用户唯一标识
    private String userId;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * concurrent包的线程安全set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap();

    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
//    private final static List<Session> SESSIONS = Collections.synchronizedList(new ArrayList<>());
    private final static ConcurrentHashMap<String,Session> SESSIONSMap = new ConcurrentHashMap();

    /**
     * @methodName: onOpen
     * @description: 建立连接
     * @Author
     * @param
     * @updateTime 2022/8/19 19:31
     * @return void
     * @throws
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketSet.add(this);
//        SESSIONS.add(session);
        SESSIONSMap.put(userId, session);
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        } else {
            webSocketMap.put(userId,this);
            addOnlineCount();
        }
        log.info("[连接ID:{}] 建立连接, 当前连接数:{}", this.userId, getOnlineCount());
    }

    /**
     * @methodName: onClose
     * @description: 断开连接
     * @Author
     * @updateTime 2022/8/19 19:31
     * @return void
     * @throws
     **/
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", userId, getOnlineCount());
    }

    /**
     * @methodName: onError
     * @description: 发送错误
     * @Author
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.userId, error.getMessage());
        error.printStackTrace();
    }

    /**
     * @methodName: onMessage
     * @description: 收到消息
     * @Author
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if(message.equals("ping")) {
            session.getBasicRemote().sendText("pong");
            return;
        }
        log.info("[连接ID:{}] 收到消息:{}", this.userId, message);
    }

    /**
     * @methodName: sendMessage
     * @description: 发送消息
     * @Author
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    public void sendMessage(String message,String userId){
        Session session = SESSIONSMap.get(userId);
        if(session == null)
            return;
        try {
            session.getBasicRemote().sendText(message);
            log.info("【websocket消息】推送消息,[toUser]userId={},message={}", userId,message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @methodName: sendMassMessage
     * @description: 群发消息
     * @Author
     * @updateTime 2022/8/19 19:33
     * @return void
     * @throws
     **/
//    public void sendMassMessage(String message) {
//        try {
//            for (Session session : SESSIONS) {
//                if (session.isOpen()) {
//                    session.getBasicRemote().sendText(message);
//                    log.info("[连接ID:{}] 发送消息:{}",session.getRequestParameterMap().get("userId"),message);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取当前连接数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 当前连接数加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前连接数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
