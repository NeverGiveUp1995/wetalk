package com.ty.wetalk.model;

import lombok.Data;

@Data
public class MessageRecord {
    int messageId;//消息id
    int conversationId;//本消息所在会话id
    int msgType;//消息类型：0.系统消息，1.私人会话消息，2.群消息
    String senderId;//发送人id
    String receiverId;//收信人id
    String sendTime;//发送时间
    String content;//发送内容

    public MessageRecord() {
    }

}
