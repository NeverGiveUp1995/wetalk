package com.ty.wetalk.model;

import lombok.Data;

@Data
public class MessageResult {
    private String account;//用户账号
    private String password;//用户密码
    private String nickName;//用户昵称
    private String headerImg;//用户头像地址
    private String phoneNum;//用户电话
    private String email;//用户邮箱
    private String gender = "2";//用户性别（1：男，2：女）
    private String birthday;//出生日期
    private String address;//地址
    private String isVip;//是否vip
    private String registerDate;//地址
    private int messageId;
    private int conversationId;
    private String msgType;//消息类型【1.系统消息。2.私人消息，3.群组消息】
    private String senderId;
    private String receiverId;
    private String sendTime;
    private String content;
    private String account1;//用户账号
    private String password1;//用户密码
    private String nickName1;//用户昵称
    private String headerImg1;//用户头像地址
    private String phoneNum1;//用户电话
    private String email1;//用户邮箱
    private String gender1 = "2";//用户性别（1：男，2：女）
    private String birthday1;//出生日期
    private String address1;//地址
    private String isVip1;//地址
    private String register1;//地址
    private String registerDate1;//地址

    public MessageResult() {
    }
}
