package com.gzsf.operation.bean;

import com.gzsf.operation.model.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoginInfo {
    private Long usrId;
    private String userName;
    private Date loginAt;
    private User user;
    //添加登录的角色
    private  User.Role role;
    private Date createdAt;
    private Date updatedAt;


}
