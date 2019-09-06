package com.ty.wetalk.service;

import com.ty.wetalk.mapper.FriendMapper;
import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
