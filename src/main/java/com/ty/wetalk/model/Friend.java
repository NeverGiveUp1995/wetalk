package com.ty.wetalk.model;

import lombok.Data;

@Data
public class Friend {
    private int friendId;
    private String activeUserId;//主动添加好友的用户
    private String passiveUserId;//被添加为好友的用户
    private String addTime;//添加好友的日期
}
