package com.ty.wetalk.controller;

import com.ty.wetalk.model.Message;
import com.ty.wetalk.model.MessageResult;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.UserService;
import com.ty.wetalk.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin//允许跨域的请求
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserByUserAccount")
    public ResponseResult getUserByUserAccount(@RequestParam String userAccount) {
        System.out.println("获取用户中。。" + userAccount + "--------" + userService.getUserByAccount(userAccount));
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(userService.getUserByAccount(userAccount));
        return responseResult;
    }

    @RequestMapping(value = "/getUserByPhoneNum")
    public ResponseResult getUserByPhoneNum(@RequestParam String phoneNum) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(userService.getUserByPhoneNum(phoneNum));
        return responseResult;
    }

    @RequestMapping(value = "/register")
    public ResponseResult register(@RequestParam String userAccount, @RequestParam String password, @RequestParam String nickName, @RequestParam String phoneNum, String email, @RequestParam String gender) {
        ResponseResult responseResult = new ResponseResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new java.sql.Date(new java.util.Date().getTime());
        User user = new User();
        user.setAccount(userAccount);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        user.setGender(gender);
        user.setRegisterDate(simpleDateFormat.format(currentDate));
        System.out.println("正在注册用户：" + user);
        try {
            responseResult.setData(userService.register(user));
            responseResult.setTip("注册成功！");
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                responseResult.setTip("该账号已存在！");
            } else {
                responseResult.setStatus("0");
                responseResult.setTip("注册失败！请稍后重新尝试");
            }
            System.out.println("注册失败！");
            System.out.println(e);
        }
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
            responseResult.setTip("登录成功！");
        }
        System.out.println("responseResult==>" + responseResult);
        return responseResult;
    }

    /**
     * 获取消息列表
     *
     * @param userAccount：必传，用户账号
     * @param msgType：消息类型（不传默认获取所有类型）
     * @param count：数量(必传：-1代表全部):两种情况： 1.传入了msgType：
     *                                  ---------传入count：获取条数
     *                                  ---------count=-1：获取该类型的所有记录
     *                                  2.未传入msgType：
     *                                  ---------count不生效，
     * @return
     */
    @RequestMapping("/getMessages")
    public ResponseResult getMessages(@RequestParam String userAccount, String msgType, @RequestParam int count) {
//        System.out.println("传入的参数===>:" + userAccount + "=====" + msgType + "=====" + count);
        ResponseResult responseResult = new ResponseResult();
        List<MessageResult> messageResults = new ArrayList<MessageResult>();
        if (msgType == null) {
            //获取系统消息
            List<MessageResult> systemMessageResults = userService.getMessages(userAccount, "1", 1);
            //获取私人聊天记录
            List<MessageResult> personMessageResults = userService.getMessages(userAccount, "2", -1);

            if (personMessageResults != null) {
                messageResults.addAll(personMessageResults);
            }
            if (systemMessageResults != null) {
                messageResults.addAll(systemMessageResults);
            }
        } else {
            messageResults = userService.getMessages(userAccount, msgType, -1);
        }
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
                    result.getGender(),
                    result.getBirthday(),
                    result.getAddress(),
                    result.getIsVip(),
                    result.getRegisterDate()
            );
            User receiver = new User(
                    result.getAccount1(),
                    result.getPassword1(),
                    result.getNickName1(),
                    result.getHeaderImg1(),
                    result.getPhoneNum1(),
                    result.getEmail1(),
                    result.getGender1(),
                    result.getBirthday1(),
                    result.getAddress1(),
                    result.getIsVip1(),
                    result.getRegisterDate1()
            );
            message.setMessageId(result.getMessageId());
            message.setConversationId(result.getConversationId());
            message.setContent(result.getContent());
            message.setSendTime(result.getSendTime());
            message.setSender(sender);
            message.setReceiver(receiver);
            messages.add(0, message);
            message.setMsgType(result.getMsgType());
        }

        System.out.println(messages);
        responseResult.setData(messages);
        return responseResult;
    }
}
