package org.nuist.myblog.websocket;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.entity.WebSocketMessage;
import org.nuist.myblog.service.UserService;
import org.nuist.myblog.util.JWTUtils;
import org.nuist.myblog.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 服务端类，用于处理客户端连接、消息收发等操作。
 * 支持基于 token 的身份验证，并实现私聊与群发功能。
 */
@Slf4j
@ServerEndpoint("/ws/chat") // WebSocket 访问路径
@Component
public class MyWebSocketServer {
    private static UserService userService;

    private UserService getUserService() {
        if (userService == null) {
            userService = SpringContextUtils.getBean(UserService.class);
        }
        return userService;
    }


    // 保存所有已连接的会话 Session，用于广播或私聊消息发送
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    /**
     * 当客户端建立连接时触发
     * @param session 客户端会话对象
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        // 从请求参数中获取 token
        List<String> tokenList = session.getRequestParameterMap().get("token");
        String token = (tokenList != null && !tokenList.isEmpty()) ? tokenList.get(0) : null;

        try {
            // 校验 token 是否存在
            if (token == null || token.isEmpty()) {
                throw new JWTVerificationException("Token 缺失");
            }

            // 验证并解析 token（只调用一次即可）
            DecodedJWT decodedJWT = JWTUtils.verify(token);
            String user_number = decodedJWT.getClaim("user_number").asString();

            // 打印日志
            System.out.println("用户 [" + user_number + "] 连接成功，Session ID: " + session.getId());

            // 将用户编号绑定到 session 上下文
            session.getUserProperties().put("userNumber", user_number);

            // 添加当前会话到全局集合
            sessions.add(session);

        } catch (JWTVerificationException e) {
            // token 验证失败，关闭连接并返回错误信息
            System.out.println("无效 token，拒绝连接，Session ID: " + session.getId());
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "无效 token"));
        }
    }

    /**
     * 接收到客户端消息时触发
     * @param message 消息内容（JSON 字符串）
     * @param session 发送消息的客户端会话
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("[websocket] 收到消息：id={}，message={}", session.getId(), message);
        // 获取当前用户编号
        String userNumber = (String) session.getUserProperties().get("userNumber");

        // 解析 JSON 消息为 WebSocketMessage 对象
        WebSocketMessage msg = new Gson().fromJson(message, WebSocketMessage.class);
        String receiver = msg.getReceiver();
        log.info("receiver: {}", receiver);
        log.info("userNumber: {}", userNumber);

        User receiverInfo = getUserService().getUserByUserNumber(receiver);
        User senderInfo = getUserService().getUserByUserNumber(userNumber);

        log.info("receiverInfo: {}", receiverInfo);
        log.info("senderInfo: {}", senderInfo);

        log.info("[websocket] 解析后的消息：{}", msg);
        msg.setSender(userNumber); // 设置发送者（防止伪造）
        msg.setTimestamp(System.currentTimeMillis()); // 设置时间戳
        msg.setSenderInfo(senderInfo);
        msg.setReceiverInfo(receiverInfo);

        // 转换为 JSON 字符串用于发送
        String json = new Gson().toJson(msg);

        // 判断是否为私聊消息
        if (msg.getReceiver() != null &&  !msg.getReceiver().isEmpty()) {
            // 遍历所有会话，找到目标用户发送消息
            for (Session s : sessions) {
                String sessionUserNumber = (String) s.getUserProperties().get("userNumber");
                if (sessionUserNumber != null && sessionUserNumber.equals(msg.getReceiver())|| sessionUserNumber.equals(msg.getSender())) {
                    System.out.println("[websocket] 私聊："+sessionUserNumber +"收到消息"+ "，消息：" + json);
                    s.getBasicRemote().sendText(json);
                }
            }
        } else {
            // 群发消息给所有在线用户
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(json);
                }
            }
        }
    }

    /**
     * 当客户端断开连接时触发
     * @param session 已断开的会话对象
     */
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session); // 从集合中移除该会话
        String userId = (String) session.getUserProperties().get("userNumber");
        System.out.println("用户 [" + userId + "] 连接关闭，Session ID: " + session.getId());
    }

    /**
     * 当连接发生异常时触发
     * @param session 异常发生的会话对象
     * @param error   异常信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        String userId = (String) session.getUserProperties().get("userNumber");
        System.err.println("连接异常 [" + userId + "]: " + error.getMessage());
        error.printStackTrace(); // 打印堆栈信息
    }


}
