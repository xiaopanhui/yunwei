package com.gzsf.operation.model;

import lombok.Data;

@Data
public class LogItem {
    private String key;
    private Type type;
    private String name;

    public enum Type{
        TEXT,DATE, IAMGE_BASE64, IMAGE_URL
    }

    public void setType(String value){
        try {
            type= Type.valueOf(value);
        }catch (Exception e){
            type=Type.TEXT;
        }
    }
}
