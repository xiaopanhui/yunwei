package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.dao.LogMapper;
import com.gzsf.operation.model.LogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LogService extends MonoService {

    @Autowired
    private LogCache logCache;
    @Autowired
    private LogMapper logMapper;

    public Mono<LogModel> save(LogModel model){
        return async(()->logCache.save(model));
    }

    public Mono<Page> getList(Integer limit,
                              Integer offset, String keyword){
        return async(()->logMapper.getList(offset,limit,keyword));
    }

    public Mono<Boolean> delete(Long id){
        return async(()->logCache.delete(id));
    }

}
