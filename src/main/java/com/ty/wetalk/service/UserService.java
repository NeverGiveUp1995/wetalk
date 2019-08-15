package com.ty.wetalk.service;

import com.ty.wetalk.mapper.UserMapper;
import com.ty.wetalk.model.MessageResult;
import com.ty.wetalk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserByAccount(String userAccount) {
        return userMapper.getUserByAccount(userAccount);
    }

    public User login(String userAccount, String password) {
        return userMapper.login(userAccount, password);
    }

    public List<MessageResult> getMessages(String userAccount){
        return userMapper.getMessages(userAccount);
    }

    public int  getConversationId(String senderId,String receiverId){return  userMapper.getConversationId(senderId,receiverId);}
}
