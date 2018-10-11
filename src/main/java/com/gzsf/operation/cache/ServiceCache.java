package com.gzsf.operation.cache;

import com.gzsf.operation.dao.ServiceMapper;
import com.gzsf.operation.model.ServiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServiceCache {
    @Autowired
    private ServiceMapper serviceMapper;
    @CacheEvict(value = "service",key = "#{serviceModel.serviceId}")
    public int save(ServiceModel serviceModel){
        serviceModel.setUpdatedAt(new Date());
        if (serviceModel.getServiceId()==null){
            serviceModel.setCreatedAt(new Date());
            return serviceMapper.insert(serviceModel);
        }else {
            return serviceMapper.update(serviceModel);
        }
    }

    @CacheEvict(value = "service",key = "#{serviceModel.serviceId}")
    public void delete(Long id){
        serviceMapper.delete(id);
    }

    @Cacheable(value = "service",key = "#{serviceId}")
    public ServiceModel getRecord(Long serviceId){
        return serviceMapper.getRecord(serviceId);
    }
}
