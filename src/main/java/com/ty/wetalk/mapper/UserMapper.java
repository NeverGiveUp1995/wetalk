package com.ty.wetalk.mapper;

import com.ty.wetalk.model.MessageResult;
import com.ty.wetalk.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface UserMapper {
    User getUserByAccount(String userAccount);

    User getUserByPhoneNum(String phoneNum);

    User login(String userAccount, String password);

    List<MessageResult> getMessages(String userAccount);

    int getConversationId(String senderId, String receiverId);

    /**
     * 用户注册
     *
     * @param user
     */
    int register(User user);
}
