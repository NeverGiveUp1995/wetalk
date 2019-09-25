package com.ty.wetalk.service;

import com.ty.wetalk.mapper.GroupMapper;
import com.ty.wetalk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupMapper groupMapper;

    public List<User> getUsersByGroupAccount(@RequestParam String groupAccount) {
        return groupMapper.getUsersByGroupAccount(groupAccount);
    }
}
