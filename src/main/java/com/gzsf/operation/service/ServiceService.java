package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.ServiceCache;
import com.gzsf.operation.dao.ServiceMapper;
import com.gzsf.operation.model.ServiceModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ServiceService extends MonoService {
    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private ServiceCache serviceCache;
    public Mono<Page> getList(int pageNum, int pageSize){
        return async(()->serviceMapper.getServices(pageNum, pageSize));
    }

    public Mono save(ServiceModel serviceModel){
        return async(()->serviceCache.save(serviceModel));
    }

    public Mono delete(Long id) {
        return async(() -> {
                    serviceCache.delete(id);
                    return true;
                }
        );
    }
}
