package com.gzsf.operation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private Long userId;
    private String userName;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private Role role;
    private String description;
    private Long createdBy;

    public enum Role{
        SYSTEM,ADMIN,USER,READONLY
    }
}
