package com.gzsf.operation.cache;

import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.model.DbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DbInfoCache {
    @Autowired
    private DbInfoMapper dbInfoMapper;

    /**
     * 获取数据库信息
     * @param dbId
     * @return
     */
    @Cacheable(value = "dbInfo",key = "#dbId")
    public DbInfo getByDbInfoId(Long dbId){
        return dbInfoMapper.getByDbInfoId(dbId);
    }

    /**
     * 修改数据库信息
     * @param dbInfo
     * @return
     */
    @CachePut(value = "dbInfo",key = "#dbId")
    public DbInfo save(DbInfo dbInfo){
        if (dbInfo==null)return null;
        dbInfo.setUpdatedAt(new Date());
        if (dbInfo.getDbId()==null){
            dbInfo.setCreatedAt(new Date());
            dbInfo.setDbId(dbInfoMapper.insert(dbInfo));
        }else {
            dbInfoMapper.update(dbInfo);
        }
        return dbInfo;
    }

    /**
     * 删除数据库信息
     */
    @CacheEvict(value = "dbInfo",key = "#dbId")
    public void delete(Long id){
        dbInfoMapper.delete(id);
    }


    @Cacheable(value = "dbInfo",key = "#dbId")
    public DbInfo getRecord(Long dbId){
        return dbInfoMapper.getRecordById(dbId);
    }

}
