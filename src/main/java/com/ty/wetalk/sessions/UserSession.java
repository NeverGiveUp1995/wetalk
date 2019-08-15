package com.ty.wetalk.sessions;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSession {
    //    存放所有在线的用户
    /**
     * 存储所有存活的用户
     * 注意1：高并发问题
     * 注意2：onlineUserSessions必须是全局变量（保证全局就他一个变量，否则每次调用都会被刷新）
     * 注意3：很难保证，用户在退出聊天室时，正确调用了WebSocket.close()，也就是调用后端的onClose()方法，这样
     * 就可能会导致Session无法被有效清除，onlineUserSessions会越来越大，服务器压力也会越来越大。
     * 所以，我们需要周期性的去检查用户是否还处于活跃状态，不活跃的，移除该用户的session
     */
    public static Map<String, Session> onlineUserSessions = new ConcurrentHashMap<>();
}
