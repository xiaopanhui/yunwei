package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.FileUtils;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.cache.ServiceCache;
import com.gzsf.operation.dao.ServiceMapper;
import com.gzsf.operation.exception.NoFileFoundException;
import com.gzsf.operation.exception.NoServiceFoundException;
import com.gzsf.operation.model.FileVersionModel;
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
    private FileCache fileCache;
    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ServiceCache serviceCache;
    @Autowired
    private ProcessService processService;
    public Mono<Page> getList(int pageNum, int pageSize, String keyword){
        return async(()->{
                    Page<ServiceModel> s= serviceMapper.getServices(pageNum, pageSize, keyword);
                    for (ServiceModel item:s){
                        item.setStatus(processService.getPid(item.getServiceId())!=0);
                    }
                    return s;
                }
               );
    }

    public Mono save(ServiceModel serviceModel){
        return async(()->serviceCache.save(serviceModel));
    }

    public Mono delete(Long id) {
        return async(() -> {
                    if(processService.getPid(id)>0){
                     processService.stopService(id);
                    }
                    serviceCache.delete(id);
                    return true;
                }
        );
    }

    }

