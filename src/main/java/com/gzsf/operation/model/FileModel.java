package com.gzsf.operation.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileModel implements Serializable {
    private Long fileId;
    private String fileName;
    private String description;
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;
}
