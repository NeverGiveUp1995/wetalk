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


    //获取好友之前所在的分组id
    public Integer getOldGroupId(String creatorId, String friendId);

    /**
     * 添加好友到分组
     *
     * @param userAccount
     * @param groupId
     * @param addTime
     * @return
     */
    public int addFriendToGroup(String userAccount, int groupId, String addTime);

    /**
     * 移动好友到分组(已添加的好友)
     *
     * @param oldGroupId：目标分组id
     * @param newGroupId:好友分组
     * @param targetUserAccount :被移动的用户账号
     * @param addTime:移动时间
     * @return
     */
    public int removeFriendToGroup(int oldGroupId, int newGroupId,String targetUserAccount ,String addTime);

    /**
     * 添加同意时间，在好友表中
     *
     * @param activeUserId
     * @param passiveUserId
     * @param agreeTime
     * @return
     */
    public int agreeAddFriend(String activeUserId, String passiveUserId, String agreeTime);

    public Friend checkFriend(String activeUserAccount, String passiveUserAccount);
}
