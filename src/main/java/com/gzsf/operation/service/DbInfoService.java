package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.exception.DbInfoAlreadyExist;
import com.gzsf.operation.exception.NoDbInfoFoundException;
import com.gzsf.operation.model.DbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.util.Date;

@Service
public class DbInfoService extends MonoService {
    @Autowired
    private DbInfoMapper dbInfoMapper;
    @Autowired
    private DbInfoCache dbInfoCache;

    public Mono<DbInfo> getByDbInfoId(Long dbId) {
        return async(()->{
            if( dbInfoCache.getByDbInfoId(dbId)==null){
                throw new NoDbInfoFoundException();
            }
            return dbInfoCache.getByDbInfoId(dbId);
        });
    }

    public Mono<Page> getList(Integer limit, Integer offset, String keyword) {
        return async(()-> dbInfoMapper.getList(offset,limit,keyword));
    }

//    public Mono<DbInfo> save(DbInfo dbInfo){
//        return async(()->dbInfoCache.save(dbInfo));
//    }

    /**
     * 修改
     * @param id
     * @param dbInfo
     * @return
     */
    public Mono<DbInfo> update(Long id,DbInfo dbInfo) {
        return async(()->{
            DbInfo dbInfo1= dbInfoCache.getByDbInfoId(id);
            if (dbInfo1==null){
                throw new NoDbInfoFoundException();
            }
            dbInfo1.setName(dbInfo.getName());
            dbInfo1.setUrl(dbInfo.getUrl());
            dbInfo1.setUserName(dbInfo1.getUserName());
            dbInfo1.setPassword(dbInfo.getPassword());
            dbInfo1.setPoolSize(dbInfo.getPoolSize());
            dbInfo1.setDescription(dbInfo.getDescription());
            dbInfo.setUpdatedAt(new Date());
            dbInfoMapper.update(dbInfo);
            return dbInfo;
        });
    }

    /**
     * 添加
     * @param
     * @return
     */
    public Mono add(DbInfo dbInfo) {
        return async(()->{
            DbInfo dbInfo1 = dbInfoMapper.getByDbInfoName(dbInfo.getName());
            if (dbInfo1 != null) {
                throw new DbInfoAlreadyExist();
            }
            dbInfo1.setUrl(dbInfo.getUrl());
            dbInfo1.setUserName(dbInfo.getUserName());
            dbInfo1.setPassword(dbInfo.getPassword());
            dbInfo1.setPoolSize(dbInfo.getPoolSize());
            dbInfo1.setDescription(dbInfo.getDescription());
            dbInfo1.setName(dbInfo.getName());
            dbInfo.setCreatedAt(new Date());
            dbInfoMapper.insert(dbInfo);
            return dbInfo;
        });
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Mono delete(Long id){
        return async(()->{
            dbInfoCache.delete(id);
            return true;
        });
    }

}
