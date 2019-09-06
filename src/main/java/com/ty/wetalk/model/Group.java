/**
 * 好友分组对象
 */
package com.ty.wetalk.model;

import lombok.Data;

import java.util.List;

@Data
public class Group {
    private int groupId;//分组的id
    private String friendGroupName;//分组的名称
    private String creatorId;//分组的创建人
    private String creatTime;//创建该分组的日期
    private List<User> users;//该分组内的好友列表
    private int onlineNum;//在线人数
}
