package com.gzsf.operation.model;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

@Data
public class ConnectionConfig implements Serializable {
    private static final long serialVersionUID = 828148347266875053L;
    
    private Integer connectionId;
    
    private Integer configId;
    
    private String keyword;
    
    private String description;
    
    private Date createdAt;



}