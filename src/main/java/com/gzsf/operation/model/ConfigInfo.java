package com.gzsf.operation.model;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;
@Data
public class ConfigInfo implements Serializable {
    private static final long serialVersionUID = -68764300382360878L;
    
    private Integer configId;
    //服务id
    private Integer serviceId;
    //名称
    private String name;
    //数据库连接信息
    private Integer dbId;
    //表名
    private String tableName;
    
    private Date createdAt;
    
    private Date updatedAt;
    
    private Boolean isDel;
    
    private Integer createdBy;

    

}