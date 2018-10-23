package com.gzsf.operation.model;

import com.gzsf.operation.Utils;
import lombok.Data;

import java.util.Map;

@Data
public class FieldItem {
    private String key;
    private Type type;
    private String name;
    private boolean details;
    public enum Type{
        KEY,JSON,TEXT,DATE, IAMGE_BASE64, IMAGE_URL
    }

    public void setType(String value){
        try {
            type= Type.valueOf(value);
        }catch (Exception e){
            type=Type.TEXT;
        }
    }

    public static FieldItem parseMap(Map<String,Object> data){
        FieldItem item=new FieldItem();
        item.setName(data.getOrDefault("name","").toString());
        item.setKey(data.getOrDefault("key","").toString());
        item.setType((data.getOrDefault("type","TEXT")).toString());
        item.setDetails(Boolean.valueOf(data.getOrDefault("details",false).toString()));
        if (!Utils.isEmpty(item.getName()) && !Utils.isEmpty(item.getKey())){
            return null;
        }else {
            return item;
        }
    }
}
