package com.gzsf.operation.cache;

import com.gzsf.operation.dao.ServiceMapper;
import com.gzsf.operation.model.ServiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServiceCache {
    @Autowired
    private ServiceMapper serviceMapper;
    public ServiceModel save(ServiceModel serviceModel){
        serviceModel.setUpdatedAt(new Date());
        if (serviceModel.getServiceId()==null){
            serviceModel.setCreatedAt(new Date());
            serviceMapper.insert(serviceModel);
        }else {
            serviceMapper.update(serviceModel);
        }
        return serviceModel;
    }
    @CachePut(value = "service",key = "#serviceModel.serviceId")
    public ServiceModel updateCache(ServiceModel serviceModel){
        return serviceModel;
    }

    @CacheEvict(value = "service",key = "#id")
    public void delete(Long id){
        serviceMapper.delete(id);
    }

    @Cacheable(value = "service",key = "#serviceId")
    public ServiceModel getRecord(Long serviceId){
        return serviceMapper.getRecord(serviceId);
    }
}
