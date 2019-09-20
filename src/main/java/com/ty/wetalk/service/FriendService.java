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
        return friendMapper.searchFriend(currentUserAccount,kw);
    }

    public int addFriend(String activeUserId, String passiveUserId, String addTime) {
        return friendMapper.addFriend(activeUserId, passiveUserId, addTime);
    }

    public Friend checkFriend(String activeUserAccount, String passiveUserAccount) {
        return friendMapper.checkFriend(activeUserAccount, passiveUserAccount);
    }

}
