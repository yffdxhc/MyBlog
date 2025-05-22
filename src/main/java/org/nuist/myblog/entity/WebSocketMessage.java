package org.nuist.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String sender;     // 发送者 user_number
    private String receiver;   // 接收者 user_number（私聊用）
    private String data;       // 消息内容
    private String type;       // 消息类型，如 chat、system 等
    private long timestamp;    // 消息发送时间（可用毫秒时间戳表示）

    private User senderInfo;
    private User receiverInfo;
}
