package com.ty.wetalk.service;

import com.ty.wetalk.mapper.MessageMapper;
import com.ty.wetalk.model.MessageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    public void addMessage(MessageRecord messageRecord) {
        messageMapper.addMessage(messageRecord.getConversationId(), messageRecord.getSenderId(), messageRecord.getReceiverId(), messageRecord.getSendTime(), messageRecord.getContent());
    }
}

