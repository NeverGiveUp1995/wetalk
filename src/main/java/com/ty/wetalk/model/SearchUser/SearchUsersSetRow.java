package com.ty.wetalk.model.SearchUser;

import lombok.Data;

@Data
public class SearchUsersSetRow {
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
    private int friendId;
    private String activeUserId;//主动添加好友的用户
    private String passiveUserId;//被添加为好友的用户
    private String addTime;//添加好友的日期
}
