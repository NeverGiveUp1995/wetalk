package com.ty.wetalk.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.ty.wetalk.Enums.SystemMsgType;
import com.ty.wetalk.model.*;
import com.ty.wetalk.service.*;
import com.ty.wetalk.utils.ResponseResult;
import com.ty.wetalk.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import static com.ty.wetalk.sessions.UserSession.onlineUserSessions;

@ServerEndpoint("/WebSocketServer/{userAccount}")
@RestController
public class WebSocketServer {
    //定义消息处理的服务，用于存放消息到数据库等操作
    @Autowired
    private MessageService messageService;
    //定义用户服务接口，用于根据用户id获取用户信息，将用户信息封装成对象返回
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    //定义静态变量onlineCount，用于记录当前在线的连接数。
    private static int onlineCount = 0;
    //concurrent 包的线程安全Set，用于存放每个用户客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    private static WebSocketServer webSocketServer;

    @PostConstruct
    public void init() {
        webSocketServer = this;
        webSocketServer.userService = this.userService;
        webSocketServer.messageService = this.messageService;
        webSocketServer.groupService = this.groupService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userAccount") String userAccount) throws Exception {
//        将当前的session存放在hashmap中
        Session checkSession = onlineUserSessions.get(userAccount);
        if (checkSession == null) {
            onlineUserSessions.put(userAccount, session);
            webSocketSet.add(this);
            LOGGER.info("创建一个{}的websocket的连接,当前登录人数{}", userAccount, webSocketSet.size());
        } else {
            onlineUserSessions.replace(userAccount, session);
            LOGGER.info("当前用户{}已连接到服务器,已替换连接", userAccount);
        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        String userKey = null;
        for (Map.Entry<String, Session> entry : onlineUserSessions.entrySet()) {
            if (entry.getValue().equals(this)) {
                userKey = entry.getKey();
                System.out.println("找到用过户：" + entry.getKey());
            }
        }
        if (userKey != null) {
            onlineUserSessions.remove(userKey);
        }
        LOGGER.info("连接中断。。。 ");
    }

    /**
     * 服务端发送消息给前端时调用
     *
     * @param userAccount 发送目标id
     * @param session     会话，每个访问对象都会有一个单独的会话
     * @param message     待发送的消息
     */
    @OnMessage
    public void onMessage(@PathParam("userAccount") String userAccount, Session session, String message) {
        System.out.println("收到的消息：" + message);
        //消息类型：1:系统消息；2：私人消息，3.群组消息
        String jsonStr = message.split("\\-")[0];
        JsonDataWithMessageContent jsonDataWithMessageContent = JSON.parseObject(jsonStr, JsonDataWithMessageContent.class);
        String receiverAccount = jsonDataWithMessageContent.getReceiverAccount();
        String msgType = jsonDataWithMessageContent.getMsgType();
        System.out.println("userAccount" + userAccount + "================receiverAccount" + receiverAccount);

        String msgContent = message.substring(message.indexOf('-') + 1, message.length());
        switch (msgType) {
            case "1"://系统消息
                sendMessage(userAccount, receiverAccount, msgContent, "1");
                break;
            case "2"://私人信息
                sendMessage(userAccount, receiverAccount, msgContent, "2");
                break;
            case "3"://群组消息
                List<User> users = groupService.getUsersByGroupAccount(receiverAccount);
                //将消息发送给该群中的所有用户
                for (User user : users) {
                    sendMessage(userAccount, user.getAccount(), msgContent, "3");
                }
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("Error while websocket. ", error);
    }


    public void sendMessageAll(String messageContent) throws Exception {

        for (Map.Entry<String, Session> stringSessionEntry : onlineUserSessions.entrySet()) {
            Map.Entry entry = (Map.Entry) (stringSessionEntry);
            try {
                String key = (String) entry.getKey();
                Session session = (Session) entry.getValue();
                synchronized (session) {
                    System.out.println("正在发送和用户【" + key + "】保持连接的消息");
                    sendMsgFromServer(key, session, messageContent);
                }
            } catch (ClassCastException e) {
                System.out.println("获取到的数据不是session类型，保持连接失败！");
            }
        }
        System.out.println("\n\n\n");
    }

    /**
     * 服务器发送消息给客户端
     */
    public void sendMsgFromServer(String key, Session session, String msgContent) throws IOException {
        if (session.isOpen()) {
            System.out.println("来自系统的消息！===>" + msgContent);
            Message message = new Message();
            message.setMsgType("1");
            message.setContent(SystemMsgType.HEARTBEAT.toString());
            message.setSender(new User());
            message.setReceiver(new User());
            session.getBasicRemote().sendText(JSON.toJSONString(new ResponseResult("1", message, null)));
        } else {
            onlineUserSessions.remove(key);
            System.out.println("客户端连接中断，心跳信息发送失败！");
        }
    }

    /**
     * 向指定Session(用户)发送message
     *
     * @param senderAccount:发送者id
     * @param receiverAccount：接收者id
     * @param messageContent：消息内容
     */
    public void sendMessage(String senderAccount, String receiverAccount, String messageContent, String msgType) {
        System.out.println(senderAccount + "正在发送消息给" + receiverAccount);
        Session session = onlineUserSessions.get(receiverAccount);
        RemoteEndpoint.Basic basicRemote = null;
//        获取发送者信息
        User sender = webSocketServer.userService.getUserByAccount(senderAccount);
        //获取接收者信息
        User receiver = webSocketServer.userService.getUserByAccount(receiverAccount);
//获取正在聊天的房间号
        int conversationId = webSocketServer.userService.getConversationId(senderAccount, receiverAccount);
        if (conversationId != -1) {
            //        将发送的消息存入数据库
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = new java.sql.Date(new java.util.Date().getTime());
            MessageRecord msg = new MessageRecord();
            msg.setConversationId(conversationId);
            msg.setSenderId(senderAccount);
            msg.setReceiverId(receiverAccount);
            msg.setContent(messageContent);
            msg.setSendTime(simpleDateFormat.format(currentDate));
            msg.setMsgType(msgType);
            //如果消息不是系统消息中的更新跟组列表指令、心跳消息，则将消息存入数据库
            if (msgType.equals("1") && !messageContent.equals(SystemMsgType.UPDATEGROUPLIST.toString()) || !messageContent.equals(SystemMsgType.HEARTBEAT.toString())) {
                webSocketServer.messageService.addMessage(msg);
            }
            /*
             * 1.发送消息对象,如果对方在线，直接将消息发送到对方，然后将消息存放在数据库中
             * 2.如果对方不在线，不用发送消息，直接将消息存放在数据库中，并且做上未收消息
             */
            if (session != null && session.isOpen()) {
                basicRemote = session.getBasicRemote();
                Message message = new Message();
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setSendTime(Utils.dateToString(new java.util.Date()));
                message.setContent(messageContent);
                message.setConversationId(conversationId);
                message.setMsgType(msgType);
                try {
                    System.out.println("准备发送消息给用户【+" + receiverAccount + "+】,消息内容为：【" + messageContent + "】");
                    //发送消息
                    ResponseResult responseResult = new ResponseResult();
                    responseResult.status = "1";
                    responseResult.data = message;
                    responseResult.tip = null;
                    synchronized (session) {
                        System.out.println("返回的json数据===》" + JSON.toJSONString(responseResult));
                        basicRemote.sendText(JSON.toJSONString(responseResult));
                    }
                } catch (Exception e) {
                    System.out.println("发送失败！");
//                e.printStackTrace();
                }
            } else {
                System.out.println("目标用户不在线，即将将数据存入数据库");
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
