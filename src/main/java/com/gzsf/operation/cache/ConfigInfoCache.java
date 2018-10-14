package com.gzsf.operation.cache;

import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class ConfigInfoCache {
    @Autowired
    private ConfigInfoDao configInfoDao;

    /**
     * 获取
     */
    @Cacheable(value = "configInfo" ,key = "#{configId}")
    public ConfigInfo getByConfigInfoId(Integer configId){
        return configInfoDao.getByConfigInfoId(configId);

    }

    /*插入*/
    @CacheEvict(value = "configInfo",key = "#{configId}")
    public Integer insert(ConfigInfo configInfo){
        configInfo.setUpdatedAt(new Date());
     if (configInfo.getConfigId()==null){
         configInfo.setCreatedAt(new Date());
         configInfoDao.insert(configInfo);
         return configInfo.getConfigId();
     }else {
         configInfoDao.update(configInfo);
         return configInfo.getConfigId();
     }
    }


    /*删除*/
    @CacheEvict
    public  Integer deleteByConfigInfoId(Integer id){
       return configInfoDao.deleteByConfigInfoId(id);
    }


}
