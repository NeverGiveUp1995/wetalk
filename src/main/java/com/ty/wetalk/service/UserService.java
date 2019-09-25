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

    public User getUserByPhoneNum(String phoneNum) {
        return userMapper.getUserByPhoneNum(phoneNum);
    }

    public User login(String userAccount, String password) {
        System.out.println("正在登录。。。。");

        User user = userMapper.login(userAccount, password);
        System.out.println("映射上了");

        return user;
    }

    public List<MessageResult> getMessages(String userAccount) {
        return userMapper.getMessages(userAccount);
    }

    public int getConversationId(String senderId, String receiverId) {
        //如果当前两个用户还没有添加好友，则先创建一个会话记录，生成一个conversationId
        if (userMapper.getConversationCountOfUsers(senderId, receiverId) == 0) {
            if (userMapper.creatConversation(senderId, receiverId) == -1) {
                return -1;
            }
        }
        return userMapper.getConversationId(senderId, receiverId);
    }

    public int register(User user) throws Exception {
        return userMapper.register(user);
    }
}
