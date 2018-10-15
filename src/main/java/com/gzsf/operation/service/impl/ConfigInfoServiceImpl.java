package com.gzsf.operation.service.impl;

import com.gzsf.operation.cache.ConfigInfoCache;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.service.ConfigInfoService;
import com.gzsf.operation.service.MonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class ConfigInfoServiceImpl extends MonoService implements ConfigInfoService {
    @Autowired
    private ConfigInfoDao configInfoDao;
    @Autowired
    private ConfigInfoCache configInfoCache;

    @Override
    public  Mono<ConfigInfo> getByConfigInfoId(Integer configId) {
        return async(()->{
            if( configInfoCache.getByConfigInfoId(configId)==null){
                throw new NoUserFoundException();}
                return   configInfoCache.getByConfigInfoId(configId);
        });
    }
    /**
     * 新增数据
     */
    @Override
    public Mono<ConfigInfo> insert(ConfigInfo configInfo) {
        return async(() -> {
            configInfo.setIsDel(false);
        return  configInfoCache.getByConfigInfoId(configInfoCache.insert(configInfo));
        });

    }
    /*
    *修改数据
    */

    @Override
    public   Mono<ConfigInfo> update(Integer id,ConfigInfo configInfo) {
        return async(()->{
            ConfigInfo  configInfo1= configInfoCache.getByConfigInfoId(id);
            configInfo1.setName(configInfo.getName());
            configInfo1.setTableName(configInfo.getTableName());
            configInfo1.setDbId(configInfo.getDbId());
            configInfo1.setServiceId(configInfo.getServiceId());
            configInfo.setIsDel(false);
            configInfo.setCreatedBy(1);
            configInfoCache.insert(configInfo);
           return configInfo;

        });
    }
}