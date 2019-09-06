package com.ty.wetalk.model;

import lombok.Data;

@Data
public class User {
    private String account;//用户账号
    private String password;//用户密码
    private String nickName;//用户昵称
    private String headerImg;//用户头像地址
    private String phoneNum;//用户电话
    private String email;//用户邮箱
    private String gender = "2";//用户性别（1：男，2：女）
    private String birthday;//出生日期
    private String address;//地址
    private String isVip;//地址
    private String registerDate;//注册日期
    private boolean isOnline;//是否在线

    public User(String account, String password, String nickName, String headerImg, String phoneNum, String email, String gender, String birthday, String address, String isVip, String registerDate) {
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.headerImg = headerImg;
        this.phoneNum = phoneNum;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.isVip = isVip;
        this.registerDate = registerDate;
    }

    public User() {
    }

}
