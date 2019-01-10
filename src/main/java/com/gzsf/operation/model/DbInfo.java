package com.gzsf.operation.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class DbInfo implements Serializable {
    private Long dbId;
    @NotBlank
    private String name;
    @NotBlank
    private String url;
    private String description;
    private Integer poolSize;
    private String password;
    private String username;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDel;
    private Long createdBy;
}
