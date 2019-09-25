/**
 * 关于好友的所有接口
 */
package com.ty.wetalk.mapper;

import com.ty.wetalk.model.Friend;
import com.ty.wetalk.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface GroupMapper {

    public List<User> getUsersByGroupAccount(@RequestParam String groupAccount);

}


