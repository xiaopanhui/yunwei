package com.gzsf.operation.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ServiceModel  implements Serializable {
    private static final long serialVersionUID = 5120224501613728810L;
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

    private boolean status = false;
    private int pid = 100 ;
    public enum Type {
        OTHER,JAR, EXE,PY2,PY3,SHELL,
    }
}

