package com.ty.wetalk.service;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import com.ty.wetalk.mapper.MessageMapper;
import com.ty.wetalk.model.MessageRecord;
import com.ty.wetalk.model.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ty.wetalk.model.Message;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    public void addMessage(MessageRecord messageRecord) {
        messageMapper.addMessage(messageRecord.getConversationId(), messageRecord.getSenderId(), messageRecord.getReceiverId(), messageRecord.getSendTime(), messageRecord.getContent(), messageRecord.getMsgType());
    }

    public List<MessageResult> getChatHistory(String currentUserAccount, String friendAccount) {
        return messageMapper.getChatHistory(currentUserAccount, friendAccount);
    }
}

