package com.ty.wetalk.controller;

import com.ty.wetalk.model.Message;
import com.ty.wetalk.model.MessageResult;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.UserService;
import com.ty.wetalk.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin//允许跨域的请求
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUser")
    public ResponseResult getUser(@RequestParam String userAccount) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(userService.getUserByAccount(userAccount));
        return responseResult;
    }

    @RequestMapping("/login")
    public ResponseResult login(@RequestParam String userAccount, @RequestParam String password) {
        ResponseResult responseResult = new ResponseResult();
        User currentUser = userService.login(userAccount, password);
        if (currentUser == null) {
            responseResult.setTip("账户或密码错误！");
            responseResult.setStatus("0");
        } else {
            responseResult.setData(currentUser);
        }
        System.out.println("responseResult==>" + responseResult);
        return responseResult;
    }

    @RequestMapping("/getMessages")
    public ResponseResult getMessages(@RequestParam String userAccount) {
        System.out.println("传入的参数===>:" + userAccount);
        ResponseResult responseResult = new ResponseResult();
        List<MessageResult> messageResults = userService.getMessages(userAccount);
        List<Message> messages = new ArrayList<Message>();
        for (MessageResult result : messageResults) {
            Message message = new Message();
            User sender = new User(
                    result.getAccount(),
                    result.getPassword(),
                    result.getNickName(),
                    result.getHeaderImg(),
                    result.getPhoneNum(),
                    result.getEmail(),
                    result.getSex(),
                    result.getBirthday(),
                    result.getAddress()
            );
            User receiver = new User(
                    result.getAccount1(),
                    result.getPassword1(),
                    result.getNickName1(),
                    result.getHeaderImg1(),
                    result.getPhoneNum1(),
                    result.getEmail1(),
                    result.getSex1(),
                    result.getBirthday1(),
                    result.getAddress1()
            );
            message.setMessageId(result.getMessageId());
            message.setConversationId(result.getConversationId());
            message.setContent(result.getContent());
            message.setSendTime(result.getSendTime());
            message.setSender(sender);
            message.setReceiver(receiver);
            messages.add(0, message);
        }

        System.out.println(messages);
        responseResult.setData(messages);
        return responseResult;
    }
}
