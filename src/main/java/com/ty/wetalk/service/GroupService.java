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

    /**
     * 新增分组
     * @param userAccount
     * @param groupName
     * @param creatTime
     * @return
     */
    public  int creatGroup(String userAccount ,String groupName,String creatTime){
        if(getGroupIdByUserAccountAndGroupName(userAccount,groupName)==null){
            System.out.println("正在创建分组");
            return groupMapper.creatGroup( userAccount , groupName, creatTime);
        }else{
            System.out.println("创建分组失败！");
            return  -1;
        }
    }

    /**
     * 查询(按照用户的分组名称)分组是否存在
     */
    public  Integer getGroupIdByUserAccountAndGroupName(String userAccount,String groupName){
        return  groupMapper.getGroupIdByUserAccountAndGroupName(userAccount,groupName);
    }
}
