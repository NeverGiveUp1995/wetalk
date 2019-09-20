/**
 * 关于好友的控制器
 */
package com.ty.wetalk.controller;

import com.ty.wetalk.model.Friend;
import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.SearchUser.SearchUser;
import com.ty.wetalk.model.SearchUser.SearchUsersSetRow;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.FriendService;
import com.ty.wetalk.utils.ResponseResult;
import com.ty.wetalk.utils.Utils;
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
@RequestMapping("/friend")
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
            responseResult.setMsgType("2");
            responseResult.setData(null);
        }
        System.out.println("====================" + responseResult);
        return responseResult;
    }
}
