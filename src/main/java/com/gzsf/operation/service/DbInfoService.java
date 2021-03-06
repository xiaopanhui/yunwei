package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.exception.DbInfoAlreadyExistException;
import com.gzsf.operation.exception.NoDbInfoFoundException;
import com.gzsf.operation.model.DbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class DbInfoService extends MonoService {
    @Autowired
    private DbInfoMapper dbInfoMapper;
    @Autowired
    private DbInfoCache dbInfoCache;

    public Mono<DbInfo> getByDbInfoId(Long dbId) {
        return async(()->{
            DbInfo dbInfo=dbInfoCache.getByDbInfoId(dbId);
            if(dbInfo==null){
                throw new NoDbInfoFoundException();
            }
            return dbInfo;
        });
    }

    public Mono<Page> getList( Integer pageNum,
                               Integer pageSize, String keyword) {
        return async(()-> dbInfoMapper.getList(pageNum,pageSize,keyword));
    }

    public Mono<DbInfo> update(Long id,DbInfo dbInfo) {
        return async(()->{
            DbInfo dbInfo1= dbInfoCache.getByDbInfoId(id);
            if (dbInfo1==null){
                throw new NoDbInfoFoundException();
            }
            dbInfo.setUpdatedAt(new Date());
            dbInfoMapper.update(dbInfo);
            return dbInfo;
        });
    }

    public Mono add(DbInfo dbInfo) {
        return async(()->{
            DbInfo dbInfo1 = dbInfoMapper.getByDbInfoName(dbInfo.getName());
            if (dbInfo1 != null) {
                throw new DbInfoAlreadyExistException();
            }
            dbInfo.setCreatedAt(new Date());
            dbInfo.setUpdatedAt(new Date());
            dbInfoMapper.insert(dbInfo);
            return dbInfo;
        });
    }

    public Mono delete(Long id){
        return async(()->{
            dbInfoCache.delete(id);
            return true;
        });
    }
}
