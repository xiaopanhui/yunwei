package com.gzsf.operation.bean;

import com.gzsf.operation.model.DbInfo;
import lombok.Data;

@Data
public class DbInfoContent {
    private Long dbId;
    private String url;
    private String username;
    private String password;
    private Integer poolSize;
    private String description;
    private String name;
    private DbInfo dbInfo;
}
