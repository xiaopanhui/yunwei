package com.gzsf.operation.model;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceModel {
    private Long serviceId;
    private Long fileId;
    private String serviceName;
    private Integer fileVersion;
    private Type type;
    private String startCmd;
    private String stopCmd;
    private String description;
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;


    private String fileName;

    private int status = 0;
    private int pid = 100 ;
    enum Type {
        OTHER,JAR, EXE,PY,SHELL,
    }
}

