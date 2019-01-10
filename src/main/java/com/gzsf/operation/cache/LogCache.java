package com.gzsf.operation.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.dao.LogMapper;
import com.gzsf.operation.model.LogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogCache {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private ObjectMapper mapper;
    @CachePut(value = "log",key = "#model.logId")
    public LogModel save(LogModel model){
        if (model==null)return null;
        model.setUpdatedAt(new Date());
        if (model.getLogId()==null){
            model.setCreatedAt(new Date());
            model.setLogId(logMapper.insert(model));
        }else {
            logMapper.update(model);
        }
        return model;
    }

    @CachePut(value = "log",key = "#model.logId")
    public LogModel updateCache(LogModel model){
        return model;
    }

    @CacheEvict(value = "log",key = "#id")
    public boolean delete(Long id){
        return logMapper.delete(id)==1;
    }

    @Cacheable(value = "log",key = "#id")
    public LogModel getRecord(Long id){
        return logMapper.getRecordById(id);
    }

    @CachePut(value = "log_fields",key = "#id")
    public String updateLogItems(String fields, Long id){
         logMapper.updateFields(fields,id);
         return fields;
    }

    @Cacheable(value = "log_fields",key = "#id")
    public String getFields(Long id){
        String fields= logMapper.getFields(id);
       return fields;

    }
}
