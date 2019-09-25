package com.ty.wetalk.mapper;

import com.ty.wetalk.model.MessageResult;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface MessageMapper {
    void addMessage(int conversationId, String senderId, String receiverId, String sendTime, String content,int msgType);

    List<MessageResult> getChatHistory(String currentUserAccount, String friendAccount);
}

