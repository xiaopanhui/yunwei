package com.gzsf.operation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 4365549038773729219L;
    private Long userId;
    @NotBlank
    private String userName;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    @NotBlank
    private Role role;
    private String description;
    private Long createdBy;

    public enum Role{
        SYSTEM,ADMIN,USER,READONLY
    }
}
