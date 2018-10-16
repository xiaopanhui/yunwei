package com.gzsf.operation.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class LogModel implements Serializable {
    private static final long serialVersionUID = 4782113877151241951L;
    private Long logId;
    private String logPath;
    @NotBlank
    private String name;
    private Long dbId;
    private String timeField;
    private String description;
    private String sql;
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;
}
