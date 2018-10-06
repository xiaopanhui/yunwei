package com.gzsf.operation.bean;

import com.gzsf.operation.model.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoginInfo {
    private Long userId;
    private String userName;
    private Date loginAt;
    private User user;
    private  User.Role role;
}
