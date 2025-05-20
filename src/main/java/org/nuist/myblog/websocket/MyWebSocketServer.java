package org.nuist.myblog.websocket;

import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/chat") // WebSocket 服务端地址
@Component
public class MyWebSocketServer {

    // 保存所有连接
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("新连接加入: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到消息: " + message);

        // 简单广播给所有客户端（你可以根据需要改为私聊）
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(message); // 直接发送原始 JSON 字符串
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("连接关闭: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("连接异常: " + session.getId() + " - " + error.getMessage());
    }
}
