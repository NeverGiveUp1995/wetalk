/**
 * 消息相关控制器
 */
package com.ty.wetalk.controller;

import com.ty.wetalk.model.Message;
import com.ty.wetalk.model.MessageResult;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.MessageService;
import com.ty.wetalk.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin//允许跨域的请求
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/getChatHistory")
    public ResponseResult getChatHistory(@RequestParam String currentUserAccount, @RequestParam String friendAccount) {
        System.out.println("接口被调用" + currentUserAccount + "------------" + friendAccount);
        ResponseResult responseResult = new ResponseResult();
        List<Message> messageHistory = new ArrayList<>();
        List<MessageResult> results = messageService.getChatHistory(currentUserAccount, friendAccount);
        System.out.println("从数据库获取的数据：" + results.size());
        results.forEach(messageResult -> {
            User sender = new User();
            User receiver = new User();
            //设置发送者用户信息
            sender.setAccount(messageResult.getSenderId());
            sender.setNickName(messageResult.getNickName());
            sender.setHeaderImg(messageResult.getHeaderImg());
            //设置接收者用户的消息
            receiver.setAccount(messageResult.getReceiverId());
            receiver.setNickName(messageResult.getNickName1());
            receiver.setHeaderImg(messageResult.getHeaderImg1());
            messageHistory.add(new Message(
                    messageResult.getMessageId(),
                    messageResult.getConversationId(),
                    sender,
                    receiver,
                    messageResult.getSendTime(),
                    messageResult.getContent()
            ));
        });
        responseResult.setMsgType("1");
        responseResult.setStatus("1");
        responseResult.setData(messageHistory);
        System.out.println(responseResult);
        return responseResult;
    }
}
