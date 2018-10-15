package com.gzsf.operation.cache;

import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.model.DbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DbInfoCache {
    @Autowired
    private DbInfoMapper dbInfoMapper;

    @CacheEvict(value = "dbInfo",key = "#{dbInfo.dbId}")
    public Long save(DbInfo dbInfo){
        dbInfo.setUpdatedAt(new Date());
        if (dbInfo.getDbId()==null){
            dbInfo.setCreatedAt(new Date());
            return dbInfoMapper.insert(dbInfo);
        }else {
            dbInfoMapper.update(dbInfo);
            return dbInfo.getDbId();
        }
    }

    @Cacheable(value = "dbInfo",key = "#{dbId}")
    public DbInfo getDbInfoById(Long dbId) {
        return dbInfoMapper.getRecordById(dbId);
    }

    @CacheEvict(value = "DbInfo",key = "#{dbId}")
    public boolean delete (Long dbId) {
        return dbInfoMapper.delete(dbId) == 1;
    }

}
