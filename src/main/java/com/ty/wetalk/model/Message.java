package com.ty.wetalk.model;

import lombok.Data;

@Data
public class Message {
    int messageId;//消息id
    int conversationId;//本消息所在会话id
    User sender;//发送人
    User receiver;//收信人
    String sendTime;//发送时间
    String content;//发送内容

    public Message() {
    }

}
