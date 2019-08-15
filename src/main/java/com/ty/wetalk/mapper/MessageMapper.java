package com.ty.wetalk.mapper;

import com.ty.wetalk.model.MessageRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    void addMessage(int conversationId, String senderId, String receiverId, String sendTime, String content);
}

