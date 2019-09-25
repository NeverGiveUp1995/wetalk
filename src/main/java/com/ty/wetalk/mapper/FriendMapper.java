/**
 * 关于好友的所有接口
 */
package com.ty.wetalk.mapper;

import com.ty.wetalk.model.Friend;
import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.SearchUser.SearchUser;
import com.ty.wetalk.model.SearchUser.SearchUsersSetRow;
import com.ty.wetalk.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface FriendMapper {
    public List<Group> getGroupsByUserAccount(String userAccount);

    public List<User> getFriendsByGroupId(int groupId);

    public List<SearchUsersSetRow> searchFriend(String currentUserAccount, @RequestParam String kw);

    public int addFriend(String activeUserId, String passiveUserId, String addTime);

    public Friend checkFriend(String activeUserAccount, String passiveUserAccount);
}
