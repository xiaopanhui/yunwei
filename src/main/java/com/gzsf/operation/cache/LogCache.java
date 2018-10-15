package com.gzsf.operation.cache;

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

    @CacheEvict(value = "log",key = "#id")
    public boolean delete(Long id){
        return logMapper.delete(id)==1;
    }

    @Cacheable(value = "log",key = "#id")
    public LogModel getRecord(Long id){
        return logMapper.getRecordById(id);
    }
}
