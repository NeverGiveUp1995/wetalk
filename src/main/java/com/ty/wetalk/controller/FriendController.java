/**
 * 关于好友的控制器
 */
package com.ty.wetalk.controller;

import com.ty.wetalk.Enums.SystemMsgType;
import com.ty.wetalk.model.Friend;
import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.SearchUser.SearchUser;
import com.ty.wetalk.model.SearchUser.SearchUsersSetRow;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.FriendService;
import com.ty.wetalk.utils.ResponseResult;
import com.ty.wetalk.utils.Utils;
import org.apache.ibatis.executor.ExecutorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ty.wetalk.sessions.UserSession.onlineUserSessions;

@CrossOrigin//允许跨域的请求
@RestController
@RequestMapping("/api/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @RequestMapping(value = "/getGroupsByUserAccount")
    public ResponseResult getGroupsByUserAccount(@RequestParam String userAccount) {
        ResponseResult responseResult = new ResponseResult();
        List<Group> groups = friendService.getGroupsByUserAccount(userAccount);
        for (Group group : groups) {
            List<User> friends = friendService.getFriendsByGroupId(group.getGroupId());
            int currentGroupOnlineNum = group.getOnlineNum();
            //检查当前好友是否在线
            for (User currentFriend : friends) {
                Session friendSession = onlineUserSessions.get(currentFriend.getAccount());
                currentFriend.setPassword(null);
                //如果在线,在线人数增加1，
                if (friendSession != null) {
                    currentFriend.setOnline(true);
                    currentGroupOnlineNum++;
                }
            }
            group.setOnlineNum(currentGroupOnlineNum);
            group.setUsers(friends);
        }
        responseResult.setTip("success");
        responseResult.setStatus("1");
        responseResult.setData(groups);
        return responseResult;
    }

    @RequestMapping("/searchUser")
    public ResponseResult searchFriend(String currentUserAccount, @RequestParam String kw) {
        ResponseResult responseResult = new ResponseResult();
        List<SearchUsersSetRow> searchUsersSetRows = friendService.searchFriend(currentUserAccount, kw);
        List<SearchUser> searchUsers = new ArrayList<SearchUser>();
        for (SearchUsersSetRow searchUsersSetRow : searchUsersSetRows) {
            SearchUser searchUser = new SearchUser();
            searchUser.setUser(new User(searchUsersSetRow.getAccount(), null, searchUsersSetRow.getNickName(), searchUsersSetRow.getHeaderImg(), searchUsersSetRow.getPhoneNum(), searchUsersSetRow.getEmail(), searchUsersSetRow.getGender(), searchUsersSetRow.getBirthday(), searchUsersSetRow.getAddress(), searchUsersSetRow.getIsVip(), searchUsersSetRow.getRegisterDate()));
            if (searchUsersSetRow.getFriendId() != 0 && currentUserAccount != null) {
                searchUser.setIsfriend(true);
            }
            searchUsers.add(searchUser);
        }
        responseResult.setData(searchUsers);
        responseResult.setStatus("1");
        responseResult.setTip("success");
        return responseResult;
    }

    /**
     * 发起添加好友请求
     *
     * @param activeUserId
     * @param passiveUserId
     * @return
     */
    @RequestMapping("/addFriend")
    public ResponseResult addFriend(String activeUserId, String passiveUserId) {
        ResponseResult responseResult = new ResponseResult();
        Friend friend = friendService.checkFriend(activeUserId, passiveUserId);
        if (friend == null) {
            int status = friendService.addFriend(activeUserId, passiveUserId, Utils.dateToString(new Date()));
            if (status != -1) {
                responseResult.setStatus("1");
                responseResult.setTip("添加成功！");
            }
        } else {
            responseResult.setStatus("0");
            responseResult.setTip("已是好友关系，不能重复添加！");
        }
        return responseResult;
    }


    /**
     * 同意添加好友
     *
     * @param activeUserId:同意的用户id
     * @param passiveUserId:添加的分组id
     * @param newGroupIdOfActive      :发起请求者需要添加好友到的分组id
     * @param newGroupIdOfPassiveUser :同意添加好友用户需要添加到分组的分组id
     * @return
     */
    @RequestMapping("/agreeAddFriend")
    public ResponseResult agreeAddFriend(@RequestParam String activeUserId, @RequestParam String passiveUserId, @RequestParam int newGroupIdOfPassiveUser, @RequestParam int newGroupIdOfActive) {
        ResponseResult responseResult = new ResponseResult();
        try {
            String agreeTime = Utils.dateToString(new Date());
            friendService.agreeAddFriend(activeUserId, passiveUserId, agreeTime);
            //因为是同意添加好友，所以创建分组的用户是被添加者，好友是主动发起请求的用户
            Integer oldGroupIdOfActiveUser = friendService.getOldGroupId(activeUserId, passiveUserId);
            Integer oldGroupIdOfPassiveUser = friendService.getOldGroupId(passiveUserId, activeUserId);

            //如果查询到旧分组id，说明在分组表中，已经添加过分组，则更新旧的分组id，并且更新移动时间，如果没有，则说明是新添加的好友，直接插入新的数据
            if (oldGroupIdOfPassiveUser != null) {
                //以防重复添加，增加冗余数据
                friendService.removeFriendToGroup(oldGroupIdOfPassiveUser, newGroupIdOfPassiveUser, activeUserId, agreeTime);
            } else {
                friendService.addFriendToGroup(activeUserId, newGroupIdOfPassiveUser, agreeTime);
            }
            if (oldGroupIdOfActiveUser != null) {
                //以防重复添加，增加冗余数据
                friendService.removeFriendToGroup(oldGroupIdOfActiveUser, newGroupIdOfActive, passiveUserId, agreeTime);
            } else {
                friendService.addFriendToGroup(passiveUserId, newGroupIdOfActive, agreeTime);
            }
            WebSocketServer webSocketServer = new WebSocketServer();
            webSocketServer.sendMessage(passiveUserId, activeUserId, "我们已经是好友啦，快来和我聊天吧！", "2");
            webSocketServer.sendMessage(passiveUserId, activeUserId, SystemMsgType.UPDATEGROUPLIST.toString(), "1");
            responseResult.setStatus("1");
            responseResult.setTip("success");
        } catch (Exception e) {
            responseResult.setStatus("0");
            responseResult.setTip("error");
        }
        return responseResult;
    }

    /**
     * 移动好友到分组
     *
     * @param passiveUserId
     * @param groupId
     * @return
     */
    @RequestMapping("/removeFriendToGroup")
    public ResponseResult removeFriendToGroup(@RequestParam String passiveUserId, @RequestParam int groupId) {
        ResponseResult responseResult = new ResponseResult();
        String agreeTime = Utils.dateToString(new Date());
        friendService.addFriendToGroup(passiveUserId, groupId, agreeTime);
        return responseResult;
    }

    @RequestMapping("/checkFriend")
    public ResponseResult checkFriend(@RequestParam String activeUserAccount, @RequestParam String passiveUserAccount) {
        ResponseResult responseResult = new ResponseResult();
        Friend friend = friendService.checkFriend(activeUserAccount, passiveUserAccount);
        if (friend != null) {
            responseResult.setStatus("1");
            responseResult.setTip("success");
            responseResult.setData(friend);
        } else {
            responseResult.setStatus("0");
            responseResult.setTip("该用户已在您好友列表中");
            responseResult.setData(null);
        }
        System.out.println("====================" + responseResult);
        return responseResult;
    }
}
