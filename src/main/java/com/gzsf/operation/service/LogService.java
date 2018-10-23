package com.gzsf.operation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.gzsf.operation.Utils;
import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.dao.LogMapper;
import com.gzsf.operation.model.LogItem;
import com.gzsf.operation.model.LogItems;
import com.gzsf.operation.model.LogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class LogService extends MonoService {

    @Autowired
    private LogCache logCache;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private ObjectMapper mapper;

    public Mono<LogModel> save(LogModel model){
        return async(()->logCache.save(model));
    }

    public Mono<Page> getList(Integer pageNum,
                              Integer pageSize, String keyword){
        return async(()->logMapper.getList(pageNum,pageSize,keyword));
    }

    public Mono<Boolean> delete(Long id){
        return async(()->logCache.delete(id));
    }

    public Mono<List<LogItem>> getLogFields(Long logId){
        return async(()-> {
            String fiels= logCache.getFields(logId);
            return Utils.StringToLogItems(fiels);
        });
    }

    public Mono<Boolean> updateLogFields(String fields, Long logId){
        return async(()-> {
             logCache.updateLogItems(fields,logId);
             return true;
        });
    }


}
