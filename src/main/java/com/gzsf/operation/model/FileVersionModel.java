package com.gzsf.operation.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileVersionModel implements Serializable {
    private static final long serialVersionUID = 4881738910424625228L;
    private Long fileVersionId;
    private Long fileId;
    private Integer version;
    private String updateLog;
    private Long updatedBy;
    private Date createdAt;
    private String fileName;
}
