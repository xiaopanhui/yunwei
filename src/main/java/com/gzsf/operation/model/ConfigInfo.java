package com.gzsf.operation.model;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;
@Data
public class ConfigInfo implements Serializable {
    private static final long serialVersionUID = -68764300382360878L;
    
    private Long configId;
    //服务id
    private Long serviceId;
    //配置名称
    private String name;
    //数据库连接信息
    private Long dbId;
    //表名
    private String tableName;
    
    private Date createdAt;
    
    private Date updatedAt;
    
//    private Boolean isDel;
    
    private Long createdBy;

    private String description;

    

}