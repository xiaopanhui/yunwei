package com.gzsf.operation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class DbInfo implements Serializable {
    private Long dbId;
    private String name;
    private String url;
    private String description;
    private Integer poolSize;
    private String password;
    private String userName;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long createdBy;
}
