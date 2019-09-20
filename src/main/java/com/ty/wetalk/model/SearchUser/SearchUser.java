package com.ty.wetalk.model.SearchUser;

import com.ty.wetalk.model.User;
import lombok.Data;

import javax.sql.RowSet;

@Data
public class SearchUser {
    public User user;
    public boolean isfriend;
}
