/**
 * 关于好友的控制器
 */
package com.ty.wetalk.controller;

import com.ty.wetalk.model.Group;
import com.ty.wetalk.model.User;
import com.ty.wetalk.service.FriendService;
import com.ty.wetalk.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
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

}
