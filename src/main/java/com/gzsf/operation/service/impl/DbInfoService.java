package com.gzsf.operation.service.impl;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.model.DbInfo;
import com.gzsf.operation.service.MonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DbInfoService extends MonoService {
    @Autowired
    private DbInfoMapper dbInfoMapper;
    @Autowired
    private DbInfoCache dbInfoCache;

    public Mono<Page> getDbIfoList(Integer limit, Integer offset, String keyword) {
        return async(()-> dbInfoMapper.getList(offset,limit,keyword));
    }

    public Mono<DbInfo> saveDbInfo(DbInfo dbInfo) {
        dbInfo.setCreatedBy(null);
        return async(()->dbInfoCache.save(dbInfo)).map(dbInfoCache::getDbInfoById);
    }

    public Mono<Boolean> delete(Long dbId) {
        return async(()->dbInfoCache.delete(dbId));
    }



}
//import com.gzsf.operation.dao.DbInfoMapper;
//import com.gzsf.operation.model.DbInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DbInfoService {
//    @Autowired
//    private DbInfoMapper dbInfoMapper;
//
//    public List<DbInfo> findByName(String name) {
//        return dbInfoMapper.findDbInfoByName(name);
//    }
//
//    public DbInfo insertDbInfo(DbInfo dbInfo) {
//        dbInfoMapper.insertDbInfo(dbInfo);
//        return dbInfo;
//    }
//
//    public List<DbInfo> ListDbInfo() {
//        return dbInfoMapper.ListDbInfo();
//    }
//
//    public int UpDate(DbInfo dbInfo) {
//        return dbInfoMapper.Update(dbInfo);
//    }
//
//    public int delete(int dbId) {
//        return dbInfoMapper.delete(dbId);
//    }
//
//}
