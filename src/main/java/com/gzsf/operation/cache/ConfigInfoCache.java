package com.gzsf.operation.cache;

import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    @Cacheable(value = "configInfo" ,key = "#configId")
    public ConfigInfo getByConfigInfoId(Long configId){
        return configInfoDao.getByConfigInfoId(configId);

    }

    /*插入*/
    @CacheEvict(value = "configInfo",key = "#configInfo.configId")
    public Long insert(ConfigInfo configInfo){
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

    @CachePut(value = "config_fields",key = "#id")
    public String updateLogItems(String fields, Long id){
        configInfoDao.updateFields(fields,id);
        return fields;
    }

    @Cacheable(value = "config_fields",key = "#id")
    public String getFields(Long id){
        String fields= configInfoDao.getFields(id);
        return fields;

    }


    /*删除*/
    @CacheEvict(value = "configInfo",key = "#configId")
    public  Integer deleteByConfigInfoId(Integer configId){
       return configInfoDao.deleteByConfigInfoId(configId);
    }


}
