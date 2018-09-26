package com.gzsf.operation.bean;

import com.gzsf.operation.model.User;
import lombok.Data;

import java.util.Date;
@Data
public class LoginInfo {
    private Long usrId;
    private String userName;
    private Date loginAt;
    private User user;
}
