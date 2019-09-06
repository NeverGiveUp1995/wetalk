/**
 * 关于好友的所有接口
 */
package com.ty.wetalk.mapper;

import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.User;

import java.util.List;

public interface FriendMapper {
    public List<Group> getGroupsByUserAccount(String userAccount);

    public List<User> getFriendsByGroupId(int groupId);
}
