package com.ty.wetalk.service;

import com.ty.wetalk.mapper.FriendMapper;
import com.ty.wetalk.model.Friend;
import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.SearchUser.SearchUser;
import com.ty.wetalk.model.SearchUser.SearchUsersSetRow;
import com.ty.wetalk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    FriendMapper friendMapper;

    public List<Group> getGroupsByUserAccount(String userAccount) {
        return friendMapper.getGroupsByUserAccount(userAccount);
    }

    public List<User> getFriendsByGroupId(int groupId) {
        return friendMapper.getFriendsByGroupId(groupId);
    }

    public List<SearchUsersSetRow> searchFriend(String currentUserAccount, @RequestParam String kw) {
        return friendMapper.searchFriend(currentUserAccount, kw);
    }
    //发起添加好友请求
    public int addFriend(String activeUserId, String passiveUserId, String addTime) {
        return friendMapper.addFriend(activeUserId, passiveUserId, addTime);
    }
    //同意添加好友
    public int agreeAddFriend(String activeUserId, String passiveUserId, String addTime) {
        return friendMapper.agreeAddFriend(activeUserId, passiveUserId, addTime);
    }

    //查询是否已经添加好友到分组(获取好友以前在自己所在的分组id)
    public  Integer getOldGroupId(String creatorId,String friendId){
        return  friendMapper.getOldGroupId(creatorId,friendId);
    }

    //添加好友到分组（新增加的好友）
    public int addFriendToGroup(String userAccount, int groupId, String addTime) {
        return friendMapper.addFriendToGroup(userAccount, groupId, addTime);
    }
    //移动好友到分组（已添加的好友）
    public int removeFriendToGroup(int oldGroupId, int newGroupId,String targetUserAccount ,String addTime) {
        return friendMapper.removeFriendToGroup(oldGroupId, newGroupId,targetUserAccount, addTime);
    }


    public Friend checkFriend(String activeUserAccount, String passiveUserAccount) {
        return friendMapper.checkFriend(activeUserAccount, passiveUserAccount);
    }

}
