package com.gzsf.operation.model;

import lombok.Data;

import java.util.Date;

@Data
public class FileVersionModel {
    private Long fileVersionId;
    private Long fileId;
    private Integer version;
    private String updateLog;
    private Long updatedBy;
    private Date createdAt;
    private String fileName;
}
